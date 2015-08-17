package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.UpdatePolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.PolicyDefinitionAlreadyExistsException;
import com.t1t.digipolis.apim.exceptions.PolicyDefinitionNotFoundException;
import com.t1t.digipolis.apim.facades.PolicyDefinitionFacade;
import com.t1t.digipolis.apim.rest.resources.IPolicyDefinitionResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/policyDefs", description = "The Policy Definition API.")
@Path("/policyDefs")
@ApplicationScoped
public class PolicyDefinitionResource implements IPolicyDefinitionResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    ISecurityContext securityContext;
    @Inject
    @APIEngineContext
    Logger log;
    @Inject
    private PolicyDefinitionFacade policyDefinitionFacade;

    /**
     * Constructor.
     */
    public PolicyDefinitionResource() {
    }

    @ApiOperation(value = "List Policy Definitions",
            notes = "This endpoint returns a list of all policy definitions that have been added.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PolicyDefinitionSummaryBean.class, message = "A list of policy definitions.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicyDefinitionSummaryBean> list() throws NotAuthorizedException {
        return policyDefinitionFacade.list();
    }

    @ApiOperation(value = "Add Policy Definition",
            notes = "Use this endpoint to add a policy definition.  The policy definition can optionall include the 'id' property.  If no 'id' is supplied, one will be generated based on the name.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyDefinitionBean.class, message = "Details about the policy definition that was added.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyDefinitionBean create(PolicyDefinitionBean bean) throws PolicyDefinitionAlreadyExistsException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(bean);
        return policyDefinitionFacade.create(bean);
    }

    @ApiOperation(value = "Get Policy Definition by ID",
            notes = "Use this endpoint to get a single policy definition by its ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyDefinitionBean.class, message = "A policy definition if found.")
    })
    @GET
    @Path("/{policyDefinitionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyDefinitionBean get(@PathParam("policyDefinitionId") String policyDefinitionId) throws PolicyDefinitionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(policyDefinitionId));
        return policyDefinitionFacade.get(policyDefinitionId);
    }

    @ApiOperation(value = "Update Policy Definition",
            notes = "Update the meta information about a policy definition.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{policyDefinitionId}")
    public void update(@PathParam("policyDefinitionId") String policyDefinitionId, UpdatePolicyDefinitionBean bean)
            throws PolicyDefinitionNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(policyDefinitionId));
        Preconditions.checkNotNull(bean);
        policyDefinitionFacade.update(policyDefinitionId, bean);
    }

    @ApiOperation(value = "Delete policy definition.",
            notes = "Use this endpoint to remove a policy definition by its ID.  If the policy definition was added automatically from an installed plugin, this will fail.  The only way to remove such policy definitions is to remove the plugin.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{policyDefinitionId}")
    public void delete(@PathParam("policyDefinitionId") String policyDefinitionId) throws PolicyDefinitionNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(policyDefinitionId));
        policyDefinitionFacade.delete(policyDefinitionId);
    }
}
