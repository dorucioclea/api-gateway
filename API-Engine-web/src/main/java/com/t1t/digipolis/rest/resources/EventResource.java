package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.EventFacade;
import com.t1t.digipolis.apim.rest.resources.IEventResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Api(value = "/events", description = "The Events API.")
@Path("/events")
@ApplicationScoped
public class EventResource implements IEventResource {

    @Inject
    private EventFacade eventFacade;
    @Inject
    private ISecurityContext securityContext;

    @Override
    @ApiOperation(value = "Get an organization's pending membership requests",
            notes = "Call this endpoint to get pending membership requests for a given organization")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of membership requests for {organizationId}.")
    })
    @GET
    @Path("/organizations/{organizationId}/membership-requests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventBean> getMembershipRequests(@PathParam("organizationId") String orgId) throws NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, orgId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        return eventFacade.getMembershipRequests(orgId);
    }
}