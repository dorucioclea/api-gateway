package com.t1t.digipolis.apim.rest.resources;

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

    public List<EventBean> getCurrentUserAllIncomingEvents();

    public List<EventBean> getCurrentUserAllOutgoingEvents();

    public <T> List<T> getCurrentUserOutgoingEventsByTypeAndStatus(String type);

    public <T> List<T> getCurrentUsersIncomingEventsByTypeAndStatus(String type);

    public void deleteEvent(Long id) throws com.t1t.digipolis.apim.exceptions.NotAuthorizedException, InvalidEventException, EventNotFoundException;

    public List<EventAggregateBean> getAllNonActionEvents();

    public List<EventAggregateBean> getAllActionEvents();
}
