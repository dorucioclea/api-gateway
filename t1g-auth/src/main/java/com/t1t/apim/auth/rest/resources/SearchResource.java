package com.t1t.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.apps.ApplicationBeanList;
import com.t1t.apim.beans.categories.CategorySearchBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.services.ServiceStatus;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.services.ServiceVersionWithMarketInfoBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.beans.summary.ServiceVersionSummaryBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.i18n.Messages;
import com.t1t.apim.exceptions.*;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.facades.OrganizationFacade;
import com.t1t.apim.facades.SearchFacade;
import com.t1t.apim.util.SearchCriteriaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@Api(value = "/search", description = "The Search API.")
@Path("/search")
@ApplicationScoped
public class SearchResource {

    @Inject IStorage storage;
    @Inject IStorageQuery query;
    @Inject private SearchFacade searchFacade;
    @Inject private OrganizationFacade orgFacade;

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
    public List<ServiceVersionWithMarketInfoBean> searchServicesByLifecycle(@PathParam("status") ServiceStatus status) {
        Preconditions.checkNotNull(status, Messages.i18n.format("nullValue", "Service status"));
        return searchFacade.searchServicesByStatus(status);
    }

    @ApiOperation(value = "Search for all used service categories",
            notes = "Use this endpoint to search for all service categories. All categories are returned, also for unpublished service versions.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = String.class, message = "If the search is successful.")
    })
    @GET
    @Path("/service/categories/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> searchCategories() {
        return searchFacade.searchCategories();
    }

    @ApiOperation(value = "Search for all used service categories",
            notes = "Use this endpoint to search for all service categories. All categories are returned, also for unpublished service versions.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = String.class, message = "If the search is successful.")
    })
    @GET
    @Path("/service/categories/published")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> searchPublishedCategories() {
        return searchFacade.searchPublishedCategories();
    }

    @ApiOperation(value = "Search for all Service versions within given category list",
            notes = "Use this endpoint to search for all PUBLISHED service versions, having a category defined in the given category list.")
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
    @ApiOperation(value = "Get Service Market information",
                  notes = "Retrieves the service uptime during the last month, and the distinct active consumers of the service.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = ServiceMarketInfo.class, message = "Service market information.")
                  })
    @GET
    @Path("/organizations/{organizationId}/services/{serviceId}/versions/{version}/market/info")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceMarketInfo getServiceMarketInfo(
            @PathParam("organizationId") String organizationId,
            @PathParam("serviceId") String serviceId,
            @PathParam("version") String version) throws InvalidMetricCriteriaException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
        return orgFacade.getMarketInfo(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Get Service Version",
                  notes = "Use this endpoint to get detailed information about a single version of a Service.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = ServiceVersionBean.class, message = "A Service version.")
                  })
    @GET
    @Path("/organizations/{organizationId}/services/{serviceId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionBean getServiceVersion(@PathParam("organizationId") String organizationId,
                                                @PathParam("serviceId") String serviceId,
                                                @PathParam("version") String version) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
        return orgFacade.getServiceVersion(organizationId, serviceId, version);
    }

    @ApiOperation(value = "List Service Versions",
                  notes = "Use this endpoint to list all of the versions of a Service.")
    @ApiResponses({
                          @ApiResponse(code = 200, responseContainer = "List", response = ServiceVersionSummaryBean.class, message = "A list of Services.")
                  })
    @GET
    @Path("/organizations/{organizationId}/services/{serviceId}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceVersionSummaryBean> listServiceVersions(@PathParam("organizationId") String organizationId,
                                                               @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        return orgFacade.listServiceVersions(organizationId, serviceId);
    }

    @ApiOperation(value = "List consumer apps for the specified service",
                  notes = "Use this endpoint to list all consumer apps using the specified service.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = ApplicationBeanList.class, message = "A list of consumer applications.")
                  })
    @GET
    @Path("/organizations/{organizationId}/services/{serviceId}/consumers")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationBeanList listServiceConsumers(@PathParam("organizationId") String organizationId,
                                                         @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        ApplicationBeanList appList = new ApplicationBeanList();
        appList.setApps(orgFacade.listServiceConsumers(organizationId,serviceId));
        return appList;
    }

    @ApiOperation(value = "List Service Versions by marketplace scope",
            notes = "Use this endpoint to list all service versions belonging to a specific marketplace scope .")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = String.class, message = "A list of Services.")
    })
    @GET
    @Path("/availabilities/{availability}/services/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> listServiceVersionEndpointsForMarketplaceType(@PathParam("availability") ManagedApplicationTypes type) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkNotNull(type, Messages.i18n.format("nullValue", "Marketplace type"));
        return searchFacade.findServiceVersionEndpointsForMarketplaceType(type);
    }

    @ApiOperation(value = "Search through the latest service versions",
            notes = "Use this endpoint to search for service versions with the latest creation date")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = SearchResultsBean.class, message = "If the search is successful.")
    })
    @POST
    @Path("/services/versions/latest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<ServiceVersionWithMarketInfoBean> searchLatestServiceVersions(SearchCriteriaBean criteria) throws OrganizationNotFoundException, InvalidSearchCriteriaException {
        return searchFacade.searchLatestServiceVersions(criteria);
    }

    @ApiOperation(value = "Search for latest Service versions within given category list",
            notes = "Use this endpoint to search for the latest PUBLISHED service versions, having a category defined in the given category list.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceVersionWithMarketInfoBean.class, message = "If the search is successful.")
    })
    @POST
    @Path("/services/versions/latest/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceVersionWithMarketInfoBean> searchLatestServiceVersionForCategories(CategorySearchBean searchBean) {
        return searchFacade.searchLatestPublishedServiceVersionsInCategory(searchBean.getCategories());
    }
}
