package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.logging.ApimanLogger;
import com.t1t.digipolis.apim.core.logging.IApimanLogger;
import com.t1t.digipolis.apim.rest.resources.ICurrentUserResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the Current User API.
 */
@ApplicationScoped
public class CurrentUserResourceImpl implements ICurrentUserResource {

    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private IStorageQuery query;
    @Inject
    private ISecurityContext securityContext;
    @Inject @ApimanLogger(CurrentUserResourceImpl.class)
    private IApimanLogger log;


    /**
     * Constructor.
     */
    public CurrentUserResourceImpl() {
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#getInfo()
     */
    @Override
    public CurrentUserBean getInfo() {
        String userId = securityContext.getCurrentUser();
        try {
            CurrentUserBean rval = new CurrentUserBean();
            UserBean user = idmStorage.getUser(userId);
            if (user == null) {
                user = new UserBean();
                user.setUsername(userId);
                if (securityContext.getFullName() != null) {
                    user.setFullName(securityContext.getFullName());
                } else {
                    user.setFullName(userId);
                }
                if (securityContext.getEmail() != null) {
                    user.setEmail(securityContext.getEmail());
                } else {
                    user.setEmail(userId + "@example.org"); //$NON-NLS-1$
                }
                user.setJoinedOn(new Date());
                try {
                    idmStorage.createUser(user);
                } catch (StorageException e1) {
                    throw new SystemErrorException(e1);
                }
                rval.initFromUser(user);
                rval.setAdmin(securityContext.isAdmin());
                rval.setPermissions(new HashSet<PermissionBean>());
            } else {
                rval.initFromUser(user);
                Set<PermissionBean> permissions = idmStorage.getPermissions(userId);
                rval.setPermissions(permissions);
                rval.setAdmin(securityContext.isAdmin());
            }

            log.debug(String.format("Getting info for user %s", user.getUsername())); //$NON-NLS-1$
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#updateInfo(com.t1t.digipolis.apim.beans.idm.UpdateUserBean)
     */
    @Override
    public void updateInfo(UpdateUserBean info) {
        try {
            UserBean user = idmStorage.getUser(securityContext.getCurrentUser());
            if (user == null) {
                throw new StorageException("User not found: " + securityContext.getCurrentUser()); //$NON-NLS-1$
            }
            if (info.getEmail() != null) {
                user.setEmail(info.getEmail());
            }
            if (info.getFullName() != null) {
                user.setFullName(info.getFullName());
            }
            idmStorage.updateUser(user);

            log.debug(String.format("Successfully updated user %s: %s", user.getUsername(), user)); //$NON-NLS-1$
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#getAppOrganizations()
     */
    @Override
    public List<OrganizationSummaryBean> getAppOrganizations() {
        Set<String> permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.appEdit);
        try {
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#getPlanOrganizations()
     */
    @Override
    public List<OrganizationSummaryBean> getPlanOrganizations() {
        Set<String> permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.planEdit);
        try {
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#getServiceOrganizations()
     */
    @Override
    public List<OrganizationSummaryBean> getServiceOrganizations() {
        Set<String> permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.svcEdit);
        try {
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#getApplications()
     */
    @Override
    public List<ApplicationSummaryBean> getApplications() {
        Set<String> permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.appView);
        try {
            return query.getApplicationsInOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.ICurrentUserResource#getServices()
     */
    @Override
    public List<ServiceSummaryBean> getServices() {
        Set<String> permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.svcView);
        try {
            return query.getServicesInOrgs(permittedOrganizations);
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
     * @return the query
     */
    public IStorageQuery getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(IStorageQuery query) {
        this.query = query;
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
