package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.gateways.*;
import com.t1t.digipolis.apim.beans.summary.GatewaySummaryBean;
import com.t1t.digipolis.apim.beans.summary.GatewayTestResultBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.GatewayFacade;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IGatewayResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Api(value = "/gateways", description = "The Gateway API.")
@Path("/gateways")
@ApplicationScoped
public class GatewayResource implements IGatewayResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IGatewayLinkFactory gatewayLinkFactory;
    @Inject private GatewayFacade gatewayFacade;

    /**
     * Constructor.
     */
    public GatewayResource() {
    }

    @ApiOperation(value = "Test a Gateway",
            notes = "This endpoint is used to test the Gateway his settings prior to either creating or updating it.  The information will be used to attempt to create a link between the API Manager and the Gateway, by simply trying to ping the Gateway his status endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayTestResultBean.class, message = "The result of testing the Gateway settings.")
    })
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GatewayTestResultBean test(NewGatewayBean bean) throws NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(bean);
        return gatewayFacade.test(bean);
    }

    @ApiOperation(value = "List All Gateways",
            notes = "This endpoint returns a list of all the Gateways that have been configured.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = GatewaySummaryBean.class, message = "A list of configured Gateways.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GatewaySummaryBean> list() throws NotAuthorizedException {
        return gatewayFacade.list();
    }

    @ApiOperation(value = "Create a Gateway",
            notes = "This endpoint is called to create a new Gateway.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayBean.class, message = "The newly created Gateway.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GatewayBean create(NewGatewayBean bean) throws GatewayAlreadyExistsException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(bean);
        return gatewayFacade.create(bean);
    }

    @ApiOperation(value = "Get a Gateway by ID",
            notes = "Call this endpoint to get the details of a single configured Gateway.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayBean.class, message = "The Gateway identified by {gatewayId}.")
    })
    @GET
    @Path("/{gatewayId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GatewayBean get(@PathParam("gatewayId") String gatewayId) throws GatewayNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
        return gatewayFacade.get(gatewayId);
    }

    @ApiOperation(value = "Update a Gateway",
            notes = "Use this endpoint to update an existing Gateway.  Note that the name of the Gateway cannot be changed, as the name is tied closely with the Gateway his ID.  If you wish to rename the Gateway you must remove it and create a new one.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{gatewayId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("gatewayId") String gatewayId, UpdateGatewayBean bean) throws GatewayNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
        Preconditions.checkNotNull(bean);
        gatewayFacade.update(gatewayId,bean);
    }

    @ApiOperation(value = "Delete a Gateway",
            notes = "This endpoint deletes a Gateway by its unique ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{gatewayId}")
    public void remove(@PathParam("gatewayId") String gatewayId) throws GatewayNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
        gatewayFacade.remove(gatewayId);
    }
}
