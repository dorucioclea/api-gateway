package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.digipolis.apim.beans.audit.data.MembershipData;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
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
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.util.PolicyTemplateUtil;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.jpa.JpaStorage;
import com.t1t.digipolis.apim.jpa.roles.JpaIdmStorage;
import com.t1t.digipolis.apim.facades.audit.AuditUtils;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.rest.impl.util.FieldValidator;
import com.t1t.digipolis.apim.rest.resources.IOrganizationResource;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;

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
import java.util.*;
import java.util.Map.Entry;

/**
 * This is the rest endpoint implementation.
 * Responsabilities:
 * <ul>
 *     <li>REST documentation (swagger based)</li>
 *     <li>Security checks (verify role of current user)</li>
 *     <li>Exception handling (with exceptionmapper)</li>
 *     <li>Parameter validation</li>
 *     <li>Facade client</li>
 * </ul>
 *
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

    @Inject
    @APIEngineContext
    Logger log;

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
            notes = "Use this endpoint to create a new Application.  Note that it is important to also create an initial version of the Application (e.g. 1.0).  This can either be done by including the 'initialVersion' property in the request, or by immediately following up with a call to \"Create Application Version\".  If the former is done, then a first Application version will be created automatically by this endpoint.")
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
                                                        @QueryParam("from") String fromDate,
                                                        @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.appView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(applicationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getAppUsagePerService(organizationId,applicationId,version,fromDate,toDate);
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
        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getApp(organizationId, applicationId);

        try {
            return query.getApplicationVersions(organizationId, applicationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
        return orgFacade.createContract(organizationId,applicationId,version,bean);
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

            ContractBean contract = storage.getContract(contractId);
            if (contract == null)
                throw ExceptionFactory.contractNotFoundException(contractId);


            // Hide some data if the user doesn't have the appView permission
            if (!hasPermission) {
                contract.setApikey(null);
            }

            log.debug(String.format("Got contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$
            return contract;
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

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
                                   @PathParam("version") String version)
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


            log.debug(String.format("Deleted contract: %s", contract)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
                                             @PathParam("version") String version) throws ApplicationNotFoundException, NotAuthorizedException {
        return getApiRegistry(organizationId, applicationId, version);
    }

    /**
     * Gets the API registry.
     *
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
        } catch (StorageException | GatewayAuthenticationException e) {
            throw new SystemErrorException(e);
        } finally {
            if (txStarted) {

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
        return createAppPolicy(organizationId, applicationId, version, bean);
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
        return orgFacade.getAppPolicy(organizationId,applicationId,version,policyId);
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

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

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
                                @PathParam("applicationId") String applicationId, @PathParam("version") String version, @PathParam("policyId") long policyId)
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

            PolicyBean policy = this.storage.getPolicy(PolicyType.Application, organizationId, applicationId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.deletePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyRemoved(policy, PolicyType.Application, securityContext));

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        return orgFacade.listAppPolicies(organizationId,applicationId,version);
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

            List<Long> newOrder = new ArrayList<>(policyChain.getPolicies().size());
            for (PolicySummaryBean psb : policyChain.getPolicies()) {
                newOrder.add(psb.getId());
            }
            storage.reorderPolicies(PolicyType.Application, organizationId, applicationId, version, newOrder);
            storage.createAuditEntry(AuditUtils.policiesReordered(avb, PolicyType.Application, securityContext));

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        FieldValidator.validateName(bean.getName());
        return orgFacade.createService(organizationId,bean);
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
        try {

            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }

            return bean;
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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

    @ApiOperation(value = "List Services",
            notes = "Use this endpoint to get a list of all Services in the Organization.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceSummaryBean.class, message = "A list of Services.")
    })
    @GET
    @Path("/{organizationId}/services")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceSummaryBean> listServices(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException,
            NotAuthorizedException {
        // make sure the org exists
        get(organizationId);

        try {
            return query.getServicesInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
        try {

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

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        return createServiceVersion(organizationId, serviceId, bean);
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
        return orgFacade.getServiceVersion(organizationId,serviceId,version);
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
            } else if (serviceVersion.getDefinitionType() == ServiceDefinitionType.WSDL) {
                builder.type("application/wsdl+xml"); //$NON-NLS-1$
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
        try {

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

            log.debug(String.format("Got endpoint summary: %s", gateway)); //$NON-NLS-1$
            return rval;
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        return orgFacade.updateServiceVersion(organizationId,serviceId,version,bean);
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
            orgFacade.updateServiceDefinition(organizationId,serviceId,version, contentType, stream);
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
                                                               @PathParam("serviceId") String serviceId)
            throws ServiceNotFoundException, NotAuthorizedException {
        // Try to get the service first - will throw a ServiceNotFoundException if not found.
        getService(organizationId, serviceId);

        try {
            return query.getServiceVersions(organizationId, serviceId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
        // Ensure the version exists first.
        getServiceVersion(organizationId, serviceId, version);

        try {
            return query.getServiceVersionPlans(organizationId, serviceId, version);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
        return orgFacade.getServicePolicy(organizationId,serviceId,version,policyId);
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

        // Make sure the service exists
        getServiceVersion(organizationId, serviceId, version);

        try {

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

            log.debug(String.format("Updated service policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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

        // Make sure the service exists
        ServiceVersionBean service = getServiceVersion(organizationId, serviceId, version);
        if (service.getStatus() == ServiceStatus.Published || service.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }

        try {

            PolicyBean policy = this.storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.deletePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyRemoved(policy, PolicyType.Service, securityContext));

            log.debug(String.format("Deleted service %s policy: %s", serviceId, policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        try {

            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            serviceVersion.setDefinitionType(ServiceDefinitionType.None);
            storage.createAuditEntry(AuditUtils.serviceDefinitionDeleted(serviceVersion, securityContext));
            storage.deleteServiceDefinition(serviceVersion);
            storage.updateServiceVersion(serviceVersion);

            log.debug(String.format("Deleted service %s definition %s", serviceId, serviceVersion)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        return orgFacade.listServicePolicies(organizationId,serviceId,version);
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

        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);

        try {

            List<Long> newOrder = new ArrayList<>(policyChain.getPolicies().size());
            for (PolicySummaryBean psb : policyChain.getPolicies()) {
                newOrder.add(psb.getId());
            }
            storage.reorderPolicies(PolicyType.Service, organizationId, serviceId, version, newOrder);
            storage.createAuditEntry(AuditUtils.policiesReordered(svb, PolicyType.Service, securityContext));

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
                                                                @QueryParam("count") int pageSize) throws ServiceVersionNotFoundException,
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

    @ApiOperation(value = "Get Service Usage Metrics",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return a full histogram of request count data based on the provided date range and interval.  Valid intervals are:  month, week, day, hour, minute")
    @ApiResponses({
            @ApiResponse(code = 200, response = UsageHistogramBean.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/usage")
    @Produces(MediaType.APPLICATION_JSON)
    public UsageHistogramBean getUsage(
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
        return orgFacade.getUsage(organizationId, serviceId, version, interval, fromDate, toDate);
    }

    @ApiOperation(value = "Get Service Usage Metrics (per App)",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return request count data broken down by application.  It basically answers the question \"who is calling my service?\".")
    @ApiResponses({
            @ApiResponse(code = 200, response = UsagePerAppBean.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/appUsage")
    @Produces(MediaType.APPLICATION_JSON)
    public UsagePerAppBean getUsagePerApp(@PathParam("organizationId") String organizationId,
                                          @PathParam("serviceId") String serviceId,
                                          @PathParam("version") String version,
                                          @QueryParam("from") String fromDate,
                                          @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getUsagePerApp(organizationId,serviceId,version,fromDate,toDate);
    }

    @ApiOperation(value = "Get Service Usage Metrics (per Plan)",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return request count data broken down by plan.  It basically answers the question which service plans are most used?.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UsagePerPlanBean.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/planUsage")
    @Produces(MediaType.APPLICATION_JSON)
    public UsagePerPlanBean getUsagePerPlan(@PathParam("organizationId") String organizationId,
                                            @PathParam("serviceId") String serviceId,
                                            @PathParam("version") String version,
                                            @QueryParam("from") String fromDate,
                                            @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getUsagePerPlan(organizationId,serviceId,version,fromDate,toDate);
    }

    @ApiOperation(value = "Get System Status",
            notes = "This endpoint simply returns the status of the apiman system. This is a useful endpoint to use when testing a client's connection to the apiman API Manager REST services.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusBean.class, message = "System status information")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/responseStats")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseStatsHistogramBean getResponseStats(@PathParam("organizationId") String organizationId,
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
        return orgFacade.getResponseStats(organizationId,serviceId,version,interval,fromDate,toDate);
    }

    @ApiOperation(value = "Get Service Response Statistics (Summary)",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return total response type statistics over the given date range.  Basically this will return three numbers: total request, # failed responses, # error responses.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ResponseStatsSummaryBean.class, message = "System status information")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/summaryResponseStats")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseStatsSummaryBean getResponseStatsSummary(@PathParam("organizationId") String organizationId,
                                                            @PathParam("serviceId") String serviceId,
                                                            @PathParam("version") String version,
                                                            @QueryParam("from") String fromDate,
                                                            @QueryParam("to") String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getResponseStatsSummary(organizationId,serviceId,version,fromDate,toDate);
    }

    @ApiOperation(value = "Get Service Response Statistics (per App)",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return response type statistics broken down by application.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ResponseStatsPerAppBean.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/appResponseStats")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseStatsPerAppBean getResponseStatsPerApp(@PathParam("organizationId") String organizationId,
                                                          @PathParam("serviceId") String serviceId,
                                                          @PathParam("version") String version,
                                                          @QueryParam("from") String fromDate,
                                                          @QueryParam("to") String toDate) throws NotAuthorizedException,
            InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getResponseStatsPerApp(organizationId,serviceId,version,fromDate,toDate);
    }

    @ApiOperation(value = "Get Service Response Statistics (per Plan)",
            notes = "Retrieves metrics/analytics information for a specific service.  This will return response type statistics broken down by plan.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusBean.class, message = "Usage metrics information.")
    })
    @GET
    @Path("/{organizationId}/services/{serviceId}/versions/{version}/metrics/planResponseStats")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseStatsPerPlanBean getResponseStatsPerPlan(@PathParam("organizationId") String organizationId,
                                                            @PathParam("serviceId") String serviceId,
                                                            @PathParam("version") String version,
                                                            @QueryParam("from") String fromDate,
                                                            @QueryParam("to") String toDate) throws NotAuthorizedException,
            InvalidMetricCriteriaException {
        if (!securityContext.hasPermission(PermissionType.svcView, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return orgFacade.getResponseStatsPerPlan(organizationId,serviceId,version,fromDate,toDate);
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

        PlanBean newPlan = new PlanBean();
        newPlan.setName(bean.getName());
        newPlan.setDescription(bean.getDescription());
        newPlan.setId(BeanUtils.idFromName(bean.getName()));
        newPlan.setCreatedOn(new Date());
        newPlan.setCreatedBy(securityContext.getCurrentUser());
        try {
            // Store/persist the new plan

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


            log.debug(String.format("Created plan: %s", newPlan)); //$NON-NLS-1$
            return newPlan;
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        try {

            PlanBean bean = storage.getPlan(organizationId, planId);
            if (bean == null) {
                throw ExceptionFactory.planNotFoundException(planId);
            }

            log.debug(String.format("Got plan: %s", bean)); //$NON-NLS-1$
            return bean;
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
                                                             @QueryParam("count") int pageSize)
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
        get(organizationId);

        try {
            return query.getPlansInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
    public void updatePlan(@PathParam("organizationId") String organizationId, @PathParam("planId") String planId, UpdatePlanBean bean)
            throws PlanNotFoundException, NotAuthorizedException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        EntityUpdatedData auditData = new EntityUpdatedData();
        try {

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

            log.debug(String.format("Updated plan: %s", planForUpdate)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
    public PlanVersionBean createPlanVersion(@PathParam("organizationId") String organizationId,
                                             @PathParam("planId") String planId, NewPlanVersionBean bean)
            throws PlanNotFoundException, NotAuthorizedException, InvalidVersionException,
            PlanVersionAlreadyExistsException {
        if (!securityContext.hasPermission(PermissionType.planEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        FieldValidator.validateVersion(bean.getVersion());

        PlanVersionBean newVersion = null;
        try {

            PlanBean plan = storage.getPlan(organizationId, planId);
            if (plan == null) {
                throw ExceptionFactory.planNotFoundException(planId);
            }

            if (storage.getPlanVersion(organizationId, planId, bean.getVersion()) != null) {
                throw ExceptionFactory.planVersionAlreadyExistsException(planId, bean.getVersion());
            }

            newVersion = createPlanVersionInternal(bean, plan);

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

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
     *
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
        return orgFacade.getPlanVersion(organizationId,planId,version);
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
            @QueryParam("count") int pageSize) throws PlanVersionNotFoundException,
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
        // Try to get the plan first - will throw a PlanNotFoundException if not found.
        getPlan(organizationId, planId);

        try {
            return query.getPlanVersions(organizationId, planId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
        return orgFacade.getPlanPolicy(organizationId,planId,version,policyId);
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

        // Make sure the plan version exists
        getPlanVersion(organizationId, planId, version);

        try {

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

            log.debug(String.format("Updated plan policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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

        // Make sure the plan version exists
        PlanVersionBean plan = getPlanVersion(organizationId, planId, version);
        if (plan.getStatus() == PlanStatus.Locked) {
            throw ExceptionFactory.invalidPlanStatusException();
        }

        try {

            PolicyBean policy = this.storage.getPolicy(PolicyType.Plan, organizationId, planId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            storage.deletePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyRemoved(policy, PolicyType.Plan, securityContext));

            log.debug(String.format("Deleted plan policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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
        // Try to get the plan first - will throw an exception if not found.
        getPlanVersion(organizationId, planId, version);

        try {
            return query.getPolicies(organizationId, planId, version, PolicyType.Plan);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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

        // Make sure the plan version exists
        PlanVersionBean pvb = getPlanVersion(organizationId, planId, version);

        try {

            List<Long> newOrder = new ArrayList<>(policyChain.getPolicies().size());
            for (PolicySummaryBean psb : policyChain.getPolicies()) {
                newOrder.add(psb.getId());
            }
            storage.reorderPolicies(PolicyType.Plan, organizationId, planId, version, newOrder);
            storage.createAuditEntry(AuditUtils.policiesReordered(pvb, PolicyType.Plan, securityContext));

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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

            storage.createAuditEntry(AuditUtils.membershipGranted(organizationId, auditData, securityContext));

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
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

                storage.createAuditEntry(AuditUtils.membershipRevoked(organizationId, auditData, securityContext));

                log.debug(String.format("Revoked User %s Role %s Org %s", userId, roleId, organizationId)); //$NON-NLS-1$
            } catch (AbstractRestException e) {

                throw e;
            } catch (Exception e) {

                throw new SystemErrorException(e);
            }
        }
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

            storage.createAuditEntry(AuditUtils.membershipRevoked(organizationId, auditData, securityContext));

        } catch (AbstractRestException e) {

            throw e;
        } catch (Exception e) {

            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "List Organization Members",
            notes = "Lists all members of the organization.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = MemberBean.class, message = "List of members.")
    })
    @GET
    @Path("/{organizationId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MemberBean> listMembers(@PathParam("organizationId") String organizationId) throws OrganizationNotFoundException,
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
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(JpaStorage storage) {
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
    public void setIdmStorage(JpaIdmStorage idmStorage) {
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
    public void setQuery(JpaStorage query) {
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

}
