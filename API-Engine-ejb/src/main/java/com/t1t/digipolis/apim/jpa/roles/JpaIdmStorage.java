package com.t1t.digipolis.apim.jpa.roles;

import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.jpa.AbstractJpaStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A JPA implementation of the role storage interface {@link com.t1t.digipolis.apim.core.IIdmStorage}.
 *
 */
@ApplicationScoped @Alternative
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
        beginTx();
        try {
            super.create(user);
            commitTx();
        } catch (StorageException e) {
            rollbackTx();
            throw e;
        }
    }

    /**
     * @see IIdmStorage#getUser(String)
     */
    @Override
    public UserBean getUser(String userId) throws StorageException {
        beginTx();
        try {
            return super.get(userId, UserBean.class);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#updateUser(UserBean)
     */
    @Override
    public void updateUser(UserBean user) throws StorageException {
        beginTx();
        try {
            super.update(user);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#findUsers(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<UserBean> findUsers(SearchCriteriaBean criteria) throws StorageException {
        beginTx();
        try {
            return super.find(criteria, UserBean.class);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#createRole(RoleBean)
     */
    @Override
    public void createRole(RoleBean role) throws StorageException {
        beginTx();
        try {
            super.create(role);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#updateRole(RoleBean)
     */
    @Override
    public void updateRole(RoleBean role) throws StorageException {
        beginTx();
        try {
            super.update(role);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#deleteRole(RoleBean)
     */
    @Override
    public void deleteRole(RoleBean role) throws StorageException {
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();

            RoleBean prole = get(role.getId(), RoleBean.class);

            // First delete all memberships in this role
            Query query = entityManager.createQuery("DELETE from RoleMembershipBean m WHERE m.roleId = :roleId" ); //$NON-NLS-1$
            query.setParameter("roleId", role.getId()); //$NON-NLS-1$
            query.executeUpdate();

            // Then delete the role itself.
            super.delete(prole);

            commitTx();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#getRole(String)
     */
    @Override
    public RoleBean getRole(String roleId) throws StorageException {
        beginTx();
        try {
            return getRoleInternal(roleId);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#findRoles(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<RoleBean> findRoles(SearchCriteriaBean criteria) throws StorageException {
        beginTx();
        try {
            return super.find(criteria, RoleBean.class);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#createMembership(RoleMembershipBean)
     */
    @Override
    public void createMembership(RoleMembershipBean membership) throws StorageException {
        beginTx();
        try {
            super.create(membership);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IIdmStorage#getMembership(String, String, String)
     */
    @Override
    public RoleMembershipBean getMembership(String userId, String roleId, String organizationId) throws StorageException {
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            Query query = entityManager.createQuery("SELECT m FROM RoleMembershipBean m WHERE m.roleId = :roleId AND m.userId = :userId AND m.organizationId = :orgId" ); //$NON-NLS-1$
            query.setParameter("roleId", roleId); //$NON-NLS-1$
            query.setParameter("userId", userId); //$NON-NLS-1$
            query.setParameter("orgId", organizationId); //$NON-NLS-1$
            RoleMembershipBean bean = null;
            List<?> resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                bean = (RoleMembershipBean) resultList.iterator().next();
            }
            commitTx();
            return bean;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#deleteMembership(String, String, String)
     */
    @Override
    public void deleteMembership(String userId, String roleId, String organizationId) throws StorageException {
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            Query query = entityManager.createQuery("DELETE FROM RoleMembershipBean m WHERE m.roleId = :roleId AND m.userId = :userId AND m.organizationId = :orgId" ); //$NON-NLS-1$
            query.setParameter("roleId", roleId); //$NON-NLS-1$
            query.setParameter("userId", userId); //$NON-NLS-1$
            query.setParameter("orgId", organizationId); //$NON-NLS-1$
            query.executeUpdate();
            commitTx();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#deleteMemberships(String, String)
     */
    @Override
    public void deleteMemberships(String userId, String organizationId) throws StorageException {
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            Query query = entityManager.createQuery("DELETE FROM RoleMembershipBean m WHERE m.userId = :userId AND m.organizationId = :orgId" ); //$NON-NLS-1$
            query.setParameter("userId", userId); //$NON-NLS-1$
            query.setParameter("orgId", organizationId); //$NON-NLS-1$
            query.executeUpdate();
            commitTx();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#getUserMemberships(String)
     */
    @Override
    public Set<RoleMembershipBean> getUserMemberships(String userId) throws StorageException {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
            Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
            criteriaQuery.where(builder.equal(from.get("userId"), userId)); //$NON-NLS-1$
            TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
            List<RoleMembershipBean> resultList = typedQuery.getResultList();
            memberships.addAll(resultList);
            commitTx();
            return memberships;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#getUserMemberships(String, String)
     */
    @Override
    public Set<RoleMembershipBean> getUserMemberships(String userId, String organizationId) throws StorageException {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
            Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
            criteriaQuery.where(
                    builder.equal(from.get("userId"), userId), //$NON-NLS-1$
                    builder.equal(from.get("organizationId"), organizationId) ); //$NON-NLS-1$
            TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
            List<RoleMembershipBean> resultList = typedQuery.getResultList();
            memberships.addAll(resultList);
            commitTx();
            return memberships;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#getOrgMemberships(String)
     */
    @Override
    public Set<RoleMembershipBean> getOrgMemberships(String organizationId) throws StorageException {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<RoleMembershipBean> criteriaQuery = builder.createQuery(RoleMembershipBean.class);
            Root<RoleMembershipBean> from = criteriaQuery.from(RoleMembershipBean.class);
            criteriaQuery.where(builder.equal(from.get("organizationId"), organizationId)); //$NON-NLS-1$
            TypedQuery<RoleMembershipBean> typedQuery = entityManager.createQuery(criteriaQuery);
            List<RoleMembershipBean> resultList = typedQuery.getResultList();
            memberships.addAll(resultList);
            commitTx();
            return memberships;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
    }

    /**
     * @see IIdmStorage#getPermissions(String)
     */
    @Override
    public Set<PermissionBean> getPermissions(String userId) throws StorageException {
        Set<PermissionBean> permissions = new HashSet<>();
        beginTx();
        try {
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
            commitTx();
            return permissions;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            rollbackTx();
            throw new StorageException(t);
        }
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
