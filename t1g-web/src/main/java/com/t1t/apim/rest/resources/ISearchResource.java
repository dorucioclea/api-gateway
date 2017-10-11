package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.categories.CategorySearchBean;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.services.ServiceStatus;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.services.ServiceVersionWithMarketInfoBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.apim.exceptions.OrganizationNotFoundException;

import java.util.List;
import java.util.Set;

/**
 * The Search API.
 */
public interface ISearchResource {

    /**
     * Use this endpoint to search for organizations.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     *
     * @param criteria The search criteria.
     * @return The search results (a page of organizations).
     * @throws InvalidSearchCriteriaException when provided criteria are invalid when provided criteria are invalid
     * @summary Search for Organizations
     * @statuscode 200 If the search is successful.
     */
    public SearchResultsBean<OrganizationSummaryBean> searchOrgs(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException;

    /**
     * Use this endpoint to search for applications.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     *
     * @param criteria The search criteria.
     * @return The search results (a page of applications).
     * @throws OrganizationNotFoundException  when provided organization is not found
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     * @summary Search for Applications
     * @statuscode 200 If the search is successful.
     */
    public SearchResultsBean<ApplicationSummaryBean> searchApps(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException;

    /**
     * Use this endpoint to search for services.  The search criteria is
     * provided in the body of the request, including filters, order-by, and paging
     * information.
     *
     * @param criteria The search criteria.
     * @return The search results (a page of services).
     * @throws OrganizationNotFoundException  when provided organization is not found
     * @throws InvalidSearchCriteriaException when provided criteria are invalid
     * @summary Search for Services
     * @statuscode 200 If the search is successful.
     */

    public SearchResultsBean<ServiceSummaryBean> searchServices(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException;

    public SearchResultsBean<ServiceVersionWithMarketInfoBean> searchLatestServiceVersions(SearchCriteriaBean criteria)
            throws OrganizationNotFoundException, InvalidSearchCriteriaException;

    /**
     * Use this endpoint to search for all services with given status.
     *
     * @param status 200 If the search is successful
     * @return The list of services with the given status.
     * @summary Search for Services with a specific status
     */
    public List<ServiceVersionWithMarketInfoBean> searchServicesByLifecycle(ServiceStatus status);

    /**
     * Use this endpoint to search for all service categories. All categories are returned, also for unpublished service versions.
     *
     * @return List of registered categories
     * @summary Search for all used service categories
     */
    public Set<String> searchCategories();

    /**
     * Use this endpoint to search for all ServiceVersions within given categories.
     *
     * @param searchBean
     * @return
     */
    public List<ServiceVersionBean> searchServiceVersionForCategories(CategorySearchBean searchBean);

    public List<ServiceVersionWithMarketInfoBean> searchLatestServiceVersionForCategories(CategorySearchBean searchBean);
}
