package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.UpdateUserBean;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.rest.resources.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.rest.resources.exceptions.UserNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The User API.
 */
@Path("/users")
public interface IUserResource {

    /**
     * Use this endpoint to get information about a specific user by the User ID.
     * @summary Get User by ID
     * @param userId The user ID.
     * @statuscode 200 If the user exists and information is returned.
     * @return Full user information.
     * @throws UserNotFoundException when specified user not found
     */
    public UserBean get(String userId) throws UserNotFoundException;

    /**
     * Use this endpoint to update the information about a user.  This will fail
     * unless the authenticated user is an admin or identical to the user being
     * updated.
     * @summary Update a User by ID
     * @param userId The user ID.
     * @param user Updated user information.
     * @statuscode 204 If the user information is successfully updated.
     * @throws UserNotFoundException when specified user not found
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public void update(String userId, UpdateUserBean user) throws UserNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to search for users.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     * @summary Search for Users
     * @param criteria The search criteria.
     * @statuscode 200 If the search is successful.
     * @return The search results (a page of organizations).
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     */
    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria) throws InvalidSearchCriteriaException;

    /**
     * This endpoint returns the list of organizations that the user is a member of.  The
     * user is a member of an organization if she has at least one role for the org.
     * @summary List User Organizations
     * @param userId The user ID.
     * @statuscode 200 If the organization list is successfully returned.
     * @return List of organizations.
     */
    public List<OrganizationSummaryBean> getOrganizations(String userId);

    /**
     * This endpoint returns all applications that the user has permission to edit.
     * @summary List User Applications
     * @param userId The user ID.
     * @statuscode 200 If the application list is successfully returned.
     * @return List of applications.
     */
    public List<ApplicationSummaryBean> getApplications(String userId);

    /**
     * This endpoint returns all services that the user has permission to edit.
     * @summary List User Services
     * @param userId The user ID.
     * @statuscode 200 If the service list is successfully returned.
     * @return List of services.
     */
    public List<ServiceSummaryBean> getServices(String userId);

    /**
     * Use this endpoint to get information about the user's audit history.  This
     * returns audit entries corresponding to each of the actions taken by the
     * user.  For example, when a user creates a new Organization, an audit entry
     * is recorded and would be included in the result of this endpoint.
     * @summary Get User Activity
     * @param userId The user ID.
     * @param page The page of the results to return.
     * @param pageSize The number of results per page to return.
     * @statuscode 200 If the activity is successfully returned.
     * @return List of audit entries.
     */
    public SearchResultsBean<AuditEntryBean> getActivity(String userId,int page, int pageSize);
    
}
