package com.t1t.apim.facades;

import com.t1t.apim.beans.BeanUtils;
import com.t1t.apim.beans.plugins.NewPluginBean;
import com.t1t.apim.beans.plugins.PluginBean;
import com.t1t.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.apim.beans.summary.PluginSummaryBean;
import com.t1t.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.apim.beans.summary.PolicyFormType;
import com.t1t.apim.common.plugin.Plugin;
import com.t1t.apim.common.plugin.PluginClassLoader;
import com.t1t.apim.common.plugin.PluginCoordinates;
import com.t1t.apim.core.IPluginRegistry;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.InvalidPluginException;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.AbstractRestException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.PluginNotFoundException;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.security.ISecurityContext;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PluginFacade {
    private static final Logger log = LoggerFactory.getLogger(PluginFacade.class.getName());
    @Inject private ISecurityContext securityContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private IPluginRegistry pluginRegistry;

    private static final ObjectMapper mapper = new ObjectMapper();

    public List<PluginSummaryBean> list(){
        try {
            return query.listPlugins();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public PluginBean create(NewPluginBean bean){
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
            log.info(String.format("Created plugin mvn:%s:%s:%s", pluginBean.getGroupId(), pluginBean.getArtifactId(),  //$NON-NLS-1$
                    pluginBean.getVersion()));
            log.info(String.format("\tCreated %s policy definitions from plugin.", String.valueOf(policyDefCounter))); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        return pluginBean;
    }

    public PluginBean get(Long pluginId){
        try {
            PluginBean bean = storage.getPlugin(pluginId);
            if (bean == null) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void delete(Long pluginId){
        try {
            PluginBean pbean = storage.getPlugin(pluginId);
            if (pbean == null) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            storage.deletePlugin(pbean);
            log.info(String.format("Deleted plugin mvn:%s:%s:%s", pbean.getGroupId(), pbean.getArtifactId(),  //$NON-NLS-1$
                    pbean.getVersion()));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public List<PolicyDefinitionSummaryBean> getPolicyDefs(Long pluginId){
        get(pluginId);
        try {
            return query.listPluginPolicyDefs(pluginId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public String getPolicyForm(Long pluginId,String policyDefId){
        PluginBean pbean = null;
        PolicyDefinitionBean pdBean = null;
        try {
            pbean = storage.getPlugin(pluginId);
            if (pbean == null) {
                throw ExceptionFactory.pluginNotFoundException(pluginId);
            }
            pdBean = storage.getPolicyDefinition(policyDefId);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
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
}
