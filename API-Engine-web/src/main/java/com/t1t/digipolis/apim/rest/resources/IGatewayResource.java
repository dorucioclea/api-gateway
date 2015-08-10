package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.NewGatewayBean;
import com.t1t.digipolis.apim.beans.gateways.UpdateGatewayBean;
import com.t1t.digipolis.apim.beans.summary.GatewaySummaryBean;
import com.t1t.digipolis.apim.beans.summary.GatewayTestResultBean;
import com.t1t.digipolis.apim.rest.resources.exceptions.GatewayAlreadyExistsException;
import com.t1t.digipolis.apim.rest.resources.exceptions.GatewayNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The Gateway API.
 */
@Path("/gateways")
public interface IGatewayResource {
    
    /**
     * This endpoint is used to test the Gateway's settings prior to either creating 
     * or updating it.  The information will be used to attempt to create a link between
     * the API Manager and the Gateway, by simply trying to ping the Gateway's "status" 
     * endpoint.
     * @summary Test a Gateway
     * @servicetag admin
     * @param bean Details of the Gateway for testing.
     * @statuscode 200 If the test is performed (regardless of the outcome of the test).
     * @return The result of testing the Gateway settings.
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do 
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GatewayTestResultBean test(NewGatewayBean bean) throws NotAuthorizedException;

    /**
     * This endpoint returns a list of all the Gateways that have been configured.
     * @summary List All Gateways
     * @statuscode 200 If the gateways are successfully returned.
     * @return A list of configured Gateways.
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GatewaySummaryBean> list() throws NotAuthorizedException;

    /**
     * This endpoint is called to create a new Gateway.
     * @summary Create a Gateway
     * @servicetag admin
     * @param bean The details of the new Gateway.
     * @statuscode 200 If the Gateway is created successfully.
     * @return The newly created Gateway.
     * @throws GatewayAlreadyExistsException when the gateway already exists
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GatewayBean create(NewGatewayBean bean) throws GatewayAlreadyExistsException, NotAuthorizedException;
    
    /**
     * Call this endpoint to get the details of a single configured Gateway.
     * @summary Get a Gateway by ID
     * @param gatewayId The ID of the Gateway to get.
     * @statuscode If the Gateway is returned successfully.
     * @return The Gateway identified by {gatewayId}
     * @throws GatewayNotFoundException when gateway is not found
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     */
    @GET
    @Path("/{gatewayId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GatewayBean get(@PathParam("gatewayId") String gatewayId) throws GatewayNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update an existing Gateway.  Note that the name of the
     * Gateway cannot be changed, as the name is tied closely with the Gateway's
     * ID.  If you wish to rename the Gateway you must delete it and create a new
     * one.
     * @summary Update a Gateway
     * @servicetag admin
     * @param gatewayId The ID of the Gateway to update.
     * @param bean The Gateway information to update.  All fields are optional.
     * @statuscode 204 If the update is successful.
     * @throws GatewayNotFoundException when gateway is not found
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     */
    @PUT
    @Path("/{gatewayId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("gatewayId") String gatewayId, UpdateGatewayBean bean)
            throws GatewayNotFoundException, NotAuthorizedException;

    /**
     * This endpoint deletes a Gateway by its unique ID.
     * @summary Delete a Gateway
     * @servicetag admin
     * @param gatewayId The ID of the Gateway to delete.
     * @statuscode 204 If the delete is successful.
     * @throws GatewayNotFoundException when gateway is not found
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     */
    @DELETE
    @Path("/{gatewayId}")
    public void delete(@PathParam("gatewayId") String gatewayId)
            throws GatewayNotFoundException, NotAuthorizedException;

}
