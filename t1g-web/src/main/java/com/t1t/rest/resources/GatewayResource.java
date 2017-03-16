package com.t1t.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.dto.GatewayDtoBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.NewGatewayBean;
import com.t1t.apim.beans.gateways.UpdateGatewayBean;
import com.t1t.apim.beans.summary.GatewaySummaryBean;
import com.t1t.apim.beans.summary.GatewayTestResultBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.i18n.Messages;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.GatewayAlreadyExistsException;
import com.t1t.apim.exceptions.GatewayNotFoundException;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.facades.GatewayFacade;
import com.t1t.apim.gateway.IGatewayLinkFactory;
import com.t1t.apim.rest.resources.IGatewayResource;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.util.DtoFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/gateways", description = "The Gateway API.")
@Path("/gateways")
@ApplicationScoped
public class GatewayResource implements IGatewayResource {

    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;
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
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New gateway"));
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
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New gateway"));
        return gatewayFacade.create(bean);
    }

    @ApiOperation(value = "Get a Gateway by ID",
            notes = "Call this endpoint to get the details of a single configured Gateway.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayDtoBean.class, message = "The Gateway identified by {gatewayId}.")
    })
    @GET
    @Path("/{gatewayId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GatewayDtoBean get(@PathParam("gatewayId") String gatewayId) throws GatewayNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId), Messages.i18n.format("emptyValue", "Gateway ID"));
        GatewayDtoBean rval = DtoFactory.createGatewayDtoBean(gatewayFacade.get(gatewayId));
        if (!securityContext.isAdmin()) {
            rval.setConfiguration(null);
            rval.setJWTPrivKey(null);
        }
        return rval;
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
        Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId), Messages.i18n.format("emptyValue", "Gateway ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Updated gateway"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId), Messages.i18n.format("emptyValue", "Gateway ID"));
        gatewayFacade.remove(gatewayId);
    }
}
