package com.t1t.digipolis.apim.jpa;

import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBasedCompositeId;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.search.*;
import com.t1t.digipolis.apim.beans.services.ServiceBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import org.opensaml.xml.encryption.P;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A base class that JPA storage impls can extend.
 */
public abstract class AbstractJpaStorage {
    private static Logger log = LoggerFactory.getLogger(AbstractJpaStorage.class.getName());

    @PersistenceContext protected EntityManager em;

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
        log.trace("create:" + bean.toString());
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
        log.trace("update:" + bean.toString());
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
        log.trace("delete:" + bean);
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
        log.trace("get(long:id):" + id);
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
        log.trace("get(string:id):" + id);
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
        log.trace("getOrganzationComposite(id):" + id);
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

    protected List<ServiceVersionBean> findAllServicesByStatus(ServiceStatus status){
        Query query = em.createQuery("SELECT e FROM ServiceVersionBean e WHERE e.status LIKE :status").setParameter("status", status);
        return (List<ServiceVersionBean>) query.getResultList();
    }

    protected List<ServiceBean> findAllServiceDefinitions(){
        Query query = em.createQuery("SELECT e FROM ServiceBean e");
        return (List<ServiceBean>) query.getResultList();
    }

    protected List<ServiceVersionBean> findAllServiceVersionsInCategory(List<String> categories)throws StorageException{
        List<ServiceBean>services = findAllServiceDefinitions();
        List<ServiceBean> filteredServices = new ArrayList<>();
        for (ServiceBean service:services){
            for(String cat:categories){
                if(service.getCategories().contains(cat))filteredServices.add(service);
            }
        }
        //get all service verisons with parent id
        Query query = em.createQuery("SELECT e FROM ServiceVersionBean e WHERE e.service IN :services AND e.status = :status").setParameter("services",filteredServices).setParameter("status",ServiceStatus.Published);
        return (List<ServiceVersionBean>) query.getResultList();
    }

    protected List<ServiceVersionBean> findLatestPublishedServiceVersionsInCategory(List<String> categories) throws StorageException {
        List<ServiceBean> services = findAllServiceDefinitions();
        List<ServiceBean> filteredServices = new ArrayList<>();
        for (ServiceBean service : services) {
            for (String cat : categories) {
                if (service.getCategories().contains(cat)) {
                    filteredServices.add(service);
                }
            }
        }
        return em.createQuery("SELECT s FROM ServiceVersionBean s WHERE s.createdOn IN (SELECT MAX(s2.createdOn) FROM ServiceVersionBean s2 WHERE s2.service IN :services AND s2.status = :status GROUP BY s2.service)")
                .setParameter("services", filteredServices)
                .setParameter("status", ServiceStatus.Published)
                .getResultList();
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
