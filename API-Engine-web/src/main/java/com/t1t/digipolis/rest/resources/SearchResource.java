package com.t1t.digipolis.rest.resources;

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
import com.t1t.digipolis.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.exceptions.OrganizationNotFoundException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/search", description = "The Search API.")
@Path("/search")
@ApplicationScoped
public class SearchResource implements ISearchResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject @APIEngineContext
    Logger log;
    /**
     * Constructor.
     */
    public SearchResource() {
    }

    @ApiOperation(value = "Search for Organizations",
            notes = "Use this endpoint to search for organizations.  The search criteria is provided in the body of the request, including filters, order-by, and paging information.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "The search results (a page of organizations)")
    })
    @POST
    @Path("/organizations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<OrganizationSummaryBean> searchOrgs(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException {
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        try {
            return query.findOrganizations(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Search for Organizations",
            notes = "Use this endpoint to search for applications.  The search criteria is provided in the body of the request, including filters, order-by, and paging information.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "The search results (a page of applications).")
    })
    @POST
    @Path("/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

    @ApiOperation(value = "Search for Services",
            notes = "Use this endpoint to search for services.  The search criteria is provided in the body of the request, including filters, order-by, and paging information.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "The search results (a page of services).")
    })
    @POST
    @Path("/services")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException {
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        try {
            return query.findServices(criteria);
        } catch (StorageException e) {
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
