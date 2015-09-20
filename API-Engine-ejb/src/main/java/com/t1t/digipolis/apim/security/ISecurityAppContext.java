package com.t1t.digipolis.apim.security;

import com.t1t.digipolis.apim.beans.idm.PermissionType;

import java.util.Set;

/**
 * The security context used by the REST API to determine whether the
 * application has appropriate access to access the authorization customizations on
 * the API Engine.
 *
 */
public interface ISecurityAppContext {

    /**
     * @return the authenticated application consumer.
     */
    public String getApplciation();

}
