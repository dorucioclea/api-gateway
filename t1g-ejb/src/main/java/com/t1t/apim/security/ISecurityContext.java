package com.t1t.apim.security;

import com.t1t.apim.beans.idm.PermissionType;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import java.util.Set;

/**
 * The security context used by the REST API to determine whether the
 * current user has appropriate access to see specific data or perform
 * certain actions.
 */
public interface ISecurityContext {

    /**
     * @return the currently authentiated user.
     */
    public String getCurrentUser();

    /**
     * Sets the current username to be validated, and validates by calling getCurrentUser.
     *
     * @return
     */
    public String setCurrentUser(JwtClaims claims, String validatedUser) throws MalformedClaimException;

    /**
     * Sets the current username to be validated, and validates by calling getCurrentUser.
     *
     * @return
     */
    public String setCurrentUser(String userName);

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
     *
     * @return true if admin, else false
     */
    public boolean isAdmin();

    /**
     * Returns true if the current user has permission to perform a particular
     * action for the provided organization.
     *
     * @param permission     the permission type
     * @param organizationId the org id
     * @return true if has permission, else false
     */
    public boolean hasPermission(PermissionType permission, String organizationId);

    /**
     * Returns the set of organizations for which the current user is allowed
     * to perform a given action.
     *
     * @param permission the permission type
     * @return set of permitted organizations
     */
    public Set<String> getPermittedOrganizations(PermissionType permission);

}
