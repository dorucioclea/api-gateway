package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.announcements.NewAnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.beans.idm.GrantRolesBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.members.MemberBean;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.digipolis.apim.beans.plans.*;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyChainBean;
import com.t1t.digipolis.apim.beans.policies.UpdatePolicyBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.beans.support.*;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.rest.impl.util.FieldValidator;
import com.t1t.digipolis.apim.rest.resources.IOrganizationResource;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsUsageList;
import com.t1t.digipolis.util.ValidationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This is the rest endpoint implementation.
 * Responsabilities:
 * <ul>
 * <li>REST documentation (swagger based)</li>
 * <li>Security checks (verify role of current user)</li>
 * <li>Exception handling (with exceptionmapper)</li>
 * <li>Parameter validation</li>
 * <li>Facade client</li>
 * </ul>
 * <p>
 * In the facade (stateless bean) a container managed transaction strategy will be used.
 */
@Api(value = "/organizations", description = "The Organization API.")
@Path("/organizations")
@ApplicationScoped
public class OrganizationResource implements IOrganizationResource {

    @Inject
    IStorage storage;
    @Inject
    IIdmStorage idmStorage;
    @Inject
    IStorageQuery query;
    @Inject
    IMetricsAccessor metrics;
    @Inject
    private OrganizationFacade orgFacade;

    @Inject
    IApplicationValidator applicationValidator;
    @Inject
    IServiceValidator serviceValidator;
    @Inject
    IApiKeyGenerator apiKeyGenerator;

    @Inject
    IUserResource users;
    @Inject
    IRoleResource roles;

    @Inject
    ISecurityContext securityContext;
    @Inject
    IGatewayLinkFactory gatewayLinkFactory;

    @Context
    HttpServletRequest request;

    /**
     * Constructor.
     */
    public OrganizationResource() {
    }

    /*************
     * ORGANIZATION
     **************/

    @ApiOperation(value = "Create Organization",
            notes = "Use this endpoint to create a new Organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationBean.class, message = "Full details about the Organization that was created."),
            @ApiResponse(code = 409, response = ErrorBean.class, message = "Conflict error.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrganizationBean create(NewOrganizationBean bean) throws OrganizationAlreadyExistsException, InvalidNameException {
        Preconditions.checkNotNull(bean);
        FieldValidator.validateName(bean.getName());
        return orgFacade.create(bean);
    }

    @ApiOperation(value = "Get Organization By ID",
            notes = "Use this endpoint to get information about a single Organization by its ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationBean.class, message = "The Organization.")
    })
    @GET
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public OrganizationBean get(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        return orgFacade.get(organizationId);
    }

    @ApiOperation(value = "Update Organization By ID",
            notes = "Updates meta-information about a single Organization.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("organizationId") String organizationId, UpdateOrganizationBean bean)
            throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkNotNull(bean);
        orgFacade.update(organizationId, bean);
    }

    @ApiOperation(value = "Get Organization Activity",
            notes = "Returns audit activity information for a single Organization.  The audit information that is returned represents all of the activity associated with this Organization (i.e. an audit log for everything in the Organization).")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "List of audit/activity entries.")
    })
    @GET
    @Path("/{organizationId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> activity(@PathParam("organizationId") String organizationId, @QueryParam("page") int page, @QueryParam("count") int pageSize)
            throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        return orgFacade.activity(organizationId, page, pageSize);
    }

    /*************
     * APPLICATIONS
     **************/
    @ApiOperation(value = "Create Application",
            notes = "Use this endpoint to create a new Application.  Note that it is important to also create an initial version of the Application (e.g. v1.0).  This can either be done by including the 'initialVersion' property in the request, or by immediately following up with a call to \"Create Application Version\".  If the former is done, then a first Application version will be created automatically by this endpoint. When providing a logo, the logo can not be greater than 10k")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationBean.class, message = "Full details about the newly created Application.")
    })
    @POST
    @Path("/{organizationId}/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationBean createApp(@PathParam("organizationId") String organizationId, NewApplicationBean bean) throws OrganizationNotFoundException, ApplicationAlreadyExistsException, NotAuthorizedException, InvalidNameException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(bean.getBase64logo().getBytes().length <= 15000, "Logo should not be greater than 10k");
        FieldValidator.validateName(bean.getName());
        return orgFacade.createApp(organizationId, bean);
    }

    @ApiOperation(value = "Get Application By ID",
            notes = "Use this endpoint to retrieve information about a single Application by ID.  Note that this only returns information about the Application, not about any particular *version* of the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationBean.class, message = "An Application.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationBean getApp(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId)
            throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        return orgFacade.getApp(organizationId, applicationId);
    }

    @ApiOperation(value = "Get Application Activity",
            notes = "This endpoint returns audit activity information about the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit activity entries.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getAppActivity(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, @QueryParam("page") int page, @QueryParam("count") int pageSize) throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        return orgFacade.getAppActivity(organizationId, applicationId, page, pageSize);
    }

    @ApiOperation(value = "List Applications",
            notes = "Use this endpoint to get a list of all Applications in the Organization.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ApplicationSummaryBean.class, message = "A list of Applications.")
    })
    @GET
    @Path("/{organizationId}/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationSummaryBean> listApps(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        return orgFacade.listApps(organizationId);
    }

    @ApiOperation(value = "Update Application",
            notes = "Use this endpoint to update information about an Application.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/applications/{applicationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateApp(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, UpdateApplicationBean bean)
            throws ApplicationNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkNotNull(bean);
        orgFacade.updateApp(organizationId, applicationId, bean);
    }

    @ApiOperation(value = "Create Application Version",
            notes = "Use this endpoint to create a new version of the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationVersionBean.class, message = "Full details about the newly created Application version.")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationVersionBean createAppVersion(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, NewApplicationVersionBean bean) throws ApplicationNotFoundException, NotAuthorizedException, InvalidVersionException, ApplicationVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkNotNull(bean);
        FieldValidator.validateVersion(bean.getVersion());
        return orgFacade.createAppVersion(organizationId, applicationId, bean);
    }

    @ApiOperation(value = "Update Application Version OAuth2 Callback URI",
            notes = "Use this endpoint to update the application version URI callback for OAuth2.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationVersionBean.class, message = "Updated Application Version.")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationVersionBean updateAppVersionCallbackURI(@PathParam("organizationId") String orgId, @PathParam("applicationId")String appId, @PathParam("version") String version ,UpdateApplicationVersionURIBean updateAppUri){
        return orgFacade.updateAppVersionURI(orgId, appId, version, updateAppUri);
    }

    @ApiOperation(value = "Get Application Version",
            notes = "Use this endpoint to get detailed information about a single version of an Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationVersionBean.class, message = "An Application version.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationVersionBean getAppVersion(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, @PathParam("version") String version)
            throws ApplicationVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getAppVersion(organizationId, applicationId, version);
    }

    @ApiOperation(value = "Get Application Version Activity",
            notes = "Use this endpoint to get audit activity information for a single version of the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit entries.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getAppVersionActivity(@PathParam("organizationId") String organizationId,
                                                                   @PathParam("applicationId") String applicationId,
                                                                   @PathParam("version") String version,
                                                                   @QueryParam("page") int page,
                                                                   @QueryParam("count") int pageSize) throws ApplicationVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getAppVersionActivity(organizationId, applicationId, version, page, pageSize);
    }

    @ApiOperation(value = "Get App Usage Metrics (per Service)",
            notes = "Retrieves metrics/analytics information for a specific application.  This will return request count data broken down by service.  It basically answers the question \"which services is my app really using?\".")
    @ApiResponses({
            @ApiResponse(code = 200, response = AppUsagePerServiceBean.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/metrics/serviceUsage")
    @Produces(MediaType.APPLICATION_JSON)
    public AppUsagePerServiceBean getAppUsagePerService(@PathParam("organizationId") String organizationId,
                                                        @PathParam("applicationId") String applicationId,
                                                        @PathParam("version") String version,
                                                        @QueryParam("interval") HistogramIntervalType interval,
                                                        @QueryParam("from") String fromDate,
                                                        @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.appView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate));
        Preconditions.checkNotNull(interval);
        Preconditions.checkArgument(!StringUtils.isEmpty(interval.toString()));
        return orgFacade.getAppUsagePerService(organizationId, applicationId, version, interval, fromDate, toDate);
    }

    @ApiOperation(value = "List Application Versions",
            notes = "Use this endpoint to list all of the versions of an Application.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ApplicationVersionSummaryBean.class, message = "A list of Applications versions.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationVersionSummaryBean> listAppVersions(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId)
            throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        return orgFacade.listAppVersions(organizationId, applicationId);
    }

    @ApiOperation(value = "Create a Service Contract",
            notes = "Use this endpoint to create a Contract between the Application and a Service.  In order to create a Contract, the caller must specify the Organization, ID, and Version of the Service.  Additionally the caller must specify the ID of the Plan it wished to use for the Contract with the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ContractBean.class, message = "Full details about the newly created Contract.")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ContractBean createContract(@PathParam("organizationId") String organizationId,
                                       @PathParam("applicationId") String applicationId,
                                       @PathParam("version") String version,
                                       NewContractBean bean) throws OrganizationNotFoundException, ApplicationNotFoundException,
            ServiceNotFoundException, PlanNotFoundException, ContractAlreadyExistsException, NotAuthorizedException {
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.createContract(organizationId, applicationId, version, bean);
    }

    @ApiOperation(value = "Get Service Contract",
            notes = "Use this endpoint to retrieve detailed information about a single Service Contract for an Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ContractBean.class, message = "Details about a single Contract.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts/{contractId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContractBean getContract(@PathParam("organizationId") String organizationId,
                                    @PathParam("applicationId") String applicationId,
                                    @PathParam("version") String version,
                                    @PathParam("contractId") Long contractId) throws ApplicationNotFoundException, ContractNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getContract(organizationId, applicationId, version, contractId);
    }

    @ApiOperation(value = "Break All Contracts",
            notes = "Use this endpoint to break all contracts between this application and its services.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts")
    public void deleteAllContracts(@PathParam("organizationId") String organizationId,
                                   @PathParam("applicationId") String applicationId,
                                   @PathParam("version") String version) throws ApplicationNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.deleteAllContracts(organizationId, applicationId, version);
    }

    @ApiOperation(value = "Break Contract",
            notes = "Use this endpoint to break a Contract with a Service.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts/{contractId}")
    public void deleteContract(@PathParam("organizationId") String organizationId,
                               @PathParam("applicationId") String applicationId,
                               @PathParam("version") String version,
                               @PathParam("contractId") Long contractId) throws ApplicationNotFoundException, ContractNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.deleteContract(organizationId, applicationId, version, contractId);
    }

    @ApiOperation(value = "List All Contracts for an Application",
            notes = "Use this endpoint to get a list of all Contracts for an Application.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ContractSummaryBean.class, message = "A list of Contracts.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContractSummaryBean> getApplicationVersionContracts(@PathParam("organizationId") String organizationId,
                                                                    @PathParam("applicationId") String applicationId,
                                                                    @PathParam("version") String version)
            throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getApplicationVersionContracts(organizationId, applicationId, version);
    }

    @ApiOperation(value = "Get API Registry (JSON)",
            notes = "Use this endpoint to get registry style information about all Services that this Application consumes.  This is a useful endpoint to invoke in order to retrieve a summary of every Service consumed by the application.  The information returned by this endpoint could potentially be included directly in a client application as a way to lookup endpoint information for the APIs it wishes to consume.  This variant of the API Registry is formatted as JSON data.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApiRegistryBean.class, message = "API Registry information.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/apiregistry/json")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiRegistryBean getApiRegistryJSON(@PathParam("organizationId") String organizationId,
                                              @PathParam("applicationId") String applicationId,
                                              @PathParam("version") String version) throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getApiRegistryJSON(organizationId, applicationId, version);
    }

    @ApiOperation(value = "Get API Registry (XML)",
            notes = "Use this endpoint to get registry style information about all Services that this Application consumes.  This is a useful endpoint to invoke in order to retrieve a summary of every Service consumed by the application.  The information returned by this endpoint could potentially be included directly in a client application as a way to lookup endpoint information for the APIs it wishes to consume.  This variant of the API Registry is formatted as XML data.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApiRegistryBean.class, message = "API Registry information.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/apiregistry/xml")
    @Produces(MediaType.APPLICATION_XML)
    public ApiRegistryBean getApiRegistryXML(@PathParam("organizationId") String organizationId,
                                             @PathParam("applicationId") String applicationId,
                                             @PathParam("version") String version) throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getApiRegistryXML(organizationId, applicationId, version);
    }

    @ApiOperation(value = "Add Application Policy",
            notes = "Use this endpoint to add a new Policy to the Application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full details about the newly added Policy.")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean createAppPolicy(@PathParam("organizationId") String organizationId,
                                      @PathParam("applicationId") String applicationId,
                                      @PathParam("version") String version,
                                      NewPolicyBean bean) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.createAppPolicy(organizationId, applicationId, version, bean);
    }

    @ApiOperation(value = "Get Application Policy",
            notes = "Use this endpoint to get information about a single Policy in the Application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full information about the Policy.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies/{policyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean getAppPolicy(@PathParam("organizationId") String organizationId,
                                   @PathParam("applicationId") String applicationId,
                                   @PathParam("version") String version,
                                   @PathParam("policyId") long policyId) throws OrganizationNotFoundException, ApplicationVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getAppPolicy(organizationId, applicationId, version, policyId);
    }

    @ApiOperation(value = "Update Application Policy",
            notes = "Use this endpoint to update the meta-data or configuration of a single Application Policy.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies/{policyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateAppPolicy(@PathParam("organizationId") String organizationId,
                                @PathParam("applicationId") String applicationId,
                                @PathParam("version") String version,
                                @PathParam("policyId") long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ApplicationVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.updateAppPolicy(organizationId, applicationId, version, policyId, bean);
    }

    @ApiOperation(value = "Remove Application Policy",
            notes = "Use this endpoint to remove a Policy from the Application.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies/{policyId}")
    public void deleteAppPolicy(@PathParam("organizationId") String organizationId,
                                @PathParam("applicationId") String applicationId, @PathParam("version") String version, @PathParam("policyId") long policyId)
            throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.deleteAppPolicy(organizationId, applicationId, version, policyId);
    }

    @ApiOperation(value = "List All Application Policies",
            notes = "Use this endpoint to list all of the Policies configured for the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PolicySummaryBean.class, message = "System status information")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicySummaryBean> listAppPolicies(@PathParam("organizationId") String organizationId,
                                                   @PathParam("applicationId") String applicationId,
                                                   @PathParam("version") String version)
            throws OrganizationNotFoundException, ApplicationVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.listAppPolicies(organizationId, applicationId, version);
    }

    @ApiOperation(value = "Re-Order Application Policies",
            notes = "Use this endpoint to change the order of Policies for an Application.  When a Policy is added to the Application, it is added as the last Policy in the list of Application Policies.  Sometimes the order of Policies is important, so it is often useful to re-order the Policies by invoking this endpoint.  The body of the request should include all of the Policies for the Application, in the new desired order.  Note that only the IDs of each of the Policies is actually required in the request, at a minimum.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/reorderPolicies")
    @Consumes(MediaType.APPLICATION_JSON)
    public void reorderApplicationPolicies(@PathParam("organizationId") String organizationId,
                                           @PathParam("applicationId") String applicationId,
                                           @PathParam("version") String version,
                                           PolicyChainBean policyChain) throws OrganizationNotFoundException,
            ApplicationVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.reorderApplicationPolicies(organizationId, applicationId, version, policyChain);
    }

    @ApiOperation(value = "Create Service",
            notes = "Use this endpoint to create a new Service.  Note that it is important to also create an initial version of the Service (e.g. 1.0).  This can either be done by including the 'initialVersion' property in the request, or by immediately following up with a call to \"Create Service Version\".  If the former is done, then a first Service version will be created automatically by this endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBean.class, message = "Full details about the newly created Service.")
    })
    @POST
    @Path("/{organizationId}/services")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceBean createService(@PathParam("organizationId") String organizationId, NewServiceBean bean)
            throws OrganizationNotFoundException, ServiceAlreadyExistsException, NotAuthorizedException,
            InvalidNameException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(bean.getBase64logo().getBytes().length <= 15000, "Logo should not be greater than 10k");
        FieldValidator.validateName(bean.getName());
        return orgFacade.createService(organizationId, bean);
    }

    @ApiOperation(value = "Create new service announcement",
            notes = "Use this endpoint to create a new service announcement.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AnnouncementBean.class, message = "Announcement created.")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/announcement")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AnnouncementBean createServiceAnnouncement(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, NewAnnouncementBean announcementBean) throws ServiceNotFoundException, NotAuthorizedException{
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkNotNull(announcementBean);
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementBean.getDescription()));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementBean.getTitle()));
        return orgFacade.createServiceAnnouncement(organizationId, serviceId, announcementBean);
    }

    @ApiOperation(value = "Retrieve a specific announcement for given service.",
            notes = "Use this endpoint to retrieve a specific announcement.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AnnouncementBean.class, message = "Announcement for given id.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/announcement/{announcementId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AnnouncementBean getServiceAnnouncement(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("announcementId")String announcementId) throws ServiceNotFoundException, NotAuthorizedException{
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementId));
        Long id = Long.parseLong(announcementId.trim(), 10);
    return orgFacade.getServiceAnnouncement(organizationId, serviceId, id);
    }

    @ApiOperation(value = "Retrieve all announcement for given service.",
            notes = "Use this endpoint to retrieve all announcement.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = AnnouncementBean.class, message = "List of announcements.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/announcement/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<AnnouncementBean> getServiceAnnouncements(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException{
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.getServiceAnnouncements(organizationId, serviceId);
    }

    @ApiOperation(value = "Remove Service Policy",
            notes = "Use this endpoint to remove a Policy from the Service.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/announcement/{announcementId}")
    public void deleteServiceAnnouncement(@PathParam("organizationId") String organizationId,
                                    @PathParam("serviceId") String serviceId,
                                    @PathParam("announcementId") String announcementId) throws OrganizationNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementId));
        orgFacade.deleteServiceAnnouncement(organizationId, serviceId,Long.parseLong(announcementId.trim(), 10));
    }

    @ApiOperation(value = "Update Service Terms",
            notes = "Use this endpoint to provide service terms. The service terms applies to all service versions.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBean.class, message = "Full details about the updated Service.")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}/terms")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceBean updateServiceTerms(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, UpdateServiceTearmsBean serviceTerms) throws ServiceNotFoundException, NotAuthorizedException{
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(serviceTerms);
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceTerms.getTerms()));
        return orgFacade.updateServiceTerms(organizationId, serviceId, serviceTerms);
    }

    @ApiOperation(value = "Get Service By ID",
            notes = "Use this endpoint to retrieve information about a single Service by ID.  Note that this only returns information about the Service, not about any particular *version* of the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBean.class, message = "A Service.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceBean getService(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId)
            throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.getService(organizationId, serviceId);
    }

    @ApiOperation(value = "Get Service Activity",
            notes = "This endpoint returns audit activity information about the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit activity entries.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getServiceActivity(@PathParam("organizationId") String organizationId,
                                                                @PathParam("serviceId") String serviceId,
                                                                @QueryParam("page") int page,
                                                                @QueryParam("count") int pageSize) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.getServiceActivity(organizationId, serviceId, page, pageSize);
    }

    @ApiOperation(value = "List Services",
            notes = "Use this endpoint to get a list of all Services in the Organization.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceSummaryBean.class, message = "A list of Services.")
    })
    @GET
    @Path("/{organizationId}/services")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceSummaryBean> listServices(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        return orgFacade.listServices(organizationId);
    }

    @ApiOperation(value = "Update Service",
            notes = "Use this endpoint to update information about a Service.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateService(@PathParam("organizationId") String organizationId,
                              @PathParam("serviceId") String serviceId,
                              UpdateServiceBean bean)
            throws ServiceNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        orgFacade.updateService(organizationId, serviceId, bean);

    }

    @ApiOperation(value = "Create Service Version",
            notes = "Use this endpoint to create a new version of the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceVersionBean.class, message = "Full details about the newly created Service version.")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionBean createServiceVersion(@PathParam("organizationId") String organizationId,
                                                   @PathParam("serviceId") String serviceId,
                                                   NewServiceVersionBean bean) throws ServiceNotFoundException, NotAuthorizedException,
            InvalidVersionException, ServiceVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkNotNull(bean);
        FieldValidator.validateVersion(bean.getVersion());
        return orgFacade.createServiceVersion(organizationId, serviceId, bean);
    }

    @ApiOperation(value = "Get Service Version",
            notes = "Use this endpoint to get detailed information about a single version of a Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceVersionBean.class, message = "A Service version.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionBean getServiceVersion(@PathParam("organizationId") String organizationId,
                                                @PathParam("serviceId") String serviceId,
                                                @PathParam("version") String version) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersion(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Get Service Definition",
            notes = "Use this endpoint to retrieve the Service's definition document.  A service definition document can be several different types, depending on the Service type and technology used to define the service.  For example, this endpoint might return a WSDL document, or a Swagger JSON document.")
    @ApiResponses({
            @ApiResponse(code = 200, response = Response.class, message = "The Service Definition document (e.g. a Swagger JSON file).")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/definition")
    @Produces({MediaType.APPLICATION_JSON, "application/wsdl+xml", "application/x-yaml"})
    public Response getServiceDefinition(@PathParam("organizationId") String organizationId,
                                         @PathParam("serviceId") String serviceId,
                                         @PathParam("version") String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException {
        try {
            ServiceVersionBean serviceVersion = getServiceVersion(organizationId, serviceId, version);
            InputStream definition = orgFacade.getServiceDefinition(organizationId, serviceId, version);
            ResponseBuilder builder = Response.ok().entity(definition);
            if (serviceVersion.getDefinitionType() == ServiceDefinitionType.SwaggerJSON) {
                builder.type(MediaType.APPLICATION_JSON);
            } else if (serviceVersion.getDefinitionType() == ServiceDefinitionType.SwaggerYAML) {
                builder.type("application/x-yaml"); //$NON-NLS-1$
/*            } else if (serviceVersion.getDefinitionType() == ServiceDefinitionType.WSDL) {
                builder.type("application/wsdl+xml"); //$NON-NLS-1$*/
            } else {
                throw new Exception("Service definition type not supported: " + serviceVersion.getDefinitionType()); //$NON-NLS-1$
            }

            return builder.build();
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get Service Endpoint",
            notes = "Use this endpoint to get information about the Managed Service's gateway endpoint.  In other words, this returns the actual live endpoint on the API Gateway - the endpoint that a client should use when invoking the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceVersionEndpointSummaryBean.class, message = "The live Service endpoint information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/endpoint")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionEndpointSummaryBean getServiceVersionEndpointInfo(@PathParam("organizationId") String organizationId,
                                                                           @PathParam("serviceId") String serviceId,
                                                                           @PathParam("version") String version) throws ServiceVersionNotFoundException,
            InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionEndpointInfo(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Get Service Version Activity",
            notes = "Use this endpoint to get audit activity information for a single version of the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit entries.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getServiceVersionActivity(@PathParam("organizationId") String organizationId,
                                                                       @PathParam("serviceId") String serviceId,
                                                                       @PathParam("version") String version,
                                                                       @QueryParam("page") int page,
                                                                       @QueryParam("count") int pageSize) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionActivity(organizationId, serviceId, version, page, pageSize);
    }

    @ApiOperation(value = "Update Service Version",
            notes = "Use this endpoint to update information about a single version of a Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceVersionBean.class, message = "The updated Service Version.")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}/versions/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionBean updateServiceVersion(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, @PathParam("version") String version,
                                                   UpdateServiceVersionBean bean) throws ServiceVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkNotNull(bean);
        //it's possible not to provide the endpoint directly
        if(!StringUtils.isEmpty(bean.getEndpoint())) Preconditions.checkArgument(ValidationUtils.isValidURL(bean.getEndpoint()));
        return orgFacade.updateServiceVersion(organizationId, serviceId, version, bean);
    }

    @ApiOperation(value = "Update Service Definition",
            notes = "Use this endpoint to update the Service's definition document.  A service definition will vary depending on the type of service, and the type of definition used.  For example, it might be a Swagger document or a WSDL file. To use this endpoint, simply PUT the updated Service Definition document in its entirety, making sure to set the Content-Type appropriately for the type of definition document.  The content will be stored and the Service's 'Definition Type' field will be updated.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/definition")
    @Consumes({MediaType.APPLICATION_JSON, "application/wsdl+xml", "application/x-yaml"})
    public void updateServiceDefinition(@PathParam("organizationId") String organizationId,
                                        @PathParam("serviceId") String serviceId,
                                        @PathParam("version") String version) throws ServiceVersionNotFoundException, NotAuthorizedException, InvalidServiceStatusException {
        String contentType = request.getContentType();
        InputStream stream = null;
        try {
            stream = request.getInputStream();
            orgFacade.updateServiceDefinition(organizationId, serviceId, version, contentType, stream);
        } catch (IOException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "List Service Versions",
            notes = "Use this endpoint to list all of the versions of a Service.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceVersionSummaryBean.class, message = "A list of Services.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceVersionSummaryBean> listServiceVersions(@PathParam("organizationId") String organizationId,
                                                               @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.listServiceVersions(organizationId, serviceId);
    }

    @ApiOperation(value = "List Service Plans",
            notes = "Use this endpoint to list the Plans configured for the given Service version.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServicePlanSummaryBean.class, message = "A list of Service plans.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plans")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServicePlanSummaryBean> getServiceVersionPlans(@PathParam("organizationId") String organizationId,
                                                               @PathParam("serviceId") String serviceId,
                                                               @PathParam("version") String version) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionPlans(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Add Service Policy",
            notes = "Use this endpoint to add a new Policy to the Service version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full details about the newly added Policy.")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/policies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean createServicePolicy(@PathParam("organizationId") String organizationId,
                                          @PathParam("serviceId") String serviceId,
                                          @PathParam("version") String version,
                                          NewPolicyBean bean) throws OrganizationNotFoundException, ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.createServicePolicy(organizationId, serviceId, version, bean);
    }

    @ApiOperation(value = "Get Service Policy",
            notes = "Use this endpoint to get information about a single Policy in the Service version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full information about the Policy.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/policies/{policyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean getServicePolicy(@PathParam("organizationId") String organizationId,
                                       @PathParam("serviceId") String serviceId,
                                       @PathParam("version") String version,
                                       @PathParam("policyId") long policyId)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServicePolicy(organizationId, serviceId, version, policyId);
    }

    @ApiOperation(value = "Update Service Policy",
            notes = "Use this endpoint to update the meta-data or configuration of a single Service Policy.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/policies/{policyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateServicePolicy(@PathParam("organizationId") String organizationId,
                                    @PathParam("serviceId") String serviceId,
                                    @PathParam("version") String version,
                                    @PathParam("policyId") long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.updateServicePolicy(organizationId, serviceId, version, policyId, bean);
    }

    @ApiOperation(value = "Remove Service Policy",
            notes = "Use this endpoint to remove a Policy from the Service.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/policies/{policyId}")
    public void deleteServicePolicy(@PathParam("organizationId") String organizationId,
                                    @PathParam("serviceId") String serviceId,
                                    @PathParam("version") String version,
                                    @PathParam("policyId") long policyId)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.deleteServicePolicy(organizationId, serviceId, version, policyId);
    }

    @ApiOperation(value = "Remove Service Definition",
            notes = "Use this endpoint to remove a Service's definition document.  When this is done, the 'definitionType' field on the Service will be set to None.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/definition")
    public void deleteServiceDefinition(@PathParam("organizationId") String organizationId,
                                        @PathParam("serviceId") String serviceId,
                                        @PathParam("version") String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.deleteServiceDefinition(organizationId, serviceId, version);

    }

    @ApiOperation(value = "List All Service Policies",
            notes = "Use this endpoint to list all of the Policies configured for the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PolicySummaryBean.class, message = "A List of Policies.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/policies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicySummaryBean> listServicePolicies(@PathParam("organizationId") String organizationId,
                                                       @PathParam("serviceId") String serviceId,
                                                       @PathParam("version") String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.listServicePolicies(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Re-Order Service Policies",
            notes = "Use this endpoint to change the order of Policies for a Service.  When a Policy is added to the Service, it is added as the last Policy in the list of Service Policies.  Sometimes the order of Policies is important, so it is often useful to re-order the Policies by invoking this endpoint.  The body of the request should include all of the Policies for the Service, in the new desired order.  Note that only the IDs of each of the Policies is actually required in the request, at a minimum.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/reorderPolicies")
    @Consumes(MediaType.APPLICATION_JSON)
    public void reorderServicePolicies(@PathParam("organizationId") String organizationId,
                                       @PathParam("serviceId") String serviceId,
                                       @PathParam("version") String version,
                                       PolicyChainBean policyChain) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.reorderServicePolicies(organizationId, serviceId, version, policyChain);
    }

    @ApiOperation(value = "Get Service Policy Chain",
            notes = "Use this endpoint to get a Policy Chain for the specific Service version.  A Policy Chain is a useful summary to better understand which Policies would be executed for a request to this Service through a particular Plan offered by the Service.  Often this information is interesting prior to create a Contract with the Service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyChainBean.class, message = "A Policy Chain.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plans/{planId}/policyChain")
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyChainBean getServicePolicyChain(@PathParam("organizationId") String organizationId,
                                                 @PathParam("serviceId") String serviceId,
                                                 @PathParam("version") String version,
                                                 @PathParam("planId") String planId) throws ServiceVersionNotFoundException, PlanNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServicePolicyChain(organizationId, serviceId, version, planId);
    }

    @ApiOperation(value = "List Service Contracts",
            notes = "Use this endpoint to get a list of all Contracts created with this Service.  This will return Contracts created by between any Application and through any Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ContractSummaryBean.class, message = "A list of Contracts.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/contracts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContractSummaryBean> getServiceVersionContracts(@PathParam("organizationId") String organizationId,
                                                                @PathParam("serviceId") String serviceId,
                                                                @PathParam("version") String version,
                                                                @QueryParam("page") int page,
                                                                @QueryParam("count") int pageSize) throws ServiceVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getServiceVersionContracts(organizationId, serviceId, version, page, pageSize);
    }

    @ApiOperation(value = "Get Service Market information",
            notes = "Retrieves the service uptime during the last month, and the distinct active consumers of the service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceMarketInfo.class, message = "Service market information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/market/info")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceMarketInfo getServiceMarketInfo(
            @PathParam("organizationId") String organizationId,
            @PathParam("serviceId") String serviceId,
            @PathParam("version") String version) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getMarketInfo(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Add a service follower",
            notes = "A user can follow all notifications for a service, use this endpoint to add a user for a service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBean.class, message = "A Service.")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/followers/add/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceBean addServiceFollower(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("userId") String userId) throws ServiceNotFoundException, javax.ws.rs.NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return orgFacade.addServiceFollower(organizationId, serviceId, userId);
    }

    @ApiOperation(value = "Remove a service follower",
            notes = "Use this endpoint to remove a user from a service, the user will not be following the service anymore.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceBean.class, message = "A Service.")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/followers/remove/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceBean removeServiceFollower(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("userId") String userId) throws ServiceNotFoundException, javax.ws.rs.NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return orgFacade.removeServiceFollower(organizationId, serviceId, userId);
    }

    @ApiOperation(value = "Get service followers.",
            notes = "Use this endpoint retrieve all service followers, and a total amount of followers.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceFollowers.class, message = "A Service.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/followers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceFollowers getServiceFollowers(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, javax.ws.rs.NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.getServiceFollowers(organizationId, serviceId);
    }

    @ApiOperation(value = "Add a new support ticket for a service",
            notes = "Use this endpoint to add a new ticket for a service within an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SupportBean.class, message = "Support ticket")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/support/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportBean createServiceSupportTicket(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, NewSupportBean bean) throws ServiceNotFoundException, NotAuthorizedException {
        //Permissions: everybody can create a ticket
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getTitle()));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getDescription()));
        return orgFacade.createServiceSupportTicket(organizationId, serviceId, bean);
    }

    @ApiOperation(value = "Update a support ticket for a service",
            notes = "Use this endpoint to update a ticket for a service within an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SupportBean.class, message = "Updated support ticket")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}/support/{supportId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportBean updateServiceSupportTicket(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("supportId") String supportId, UpdateSupportBean bean) throws ServiceNotFoundException, NotAuthorizedException {
        //Permissions: everybody can create a ticket
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getTitle()));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getDescription()));
        return orgFacade.updateServiceSupportTicket(organizationId,serviceId,Long.parseLong(supportId.trim(),10),bean);
    }

    @ApiOperation(value = "Get a support ticket for a service",
            notes = "Use this endpoint to retrieve a ticket for a service within an organization, based on the ticket id.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SupportBean.class, message = "Updated support ticket")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/support/{supportId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportBean getServiceSupportTicket(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("supportId") String supportId) throws ServiceNotFoundException, NotAuthorizedException {
        //Permissions: everybody can create a ticket
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        return orgFacade.getServiceSupportTicket(organizationId,serviceId,Long.parseLong(supportId.trim(),10));
    }

    @ApiOperation(value = "Delete a support ticket for a service",
            notes = "Use this endpoint to delete a ticket for a service within an organization, based on the ticket id.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/support/{supportId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteServiceSupportTicket(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,@PathParam("supportId") String supportId) throws ServiceNotFoundException, NotAuthorizedException {
        //Permissions: only admin can remove tickets
        if (!securityContext.hasPermission(PermissionType.svcAdmin, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        orgFacade.deleteSupportTicket(organizationId,serviceId,Long.parseLong(supportId.trim(),10));
    }

    @ApiOperation(value = "Retrieve a list of all support tickets for a service.",
            notes = "Use this endpoint to retrieve a list of all support tickets for a service.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = SupportBean.class, message = "Service support tickets")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/support")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SupportBean> listServiceSupportTickets(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId) throws ServiceNotFoundException, NotAuthorizedException {
        //Permissions: everybody can create a ticket
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        return orgFacade.listServiceSupportTickets(organizationId, serviceId);
    }

    @ApiOperation(value = "Add a new comment to a support ticket for a service",
            notes = "Use this endpoint to add a new comment to a support Ticket for a service within an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SupportComment.class, message = "Support ticket comment")
    })
    @POST
    @Path("/services/support/{supportId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportComment addServiceSupportComment(@PathParam("supportId") String supportId, NewSupportComment bean) throws NotAuthorizedException {
        //Permissions: everybody can create a ticket
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getComment()));
        return orgFacade.addServiceSupportComment(Long.parseLong(supportId.trim(),10),bean);
    }

    @ApiOperation(value = "Update a comment to a support ticket for a service",
            notes = "Use this endpoint to update a comment to a support Ticket for a service within an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SupportComment.class, message = "Updated support ticket comment")
    })
    @PUT
    @Path("/services/support/{supportId}/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportComment updateServiceSupportComment(@PathParam("supportId") String supportId, @PathParam("commentId") String commentId, UpdateSupportComment bean) throws NotAuthorizedException {
        //Permissions: everybody can create a ticket
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        Preconditions.checkArgument(!StringUtils.isEmpty(commentId));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getComment()));
        return orgFacade.updateServiceSupportComment(Long.parseLong(supportId.trim(),10),Long.parseLong(commentId.trim(),10),bean);
    }

    @ApiOperation(value = "Delete a support ticket comment for a service",
            notes = "Use this endpoint to delete a ticket comment for a service within an organization, based on the ticket id.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/services/support/{supportId}/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteServiceSupportComment(@PathParam("supportId") String supportId, @PathParam("commentId") String commentId) throws NotAuthorizedException {
        //TODO Permissions: only the user created the ticket can remove his own comment
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        Preconditions.checkArgument(!StringUtils.isEmpty(commentId));
        orgFacade.deleteServiceSupportComment(Long.parseLong(supportId.trim(),10),Long.parseLong(commentId.trim(),10));
    }

    @ApiOperation(value = "Get a support ticket comment.",
            notes = "Use this endpoint to retrieve a support ticket comment.")
    @ApiResponses({
            @ApiResponse(code = 200,response = SupportComment.class, message = "Support tickets comment")
    })
    @GET
    @Path("/services/support/{supportId}/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SupportComment getServiceSupportComment(@PathParam("supportId")String supportId ,@PathParam("commentId")String commentId) throws NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        Preconditions.checkArgument(!StringUtils.isEmpty(commentId));
        return orgFacade.getServiceSupportComment(Long.parseLong(supportId.trim(),10),Long.parseLong(commentId.trim(),10));
    }

    @ApiOperation(value = "Retrieve a list of all comments for a support ticket.",
            notes = "Use this endpoint to retrieve a list of all comments for a support ticket.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = SupportComment.class, message = "List of support comments for a specific ticket")
    })
    @GET
    @Path("/services/support/{supportId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SupportComment> listServiceSupportComments(@PathParam("supportId")String supportId) throws NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId));
        return orgFacade.listServiceSupportTicketComments(Long.parseLong(supportId.trim(),10));
    }

    @ApiOperation(value = "Get Service Usage Metrics",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return a full histogram of request count data based on the provided date range and interval.  Valid intervals are:  month, week, day, hour, minute")
    @ApiResponses({
            @ApiResponse(code = 200, response = MetricsUsageList.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/usage")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsUsageList getUsage(
            @PathParam("organizationId") String organizationId,
            @PathParam("serviceId") String serviceId,
            @PathParam("version") String version,
            @QueryParam("interval") HistogramIntervalType interval,
            @QueryParam("from") String fromDate,
            @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate));
        Preconditions.checkNotNull(interval);
        Preconditions.checkArgument(!StringUtils.isEmpty(interval.toString()));
        return orgFacade.getUsage(organizationId, serviceId, version, interval, fromDate, toDate);
    }

    @ApiOperation(value = "Get System Status",
            notes = "This endpoint simply returns the status of the api-engine system. This is a useful endpoint to use when testing a client's connection to the apiman API Manager REST services.")
    @ApiResponses({
            @ApiResponse(code = 200, response = MetricsResponseStatsList.class, message = "System status information")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/responseStats")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsResponseStatsList getResponseStats(@PathParam("organizationId") String organizationId,
                                                       @PathParam("serviceId") String serviceId,
                                                       @PathParam("version") String version,
                                                       @QueryParam("interval") HistogramIntervalType interval,
                                                       @QueryParam("from") String fromDate,
                                                       @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate));
        Preconditions.checkNotNull(interval);
        Preconditions.checkArgument(!StringUtils.isEmpty(interval.toString()));
        return orgFacade.getResponseStats(organizationId, serviceId, version, interval, fromDate, toDate);
    }

    @ApiOperation(value = "Get Service Response Statistics (Summary)",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return total response type statistics over the given date range.  Basically this will return three numbers: total request, # failed responses, # error responses.")
    @ApiResponses({
            @ApiResponse(code = 200, response = MetricsResponseSummaryList.class, message = "System status information")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/summaryResponseStats")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsResponseSummaryList getResponseStatsSummary(@PathParam("organizationId") String organizationId,
                                                            @PathParam("serviceId") String serviceId,
                                                            @PathParam("version") String version,
                                                            @QueryParam("from") String fromDate,
                                                            @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate));
        return orgFacade.getResponseStatsSummary(organizationId, serviceId, version, fromDate, toDate);
    }

    @ApiOperation(value = "Create Plan",
            notes = "Use this endpoint to create a new Plan.  Note that it is important to also create an initial version of the Plan (e.g. 1.0).  This can either be done by including the 'initialVersion' property in the request, or by immediately following up with a call to \"Create Plan Version\".  If the former is done, then a first Plan version will be created automatically by this endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PlanBean.class, message = "Full details about the newly created Plan.")
    })
    @POST
    @Path("/{organizationId}/plans")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PlanBean createPlan(@PathParam("organizationId") String organizationId, NewPlanBean bean) throws OrganizationNotFoundException,
            PlanAlreadyExistsException, NotAuthorizedException, InvalidNameException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateName(bean.getName());
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkNotNull(bean);
        return orgFacade.createPlan(organizationId, bean);
    }

    @ApiOperation(value = "Get Plan By ID",
            notes = "Use this endpoint to retrieve information about a single Plan by ID.  Note that this only returns information about the Plan, not about any particular *version* of the Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PlanBean.class, message = "A Plan")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlanBean getPlan(@PathParam("organizationId") String organizationId, @PathParam("planId") String planId)
            throws PlanNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        return orgFacade.getPlan(organizationId, planId);
    }

    @ApiOperation(value = "Get Plan Activity",
            notes = "This endpoint returns audit activity information about the Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit activity entries.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getPlanActivity(@PathParam("organizationId") String organizationId,
                                                             @PathParam("planId") String planId,
                                                             @QueryParam("page") int page,
                                                             @QueryParam("count") int pageSize) throws PlanNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        return orgFacade.getPlanActivity(organizationId, planId, page, pageSize);

    }

    @ApiOperation(value = "List Plans",
            notes = "Use this endpoint to get a list of all Plans in the Organization.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PlanSummaryBean.class, message = "A list of Plans.")
    })
    @GET
    @Path("/{organizationId}/plans")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlanSummaryBean> listPlans(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        return orgFacade.listPlans(organizationId);
    }

    @ApiOperation(value = "Update Plan",
            notes = "Use this endpoint to update information about a Plan.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/plans/{planId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updatePlan(@PathParam("organizationId") String organizationId, @PathParam("planId") String planId, UpdatePlanBean bean) throws PlanNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        orgFacade.updatePlan(organizationId, planId, bean);
    }

    @ApiOperation(value = "Create Plan Version",
            notes = "Use this endpoint to create a new version of the Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PlanVersionBean.class, message = "Full details about the newly created Plan version. When trying to get, update, or remove an plan that does not exist")
    })
    @POST
    @Path("/{organizationId}/plans/{planId}/versions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PlanVersionBean createPlanVersion(@PathParam("organizationId") String organizationId, @PathParam("planId") String planId, NewPlanVersionBean bean) throws PlanNotFoundException, NotAuthorizedException, InvalidVersionException, PlanVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateVersion(bean.getVersion());
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        return orgFacade.createPlanVersion(organizationId, planId, bean);
    }

    @ApiOperation(value = "Get Plan Version",
            notes = "Use this endpoint to get detailed information about a single version of")
    @ApiResponses({
            @ApiResponse(code = 200, response = PlanVersionBean.class, message = "An Plan version.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlanVersionBean getPlanVersion(@PathParam("organizationId") String organizationId,
                                          @PathParam("planId") String planId,
                                          @PathParam("version") String version)
            throws PlanVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getPlanVersion(organizationId, planId, version);
    }

    @ApiOperation(value = "Get Plan Version Activity",
            notes = "Use this endpoint to get audit activity information for a single version of the Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit entries.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/versions/{version}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getPlanVersionActivity(
            @PathParam("organizationId") String organizationId,
            @PathParam("planId") String planId,
            @PathParam("version") String version,
            @QueryParam("page") int page,
            @QueryParam("count") int pageSize) throws PlanVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getPlanVersionActivity(organizationId, planId, version, page, pageSize);
    }

    @ApiOperation(value = "List Plan Versions",
            notes = "Use this endpoint to list all of the versions of a Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PlanVersionSummaryBean.class, message = "A list of Plans.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlanVersionSummaryBean> listPlanVersions(@PathParam("organizationId") String organizationId,
                                                         @PathParam("planId") String planId) throws PlanNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        return orgFacade.listPlanVersions(organizationId, planId);
    }

    @ApiOperation(value = "Add Plan Policy",
            notes = "Use this endpoint to add a new Policy to the Plan version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full details about the newly added Policy.")
    })
    @POST
    @Path("/{organizationId}/plans/{planId}/versions/{version}/policies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean createPlanPolicy(@PathParam("organizationId") String organizationId,
                                       @PathParam("planId") String planId,
                                       @PathParam("version") String version,
                                       NewPolicyBean bean) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.createPlanPolicy(organizationId, planId, version, bean);
    }

    @ApiOperation(value = "Get Plan Policy",
            notes = "Use this endpoint to get information about a single Policy in the Plan version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PolicyBean.class, message = "Full information about the Policy.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/versions/{version}/policies/{policyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PolicyBean getPlanPolicy(@PathParam("organizationId") String organizationId,
                                    @PathParam("planId") String planId,
                                    @PathParam("version") String version,
                                    @PathParam("policyId") long policyId)
            throws OrganizationNotFoundException, PlanVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getPlanPolicy(organizationId, planId, version, policyId);
    }

    @ApiOperation(value = "Update Plan Policy",
            notes = "Use this endpoint to update the meta-data or configuration of a single Plan Policy.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/plans/{planId}/versions/{version}/policies/{policyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updatePlanPolicy(@PathParam("organizationId") String organizationId,
                                 @PathParam("planId") String planId,
                                 @PathParam("version") String version,
                                 @PathParam("policyId") long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.updatePlanPolicy(organizationId, planId, version, policyId, bean);
    }

    @ApiOperation(value = "Remove Plan Policy",
            notes = "Use this endpoint to remove a Policy from the Plan.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/plans/{planId}/versions/{version}/policies/{policyId}")
    public void deletePlanPolicy(@PathParam("organizationId") String organizationId,
                                 @PathParam("planId") String planId,
                                 @PathParam("version") String version,
                                 @PathParam("policyId") long policyId)
            throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.deletePlanPolicy(organizationId, planId, version, policyId);
    }

    @ApiOperation(value = "List All Plan Policies",
            notes = "Use this endpoint to list all of the Policies configured for the Plan.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = PolicySummaryBean.class, message = "A List of Policies.")
    })
    @GET
    @Path("/{organizationId}/plans/{planId}/versions/{version}/policies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicySummaryBean> listPlanPolicies(@PathParam("organizationId") String organizationId,
                                                    @PathParam("planId") String planId,
                                                    @PathParam("version") String version)
            throws OrganizationNotFoundException, PlanVersionNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.listPlanPolicies(organizationId, planId, version);
    }

    @ApiOperation(value = "Re-Order Plan Policies",
            notes = "Use this endpoint to change the order of Policies for a Plan.  When a Policy is added to the Plan, it is added as the last Policy in the list of Plan Policies.  Sometimes the order of Policies is important, so it is often useful to re-order the Policies by invoking this endpoint.  The body of the request should include all of the Policies for the Plan, in the new desired order.  Note that only the IDs of each of the Policies is actually required in the request, at a minimum.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Path("/{organizationId}/plans/{planId}/versions/{version}/reorderPolicies")
    @Consumes(MediaType.APPLICATION_JSON)
    public void reorderPlanPolicies(@PathParam("organizationId") String organizationId,
                                    @PathParam("planId") String planId,
                                    @PathParam("version") String version,
                                    PolicyChainBean policyChain) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        orgFacade.reorderPlanPolicies(organizationId, planId, version, policyChain);
    }


    @ApiOperation(value = "Grant Membership(s)",
            notes = "Grant membership in a role to a user.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Path("/{organizationId}/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    public void grant(@PathParam("organizationId") String organizationId, GrantRolesBean bean) throws OrganizationNotFoundException,
            RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        orgFacade.grant(organizationId, bean);
    }

    @ApiOperation(value = "Revoke Single Membership",
            notes = "Revoke membership in a role.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/roles/{roleId}/{userId}")
    public void revoke(@PathParam("organizationId") String organizationId,
                       @PathParam("roleId") String roleId,
                       @PathParam("userId") String userId)
            throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(roleId));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        orgFacade.revoke(organizationId, roleId, userId);
    }

    @ApiOperation(value = "Revoke All Memberships",
            notes = "Revoke all of a user's role memberships from the org.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/members/{userId}")
    public void revokeAll(@PathParam("organizationId") String organizationId, @PathParam("userId") String userId) throws OrganizationNotFoundException,
            RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        orgFacade.revokeAll(organizationId, userId);
    }

    @ApiOperation(value = "List Organization Members",
            notes = "Lists all members of the organization.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = MemberBean.class, message = "List of members.")
    })
    @GET
    @Path("/{organizationId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MemberBean> listMembers(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        return orgFacade.listMembers(organizationId);
    }

    @ApiOperation(value = "Test endpoint: Clean organization",
            notes = "Remove a complete organization. This endpoint is meant for testing purpose.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = MemberBean.class, message = "List of members.")
    })
    @DELETE
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteOrganization(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        orgFacade.deleteOrganization(organizationId);
    }
}
