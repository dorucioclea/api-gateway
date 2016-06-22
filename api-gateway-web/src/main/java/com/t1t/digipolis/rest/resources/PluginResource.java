package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.plugins.NewPluginBean;
import com.t1t.digipolis.apim.beans.plugins.PluginBean;
import com.t1t.digipolis.apim.beans.summary.PluginSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.digipolis.apim.core.IPluginRegistry;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.PluginFacade;
import com.t1t.digipolis.apim.rest.resources.IPluginResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/plugins", description = "The Plugin API.")
@Path("/plugins")
@ApplicationScoped
public class PluginResource implements IPluginResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IPluginRegistry pluginRegistry;
    @Inject
    private PluginFacade pluginFacade;

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
        return pluginFacade.list();
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
        Preconditions.checkNotNull(bean);
        return pluginFacade.create(bean);
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
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(pluginId);
        return pluginFacade.get(pluginId);
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
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(pluginId);
        pluginFacade.delete(pluginId);
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
        Preconditions.checkNotNull(pluginId);
        return pluginFacade.getPolicyDefs(pluginId);
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
        return pluginFacade.getPolicyForm(pluginId, policyDefId);
    }
}
