package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.digipolis.apim.beans.audit.data.MembershipData;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.members.MemberBean;
import com.t1t.digipolis.apim.beans.members.MemberRoleBean;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.digipolis.apim.beans.plans.*;
import com.t1t.digipolis.apim.beans.policies.*;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaFilterOperator;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.logging.ApimanLogger;
import com.t1t.digipolis.apim.core.logging.IApimanLogger;
import com.t1t.digipolis.apim.core.util.PolicyTemplateUtil;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.rest.impl.audit.AuditUtils;
import com.t1t.digipolis.apim.rest.impl.i18n.Messages;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.impl.util.FieldValidator;
import com.t1t.digipolis.apim.rest.resources.IOrganizationResource;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.rest.resources.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

@Api(value = "/organizations", description = "The Organization API.")
@Path("/organizations")
@ApplicationScoped
public class OrganizationResourceImpl implements IOrganizationResource {

    @SuppressWarnings("nls")
    public static final String [] DATE_FORMATS = {
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ssz",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd",
        "EEE, dd MMM yyyy HH:mm:ss z",
        "EEE, dd MMM yyyy HH:mm:ss",
        "EEE, dd MMM yyyy"
    };

    private static final long ONE_MINUTE_MILLIS = 1 * 60 * 1000;
    private static final long ONE_HOUR_MILLIS = 1 * 60 * 60 * 1000;
    private static final long ONE_DAY_MILLIS = 1 * 24 * 60 * 60 * 1000;
    private static final long ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000;
    private static final long ONE_MONTH_MILLIS = 30 * 24 * 60 * 60 * 1000;

    @Inject IStorage storage;
    @Inject IIdmStorage idmStorage;
    @Inject IStorageQuery query;
    @Inject IMetricsAccessor metrics;

    @Inject IApplicationValidator applicationValidator;
    @Inject IServiceValidator serviceValidator;
    @Inject IApiKeyGenerator apiKeyGenerator;

    @Inject
    IUserResource users;
    @Inject
    IRoleResource roles;

    @Inject ISecurityContext securityContext;
    @Inject IGatewayLinkFactory gatewayLinkFactory;

    @Context HttpServletRequest request;

    @Inject @ApimanLogger(OrganizationResourceImpl.class) IApimanLogger log;

    /**
     * Constructor.
     */
    public OrganizationResourceImpl() {
    }

    /*************ORGANIZATION**************/

    @ApiOperation(value = "Create Organization",
            notes = "Use this endpoint to create a new Organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationBean.class, message = "Full details about the Organization that was created.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrganizationBean create(NewOrganizationBean bean) throws OrganizationAlreadyExistsException, InvalidNameException {
        FieldValidator.validateName(bean.getName());

        List<RoleBean> autoGrantedRoles = null;
        SearchCriteriaBean criteria = new SearchCriteriaBean();
        criteria.setPage(1);
        criteria.setPageSize(100);
        criteria.addFilter("autoGrant", "true", SearchCriteriaFilterOperator.bool_eq); //$NON-NLS-1$ //$NON-NLS-2$
        try {
            autoGrantedRoles = idmStorage.findRoles(criteria).getBeans();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }

        if ("true".equals(System.getProperty("apiman.manager.require-auto-granted-org", "true"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            if (autoGrantedRoles.isEmpty()) {
                throw new SystemErrorException(Messages.i18n.format("OrganizationResourceImpl.NoAutoGrantRoleAvailable")); //$NON-NLS-1$
            }
        }

        OrganizationBean orgBean = new OrganizationBean();
        orgBean.setName(bean.getName());
        orgBean.setDescription(bean.getDescription());
        orgBean.setId(BeanUtils.idFromName(bean.getName()));
        orgBean.setCreatedOn(new Date());
        orgBean.setCreatedBy(securityContext.getCurrentUser());
        orgBean.setModifiedOn(new Date());
        orgBean.setModifiedBy(securityContext.getCurrentUser());
        try {
            // Store/persist the new organization
            storage.beginTx();
            if (storage.getOrganization(orgBean.getId()) != null) {
                throw ExceptionFactory.organizationAlreadyExistsException(bean.getName());
            }
            storage.createOrganization(orgBean);
            storage.createAuditEntry(AuditUtils.organizationCreated(orgBean, securityContext));
            storage.commitTx();

            // Auto-grant memberships in roles to the creator of the organization
            for (RoleBean roleBean : autoGrantedRoles) {
                String currentUser = securityContext.getCurrentUser();
                String orgId = orgBean.getId();
                RoleMembershipBean membership = RoleMembershipBean.create(currentUser, roleBean.getId(), orgId);
                membership.setCreatedOn(new Date());
                idmStorage.createMembership(membership);
            }

            log.debug(String.format("Created organization %s: %s", orgBean.getName(), orgBean)); //$NON-NLS-1$
            return orgBean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
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
        try {
            storage.beginTx();
            OrganizationBean organizationBean = storage.getOrganization(organizationId);
            if (organizationBean == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            storage.commitTx();
            log.debug(String.format("Got organization %s: %s", organizationBean.getName(), organizationBean)); //$NON-NLS-1$
            return organizationBean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
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
        if (!securityContext.hasPermission(PermissionType.orgEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            OrganizationBean orgForUpdate = storage.getOrganization(organizationId);
            if (orgForUpdate == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }

            EntityUpdatedData auditData = new EntityUpdatedData();
            if (AuditUtils.valueChanged(orgForUpdate.getDescription(), bean.getDescription())) {
                auditData.addChange("description", orgForUpdate.getDescription(), bean.getDescription()); //$NON-NLS-1$
                orgForUpdate.setDescription(bean.getDescription());
            }
            storage.updateOrganization(orgForUpdate);
            storage.createAuditEntry(AuditUtils.organizationUpdated(orgForUpdate, auditData, securityContext));
            storage.commitTx();
            log.debug(String.format("Updated organization %s: %s", orgForUpdate.getName(), orgForUpdate)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get Organization Activity",
            notes = "Returns audit activity information for a single Organization.  The audit information that is returned represents all of the activity associated with this Organization (i.e. an audit log for everything in the Organization).")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "List of audit/activity entries.")
    })
    @GET
    @Path("/{organizationId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> activity(@PathParam("organizationId") String organizationId,@QueryParam("page")  int page,@QueryParam("count")  int pageSize)
            throws OrganizationNotFoundException, NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, null, null, null, paging);
            return rval;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    /*************APPLICATIONS**************/
    @ApiOperation(value = "Create Application",
            notes = "Use this endpoint to create a new Application.  Note that it is important to also create an initial version of the Application (e.g. 1.0).  This can either be done by including the 'initialVersion' property in the request, or by immediately following up with a call to \"Create Application Version\".  If the former is done, then a first Application version will be created automatically by this endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationBean.class, message = "Full details about the newly created Application.")
    })
    @POST
    @Path("/{organizationId}/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationBean createApp(@PathParam("organizationId") String organizationId, NewApplicationBean bean)
            throws OrganizationNotFoundException, ApplicationAlreadyExistsException, NotAuthorizedException,
            InvalidNameException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateName(bean.getName());

        ApplicationBean newApp = new ApplicationBean();
        newApp.setId(BeanUtils.idFromName(bean.getName()));
        newApp.setName(bean.getName());
        newApp.setDescription(bean.getDescription());
        newApp.setCreatedBy(securityContext.getCurrentUser());
        newApp.setCreatedOn(new Date());
        try {
            // Store/persist the new application
            storage.beginTx();
            OrganizationBean org = storage.getOrganization(organizationId);
            if (org == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            newApp.setOrganization(org);

            if (storage.getApplication(org.getId(), newApp.getId()) != null) {
                throw ExceptionFactory.organizationAlreadyExistsException(bean.getName());
            }

            storage.createApplication(newApp);
            storage.createAuditEntry(AuditUtils.applicationCreated(newApp, securityContext));

            if (bean.getInitialVersion() != null) {
                NewApplicationVersionBean newAppVersion = new NewApplicationVersionBean();
                newAppVersion.setVersion(bean.getInitialVersion());
                createAppVersionInternal(newAppVersion, newApp);
            }

            storage.commitTx();

            log.debug(String.format("Created application %s: %s", newApp.getName(), newApp)); //$NON-NLS-1$
            return newApp;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get Application By ID",
            notes = "Use this endpoint to retrieve information about a single Application by ID.  Note that this only returns information about the Application, not about any particular *version* of the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationBean.class, message = "An Application.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationBean getApp(@PathParam("organizationId") String organizationId,@PathParam("applicationId") String applicationId)
            throws ApplicationNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            ApplicationBean applicationBean = storage.getApplication(organizationId, applicationId);
            if (applicationBean == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }
            storage.commitTx();
            log.debug(String.format("Got application %s: %s", applicationBean.getName(), applicationBean)); //$NON-NLS-1$
            return applicationBean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get Application Activity",
            notes = "This endpoint returns audit activity information about the Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "A list of audit activity entries.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getAppActivity(@PathParam("organizationId") String organizationId,@PathParam("applicationId")  String applicationId,
                                                            @QueryParam("page") int page,@QueryParam("count")  int pageSize) throws ApplicationNotFoundException, NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, applicationId, null, ApplicationBean.class, paging);
            return rval;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "List Applications",
            notes = "Use this endpoint to get a list of all Applications in the Organization.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = ApplicationSummaryBean.class, message = "A list of Applications.")
    })
    @GET
    @Path("/{organizationId}/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationSummaryBean> listApps(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        get(organizationId);

        try {
            return query.getApplicationsInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
    public void updateApp(@PathParam("organizationId") String organizationId,@PathParam("applicationId") String applicationId, UpdateApplicationBean bean)
            throws ApplicationNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            ApplicationBean appForUpdate = storage.getApplication(organizationId, applicationId);
            if (appForUpdate == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }
            EntityUpdatedData auditData = new EntityUpdatedData();
            if (AuditUtils.valueChanged(appForUpdate.getDescription(), bean.getDescription())) {
                auditData.addChange("description", appForUpdate.getDescription(), bean.getDescription()); //$NON-NLS-1$
                appForUpdate.setDescription(bean.getDescription());
            }
            storage.updateApplication(appForUpdate);
            storage.createAuditEntry(AuditUtils.applicationUpdated(appForUpdate, auditData, securityContext));
            storage.commitTx();
            log.debug(String.format("Updated application %s: %s", appForUpdate.getName(), appForUpdate)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
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
    public ApplicationVersionBean createAppVersion(@PathParam("organizationId") String organizationId,@PathParam("applicationId")  String applicationId,
            NewApplicationVersionBean bean) throws ApplicationNotFoundException, NotAuthorizedException,
            InvalidVersionException, ApplicationVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateVersion(bean.getVersion());

        ApplicationVersionBean newVersion;
        try {
            storage.beginTx();
            ApplicationBean application = storage.getApplication(organizationId, applicationId);
            if (application == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }

            if (storage.getApplicationVersion(organizationId, applicationId, bean.getVersion()) != null) {
                throw ExceptionFactory.applicationVersionAlreadyExistsException(applicationId, bean.getVersion());
            }

            newVersion = createAppVersionInternal(bean, application);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }

        if (bean.isClone() && bean.getCloneVersion() != null) {
            try {
                List<ContractSummaryBean> contracts = getApplicationVersionContracts(organizationId, applicationId, bean.getCloneVersion());
                for (ContractSummaryBean contract : contracts) {
                    NewContractBean ncb = new NewContractBean();
                    ncb.setPlanId(contract.getPlanId());
                    ncb.setServiceId(contract.getServiceId());
                    ncb.setServiceOrgId(contract.getServiceOrganizationId());
                    ncb.setServiceVersion(contract.getServiceVersion());
                    createContract(organizationId, applicationId, newVersion.getVersion(), ncb);
                }
                List<PolicySummaryBean> policies = listAppPolicies(organizationId, applicationId, bean.getCloneVersion());
                for (PolicySummaryBean policySummary : policies) {
                    PolicyBean policy = getAppPolicy(organizationId, applicationId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(policy.getConfiguration());
                    createAppPolicy(organizationId, applicationId, newVersion.getVersion(), npb);
                }
            } catch (Exception e) {
                // TODO it's ok if the clone fails - we did our best
            }
        }

        return newVersion;
    }

    /**
     * Creates a new application version.
     * @param bean
     * @param application
     * @throws StorageException
     */
    protected ApplicationVersionBean createAppVersionInternal(NewApplicationVersionBean bean,
            ApplicationBean application) throws StorageException {
        if (!BeanUtils.isValidVersion(bean.getVersion())) {
            throw new StorageException("Invalid/illegal application version: " + bean.getVersion()); //$NON-NLS-1$
        }

        ApplicationVersionBean newVersion = new ApplicationVersionBean();
        newVersion.setApplication(application);
        newVersion.setCreatedBy(securityContext.getCurrentUser());
        newVersion.setCreatedOn(new Date());
        newVersion.setModifiedBy(securityContext.getCurrentUser());
        newVersion.setModifiedOn(new Date());
        newVersion.setStatus(ApplicationStatus.Created);
        newVersion.setVersion(bean.getVersion());

        storage.createApplicationVersion(newVersion);
        storage.createAuditEntry(AuditUtils.applicationVersionCreated(newVersion, securityContext));

        log.debug(String.format("Created new application version %s: %s", newVersion.getApplication().getName(), newVersion)); //$NON-NLS-1$
        return newVersion;
    }

    @ApiOperation(value = "Get Application Version",
            notes = "Use this endpoint to get detailed information about a single version of an Application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ApplicationVersionBean.class, message = "An Application version.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationVersionBean getAppVersion(@PathParam("organizationId") String organizationId,@PathParam("applicationId")  String applicationId,@PathParam("version")  String version)
            throws ApplicationVersionNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            ApplicationVersionBean applicationVersion = storage.getApplicationVersion(organizationId, applicationId, version);
            if (applicationVersion == null) {
                throw ExceptionFactory.applicationVersionNotFoundException(applicationId, version);
            }
            storage.commitTx();
            log.debug(String.format("Got new application version %s: %s", applicationVersion.getApplication().getName(), applicationVersion)); //$NON-NLS-1$
            return applicationVersion;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
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
                                                                   @QueryParam("count") int pageSize)
            throws ApplicationVersionNotFoundException, NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, applicationId, version, ApplicationBean.class, paging);
            return rval;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
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
                                                        @QueryParam("from") String fromDate,
                                                        @QueryParam("to") String toDate) throws NotAuthorizedException,
            InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.appView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getAppUsagePerService(organizationId, applicationId, version, from, to);
    }

    @ApiOperation(value = "List Application Versions",
            notes = "Use this endpoint to list all of the versions of an Application.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = ApplicationVersionSummaryBean.class, message = "A list of Applications versions.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationVersionSummaryBean> listAppVersions(@PathParam("organizationId") String organizationId,@PathParam("applicationId")  String applicationId)
            throws ApplicationNotFoundException, NotAuthorizedException {
        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getApp(organizationId, applicationId);

        try {
            return query.getApplicationVersions(organizationId, applicationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get System Status",
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
            ServiceNotFoundException, PlanNotFoundException, ContractAlreadyExistsException,
            NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        try {
            storage.beginTx();
            ContractBean contract = createContractInternal(organizationId, applicationId, version, bean);

            storage.commitTx();
            log.debug(String.format("Created new contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$
            return contract;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            // Up above we are optimistically creating the contract.  If it fails, check to see
            // if it failed because it was a duplicate.  If so, throw something sensible.  We
            // only do this on failure (we would get a FK contraint failure, for example) to
            // reduce overhead on the typical happy path.
            if (contractAlreadyExists(organizationId, applicationId, version, bean)) {
                throw ExceptionFactory.contractAlreadyExistsException();
            } else {
                throw new SystemErrorException(e);
            }
        }
    }

    /**
     * Creates a contract.
     * @param organizationId
     * @param applicationId
     * @param version
     * @param bean
     * @throws StorageException
     * @throws Exception
     */
    protected ContractBean createContractInternal(String organizationId, String applicationId,
            String version, NewContractBean bean) throws StorageException, Exception {
        ContractBean contract;
        ApplicationVersionBean avb;
        avb = storage.getApplicationVersion(organizationId, applicationId, version);
        if (avb == null) {
            throw ExceptionFactory.applicationNotFoundException(applicationId);
        }
        if (avb.getStatus() == ApplicationStatus.Registered || avb.getStatus() == ApplicationStatus.Retired) {
            throw ExceptionFactory.invalidApplicationStatusException();
        }
        ServiceVersionBean svb = storage.getServiceVersion(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion());
        if (svb == null) {
            throw ExceptionFactory.serviceNotFoundException(bean.getServiceId());
        }
        if (svb.getStatus() != ServiceStatus.Published) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        Set<ServicePlanBean> plans = svb.getPlans();
        String planVersion = null;
        if (plans != null) {
            for (ServicePlanBean servicePlanBean : plans) {
                if (servicePlanBean.getPlanId().equals(bean.getPlanId())) {
                    planVersion = servicePlanBean.getVersion();
                }
            }
        }
        if (planVersion == null) {
            throw ExceptionFactory.planNotFoundException(bean.getPlanId());
        }
        PlanVersionBean pvb = storage.getPlanVersion(bean.getServiceOrgId(), bean.getPlanId(), planVersion);
        if (pvb == null) {
            throw ExceptionFactory.planNotFoundException(bean.getPlanId());
        }
        if (pvb.getStatus() != PlanStatus.Locked) {
            throw ExceptionFactory.invalidPlanStatusException();
        }

        contract = new ContractBean();
        contract.setApplication(avb);
        contract.setService(svb);
        contract.setPlan(pvb);
        contract.setCreatedBy(securityContext.getCurrentUser());
        contract.setCreatedOn(new Date());
        contract.setApikey(apiKeyGenerator.generate());

        // Validate the state of the application.
        if (applicationValidator.isReady(avb, true)) {
            avb.setStatus(ApplicationStatus.Ready);
        }

        storage.createContract(contract);
        storage.createAuditEntry(AuditUtils.contractCreatedFromApp(contract, securityContext));
        storage.createAuditEntry(AuditUtils.contractCreatedToService(contract, securityContext));

        // Update the version with new meta-data (e.g. modified-by)
        avb.setModifiedBy(securityContext.getCurrentUser());
        avb.setModifiedOn(new Date());
        storage.updateApplicationVersion(avb);

        return contract;
    }

    /**
     * Check to see if the contract already exists, by getting a list of all the
     * application's contracts and comparing with the one being created.
     * @param organizationId
     * @param applicationId
     * @param version
     * @param bean
     */
    private boolean contractAlreadyExists(String organizationId, String applicationId, String version,
            NewContractBean bean) {
        try {
            List<ContractSummaryBean> contracts = query.getApplicationContracts(organizationId, applicationId, version);
            for (ContractSummaryBean contract : contracts) {
                if (contract.getServiceOrganizationId().equals(bean.getServiceOrgId()) &&
                    contract.getServiceId().equals(bean.getServiceId()) &&
                    contract.getServiceVersion().equals(bean.getServiceVersion()) &&
                    contract.getPlanId().equals(bean.getPlanId()))
                {
                    return true;
                }
            }
            return false;
        } catch (StorageException e) {
            return false;
        }
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
        boolean hasPermission = securityContext.hasPermission(PermissionType.appView, organizationId);
        try {
            storage.beginTx();
            ContractBean contract = storage.getContract(contractId);
            if (contract == null)
                throw ExceptionFactory.contractNotFoundException(contractId);

            storage.commitTx();

            // Hide some data if the user doesn't have the appView permission
            if (!hasPermission) {
                contract.setApikey(null);
            }

            log.debug(String.format("Got contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$
            return contract;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
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
                                   @PathParam("version")  String version)
            throws ApplicationNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        List<ContractSummaryBean> contracts = getApplicationVersionContracts(organizationId, applicationId, version);
        for (ContractSummaryBean contract : contracts) {
            deleteContract(organizationId, applicationId, version, contract.getContractId());
        }
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
                               @PathParam("contractId") Long contractId)
            throws ApplicationNotFoundException, ContractNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            ContractBean contract = storage.getContract(contractId);
            if (contract == null) {
                throw ExceptionFactory.contractNotFoundException(contractId);
            }
            if (!contract.getApplication().getApplication().getOrganization().getId().equals(organizationId)) {
                throw ExceptionFactory.contractNotFoundException(contractId);
            }
            if (!contract.getApplication().getApplication().getId().equals(applicationId)) {
                throw ExceptionFactory.contractNotFoundException(contractId);
            }
            if (!contract.getApplication().getVersion().equals(version)) {
                throw ExceptionFactory.contractNotFoundException(contractId);
            }
            storage.deleteContract(contract);
            storage.createAuditEntry(AuditUtils.contractBrokenFromApp(contract, securityContext));
            storage.createAuditEntry(AuditUtils.contractBrokenToService(contract, securityContext));

            storage.commitTx();
            log.debug(String.format("Deleted contract: %s", contract)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "List All Contracts for an Application",
            notes = "Use this endpoint to get a list of all Contracts for an Application.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = ContractSummaryBean.class, message = "A list of Contracts.")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/contracts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContractSummaryBean> getApplicationVersionContracts(@PathParam("organizationId") String organizationId,
                                                                    @PathParam("applicationId") String applicationId,
                                                                    @PathParam("version")String version)
            throws ApplicationNotFoundException, NotAuthorizedException {
        boolean hasPermission = securityContext.hasPermission(PermissionType.appView, organizationId);

        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getAppVersion(organizationId, applicationId, version);

        try {
            List<ContractSummaryBean> contracts = query.getApplicationContracts(organizationId, applicationId, version);

            // Hide some stuff if the user doesn't have the appView permission
            if (!hasPermission) {
                for (ContractSummaryBean contract : contracts) {
                    contract.setApikey(null);
                }
            }

            return contracts;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
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
                                              @PathParam("applicationId")String applicationId,
                                              @PathParam("version") String version)
            throws ApplicationNotFoundException, NotAuthorizedException {
        return getApiRegistry(organizationId, applicationId, version);
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
                                             @PathParam("version")  String version) throws ApplicationNotFoundException, NotAuthorizedException {
        return getApiRegistry(organizationId, applicationId, version);
    }

    /**
     * Gets the API registry.
     * @param organizationId
     * @param applicationId
     * @param version
     * @throws ApplicationNotFoundException
     * @throws NotAuthorizedException
     */
    protected ApiRegistryBean getApiRegistry(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException {
        boolean hasPermission = securityContext.hasPermission(PermissionType.appView, organizationId);
        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getAppVersion(organizationId, applicationId, version);

        Map<String, IGatewayLink> gatewayLinks = new HashMap<>();
        Map<String, GatewayBean> gateways = new HashMap<>();
        boolean txStarted = false;
        try {
            ApiRegistryBean apiRegistry = query.getApiRegistry(organizationId, applicationId, version);

            // Hide some stuff if the user doesn't have the appView permission
            if (!hasPermission) {
                List<ApiEntryBean> apis = apiRegistry.getApis();
                for (ApiEntryBean api : apis) {
                    api.setApiKey(null);
                }
            }

            List<ApiEntryBean> apis = apiRegistry.getApis();

            storage.beginTx();
            txStarted = true;
            for (ApiEntryBean api : apis) {
                String gatewayId = api.getGatewayId();
                // Don't return the gateway id.
                api.setGatewayId(null);
                GatewayBean gateway = gateways.get(gatewayId);
                if (gateway == null) {
                    gateway = storage.getGateway(gatewayId);
                    gateways.put(gatewayId, gateway);
                }
                IGatewayLink link = gatewayLinks.get(gatewayId);
                if (link == null) {
                    link = gatewayLinkFactory.create(gateway);
                    gatewayLinks.put(gatewayId, link);
                }

                ServiceEndpoint se = link.getServiceEndpoint(api.getServiceOrgId(), api.getServiceId(), api.getServiceVersion());
                String apiEndpoint = se.getEndpoint();
                api.setHttpEndpoint(apiEndpoint);
            }

            return apiRegistry;
        } catch (StorageException|GatewayAuthenticationException e) {
            throw new SystemErrorException(e);
        } finally {
            if (txStarted) {
                storage.rollbackTx();
            }
            for (IGatewayLink link : gatewayLinks.values()) {
                link.close();
            }
        }
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

        // Make sure the app version exists and is in the right state.
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        if (avb.getStatus() == ApplicationStatus.Registered || avb.getStatus() == ApplicationStatus.Retired) {
            throw ExceptionFactory.invalidApplicationStatusException();
        }

        return doCreatePolicy(organizationId, applicationId, version, bean, PolicyType.Application);
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
                                   @PathParam("applicationId")String applicationId,
                                   @PathParam("version") String version,
                                   @PathParam("policyId") long policyId) throws OrganizationNotFoundException, ApplicationVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        boolean hasPermission = securityContext.hasPermission(PermissionType.appView, organizationId);
        // Make sure the app version exists
        getAppVersion(organizationId, applicationId, version);
        PolicyBean policy = doGetPolicy(PolicyType.Application, organizationId, applicationId, version, policyId);
        if (!hasPermission) {
            policy.setConfiguration(null);
        }
        return policy;
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

        // Make sure the app version exists.
        getAppVersion(organizationId, applicationId, version);

        try {
            storage.beginTx();
            PolicyBean policy = this.storage.getPolicy(PolicyType.Application, organizationId, applicationId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                policy.setConfiguration(bean.getConfiguration());
                // TODO figure out what changed an include that in the audit entry
            }
            policy.setModifiedOn(new Date());
            policy.setModifiedBy(this.securityContext.getCurrentUser());
            storage.updatePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyUpdated(policy, PolicyType.Application, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Remove Application Policy",
            notes = "Use this endpoint to remove a Policy from the Application.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies/{policyId}")
    public void deleteAppPolicy(@PathParam("organizationId") String organizationId,
                                @PathParam("applicationId") String applicationId,@PathParam("version") String version,@PathParam("policyId")  long policyId)
            throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the app version exists;
        ApplicationVersionBean app = getAppVersion(organizationId, applicationId, version);
        if (app.getStatus() == ApplicationStatus.Registered || app.getStatus() == ApplicationStatus.Retired) {
            throw ExceptionFactory.invalidApplicationStatusException();
        }

        try {
            storage.beginTx();
            PolicyBean policy = this.storage.getPolicy(PolicyType.Application, organizationId, applicationId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.deletePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyRemoved(policy, PolicyType.Application, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "List All Application Policies",
            notes = "Use this endpoint to list all of the Policies configured for the Application.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = PolicySummaryBean.class, message = "System status information")
    })
    @GET
    @Path("/{organizationId}/applications/{applicationId}/versions/{version}/policies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PolicySummaryBean> listAppPolicies(@PathParam("organizationId") String organizationId,
                                                   @PathParam("applicationId") String applicationId,
                                                   @PathParam("version") String version)
            throws OrganizationNotFoundException, ApplicationVersionNotFoundException, NotAuthorizedException {
        // Try to get the application first - will throw an exception if not found.
        getAppVersion(organizationId, applicationId, version);

        try {
            return query.getPolicies(organizationId, applicationId, version, PolicyType.Application);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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

        // Make sure the app version exists.
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);

        try {
            storage.beginTx();
            List<Long> newOrder = new ArrayList<>(policyChain.getPolicies().size());
            for (PolicySummaryBean psb : policyChain.getPolicies()) {
                newOrder.add(psb.getId());
            }
            storage.reorderPolicies(PolicyType.Application, organizationId, applicationId, version, newOrder);
            storage.createAuditEntry(AuditUtils.policiesReordered(avb, PolicyType.Application, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Create Service",
            notes = "Use this endpoint to create a new Service.  Note that it is important to also\n" +
                    "     * create an initial version of the Service (e.g. 1.0).  This can either be done\n" +
                    "     * by including the 'initialVersion' property in the request, or by immediately following\n" +
                    "     * up with a call to \"Create Service Version\".  If the former is done, then a first\n" +
                    "     * Service version will be created automatically by this endpoint.")
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
        FieldValidator.validateName(bean.getName());

        ServiceBean newService = new ServiceBean();
        newService.setName(bean.getName());
        newService.setDescription(bean.getDescription());
        newService.setId(BeanUtils.idFromName(bean.getName()));
        newService.setCreatedOn(new Date());
        newService.setCreatedBy(securityContext.getCurrentUser());
        try {
            GatewaySummaryBean gateway = getSingularGateway();

            storage.beginTx();
            OrganizationBean orgBean = storage.getOrganization(organizationId);
            if (orgBean == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            if (storage.getService(orgBean.getId(), newService.getId()) != null) {
                throw ExceptionFactory.serviceAlreadyExistsException(bean.getName());
            }
            newService.setOrganization(orgBean);
            // Store/persist the new service
            storage.createService(newService);
            storage.createAuditEntry(AuditUtils.serviceCreated(newService, securityContext));

            if (bean.getInitialVersion() != null) {
                NewServiceVersionBean newServiceVersion = new NewServiceVersionBean();
                newServiceVersion.setVersion(bean.getInitialVersion());
                createServiceVersionInternal(newServiceVersion, newService, gateway);
            }

            storage.commitTx();
            return newService;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    @GET
    @Path("/{organizationId}/services/{serviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceBean getService(@PathParam("organizationId") String organizationId,@PathParam("serviceId")  String serviceId)
            throws ServiceNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            storage.commitTx();
            return bean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceActivity(String, String, int, int)
     */
    @Override
    public SearchResultsBean<AuditEntryBean> getServiceActivity(String organizationId, String serviceId,
            int page, int pageSize) throws ServiceNotFoundException, NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, serviceId, null, ServiceBean.class, paging);
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listServices(String)
     */
    @Override
    public List<ServiceSummaryBean> listServices(String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        // make sure the org exists
        get(organizationId);

        try {
            return query.getServicesInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#updateService(String, String, com.t1t.digipolis.apim.beans.services.UpdateServiceBean)
     */
    @Override
    public void updateService(String organizationId, String serviceId, UpdateServiceBean bean)
            throws ServiceNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            ServiceBean serviceForUpdate = storage.getService(organizationId, serviceId);
            if (serviceForUpdate == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            EntityUpdatedData auditData = new EntityUpdatedData();
            if (AuditUtils.valueChanged(serviceForUpdate.getDescription(), bean.getDescription())) {
                auditData.addChange("description", serviceForUpdate.getDescription(), bean.getDescription()); //$NON-NLS-1$
                serviceForUpdate.setDescription(bean.getDescription());
            }
            storage.updateService(serviceForUpdate);
            storage.createAuditEntry(AuditUtils.serviceUpdated(serviceForUpdate, auditData, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#createServiceVersion(String, String, com.t1t.digipolis.apim.beans.services.NewServiceVersionBean)
     */
    @Override
    public ServiceVersionBean createServiceVersion(String organizationId, String serviceId,
            NewServiceVersionBean bean) throws ServiceNotFoundException, NotAuthorizedException,
            InvalidVersionException, ServiceVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateVersion(bean.getVersion());

        ServiceVersionBean newVersion = null;
        try {
            GatewaySummaryBean gateway = getSingularGateway();

            storage.beginTx();
            ServiceBean service = storage.getService(organizationId, serviceId);
            if (service == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }

            if (storage.getServiceVersion(organizationId, serviceId, bean.getVersion()) != null) {
                throw ExceptionFactory.serviceVersionAlreadyExistsException(serviceId, bean.getVersion());
            }

            newVersion = createServiceVersionInternal(bean, service, gateway);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }

        if (bean.isClone() && bean.getCloneVersion() != null) {
            try {
                ServiceVersionBean cloneSource = getServiceVersion(organizationId, serviceId, bean.getCloneVersion());

                // Clone primary attributes of the service version
                UpdateServiceVersionBean updatedService = new UpdateServiceVersionBean();
                updatedService.setEndpoint(cloneSource.getEndpoint());
                updatedService.setEndpointType(cloneSource.getEndpointType());
                updatedService.setEndpointProperties(cloneSource.getEndpointProperties());
                updatedService.setGateways(cloneSource.getGateways());
                updatedService.setPlans(cloneSource.getPlans());
                updatedService.setPublicService(cloneSource.isPublicService());
                newVersion = updateServiceVersion(organizationId, serviceId, bean.getVersion(), updatedService );

                // Clone the service definition document
                try {
                    Response response = getServiceDefinition(organizationId, serviceId, bean.getCloneVersion());
                    InputStream definition = (InputStream) response.getEntity();
                    storeServiceDefinition(organizationId, serviceId, newVersion.getVersion(),
                            cloneSource.getDefinitionType(), definition);
                } catch (ServiceDefinitionNotFoundException svnfe) {
                    // This is ok - it just means the service doesn't have one, so do nothing.
                } catch (Exception sdnfe) {
                    log.error("Unable to create response", sdnfe); //$NON-NLS-1$
                }

                // Clone all service policies
                List<PolicySummaryBean> policies = listServicePolicies(organizationId, serviceId, bean.getCloneVersion());
                for (PolicySummaryBean policySummary : policies) {
                    PolicyBean policy = getServicePolicy(organizationId, serviceId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(policy.getConfiguration());
                    createServicePolicy(organizationId, serviceId, newVersion.getVersion(), npb);
                }
            } catch (Exception e) {
                // TODO it's ok if the clone fails - we did our best
                if (e != null) {
                    Throwable t = e;
                    e = (Exception) t;
                }
            }
        }

        return newVersion;
    }

    /**
     * Creates a service version.
     * @param bean
     * @param service
     * @param gateway
     * @throws Exception
     * @throws StorageException
     */
    protected ServiceVersionBean createServiceVersionInternal(NewServiceVersionBean bean,
            ServiceBean service, GatewaySummaryBean gateway) throws Exception, StorageException {
        if (!BeanUtils.isValidVersion(bean.getVersion())) {
            throw new StorageException("Invalid/illegal service version: " + bean.getVersion()); //$NON-NLS-1$
        }

        ServiceVersionBean newVersion = new ServiceVersionBean();
        newVersion.setVersion(bean.getVersion());
        newVersion.setCreatedBy(securityContext.getCurrentUser());
        newVersion.setCreatedOn(new Date());
        newVersion.setModifiedBy(securityContext.getCurrentUser());
        newVersion.setModifiedOn(new Date());
        newVersion.setStatus(ServiceStatus.Created);
        newVersion.setService(service);

        if (gateway != null) {
            if (newVersion.getGateways() == null) {
                newVersion.setGateways(new HashSet<ServiceGatewayBean>());
                ServiceGatewayBean sgb = new ServiceGatewayBean();
                sgb.setGatewayId(gateway.getId());
                newVersion.getGateways().add(sgb);
            }
        }

        if (serviceValidator.isReady(newVersion)) {
            newVersion.setStatus(ServiceStatus.Ready);
        } else {
            newVersion.setStatus(ServiceStatus.Created);
        }

        // Ensure all of the plans are in the right status (locked)
        Set<ServicePlanBean> plans = newVersion.getPlans();
        if (plans != null) {
            for (ServicePlanBean splanBean : plans) {
                String orgId = newVersion.getService().getOrganization().getId();
                PlanVersionBean pvb = storage.getPlanVersion(orgId, splanBean.getPlanId(), splanBean.getVersion());
                if (pvb == null) {
                    throw new StorageException(Messages.i18n.format("PlanVersionDoesNotExist", splanBean.getPlanId(), splanBean.getVersion())); //$NON-NLS-1$
                }
                if (pvb.getStatus() != PlanStatus.Locked) {
                    throw new StorageException(Messages.i18n.format("PlanNotLocked", splanBean.getPlanId(), splanBean.getVersion())); //$NON-NLS-1$
                }
            }
        }

        storage.createServiceVersion(newVersion);
        storage.createAuditEntry(AuditUtils.serviceVersionCreated(newVersion, securityContext));
        return newVersion;
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceVersion(String, String, String)
     */
    @Override
    public ServiceVersionBean getServiceVersion(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException {
        boolean hasPermission = securityContext.hasPermission(PermissionType.svcView, organizationId);
        try {
            storage.beginTx();
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            storage.commitTx();
            if (!hasPermission) {
                serviceVersion.setGateways(null);
            }
            decryptEndpointProperties(serviceVersion);
            return serviceVersion;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceDefinition(String, String, String)
     */
    @Override
    public Response getServiceDefinition(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            if (serviceVersion.getDefinitionType() == ServiceDefinitionType.None || serviceVersion.getDefinitionType() == null) {
                throw ExceptionFactory.serviceDefinitionNotFoundException(serviceId, version);
            }
            InputStream  definition = storage.getServiceDefinition(serviceVersion);
            if (definition == null) {
                throw ExceptionFactory.serviceDefinitionNotFoundException(serviceId, version);
            }
            ResponseBuilder builder = Response.ok().entity(definition);
            if (serviceVersion.getDefinitionType() == ServiceDefinitionType.SwaggerJSON) {
                builder.type(MediaType.APPLICATION_JSON);
            } else if (serviceVersion.getDefinitionType() == ServiceDefinitionType.SwaggerYAML) {
                builder.type("application/x-yaml"); //$NON-NLS-1$
            } else if (serviceVersion.getDefinitionType() == ServiceDefinitionType.WSDL) {
                builder.type("application/wsdl+xml"); //$NON-NLS-1$
            } else {
                throw new Exception("Service definition type not supported: " + serviceVersion.getDefinitionType()); //$NON-NLS-1$
            }
            storage.commitTx();
            return builder.build();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceVersionEndpointInfo(String, String, String)
     */
    @Override
    public ServiceVersionEndpointSummaryBean getServiceVersionEndpointInfo(String organizationId,
            String serviceId, String version) throws ServiceVersionNotFoundException,
            InvalidServiceStatusException, GatewayNotFoundException {
        try {
            storage.beginTx();
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            if (serviceVersion.getStatus() != ServiceStatus.Published) {
                throw new InvalidServiceStatusException(Messages.i18n.format("ServiceNotPublished")); //$NON-NLS-1$
            }
            Set<ServiceGatewayBean> gateways = serviceVersion.getGateways();
            if (gateways.isEmpty()) {
                throw new SystemErrorException("No Gateways for published Service!"); //$NON-NLS-1$
            }
            GatewayBean gateway = storage.getGateway(gateways.iterator().next().getGatewayId());
            if (gateway == null) {
                throw new GatewayNotFoundException();
            }
            IGatewayLink link = gatewayLinkFactory.create(gateway);
            ServiceEndpoint endpoint = link.getServiceEndpoint(organizationId, serviceId, version);
            ServiceVersionEndpointSummaryBean rval = new ServiceVersionEndpointSummaryBean();
            rval.setManagedEndpoint(endpoint.getEndpoint());
            storage.commitTx();
            log.debug(String.format("Got endpoint summary: %s", gateway)); //$NON-NLS-1$
            return rval;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceVersionActivity(String, String, String, int, int)
     */
    @Override
    public SearchResultsBean<AuditEntryBean> getServiceVersionActivity(String organizationId,
            String serviceId, String version, int page, int pageSize) throws ServiceVersionNotFoundException,
            NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, serviceId, version, ServiceBean.class, paging);
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#updateServiceVersion(String, String, String, com.t1t.digipolis.apim.beans.services.UpdateServiceVersionBean)
     */
    @Override
    public ServiceVersionBean updateServiceVersion(String organizationId, String serviceId, String version,
            UpdateServiceVersionBean bean) throws ServiceVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        if (svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }

        svb.setModifiedBy(securityContext.getCurrentUser());
        svb.setModifiedOn(new Date());
        EntityUpdatedData data = new EntityUpdatedData();
        if (AuditUtils.valueChanged(svb.getPlans(), bean.getPlans())) {
            data.addChange("plans", AuditUtils.asString_ServicePlanBeans(svb.getPlans()), AuditUtils.asString_ServicePlanBeans(bean.getPlans())); //$NON-NLS-1$
            if (svb.getPlans() == null) {
                svb.setPlans(new HashSet<ServicePlanBean>());
            }
            svb.getPlans().clear();
            if (bean.getPlans() != null) {
                svb.getPlans().addAll(bean.getPlans());
            }
        }
        if (AuditUtils.valueChanged(svb.getGateways(), bean.getGateways())) {
            data.addChange("gateways", AuditUtils.asString_ServiceGatewayBeans(svb.getGateways()), AuditUtils.asString_ServiceGatewayBeans(bean.getGateways())); //$NON-NLS-1$
            if (svb.getGateways() == null) {
                svb.setGateways(new HashSet<ServiceGatewayBean>());
            }
            svb.getGateways().clear();
            svb.getGateways().addAll(bean.getGateways());
        }
        if (AuditUtils.valueChanged(svb.getEndpoint(), bean.getEndpoint())) {
            data.addChange("endpoint", svb.getEndpoint(), bean.getEndpoint()); //$NON-NLS-1$
            svb.setEndpoint(bean.getEndpoint());
        }
        if (AuditUtils.valueChanged(svb.getEndpointType(), bean.getEndpointType())) {
            data.addChange("endpointType", svb.getEndpointType(), bean.getEndpointType()); //$NON-NLS-1$
            svb.setEndpointType(bean.getEndpointType());
        }
        if (AuditUtils.valueChanged(svb.getEndpointProperties(), bean.getEndpointProperties())) {
            if (svb.getEndpointProperties() == null) {
                svb.setEndpointProperties(new HashMap<String, String>());
            } else {
                svb.getEndpointProperties().clear();
            }
            if (bean.getEndpointProperties() != null) {
                svb.getEndpointProperties().putAll(bean.getEndpointProperties());
            }
        }
        if (AuditUtils.valueChanged(svb.isPublicService(), bean.getPublicService())) {
            data.addChange("publicService", String.valueOf(svb.isPublicService()), String.valueOf(bean.getPublicService())); //$NON-NLS-1$
            svb.setPublicService(bean.getPublicService());
        }

        try {
            if (svb.getGateways() == null || svb.getGateways().isEmpty()) {
                GatewaySummaryBean gateway = getSingularGateway();
                if (gateway != null) {
                    if (svb.getGateways() == null) {
                        svb.setGateways(new HashSet<ServiceGatewayBean>());
                        ServiceGatewayBean sgb = new ServiceGatewayBean();
                        sgb.setGatewayId(gateway.getId());
                        svb.getGateways().add(sgb);
                    }
                }
            }

            if (serviceValidator.isReady(svb)) {
                svb.setStatus(ServiceStatus.Ready);
            } else {
                svb.setStatus(ServiceStatus.Created);
            }
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }

        try {
            encryptEndpointProperties(svb);
            storage.beginTx();

            // Ensure all of the plans are in the right status (locked)
            Set<ServicePlanBean> plans = svb.getPlans();
            if (plans != null) {
                for (ServicePlanBean splanBean : plans) {
                    String orgId = svb.getService().getOrganization().getId();
                    PlanVersionBean pvb = storage.getPlanVersion(orgId, splanBean.getPlanId(), splanBean.getVersion());
                    if (pvb == null) {
                        throw new StorageException(Messages.i18n.format("PlanVersionDoesNotExist", splanBean.getPlanId(), splanBean.getVersion())); //$NON-NLS-1$
                    }
                    if (pvb.getStatus() != PlanStatus.Locked) {
                        throw new StorageException(Messages.i18n.format("PlanNotLocked", splanBean.getPlanId(), splanBean.getVersion())); //$NON-NLS-1$
                    }
                }
            }

            storage.updateServiceVersion(svb);
            storage.createAuditEntry(AuditUtils.serviceVersionUpdated(svb, data, securityContext));
            storage.commitTx();
            log.debug(String.format("Successfully updated Service Version: %s", svb)); //$NON-NLS-1$
            decryptEndpointProperties(svb);
            return svb;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#updateServiceDefinition(String, String, String)
     */
    @Override
    public void updateServiceDefinition(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException, InvalidServiceStatusException {
        String contentType = request.getContentType();
        InputStream data;
        try {
            data = request.getInputStream();
        } catch (IOException e) {
            throw new SystemErrorException(e);
        }
        ServiceDefinitionType newDefinitionType = null;
        if (contentType.toLowerCase().contains("application/json")) { //$NON-NLS-1$
            newDefinitionType = ServiceDefinitionType.SwaggerJSON;
        } else if (contentType.toLowerCase().contains("application/x-yaml")) { //$NON-NLS-1$
            newDefinitionType = ServiceDefinitionType.SwaggerYAML;
        } else if (contentType.toLowerCase().contains("application/wsdl+xml")) { //$NON-NLS-1$
            newDefinitionType = ServiceDefinitionType.WSDL;
        } else {
            throw new SystemErrorException(Messages.i18n.format("InvalidServiceDefinitionContentType", contentType)); //$NON-NLS-1$
        }
        storeServiceDefinition(organizationId, serviceId, version, newDefinitionType, data);
        log.debug(String.format("Updated service definition for %s", serviceId)); //$NON-NLS-1$
    }

    /**
     * @param organizationId
     * @param serviceId
     * @param version
     * @param data
     */
    protected void storeServiceDefinition(String organizationId, String serviceId, String version,
            ServiceDefinitionType definitionType, InputStream data) {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            if (serviceVersion.getDefinitionType() != definitionType) {
                serviceVersion.setDefinitionType(definitionType);
                storage.updateServiceVersion(serviceVersion);
            }
            storage.createAuditEntry(AuditUtils.serviceDefinitionUpdated(serviceVersion, securityContext));
            storage.updateServiceDefinition(serviceVersion, data);
            storage.commitTx();
            log.debug(String.format("Stored service definition %s: %s", serviceId, serviceVersion)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listServiceVersions(String, String)
     */
    @Override
    public List<ServiceVersionSummaryBean> listServiceVersions(String organizationId, String serviceId)
            throws ServiceNotFoundException, NotAuthorizedException {
        // Try to get the service first - will throw a ServiceNotFoundException if not found.
        getService(organizationId, serviceId);

        try {
            return query.getServiceVersions(organizationId, serviceId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceVersionPlans(String, String, String)
     */
    @Override
    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId,
            String version) throws ServiceVersionNotFoundException, NotAuthorizedException {
        // Ensure the version exists first.
        getServiceVersion(organizationId, serviceId, version);

        try {
            return query.getServiceVersionPlans(organizationId, serviceId, version);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#createServicePolicy(String, String, String, com.t1t.digipolis.apim.beans.policies.NewPolicyBean)
     */
    @Override
    public PolicyBean createServicePolicy(String organizationId, String serviceId, String version,
            NewPolicyBean bean) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        if (svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }

        log.debug(String.format("Created service policy %s", svb)); //$NON-NLS-1$
        return doCreatePolicy(organizationId, serviceId, version, bean, PolicyType.Service);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServicePolicy(String, String, String, long)
     */
    @Override
    public PolicyBean getServicePolicy(String organizationId, String serviceId, String version, long policyId)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {

        // Make sure the service exists
        getServiceVersion(organizationId, serviceId, version);

        PolicyBean policy = doGetPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);

        if (!securityContext.hasPermission(PermissionType.svcView, organizationId)) {
            policy.setConfiguration(null);
        }

        return policy;
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#updateServicePolicy(String,
     *      String, String, long, com.t1t.digipolis.apim.beans.policies.UpdatePolicyBean)
     */
    @Override
    public void updateServicePolicy(String organizationId, String serviceId, String version,
            long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the service exists
        getServiceVersion(organizationId, serviceId, version);

        try {
            storage.beginTx();
            PolicyBean policy = storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            // TODO capture specific change values when auditing policy updates
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                policy.setConfiguration(bean.getConfiguration());
            }
            policy.setModifiedOn(new Date());
            policy.setModifiedBy(securityContext.getCurrentUser());
            storage.updatePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyUpdated(policy, PolicyType.Service, securityContext));
            storage.commitTx();
            log.debug(String.format("Updated service policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#deleteServicePolicy(String, String, String, long)
     */
    @Override
    public void deleteServicePolicy(String organizationId, String serviceId, String version, long policyId)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the service exists
        ServiceVersionBean service = getServiceVersion(organizationId, serviceId, version);
        if (service.getStatus() == ServiceStatus.Published || service.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }

        try {
            storage.beginTx();
            PolicyBean policy = this.storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.deletePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyRemoved(policy, PolicyType.Service, securityContext));
            storage.commitTx();
            log.debug(String.format("Deleted service %s policy: %s", serviceId, policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#deleteServiceDefinition(String, String, String)
     */
    @Override
    public void deleteServiceDefinition(String organizationId, String serviceId, String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            serviceVersion.setDefinitionType(ServiceDefinitionType.None);
            storage.createAuditEntry(AuditUtils.serviceDefinitionDeleted(serviceVersion, securityContext));
            storage.deleteServiceDefinition(serviceVersion);
            storage.updateServiceVersion(serviceVersion);
            storage.commitTx();
            log.debug(String.format("Deleted service %s definition %s", serviceId, serviceVersion)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listServicePolicies(String, String, String)
     */
    @Override
    public List<PolicySummaryBean> listServicePolicies(String organizationId, String serviceId, String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException, NotAuthorizedException {
        // Try to get the service first - will throw an exception if not found.
        getServiceVersion(organizationId, serviceId, version);

        try {
            return query.getPolicies(organizationId, serviceId, version, PolicyType.Service);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#reorderServicePolicies(String, String, String, com.t1t.digipolis.apim.beans.policies.PolicyChainBean)
     */
    @Override
    public void reorderServicePolicies(String organizationId, String serviceId, String version,
            PolicyChainBean policyChain) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);

        try {
            storage.beginTx();
            List<Long> newOrder = new ArrayList<>(policyChain.getPolicies().size());
            for (PolicySummaryBean psb : policyChain.getPolicies()) {
                newOrder.add(psb.getId());
            }
            storage.reorderPolicies(PolicyType.Service, organizationId, serviceId, version, newOrder);
            storage.createAuditEntry(AuditUtils.policiesReordered(svb, PolicyType.Service, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServicePolicyChain(String, String, String, String)
     */
    @Override
    public PolicyChainBean getServicePolicyChain(String organizationId, String serviceId, String version,
            String planId) throws ServiceVersionNotFoundException, PlanNotFoundException, NotAuthorizedException {
        // Try to get the service first - will throw an exception if not found.
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);

        try {
            String planVersion = null;
            Set<ServicePlanBean> plans = svb.getPlans();
            if (plans != null) {
                for (ServicePlanBean servicePlanBean : plans) {
                    if (servicePlanBean.getPlanId().equals(planId)) {
                        planVersion = servicePlanBean.getVersion();
                        break;
                    }
                }
            }
            if (planVersion == null) {
                throw ExceptionFactory.planNotFoundException(planId);
            }
            List<PolicySummaryBean> servicePolicies = query.getPolicies(organizationId, serviceId, version, PolicyType.Service);
            List<PolicySummaryBean> planPolicies = query.getPolicies(organizationId, planId, planVersion, PolicyType.Plan);

            PolicyChainBean chain = new PolicyChainBean();
            chain.getPolicies().addAll(planPolicies);
            chain.getPolicies().addAll(servicePolicies);
            return chain;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getServiceVersionContracts(String, String, String, int, int)
     */
    @Override
    public List<ContractSummaryBean> getServiceVersionContracts(String organizationId,
            String serviceId, String version, int page, int pageSize) throws ServiceVersionNotFoundException,
            NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }

        // Try to get the service first - will throw an exception if not found.
        getServiceVersion(organizationId, serviceId, version);

        try {
            List<ContractSummaryBean> contracts = query.getServiceContracts(organizationId, serviceId, version, page, pageSize);

            for (ContractSummaryBean contract : contracts) {
                if (!securityContext.hasPermission(PermissionType.appView, contract.getAppOrganizationId())) {
                    contract.setApikey(null);
                }
            }

            log.debug(String.format("Got service %s version %s contracts: %s", serviceId, version, contracts)); //$NON-NLS-1$
            return contracts;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getUsage(String, String, String, com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType, String, String)
     */
    @Override
    public UsageHistogramBean getUsage(String organizationId, String serviceId, String version,
            HistogramIntervalType interval, String fromDate, String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        if (interval == null) {
            interval = HistogramIntervalType.day;
        }
        validateMetricRange(from, to);
        validateTimeSeriesMetric(from, to, interval);
        return metrics.getUsage(organizationId, serviceId, version, interval, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getUsagePerApp(String, String, String, String, String)
     */
    @Override
    public UsagePerAppBean getUsagePerApp(String organizationId, String serviceId, String version,
            String fromDate, String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getUsagePerApp(organizationId, serviceId, version, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getUsagePerPlan(String, String, String, String, String)
     */
    @Override
    public UsagePerPlanBean getUsagePerPlan(String organizationId, String serviceId, String version,
            String fromDate, String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getUsagePerPlan(organizationId, serviceId, version, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getResponseStats(String, String, String, com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType, String, String)
     */
    @Override
    public ResponseStatsHistogramBean getResponseStats(String organizationId, String serviceId,
            String version, HistogramIntervalType interval, String fromDate, String toDate)
            throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        if (interval == null) {
            interval = HistogramIntervalType.day;
        }
        validateMetricRange(from, to);
        validateTimeSeriesMetric(from, to, interval);
        return metrics.getResponseStats(organizationId, serviceId, version, interval, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getResponseStatsSummary(String, String, String, String, String)
     */
    @Override
    public ResponseStatsSummaryBean getResponseStatsSummary(String organizationId, String serviceId,
            String version, String fromDate, String toDate) throws NotAuthorizedException,
            InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getResponseStatsSummary(organizationId, serviceId, version, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getResponseStatsPerApp(String, String, String, String, String)
     */
    @Override
    public ResponseStatsPerAppBean getResponseStatsPerApp(String organizationId, String serviceId,
            String version, String fromDate, String toDate) throws NotAuthorizedException,
            InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getResponseStatsPerApp(organizationId, serviceId, version, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getResponseStatsPerPlan(String, String, String, String, String)
     */
    @Override
    public ResponseStatsPerPlanBean getResponseStatsPerPlan(String organizationId, String serviceId,
            String version, String fromDate, String toDate) throws NotAuthorizedException,
            InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getResponseStatsPerPlan(organizationId, serviceId, version, from, to);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#createPlan(String,
     *      com.t1t.digipolis.apim.beans.plans.NewPlanBean)
     */
    @Override
    public PlanBean createPlan(String organizationId, NewPlanBean bean) throws OrganizationNotFoundException,
            PlanAlreadyExistsException, NotAuthorizedException, InvalidNameException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateName(bean.getName());

        PlanBean newPlan = new PlanBean();
        newPlan.setName(bean.getName());
        newPlan.setDescription(bean.getDescription());
        newPlan.setId(BeanUtils.idFromName(bean.getName()));
        newPlan.setCreatedOn(new Date());
        newPlan.setCreatedBy(securityContext.getCurrentUser());
        try {
            // Store/persist the new plan
            storage.beginTx();
            OrganizationBean orgBean = storage.getOrganization(organizationId);
            if (orgBean == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            if (storage.getPlan(orgBean.getId(), newPlan.getId()) != null) {
                throw ExceptionFactory.planAlreadyExistsException(newPlan.getName());
            }
            newPlan.setOrganization(orgBean);
            storage.createPlan(newPlan);
            storage.createAuditEntry(AuditUtils.planCreated(newPlan, securityContext));

            if (bean.getInitialVersion() != null) {
                NewPlanVersionBean newPlanVersion = new NewPlanVersionBean();
                newPlanVersion.setVersion(bean.getInitialVersion());
                createPlanVersionInternal(newPlanVersion, newPlan);
            }

            storage.commitTx();
            log.debug(String.format("Created plan: %s", newPlan)); //$NON-NLS-1$
            return newPlan;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getPlan(String, String)
     */
    @Override
    public PlanBean getPlan(String organizationId, String planId)
            throws PlanNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            PlanBean bean = storage.getPlan(organizationId, planId);
            if (bean == null) {
                throw ExceptionFactory.planNotFoundException(planId);
            }
            storage.commitTx();
            log.debug(String.format("Got plan: %s", bean)); //$NON-NLS-1$
            return bean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getPlanActivity(String, String, int, int)
     */
    @Override
    public SearchResultsBean<AuditEntryBean> getPlanActivity(String organizationId, String planId, int page, int pageSize)
            throws PlanNotFoundException, NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, planId, null, PlanBean.class, paging);
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listPlans(String)
     */
    @Override
    public List<PlanSummaryBean> listPlans(String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        get(organizationId);

        try {
            return query.getPlansInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#updatePlan(String,
     * String, com.t1t.digipolis.apim.beans.plans.UpdatePlanBean)
     */
    @Override
    public void updatePlan(String organizationId, String planId, UpdatePlanBean bean)
            throws PlanNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        EntityUpdatedData auditData = new EntityUpdatedData();
        try {
            storage.beginTx();
            PlanBean planForUpdate = storage.getPlan(organizationId, planId);
            if (planForUpdate == null) {
                throw ExceptionFactory.planNotFoundException(planId);
            }
            if (AuditUtils.valueChanged(planForUpdate.getDescription(), bean.getDescription())) {
                auditData.addChange("description", planForUpdate.getDescription(), bean.getDescription()); //$NON-NLS-1$
                planForUpdate.setDescription(bean.getDescription());
            }
            storage.updatePlan(planForUpdate);
            storage.createAuditEntry(AuditUtils.planUpdated(planForUpdate, auditData, securityContext));
            storage.commitTx();
            log.debug(String.format("Updated plan: %s", planForUpdate)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#createPlanVersion(String,
     *      String, com.t1t.digipolis.apim.beans.plans.NewPlanVersionBean)
     */
    @Override
    public PlanVersionBean createPlanVersion(String organizationId, String planId, NewPlanVersionBean bean)
            throws PlanNotFoundException, NotAuthorizedException, InvalidVersionException,
            PlanVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateVersion(bean.getVersion());

        PlanVersionBean newVersion = null;
        try {
            storage.beginTx();
            PlanBean plan = storage.getPlan(organizationId, planId);
            if (plan == null) {
                throw ExceptionFactory.planNotFoundException(planId);
            }

            if (storage.getPlanVersion(organizationId, planId, bean.getVersion()) != null) {
                throw ExceptionFactory.planVersionAlreadyExistsException(planId, bean.getVersion());
            }

            newVersion = createPlanVersionInternal(bean, plan);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }

        if (bean.isClone() && bean.getCloneVersion() != null) {
            try {
                List<PolicySummaryBean> policies = listPlanPolicies(organizationId, planId, bean.getCloneVersion());
                for (PolicySummaryBean policySummary : policies) {
                    PolicyBean policy = getPlanPolicy(organizationId, planId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(policy.getConfiguration());
                    createPlanPolicy(organizationId, planId, newVersion.getVersion(), npb);
                }
            } catch (Exception e) {
                // TODO it's ok if the clone fails - we did our best
            }
        }

        log.debug(String.format("Created plan %s version: %s", planId, newVersion)); //$NON-NLS-1$
        return newVersion;
    }

    /**
     * Creates a plan version.
     * @param bean
     * @param plan
     * @throws StorageException
     */
    protected PlanVersionBean createPlanVersionInternal(NewPlanVersionBean bean, PlanBean plan)
            throws StorageException {
        if (!BeanUtils.isValidVersion(bean.getVersion())) {
            throw new StorageException("Invalid/illegal plan version: " + bean.getVersion()); //$NON-NLS-1$
        }

        PlanVersionBean newVersion = new PlanVersionBean();
        newVersion.setCreatedBy(securityContext.getCurrentUser());
        newVersion.setCreatedOn(new Date());
        newVersion.setModifiedBy(securityContext.getCurrentUser());
        newVersion.setModifiedOn(new Date());
        newVersion.setStatus(PlanStatus.Created);
        newVersion.setPlan(plan);
        newVersion.setVersion(bean.getVersion());
        storage.createPlanVersion(newVersion);
        storage.createAuditEntry(AuditUtils.planVersionCreated(newVersion, securityContext));
        return newVersion;
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getPlanVersion(String, String, String)
     */
    @Override
    public PlanVersionBean getPlanVersion(String organizationId, String planId, String version)
            throws PlanVersionNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            PlanVersionBean planVersion = storage.getPlanVersion(organizationId, planId, version);
            if (planVersion == null) {
                throw ExceptionFactory.planVersionNotFoundException(planId, version);
            }
            storage.commitTx();
            log.debug(String.format("Got plan %s version: %s", planId, planVersion)); //$NON-NLS-1$
            return planVersion;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getPlanVersionActivity(String, String, String, int, int)
     */
    @Override
    public SearchResultsBean<AuditEntryBean> getPlanVersionActivity(String organizationId, String planId,
            String version, int page, int pageSize) throws PlanVersionNotFoundException,
            NotAuthorizedException {
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditEntity(organizationId, planId, version, PlanBean.class, paging);
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listPlanVersions(String, String)
     */
    @Override
    public List<PlanVersionSummaryBean> listPlanVersions(String organizationId, String planId)
            throws PlanNotFoundException, NotAuthorizedException {
        // Try to get the plan first - will throw a PlanNotFoundException if not found.
        getPlan(organizationId, planId);

        try {
            return query.getPlanVersions(organizationId, planId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#createPlanPolicy(String,
     *      String, String, com.t1t.digipolis.apim.beans.policies.NewPolicyBean)
     */
    @Override
    public PolicyBean createPlanPolicy(String organizationId, String planId, String version,
            NewPolicyBean bean) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the plan version exists and is in the right state
        PlanVersionBean pvb = getPlanVersion(organizationId, planId, version);
        if (pvb.getStatus() == PlanStatus.Locked) {
            throw ExceptionFactory.invalidPlanStatusException();
        }

        log.debug(String.format("Creating plan %s policy %s", planId, pvb)); //$NON-NLS-1$
        return doCreatePolicy(organizationId, planId, version, bean, PolicyType.Plan);
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#getPlanPolicy(String, String, String, long)
     */
    @Override
    public PolicyBean getPlanPolicy(String organizationId, String planId, String version, long policyId)
            throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        boolean hasPermission = securityContext.hasPermission(PermissionType.planView, organizationId);

        // Make sure the plan version exists
        getPlanVersion(organizationId, planId, version);

        PolicyBean policy = doGetPolicy(PolicyType.Plan, organizationId, planId, version, policyId);

        if (!hasPermission) {
            policy.setConfiguration(null);
        }

        log.debug(String.format("Got plan policy %s", policy)); //$NON-NLS-1$
        return policy;
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#updatePlanPolicy(String,
     *      String, String, long, com.t1t.digipolis.apim.beans.policies.UpdatePolicyBean)
     */
    @Override
    public void updatePlanPolicy(String organizationId, String planId, String version,
            long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the plan version exists
        getPlanVersion(organizationId, planId, version);

        try {
            storage.beginTx();
            PolicyBean policy = storage.getPolicy(PolicyType.Plan, organizationId, planId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                policy.setConfiguration(bean.getConfiguration());
                // Note: we do not audit the policy configuration since it may have sensitive data
            }
            policy.setModifiedOn(new Date());
            policy.setModifiedBy(this.securityContext.getCurrentUser());
            storage.updatePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyUpdated(policy, PolicyType.Plan, securityContext));
            storage.commitTx();
            log.debug(String.format("Updated plan policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#deletePlanPolicy(String, String, String, long)
     */
    @Override
    public void deletePlanPolicy(String organizationId, String planId, String version, long policyId)
            throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the plan version exists
        PlanVersionBean plan = getPlanVersion(organizationId, planId, version);
        if (plan.getStatus() == PlanStatus.Locked) {
            throw ExceptionFactory.invalidPlanStatusException();
        }

        try {
            storage.beginTx();
            PolicyBean policy = this.storage.getPolicy(PolicyType.Plan, organizationId, planId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.deletePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyRemoved(policy, PolicyType.Plan, securityContext));
            storage.commitTx();
            log.debug(String.format("Deleted plan policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listPlanPolicies(String, String, String)
     */
    @Override
    public List<PolicySummaryBean> listPlanPolicies(String organizationId, String planId, String version)
            throws OrganizationNotFoundException, PlanVersionNotFoundException, NotAuthorizedException {
        // Try to get the plan first - will throw an exception if not found.
        getPlanVersion(organizationId, planId, version);

        try {
            return query.getPolicies(organizationId, planId, version, PolicyType.Plan);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#reorderPlanPolicies(String, String, String, com.t1t.digipolis.apim.beans.policies.PolicyChainBean)
     */
    @Override
    public void reorderPlanPolicies(String organizationId, String planId, String version,
            PolicyChainBean policyChain) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();

        // Make sure the plan version exists
        PlanVersionBean pvb = getPlanVersion(organizationId, planId, version);

        try {
            storage.beginTx();
            List<Long> newOrder = new ArrayList<>(policyChain.getPolicies().size());
            for (PolicySummaryBean psb : policyChain.getPolicies()) {
                newOrder.add(psb.getId());
            }
            storage.reorderPolicies(PolicyType.Plan, organizationId, planId, version, newOrder);
            storage.createAuditEntry(AuditUtils.policiesReordered(pvb, PolicyType.Plan, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }


    /**
     * Creates a policy for the given entity (supports creating policies for applications,
     * services, and plans).
     *
     * @param organizationId
     * @param entityId
     * @param entityVersion
     * @param bean
     * @return the stored policy bean (with updated information)
     * @throws NotAuthorizedException
     */
    protected PolicyBean doCreatePolicy(String organizationId, String entityId, String entityVersion,
            NewPolicyBean bean, PolicyType type) throws PolicyDefinitionNotFoundException {
        if (bean.getDefinitionId() == null) {
            ExceptionFactory.policyDefNotFoundException("null"); //$NON-NLS-1$
        }
        PolicyDefinitionBean def = null;
        try {
            storage.beginTx();
            def = storage.getPolicyDefinition(bean.getDefinitionId());
            if (def == null) {
                throw ExceptionFactory.policyDefNotFoundException(bean.getDefinitionId());
            }
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }

        int newIdx = 0;
        try {
            newIdx = query.getMaxPolicyOrderIndex(organizationId, entityId, entityVersion, type) + 1;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }

        try {
            PolicyBean policy = new PolicyBean();
            policy.setId(null);
            policy.setDefinition(def);
            policy.setName(def.getName());
            policy.setConfiguration(bean.getConfiguration());
            policy.setCreatedBy(securityContext.getCurrentUser());
            policy.setCreatedOn(new Date());
            policy.setModifiedBy(securityContext.getCurrentUser());
            policy.setModifiedOn(new Date());
            policy.setOrganizationId(organizationId);
            policy.setEntityId(entityId);
            policy.setEntityVersion(entityVersion);
            policy.setType(type);
            policy.setOrderIndex(newIdx);

            storage.beginTx();
            storage.createPolicy(policy);
            storage.createAuditEntry(AuditUtils.policyAdded(policy, type, securityContext));
            storage.commitTx();

            PolicyTemplateUtil.generatePolicyDescription(policy);

            log.debug(String.format("Created app policy: %s", policy)); //$NON-NLS-1$
            return policy;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#grant(String, com.t1t.digipolis.apim.beans.idm.GrantRolesBean)
     */
    @Override
    public void grant(String organizationId, GrantRolesBean bean) throws OrganizationNotFoundException,
            RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        // Verify that the references are valid.
        get(organizationId);
        users.get(bean.getUserId());
        for (String roleId : bean.getRoleIds()) {
            roles.get(roleId);
        }

        MembershipData auditData = new MembershipData();
        auditData.setUserId(bean.getUserId());
        try {
            for (String roleId : bean.getRoleIds()) {
                RoleMembershipBean membership = RoleMembershipBean.create(bean.getUserId(), roleId, organizationId);
                membership.setCreatedOn(new Date());
                // If the membership already exists, that's fine!
                if (idmStorage.getMembership(bean.getUserId(), roleId, organizationId) == null) {
                    idmStorage.createMembership(membership);
                }
                auditData.addRole(roleId);
            }
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
        try {
            storage.beginTx();
            storage.createAuditEntry(AuditUtils.membershipGranted(organizationId, auditData, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#revoke(String, String, String)
     */
    @Override
    public void revoke(String organizationId, String roleId, String userId)
            throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException,
            NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        get(organizationId);
        users.get(userId);
        roles.get(roleId);

        MembershipData auditData = new MembershipData();
        auditData.setUserId(userId);
        boolean revoked = false;
        try {
            idmStorage.deleteMembership(userId, roleId, organizationId);
            auditData.addRole(roleId);
            revoked = true;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }

        if (revoked) {
            try {
                storage.beginTx();
                storage.createAuditEntry(AuditUtils.membershipRevoked(organizationId, auditData, securityContext));
                storage.commitTx();
                log.debug(String.format("Revoked User %s Role %s Org %s", userId, roleId, organizationId)); //$NON-NLS-1$
            } catch (AbstractRestException e) {
                storage.rollbackTx();
                throw e;
            } catch (Exception e) {
                storage.rollbackTx();
                throw new SystemErrorException(e);
            }
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#revokeAll(String, String)
     */
    @Override
    public void revokeAll(String organizationId, String userId) throws OrganizationNotFoundException,
            RoleNotFoundException, UserNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.orgAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        get(organizationId);
        users.get(userId);

        try {
            idmStorage.deleteMemberships(userId, organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }

        MembershipData auditData = new MembershipData();
        auditData.setUserId(userId);
        auditData.addRole("*"); //$NON-NLS-1$
        try {
            storage.beginTx();
            storage.createAuditEntry(AuditUtils.membershipRevoked(organizationId, auditData, securityContext));
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IOrganizationResource#listMembers(String)
     */
    @Override
    public List<MemberBean> listMembers(String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        get(organizationId);

        try {
            Set<RoleMembershipBean> memberships = idmStorage.getOrgMemberships(organizationId);
            TreeMap<String, MemberBean> members = new TreeMap<>();
            for (RoleMembershipBean membershipBean : memberships) {
                String userId = membershipBean.getUserId();
                MemberBean member = members.get(userId);
                if (member == null) {
                    UserBean user = idmStorage.getUser(userId);
                    member = new MemberBean();
                    member.setEmail(user.getEmail());
                    member.setUserId(userId);
                    member.setUserName(user.getFullName());
                    member.setRoles(new ArrayList<MemberRoleBean>());
                    members.put(userId, member);
                }
                String roleId = membershipBean.getRoleId();
                RoleBean role = idmStorage.getRole(roleId);
                MemberRoleBean mrb = new MemberRoleBean();
                mrb.setRoleId(roleId);
                mrb.setRoleName(role.getName());
                member.getRoles().add(mrb);
                if (member.getJoinedOn() == null || membershipBean.getCreatedOn().compareTo(member.getJoinedOn()) < 0) {
                    member.setJoinedOn(membershipBean.getCreatedOn());
                }
            }
            return new ArrayList<>(members.values());
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * Gets a policy by its id.  Also verifies that the policy really does belong to
     * the entity indicated.
     * @param type
     * @param organizationId
     * @param entityId
     * @param entityVersion
     * @param policyId
     * @return a policy bean
     * @throws PolicyNotFoundException
     */
    protected PolicyBean doGetPolicy(PolicyType type, String organizationId, String entityId,
            String entityVersion, long policyId) throws PolicyNotFoundException {
        try {
            storage.beginTx();
            PolicyBean policy = storage.getPolicy(type, organizationId, entityId, entityVersion, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.commitTx();
            if (policy.getType() != type) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (!policy.getOrganizationId().equals(organizationId)) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (!policy.getEntityId().equals(entityId)) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (!policy.getEntityVersion().equals(entityVersion)) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            PolicyTemplateUtil.generatePolicyDescription(policy);
            return policy;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @return a {@link GatewayBean} iff there is a single configured gateway in the system
     * @throws StorageException
     */
    private GatewaySummaryBean getSingularGateway() throws StorageException {
        List<GatewaySummaryBean> gateways = query.listGateways();
        if (gateways != null && gateways.size() == 1) {
            return gateways.get(0);
        } else {
            return null;
        }
    }

    /**
     * @param svb
     */
    private void decryptEndpointProperties(ServiceVersionBean svb) {
        Map<String, String> endpointProperties = svb.getEndpointProperties();
        if (endpointProperties != null) {
            for (Entry<String, String> entry : endpointProperties.entrySet()) {
                entry.setValue(AesEncrypter.decrypt(entry.getValue()));
            }
        }
    }

    /**
     * @param svb
     */
    private void encryptEndpointProperties(ServiceVersionBean svb) {
        Map<String, String> endpointProperties = svb.getEndpointProperties();
        if (endpointProperties != null) {
            for (Entry<String, String> entry : endpointProperties.entrySet()) {
                entry.setValue(AesEncrypter.encrypt(entry.getValue()));
            }
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

    /**
     * @return the idmStorage
     */
    public IIdmStorage getIdmStorage() {
        return idmStorage;
    }

    /**
     * @param idmStorage the idmStorage to set
     */
    public void setIdmStorage(IIdmStorage idmStorage) {
        this.idmStorage = idmStorage;
    }

    /**
     * @return the users
     */
    public IUserResource getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(IUserResource users) {
        this.users = users;
    }

    /**
     * @return the roles
     */
    public IRoleResource getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(IRoleResource roles) {
        this.roles = roles;
    }

    /**
     * @return the securityContext
     */
    public ISecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(ISecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    /**
     * @return the query
     */
    public IStorageQuery getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(IStorageQuery query) {
        this.query = query;
    }

    /**
     * @return the metrics
     */
    public IMetricsAccessor getMetrics() {
        return this.metrics;
    }

    /**
     * @param metrics the metrics to set
     */
    public void setMetrics(IMetricsAccessor metrics) {
        this.metrics = metrics;
    }

    /**
     * @return the applicationValidator
     */
    public IApplicationValidator getApplicationValidator() {
        return applicationValidator;
    }

    /**
     * @param applicationValidator the applicationValidator to set
     */
    public void setApplicationValidator(IApplicationValidator applicationValidator) {
        this.applicationValidator = applicationValidator;
    }

    /**
     * @return the serviceValidator
     */
    public IServiceValidator getServiceValidator() {
        return serviceValidator;
    }

    /**
     * @param serviceValidator the serviceValidator to set
     */
    public void setServiceValidator(IServiceValidator serviceValidator) {
        this.serviceValidator = serviceValidator;
    }

    /**
     * @return the apiKeyGenerator
     */
    public IApiKeyGenerator getApiKeyGenerator() {
        return apiKeyGenerator;
    }

    /**
     * @param apiKeyGenerator the apiKeyGenerator to set
     */
    public void setApiKeyGenerator(IApiKeyGenerator apiKeyGenerator) {
        this.apiKeyGenerator = apiKeyGenerator;
    }

    /**
     * Parse the to date query param.
     * @param fromDate
     */
    private DateTime parseFromDate(String fromDate) {
        // Default to the last 30 days
        DateTime defaultFrom = DateTime.now().withZone(DateTimeZone.UTC).minusDays(30).withHourOfDay(0)
                .withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        return parseDate(fromDate, defaultFrom, true);
    }

    /**
     * Parse the from date query param.
     * @param toDate
     */
    private DateTime parseToDate(String toDate) {
        // Default to now
        return parseDate(toDate, DateTime.now().withZone(DateTimeZone.UTC), false);
    }

    /**
     * Parses a query param representing a date into an actual date object.
     * @param dateStr
     * @param defaultDate
     * @param floor
     */
    private static DateTime parseDate(String dateStr, DateTime defaultDate, boolean floor) {
        if ("now".equals(dateStr)) { //$NON-NLS-1$
            return DateTime.now();
        }
        if (dateStr.length() == 10) {
            DateTime parsed = ISODateTimeFormat.date().withZoneUTC().parseDateTime(dateStr);
            // If what we want is the floor, then just return it.  But if we want the
            // ceiling of the date, then we need to set the right params.
            if (!floor) {
                parsed = parsed.plusDays(1).minusMillis(1);
            }
            return parsed;
        }
        if (dateStr.length() == 20) {
            return ISODateTimeFormat.dateTimeNoMillis().withZoneUTC().parseDateTime(dateStr);
        }
        if (dateStr.length() == 24) {
            return ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(dateStr);
        }
        return defaultDate;
    }

    /**
     * Ensures that the given date range is valid.
     * @param from
     * @param to
     */
    private void validateMetricRange(DateTime from, DateTime to) throws InvalidMetricCriteriaException {
        if (from.isAfter(to)) {
            throw ExceptionFactory.invalidMetricCriteriaException(Messages.i18n.format("OrganizationResourceImpl.InvalidMetricDateRange")); //$NON-NLS-1$
        }
    }

    /**
     * Ensures that a time series can be created for the given date range and
     * interval, and that the
     * @param from
     * @param to
     * @param interval
     */
    private void validateTimeSeriesMetric(DateTime from, DateTime to, HistogramIntervalType interval)
            throws InvalidMetricCriteriaException {
        long millis = to.getMillis() - from.getMillis();
        long divBy = ONE_DAY_MILLIS;
        switch (interval) {
        case day:
            divBy = ONE_DAY_MILLIS;
            break;
        case hour:
            divBy = ONE_HOUR_MILLIS;
            break;
        case minute:
            divBy = ONE_MINUTE_MILLIS;
            break;
        case month:
            divBy = ONE_MONTH_MILLIS;
            break;
        case week:
            divBy = ONE_WEEK_MILLIS;
            break;
        default:
            break;
        }
        long totalDataPoints = millis / divBy;
        if (totalDataPoints > 5000) {
            throw ExceptionFactory.invalidMetricCriteriaException(Messages.i18n.format("OrganizationResourceImpl.MetricDataSetTooLarge")); //$NON-NLS-1$
        }
    }

}
