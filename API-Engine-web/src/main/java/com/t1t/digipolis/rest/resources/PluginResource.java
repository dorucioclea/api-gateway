package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.plugins.NewPluginBean;
import com.t1t.digipolis.apim.beans.plugins.PluginBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.summary.PluginSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicyFormType;
import com.t1t.digipolis.apim.common.plugin.Plugin;
import com.t1t.digipolis.apim.common.plugin.PluginClassLoader;
import com.t1t.digipolis.apim.common.plugin.PluginCoordinates;
import com.t1t.digipolis.apim.core.IPluginRegistry;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.InvalidPluginException;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.logging.ApimanLogger;
import com.t1t.digipolis.apim.core.logging.IApimanLogger;
import com.t1t.digipolis.apim.rest.impl.i18n.Messages;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IPluginResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.rest.resources.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Api(value = "/plugins", description = "The Plugin API.")
@Path("/plugins")
@ApplicationScoped
public class PluginResource implements IPluginResource {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IPluginRegistry pluginRegistry;

    @Inject
    @ApimanLogger(PluginResource.class)
    IApimanLogger log;

    /**
     * Constructor.
     */
    public PluginResource() {
    }

    @ApiOperation(value = "List All Plugins",
            notes = "This endpoint returns a list of all plugins that have been added to the")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PluginSummaryBean.class, message = "A list of plugins.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PluginSummaryBean> list() throws NotAuthorizedException {
        try {
            return query.listPlugins();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Add a Plugin",
            notes = "Use this endpoint to add a plugin to apiman.  A plugin consists of the maven coordinates of an artifact deployed to a remote maven repository (e.g. maven central).")
    @ApiResponses({
            @ApiResponse(code = 200, response = PluginBean.class, message = "Full details about the plugin that was added.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PluginBean create(NewPluginBean bean) throws PluginAlreadyExistsException, PluginNotFoundException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();

        PluginCoordinates coordinates = new PluginCoordinates(bean.getGroupId(), bean.getArtifactId(), bean.getVersion(),
                bean.getClassifier(), bean.getType());
        Plugin plugin = null;
        try {
            plugin = pluginRegistry.loadPlugin(coordinates);
            bean.setName(plugin.getName());
            bean.setDescription(plugin.getDescription());
        } catch (InvalidPluginException e) {
            throw new PluginNotFoundException(coordinates.toString(), e);
        }

        PluginBean pluginBean = new PluginBean();
        pluginBean.setGroupId(bean.getGroupId());
        pluginBean.setArtifactId(bean.getArtifactId());
        pluginBean.setVersion(bean.getVersion());
        pluginBean.setClassifier(bean.getClassifier());
        pluginBean.setType(bean.getType());
        pluginBean.setName(bean.getName());
        pluginBean.setDescription(bean.getDescription());
        pluginBean.setCreatedBy(securityContext.getCurrentUser());
        pluginBean.setCreatedOn(new Date());
        try {
            storage.beginTx();
            if (storage.getPlugin(bean.getGroupId(), bean.getArtifactId()) != null) {
                throw ExceptionFactory.pluginAlreadyExistsException();
            }

            storage.createPlugin(pluginBean);

            // Process any contributed policy definitions.
            List<URL> policyDefs = plugin.getPolicyDefinitions();
            int policyDefCounter = 0;
            for (URL url : policyDefs) {
                PolicyDefinitionBean policyDef = (PolicyDefinitionBean) mapper.reader(PolicyDefinitionBean.class).readValue(url);
                if (policyDef.getId() == null || policyDef.getId().trim().isEmpty()) {
                    throw ExceptionFactory.policyDefInvalidException(Messages.i18n.format("PluginResourceImpl.MissingPolicyDefId", policyDef.getName())); //$NON-NLS-1$
                }
                policyDef.setPluginId(pluginBean.getId());
                if (policyDef.getId() == null) {
                    policyDef.setId(BeanUtils.idFromName(policyDef.getName()));
                } else {
                    policyDef.setId(BeanUtils.idFromName(policyDef.getId()));
                }
                if (policyDef.getFormType() == null) {
                    policyDef.setFormType(PolicyFormType.Default);
                }
                if (storage.getPolicyDefinition(policyDef.getId()) == null) {
                    storage.createPolicyDefinition(policyDef);
                    policyDefCounter++;
                }
            }

            storage.commitTx();
            log.info(String.format("Created plugin mvn:%s:%s:%s", pluginBean.getGroupId(), pluginBean.getArtifactId(),  //$NON-NLS-1$
                    pluginBean.getVersion()));
            log.info(String.format("\tCreated %s policy definitions from plugin.", String.valueOf(policyDefCounter))); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
        return pluginBean;
    }

    @ApiOperation(value = "Get Plugin by ID",
            notes = "This endpoint can be used to access the full information about an apiman plugin.  The plugin is retrieved using the ID it was given when it was added.  The ID information can be retrieved by listing all plugins or remembered when a plugin is first added.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PluginBean.class, message = "An api-engine plugin.")
    })
    @GET
    @Path("/{pluginId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PluginBean get(@PathParam("pluginId") Long pluginId) throws PluginNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            PluginBean bean = storage.getPlugin(pluginId);
            if (bean == null) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            storage.commitTx();
            return bean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Delete a Plugin by ID",
            notes = "Call this endpoint to remove a plugin.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{pluginId}")
    public void delete(@PathParam("pluginId") Long pluginId) throws PluginNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            PluginBean pbean = storage.getPlugin(pluginId);
            if (pbean == null) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            storage.deletePlugin(pbean);
            storage.commitTx();
            log.info(String.format("Deleted plugin mvn:%s:%s:%s", pbean.getGroupId(), pbean.getArtifactId(),  //$NON-NLS-1$
                    pbean.getVersion()));
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get Plugin Policy Definitions",
            notes = "Use this endpoint to get a list of all policy definitions contributed by the plugin.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PolicyDefinitionSummaryBean.class, message = "A list of policy definitions.")
    })
    @GET
    @Path("/{pluginId}/policyDefs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicyDefinitionSummaryBean> getPolicyDefs(@PathParam("pluginId") Long pluginId) throws PluginNotFoundException {
        get(pluginId);
        try {
            return query.listPluginPolicyDefs(pluginId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get Plugin Policy Form",
            notes = "Use this endpoint to retrieve the form associated with a particular policy definition.  Plugins may contribute policy definitions to apiman.  Part of that contribution *may* include a form for the UI to display when configuring an instance of the policy.  This endpoint returns this form.")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "A policy configuration form.")
    })
    @GET
    @Path("/{pluginId}/policyDefs/{policyDefId}/form")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPolicyForm(@PathParam("pluginId") Long pluginId, @PathParam("policyDefId") String policyDefId) throws PluginNotFoundException,
            PluginResourceNotFoundException, PolicyDefinitionNotFoundException {
        PluginBean pbean = null;
        PolicyDefinitionBean pdBean = null;
        try {
            storage.beginTx();
            pbean = storage.getPlugin(pluginId);
            if (pbean == null) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            pdBean = storage.getPolicyDefinition(policyDefId);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
        PluginCoordinates coordinates = new PluginCoordinates(pbean.getGroupId(), pbean.getArtifactId(),
                pbean.getVersion(), pbean.getClassifier(), pbean.getType());
        try {
            if (pdBean == null) {
                throw ExceptionFactory.policyDefNotFoundException(policyDefId);
            }
            if (pdBean.getPluginId() == null || !pdBean.getPluginId().equals(pbean.getId())) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            if (pdBean.getFormType() == PolicyFormType.JsonSchema && pdBean.getForm() != null) {
                String formPath = pdBean.getForm();
                if (!formPath.startsWith("/")) { //$NON-NLS-1$
                    formPath = "META-INF/apiman/policyDefs/" + formPath; //$NON-NLS-1$
                } else {
                    formPath = formPath.substring(1);
                }
                Plugin plugin = pluginRegistry.loadPlugin(coordinates);
                PluginClassLoader loader = plugin.getLoader();
                InputStream resource = null;
                try {
                    resource = loader.getResourceAsStream(formPath);
                    if (resource == null) {
                        throw ExceptionFactory.pluginResourceNotFoundException(formPath, coordinates);
                    }
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(resource, writer);
                    return writer.toString();
                } finally {
                    IOUtils.closeQuietly(resource);
                }
            } else {
                throw ExceptionFactory.pluginResourceNotFoundException(null, coordinates);
            }
        } catch (AbstractRestException e) {
            throw e;
        } catch (Throwable t) {
            throw new SystemErrorException(t);
        }
    }

    /**
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    /**
     * @return the securityContext
     */
    public ISecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(ISecurityContext securityContext) {
        this.securityContext = securityContext;
    }

}
