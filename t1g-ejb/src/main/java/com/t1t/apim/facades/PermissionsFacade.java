package com.t1t.apim.facades;

import com.t1t.apim.beans.idm.UserPermissionsBean;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.security.ISecurityContext;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PermissionsFacade {

    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IIdmStorage idmStorage;

    public UserPermissionsBean getPermissionsForUser(String userId) {
        try {
            UserPermissionsBean bean = new UserPermissionsBean();
            bean.setUserId(userId);
            bean.setPermissions(idmStorage.getPermissions(userId));
            return bean;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public UserPermissionsBean getPermissionsForCurrentUser() {
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
}
