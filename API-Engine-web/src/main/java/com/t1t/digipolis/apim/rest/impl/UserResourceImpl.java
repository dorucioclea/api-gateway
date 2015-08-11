package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.rest.resources.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.rest.resources.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.rest.resources.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the User API.
 */
@ApplicationScoped
public class UserResourceImpl implements IUserResource {
    
    @Inject
    private
    IStorage storage;
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IStorageQuery query;

    /**
     * Constructor.
     */
    public UserResourceImpl() {
    }

    /**
     * @see IUserResource#get(String)
     */
    @Override
    public UserBean get(String userId) throws UserNotFoundException {
        try {
            UserBean user = idmStorage.getUser(userId);
            if (user == null) {
                throw ExceptionFactory.userNotFoundException(userId);
            }
            return user;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IUserResource#update(String, UpdateUserBean)
     */
    @Override
    public void update(String userId, UpdateUserBean user) throws UserNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin() && !securityContext.getCurrentUser().equals(userId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            UserBean updatedUser = idmStorage.getUser(userId);
            if (updatedUser == null) {
                throw ExceptionFactory.userNotFoundException(userId);
            }
            if (user.getEmail() != null) {
                updatedUser.setEmail(user.getEmail());
            }
            if (user.getFullName() != null) {
                updatedUser.setFullName(user.getFullName());
            }
            idmStorage.updateUser(updatedUser);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IUserResource#search(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException {
        try {
            return idmStorage.findUsers(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IUserResource#getOrganizations(String)
     */
    @Override
    public List<OrganizationSummaryBean> getOrganizations(String userId) {
        Set<String> permittedOrganizations = new HashSet<>();
        try {
            Set<RoleMembershipBean> memberships = idmStorage.getUserMemberships(userId);
            for (RoleMembershipBean membership : memberships) {
                permittedOrganizations.add(membership.getOrganizationId());
            }
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IUserResource#getApplications(String)
     */
    @Override
    public List<ApplicationSummaryBean> getApplications(String userId) {
        Set<String> permittedOrganizations = new HashSet<>();
        try {
            Set<PermissionBean> permissions = idmStorage.getPermissions(userId);
            for (PermissionBean permission : permissions) {
                if (permission.getName() == PermissionType.appView) {
                    permittedOrganizations.add(permission.getOrganizationId());
                }
            }
            return query.getApplicationsInOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IUserResource#getServices(String)
     */
    @Override
    public List<ServiceSummaryBean> getServices(String userId) {
        Set<String> permittedOrganizations = new HashSet<>();
        try {
            Set<PermissionBean> permissions = idmStorage.getPermissions(userId);
            for (PermissionBean permission : permissions) {
                if (permission.getName() == PermissionType.svcView) {
                    permittedOrganizations.add(permission.getOrganizationId());
                }
            }
            return query.getServicesInOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }

    }

    /**
     * @see IUserResource#getActivity(String, int, int)
     */
    @Override
    public SearchResultsBean<AuditEntryBean> getActivity(String userId, int page, int pageSize) {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditUser(userId, paging);
            return rval;
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
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }
}
