package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.facades.UserFacade;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by michallispashidis on 5/09/15.
 */
@SessionScoped
@Default
public class ApiEngineAppSecurityContext extends AbstractSecurityAppContext implements Serializable {
    //Logger
    private static Logger LOG = LoggerFactory.getLogger(ApiEngineAppSecurityContext.class.getName());
    @Inject private UserFacade userFacade;
    @Inject private IStorageQuery query;

    private String nonManagedApplication;
    private String currentApplication;
    private AppIdentifier appIdentifier;

    public ApiEngineAppSecurityContext() {
    }

    @Override
    public String getApplication() {
        if (!StringUtils.isEmpty(currentApplication)) {
            //TODO optionally we can load the application registered services - but no need at the moment.
        } else {
            return "";
        }
        return currentApplication;
    }

    /**
     * We consult the filter for marketplaces in order to provide a scope.
     * The scope is the context of the API Marketplace.
     * A scope must be available in the config file AND must be available in the DB. The DB info provides the necessary info for the API publisher, while the config file
     * triggers the activation of a specific scope.
     * A scope is retrieved by the prefix of the API Engine consumer. Be sure that the consumer uses the conventional dotted-notation for a consumer name, aka:
     * - prefix.name.version
     * - prefix.name
     *
     * Examples:
     * - int.apimktinternal.v1
     * - ext.apimktexternal
     *
     * @param currentApplication
     * @return
     * @throws StorageException
     */
    public String setCurrentApplication(String currentApplication) throws StorageException {
        this.currentApplication = currentApplication;
        this.appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(this.currentApplication);
        if(appIdentifier!=null && appIdentifier.getPrefix()!=null){
            final ManagedApplicationBean managedApplication = query.findManagedApplication(appIdentifier.getPrefix());
            if(managedApplication!=null){
                LOG.debug("Managed application used for request: {}",managedApplication);
            }
        }
        return getApplication();
    }

    @Override
    public AppIdentifier getApplicationIdentifier() {
        return this.appIdentifier;
    }

    @Override
    public String getApplicationPrefix() {
        if(appIdentifier!=null && !StringUtils.isEmpty(appIdentifier.getPrefix())){
            return appIdentifier.getPrefix();
        }return "";
    }

    @Override
    public String getNonManagedApplication() {
        return this.nonManagedApplication;
    }

    @Override
    public String setNonManagedApplication(String application) {
        this.nonManagedApplication = application;
        return getNonManagedApplication();
    }
}
