package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.idm.NewRoleBean;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.idm.UpdateRoleBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.impl.util.SearchCriteriaUtil;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.security.ISecurityContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the Role API.
 */
@ApplicationScoped
public class RoleResourceImpl implements IRoleResource {
    
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    
    /**
     * Constructor.
     */
    public RoleResourceImpl() {
    }
    
    /**
     * @see IRoleResource#create(NewRoleBean)
     */
    @Override
    public RoleBean create(NewRoleBean bean) throws RoleAlreadyExistsException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();

        RoleBean role = new RoleBean();
        role.setAutoGrant(bean.getAutoGrant());
        role.setCreatedBy(securityContext.getCurrentUser());
        role.setCreatedOn(new Date());
        role.setDescription(bean.getDescription());
        role.setId(BeanUtils.idFromName(bean.getName()));
        role.setName(bean.getName());
        role.setPermissions(bean.getPermissions());
        try {
            if (idmStorage.getRole(role.getId()) != null) {
                throw ExceptionFactory.roleAlreadyExistsException(role.getId());
            }
            idmStorage.createRole(role);
            return role;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
    
    /**
     * @see IRoleResource#get(String)
     */
    @Override
    public RoleBean get(String roleId) throws RoleNotFoundException, NotAuthorizedException {
        try {
            RoleBean role = idmStorage.getRole(roleId);
            if (role == null) {
                throw ExceptionFactory.roleNotFoundException(roleId);
            }
            return role;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IRoleResource#update(String, UpdateRoleBean)
     */
    @Override
    public void update(String roleId, UpdateRoleBean bean) throws RoleNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            RoleBean role = idmStorage.getRole(roleId);
            if (role == null) {
                throw ExceptionFactory.roleNotFoundException(roleId);
            }
            if (bean.getDescription() != null) {
                role.setDescription(bean.getDescription());
            }
            if (bean.getAutoGrant() != null) {
                role.setAutoGrant(bean.getAutoGrant());
            }
            if (bean.getName() != null) {
                role.setName(bean.getName());
            }
            if (bean.getPermissions() != null) {
                role.getPermissions().clear();
                role.getPermissions().addAll(bean.getPermissions());
            }
            idmStorage.updateRole(role);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IRoleResource#delete(String)
     */
    @Override
    public void delete(String roleId) throws RoleNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        RoleBean bean = get(roleId);
        try {
            idmStorage.deleteRole(bean);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
    
    /**
     * @see IRoleResource#list()
     */
    @Override
    public List<RoleBean> list() throws NotAuthorizedException {
        try {
            SearchCriteriaBean criteria = new SearchCriteriaBean();
            criteria.setOrder("name", true); //$NON-NLS-1$
            return idmStorage.findRoles(criteria).getBeans();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
    
    /**
     * @see IRoleResource#search(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<RoleBean> search(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException, NotAuthorizedException {
        try {
            SearchCriteriaUtil.validateSearchCriteria(criteria);
            return idmStorage.findRoles(criteria);
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
