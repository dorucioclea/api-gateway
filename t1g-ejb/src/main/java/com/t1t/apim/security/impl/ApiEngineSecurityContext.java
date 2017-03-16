package com.t1t.apim.security.impl;

import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 5/09/15.
 */
@SessionScoped
@Default
public class ApiEngineSecurityContext extends AbstractSecurityContext {
    @Inject
    private UserFacade userFacade;
    private String currentUser;

    public ApiEngineSecurityContext() {
    }

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

    public String setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        //calling get to perform a validation
        return getCurrentUser();
    }
}
