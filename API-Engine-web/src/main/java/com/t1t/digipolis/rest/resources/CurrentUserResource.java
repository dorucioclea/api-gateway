package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.events.EventAggregateBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.CurrentUserFacade;
import com.t1t.digipolis.apim.facades.EventFacade;
import com.t1t.digipolis.apim.rest.resources.ICurrentUserResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
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
import java.util.Set;

/**
 * Implementation of the Current User API.
 */
@Api(value = "/currentuser", description = "The Current User API. Returns information about the authenticated")
@Path("/currentuser")
@ApplicationScoped
public class CurrentUserResource implements ICurrentUserResource {
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private CurrentUserFacade currentUserFacade;
    @Inject
    private EventFacade eventFacade;

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
        if (info.getPic() != null) {
            Preconditions.checkArgument(info.getPic().getBytes().length <= 150_000, "Logo should not be greater than 100k");
        }
        if (info.getBio() != null) {
            Preconditions.checkArgument(info.getBio().length() <= 100_000, "Bio should not exceed 1,00,000 characters: " + info.getBio().length());
        }
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceSummaryBean> getServices() {
        return currentUserFacade.getServices();
    }

    @Override
    @ApiOperation(value = "Get all incoming events for current user",
            notes = "Call this endpoint to get all incoming events for the current user")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of incoming events for current user")
    })
    @GET
    @Path("/notifications/incoming")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventBean> getCurrentUserAllIncomingEvents() {
        return eventFacade.getCurrentUserAllIncomingEvents();
    }

    @Override
    @ApiOperation(value = "Get all outgoing events for current user",
            notes = "Call this endpoint to get all outgoing events for the current user")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of outgoing events for current user")
    })
    @GET
    @Path("/notifications/outgoing")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventBean> getCurrentUserAllOutgoingEvents() {
        return eventFacade.getCurrentUserAllOutgoingEvents();
    }

    @Override
    @ApiOperation(value = "Get the current user's incoming event by type and status",
            notes = "Call this endpoint to get the current user's incoming events by type (MEMBERSHIP_GRANTED, MEMBERSHIP_REJECTED)")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of incoming events for current user")
    })
    @GET
    @Path("/notifications/incoming/{eventType}")
    @Produces(MediaType.APPLICATION_JSON)
    public <T> List<T> getCurrentUsersIncomingEventsByTypeAndStatus(@PathParam("eventType") String type) {
        Preconditions.checkArgument(!StringUtils.isEmpty(type));
        return eventFacade.getCurrentUserIncomingEventsByType(type);
    }

    @Override
    @ApiOperation(value = "Get the current user's outgoing events by type and status",
            notes = "Call this endpoint to get the current user's outgoing events by type (MEMBERSHIP_PENDING)")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of outgoing events for current user")
    })
    @GET
    @Path("/notifications/outgoing/{eventType}")
    @Produces(MediaType.APPLICATION_JSON)
    public <T> List<T> getCurrentUserOutgoingEventsByTypeAndStatus(@PathParam("eventType") String type) {
        Preconditions.checkArgument(!StringUtils.isEmpty(type));
        return eventFacade.getCurrentUserOutgoingEventsByType(type);
    }

    @Override
    @ApiOperation(value = "Clear an incoming notification",
            notes = "Call this endpoint to delete a notification addressed to the current user")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Notification deleted")
    })
    @DELETE
    @Path("/notifications/incoming/{notificationId}")
    public void deleteEvent(@PathParam("notificationId") Long id) throws NotAuthorizedException, InvalidEventException, EventNotFoundException {
        eventFacade.deleteUserEvent(id);
    }

    @Override
    @ApiOperation(value = "Get current user's organization notifications",
            notes = "Call this endpoint to get all of the current user's incoming notifications that do not require action, including those meant for organizations the current user has owner rights to")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventAggregateBean.class, message = "List of incoming events for current user")
    })
    @GET
    @Path("/notifications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventAggregateBean> getAllNonActionEvents() {
        return eventFacade.getAllNonActionEvents(currentUserFacade.getInfo());
    }

    @Override
    @ApiOperation(value = "Get current user's pending organization notifications",
            notes = "Call this endpoint to get all of the current user's incoming notifications that require action, including those meant for organizations the current user has owner rights to")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventAggregateBean.class, message = "List of incoming events for current user")
    })
    @GET
    @Path("/notifications/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventAggregateBean> getAllActionEvents() {
        return eventFacade.getAllIncomingActionEvents(currentUserFacade.getInfo());
    }

    @Override
    @ApiOperation(value = "Clear all incoming notification",
            notes = "Call this endpoint to delete all informative notifications addressed to the current user")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Notifications deleted")
    })
    @DELETE
    @Path("/notifications/incoming/")
    public void deleteAll() {
        eventFacade.deleteAllEvents(currentUserFacade.getInfo());
    }

    @Override
    @ApiOperation("Retrieve current user's Oauth2 tokens")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OAuth2TokenBean.class, message = "OAuth2 Tokens")
    })
    @GET
    @Path("/oauth2/tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<OAuth2TokenBean> getCurrentUserOAuthTokens() {
        return currentUserFacade.getCurrentUserOAuth2Tokens();
    }

    @Override
    @ApiOperation("Revoke current user's Oauth2 Token")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @DELETE
    @Path("/oauth2/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    public void revokeCurrentUserOAuthToken(OAuth2TokenBean token) {
        Preconditions.checkNotNull(token);
        Preconditions.checkArgument(StringUtils.isNotEmpty(token.getId()) && StringUtils.isNotEmpty(token.getGatewayId()) && StringUtils.isNotEmpty(token.getAuthenticatedUserid()));
        if (!token.getAuthenticatedUserid().equals(securityContext.getCurrentUser())) {
            throw ExceptionFactory.notAuthorizedException();
        }
        currentUserFacade.revokeCurrentUserOAuth2Token(token);
    }
}
