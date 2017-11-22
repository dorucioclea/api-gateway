package com.t1t.apim.security.impl;

import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.UserNotFoundException;
import com.t1t.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 5/09/15.
 */
@SessionScoped
@Default
public class ApiEngineSecurityContext extends AbstractSecurityContext {

    private static final Logger log = LoggerFactory.getLogger(ApiEngineSecurityContext.class);

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

    public String setCurrentUser(JwtClaims claims, String validatedUser) throws MalformedClaimException {
        this.currentUser = validatedUser;
        //calling get to perform a validation
        try {
            return getCurrentUser();
        } catch (Exception ex) {
            log.warn("User not found, attempting to initialize new user: ", ex);
            try {
                userFacade.initNewUser(claims, validatedUser);
                return currentUser;
            } catch (Exception e) {
                log.error("Could not initialize new user: ", e);
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
