package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.facades.CurrentUserFacade;
import com.t1t.digipolis.apim.rest.resources.ICurrentUserResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the Current User API.
 */
@Api(value = "/currentuser", description = "The Current User API. Returns information about the authenticated")
@Path("/currentuser")
@ApplicationScoped
public class CurrentUserResource implements ICurrentUserResource {

    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private IStorageQuery query;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    @APIEngineContext
    private Logger log;
    @Inject private CurrentUserFacade currentUserFacade;

    /**
     * Constructor.
     */
    public CurrentUserResource() {
    }

    @ApiOperation(value = "Get Current User Information",
            notes = "Use this endpoint to get information about the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(code = 200, response = CurrentUserBean.class, message = "Information about the authenticated user.")
    })
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public CurrentUserBean getInfo() {
        return currentUserFacade.getInfo();
    }

    @ApiOperation(value = "Update Current User Information",
            notes = "This endpoint allows updating information about the authenticated user.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/info")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateInfo(UpdateUserBean info) {
        Preconditions.checkNotNull(info);
        currentUserFacade.updateInfo(info);
    }

    @ApiOperation(value = "Get Organizations (app-edit)",
            notes = "This endpoint returns a list of all the organizations for which the current user has permission to edit applications.  For example, when creating a new Application, the user interface must ask the user to choose within which Organization to create it.  This endpoint lists the valid choices for the current user.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationSummaryBean.class, message = "A list of organizations.")
    })
    @GET
    @Path("/apporgs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationSummaryBean> getAppOrganizations() {
        return currentUserFacade.getAppOrganizations();
    }

    @ApiOperation(value = "Get Organizations (plan-edit)",
            notes = "This endpoint returns a list of all the organizations for which the current user has permission to edit plans.  For example, when creating a new Plan, the user interface must ask the user to choose within which Organization to create it.  This endpoint lists the valid choices for the current user.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationSummaryBean.class, message = "A list of organizations.")
    })
    @GET
    @Path("/planorgs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationSummaryBean> getPlanOrganizations() {
        return currentUserFacade.getPlanOrganizations();
    }

    @ApiOperation(value = "Get Organizations (svc-edit)",
            notes = "This endpoint returns a list of all the organizations for which the current user has permission to edit services.  For example, when creating a new Service, the user interface must ask the user to choose within which Organization to create it.  This endpoint lists the valid choices for the current user.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationSummaryBean.class, message = "A list of organizations.")
    })
    @GET
    @Path("/svcorgs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationSummaryBean> getServiceOrganizations() {
        return currentUserFacade.getServiceOrganizations();
    }

    @ApiOperation(value = "Get Current User's Applications",
            notes = "Use this endpoint to list all of the Applications the current user has permission to edit.  This includes all Applications from all Organizations the user has application edit privileges for.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusBean.class, message = "A list of Applications.")
    })
    @GET
    @Path("/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationSummaryBean> getApplications() {
        return currentUserFacade.getApplications();
    }

    @ApiOperation(value = "Get Current User's Services",
            notes = "Use this endpoint to list all of the Services the current user has permission to edit.  This includes all Services from all Organizations the user has service edit privileges for.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceSummaryBean.class, message = "A list of Services.")
    })
    @GET
    @Path("/services")
    public List<ServiceSummaryBean> getServices() {
        return currentUserFacade.getServices();
    }
}
