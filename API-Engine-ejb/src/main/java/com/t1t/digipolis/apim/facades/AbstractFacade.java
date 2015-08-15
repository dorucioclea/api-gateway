package com.t1t.digipolis.apim.facades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Michallis
 */
public abstract class AbstractFacade<T> {


    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Create new entity, managed entity after creation
     *
     * @param entity
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T create(T entity) {
        log("create", entity);
        getEntityManager().persist(entity);
        getEntityManager().flush();
        log("created", entity);
        return entity;
    }

    /**
     * Merge managed entity
     *
     * @param entity
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T edit(T entity) {
        log("edit: ", entity);
        T t = getEntityManager().merge(entity);
        getEntityManager().flush();
        return t;
    }

    /**
     * Remove managed entity
     *
     * @param entity
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void remove(T entity) {
        log("remove", entity);
        if (entity != null) {
            getEntityManager().remove(getEntityManager().merge(entity));
            log("removed", entity);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeById(Object id) {
        remove(find(id));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeAll() {
        List<T> all = findAll();
        for (T t : all) {
            remove(t);
        }
    }

    /**
     * Find entity and add the entity to persistence context
     *
     * @param id
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T find(Object id) {
        log("find", null);
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Gets a reference to an entity
     *
     * @param id
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T getReference(Object id) {
        log("reference", null);
        return getEntityManager().getReference(entityClass, id);
    }

    /**
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> findAll() {
        log("find all", null);
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Find a range of entities Can be used with a paginator pattern or a fast
     * lane
     *
     * @param range
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @SuppressWarnings("unchecked")
    public List<T> findRange(int[] range) {
        log("find range", null);
        @SuppressWarnings("rawtypes")
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Count
     *
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @SuppressWarnings("unchecked")
    public int count() {
        @SuppressWarnings("rawtypes")
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Utility log method
     *
     * @param logMessage
     * @param entity
     */
    private void log(String logMessage, T entity) {
        if (entity != null) {
            LOG.debug("@ {} : logMessage", entity.getClass());
        } else {
            LOG.debug(logMessage);
        }
    }

}
