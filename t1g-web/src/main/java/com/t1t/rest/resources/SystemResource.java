package com.t1t.rest.resources;

import com.t1t.apim.beans.summary.ServiceVersionAvailabilityBean;
import com.t1t.apim.beans.system.SystemStatusBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.GatewayNotFoundException;
import com.t1t.apim.exceptions.InvalidServiceStatusException;
import com.t1t.apim.exceptions.ServiceVersionNotFoundException;
import com.t1t.apim.facades.SystemFacade;
import com.t1t.apim.exceptions.GatewayAuthenticationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Implementation of the System API.
 */
@Api(value = "/system", description = "Test endpoint. Can be used to validate the url endpoint.")
@Path("/system")
@ApplicationScoped
public class SystemResource {

    @Inject
    private SystemFacade systemFacade;

    @ApiOperation(value = "Get System Status",
            notes = "This endpoint simply returns the status of the api engine system. This is a useful endpoint to use when testing a client's connection to the API Manager REST services.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusBean.class, message = "System status information")
    })
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public SystemStatusBean getStatus() throws GatewayAuthenticationException, StorageException {
        SystemStatusBean rval = systemFacade.getStatus();
        return rval;
    }

    @ApiOperation(value = "Get Service Availabilities",
            notes = "Use this endpoint to get information about the available marketplaces that are defined on the API.")
    @ApiResponses({@ApiResponse(code = 200, response = ServiceVersionAvailabilityBean.class, message = "Available API marketplaces information.")})
    @GET
    @Path("/marketplaces")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionAvailabilityBean getAvailabeMarkets() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        ServiceVersionAvailabilityBean svab = new ServiceVersionAvailabilityBean();
        svab.setAvailableMarketplaces(systemFacade.getAvailableMarketplaces());
        return svab;
    }
}
