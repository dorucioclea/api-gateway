package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.rest.resources.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.rest.resources.exceptions.OrganizationNotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The Search API.
 */
@Path("/search")
public interface ISearchResource {

    /**
     * Use this endpoint to search for organizations.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     * @summary Search for Organizations
     * @param criteria The search criteria.
     * @statuscode 200 If the search is successful.
     * @return The search results (a page of organizations).
     * @throws InvalidSearchCriteriaException when provided criteria are invalid when provided criteria are invalid 
     */
    @POST
    @Path("/organizations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<OrganizationSummaryBean> searchOrgs(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException;

    /**
     * Use this endpoint to search for applications.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     * @summary Search for Applications
     * @param criteria The search criteria.
     * @statuscode 200 If the search is successful.
     * @return The search results (a page of applications).
     * @throws OrganizationNotFoundException when provided organization is not found
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     */
    @POST
    @Path("/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<ApplicationSummaryBean> searchApps(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException;

    /**
     * Use this endpoint to search for services.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     * @summary Search for Services
     * @param criteria The search criteria.
     * @statuscode 200 If the search is successful.
     * @return The search results (a page of services).
     * @throws OrganizationNotFoundException when provided organization is not found
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     */
    @POST
    @Path("/services")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException;

}
