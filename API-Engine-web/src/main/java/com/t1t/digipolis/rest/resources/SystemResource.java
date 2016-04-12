package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.apim.beans.summary.ServiceVersionAvailabilityBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.config.Version;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.InvalidServiceStatusException;
import com.t1t.digipolis.apim.exceptions.ServiceVersionNotFoundException;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.facades.SystemFacade;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
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
import javax.ws.rs.core.Response;
import java.util.List;

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
    @Inject private SystemFacade systemFacade;
    @ApiOperation(value = "Get System Status",
            notes = "This endpoint simply returns the status of the apiman system. This is a useful endpoint to use when testing a client's connection to the apiman API Manager REST services.")
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

    @ApiOperation(value = "Get general whitelist records",
                  notes = "Use this endpoint to retrieve the general whitelist records. Those will be default applied for exposed services which are not visible on any API Marketplace.")
    @ApiResponses({@ApiResponse(code = 200,responseContainer = "List", response = WhitelistBean.class, message = "Default whitelist records.")})
    @GET
    @Path("/whitelist/records")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WhitelistBean> getWhitelistRecords() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        return systemFacade.getWhitelistRecords();
    }

    @ApiOperation(value = "Get general blacklist records",
                  notes = "Use this endpoint to retrieve the general blacklist records. Those will be default applied for exposed services which are not visible on any API Marketplace.")
    @ApiResponses({@ApiResponse(code = 200,responseContainer = "List", response = BlacklistBean.class, message = "Default blacklist records.")})
    @GET
    @Path("/blacklist/records")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BlacklistBean> getBlacklistRecords() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        return systemFacade.getBlacklistRecords();
    }
}
