package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.idm.NewRoleBean;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.idm.UpdateRoleBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.exceptions.RoleAlreadyExistsException;
import com.t1t.digipolis.apim.exceptions.RoleNotFoundException;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

/**
 * The Role API. Used to manage roles. Note: not used to manage users or user
 * membership in roles. This API simply provides a way to create and manage role
 * definitions. Typically this API is only available to system admins.
 *
 * @author eric.wittmann@redhat.com
 */
public interface IRoleResource {

    /**
     * Use this endpoint to create a new apiman role.  A role consists of
     * a set of permissions granted to a user when that user is given the
     * role within the context of an organization.
     * @summary Create Role
     * @servicetag admin
     * @param bean The new role.
     * @statuscode 200 If the role is created successfully.
     * @return Full information about the created role.
     * @throws RoleAlreadyExistsException when role already exists
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public RoleBean create(NewRoleBean bean) throws RoleAlreadyExistsException, NotAuthorizedException;

    /**
     * This endpoint lists all of the roles currently defined in apiman.
     * @summary List all Roles
     * @statuscode 200 If the role list is returned successfully.
     * @return A list of roles.
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public List<RoleBean> list() throws NotAuthorizedException;

    /**
     * Use this endpoint to retrieve information about a single Role by its
     * ID.
     * @summary Get a Role by ID
     * @param roleId The role ID.
     * @statuscode 200 If the role is returned successfully.
     * @return A role.
     * @throws RoleNotFoundException when a request is sent for a role that does not exist
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public RoleBean get(String roleId) throws RoleNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the information about an existing role.  The
     * role is identified by its ID.
     * @summary Update a Role by ID
     * @servicetag admin
     * @param roleId The role ID.
     * @param bean Updated role information.
     * @statuscode 204 If the role is updated successfully.
     * @throws RoleNotFoundException when a request is sent for a role that does not exist
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public void update(String roleId, UpdateRoleBean bean) throws RoleNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to remove a role by its ID.
     * @summary Delete a Role by ID
     * @servicetag admin
     * @param roleId The role ID.
     * @statuscode 204 If the role is deleted.
     * @throws RoleNotFoundException when a request is sent for a role that does not exist
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public void delete(String roleId) throws RoleNotFoundException, NotAuthorizedException;

    /**
     * This endpoint provides a way to search for roles.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     * @summary Search for Roles
     * @param criteria The search criteria.
     * @statuscode 200 If the search completes successfully.
     * @return The search results (a page of roles).
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     * @throws NotAuthorizedException when not authorized to invoke this method
     */
    public SearchResultsBean<RoleBean> search(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException, NotAuthorizedException;

}
