package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.idm.NewRoleBean;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.idm.UpdateRoleBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RoleFacade {
    private static Logger log = LoggerFactory.getLogger(RoleFacade.class.getName());
    @PersistenceContext
    private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private IIdmStorage idmStorage;

    public RoleBean create(NewRoleBean bean){
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

    public RoleBean get(String roleId){
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

    public void update(String roleId, UpdateRoleBean bean){
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

    public void delete(String roleId){
        RoleBean bean = get(roleId);
        try {
            idmStorage.deleteRole(bean);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<RoleBean> list(){
        try {
            SearchCriteriaBean criteria = new SearchCriteriaBean();
            criteria.setOrder("name", true); //$NON-NLS-1$
            return idmStorage.findRoles(criteria).getBeans();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<RoleBean> search(SearchCriteriaBean criteria){
        try {
            return idmStorage.findRoles(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
}
