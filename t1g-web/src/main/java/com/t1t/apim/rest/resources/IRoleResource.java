package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.idm.NewRoleBean;
import com.t1t.apim.beans.idm.RoleBean;
import com.t1t.apim.beans.idm.UpdateRoleBean;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.apim.exceptions.RoleAlreadyExistsException;
import com.t1t.apim.exceptions.RoleNotFoundException;

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
     *
     * @param bean The new role.
     * @return Full information about the created role.
     * @throws RoleAlreadyExistsException when role already exists
     * @throws NotAuthorizedException     when not authorized to invoke this method
     * @summary Create Role
     * @servicetag admin
     * @statuscode 200 If the role is created successfully.
     */
    public RoleBean create(NewRoleBean bean) throws RoleAlreadyExistsException, NotAuthorizedException;

    /**
     * This endpoint lists all of the roles currently defined in apiman.
     *
     * @return A list of roles.
     * @throws NotAuthorizedException when not authorized to invoke this method
     * @summary List all Roles
     * @statuscode 200 If the role list is returned successfully.
     */
    public List<RoleBean> list() throws NotAuthorizedException;

    /**
     * Use this endpoint to retrieve information about a single Role by its
     * ID.
     *
     * @param roleId The role ID.
     * @return A role.
     * @throws RoleNotFoundException  when a request is sent for a role that does not exist
     * @throws NotAuthorizedException when not authorized to invoke this method
     * @summary Get a Role by ID
     * @statuscode 200 If the role is returned successfully.
     */
    public RoleBean get(String roleId) throws RoleNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the information about an existing role.  The
     * role is identified by its ID.
     *
     * @param roleId The role ID.
     * @param bean   Updated role information.
     * @throws RoleNotFoundException  when a request is sent for a role that does not exist
     * @throws NotAuthorizedException when not authorized to invoke this method
     * @summary Update a Role by ID
     * @servicetag admin
     * @statuscode 204 If the role is updated successfully.
     */
    public void update(String roleId, UpdateRoleBean bean) throws RoleNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to remove a role by its ID.
     *
     * @param roleId The role ID.
     * @throws RoleNotFoundException  when a request is sent for a role that does not exist
     * @throws NotAuthorizedException when not authorized to invoke this method
     * @summary Delete a Role by ID
     * @servicetag admin
     * @statuscode 204 If the role is deleted.
     */
    public void delete(String roleId) throws RoleNotFoundException, NotAuthorizedException;

    /**
     * This endpoint provides a way to search for roles.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     *
     * @param criteria The search criteria.
     * @return The search results (a page of roles).
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     * @throws NotAuthorizedException         when not authorized to invoke this method
     * @summary Search for Roles
     * @statuscode 200 If the search completes successfully.
     */
    public SearchResultsBean<RoleBean> search(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException, NotAuthorizedException;

}
