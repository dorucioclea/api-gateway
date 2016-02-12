package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.summary.ServiceVersionAvailabilityBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.config.Version;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.InvalidServiceStatusException;
import com.t1t.digipolis.apim.exceptions.ServiceVersionNotFoundException;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.rest.resources.ISystemResource;
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
public class SystemResource implements ISystemResource {

    @Inject private IStorage storage;
    @Inject private AppConfig config;
    @Inject private Version version;
    @Inject private OrganizationFacade orgFacade;
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
        rval.setName("API Manager REST API"); //$NON-NLS-1$
        rval.setDescription("The API Manager REST API is used by the API Manager UI to get stuff done.  You can use it to automate any api task you wish.  For example, create new Organizations, Plans, Applications, and Services."); //$NON-NLS-1$
        rval.setMoreInfo("http://www.trust1team.com"); //$NON-NLS-1$
        rval.setEnvironment(config.getEnvironment());
        rval.setBuiltOn(config.getBuildDate());
        rval.setVersion(config.getVersion());
        rval.setUp(storage != null);
        return rval;
    }

    @ApiOperation(value = "Get Service Availabilities",
                  notes = "Use this endpoint to get information about the available marketplaces that are defined on the API.")
    @ApiResponses({@ApiResponse(code = 200, response = ServiceVersionAvailabilityBean.class, message = "Available API marketplaces information.")})
    @GET
    @Path("/marketplaces")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionAvailabilityBean getServiceVersionAvailabilityInfo() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        ServiceVersionAvailabilityBean svab = new ServiceVersionAvailabilityBean();
        svab.setAvailableMarketplaces(orgFacade.getAvailableMarketplaces());
        return svab;
    }
}
