/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.t1t.digipolis.apim.rest.impl;

import io.apiman.manager.api.beans.BeanUtils;
import io.apiman.manager.api.beans.idm.NewRoleBean;
import io.apiman.manager.api.beans.idm.RoleBean;
import io.apiman.manager.api.beans.idm.UpdateRoleBean;
import io.apiman.manager.api.beans.search.SearchCriteriaBean;
import io.apiman.manager.api.beans.search.SearchResultsBean;
import io.apiman.manager.api.core.IIdmStorage;
import io.apiman.manager.api.core.exceptions.StorageException;
import io.apiman.manager.api.rest.contract.IRoleResource;
import io.apiman.manager.api.rest.contract.exceptions.*;
import io.apiman.manager.api.rest.impl.util.ExceptionFactory;
import io.apiman.manager.api.rest.impl.util.SearchCriteriaUtil;
import io.apiman.manager.api.security.ISecurityContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the Role API.
 * 
 * @author eric.wittmann@redhat.com
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
     * @see io.apiman.manager.api.rest.contract.IRoleResource#create(io.apiman.manager.api.beans.idm.NewRoleBean)
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
     * @see io.apiman.manager.api.rest.contract.IRoleResource#get(String)
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
     * @see io.apiman.manager.api.rest.contract.IRoleResource#update(String, io.apiman.manager.api.beans.idm.UpdateRoleBean)
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
     * @see io.apiman.manager.api.rest.contract.IRoleResource#delete(String)
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
     * @see io.apiman.manager.api.rest.contract.IRoleResource#list()
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
     * @see io.apiman.manager.api.rest.contract.IRoleResource#search(io.apiman.manager.api.beans.search.SearchCriteriaBean)
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
