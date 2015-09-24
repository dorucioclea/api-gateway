package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by michallispashidis on 5/09/15.
 */
@SessionScoped
@Default
public class ApiEngineAppSecurityContext extends AbstractSecurityAppContext implements Serializable {
    //Logger
    private static Logger LOG = LoggerFactory.getLogger(ApiEngineAppSecurityContext.class.getName());
    @Inject
    private UserFacade userFacade;
    private String currentApplication;

    public ApiEngineAppSecurityContext() {
    }

    @Override
    public String getApplication() {
        if (!StringUtils.isEmpty(currentApplication)) {
            //TODO optionally we can load the application already-not necessary at the moment
        } else {
            throw new UserNotFoundException("Unauthorized application access");
        }
        LOG.info("Logged-in application:{}", currentApplication);
        return currentApplication;
    }

    public String setCurrentApplication(String currentApplication) {
        this.currentApplication = currentApplication;
        return getApplication();
    }
}