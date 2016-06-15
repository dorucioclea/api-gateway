package com.t1t.digipolis.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServicePlanSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceVersionEndpointSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceVersionVisibilityBean;
import com.t1t.digipolis.apim.beans.support.*;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
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

/**
 * This is the rest endpoint implementation.
 * Responsabilities:
 * <ul>
 * <li>REST documentation (swagger based)</li>
 * <li>Security checks (verify role of current user)</li>
 * <li>Exception handling (with exceptionmapper)</li>
 * <li>Parameter validation</li>
 * <li>Facade client</li>
 * </ul>
 * <p>
 * In the facade (stateless bean) a container managed transaction strategy will be used.
 */
@Api(value = "/organizations", description = "The Organization API.")
@Path("/organizations")
@ApplicationScoped
public class OrganizationResource {

    @Inject
    private OrganizationFacade orgFacade;

    @ApiOperation(value = "Get a support ticket for a service",
            notes = "Use this endpoint to retrieve a ticket for a service within an organization, based on the ticket id.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SupportBean.class, message = "Updated support ticket")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/support/{supportId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportBean getServiceSupportTicket(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("supportId") String supportId) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        return orgFacade.getServiceSupportTicket(organizationId, serviceId, Long.parseLong(supportId.trim(), 10));
    }

    @ApiOperation(value = "Get Service Version",
            notes = "Use this endpoint to get detailed information about a single version of a Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceVersionBean.class, message = "A Service version.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionBean getServiceVersion(@PathParam("organizationId") String organizationId,
                                                @PathParam("serviceId") String serviceId,
                                                @PathParam("version") String version) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersion(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Retrieve all announcement for given service.",
            notes = "Use this endpoint to retrieve all announcement.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = AnnouncementBean.class, message = "List of announcements.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/announcement/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<AnnouncementBean> getServiceAnnouncements(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException{
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.getServiceAnnouncements(organizationId, serviceId);
    }

    @ApiOperation(value = "Get Service Endpoint",
            notes = "Use this endpoint to get information about the Managed Service's gateway endpoint.  In other words, this returns the actual live endpoint on the API Gateway - the endpoint that a client should use when invoking the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceVersionEndpointSummaryBean.class, message = "The live Service endpoint information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/endpoint")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionEndpointSummaryBean getServiceVersionEndpointInfo(@PathParam("organizationId") String organizationId,
                                                                           @PathParam("serviceId") String serviceId,
                                                                           @PathParam("version") String version) throws ServiceVersionNotFoundException,
            InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionEndpointInfo(organizationId, serviceId, version);
    }

    @ApiOperation(value = "List Service Contracts",
            notes = "Use this endpoint to get a list of all Contracts created with this Service.  This will return Contracts created by between any Application and through any Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ContractSummaryBean.class, message = "A list of Contracts.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/contracts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContractSummaryBean> getServiceVersionContracts(@PathParam("organizationId") String organizationId,
                                                                @PathParam("serviceId") String serviceId,
                                                                @PathParam("version") String version,
                                                                @QueryParam("page") int page,
                                                                @QueryParam("count") int pageSize) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionContracts(organizationId, serviceId, version, page, pageSize);
    }

    @ApiOperation(value = "Get Plan Policy",
            notes = "Use this endpoint to get information about a single Policy in the Plan version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full information about the Policy.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/versions/{version}/policies/{policyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean getPlanPolicy(@PathParam("organizationId") String organizationId,
                                    @PathParam("planId") String planId,
                                    @PathParam("version") String version,
                                    @PathParam("policyId") long policyId)
            throws OrganizationNotFoundException, PlanVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getPlanPolicy(organizationId, planId, version, policyId);
    }

    @ApiOperation(value = "List Service Plans",
            notes = "Use this endpoint to list the Plans configured for the given Service version.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServicePlanSummaryBean.class, message = "A list of Service plans.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plans")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServicePlanSummaryBean> getServiceVersionPlans(@PathParam("organizationId") String organizationId,
                                                               @PathParam("serviceId") String serviceId,
                                                               @PathParam("version") String version) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionPlans(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Get Service Plugins",
            notes = "Use this endpoint to get information about the plugins that are applied on this service.")
    @ApiResponses({@ApiResponse(code = 200, response = KongPluginConfigList.class, message = "Available API plugin information.")})
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plugins")
    @Produces(MediaType.APPLICATION_JSON)
    public KongPluginConfigList getServiceVersionPluginInfo(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,
                                                            @PathParam("version") String version) throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        KongPluginConfigList servicePlugins = orgFacade.getServicePlugins(organizationId, serviceId, version);
        return servicePlugins;
    }

    @ApiOperation(value = "Get Service Availabilities",
            notes = "Use this endpoint to get information about the available marketplaces that are defined on the API.")
    @ApiResponses({@ApiResponse(code = 200, response = ServiceVersionVisibilityBean.class, message = "Available API marketplaces information.")})
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/availability")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionVisibilityBean getServiceVersionAvailabilityInfo(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,
                                                                          @PathParam("version") String version) throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        ServiceVersionVisibilityBean svab = new ServiceVersionVisibilityBean();
        //TODO do this for a service
        svab.setAvailableMarketplaces(orgFacade.getServiceVersionAvailabilityInfo(organizationId, serviceId, version));
        return svab;
    }

    @ApiOperation(value = "Retrieve a list of all support tickets for a service.",
            notes = "Use this endpoint to retrieve a list of all support tickets for a service.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = SupportBean.class, message = "Service support tickets")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/support")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SupportBean> listServiceSupportTickets(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.listServiceSupportTickets(organizationId, serviceId);
    }
}