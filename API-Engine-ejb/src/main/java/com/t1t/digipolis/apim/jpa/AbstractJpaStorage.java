package com.t1t.digipolis.apim.jpa;

import com.t1t.digipolis.apim.beans.orgs.OrganizationBasedCompositeId;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.search.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.slf4j.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class that JPA storage impls can extend.
 */
public abstract class AbstractJpaStorage {

    @Inject
    @APIEngineContext
    private Logger log;

    @Inject
    @APIEngineContext
    private EntityManager em;

    /**
     * Constructor.
     */
    public AbstractJpaStorage() {
    }

    /**
     * @return the thread's entity manager
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    protected EntityManager getActiveEntityManager() throws StorageException {
        return em;
    }

    /**
     * @param bean the bean to create
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T create(T bean) throws StorageException {
        log.debug("create:" + bean.toString());
        em.persist(bean);
        em.flush();
        return bean;
    }

    /**
     * @param bean the bean to update
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T update(T bean) throws StorageException {
        log.debug("update:" + bean.toString());
        T result = em.merge(bean);
        em.flush();
        return result;
    }

    /**
     * Delete using bean
     *
     * @param bean the bean to delete
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> void delete(T bean) throws StorageException {
        log.debug("delete:" + bean.toString());
        em.remove(em.merge(bean));
    }

    /**
     * Get object of type T
     *
     * @param id   identity key
     * @param type class of type T
     * @return Instance of type T
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T get(Long id, Class<T> type) throws StorageException {
        log.debug("get(long:id):" + id.toString());
        T rval = null;
        rval = em.find(type, id);
        return rval;
    }

    /**
     * Get object of type T
     *
     * @param id   identity key
     * @param type class of type T
     * @return Instance of type T
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    public <T> T get(String id, Class<T> type) throws StorageException {
        log.debug("get(string:id):" + id.toString());
        T rval = null;
        rval = em.find(type, id);
        return rval;
    }

    /**
     * Get object of type T
     *
     * @param organizationId org id
     * @param id             identity
     * @param type           class of type T
     * @return Instance of type T
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    public <T> T get(String organizationId, String id, Class<T> type) throws StorageException {
        log.debug("getOrganzationComposite(id):" + id.toString());
        T rval = null;
        OrganizationBean orgBean = em.find(OrganizationBean.class, organizationId);
        Object key = new OrganizationBasedCompositeId(orgBean, id);
        rval = em.find(type, key);
        return rval;
    }

    /**
     * Get a list of entities based on the provided criteria and entity type.
     *
     * @param criteria
     * @param type
     * @throws StorageException if a storage problem occurs while storing a bean
     */
    protected <T> SearchResultsBean<T> find(SearchCriteriaBean criteria, Class<T> type) throws StorageException {
        SearchResultsBean<T> results = new SearchResultsBean<>();
        // Set some default in the case that paging information was not included in the request.
        PagingBean paging = criteria.getPaging();
        if (paging == null) {
            paging = new PagingBean();
            paging.setPage(1);
            paging.setPageSize(20);
        }
        int page = paging.getPage();
        int pageSize = paging.getPageSize();
        int start = (page - 1) * pageSize;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        applySearchCriteriaToQuery(criteria, builder, criteriaQuery, from, false);
        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(pageSize + 1);
        boolean hasMore = false;

        // Now query for the actual results
        List<T> resultList = typedQuery.getResultList();

        // Check if we got back more than we actually needed.
        if (resultList.size() > pageSize) {
            resultList.remove(resultList.size() - 1);
            hasMore = true;
        }

        // If there are more results than we needed, then we will need to do another
        // query to determine how many rows there are in total
        int totalSize = start + resultList.size();
        if (hasMore) {
            totalSize = executeCountQuery(criteria, em, type);
        }
        results.setTotalSize(totalSize);
        results.setBeans(resultList);
        return results;
    }

    /**
     * Gets a count of the number of rows that would be returned by the search.
     *
     * @param criteria
     * @param entityManager
     * @param type
     */
    protected <T> int executeCountQuery(SearchCriteriaBean criteria, EntityManager entityManager, Class<T> type) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> from = countQuery.from(type);
        countQuery.select(builder.count(from));
        applySearchCriteriaToQuery(criteria, builder, countQuery, from, true);
        TypedQuery<Long> query = entityManager.createQuery(countQuery);
        return query.getSingleResult().intValue();
    }

    /**
     * Applies the criteria found in the {@link SearchCriteriaBean} to the JPA query.
     *
     * @param criteria
     * @param builder
     * @param query
     * @param from
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <T> void applySearchCriteriaToQuery(SearchCriteriaBean criteria, CriteriaBuilder builder, CriteriaQuery<?> query, Root<T> from, boolean countOnly) {
        List<SearchCriteriaFilterBean> filters = criteria.getFilters();
        if (filters != null && !filters.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchCriteriaFilterBean filter : filters) {
                if (filter.getOperator() == SearchCriteriaFilterOperator.eq) {
                    Path<Object> path = from.get(filter.getName());
                    Class<?> pathc = path.getJavaType();
                    if (pathc.isAssignableFrom(String.class)) {
                        predicates.add(builder.equal(path, filter.getValue()));
                    } else if (pathc.isEnum()) {
                        predicates.add(builder.equal(path, Enum.valueOf((Class) pathc, filter.getValue())));
                    }
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.bool_eq) {
                    predicates.add(builder.equal(from.<Boolean>get(filter.getName()), Boolean.valueOf(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.gt) {
                    predicates.add(builder.greaterThan(from.<Long>get(filter.getName()), new Long(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.gte) {
                    predicates.add(builder.greaterThanOrEqualTo(from.<Long>get(filter.getName()), new Long(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.lt) {
                    predicates.add(builder.lessThan(from.<Long>get(filter.getName()), new Long(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.lte) {
                    predicates.add(builder.lessThanOrEqualTo(from.<Long>get(filter.getName()), new Long(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.neq) {
                    predicates.add(builder.notEqual(from.get(filter.getName()), filter.getValue()));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.like) {
                    predicates.add(builder.like(builder.upper(from.<String>get(filter.getName())), filter.getValue().toUpperCase().replace('*', '%')));
                }
            }
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        OrderByBean orderBy = criteria.getOrderBy();
        if (orderBy != null && !countOnly) {
            if (orderBy.isAscending()) {
                query.orderBy(builder.asc(from.get(orderBy.getName())));
            } else {
                query.orderBy(builder.desc(from.get(orderBy.getName())));
            }
        }
    }
}
