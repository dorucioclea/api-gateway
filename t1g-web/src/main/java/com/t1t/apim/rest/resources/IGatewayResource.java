package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.dto.GatewayDtoBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.NewGatewayBean;
import com.t1t.apim.beans.gateways.UpdateGatewayBean;
import com.t1t.apim.beans.summary.GatewaySummaryBean;
import com.t1t.apim.beans.summary.GatewayTestResultBean;
import com.t1t.apim.exceptions.GatewayAlreadyExistsException;
import com.t1t.apim.exceptions.GatewayNotFoundException;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

/**
 * The Gateway API.
 */
public interface IGatewayResource {

    /**
     * This endpoint is used to test the Gateway's settings prior to either creating
     * or updating it.  The information will be used to attempt to create a link between
     * the API Manager and the Gateway, by simply trying to ping the Gateway's "status"
     * endpoint.
     *
     * @param bean Details of the Gateway for testing.
     * @return The result of testing the Gateway settings.
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     * @summary Test a Gateway
     * @servicetag admin
     * @statuscode 200 If the test is performed (regardless of the outcome of the test).
     */
    public GatewayTestResultBean test(NewGatewayBean bean) throws NotAuthorizedException;

    /**
     * This endpoint returns a list of all the Gateways that have been configured.
     *
     * @return A list of configured Gateways.
     * @throws NotAuthorizedException when attempt to do something user is not authorized to do
     * @summary List All Gateways
     * @statuscode 200 If the gateways are successfully returned.
     */
    public List<GatewaySummaryBean> list() throws NotAuthorizedException;

    /**
     * This endpoint is called to create a new Gateway.
     *
     * @param bean The details of the new Gateway.
     * @return The newly created Gateway.
     * @throws GatewayAlreadyExistsException when the gateway already exists
     * @throws NotAuthorizedException        when attempt to do something user is not authorized to do
     * @summary Create a Gateway
     * @servicetag admin
     * @statuscode 200 If the Gateway is created successfully.
     */
    public GatewayBean create(NewGatewayBean bean) throws GatewayAlreadyExistsException, NotAuthorizedException;

    /**
     * Call this endpoint to get the details of a single configured Gateway.
     *
     * @param gatewayId The ID of the Gateway to get.
     * @return The Gateway identified by {gatewayId}
     * @throws GatewayNotFoundException when gateway is not found
     * @throws NotAuthorizedException   when attempt to do something user is not authorized to do
     * @summary Get a Gateway by ID
     * @statuscode If the Gateway is returned successfully.
     */
    public GatewayDtoBean get(String gatewayId) throws GatewayNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update an existing Gateway.  Note that the name of the
     * Gateway cannot be changed, as the name is tied closely with the Gateway's
     * ID.  If you wish to rename the Gateway you must remove it and create a new
     * one.
     *
     * @param gatewayId The ID of the Gateway to update.
     * @param bean      The Gateway information to update.  All fields are optional.
     * @throws GatewayNotFoundException when gateway is not found
     * @throws NotAuthorizedException   when attempt to do something user is not authorized to do
     * @summary Update a Gateway
     * @servicetag admin
     * @statuscode 204 If the update is successful.
     */
    public void update(String gatewayId, UpdateGatewayBean bean)
            throws GatewayNotFoundException, NotAuthorizedException;

    /**
     * This endpoint deletes a Gateway by its unique ID.
     *
     * @param gatewayId The ID of the Gateway to remove.
     * @throws GatewayNotFoundException when gateway is not found
     * @throws NotAuthorizedException   when attempt to do something user is not authorized to do
     * @summary Delete a Gateway
     * @servicetag admin
     * @statuscode 204 If the remove is successful.
     */
    public void remove(String gatewayId)
            throws GatewayNotFoundException, NotAuthorizedException;

}
