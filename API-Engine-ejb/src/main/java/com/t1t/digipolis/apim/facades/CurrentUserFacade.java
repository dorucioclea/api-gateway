package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CurrentUserFacade {
    private static Logger log = LoggerFactory.getLogger(CurrentUserFacade.class.getName());
    @PersistenceContext
    private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private IStorageQuery query;
    @Inject private IIdmStorage idmStorage;
    @Inject private IStorage storage;

    public CurrentUserBean getInfo(){
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
                    user.setEmail(userId + "@example.org");
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
                if(securityContext.isAdmin()){
                    rval.setPermissions(idmStorage.getAllPermissions());
                }else{
                    rval.setPermissions(permissions);
                }
                rval.setAdmin(securityContext.isAdmin());
            }

            log.debug(String.format("Getting info for user %s", user.getUsername())); //$NON-NLS-1$
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updateInfo(UpdateUserBean info){
        try {
            String username = securityContext.getCurrentUser();
            UserBean user = idmStorage.getUser(securityContext.getCurrentUser());
            if (user == null) {
                throw new StorageException("User not found: " + securityContext.getCurrentUser()); //$NON-NLS-1$
            }
            if (info.getEmail() != null)user.setEmail(info.getEmail());
            if (info.getFullName() != null)user.setFullName(info.getFullName());
            if (info.getPic() != null)user.setBase64pic(info.getPic());else user.setBase64pic("");
            if (info.getCompany() != null) user.setCompany(info.getCompany());
            if (info.getLocation()!=null)user.setLocation(info.getLocation());
            if (info.getWebsite()!=null) user.setWebsite(info.getWebsite());
            if (info.getBio()!=null)user.setBio(info.getBio());

            idmStorage.updateUser(user);
            log.debug(String.format("Successfully updated user %s: %s", user.getUsername(), user)); //$NON-NLS-1$
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getAppOrganizations(){
        log.info("Getting organizations");
        String currentUser = securityContext.getCurrentUser();
        Set<String> permittedOrganizations = new TreeSet<>();
        try {
            log.info("currentuser:{}",currentUser);
            log.info("isadmin:{}",idmStorage.getUser(currentUser).getAdmin());
            if(!StringUtils.isEmpty(currentUser) && idmStorage.getUser(currentUser).getAdmin()){
                permittedOrganizations = storage.getAllOrganizations();
            }else{
                permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.appEdit);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.userNotFoundException(currentUser);
        }
        try {
            log.info("Permitted organizations:"+permittedOrganizations);
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getPlanOrganizations(){
        String currentUser = securityContext.getCurrentUser();
        Set<String> permittedOrganizations = new TreeSet<>();
        try {
            if(!StringUtils.isEmpty(currentUser) && idmStorage.getUser(currentUser).getAdmin()){
                permittedOrganizations = storage.getAllOrganizations();
            }else{
                permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.planEdit);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.userNotFoundException(currentUser);
        }
        try {
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getServiceOrganizations(){
        String currentUser = securityContext.getCurrentUser();
        Set<String> permittedOrganizations = new TreeSet<>();
        try {
            if(!StringUtils.isEmpty(currentUser) && idmStorage.getUser(currentUser).getAdmin()){
                permittedOrganizations = storage.getAllOrganizations();
            }else{
                permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.svcEdit);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.userNotFoundException(currentUser);
        }
        try {
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ApplicationSummaryBean> getApplications(){
        String currentUser = securityContext.getCurrentUser();
        Set<String> permittedOrganizations = new TreeSet<>();
        try {
            if(!StringUtils.isEmpty(currentUser) && idmStorage.getUser(currentUser).getAdmin()){
                permittedOrganizations = storage.getAllOrganizations();
            }else{
                permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.appView);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.userNotFoundException(currentUser);
        }
        try {
            return query.getApplicationsInOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServiceSummaryBean> getServices(){
        String currentUser = securityContext.getCurrentUser();
        Set<String> permittedOrganizations = new TreeSet<>();
        try {
            if(!StringUtils.isEmpty(currentUser) && idmStorage.getUser(currentUser).getAdmin()){
                permittedOrganizations = storage.getAllOrganizations();
            }else{
                permittedOrganizations = securityContext.getPermittedOrganizations(PermissionType.svcView);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.userNotFoundException(currentUser);
        }
        try {
            return query.getServicesInOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }



}
