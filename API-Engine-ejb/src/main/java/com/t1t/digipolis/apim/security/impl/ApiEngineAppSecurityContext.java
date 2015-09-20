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
public class ApiEngineAppSecurityContext extends AbstractSecurityAppContext {
    @Inject
    private UserFacade userFacade;

    public static final ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<>();
    private static final String AUTHENTICATED_USER_KEY = "authenticated_user";
    private static final String HEADER_APIKEY_USER = "X-Consumer-Username";

    public ApiEngineAppSecurityContext() {
    }

    @Override
    public String getApplciation() {
        String application = "";
        String authHeader = servletRequest.get().getHeader(HEADER_APIKEY_USER);
        if (!StringUtils.isEmpty(authHeader)) {
            //TODO optionally we can load the application already-not necessary at the moment
            application = authHeader;
        } else {
            clearServletRequest();
            throw new UserNotFoundException("Unauthorized access");
        }
        return application;
    }

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
     * Called to clear the context http servlet request.
     */
    protected static void clearServletRequest() {
        servletRequest.remove();
    }

}
