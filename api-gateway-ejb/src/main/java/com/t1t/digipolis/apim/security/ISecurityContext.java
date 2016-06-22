package com.t1t.digipolis.apim.security;

import com.t1t.digipolis.apim.beans.idm.PermissionType;

import java.util.Set;

/**
 * The security context used by the REST API to determine whether the
 * current user has appropriate access to see specific data or perform
 * certain actions.
 *
 */
public interface ISecurityContext {

    /**
     * @return the currently authentiated user.
     */
    public String getCurrentUser();

    /**
     * Sets the current username to be validated, and validates by calling getCurrentUser.
     * @return
     */
    public String setCurrentUser(String userId);

    /**
     * @return the currently authenticated user's full name
     */
    public String getFullName();

    /**
     * @return the currently authenticated user's email address
     */
    public String getEmail();

    /**
     * Returns true if the current user is an administrator.
     * @return true if admin, else false
     */
    public boolean isAdmin();

    /**
     * Returns true if the current user has permission to perform a particular
     * action for the provided organization.
     * @param permission the permission type
     * @param organizationId the org id
     * @return true if has permission, else false
     */
    public boolean hasPermission(PermissionType permission, String organizationId);

    /**
     * Returns the set of organizations for which the current user is allowed
     * to perform a given action.
     * @param permission the permission type
     * @return set of permitted organizations
     */
    public Set<String> getPermittedOrganizations(PermissionType permission);

}
