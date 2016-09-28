package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.brandings.NewServiceBrandingBean;
import com.t1t.digipolis.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.digipolis.apim.beans.brandings.ServiceBrandingSummaryBean;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.BrandingFacade;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.rest.resources.IBrandingResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Api(value = "/brandings", description = "The Branding API.")
@Path("/brandings")
@ApplicationScoped
public class BrandingResource implements IBrandingResource {

    @Inject
    private OrganizationFacade orgFacade;
    @Inject
    private BrandingFacade brandingFacade;
    @Inject
    private ISecurityContext security;

    @Override
    @ApiOperation("Get service branding")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBrandingSummaryBean.class, message = "Service Branding")
    })
    @GET
    @Path("/services/{brandingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceBrandingBean getServiceBranding(@PathParam("brandingId") String id) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(id));
        return brandingFacade.getServiceBranding(id);
    }

    @Override
    @ApiOperation("Create service branding")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBrandingSummaryBean.class, message = "Service Branding")
    })
    @POST
    @Path("/services")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceBrandingBean createNewServiceBranding(NewServiceBrandingBean branding) throws NotAuthorizedException {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkNotNull(branding);
        Preconditions.checkArgument(StringUtils.isNotEmpty(branding.getName()));
        return brandingFacade.createServiceBranding(branding);
    }

    @Override
    @ApiOperation("Delete service branding")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Service Branding deleted")
    })
    @DELETE
    @Path("/services/{brandingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteServiceBranding(@PathParam("brandingId") String id) throws NotAuthorizedException {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(id));
        brandingFacade.deleteServiceBranding(id);
    }

    @Override
    @ApiOperation("Retrieve available service brandings")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceBrandingSummaryBean.class, message = "Service Brandings")
    })
    @GET
    @Path("/services")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<ServiceBrandingBean> getAllServiceBrandings() {
        return brandingFacade.getAllServiceBrandings();
    }
}