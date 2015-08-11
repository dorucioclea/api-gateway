package com.t1t.digipolis.apim.security.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.servlet.http.HttpServletRequest;

/**
 * The basic/default implementation of a security context.
 */
@ApplicationScoped @Alternative
public class DefaultSecurityContext extends AbstractSecurityContext {

    public static final ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<>();

    /**
     * Constructor.
     */
    public DefaultSecurityContext() {
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#getRequestHeader(String)
     */
    @Override
    public String getRequestHeader(String headerName) {
        return servletRequest.get().getHeader(headerName);
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#getCurrentUser()
     */
    @Override
    public String getCurrentUser() {
        return servletRequest.get().getRemoteUser();
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#getEmail()
     */
    @Override
    public String getEmail() {
        return null;
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#getFullName()
     */
    @Override
    public String getFullName() {
        return null;
    }

    /**
     * @see com.t1t.digipolis.apim.security.ISecurityContext#isAdmin()
     */
    @Override
    public boolean isAdmin() {
        // TODO warning - hard coded role value here
        return servletRequest.get().isUserInRole("apiadmin"); //$NON-NLS-1$
    }

    /**
     * Called to set the current context http servlet request.
     * @param request
     */
    protected static void setServletRequest(HttpServletRequest request) {
        servletRequest.set(request);
    }

    /**
     * Called to clear the current thread local permissions bean.
     */
    protected static void clearPermissions() {
        AbstractSecurityContext.clearPermissions();
    }

    /**
     * Called to clear the context http servlet request.
     */
    protected static void clearServletRequest() {
        servletRequest.remove();
    }

}