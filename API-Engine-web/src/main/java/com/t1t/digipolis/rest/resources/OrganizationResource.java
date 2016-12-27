package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.announcements.NewAnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.brandings.ServiceBrandingSummaryBean;
import com.t1t.digipolis.apim.beans.categories.ServiceTagsBean;
import com.t1t.digipolis.apim.beans.categories.TagBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.ContractCancellationBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractRequestBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.digipolis.apim.beans.members.MemberBean;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.digipolis.apim.beans.pagination.OAuth2TokenPaginationBean;
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
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.i18n.Messages;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.EventFacade;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.rest.impl.util.FieldValidator;
import com.t1t.digipolis.apim.rest.resources.IOrganizationResource;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private EventFacade eventFacade;
    @Inject
    private ISecurityAppContext appContext;

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
    public OrganizationBean create(NewOrganizationBean bean) throws OrganizationAlreadyExistsException, InvalidNameException, StorageException {
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New organization"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Updated organization"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        return orgFacade.activity(organizationId, page, pageSize);
    }

    /*************
     * APPLICATIONS
     **************/
    @ApiOperation(value = "Create Application",
            notes = "Use this endpoint to create a new Application.  Note that it is important to also create an initial version of the Application (e.g. v1.0).  This can either be done by including the 'initialVersion' property in the request, or by immediately following up with a call to \"Create Application Version\".  If the former is done, then a first Application version will be created automatically by this endpoint. When providing a logo, the logo can not be greater than 100k")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationBean.class, message = "Full details about the newly created Application.")
    })
    @POST
    @Path("/{organizationId}/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationBean createApp(@PathParam("organizationId") String organizationId, NewApplicationBean bean) throws OrganizationNotFoundException, ApplicationAlreadyExistsException, NotAuthorizedException, InvalidNameException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New application"));
        Preconditions.checkArgument(bean.getBase64logo().getBytes().length <= 150_000, "Logo should not be greater than 100k");
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        return orgFacade.getApp(organizationId, applicationId);
    }

    @ApiOperation(value = "Delete Application By ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/applications/{applicationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteApp(@PathParam("organizationId") String organizationId,
                                  @PathParam("applicationId") String applicationId)
            throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        orgFacade.deleteApp(organizationId, applicationId);
    }

    @ApiOperation(value = "Delete Application Version")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteAppVersion(@PathParam("organizationId") String organizationId,
                                 @PathParam("applicationId") String applicationId,
                                 @PathParam("version") String version)
            throws ApplicationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
        if (!securityContext.hasPermission(PermissionType.appAdmin, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.deleteAppVersion(organizationId, applicationId, version);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Updated application"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New application version"));
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
    public ApplicationVersionBean updateAppVersionCallbackURI(@PathParam("organizationId") String orgId, @PathParam("applicationId")String appId, @PathParam("version") String version, UpdateApplicationVersionURIBean updateAppUri) {
        if (!securityContext.hasPermission(PermissionType.appEdit, orgId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(appId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkNotNull(updateAppUri, Messages.i18n.format("nullValue", "Updated redirect URI"));
        if (updateAppUri.getUris() == null || updateAppUri.getUris().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList()).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orgFacade.updateAppVersionURI(orgId, appId, version, updateAppUri);
    }

    @Override
    @ApiOperation(value = "Reissue Application version's API key",
            notes = "Use this endpoint to revoke the application version's current API key and assign a new one")
    @ApiResponses({
            @ApiResponse(code = 200, response = NewApiKeyBean.class, message = "API key reissued")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/key-auth/reissue")
    @Produces(MediaType.APPLICATION_JSON)
    public NewApiKeyBean reissueAppVersionApiKey(@PathParam("organizationId") String orgId, @PathParam("applicationId") String appId, @PathParam("version") String version) {
        if (!securityContext.hasPermission(PermissionType.appEdit, orgId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(appId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        return orgFacade.reissueApplicationVersionApiKey(orgId, appId, version);
    }

    @Override
    @ApiOperation(value = "Reissue Application version's OAuth2 credentials",
            notes = "Use this endpoint to revoke the application version's current OAuth2 credentials and assign new credentials")
    @ApiResponses({
            @ApiResponse(code = 200, response = NewOAuthCredentialsBean.class, message = "OAuth2 credentials reissued")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/oauth2/reissue")
    @Produces(MediaType.APPLICATION_JSON)
    public NewOAuthCredentialsBean reissueAppVersionOAuthCredentials(@PathParam("organizationId") String orgId, @PathParam("applicationId") String appId, @PathParam("version") String version) {
        if (!securityContext.hasPermission(PermissionType.appEdit, orgId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(appId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        return orgFacade.reissueApplicationVersionOAuthCredentials(orgId, appId, version);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate), Messages.i18n.format("emptyValue", "From date"));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate), Messages.i18n.format("emptyValue", "To date"));
        Preconditions.checkNotNull(interval, Messages.i18n.format("nullValue", "Interval"));
        Preconditions.checkArgument(!StringUtils.isEmpty(interval.toString()), Messages.i18n.format("emptyValue", "Interval"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        return orgFacade.listAppVersions(organizationId, applicationId);
    }

    //Removed the create contract endpoint -> application owner can no longer create contract unilaterally, he needs to
    // request a contract from the service owner.
    /*
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.createContract(organizationId, applicationId, version, bean);
    }*/

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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Application organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New service"));
        Preconditions.checkArgument(bean.getBase64logo().getBytes().length <= 150_000, "Logo should not be greater than 100k");
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkNotNull(announcementBean, Messages.i18n.format("nullValue", "New announcement"));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementBean.getDescription()), Messages.i18n.format("emptyValue", "Description"));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementBean.getTitle()), Messages.i18n.format("emptyValue", "Title"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementId), Messages.i18n.format("emptyValue", "Announcement ID"));
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
        //This endpoint is also available as an API-Engine-auth endpoint, so security check seems superfluous
        //if (!securityContext.hasPermission(PermissionType.svcView, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        return orgFacade.getServiceAnnouncements(organizationId, serviceId);
    }

    @ApiOperation(value = "Remove Service Announcement",
            notes = "Use this endpoint to remove an Announcement from the Service.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/announcement/{announcementId}")
    public void deleteServiceAnnouncement(@PathParam("organizationId") String organizationId,
                                    @PathParam("serviceId") String serviceId,
                                    @PathParam("announcementId") String announcementId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(announcementId), Messages.i18n.format("emptyValue", "Announcement ID"));
        orgFacade.deleteServiceAnnouncement(organizationId, serviceId, Long.parseLong(announcementId.trim(), 10));
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
    public ServiceBean updateServiceTerms(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, UpdateServiceTermsBean serviceTerms) throws ServiceNotFoundException, NotAuthorizedException{
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(serviceTerms, Messages.i18n.format("nullValue", "Service terms"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceTerms.getTerms()), Messages.i18n.format("emptyValue", "Service terms"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        return orgFacade.getService(organizationId, serviceId);
    }

    @ApiOperation(value = "Delete Plan ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content"),
            @ApiResponse(code = 412, message = "preconditions not met")
    })
    @DELETE
    @Path("/{organizationId}/plans/{planId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deletePlan(@PathParam("organizationId") String organizationId, @PathParam("planId") String planId) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        orgFacade.deletePlan(organizationId, planId);
    }

    @ApiOperation(value = "Delete Plan Version")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content"),
            @ApiResponse(code = 412, message = "preconditions not met")
    })
    @DELETE
    @Path("/{organizationId}/plans/{planId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deletePlanVersion(@PathParam("organizationId") String organizationId, @PathParam("planId") String planId, @PathParam("version") String version) throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        orgFacade.deletePlanVersion(organizationId, planId, version);
    }

    @ApiOperation(value = "Delete Service By ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content"),
            @ApiResponse(code = 409, message = "service has contracts")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteService(@PathParam("organizationId") String organizationId,
                          @PathParam("serviceId") String serviceId)
            throws ServiceNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        orgFacade.deleteService(organizationId, serviceId);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New service version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Set<String> consentPrefixes = new HashSet<>();
        try {
            consentPrefixes.addAll(query.getManagedAppPrefixesForTypes(Collections.singletonList(ManagedApplicationTypes.Consent)));
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
        ServiceVersionBean svb = orgFacade.getServiceVersion(organizationId, serviceId, version);
        //TODO - Remove provision key from bean
        /*if (!(securityContext.hasPermission(PermissionType.svcEdit, organizationId) || consentPrefixes.contains(appContext.getApplicationPrefix()))) {
            svb.setProvisionKey(null);
        }*/
        return svb;
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
            Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Service organization ID"));
            Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
            Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
            ServiceVersionBean serviceVersion = getServiceVersion(organizationId, serviceId, version);
            InputStream definition = orgFacade.getServiceDefinition(organizationId, serviceId, version);
            if (definition == null) return null;
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        return orgFacade.getServiceVersionEndpointInfo(organizationId, serviceId, version);
    }

    @ApiOperation(value = "Get Service Availabilities",
                  notes = "Use this endpoint to get information about the available marketplaces that are defined on the API.")
    @ApiResponses({@ApiResponse(code = 200, response = ServiceVersionVisibilityBean.class, message = "Available API marketplaces information.")})
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/availability")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceVersionVisibilityBean getServiceVersionAvailabilityInfo(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,
                                                                           @PathParam("version") String version) throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        ServiceVersionVisibilityBean svab = new ServiceVersionVisibilityBean();
        //TODO do this for a service
        svab.setAvailableMarketplaces(orgFacade.getServiceVersionAvailabilityInfo(organizationId, serviceId, version));
        return svab;
    }

    @ApiOperation(value = "Get Service Plugins",
                  notes = "Use this endpoint to get information about the plugins that are applied on this service.")
    @ApiResponses({@ApiResponse(code = 200, response = KongPluginConfigList.class, message = "Available API plugin information.")})
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plugins")
    @Produces(MediaType.APPLICATION_JSON)
    public KongPluginConfigList getServiceVersionPluginInfo(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,
                                                                          @PathParam("version") String version) throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        KongPluginConfigList servicePlugins = orgFacade.getServicePlugins(organizationId, serviceId, version);
        return servicePlugins;
    }

    //Enabling and disabling plugins is now done through the update service policy endpoint

    /*@ApiOperation(value = "Enable Service Plugins",
                  notes = "Use this endpoint to enable an existing plugin for a service.")
    @ApiResponses({@ApiResponse(code = 200, response = KongPluginConfig.class, message = "Enabled plugin for a service.")})
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plugins/{pluginId}/enable")
    @Produces(MediaType.APPLICATION_JSON)
    public KongPluginConfig enableServicePlugin(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,
                                                            @PathParam("version") String version, @PathParam("pluginId") String pluginId) throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(pluginId));
        KongPluginConfig servicePlugin = orgFacade.changeEnabledStateServicePlugin(organizationId, serviceId, version, pluginId,true);
        return servicePlugin;
    }

    @ApiOperation(value = "Disable Service Plugins",
                  notes = "Use this endpoint to disable an existing plugin for a service.")
    @ApiResponses({@ApiResponse(code = 200, response = KongPluginConfig.class, message = "Disabled plugin for a service.")})
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/plugins/{pluginId}/disable")
    @Produces(MediaType.APPLICATION_JSON)
    public KongPluginConfig disableServicePlugin(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId,
                                                @PathParam("version") String version, @PathParam("pluginId") String pluginId) throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(pluginId));
        KongPluginConfig servicePlugin = orgFacade.changeEnabledStateServicePlugin(organizationId, serviceId, version, pluginId,false);
        return servicePlugin;
    }*/

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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
                                                   UpdateServiceVersionBean bean) throws ServiceVersionNotFoundException, NotAuthorizedException, StorageException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Updated service version"));
        //it's possible not to provide the endpoint directly
        if(!StringUtils.isEmpty(bean.getEndpoint())) Preconditions.checkArgument(ValidationUtils.isValidURL(bean.getEndpoint()), Messages.i18n.format("InvalidURL", bean.getEndpoint()));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Service organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Service organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
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
    public PolicyBean updateServicePolicy(@PathParam("organizationId") String organizationId,
                                    @PathParam("serviceId") String serviceId,
                                    @PathParam("version") String version,
                                    @PathParam("policyId") long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Service organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
        return orgFacade.updateServicePolicy(organizationId, serviceId, version, policyId, bean);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
            @PathParam("version") String version) throws InvalidMetricCriteriaException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
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
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New support ticket"));
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Service organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getTitle()), Messages.i18n.format("emptyValue", "Title"));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getDescription()), Messages.i18n.format("emptyValue", "Description"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getTitle()), Messages.i18n.format("emptyValue", "Ticket title"));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getDescription()), Messages.i18n.format("emptyValue", "Ticket description"));
        return orgFacade.updateServiceSupportTicket(organizationId, serviceId, Long.parseLong(supportId.trim(), 10), bean);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        return orgFacade.getServiceSupportTicket(organizationId, serviceId, Long.parseLong(supportId.trim(), 10));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        orgFacade.deleteSupportTicket(organizationId, serviceId, Long.parseLong(supportId.trim(), 10));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
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
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Support comment"));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getComment()), Messages.i18n.format("emptyValue", "Support comment"));
        return orgFacade.addServiceSupportComment(Long.parseLong(supportId.trim(), 10), bean);
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
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Comment update"));
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(commentId), Messages.i18n.format("emptyValue", "Comment ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(bean.getComment()), Messages.i18n.format("emptyValue", "Support comment"));
        return orgFacade.updateServiceSupportComment(Long.parseLong(supportId.trim(), 10), Long.parseLong(commentId.trim(), 10), bean);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(commentId), Messages.i18n.format("emptyValue", "Comment ID"));
        orgFacade.deleteServiceSupportComment(Long.parseLong(supportId.trim(), 10), Long.parseLong(commentId.trim(), 10));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(commentId), Messages.i18n.format("emptyValue", "Comment ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(supportId), Messages.i18n.format("emptyValue", "Support ID"));
        return orgFacade.listServiceSupportTicketComments(Long.parseLong(supportId.trim(), 10));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate), Messages.i18n.format("emptyValue", "From date"));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate), Messages.i18n.format("emptyValue", "To date"));
        Preconditions.checkNotNull(interval, Messages.i18n.format("nullValue", "Interval"));
        Preconditions.checkArgument(!StringUtils.isEmpty(interval.toString()), Messages.i18n.format("emptyValue", "Interval"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate), Messages.i18n.format("emptyValue", "From date"));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate), Messages.i18n.format("emptyValue", "To date"));
        Preconditions.checkNotNull(interval, Messages.i18n.format("nullValue", "Interval"));
        Preconditions.checkArgument(!StringUtils.isEmpty(interval.toString()), Messages.i18n.format("emptyValue", "Interval"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(fromDate), Messages.i18n.format("emptyValue", "From date"));
        Preconditions.checkArgument(!StringUtils.isEmpty(toDate), Messages.i18n.format("emptyValue", "To date"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New plan"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Plan version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(planId), Messages.i18n.format("emptyValue", "Plan ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        orgFacade.reorderPlanPolicies(organizationId, planId, version, policyChain);
    }


    @ApiOperation(value = "Grant Membership",
            notes = "Grant membership in a role to a user.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Path("/{organizationId}/members")
    @Consumes(MediaType.APPLICATION_JSON)
    public void grant(@PathParam("organizationId") String organizationId, GrantRoleBean bean) throws OrganizationNotFoundException,
            RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        orgFacade.grant(organizationId, bean);
    }

    @ApiOperation(value = "Revoke Single Membership",
            notes = "Revoke membership in a role.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/members/{userId}/{roleId}")
    public void revoke(@PathParam("organizationId") String organizationId,
                       @PathParam("userId") String userId,
                       @PathParam("roleId") String roleId)
            throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(roleId), Messages.i18n.format("emptyValue", "Role ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        orgFacade.revoke(organizationId, roleId, userId);
    }

    @ApiOperation(value = "Update Membership Role",
            notes = "Update a user's role within the org.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{organizationId}/members/{userId}")
    public void updateMembership(@PathParam("organizationId") String organizationId,
                                 @PathParam("userId") String userId,
                                 GrantRoleBean bean)
            throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        orgFacade.updateMembership(organizationId, userId, bean);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        orgFacade.revokeAll(organizationId, userId);
    }

    @ApiOperation(value = "Transfer Organization Ownership",
            notes = "Transfer organization ownership to another member of the organization.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Path("/{organizationId}/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void transferOrgOwnership(@PathParam("organizationId") String organizationId,
                                     TransferOwnershipBean bean) throws OrganizationNotFoundException,
            MemberNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.transferOrgOwnership(organizationId, bean);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        return orgFacade.listMembers(organizationId);
    }

    @ApiOperation(value = "Delete Organization",notes = "Delete a complete organization, when plans and services are already deleted up-front.")
    @ApiResponses({
                          @ApiResponse(code = 204, message = "successful, no content"),
                          @ApiResponse(code = 412, message = "preconditions not met")
                  })
    @DELETE
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteOrganization(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.deleteOrganization(organizationId);
    }

    @ApiOperation(value = "Request membership for organization",
            notes = "This action will result in a mail sent to the organization owners, in order for them to manually add the user to the organization.")
    @ApiResponses({
            @ApiResponse(code = 204, response = MemberBean.class, message = "Request sent.")
    })
    @POST
    @Path("/{organizationId}/request-membership")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestMembership(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException, NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        orgFacade.requestMembership(organizationId);
    }

    @Override
    @ApiOperation(value = "Reject a user's membership request",
            notes = "Call this endpoint to reject a user's membership requests to your organization")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Request rejected")
    })
    @POST
    @Path("/{organizationId}/membership-requests/{userId}/reject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void rejectMembershipRequest(@PathParam("organizationId") String organizationId, @PathParam("userId") String userId) throws NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.rejectMembershipRequest(organizationId, userId);
    }

    @Override
    @ApiOperation(value = "Get all incoming events for organization",
            notes = "Call this endpoint to get an organization's incoming events (only users with owner rights can call this endpoint)")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of incoming events for {organizationId}")
    })
    @GET
    @Path("/{organizationId}/notifications/incoming")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventBean> getOrganizationAllIncomingEvents(@PathParam("organizationId") String organizationId) throws NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgEdit, organizationId)) {
            //If user doesn't have correct permission, don't throw unauthorized, but return empty list
            return Collections.emptyList();
        }
        return eventFacade.getOrganizationIncomingEvents(organizationId);
    }

    @Override
    @ApiOperation(value = "Get all outgoing events for an organization",
            notes = "Call this endpoint to get all outgoing events for an organization")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of outgoing events for {organizationId}")
    })
    @GET
    @Path("/{organizationId}/notifications/outgoing")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventBean> getOrganizationAllOutgoingEvents(@PathParam("organizationId") String organizationId) throws NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgEdit, organizationId)) {
            //If user doesn't have correct permission, don't throw unauthorized, but return empty list
            return Collections.emptyList();
        }
        return eventFacade.getOrganizationOutgoingEvents(organizationId);
    }

    @Override
    @ApiOperation(value = "Get an organization's incoming events by type",
            notes = "Call this endpoint to get an organization's incoming events by type (MEMBERSHIP_PENDING, CONTRACT_PENDING, CONTRACT_ACCEPTED, CONTRACT_REJECTED)")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of incoming events for {organizationId}")
    })
    @GET
    @Path("/{organizationId}/notifications/incoming/{eventType}")
    @Produces(MediaType.APPLICATION_JSON)
    public <T> List<T> getOrganizationIncomingEventsByTypeAndStatus(@PathParam("organizationId") String organizationId, @PathParam("eventType") String type) throws NotAuthorizedException, InvalidEventException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgEdit, organizationId)) {
            //If user doesn't have correct permission, don't throw unauthorized, but return empty list
            return Collections.emptyList();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(type), Messages.i18n.format("emptyValue", "Event type"));
        return eventFacade.getOrganizationIncomingEventsByType(organizationId, type);
    }

    @Override
    @ApiOperation(value = "Get an organization's outgoing events by type",
            notes = "Call this endpoint to get an organization's outgoing events by type ((MEMBERSHIP_GRANTED, MEMBERSHIP_REFUSED, CONTRACT_PENDING, CONTRACT_ACCEPTED, CONTRACT_REJECTED)")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = EventBean.class, message = "List of outgoing events for {organizationId}")
    })
    @GET
    @Path("/{organizationId}/notifications/outgoing/{eventType}")
    @Produces(MediaType.APPLICATION_JSON)
    public <T> List<T> getOrganizationOutgoingEventsByTypeAndStatus(@PathParam("organizationId") String organizationId, @PathParam("eventType") String type) throws NotAuthorizedException, InvalidEventException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgEdit, organizationId)) {
            //If user doesn't have correct permission, don't throw unauthorized, but return empty list
            return Collections.emptyList();
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(type), Messages.i18n.format("emptyValue", "Event type"));
        return eventFacade.getOrganizationOutgoingEventsByType(organizationId, type);
    }

    @Override
    @ApiOperation(value = "Clear an incoming notification",
            notes = "Call this endpoint to delete a notification addressed to the organization. Notifications with a \"Pending\" status cannot be deleted")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Notification deleted")
    })
    @DELETE
    @Path("/{organizationId}/notifications/incoming/{notificationId}")
    public void deleteEvent(@PathParam("organizationId") String organizationId, @PathParam("notificationId") Long id) throws NotAuthorizedException, InvalidEventException, EventNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        eventFacade.deleteOrgEvent(organizationId, id);
    }

    @Override
    @ApiOperation(value = "Cancel an pending contract request",
            notes = "Call this endpoint to cancel a pending contract request.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Pending request deleted")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/contracts/requests/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cancelContractRequest(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, @PathParam("version") String version, ContractCancellationBean request) throws NotAuthorizedException, InvalidEventException, EventNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Service organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Service version"));
        Preconditions.checkNotNull(request, Messages.i18n.format("nullValue", "Contract cancellation request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getOrganizationId()), Messages.i18n.format("emptyValue", "Application organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getApplicationId()), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getVersion()), Messages.i18n.format("emptyValue", "Application version"));
        if (!securityContext.hasPermission(PermissionType.orgAdmin, request.getOrganizationId())) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.cancelContractRequest(organizationId, serviceId, version, request.getOrganizationId(), request.getApplicationId(), request.getVersion());
    }

    @Override
    @ApiOperation(value = "Cancel a pending membership request",
            notes = "Call this endpoint to cancel a pending membership request")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Pending request deleted")
    })
    @POST
    @Path("/{organizationId}/membership-requests/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cancelMembershipRequest(@PathParam("organizationId") String organizationId) throws InvalidEventException, EventNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(securityContext.getCurrentUser()), Messages.i18n.format("emptyValue", "User ID"));
        orgFacade.cancelMembershipRequest(organizationId);
    }

    @Override
    @ApiOperation(value = "Request a Service Contract",
            notes = "Use this endpoint to request a Contract between an Application and the Service.  In order to create a Contract, the caller must specify the Organization, ID, and Version of the Service.  Additionally the caller must specify the ID of the Plan it wished to use for the Contract with the Service.")
    @ApiResponses({
            @ApiResponse(code = 204, response = ContractBean.class, message = "Contract requested")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/contracts/request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ContractBean requestContract(@PathParam("organizationId") String organizationId,
                                    @PathParam("serviceId") String serviceId,
                                    @PathParam("version") String version,
                                    NewContractRequestBean bean) throws AbstractRestException {
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "Contract request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        if (!securityContext.hasPermission(PermissionType.appEdit, bean.getApplicationOrg()))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.requestContract(organizationId, serviceId, version, bean);
    }

    @Override
    @ApiOperation(value = "Reject an Application's Contract Request",
            notes = "Use this endpoint to reject a Contract request between an Application and the Service.  In order to reject a request, the caller must specify the Organization, ID, and Version of the Application.  Additionally the caller must specify the ID of the Plan it wished to use for the Contract with the Service.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Contract rejected")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts/reject")
    @Consumes(MediaType.APPLICATION_JSON)
    public void rejectContractRequest(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, @PathParam("version") String version, NewContractBean response) throws NotAuthorizedException {
        Preconditions.checkNotNull(response, Messages.i18n.format("nullValue", "Contract request rejection"));
        Preconditions.checkArgument(!StringUtils.isEmpty(response.getServiceOrgId()), Messages.i18n.format("emptyValue", "Service organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        if (!securityContext.hasPermission(PermissionType.svcAdmin, response.getServiceOrgId()))
            throw ExceptionFactory.notAuthorizedException();
        orgFacade.rejectContractRequest(organizationId, applicationId, version, response);
    }

    @Override
    @ApiOperation(value = "Accept an Application's Contract Request",
            notes = "Use this endpoint to accpet a Contract request between an Application and the Service.  In order to create a Contract, the caller must specify the Organization, ID, and Version of the Application.  Additionally the caller must specify the ID of the Plan it wished to use for the Contract with the Service.")
    @ApiResponses({
            @ApiResponse(code = 204, response = ContractBean.class, message = "Contract Accepted")
    })
    @POST
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ContractBean acceptContractRequest(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, @PathParam("version") String version, NewContractBean response) throws NotAuthorizedException {
        Preconditions.checkNotNull(response, Messages.i18n.format("nullValue", "Contract request acceptance"));
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId), Messages.i18n.format("emptyValue", "Application ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Application version"));
        Preconditions.checkArgument(!StringUtils.isEmpty(response.getServiceOrgId()), Messages.i18n.format("emptyValue", "Service organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcAdmin, response.getServiceOrgId()))
            throw ExceptionFactory.notAuthorizedException();
        return orgFacade.acceptContractRequest(organizationId, applicationId, version, response);
    }

    @ApiOperation(value = "Delete Service version")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content"),
            @ApiResponse(code = 409, message = "service has contracts")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteServiceVersion(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, @PathParam("version") String version) throws NotAuthorizedException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkArgument(!StringUtils.isEmpty(version), Messages.i18n.format("emptyValue", "Version"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.deleteServiceVersion(organizationId, serviceId, version);
    }

    @Override
    @ApiOperation(value = "Get Service tags")
    @ApiResponses({
            @ApiResponse(code = 200, response = ServiceTagsBean.class, message = "Service tags")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceTagsBean getTags(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        return orgFacade.getServiceTags(organizationId, serviceId);
    }

    @Override
    @ApiOperation("Update Service Tags")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @PUT
    @Path("/{organizationId}/services/{serviceId}/tags")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateTags(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, ServiceTagsBean tags) throws NotAuthorizedException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkNotNull(tags, Messages.i18n.format("nullValue", "Tags"));
        orgFacade.updateServiceTags(organizationId, serviceId, tags);
    }

    @Override
    @ApiOperation("Delete Service Tag")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/tags")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteTag(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, TagBean tag) throws NotAuthorizedException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkNotNull(tag, Messages.i18n.format("nullValue", "Tags"));
        orgFacade.deleteServiceTag(organizationId, serviceId, tag);
    }

    @Override
    @ApiOperation("Add Service Tag")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/tags")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addTag(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, TagBean tag) throws NotAuthorizedException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyValue", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(serviceId), Messages.i18n.format("emptyValue", "Service ID"));
        Preconditions.checkNotNull(tag, Messages.i18n.format("nullValue", "Tag"));
        orgFacade.addServiceTag(organizationId, serviceId, tag);
    }

    @Override
    @ApiOperation("Retrieve Application Version Oauth2 Tokens")
    @ApiResponses({
        @ApiResponse(code = 200, responseContainer = "List", response = OAuth2TokenPaginationBean.class, message = "OAuth2 Tokens")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/oauth2/tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public OAuth2TokenPaginationBean getApplicationVersionOAuthTokens(@PathParam("organizationId") String organizationId, @PathParam("applicationId") String applicationId, @PathParam("version") String version, @QueryParam("page") String offset) throws NotAuthorizedException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId) && StringUtils.isNotEmpty(applicationId) && StringUtils.isNotEmpty(version), Messages.i18n.format("emptyValue", "Organization ID, application ID & version"));
        if (!securityContext.hasPermission(PermissionType.appAdmin, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        return orgFacade.getApplicationVersionOAuthTokens(organizationId, applicationId, version, offset);
    }

    @Override
    @ApiOperation("Add branding to service")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @POST
    @Path("/{organizationId}/services/{serviceId}/brandings")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addBrandingToService(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, ServiceBrandingSummaryBean branding) throws NotAuthorizedException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId) && StringUtils.isNotEmpty(serviceId) && branding != null && StringUtils.isNotEmpty(branding.getId()), Messages.i18n.format("emptyValue", "Organization ID, service ID & branding ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.addServiceBranding(organizationId, serviceId, branding.getId());
    }

    @Override
    @ApiOperation("Remove branding from service")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @DELETE
    @Path("/{organizationId}/services/{serviceId}/brandings/{brandingId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeBrandingFromService(@PathParam("organizationId") String organizationId, @PathParam("serviceId") String serviceId, @PathParam("brandingId") String brandingId) throws NotAuthorizedException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId) && StringUtils.isNotEmpty(serviceId) && StringUtils.isNotEmpty(brandingId), Messages.i18n.format("emptyValue", "Organization ID, service ID & branding ID"));
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.removeServiceBranding(organizationId, serviceId, brandingId);
    }
}
