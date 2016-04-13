package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
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
import java.util.*;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SearchFacade {
    private static Logger log = LoggerFactory.getLogger(SearchFacade.class.getName());

    @PersistenceContext
    private EntityManager em;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;

    public SearchResultsBean<OrganizationSummaryBean> searchOrgs(SearchCriteriaBean criteria) {
        try {
            return query.findOrganizations(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<ApplicationSummaryBean> searchApps(SearchCriteriaBean criteria) {
        try {
            return query.findApplications(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria) {
        //TODO: temporary solution - Service contians no visibility option, thus we return modified service versions
        try {
            //we store records in sorted set, otherwise we'll have duplicates
            Set<ServiceSummaryBean> resultServices = new TreeSet<>();
            List<ServiceVersionBean> serviceByStatus = query.findServiceByStatus(ServiceStatus.Published);
            serviceByStatus.forEach(serviceVersionBean -> {
                ServiceSummaryBean summBean = new ServiceSummaryBean();
                summBean.setCreatedOn(serviceVersionBean.getService().getCreatedOn());
                summBean.setDescription(serviceVersionBean.getService().getDescription());
                summBean.setId(serviceVersionBean.getService().getId());
                summBean.setName(serviceVersionBean.getService().getName());
                summBean.setOrganizationId(serviceVersionBean.getService().getOrganization().getId());
                summBean.setOrganizationName(serviceVersionBean.getService().getOrganization().getName());
                resultServices.add(summBean);
            });
            SearchResultsBean<ServiceSummaryBean> searchResult = new SearchResultsBean<>();
            List<ServiceSummaryBean> uniqueServiceList = new ArrayList<>();
            uniqueServiceList.addAll(resultServices);
            searchResult.setBeans(uniqueServiceList);
            searchResult.setTotalSize(resultServices.size());
            return searchResult;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServiceVersionBean> searchServicesByStatus(ServiceStatus status){
        try {
            return query.findServiceByStatus(status);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public Set<String> searchCategories(){
        try {
            return query.findAllUniqueCategories();
        } catch (StorageException e) {
            throw new SystemErrorException();
        }
    }

    public Set<String> searchPublishedCategories(){
        try {
            //TODO take into account int/ext or scopes in general
            return query.findAllUniquePublishedCategories();
        } catch (StorageException e) {
            throw new SystemErrorException();
        }
    }

    public List<ServiceVersionBean> searchServicesPublishedInCategories(List<String> categories){
        try {
            return query.findAllServicesWithCategory(categories);
        } catch (StorageException e) {
            throw new SystemErrorException();
        }
    }
}
