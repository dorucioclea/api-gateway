package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.idm.UserPermissionsBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IPermissionsResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.rest.resources.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.rest.resources.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Implementation of the Permissions API.
 */
@ApplicationScoped
public class PermissionsResourceImpl implements IPermissionsResource {
    
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    
    /**
     * Constructor.
     */
    public PermissionsResourceImpl() {
    }
    
    /**
     * @see IPermissionsResource#getPermissionsForUser(String)
     */
    @Override
    public UserPermissionsBean getPermissionsForUser(String userId) throws UserNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();

        try {
            UserPermissionsBean bean = new UserPermissionsBean();
            bean.setUserId(userId);
            bean.setPermissions(idmStorage.getPermissions(userId));
            return bean;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
    
    /**
     * @see IPermissionsResource#getPermissionsForCurrentUser()
     */
    @Override
    public UserPermissionsBean getPermissionsForCurrentUser() throws UserNotFoundException {
        try {
            String currentUser = securityContext.getCurrentUser();
            UserPermissionsBean bean = new UserPermissionsBean();
            bean.setUserId(currentUser);
            bean.setPermissions(idmStorage.getPermissions(currentUser));
            return bean;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @return the idmStorage
     */
    public IIdmStorage getIdmStorage() {
        return idmStorage;
    }

    /**
     * @param idmStorage the idmStorage to set
     */
    public void setIdmStorage(IIdmStorage idmStorage) {
        this.idmStorage = idmStorage;
    }

    /**
     * @return the securityContext
     */
    public ISecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(ISecurityContext securityContext) {
        this.securityContext = securityContext;
    }
    
}
