package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.rest.impl.util.SearchCriteriaUtil;
import com.t1t.digipolis.apim.rest.resources.ISearchResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.rest.resources.exceptions.OrganizationNotFoundException;
import com.t1t.digipolis.apim.rest.resources.exceptions.SystemErrorException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Implementation of the Search API.
 */
@ApplicationScoped
public class SearchResourceImpl implements ISearchResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    
    /**
     * Constructor.
     */
    public SearchResourceImpl() {
    }
    
    /**
     * @see ISearchResource#searchOrgs(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<OrganizationSummaryBean> searchOrgs(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException {
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        try {
            return query.findOrganizations(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
    
    /**
     * @see ISearchResource#searchApps(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<ApplicationSummaryBean> searchApps(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException {
        // TODO only return applications that the user is permitted to see?
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        try {
            return query.findApplications(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see ISearchResource#searchServices(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException {
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        try {
            return query.findServices(criteria);
        } catch (StorageException e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }
}
