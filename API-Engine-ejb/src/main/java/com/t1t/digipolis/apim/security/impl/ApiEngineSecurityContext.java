package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by michallispashidis on 5/09/15.
 */
@RequestScoped
@Default
public class ApiEngineSecurityContext extends AbstractSecurityContext implements Serializable {
    //Logger
    private static Logger LOG = LoggerFactory.getLogger(ApiEngineSecurityContext.class.getName());
    @Inject private UserFacade userFacade;
    private String currentUser;

    public ApiEngineSecurityContext() {}

    @Override
    public String getCurrentUser() {
        if (!StringUtils.isEmpty(currentUser)) {
            UserBean userName = userFacade.get(currentUser);
            if (userName == null) {
                clearPermissions();
                throw new UserNotFoundException("Unauthorized access");
            }
        } else {
            clearPermissions();
            throw new UserNotFoundException("Unauthorized access");
        }
        LOG.info("Logged-in user:{}",currentUser);
        return currentUser;
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

    /**
     * Called to clear the current thread local permissions bean.
     */
    protected static void clearPermissions() {
        AbstractSecurityContext.clearPermissions();
    }

    public String setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        //calling get to perform a validation
        return getCurrentUser();
    }
}
