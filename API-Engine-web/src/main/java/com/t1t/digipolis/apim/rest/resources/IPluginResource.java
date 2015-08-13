package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.plugins.NewPluginBean;
import com.t1t.digipolis.apim.beans.plugins.PluginBean;
import com.t1t.digipolis.apim.beans.summary.PluginSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.digipolis.apim.rest.resources.exceptions.PluginAlreadyExistsException;
import com.t1t.digipolis.apim.rest.resources.exceptions.PluginNotFoundException;
import com.t1t.digipolis.apim.rest.resources.exceptions.PluginResourceNotFoundException;
import com.t1t.digipolis.apim.rest.resources.exceptions.PolicyDefinitionNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The Plugin API.
 */
@Path("plugins")
public interface IPluginResource {

    /**
     * This endpoint returns a list of all plugins that have been added to the
     * system.
     * @summary List All Plugins
     * @statuscode 200 If the list of plugins is successfully returned.
     * @return A list of plugins.
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PluginSummaryBean> list() throws NotAuthorizedException;

    /**
     * Use this endpoint to add a plugin to apiman.  A plugin consists of the maven
     * coordinates of an artifact deployed to a remote maven repository (e.g. maven
     * central).
     * @summary Add a Plugin
     * @servicetag admin
     * @param bean The plugin to add.
     * @statuscode 200 If the plugin was added successfully.
     * @return Full details about the plugin that was added.
     * @throws PluginAlreadyExistsException when attempting to create a plugin that 
     * already exists
     * @throws PluginNotFoundException when specified plugin not found
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PluginBean create(NewPluginBean bean) throws PluginAlreadyExistsException, PluginNotFoundException, NotAuthorizedException;
    
    /**
     * This endpoint can be used to access the full information about an apiman
     * plugin.  The plugin is retrieved using the ID it was given when it was 
     * added.  The ID information can be retrieved by listing all plugins or 
     * remembered when a plugin is first added.
     * @summary Get Plugin by ID
     * @servicetag admin
     * @param pluginId the plugin id
     * @statuscode 200 If the plugin exists and is returned.
     * @return An apiman plugin.
     * @throws PluginNotFoundException when specified plugin not found
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    @GET
    @Path("{pluginId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PluginBean get(@PathParam("pluginId") Long pluginId) throws PluginNotFoundException, NotAuthorizedException;

    /**
     * Call this endpoint to delete a plugin.
     * @summary Delete a Plugin by ID
     * @servicetag admin
     * @statuscode 204 If the plugin was deleted successfully.
     * @param pluginId the plugin id The plugin's ID.
     * @throws PluginNotFoundException when specified plugin not found
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    @DELETE
    @Path("{pluginId}")
    public void delete(@PathParam("pluginId") Long pluginId)
            throws PluginNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all policy definitions contributed by the plugin.
     * @summary Get Plugin Policy Definitions
     * @param pluginId the plugin id The plugin ID.
     * @statuscode 200 If the list of policy definitions is returned successfully.
     * @statuscode 404 If the plugin does not exist.
     * @return A list of policy definitions.
     * @throws PluginNotFoundException when specified plugin not found
     */
    @GET
    @Path("{pluginId}/policyDefs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicyDefinitionSummaryBean> getPolicyDefs(@PathParam("pluginId") Long pluginId)
            throws PluginNotFoundException;
    
    /**
     * Use this endpoint to retrieve the form associated with a particular policy
     * definition.  Plugins may contribute policy definitions to apiman.  Part of that
     * contribution *may* include a form for the UI to display when configuring an 
     * instance of the policy.  This endpoint returns this form.
     * @summary Get Plugin Policy Form
     * @param pluginId the plugin id The plugin ID.
     * @param policyDefId The policy definition ID.
     * @statuscode 200 If the form is returned successfully.
     * @statuscode 404 If the plugin does not exist.
     * @statuscode 404 If the policy definition does not exist.
     * @statuscode 404 If the form does not exist.
     * @return A policy configuration form.
     * @throws PluginNotFoundException when specified plugin not found
     * @throws PolicyDefinitionNotFoundException when trying to get, update, or delete 
     * a policy definition that does not exist
     * @throws PluginResourceNotFoundException when plugin resource not found
     */
    @GET
    @Path("{pluginId}/policyDefs/{policyDefId}/form")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPolicyForm(@PathParam("pluginId") Long pluginId,
                                @PathParam("policyDefId") String policyDefId) throws PluginNotFoundException,
            PolicyDefinitionNotFoundException, PluginResourceNotFoundException;

}
