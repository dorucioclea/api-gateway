package com.t1t.apim.facades;

import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchCriteriaFilterBean;
import com.t1t.apim.beans.search.SearchCriteriaFilterOperator;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.services.ServiceStatus;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.services.ServiceVersionWithMarketInfoBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.core.IMetricsAccessor;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
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

    @Inject private IStorageQuery query;
    //@Inject private IMetricsAccessor metrics;
    @Inject private ISecurityAppContext appContext;

    private static final String NAME = "name";
    private static final String STATUS = "status";

    private SearchCriteriaFilterBean getAppContextFilter(){
        SearchCriteriaFilterBean appContextFilter = new SearchCriteriaFilterBean();
        appContextFilter.setName("context");
        appContextFilter.setValue(appContext.getApplicationPrefix());
        appContextFilter.setOperator(SearchCriteriaFilterOperator.eq);
        return appContextFilter;
    }

    public SearchResultsBean<OrganizationSummaryBean> searchOrgs(SearchCriteriaBean criteria) {
        try {
            criteria.getFilters().add(getAppContextFilter());
            SearchResultsBean<OrganizationSummaryBean> results = query.findOrganizations(criteria);
            //Enrich the beans with counters
            for (OrganizationSummaryBean summary : results.getBeans()) {
                summary.setNumApps(query.getRegisteredApplicationCountForOrg(summary.getId()));
                summary.setNumPlans(query.getLockedPlanCountForOrg(summary.getId()));
                summary.setNumMembers(query.getMemberCountForOrg(summary.getId()));
                summary.setNumServices(query.getPublishedServiceCountForOrg(summary.getId()));
                summary.setNumEvents(query.getEventCountForOrg(summary.getId()));
            }
            return results;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<ApplicationSummaryBean> searchApps(SearchCriteriaBean criteria) {
        try {
            criteria.getFilters().add(getAppContextFilter());
            return query.findApplications(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria) {
        //temporary solution - Service contains no visibility option, thus we return modified service versions
        try {
            Set<ServiceSummaryBean> resultServices = new TreeSet<>();
            List<ServiceVersionBean> serviceByStatus = new ArrayList<>();
            for (SearchCriteriaFilterBean filter : criteria.getFilters()) {
                if (filter.getName().equalsIgnoreCase("name")) {
                    serviceByStatus.addAll(query.findPublishedServiceVersionsByServiceName(filter.getValue().toLowerCase()));
                }
            }
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

    public List<ServiceVersionWithMarketInfoBean> searchServicesByStatus(ServiceStatus status){
        try {
            return enrichServiceVersionsWithMarketInfo(query.findServiceByStatus(status));
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
            return query.findAllUniquePublishedCategories();
        } catch (StorageException e) {
            throw new SystemErrorException();
        }
    }

    public List<ServiceVersionBean> searchServicesPublishedInCategories(List<String> categories){
        try {
            Set<String> consentPrefixes = new HashSet<>();
            consentPrefixes.addAll(query.getManagedAppPrefixesForTypes(Collections.singletonList(ManagedApplicationTypes.Consent)));
            List<ServiceVersionBean> rval = query.findAllServicesWithCategory(categories);
            //TODO - Remove provision key
            /*for (ServiceVersionBean svb : rval) {
                if (!(securityContext.hasPermission(PermissionType.svcEdit, svb.getService().getOrganization().getId()) || consentPrefixes.contains(appContext.getApplicationPrefix()))) {
                    svb.setProvisionKey(null);
                }
            }*/
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException();
        }
    }

    public List<String> findServiceVersionEndpointsForMarketplaceType(ManagedApplicationTypes type) {
        List<String> returnValue = new ArrayList<>();
        try {
            final List<ManagedApplicationBean> manappList = query.findManagedApplication(type);

            if (manappList==null) {
                throw ExceptionFactory.availabilityNotFoundException();
            }

            for(ManagedApplicationBean mb:manappList){
                List<ServiceVersionBean> svbs = query.findServiceVersionsByAvailability(mb.getPrefix());
                svbs.forEach(sv -> {
                    returnValue.add(new StringBuilder("/")
                            .append(sv.getService().getOrganization().getId().toLowerCase())
                            .append("/")
                            .append(sv.getService().getId().toLowerCase())
                            .append("/")
                            .append(sv.getVersion().toLowerCase())
                            .append("/")
                            .toString());
                });
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
        return returnValue;
    }

    public SearchResultsBean<ServiceVersionWithMarketInfoBean> searchLatestServiceVersions(SearchCriteriaBean criteria) {
        //TODO - paging
        //TODO - take operator into account. Right now we assume that searches on name use "like" and on status use "eq"
        SearchResultsBean<ServiceVersionWithMarketInfoBean> rval = new SearchResultsBean<>();
        String name = null;
        ServiceStatus status = null;
        for (SearchCriteriaFilterBean filter : criteria.getFilters()) {
            switch(filter.getName()) {
                case NAME:
                    name = filter.getValue();
                    break;
                case STATUS:
                    try {
                        status = ServiceStatus.valueOf(filter.getValue());
                    }
                    catch (IllegalArgumentException ex) {
                        throw ExceptionFactory.invalidServiceStatusException();
                    }
                    break;
                default:
                    throw ExceptionFactory.invalidSearchCriteriaException(filter.getName());
            }
        }
        List<ServiceVersionWithMarketInfoBean> svmibs = new ArrayList<>();
        try {
            if (status != null) {
                if (name != null) {
                    svmibs.addAll(
                            enrichServiceVersionsWithMarketInfo(query.findLatestServiceVersionByStatusAndServiceName(name, status)));
                }
                else {
                    svmibs.addAll(
                            enrichServiceVersionsWithMarketInfo(query.findLatestServiceVersionByStatus(status)));
                }
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
        rval.setBeans(svmibs);
        rval.setTotalSize(svmibs.size());
        return rval;
    }

    public List<ServiceVersionWithMarketInfoBean> searchLatestPublishedServiceVersionsInCategory(List<String> category) {
        try {
            return enrichServiceVersionsWithMarketInfo(query.findLatestServicesWithCategory(category));
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private List<ServiceVersionWithMarketInfoBean> enrichServiceVersionsWithMarketInfo(List<ServiceVersionBean> svbs) {
        List<ServiceVersionWithMarketInfoBean> svmibs = new ArrayList<>();
        svbs.forEach(svb -> {
            ServiceVersionWithMarketInfoBean svmib = new ServiceVersionWithMarketInfoBean(svb);
            //TODO - Implement fail silent
            svmib.setMarketInfo(null);
            svmibs.add(svmib);
        });
        return svmibs;
    }

    public ApplicationVersionSummaryBean resolveApiKey(String apikey) {
        try {
            return query.resolveApplicationVersionByAPIKey(apikey);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }
}
