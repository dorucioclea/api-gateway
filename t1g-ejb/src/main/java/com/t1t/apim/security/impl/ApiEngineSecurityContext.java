package com.t1t.apim.security.impl;

import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.UserNotFoundException;
import com.t1t.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 5/09/15.
 */
@SessionScoped
@Default
public class ApiEngineSecurityContext extends AbstractSecurityContext {

    private static final String HEADER_CREDENTIAL_USERNAME = "X-Credential-Username";

    @Inject
    private UserFacade userFacade;
    private String currentUser;

    @Override
    public String getCurrentUser() {
        if (!StringUtils.isEmpty(currentUser)) {
            UserBean userName = userFacade.get(currentUser);//should be unique username
            if (userName == null) {
                clearPermissions();
                currentUser = "";
            }
        } else {
            clearPermissions();
            currentUser = "";
        }
        return currentUser;
    }

    @Override
    public String getFullName() {
        String fullName = "";
        try {
            if (!StringUtils.isEmpty(getCurrentUser()))
                fullName = getIdmStorage().getUser(getCurrentUser()).getFullName();
        } catch (StorageException e) {
            logger.warn("User::getFullName: no user set");
            fullName = "";
        }
        return fullName;
    }

    @Override
    public String getEmail() {
        String email = "";
        try {
            if (!StringUtils.isEmpty(getCurrentUser())) email = getIdmStorage().getUser(getCurrentUser()).getEmail();
        } catch (StorageException e) {
            logger.warn("User::getEmail: no user set");
            email = "";
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

    /**
     * Called to clear the current thread local permissions bean.
     */
    protected void clearPermissions() {
        permissions = null;
    }

    public String setCurrentUser(JwtClaims claims) throws MalformedClaimException {
        this.currentUser = claims.getSubject() != null ?
                claims.getSubject() : claims.getStringClaimValue(HEADER_CREDENTIAL_USERNAME) != null ?
                claims.getStringClaimValue(HEADER_CREDENTIAL_USERNAME) : "";
        //calling get to perform a validation
        try {
            return getCurrentUser();
        }
        catch (Exception ex) {
            try {
                userFacade.initNewUser(claims);
                return currentUser;
            }
            catch (Exception e) {
                throw ExceptionFactory.userNotFoundException(currentUser);
            }
        }
    }

    @Override
    public String setCurrentUser(String userName) {
        this.currentUser = userName;
        return getCurrentUser();
    }
}
