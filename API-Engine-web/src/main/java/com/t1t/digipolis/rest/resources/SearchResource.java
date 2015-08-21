package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.categories.CategorySearchBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.exceptions.OrganizationNotFoundException;
import com.t1t.digipolis.apim.facades.SearchFacade;
import com.t1t.digipolis.apim.rest.impl.util.SearchCriteriaUtil;
import com.t1t.digipolis.apim.rest.resources.ISearchResource;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jboss.resteasy.annotations.Body;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@Api(value = "/search", description = "The Search API.")
@Path("/search")
@ApplicationScoped
public class SearchResource implements ISearchResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    @APIEngineContext
    Logger log;
    @Inject
    private SearchFacade searchFacade;

    /**
     * Constructor.
     */
    public SearchResource() {
    }

    @ApiOperation(value = "Search for Organizations",
            notes = "Use this endpoint to search for organizations.  The search criteria is provided in the body of the request, including filters, order-by, and paging information. Possible values are: publishService, retireService, registerApplication, unregisterApplciation, lockPlan")
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
        return searchFacade.searchOrgs(criteria);
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
    public SearchResultsBean<ApplicationSummaryBean> searchApps(SearchCriteriaBean criteria) throws OrganizationNotFoundException, InvalidSearchCriteriaException {
        // TODO only return applications that the user is permitted to see?
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        return searchFacade.searchApps(criteria);
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
    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria) throws OrganizationNotFoundException, InvalidSearchCriteriaException {
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        return searchFacade.searchServices(criteria);
    }

    @ApiOperation(value = "Search for Services with a specific status",
            notes = "Use this endpoint to search for all services with given status. Possible values are: Created, Ready, Published, Retired")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceVersionBean.class, message = "If the search is successful.")
    })
    @GET
    @Path("/services/{status}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceVersionBean> searchServicesByLifecycle(@PathParam("status") ServiceStatus status) {
        Preconditions.checkNotNull(status);
        return searchFacade.searchServicesByStatus(status);
    }

    @ApiOperation(value = "Search for all used service categories",
            notes = "Use this endpoint to search for all service categories. All categories are returned, also for unpublished service versions.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = String.class, message = "If the search is successful.")
    })
    @GET
    @Path("/service/categories/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> searchCategories() {
        return searchFacade.searchCategories();
    }

    @ApiOperation(value = "Search for all Service versions within given category list",
            notes = "Use this endpoint to search for all service versions, having a category defined in the given category list.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceVersionBean.class, message = "If the search is successful.")
    })
    @POST
    @Path("/services/versions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceVersionBean> searchServiceVersionForCategories(CategorySearchBean catSearch) {
        return searchFacade.searchServicesPublishedInCategories(catSearch.getCategories());
    }
}
