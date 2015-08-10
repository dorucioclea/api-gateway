package com.t1t.digipolis.apim.security.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.servlet.http.HttpServletRequest;

/**
 * An alternative security context used when protected by keycloak.
 *
 */
@ApplicationScoped @Alternative
public class KeycloakSecurityContext extends AbstractSecurityContext {

    /**
     * Constructor.
     */
    public KeycloakSecurityContext() {
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#getRequestHeader(String)
     */
    @Override
    public String getRequestHeader(String headerName) {
        return DefaultSecurityContext.servletRequest.get().getHeader(headerName);
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#getCurrentUser()
     */
    @Override
    public String getCurrentUser() {
        return DefaultSecurityContext.servletRequest.get().getRemoteUser();
    }

    /**
     * @see com.t1t.digipolis.apim.security.impl.DefaultSecurityContext#getFullName()
     */
    @Override
    public String getFullName() {
        HttpServletRequest request = DefaultSecurityContext.servletRequest.get();
        org.keycloak.KeycloakSecurityContext session = (org.keycloak.KeycloakSecurityContext) request.getAttribute(org.keycloak.KeycloakSecurityContext.class.getName());
        if (session != null) {
            return session.getToken().getName();
        } else {
            return null;
        }
    }

    /**
     * @see com.t1t.digipolis.apim.security.impl.DefaultSecurityContext#getEmail()
     */
    @Override
    public String getEmail() {
        HttpServletRequest request = DefaultSecurityContext.servletRequest.get();
        org.keycloak.KeycloakSecurityContext session = (org.keycloak.KeycloakSecurityContext) request.getAttribute(org.keycloak.KeycloakSecurityContext.class.getName());
        if (session != null) {
            return session.getToken().getEmail();
        } else {
            return null;
        }
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#isAdmin()
     */
    @Override
    public boolean isAdmin() {
        // TODO warning - hard coded role value here
        return DefaultSecurityContext.servletRequest.get().isUserInRole("apiadmin"); //$NON-NLS-1$
    }

}
