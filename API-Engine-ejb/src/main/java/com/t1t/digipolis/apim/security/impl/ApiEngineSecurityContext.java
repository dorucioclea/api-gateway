package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by michallispashidis on 5/09/15.
 */
@ApplicationScoped
@Default
//@Alternative
public class ApiEngineSecurityContext extends AbstractSecurityContext {
    @Inject
    private UserFacade userFacade;

    public static final ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<>();
    private static final String AUTHENTICATED_USER_KEY = "authenticated_user";
    private static final String HEADER_APIKEY_USER = "X-Consumer-Username";

    public ApiEngineSecurityContext() {
    }

    @Override
    public String getCurrentUser() {
        String user = "";
        String authHeader = servletRequest.get().getHeader(HEADER_APIKEY_USER);
        if (!StringUtils.isEmpty(authHeader)) {
            UserBean userName = userFacade.get(authHeader);
            if (userName == null) {
                clearPermissions();
                clearServletRequest();
                throw new UserNotFoundException("Unauthorized access");
            } else user = authHeader;
        } else {
            clearPermissions();
            clearServletRequest();
            throw new UserNotFoundException("Unauthorized access");
        }

        return user;
    }

    @Override
    public String getFullName() {
        String fullName = "";
        try {
            if (!StringUtils.isEmpty(getCurrentUser()))
                fullName = getIdmStorage().getUser(getCurrentUser()).getFullName();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return fullName;
    }

    @Override
    public String getEmail() {
        String email = "";
        try {
            if (!StringUtils.isEmpty(getCurrentUser())) email = getIdmStorage().getUser(getCurrentUser()).getEmail();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return email;
    }


    @Override
    public boolean isAdmin() {
        boolean result = false;
        if (!StringUtils.isEmpty(getCurrentUser())) {
            result = userFacade.get(getCurrentUser()).getAdmin();
        }
        return result;
    }

    @Override
    public String getRequestHeader(String headerName) {
        return servletRequest.get().getHeader(headerName);
    }

    /**
     * Called to set the current context http servlet request.
     *
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
