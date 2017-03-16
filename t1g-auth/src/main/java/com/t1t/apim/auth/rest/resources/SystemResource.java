package com.t1t.apim.auth.rest.resources;

import com.t1t.apim.beans.system.SystemStatusBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.facades.SystemFacade;
import com.t1t.apim.gateway.GatewayAuthenticationException;
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
    @Inject private SystemFacade systemFacade;

    @ApiOperation(value = "Get System Status",
            notes = "This endpoint simply returns the status of the api engine system. This is a useful endpoint to use when testing a client's connection to the API Manager REST services.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusBean.class, message = "System status information")
    })
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public SystemStatusBean getStatus() throws GatewayAuthenticationException, StorageException {
        return systemFacade.getStatus();
    }
}
