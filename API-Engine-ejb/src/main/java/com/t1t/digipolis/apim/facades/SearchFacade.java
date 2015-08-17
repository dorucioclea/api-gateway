package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SearchFacade {
    @Inject
    @APIEngineContext
    private Logger log;
    @Inject
    @APIEngineContext
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
        try {
            return query.findServices(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }
}
