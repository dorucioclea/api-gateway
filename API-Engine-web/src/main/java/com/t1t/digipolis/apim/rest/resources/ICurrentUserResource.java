package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenSet;
import com.t1t.digipolis.apim.beans.events.EventAggregateBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.idm.CurrentUserBean;
import com.t1t.digipolis.apim.beans.idm.UpdateUserBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

/**
 * The Current User API.  Returns information about the authenticated
 * user.
 */
public interface ICurrentUserResource {

    /**
     * Use this endpoint to get information about the currently authenticated user.
     * @summary Get Current User Information
     * @statuscode 200 If the information is correctly returned.
     * @return Information about the authenticated user.
     */
    public CurrentUserBean getInfo();

    /**
     * This endpoint allows updating information about the authenticated user.
     * @summary Update Current User Information
     * @param info Data to use when updating the user.
     * @statuscode 204 If the update is successful.
     */
    public void updateInfo(UpdateUserBean info);
    
    /**
     * This endpoint returns a list of all the organizations for which the current user
     * has permission to edit applications.  For example, when creating a new Application,
     * the user interface must ask the user to choose within which Organization to create
     * it.  This endpoint lists the valid choices for the current user.
     * @summary Get Organizations (app-edit)
     * @statuscode 200 If the organizations are successfully returned.
     * @return A list of organizations.
     */
    public List<OrganizationSummaryBean> getAppOrganizations();
    
    /**
     * This endpoint returns a list of all the organizations for which the current user
     * has permission to edit services.  For example, when creating a new Service,
     * the user interface must ask the user to choose within which Organization to create
     * it.  This endpoint lists the valid choices for the current user.
     * @summary Get Organizations (svc-edit)
     * @statuscode 200 If the organizations are successfully returned.
     * @return A list of organizations.
     */
    public List<OrganizationSummaryBean> getServiceOrganizations();
    
    /**
     * This endpoint returns a list of all the organizations for which the current user
     * has permission to edit plans.  For example, when creating a new Plan,
     * the user interface must ask the user to choose within which Organization to create
     * it.  This endpoint lists the valid choices for the current user.
     * @summary Get Organizations (plan-edit)
     * @statuscode 200 If the organizations are successfully returned.
     * @return A list of organizations.
     */
    public List<OrganizationSummaryBean> getPlanOrganizations();

    /**
     * Use this endpoint to list all of the Applications the current user has permission
     * to edit.  This includes all Applications from all Organizations the user has 
     * application edit privileges for.
     * @summary Get Current User's Applications
     * @statuscode 200 If the applications are successfully returned.
     * @return A list of Applications.
     */
    public List<ApplicationSummaryBean> getApplications();

    /**
     * Use this endpoint to list all of the Services the current user has permission
     * to edit.  This includes all Services from all Organizations the user has 
     * service edit privileges for.
     * @summary Get Current User's Services
     * @statuscode 200 If the services are successfully returned.
     * @return A list of Services.
     */
    public List<ServiceSummaryBean> getServices();

    /**
     * Use this endpoint to retrieve all incoming events for the current user
     * @return
     */
    public List<EventBean> getCurrentUserAllIncomingEvents();

    /**
     * Use this endpoint to retrieve all outgoing event for the current user
     * @return
     */
    public List<EventBean> getCurrentUserAllOutgoingEvents();

    /**
     * Use this endpoint to retrieve the current user's outgoing events by type and status
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getCurrentUserOutgoingEventsByTypeAndStatus(String type);

    /**
     * Use this endpoint to retrieve the current user's incoming events by type and status
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getCurrentUsersIncomingEventsByTypeAndStatus(String type);

    /**
     * Delete an event
     * @param id
     * @throws com.t1t.digipolis.apim.exceptions.NotAuthorizedException
     * @throws InvalidEventException
     * @throws EventNotFoundException
     */
    public void deleteEvent(Long id) throws com.t1t.digipolis.apim.exceptions.NotAuthorizedException, InvalidEventException, EventNotFoundException;

    /**
     * Get all events for the current user that require no action on the user's behalf
     * @return
     */
    public List<EventAggregateBean> getAllNonActionEvents();

    /**
     * Get all events for the current user that require an action on the user's behalf
     * @return
     */
    public List<EventAggregateBean> getAllActionEvents();

    /**
     * Delete all events for the current user that do not require any actions on the user's behalf
     */
    public void deleteAll();

    /**
     * Retrieve the current user's OAuth2 tokens
     * @return
     */
    public OAuth2TokenSet getCurrentUserOAuthTokens(Integer page);

    /**
     * Revoke a current user's OAuth2 token
     * @param token
     */
    public void revokeCurrentUserOAuthToken(OAuth2TokenRevokeBean token);
}
