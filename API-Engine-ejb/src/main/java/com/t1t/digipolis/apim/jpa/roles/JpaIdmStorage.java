package com.t1t.digipolis.apim.jpa.roles;

import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.jpa.AbstractJpaStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A JPA implementation of the role storage interface {@link com.t1t.digipolis.apim.core.IIdmStorage}.
 */
@ApplicationScoped
@Default
public class JpaIdmStorage extends AbstractJpaStorage implements IIdmStorage {

    private static Logger logger = LoggerFactory.getLogger(JpaIdmStorage.class);

    /**
     * Constructor.
     */
    public JpaIdmStorage() {
    }

    /**
     * @see IIdmStorage#createUser(UserBean)
     */
    @Override
    public void createUser(UserBean user) throws StorageException {
        user.setJoinedOn(new Date());
        super.create(user);
    }

    @Override
    public void deleteUser(String userId) throws StorageException {
        final UserBean user = getUser(userId);
        super.delete(user);
    }

    /**
     * @see IIdmStorage#getUser(String)
     */
    @Override
    public UserBean getUser(String userId) throws StorageException {
        return super.get(userId, UserBean.class);
    }

    /**
     * @see IIdmStorage#updateUser(UserBean)
     */
    @Override
    public void updateUser(UserBean user) throws StorageException {
        super.update(user);
    }

    /**
     * @see IIdmStorage#findUsers(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<UserBean> findUsers(SearchCriteriaBean criteria) throws StorageException {
        return super.find(criteria, UserBean.class);
    }

    /**
     * @see IIdmStorage#createRole(RoleBean)
     */
    @Override
    public void createRole(RoleBean role) throws StorageException {
        super.create(role);
    }

    /**
     * @see IIdmStorage#updateRole(RoleBean)
     */
    @Override
    public void updateRole(RoleBean role) throws StorageException {
        super.update(role);
    }

    /**
     * @see IIdmStorage#deleteRole(RoleBean)
     */
    @Override
    public void deleteRole(RoleBean role) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();

        RoleBean prole = get(role.getId(), RoleBean.class);

        // First delete all memberships in this role
        Query query = entityManager.createQuery("DELETE from RoleMembershipBean m WHERE m.roleId = :roleId"); //$NON-NLS-1$
        query.setParameter("roleId", role.getId()); //$NON-NLS-1$
        query.executeUpdate();

        // Then delete the role itself.
        super.delete(prole);
    }

    @Override
    public UserBean getUserByMail(String mail) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        Query query = entityManager.createQuery("SELECT from UserBean u WHERE u.email = :mail").setParameter("mail", mail);
        return (UserBean) query.getResultList();
    }

    /**
     * @see IIdmStorage#getRole(String)
     */
    @Override
    public RoleBean getRole(String roleId) throws StorageException {
        return getRoleInternal(roleId);
    }

    /**
     * @see IIdmStorage#findRoles(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<RoleBean> findRoles(SearchCriteriaBean criteria) throws StorageException {
        return super.find(criteria, RoleBean.class);
    }

    /**
     * @see IIdmStorage#createMembership(RoleMembershipBean)
     */
    @Override
    public void createMembership(RoleMembershipBean membership) throws StorageException {
        super.create(membership);
    }

    /**
     * @see IIdmStorage#getMembership(String, String, String)
     */
    @Override
    public RoleMembershipBean getMembership(String userId, String roleId, String organizationId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        Query query = entityManager.createQuery("SELECT m FROM RoleMembershipBean m WHERE m.roleId = :roleId AND m.userId = :userId AND m.organizationId = :orgId"); //$NON-NLS-1$
        query.setParameter("roleId", roleId); //$NON-NLS-1$
        query.setParameter("userId", userId); //$NON-NLS-1$
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        RoleMembershipBean bean = null;
        List<?> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            bean = (RoleMembershipBean) resultList.iterator().next();
        }
        return bean;
    }

    /**
     * @see IIdmStorage#deleteMembership(String, String, String)
     */
    @Override
    public void deleteMembership(String userId, String roleId, String organizationId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        Query query = entityManager.createQuery("DELETE FROM RoleMembershipBean m WHERE m.roleId = :roleId AND m.userId = :userId AND m.organizationId = :orgId"); //$NON-NLS-1$
        query.setParameter("roleId", roleId); //$NON-NLS-1$
        query.setParameter("userId", userId); //$NON-NLS-1$
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        query.executeUpdate();
    }

    /**
     * @see IIdmStorage#deleteMemberships(String, String)
     */
    @Override
    public void deleteMemberships(String userId, String organizationId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        Query query = entityManager.createQuery("DELETE FROM RoleMembershipBean m WHERE m.userId = :userId AND m.organizationId = :orgId"); //$NON-NLS-1$
        query.setParameter("userId", userId); //$NON-NLS-1$
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        query.executeUpdate();
    }

    /**
     * @see IIdmStorage#getUserMemberships(String)
     */
    @Override
    public Set<RoleMembershipBean> getUserMemberships(String userId) throws StorageException {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        EntityManager entityManager = getActiveEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
        Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
        criteriaQuery.where(builder.equal(from.get("userId"), userId)); //$NON-NLS-1$
        TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
        List<RoleMembershipBean> resultList = typedQuery.getResultList();
        memberships.addAll(resultList);
        return memberships;
    }

    /**
     * @see IIdmStorage#getUserMemberships(String, String)
     */
    @Override
    public Set<RoleMembershipBean> getUserMemberships(String userId, String organizationId) throws StorageException {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        EntityManager entityManager = getActiveEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
        Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
        criteriaQuery.where(
                builder.equal(from.get("userId"), userId), //$NON-NLS-1$
                builder.equal(from.get("organizationId"), organizationId)); //$NON-NLS-1$
        TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
        List<RoleMembershipBean> resultList = typedQuery.getResultList();
        memberships.addAll(resultList);
        return memberships;
    }

    /**
     * @see IIdmStorage#getOrgMemberships(String)
     */
    @Override
    public Set<RoleMembershipBean> getOrgMemberships(String organizationId) throws StorageException {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        EntityManager entityManager = getActiveEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
        Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
        criteriaQuery.where(builder.equal(from.get("organizationId"), organizationId)); //$NON-NLS-1$
        TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
        List<RoleMembershipBean> resultList = typedQuery.getResultList();
        memberships.addAll(resultList);
        return memberships;
    }

    /**
     * @see IIdmStorage#getPermissions(String)
     */
    @Override
    public Set<PermissionBean> getPermissions(String userId) throws StorageException {
        Set<PermissionBean> permissions = new HashSet<>();
        EntityManager entityManager = getActiveEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
        Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
        criteriaQuery.where(builder.equal(from.get("userId"), userId)); //$NON-NLS-1$
        TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(500);
        List<RoleMembershipBean> resultList = typedQuery.getResultList();
        for (RoleMembershipBean membership : resultList) {
            RoleBean role = getRoleInternal(membership.getRoleId());
            String qualifier = membership.getOrganizationId();
            for (PermissionType permission : role.getPermissions()) {
                PermissionBean p = new PermissionBean();
                p.setName(permission);
                p.setOrganizationId(qualifier);
                permissions.add(p);
            }
        }
        return permissions;
    }

    public List<UserBean> getAllUsers() throws StorageException {
        EntityManager em = getActiveEntityManager();
        Query query = em.createQuery("SELECT u FROM UserBean u");
        return (List<UserBean>)query.getResultList();
    }

    public List<UserBean> getAdminUsers() throws StorageException {
        EntityManager em = getActiveEntityManager();
        Query query = em.createQuery("SELECT u FROM UserBean u WHERE u.admin = true");
        return (List<UserBean>)query.getResultList();
    }

    /**
     * @param roleId
     * @return a role by id
     * @throws StorageException
     */
    protected RoleBean getRoleInternal(String roleId) throws StorageException {
        return super.get(roleId, RoleBean.class);
    }

}
