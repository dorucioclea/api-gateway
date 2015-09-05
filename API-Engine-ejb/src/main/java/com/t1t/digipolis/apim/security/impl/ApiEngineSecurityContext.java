package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.beans.idm.PermissionBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.UnmarshallingException;
import org.xml.sax.SAXException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

/**
 * Created by michallispashidis on 5/09/15.
 */
@ApplicationScoped
@Default
public class ApiEngineSecurityContext extends AbstractSecurityContext {
    //needed for SAML2 verification
    @Inject
    private UserFacade userFacade;

    public static final ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<>();
    private static final String AUTHENTICATED_USER_KEY = "authenticated_user";

    public ApiEngineSecurityContext() {
    }

    @Override
    public String getCurrentUser() {
        String user = "";
        String authHeader = servletRequest.get().getHeader("Authorization");
        String authenticated_user = (String) servletRequest.get().getAttribute(AUTHENTICATED_USER_KEY);
        if (StringUtils.isEmpty(authenticated_user)) {
            if (authHeader!=null && authHeader.toUpperCase().startsWith("BEARER")) {
                String samlToken = authHeader.substring(6).trim();
                try {
                    user = userFacade.userFromSAML2BearerToken(samlToken);
                    servletRequest.get().setAttribute(AUTHENTICATED_USER_KEY, user);
                } catch (SAXException | ParserConfigurationException | ConfigurationException | IOException | UnmarshallingException e) {
                    throw new SecurityException("SAML2 Bearer token cannot be parsed: " + e.getMessage());
                }
            } else throw new SecurityException("Unauthorized access");
        } else {
            //user already in threadlocal
            user = authenticated_user;
        }
        return user;
    }

    @Override
    public String getFullName() {
        String fullName = "";
        try {
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
            email = getIdmStorage().getUser(getCurrentUser()).getEmail();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return email;
    }

    @Override
    public boolean isAdmin() {
        boolean result = false;
        try {
            Set<PermissionBean> permissions = getIdmStorage().getPermissions(getCurrentUser());
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return true;
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
