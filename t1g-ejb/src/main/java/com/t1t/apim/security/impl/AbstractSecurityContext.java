package com.t1t.apim.security.impl;

import com.t1t.apim.beans.idm.PermissionBean;
import com.t1t.apim.beans.idm.PermissionType;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.apim.security.i18n.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for security context implementations.
 *
 */
public abstract class AbstractSecurityContext implements ISecurityContext, Serializable {

    protected static Logger logger = LoggerFactory.getLogger(AbstractSecurityContext.class);
    
    protected IndexedPermissions permissions;

    @Inject private IIdmStorage idmStorage;

    /**
     * @see ISecurityContext#hasPermission(PermissionType, String)
     */
    @Override
    public boolean hasPermission(PermissionType permission, String organizationId) {
        // Admins can do everything.
        if (isAdmin())
            return true;
        return getPermissions().hasQualifiedPermission(permission, organizationId);
    }

    /**
     * @see ISecurityContext#getPermittedOrganizations(PermissionType)
     */
    @Override
    public Set<String> getPermittedOrganizations(PermissionType permission) {
        return getPermissions().getOrgQualifiers(permission);
    }
    
    /**
     * @return the user permissions for the current user
     */
    private IndexedPermissions getPermissions() {
        IndexedPermissions rval = permissions;
        if (rval == null) {
            rval = loadPermissions();
            permissions=rval;
        }
        return rval;
    }

    /**
     * Loads the current user's permissions into a thread local variable.
     */
    private IndexedPermissions loadPermissions() {
        String userId = getCurrentUser();
        try {
            return new IndexedPermissions(getIdmStorage().getPermissions(userId));
        } catch (StorageException e) {
            logger.error(Messages.getString("AbstractSecurityContext.ErrorLoadingPermissions") + userId, e); //$NON-NLS-1$
            return new IndexedPermissions(new HashSet<PermissionBean>());
        }
    }
    
    /**
     * Called to clear the current thread local permissions bean.
     */
    protected void clearPermissions() {
        permissions = null;
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

}
