package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.config.Version;
import com.t1t.digipolis.apim.core.IStorage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SystemResource implements ISystemResource {

    @Inject
    private IStorage storage;
    @Inject
    AppConfig config;
    @Inject
    private Version version;
    private static Logger log = LoggerFactory.getLogger(SystemResource.class.getName());
    @ApiOperation(value = "Get System Status",
            notes = "This endpoint simply returns the status of the apiman system. This is a useful endpoint to use when testing a client's connection to the apiman API Manager REST services.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusBean.class, message = "System status information")
    })
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public SystemStatusBean getStatus() {
        SystemStatusBean rval = new SystemStatusBean();
        rval.setId("apim-manager-api"); //$NON-NLS-1$
        rval.setName("API Manager REST API - Authorization endpoint"); //$NON-NLS-1$
        rval.setDescription("The API Manager REST API AUTH endpoint is used for 3rd Party application to implement their own authentication/authorization."); //$NON-NLS-1$
        rval.setMoreInfo("http://www.trust1team.com"); //$NON-NLS-1$
        rval.setEnvironment(config.getEnvironment());
        rval.setVersion(config.getVersion());
        rval.setUp(storage != null);
        return rval;
    }
}
