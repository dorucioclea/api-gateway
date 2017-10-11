package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.dto.UserDtoBean;
import com.t1t.apim.beans.idm.UpdateUserBean;
import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.apim.exceptions.UserNotFoundException;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

/**
 * The User API.
 */
public interface IUserResource {

    /**
     * Use this endpoint to get information about a specific user by the User ID.
     *
     * @param userId The user ID.
     * @return Full user information.
     * @throws UserNotFoundException when specified user not found
     * @summary Get User by ID
     * @statuscode 200 If the user exists and information is returned.
     */
    public UserDtoBean get(String userId) throws UserNotFoundException;

    /**
     * Use this endpoint to update the information about a user.  This will fail
     * unless the authenticated user is an admin or identical to the user being
     * updated.
     *
     * @param userId The user ID.
     * @param user   Updated user information.
     * @throws UserNotFoundException  when specified user not found
     * @throws NotAuthorizedException when not authorized to invoke this method
     * @summary Update a User by ID
     * @statuscode 204 If the user information is successfully updated.
     */
    public void update(String userId, UpdateUserBean user) throws UserNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to search for users.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     *
     * @param criteria The search criteria.
     * @return The search results (a page of organizations).
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     * @summary Search for Users
     * @statuscode 200 If the search is successful.
     */
    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria) throws InvalidSearchCriteriaException;

    /**
     * This endpoint returns the list of organizations that the user is a member of.  The
     * user is a member of an organization if she has at least one role for the org.
     *
     * @param userId The user ID.
     * @return List of organizations.
     * @summary List User Organizations
     * @statuscode 200 If the organization list is successfully returned.
     */
    public List<OrganizationSummaryBean> getOrganizations(String userId);

    /**
     * This endpoint returns all applications that the user has permission to edit.
     *
     * @param userId The user ID.
     * @return List of applications.
     * @summary List User Applications
     * @statuscode 200 If the application list is successfully returned.
     */
    public List<ApplicationSummaryBean> getApplications(String userId);

    /**
     * This endpoint returns all services that the user has permission to edit.
     *
     * @param userId The user ID.
     * @return List of services.
     * @summary List User Services
     * @statuscode 200 If the service list is successfully returned.
     */
    public List<ServiceSummaryBean> getServices(String userId);

    /**
     * Use this endpoint to get information about the user's audit history.  This
     * returns audit entries corresponding to each of the actions taken by the
     * user.  For example, when a user creates a new Organization, an audit entry
     * is recorded and would be included in the result of this endpoint.
     *
     * @param userId   The user ID.
     * @param page     The page of the results to return.
     * @param pageSize The number of results per page to return.
     * @return List of audit entries.
     * @summary Get User Activity
     * @statuscode 200 If the activity is successfully returned.
     */
    public SearchResultsBean<AuditEntryBean> getActivity(String userId, int page, int pageSize);

}
