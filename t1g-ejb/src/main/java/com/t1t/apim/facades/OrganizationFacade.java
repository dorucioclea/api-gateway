package com.t1t.apim.facades;

import com.google.gson.Gson;
import com.t1t.apim.AppConfigBean;
import com.t1t.apim.T1G;
import com.t1t.apim.beans.BeanUtils;
import com.t1t.apim.beans.announcements.AnnouncementBean;
import com.t1t.apim.beans.announcements.NewAnnouncementBean;
import com.t1t.apim.beans.apps.*;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.audit.AuditEntryType;
import com.t1t.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.apim.beans.audit.data.MembershipData;
import com.t1t.apim.beans.audit.data.OwnershipTransferData;
import com.t1t.apim.beans.authorization.OAuth2Token;
import com.t1t.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.beans.categories.ServiceTagsBean;
import com.t1t.apim.beans.categories.TagBean;
import com.t1t.apim.beans.contracts.ContractBean;
import com.t1t.apim.beans.contracts.NewContractBean;
import com.t1t.apim.beans.contracts.NewContractRequestBean;
import com.t1t.apim.beans.dto.ServiceUpstreamsDtoBean;
import com.t1t.apim.beans.events.EventBean;
import com.t1t.apim.beans.events.EventType;
import com.t1t.apim.beans.events.NewEventBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.idm.*;
import com.t1t.apim.beans.jwt.IJWT;
import com.t1t.apim.beans.jwt.JWT;
import com.t1t.apim.beans.jwt.ServiceAccountTokenRequest;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.beans.members.MemberBean;
import com.t1t.apim.beans.members.MemberRoleBean;
import com.t1t.apim.beans.metrics.AppUsagePerServiceBean;
import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.orgs.NewOrganizationBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.apim.beans.pagination.OAuth2TokenPaginationBean;
import com.t1t.apim.beans.plans.*;
import com.t1t.apim.beans.policies.*;
import com.t1t.apim.beans.search.PagingBean;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchCriteriaFilterOperator;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.services.*;
import com.t1t.apim.beans.summary.*;
import com.t1t.apim.beans.support.*;
import com.t1t.apim.beans.visibility.VisibilityBean;
import com.t1t.apim.core.*;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.core.metrics.MetricsService;
import com.t1t.apim.exceptions.*;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.util.AuditUtils;
import com.t1t.apim.exceptions.GatewayAuthenticationException;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.gateway.IGatewayLinkFactory;
import com.t1t.apim.gateway.dto.*;
import com.t1t.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.apim.gateway.rest.GatewayValidation;
import com.t1t.apim.rest.KongConstants;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.kong.model.*;
import com.t1t.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.jose4j.jwt.JwtClaims;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.DefinitionException;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.t1t.util.ServiceConventionUtil.generateServiceUniqueName;

/**
 * Created by michallispashidis on 15/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrganizationFacade {
    public final static String MARKET_SEPARATOR = "-";
    public static final String PLACEHOLDER_CALLBACK_URI = "http://localhost/";
    private static final Logger log = LoggerFactory.getLogger(OrganizationFacade.class.getName());
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private ISecurityAppContext appContext;
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private IApiKeyGenerator apiKeyGenerator;
    @Inject
    private IApplicationValidator applicationValidator;
    @Inject
    private IServiceValidator serviceValidator;
    @Inject
    private MetricsService metrics;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    private UserFacade userFacade;
    @Inject
    private RoleFacade roleFacade;
    @Inject
    private BrandingFacade brandingFacade;
    @Inject
    @T1G
    private AppConfigBean config;
    @Inject
    private Event<NewEventBean> event;
    @Inject
    private Event<AnnouncementBean> announcement;
    @Inject
    private GatewayValidation gatewayValidation;

    //create organization
    public OrganizationBean create(NewOrganizationBean bean) throws StorageException {
        List<RoleBean> autoGrantedRoles = null;
        SearchCriteriaBean criteria = new SearchCriteriaBean();
        criteria.setPage(1);
        criteria.setPageSize(100);
        criteria.addFilter("autoGrant", "true", SearchCriteriaFilterOperator.bool_eq);
        try {
            autoGrantedRoles = idmStorage.findRoles(criteria).getBeans();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }

        if ("true".equals(System.getProperty("apim.manager.require-auto-granted-org", "true")) && autoGrantedRoles.isEmpty()) {
            throw new SystemErrorException(Messages.i18n.format("OrganizationResourceImpl.NoAutoGrantRoleAvailable"));
        }
        //determine org id
        String orgUniqueId = bean.getName();
        orgUniqueId = orgUniqueId.replaceAll(MARKET_SEPARATOR, "");
        orgUniqueId = BeanUtils.idFromName(orgUniqueId);

        //verify if organization is created in marketplace (aka not publisher or consent type)
        ManagedApplicationBean managedApp = query.findManagedApplication(appContext.getApplicationPrefix());
        if (managedApp != null){
            if (managedApp.getType().equals(ManagedApplicationTypes.InternalMarketplace) || managedApp.getType().equals(ManagedApplicationTypes.ExternalMarketplace)) {
                //the request comes from a marketplace => prefix the org
                orgUniqueId = managedApp.getPrefix() + MARKET_SEPARATOR + orgUniqueId;
            } else if (managedApp.getType().equals(ManagedApplicationTypes.Admin) && StringUtils.isNotEmpty(bean.getOptionalTargetContext())){
                //admin managed application type can provide a target context
                orgUniqueId = bean.getOptionalTargetContext().toLowerCase().trim() + MARKET_SEPARATOR + orgUniqueId;
            }
        }
        OrganizationBean orgBean = new OrganizationBean();
        orgBean.setName(bean.getName());
        orgBean.setContext(managedApp!=null && (managedApp.getType().equals(ManagedApplicationTypes.Admin) && StringUtils.isNotEmpty(bean.getOptionalTargetContext()))?bean.getOptionalTargetContext():appContext.getApplicationPrefix());
        orgBean.setDescription(bean.getDescription());
        orgBean.setId(orgUniqueId);
        orgBean.setCreatedOn(new Date());
        orgBean.setCreatedBy(securityContext.getCurrentUser());
        orgBean.setModifiedOn(new Date());
        orgBean.setModifiedBy(securityContext.getCurrentUser());
        orgBean.setOrganizationPrivate(bean.getOrganizationPrivate() == null ? true : bean.getOrganizationPrivate());
        if (StringUtils.isNotEmpty(bean.getFriendlyName())) {orgBean.setFriendlyName(bean.getFriendlyName());}

        try {
            // Store/persist the new organization
            if (storage.getOrganization(orgBean.getId()) != null || storage.getBranding(orgUniqueId) != null) {
                throw ExceptionFactory.organizationAlreadyExistsException(bean.getName());
            }
            storage.createOrganization(orgBean);
            storage.createAuditEntry(AuditUtils.organizationCreated(orgBean, securityContext));

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
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public OrganizationBean get(String organizationId) {
        try {
            OrganizationBean organizationBean = storage.getOrganization(organizationId);
            if (organizationBean == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            log.debug(String.format("Got organization %s: %s", organizationBean.getName(), organizationBean));
            return organizationBean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void update(String organizationId, UpdateOrganizationBean bean) {
        if (!securityContext.hasPermission(PermissionType.orgEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            OrganizationBean orgForUpdate = storage.getOrganization(organizationId);
            if (orgForUpdate == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            EntityUpdatedData auditData = new EntityUpdatedData();
            if (AuditUtils.valueChanged(orgForUpdate.getDescription(), bean.getDescription())) {
                auditData.addChange("description", orgForUpdate.getDescription(), bean.getDescription());
                orgForUpdate.setDescription(bean.getDescription());
            }
            if (AuditUtils.valueChanged(orgForUpdate.getFriendlyName(), bean.getFriendlyName())) {
                if (!userFacade.get(securityContext.getCurrentUser()).getAdmin()) {
                    throw ExceptionFactory.notAuthorizedException();
                }
                auditData.addChange("friendlyName", orgForUpdate.getFriendlyName(), bean.getFriendlyName());
                orgForUpdate.setFriendlyName(bean.getFriendlyName());
            }
            if (AuditUtils.valueChanged(orgForUpdate.isOrganizationPrivate(), bean.isOrganizationPrivate())) {
                auditData.addChange("private", Boolean.toString(orgForUpdate.isOrganizationPrivate()), Boolean.toString(bean.isOrganizationPrivate()));
                orgForUpdate.setOrganizationPrivate(bean.isOrganizationPrivate());
            }
            storage.updateOrganization(orgForUpdate);
            storage.createAuditEntry(AuditUtils.organizationUpdated(orgForUpdate, auditData, securityContext));

            log.debug(String.format("Updated organization %s: %s", orgForUpdate.getName(), orgForUpdate));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<AuditEntryBean> activity(String organizationId, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, null, null, null, getPagingBean(page, pageSize));
            return rval;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ApplicationBean createApp(String organizationId, NewApplicationBean bean) {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId)) throw ExceptionFactory.notAuthorizedException();
        //verify if organization is created in marketplace (aka not publisher or consent type)
        ManagedApplicationBean managedApp = null;
        try {
            managedApp = query.findManagedApplication(appContext.getApplicationPrefix());
        } catch (StorageException e) {
            throw ExceptionFactory.invalidApplicationStatusException();
        }

        ApplicationBean newApp = new ApplicationBean();
        newApp.setId(BeanUtils.idFromName(bean.getName()));
        newApp.setName(bean.getName());
        newApp.setBase64logo(bean.getBase64logo());
        newApp.setDescription(bean.getDescription());
        newApp.setCreatedBy(securityContext.getCurrentUser());
        newApp.setCreatedOn(new Date());
        newApp.setContext(managedApp!=null && (managedApp.getType().equals(ManagedApplicationTypes.Admin) && StringUtils.isNotEmpty(bean.getOptionalTargetContext()))?bean.getOptionalTargetContext():appContext.getApplicationIdentifier().getPrefix());
        newApp.setEmail(bean.getEmail());
        try {
            // Store/persist the new application
            OrganizationBean org = storage.getOrganization(organizationId);
            if (org == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            newApp.setOrganization(org);
            if (storage.getApplication(org.getId(), newApp.getId()) != null) {
                throw ExceptionFactory.applicationAlreadyExistsException(bean.getName());
            }
            storage.createApplication(newApp);
            storage.createAuditEntry(AuditUtils.applicationCreated(newApp, securityContext));
            if (bean.getInitialVersion() != null) {
                NewApplicationVersionBean newAppVersion = new NewApplicationVersionBean();
                newAppVersion.setVersion(bean.getInitialVersion());
                createAppVersionInternal(newAppVersion, newApp);
            }
            log.debug(String.format("Created application %s: %s", newApp.getName(), newApp)); //$NON-NLS-1$
            return newApp;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ApplicationVersionBean createAppVersion(String organizationId, String applicationId, NewApplicationVersionBean bean) {
        ApplicationVersionBean newVersion;
        try {
            ApplicationBean application = storage.getApplication(organizationId, applicationId);
            if (application == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }
            if (storage.getApplicationVersion(organizationId, applicationId, bean.getVersion()) != null) {
                throw ExceptionFactory.applicationVersionAlreadyExistsException(applicationId, bean.getVersion());
            }
            newVersion = createAppVersionInternal(bean, application);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        if (bean.isClone() && bean.getCloneVersion() != null) {
            try {
                List<ContractSummaryBean> contracts = getApplicationVersionContracts(organizationId, applicationId, bean.getCloneVersion());
                for (ContractSummaryBean contract : contracts) {
                    ServiceVersionBean svb = storage.getServiceVersion(contract.getServiceOrganizationId(), contract.getServiceId(), contract.getServiceVersion());
                    if (svb.getAutoAcceptContracts()) {
                        NewContractBean ncb = new NewContractBean();
                        ncb.setPlanId(contract.getPlanId());
                        ncb.setServiceId(contract.getServiceId());
                        ncb.setServiceOrgId(contract.getServiceOrganizationId());
                        ncb.setServiceVersion(contract.getServiceVersion());
                        ncb.setTermsAgreed(contract.getTermsAgreed());
                        createContract(organizationId, applicationId, newVersion.getVersion(), ncb);
                    } else {
                        NewContractRequestBean request = new NewContractRequestBean();
                        request.setTermsAgreed(true);
                        request.setApplicationOrg(organizationId);
                        request.setApplicationId(applicationId);
                        request.setApplicationVersion(newVersion.getVersion());
                        request.setPlanId(contract.getPlanId());
                        requestContract(contract.getServiceOrganizationId(), contract.getServiceId(), contract.getServiceVersion(), request);
                    }
                }
                List<PolicySummaryBean> policies = listAppPolicies(organizationId, applicationId, bean.getCloneVersion());
                for (PolicySummaryBean policySummary : policies) {
                    PolicyBean policy = getAppPolicy(organizationId, applicationId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration(), ConsumerConventionUtil.createAppUniqueId(newVersion)), PolicyType.Application).getPolicyJsonConfig());
                    createAppPolicy(organizationId, applicationId, newVersion.getVersion(), npb);
                }
            } catch (Exception e) {
                // TODO it's ok if the clone fails - we did our best
            }
        }
        return newVersion;
    }

    public ApplicationVersionBean updateAppVersionURI(String organizationId, String applicationId, String version, UpdateApplicationVersionURIBean uri) {
        try {
            log.debug("Enter updateAppversionURI:{}", uri);
            ApplicationVersionBean avb = storage.getApplicationVersion(organizationId, applicationId, version);
            if (avb == null) throw ExceptionFactory.applicationNotFoundException(applicationId);
            if(uri != null && uri.getUris() !=null && uri.getUris().size()>0)
            for (String redirectURI : uri.getUris()) {
                if (!ValidationUtils.isValidAbsoluteURI(redirectURI))
                    throw ExceptionFactory.invalidArgumentException("invalidAbsoluteURI", redirectURI);
            }
            Set<String> previousURIs = avb.getOauthClientRedirects();
            avb.setOauthClientRedirects(uri.getUris());
            //register application credentials for OAuth2
            //create OAuth2 application credentials on the application consumer - should only been done once for this application
            if (avb != null && !StringUtils.isEmpty(avb.getOauthClientSecret())) {
                String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
                //String uniqueUserId = securityContext.getCurrentUser();
                KongPluginOAuthConsumerRequest OAuthRequest = new KongPluginOAuthConsumerRequest()
                        .withClientId(appConsumerName)
                        .withClientSecret(avb.getOauthClientSecret())
                        .withName(avb.getApplication().getName())
                        .withRedirectUri(avb.getOauthClientRedirects())
                        .withId(avb.getOauthCredentialId());
                if (avb.getStatus() == ApplicationStatus.Registered) {
                    List<ContractSummaryBean> csb = query.getApplicationContracts(organizationId, applicationId, version);
                    Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(csb);
                    for (IGatewayLink gateway : gateways.values()) {
                        gateway.updateConsumerOAuthCredentials(appConsumerName, OAuthRequest);
                    }
                } else {
                    if (avb.getStatus() != ApplicationStatus.Retired) {
                        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                        gateway.updateConsumerOAuthCredentials(appConsumerName, OAuthRequest);
                    } else {
                        throw ExceptionFactory.invalidApplicationStatusException();
                    }
                }
            }
            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("OAuth2 Callback URI", String.valueOf(previousURIs), String.valueOf(avb.getOauthClientRedirects()));
            if (avb.getStatus() != ApplicationStatus.Registered && applicationValidator.isReady(avb)) {
                avb.setStatus(ApplicationStatus.Ready);
            }
            storage.updateApplicationVersion(avb);
            storage.createAuditEntry(AuditUtils.applicationVersionUpdated(avb, data, securityContext));
            return avb;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public EnrichedPolicySummaryBean createAndEnrichAppPolicy(String organizationId, String applicationId, String version, NewPolicyBean bean) {
        return PolicyUtil.createEnrichedPolicySummary(createAppPolicy(organizationId, applicationId, version, bean));
    }

    public PolicyBean createAppPolicy(String organizationId, String applicationId, String version, NewPolicyBean bean) {
        // Make sure the app version exists and is in the right state.
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        if (avb.getStatus() == ApplicationStatus.Registered || avb.getStatus() == ApplicationStatus.Retired) {
            throw ExceptionFactory.invalidApplicationStatusException();
        }
        return doCreatePolicy(organizationId, applicationId, version, bean, PolicyType.Application);
    }

    public PolicyBean createManagedApplicationPolicy(ManagedApplicationBean managedApp, NewPolicyBean bean) {
        PolicyType type = null;
        switch (managedApp.getType()) {
            case InternalMarketplace:
            case ExternalMarketplace:
                type = PolicyType.Marketplace;
                break;
            case Consent:
                type = PolicyType.Consent;
                break;
            default:
                throw ExceptionFactory.invalidPolicyException("Invalid policy type");
        }
        return doCreatePolicy(managedApp.getPrefix(), managedApp.getName(), managedApp.getVersion(), bean, type);
    }

    public EnrichedPolicySummaryBean getEnrichedAppPolicy(String organizationId, String applicationId, String version, long policyId) {
        return PolicyUtil.createEnrichedPolicySummary(getAppPolicy(organizationId, applicationId, version, policyId));
    }

    public PolicyBean getAppPolicy(String organizationId, String applicationId, String version, long policyId) {
        boolean hasPermission = securityContext.hasPermission(PermissionType.appView, organizationId);
        // Make sure the app version exists
        getAppVersion(organizationId, applicationId, version);
        PolicyBean policy = doGetPolicy(PolicyType.Application, organizationId, applicationId, version, policyId);
        if (!hasPermission) {
            policy.setConfiguration(null);
        }
        return policy;
    }

    public List<PolicySummaryBean> listAppPolicies(String organizationId, String applicationId, String version) {
        // Try to get the application first - will throw an exception if not found.
        getAppVersion(organizationId, applicationId, version);
        try {
            return query.getPolicies(organizationId, applicationId, version, PolicyType.Application);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public ContractBean requestContract(String organizationId, String serviceId, String version, NewContractRequestBean bean) {
        try {
            //verify if serviceversion and appversion exist
            ApplicationVersionBean avb = getAppVersion(bean.getApplicationOrg(), bean.getApplicationId(), bean.getApplicationVersion());
            ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
            String appId = ConsumerConventionUtil.createAppUniqueId(avb);
            String svcId = generateServiceUniqueName(svb);
            //Check if there already is a contract between the service and application
            List<ContractSummaryBean> csbs = query.getApplicationContracts(bean.getApplicationOrg(), bean.getApplicationId(), bean.getApplicationVersion());
            for (ContractSummaryBean sum : csbs) {
                if (sum.getServiceOrganizationId().equals(organizationId) && sum.getServiceId().equals(serviceId) && sum.getServiceVersion().equals(version)) {
                    throw ExceptionFactory.contractAlreadyExistsException();
                }
            }
            if (svb.getTermsAgreementRequired() != null && svb.getTermsAgreementRequired() && (bean.getTermsAgreed() == null || !bean.getTermsAgreed())) {
                throw ExceptionFactory.termsAgreementException("Agreement to terms & conditions required for contract creation");
            }
            //Check if service allows auto contract creation or is an admin service
            if (!svb.getAutoAcceptContracts() || (svb.getService().isAdmin() != null && svb.getService().isAdmin())) {

                //Check if there is a pending contract request
                if (query.getEventByOriginDestinationAndType(appId, svcId, EventType.CONTRACT_PENDING) != null) {
                    throw ExceptionFactory.contractRequestFailedException("Pending request already exists");
                }
                String servicePlanVersion = null;
                for (ServicePlanBean spb : svb.getPlans()) {
                    if (spb.getPlanId().equalsIgnoreCase(bean.getPlanId())) {
                        servicePlanVersion = spb.getVersion();
                        break;
                    }
                }
                if (servicePlanVersion == null) {
                    throw ExceptionFactory.planNotFoundException(bean.getPlanId());
                }
                PlanVersionBean pvb = getPlanVersion(organizationId, bean.getPlanId(), servicePlanVersion);
                PlanVersionSummaryBean pvsb = new PlanVersionSummaryBean();
                pvsb.setId(pvb.getPlan().getId());
                pvsb.setName(pvb.getPlan().getName());
                pvsb.setVersion(pvb.getVersion());

                fireEvent(appId, svcId, EventType.CONTRACT_PENDING, new Gson().toJson(pvsb));
                return null;
            } else {
                //Service accepts all incoming contract requests so create and return new contract
                NewContractBean ncb = new NewContractBean();
                ncb.setServiceOrgId(organizationId);
                ncb.setServiceId(serviceId);
                ncb.setServiceVersion(version);
                ncb.setPlanId(bean.getPlanId());
                ncb.setTermsAgreed(bean.getTermsAgreed());
                return createContract(bean.getApplicationOrg(), bean.getApplicationId(), bean.getApplicationVersion(), ncb);
            }
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public void rejectContractRequest(String organizationId, String applicationId, String version, NewContractBean bean) {
        //Validate service and app version, and verify if request actually occurred
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        ServiceVersionBean svb = getServiceVersionInternal(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion());

        fireEvent(generateServiceUniqueName(svb), ConsumerConventionUtil.createAppUniqueId(avb), EventType.CONTRACT_REJECTED, null);
    }

    public ContractBean acceptContractRequest(String organizationId, String applicationId, String version, NewContractBean bean) {
        //Validate service and app version, and verify if request actually occurred
        getAppVersion(organizationId, applicationId, version);
        ServiceVersionBean svb = getServiceVersionInternal(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion());
        if (svb.getService().isAdmin() && !securityContext.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        if (svb.getTermsAgreementRequired() != null && svb.getTermsAgreementRequired()) {
            bean.setTermsAgreed(true);
        }
        ContractBean contract = createContract(organizationId, applicationId, version, bean);

        fireEvent(generateServiceUniqueName(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion()),
                ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version),
                EventType.CONTRACT_ACCEPTED,
                null);

        return contract;
    }

    public ContractBean createContract(String organizationId, String applicationId, String version, NewContractBean bean) {
        try {
            //add OAuth2 consumer default to the application
            ContractBean contract = createContractInternal(organizationId, applicationId, version, bean);
            //If the service is an admin service, the application version must be added as a custom managed app
            if (contract.getService().getService().isAdmin()) {
                Set<ManagedApplicationBean> mabs = query.getManagedApplicationsByType(ManagedApplicationTypes.Admin);
                if (mabs != null && !mabs.isEmpty()) {
                    for (ManagedApplicationBean mab : mabs) {
                        mab.getApiKeys().add(contract.getApplication().getApikey());
                        storage.updateManagedApplication(mab);
                    }
                }
            }
            log.debug(String.format("Created new contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$

            Application app = getApplicationForNewContractRegistration(contract);
            gatewayFacade.getApplicationVersionGatewayLinks(contract.getApplication()).forEach(gw -> {
                try {
                    enableContractonGateway(contract, gw);
                    //persist the new contract policies
                    Map<Contract, KongPluginConfigList> response = gw.registerApplication(app);
                    for (Map.Entry<Contract, KongPluginConfigList> entry : response.entrySet()) {
                        for (KongPluginConfig config : entry.getValue().getData()) {
                            NewPolicyBean npb = new NewPolicyBean();
                            npb.setGatewayId(gw.getGatewayId());
                            npb.setConfiguration(new Gson().toJson(config.getConfig()));
                            npb.setContractId(contract.getId());
                            npb.setKongPluginId(config.getId());
                            npb.setDefinitionId(GatewayUtils.convertKongPluginNameToPolicy(config.getName()).getPolicyDefId());
                            doCreatePolicy(organizationId, applicationId, version, npb, PolicyType.Contract);
                        }
                    }
                } catch (StorageException | GatewayAuthenticationException ex) {
                    throw ExceptionFactory.systemErrorException(ex);
                }
            });


            //TODO - Remove the OAuth enabling code
            //verify if the contracting service has OAuth enabled

            /*List<PolicySummaryBean> policySummaryBeans = listServicePolicies(serviceOrgId, serviceId, svcVersion);
            for (PolicySummaryBean summaryBean : policySummaryBeans) {
                if (summaryBean.getPolicyDefinitionId().toLowerCase().equals(Policies.OAUTH2.getKongIdentifier())) {
                    ApplicationVersionBean avb = contract.getApplication();
                    boolean changed = false;
                    if (StringUtils.isEmpty(avb.getoAuthClientId())) {
                        //create client_id and client_secret for the application - the same client_id/secret must be used for all services
                        //upon publication the application credentials will be enabled for the current user.
                        avb.setoAuthClientId(apiKeyGenerator.generate());
                        avb.setOauthClientSecret(apiKeyGenerator.generate());
                        changed = true;
                    }
                    //Check if client credentials is enabled, and if so, already enable oauth on gateway
                    PolicyBean pb = storage.getPolicy(PolicyType.Service, serviceOrgId, serviceId, svcVersion, summaryBean.getId());
                    KongPluginOAuth oAuthValue = new Gson().fromJson(pb.getConfiguration(), KongPluginOAuth.class);
                    if (oAuthValue.getEnableClientCredentials() && (avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().isEmpty() || avb.getOauthClientRedirects().stream().filter(redirect -> !StringUtils.isEmpty(redirect)).collect(Collectors.toSet()).isEmpty())) {
                        avb.setOauthClientRedirects(new HashSet<>(Arrays.asList(PLACEHOLDER_CALLBACK_URI)));
                        String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
                        OAuthConsumerRequestBean requestBean = new OAuthConsumerRequestBean();
                        requestBean.setUniqueUserName(appConsumerName);
                        requestBean.setAppOAuthId(avb.getoAuthClientId());
                        requestBean.setAppOAuthSecret(avb.getOauthClientSecret());
                        enableOAuthForConsumer(requestBean);
                        changed = true;
                    }
                    if (changed) {
                        storage.updateApplicationVersion(avb);
                    }
                }
            }*/
            return contract;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            // Up above we are optimistically creating the contract.  If it fails, check to see
            // if it failed because it was a duplicate.  If so, throw something sensible.  We
            // only do this on failure (we would get a FK contraint failure, for example) to
            // reduce overhead on the typical happy path.
            // we asume it already exists
            //
            // However, we should probably log the exception for debugging purposes
            log.debug("Contract creation error:{}", e);
            throw ExceptionFactory.contractAlreadyExistsException();
/*            if (contractAlreadyExists(organizationId, applicationId, version, bean)) {
        throw ExceptionFactory.contractAlreadyExistsException();
    } else {
        throw new SystemErrorException(e);
    }*/
        }
    }

    private void enableContractonGateway(ContractBean contract, IGatewayLink gateway) throws StorageException {
        String applicationOrgId = contract.getApplication().getApplication().getOrganization().getId();
        String applicationId = contract.getApplication().getApplication().getId();
        String appVersion = contract.getApplication().getVersion();
        if (contract != null) {
            String appConsumerName = ConsumerConventionUtil.createAppUniqueId(contract.getApplication());
            //Add ACL group membership by default on gateway
            KongPluginACLResponse response = gateway.addConsumerToACL(appConsumerName,
                    generateServiceUniqueName(contract.getService()));
            //Persist the unique Kong plugin id in a new policy associated with the app.
            NewPolicyBean npb = new NewPolicyBean();
            KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
            npb.setDefinitionId(Policies.ACL.name());
            npb.setKongPluginId(response.getId());
            npb.setContractId(contract.getId());
            npb.setConfiguration(new Gson().toJson(conf));
            npb.setGatewayId(gateway.getGatewayId());
            doCreatePolicy(applicationOrgId, applicationId, appVersion, npb, PolicyType.Contract);
        }
    }

    private Application getApplicationForNewContractRegistration(ContractBean cb) throws StorageException {
        Application app = new Application(cb.getApplication().getApplication().getOrganization().getId(), cb.getApplication().getApplication().getId(), cb.getApplication().getVersion());
        Contract contract = new Contract(cb);
        List<Policy> contractPolicies = new ArrayList<>();
        for (PolicyBean policy : getContractedServicePlanPolicies(cb)) {
            String appId = ConsumerConventionUtil.createAppUniqueId(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion());
            contractPolicies.add(new Policy(policy.getDefinition().getId(), policy.getConfiguration(), appId));
        }
        contract.setPolicies(contractPolicies);
        app.setContracts(new HashSet<>());
        app.getContracts().add(contract);
        return app;
    }

    private List<PolicyBean> getContractedServicePlanPolicies(ContractBean c) throws StorageException {
        List<PolicyBean> rval = new ArrayList<>();
        String planOrg = c.getPlan().getPlan().getOrganization().getId();
        String planId = c.getPlan().getPlan().getId();
        String planVersion = c.getPlan().getVersion();
        List<PolicySummaryBean> sums = query.getPolicies(planOrg, planId, planVersion, PolicyType.Plan);
        for (PolicySummaryBean sum : sums) {
            rval.add(getPlanPolicy(planOrg, planId, planVersion, sum.getId()));
        }
        return rval;
    }

    public KongPluginOAuthConsumerResponse enableOAuthForConsumer(OAuthConsumerRequestBean request) throws StorageException {
        //get the application version based on provided client_id and client_secret - we need the name and
        log.info("Start enabling consumer for oauth2");
        KongPluginOAuthConsumerRequest oauthRequest = new KongPluginOAuthConsumerRequest()
                .withClientId(request.getAppOAuthId())
                .withClientSecret(request.getAppOAuthSecret());
        KongPluginOAuthConsumerResponse response = null;
        //retrieve application name and redirect URI.
        try {
            ApplicationVersionBean avb = query.getApplicationForOAuth(request.getAppOAuthId(), request.getAppOAuthSecret());
            if (avb == null)
                throw new ApplicationNotFoundException("Application not found with given OAuth2 clientId and clientSecret.");
            String appUniqueName = ConsumerConventionUtil.createAppUniqueId(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion());
            oauthRequest.setName(avb.getApplication().getName());
            if (avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().isEmpty() || avb.getOauthClientRedirects().stream().filter(redirect -> !StringUtils.isEmpty(redirect)).collect(Collectors.toSet()).isEmpty())
                throw new OAuthException("The application must provide an OAuth2 redirect URL");
            oauthRequest.setRedirectUri(avb.getOauthClientRedirects());
            String defaultGateway = query.listGateways().get(0).getId();
            if (!StringUtils.isEmpty(defaultGateway)) {
                try {
                    IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                    log.info("Enable consumer for oauth:{} with values: {}", appUniqueName, oauthRequest);
                    response = gatewayLink.enableConsumerForOAuth(appUniqueName, oauthRequest);
                    avb.setOauthCredentialId(response.getId());
                    storage.updateApplicationVersion(avb);
                } catch (Exception e) {
                    log.debug("Error enabling application for oauth:{}", e.getStackTrace());
                    //don't do anything
                }
                if (response == null) {
                    log.debug("Enable consumer for oauth - response empty");
                    //try to recover existing application
                    try {
                        IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                        KongPluginOAuthConsumerResponseList credentials = gatewayLink.getConsumerOAuthCredentials(appUniqueName);
                        for (KongPluginOAuthConsumerResponse cred : credentials.getData()) {
                            if (cred.getClientId().equals(request.getAppOAuthId())) response = cred;
                        }
                    } catch (Exception e) {
                        //now throw an error if that's not working too.
                        throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e);
                    }
                }
            } else throw new GatewayException("No default gateway found!");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Creates a gateway link given a gateway id.
     *
     * @param gatewayId
     */
    private IGatewayLink createGatewayLink(String gatewayId) throws PublishingException {
        try {
            GatewayBean gateway = storage.getGateway(gatewayId);
            if (gateway == null) {
                throw new GatewayNotFoundException();
            }
            IGatewayLink link = gatewayLinkFactory.create(gateway);
            return link;
        } catch (GatewayNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PublishingException(e.getMessage(), e);
        }
    }

    public List<ContractSummaryBean> getApplicationVersionContracts(String organizationId, String applicationId, String version) {
        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getAppVersion(organizationId, applicationId, version);
        try {
            List<ContractSummaryBean> contracts = query.getApplicationContracts(organizationId, applicationId, version);
            // Hide some stuff if the user doesn't have the appView permission
            return contracts;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ApplicationBean getApp(String organizationId, String applicationId) {
        try {
            ApplicationBean applicationBean = storage.getApplication(organizationId, applicationId);
            if (applicationBean == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }
            log.debug(String.format("Got application %s: %s", applicationBean.getName(), applicationBean)); //$NON-NLS-1$
            return applicationBean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteApp(String organizationId, String applicationId) {
        if (!securityContext.hasPermission(PermissionType.appAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            // Get Application
            ApplicationBean applicationBean = storage.getApplication(organizationId, applicationId);
            if (applicationBean == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }
            // Get Application versions
            List<ApplicationVersionSummaryBean> versions = query.getApplicationVersions(applicationBean.getOrganization().getId(), applicationBean.getId());
            for (ApplicationVersionSummaryBean appVersion : versions) {
                deleteAppVersionInternal(appVersion.getOrganizationId(), appVersion.getId(), appVersion.getVersion());
            }
            // Finally, delete the application from API Engine
            storage.deleteApplication(applicationBean);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteAppVersion(String organizationId, String applicationId, String version) {
        try {
            List<ApplicationVersionSummaryBean> versions = query.getApplicationVersions(organizationId, applicationId);
            //If there's only one version left, delete the entire application...
            if (versions.size() == 1) {
                deleteApp(organizationId, applicationId);
            } else {
                deleteAppVersionInternal(organizationId, applicationId, version);
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deleteAppVersionInternal(String organizationId, String applicationId, String version) {
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        try {
            List<ContractSummaryBean> summaries = query.getApplicationContracts(organizationId, applicationId, version);

            //Delete policies before deleting contracts
            for (ContractSummaryBean sum : summaries) {
                List<PolicyBean> policies = query.getApplicationVersionContractPolicies(organizationId, applicationId, version, sum.getContractId());
                for (PolicyBean policy : policies) {
                    storage.deletePolicy(policy);
                }
            }

            //Delete the contracts
            deleteContractsForSummaries(summaries);

            Application application = new Application(organizationId, applicationId, version);
            if (avb.getStatus() == ApplicationStatus.Registered) {
                Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(summaries);
                for (IGatewayLink gateway : gateways.values()) {
                    try {
                        gateway.unregisterApplication(application);
                        gateway.close();
                    } catch (GatewayAuthenticationException ex) {
                        throw ExceptionFactory.systemErrorException(ex);
                    }
                }
            } else {
                if (avb.getStatus() != ApplicationStatus.Retired) {
                    IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                    try {
                        gateway.unregisterApplication(application);
                        gateway.close();
                    } catch (GatewayAuthenticationException ex) {
                        throw ExceptionFactory.systemErrorException(ex);
                    }
                }
            }
            // Delete related application events
            query.deleteAllEventsForEntity(ConsumerConventionUtil.createAppUniqueId(avb));
            // Finally delete the application
            storage.deleteApplicationVersion(avb);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public SearchResultsBean<AuditEntryBean> getAppActivity(String organizationId, String applicationId, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, applicationId, null, ApplicationBean.class, getPagingBean(page, pageSize));
            return rval;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ApplicationSummaryBean> listApps(String organizationId) {
        get(organizationId);
        try {
            return query.getApplicationsInOrg(organizationId, true);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updateApp(String organizationId, String applicationId, UpdateApplicationBean bean) {
        try {
            ApplicationBean appForUpdate = storage.getApplication(organizationId, applicationId);
            if (appForUpdate == null) {
                throw ExceptionFactory.applicationNotFoundException(applicationId);
            }
            EntityUpdatedData auditData = new EntityUpdatedData();
            if (AuditUtils.valueChanged(appForUpdate.getDescription(), bean.getDescription())) {
                auditData.addChange("description", appForUpdate.getDescription(), bean.getDescription()); //$NON-NLS-1$
                appForUpdate.setDescription(bean.getDescription());
            }
            if (AuditUtils.valueChanged(appForUpdate.getBase64logo(), bean.getBase64logo())) {
                auditData.addChange("logo", appForUpdate.getBase64logo(), bean.getBase64logo());
                appForUpdate.setBase64logo(bean.getBase64logo());
            }
            if (AuditUtils.valueChanged(appForUpdate.getEmail(), bean.getEmail())) {
                auditData.addChange("email", appForUpdate.getEmail(), bean.getEmail());
                appForUpdate.setEmail(bean.getEmail());
            }
            storage.updateApplication(appForUpdate);
            storage.createAuditEntry(AuditUtils.applicationUpdated(appForUpdate, auditData, securityContext));

            log.debug(String.format("Updated application %s: %s", appForUpdate.getName(), appForUpdate)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ApplicationVersionBean getAppVersion(String organizationId, String applicationId, String version) {
        try {
            ApplicationVersionBean applicationVersion = storage.getApplicationVersion(organizationId, applicationId, version);
            if (applicationVersion == null) {
                throw ExceptionFactory.applicationVersionNotFoundException(applicationId, version);
            }
            log.debug(String.format("Got new application version %s: %s", applicationVersion.getApplication().getName(), applicationVersion)); //$NON-NLS-1$
            return applicationVersion;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public EnrichedPolicySummaryBean getEnrichedPlanPolicy(String organizationId, String planId, String version, long policyId) {
        return PolicyUtil.createEnrichedPolicySummary(getPlanPolicy(organizationId, planId, version, policyId));
    }

    public PolicyBean getPlanPolicy(String organizationId, String planId, String version, long policyId) {

        // Make sure the plan version exists
        getPlanVersion(organizationId, planId, version);

        PolicyBean policy = doGetPolicy(PolicyType.Plan, organizationId, planId, version, policyId);

        log.debug(String.format("Got plan policy %s", policy)); //$NON-NLS-1$
        return policy;
    }

    public PlanVersionBean getPlanVersion(String organizationId, String planId, String version) {
        try {
            PlanVersionBean planVersion = storage.getPlanVersion(organizationId, planId, version);
            if (planVersion == null) {
                throw ExceptionFactory.planVersionNotFoundException(planId, version);
            }
            log.debug(String.format("Got plan %s version: %s", planId, planVersion)); //$NON-NLS-1$
            return planVersion;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public EnrichedPolicySummaryBean createAndEnrichPlanPolicy(String organizationId, String planId, String version, NewPolicyBean bean) {
        return PolicyUtil.createEnrichedPolicySummary(createPlanPolicy(organizationId, planId, version, bean));
    }

    public PolicyBean createPlanPolicy(String organizationId, String planId, String version, NewPolicyBean bean) {
        // Make sure the plan version exists and is in the right state
        PlanVersionBean pvb = getPlanVersion(organizationId, planId, version);
        if (pvb.getStatus() == PlanStatus.Locked) {
            throw ExceptionFactory.invalidPlanStatusException();
        }
        //validate no other policy of the same type has been added for this plan versions - only one policy of the same type is allowed
        List<PolicySummaryBean> policies = listPlanPolicies(organizationId, planId, version);
        for (PolicySummaryBean polsum : policies) {
            if (polsum.getPolicyDefinitionId().equals(bean.getDefinitionId()))
                throw new PolicyDefinitionAlreadyExistsException("The policy already exists for the service: " + bean.getDefinitionId());
        }
        log.debug(String.format("Creating plan %s policy %s", planId, pvb)); //$NON-NLS-1$
        return doCreatePolicy(organizationId, planId, version, bean, PolicyType.Plan);
    }

    public EnrichedPolicySummaryBean createAndEnrichedServicePolicy(String organizationId, String serviceId, String version, NewPolicyBean bean) {
        return PolicyUtil.createEnrichedPolicySummary(createServicePolicy(organizationId, serviceId, version, bean));
    }

    public PolicyBean createServicePolicy(String organizationId, String serviceId, String version, NewPolicyBean bean) {
        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
        if (svb.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        //validate no other policy of the same type has been added for this service - only on policy of the same type is allowed
        List<PolicySummaryBean> policies = listServicePolicies(organizationId, serviceId, version);
        for (PolicySummaryBean polsum : policies) {
            if (polsum.getPolicyDefinitionId().equals(bean.getDefinitionId()))
                throw new PolicyDefinitionAlreadyExistsException("The policy already exists for the service: " + bean.getDefinitionId());
        }
        log.debug(String.format("Created service policy %s", svb)); //$NON-NLS-1$
        PolicyBean policy = doCreatePolicy(organizationId, serviceId, version, bean, PolicyType.Service);
        //Apply the new policy on the gateway if the service is published
        if (svb.getStatus() == ServiceStatus.Deprecated || svb.getStatus() == ServiceStatus.Published) {
            IGatewayLink gw = gatewayFacade.createGatewayLink(policy.getGatewayId());
            Policy newPolicy = new Policy();
            newPolicy.setPolicyJsonConfig(policy.getConfiguration());
            newPolicy.setPolicyImpl(policy.getDefinition().getId());
            newPolicy.setPolicyId(policy.getId());
            newPolicy = gw.createServicePolicy(organizationId, serviceId, version, newPolicy);
            policy.setKongPluginId(newPolicy.getKongPluginId());
            policy.setGatewayId(gw.getGatewayId());
            //policy.setConfiguration(newPolicy.getPolicyJsonConfig());
            try {
                storage.updatePolicy(policy);
            } catch (StorageException ex) {
                throw ExceptionFactory.systemErrorException(ex);
            }
        }
        return policy;
    }

    public ServiceVersionBean getServiceVersionInternal(String organizationId, String serviceId, String version) {
        try {
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);

            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            decryptEndpointProperties(serviceVersion);
            return serviceVersion;
        } catch (AbstractRestException e) {
            throw e;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public ServiceVersionBean getServiceVersion(String orgId, String svcId, String version) {
        return filterServiceVersionByAppPrefix(getServiceVersionInternal(orgId, svcId, version));
    }

    public PolicyBean getServicePolicyInternal(String organizationId, String serviceId, String version, long policyId) {
        // Make sure the service exists
        getServiceVersionInternal(organizationId, serviceId, version);
        return doGetPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
    }

    public EnrichedPolicySummaryBean getServicePolicy(String organizationId, String serviceId, String version, long policyId) {
        try {
            return scrubPolicy(getServicePolicyInternal(organizationId, serviceId, version, policyId));
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private EnrichedPolicySummaryBean scrubPolicy(PolicyBean policy) throws StorageException {
        //TODO - scrub the sensitive information out of policy configurations
        boolean doFilter = !query.getManagedAppPrefixesForTypes(Arrays.asList(ManagedApplicationTypes.Consent, ManagedApplicationTypes.Publisher, ManagedApplicationTypes.Admin)).contains(appContext.getApplicationPrefix());
        EnrichedPolicySummaryBean rval = PolicyUtil.createEnrichedPolicySummary(policy, doFilter);
        return rval;
    }

    public ServiceUpstreamsDtoBean getServiceUpstreamTargets(String organizationId, String serviceId, String version) {
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        return DtoFactory.createServiceUpstreamDtoBean(svb);
    }

    public ServiceUpstreamsDtoBean addServiceUpstreamTarget(String organizationId, String serviceId, String version, ServiceUpstreamRequest request) {
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        if (svb.getStatus().equals(ServiceStatus.Retired)) throw ExceptionFactory.invalidServiceStatusException();

        ServiceUpstreamTargetBean target = new ServiceUpstreamTargetBean();
        target.setPort(request.getPort());
        target.setWeight(request.getWeight());
        target.setTarget(request.getTarget());
        if (!svb.getUpstreamTargets().contains(target)) {
            svb.getUpstreamTargets().add(target);
            if (svb.getStatus().equals(ServiceStatus.Deprecated) || svb.getStatus().equals(ServiceStatus.Published)) {
                String gwId = ServiceConventionUtil.generateServiceUniqueName(svb);
                List<IGatewayLink> serviceGatewayLinks = svb.getGateways().stream().map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId())).collect(Collectors.toList());
                // If the service is published/deprecated on the gateway, it should already have at least one upstream
                for (IGatewayLink gw : serviceGatewayLinks) {
                    try {
                        if (svb.getUpstreamTargets().size() == 2) {
                            //This means that there was previously only one upstream and that no virtual host was set
                            gw.createServiceUpstream(organizationId, serviceId, version);
                            gw.updateServiceVersionOnGateway(svb);
                        }
                        gw.createOrUpdateServiceUpstreamTarget(gwId, target);
                    } catch (Exception ex) {

                    }
                }
            } else {
                try {
                    if (serviceValidator.isReady(svb)) {
                        svb.setStatus(ServiceStatus.Ready);
                    }
                } catch (Exception ex) {
                    log.error("Error validating service readiness: {}", ex);
                }
            }
            try {
                storage.updateServiceVersion(svb);
            } catch (StorageException e) {
                throw ExceptionFactory.systemErrorException(e);
            }
        }
        return DtoFactory.createServiceUpstreamDtoBean(svb);
    }

    public void removeServiceUpstreamTarget(String organizationId, String serviceId, String version, ServiceUpstreamRequest request) {
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        if (svb.getStatus().equals(ServiceStatus.Retired)) throw ExceptionFactory.invalidServiceStatusException();

        ServiceUpstreamTargetBean targetToBeRemoved = new ServiceUpstreamTargetBean();
        targetToBeRemoved.setPort(request.getPort());
        targetToBeRemoved.setTarget(request.getTarget());
        if (svb.getUpstreamTargets().contains(targetToBeRemoved)) {
            if (svb.getStatus().equals(ServiceStatus.Deprecated) || svb.getStatus().equals(ServiceStatus.Published)) {

                String gwId = ServiceConventionUtil.generateServiceUniqueName(svb);
                List<IGatewayLink> serviceGatewayLinks = svb.getGateways().stream().map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId())).collect(Collectors.toList());

                switch (svb.getUpstreamTargets().size()) {
                    case 1:
                        // There must be at least one upstream target defined for the service
                        throw ExceptionFactory.invalidServiceStatusException();
                    case 2:
                        svb.getUpstreamTargets().remove(targetToBeRemoved);
                        serviceGatewayLinks.forEach(gw -> {
                            gw.updateServiceVersionOnGateway(svb);
                            gw.deleteServiceUpstream(gwId);
                        });
                        break;
                    default:
                        svb.getUpstreamTargets().remove(targetToBeRemoved);
                        serviceGatewayLinks.forEach(gw -> {
                            targetToBeRemoved.setWeight(0L);
                            gw.createOrUpdateServiceUpstreamTarget(gwId, targetToBeRemoved);
                        });
                        break;
                }
            } else {
                svb.getUpstreamTargets().remove(targetToBeRemoved);
                try {
                    if (serviceValidator.isReady(svb)) {
                        svb.setStatus(ServiceStatus.Ready);
                    }
                } catch (Exception ex) {
                    log.error("Error validating service readiness: {}", ex);
                }
            }
            try {
                storage.updateServiceVersion(svb);
            } catch (StorageException e) {
                throw ExceptionFactory.systemErrorException(e);
            }
        }
    }

    /*public void updateServiceVersionLoadBalancing(String organizationId, String serviceId, String version, ServiceLoadBalancingConfigurationBean bean) {
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        if (svb.getStatus() == ServiceStatus.Retired) throw ExceptionFactory.invalidServiceStatusException();
        EntityUpdatedData data = new EntityUpdatedData();
        boolean targetsChanged = false;
        boolean gwUpdateNecessary = false;
        if (AuditUtils.valueChanged(svb.getCustomLoadBalancing(), bean.getCustomLoadBalancing())) {
            //if enabled, check if at least one upstream target is specified
            if (bean.getCustomLoadBalancing() && (svb.getUpstreamTargets() == null || svb.getUpstreamTargets().isEmpty()) && (bean.getUpstreamTargets() == null || bean.getUpstreamTargets().isEmpty())) {
                throw ExceptionFactory.invalidLoadBalancingConfigurationException(Messages.i18n.format("lbNoTargets"));
            }
            if (svb.getCustomLoadBalancing() && !bean.getCustomLoadBalancing() && StringUtils.isEmpty(bean.getEndpoint())) {
                throw ExceptionFactory.invalidLoadBalancingConfigurationException(Messages.i18n.format("lbNewEndpointConfig"));
            }
            data.addChange("customLoadBalancing", String.valueOf(svb.getCustomLoadBalancing()), String.valueOf(bean.getCustomLoadBalancing()));
        }
        if (bean.getUpstreamTargets() != null && !bean.getUpstreamTargets().isEmpty() && AuditUtils.valueChanged(svb.getUpstreamTargets(), bean.getUpstreamTargets())) {
            for (ServiceUpstreamTargetBean target : bean.getUpstreamTargets()) {
                if (StringUtils.isEmpty(target.getTarget())) throw ExceptionFactory.invalidLoadBalancingConfigurationException("emptyTarget");
                if (target.getWeight() != null && (target.getWeight() < 0 || target.getWeight() > 1000)) throw ExceptionFactory.invalidLoadBalancingConfigurationException("invalidWeight");
            }
            data.addChange("loadBalancingTargets", svb.getUpstreamTargets().toString(), bean.getUpstreamTargets().toString());
            targetsChanged = true;
        }
        if (svb.getCustomLoadBalancing() && !bean.getCustomLoadBalancing()) {
            svb.setCustomLoadBalancing(bean.getCustomLoadBalancing());
            svb.setEndpoint(bean.getEndpoint());
            svb.setUpstreamTargets(Collections.emptySet());
        }
        else if (!svb.getCustomLoadBalancing() && bean.getCustomLoadBalancing()) {
            //Replace the service endpoint host with the virtual DNS
            String virtualEndpoint = URIUtils.setVirtualHost(svb.getEndpoint(), ServiceConventionUtil.generateServiceUniqueName(svb));
            if (virtualEndpoint != null) {
                svb.setEndpoint(virtualEndpoint);
                svb.setCustomLoadBalancing(bean.getCustomLoadBalancing());
                svb.setUpstreamTargets(bean.getUpstreamTargets());
            }
            else {
                throw ExceptionFactory.invalidLoadBalancingConfigurationException(Messages.i18n.format("invalidVirtualHost"));
            }
        }
        if (svb.getStatus() == ServiceStatus.Deprecated || svb.getStatus() == ServiceStatus.Published) {
            List<IGatewayLink> gwLinks = svb.getGateways().stream().map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId())).collect(Collectors.toList());
            if (svb.getCustomLoadBalancing() && !bean.getCustomLoadBalancing()) {
                gwLinks.forEach(gw -> gw.deleteServiceUpstream(ServiceConventionUtil.generateServiceUniqueName(svb)));
                gwUpdateNecessary = true;
            }
            else if (!svb.getCustomLoadBalancing() && bean.getCustomLoadBalancing()) {
                //Replace the service endpoint host with the virtual DNS
                String virtualEndpoint = URIUtils.setVirtualHost(svb.getEndpoint(), ServiceConventionUtil.generateServiceUniqueName(svb));
                if (virtualEndpoint != null) {
                    gwLinks.forEach(gw -> gw.createServiceUpstream(organizationId, serviceId, version, bean.getUpstreamTargets()));
                    gwUpdateNecessary = true;
                }
                else {
                    throw ExceptionFactory.invalidLoadBalancingConfigurationException(Messages.i18n.format("invalidVirtualHost"));
                }
            }
            //Check the difference between the existing set of targets and the new one
            if (svb.getCustomLoadBalancing() && targetsChanged) {
                Set<ServiceUpstreamTargetBean> processedTargets = new HashSet<>(bean.getUpstreamTargets());
                Set<ServiceUpstreamTargetBean> current = svb.getUpstreamTargets();
                Set<String> targets = processedTargets.stream().map(ServiceUpstreamTargetBean::getTarget).collect(Collectors.toSet());
                for (ServiceUpstreamTargetBean oldTarget : current) {
                    if (!targets.contains(oldTarget.getTarget())) {
                        //Targets are not deleted from the gateway, to "delete" one, the weight must be set to 0
                        ServiceUpstreamTargetBean targetToRemove = new ServiceUpstreamTargetBean();
                        targetToRemove.setTarget(oldTarget.getTarget());
                        targetToRemove.setWeight(0L);
                        processedTargets.add(targetToRemove);
                    }
                }
                svb.setUpstreamTargets(bean.getUpstreamTargets());
                gwLinks.forEach(gw -> {
                    gw.createOrUpdateServiceUpstreamTarget(ServiceConventionUtil.generateServiceUniqueName(svb), processedTargets);
                });

            }
            if (gwUpdateNecessary) gwLinks.forEach(gw -> gw.updateServiceVersionOnGateway(svb));
        }
        if (svb.getCustomLoadBalancing())
        try {
            storage.updateServiceVersion(svb);
            AuditEntryBean entry = AuditUtils.serviceVersionUpdated(svb, data, securityContext);
            if (entry != null) {
                storage.createAuditEntry(entry);
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }*/

    public ServiceVersionBean updateServiceVersion(String organizationId, String serviceId, String version, UpdateServiceVersionBean bean) throws StorageException {
        ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
        EntityUpdatedData data = new EntityUpdatedData();
        boolean gwValueChanged = false;
        if (svb.getStatus() != ServiceStatus.Retired) {
            /*if (AuditUtils.valueChanged(svb.getEndpoint(), bean.getEndpoint())) {
                data.addChange("endpoint", svb.getEndpoint(), bean.getEndpoint()); //$NON-NLS-1$
                svb.setEndpoint(URIUtils.uriBackslashRemover(bean.getEndpoint()));
                //If the service is already published, update the upstream URL's on the gateways the service is published on
                gwValueChanged = true;
                log.debug("BEAN ENDPOINT UPDATED");
            }*/
            if (AuditUtils.valueChanged(svb.getHostnames(), bean.getHostnames())) {
                data.addChange("hostnames", svb.getHostnames() == null ? "" : svb.getHostnames().toString(), bean.getHostnames() == null ? "" : bean.getHostnames().toString());
                svb.setHostnames(bean.getHostnames());
                gwValueChanged = true;
            }
            if (AuditUtils.valueChanged(svb.getUpstreamConnectTimeout(), bean.getUpstreamConnectTimeout())) {
                data.addChange("upstreamConnectTimeout", svb.getUpstreamConnectTimeout() == null ? null : svb.getUpstreamConnectTimeout().toString(), bean.getUpstreamConnectTimeout() == null ? null : bean.getUpstreamConnectTimeout().toString());
                svb.setUpstreamConnectTimeout(bean.getUpstreamConnectTimeout());
                gwValueChanged = true;
            }
            if (AuditUtils.valueChanged(svb.getUpstreamReadTimeout(), bean.getUpstreamReadTimeout())) {
                data.addChange("upstreamReadTimeout", svb.getUpstreamReadTimeout() == null ? null : svb.getUpstreamReadTimeout().toString(), bean.getUpstreamReadTimeout() == null ? null : bean.getUpstreamReadTimeout().toString());
                svb.setUpstreamReadTimeout(bean.getUpstreamReadTimeout());
                gwValueChanged = true;
            }
            if (AuditUtils.valueChanged(svb.getUpstreamSendTimeout(), bean.getUpstreamSendTimeout())) {
                data.addChange("upstreamSendTimeout", svb.getUpstreamSendTimeout() == null ? null : svb.getUpstreamSendTimeout().toString(), bean.getUpstreamSendTimeout() == null ? null : bean.getUpstreamSendTimeout().toString());
                svb.setUpstreamSendTimeout(bean.getUpstreamSendTimeout());
                gwValueChanged = true;
            }
            if (AuditUtils.valueChanged(svb.getUpstreamScheme(), bean.getUpstreamScheme())) {
                data.addChange("upstreamScheme", svb.getUpstreamScheme() == null ? null : svb.getUpstreamScheme().toString(), bean.getUpstreamScheme() == null ? null : bean.getUpstreamScheme().toString());
                svb.setUpstreamScheme(bean.getUpstreamScheme());
                gwValueChanged = true;
            }
            if (AuditUtils.valueChanged(svb.getUpstreamPath(), bean.getUpstreamPath())) {
                data.addChange("upstreamPath", svb.getUpstreamPath() == null ? null : svb.getUpstreamPath().toString(), bean.getUpstreamPath() == null ? null : bean.getUpstreamPath().toString());
                svb.setUpstreamPath(URIUtils.uriLeadingSlashPrepender(bean.getUpstreamPath()));
                gwValueChanged = true;
            }
            if (AuditUtils.valueChanged(svb.getEndpointType(), bean.getEndpointType())) {
                data.addChange("endpointType", svb.getEndpointType(), bean.getEndpointType()); //$NON-NLS-1$
                svb.setEndpointType(bean.getEndpointType());
                log.debug("BEAN ENDPOINT TYPE UPDATED");
            }
            if (AuditUtils.valueChanged(svb.getReadme(), bean.getReadme())) {
                data.addChange("readme", svb.getReadme(), bean.getReadme());
                svb.setReadme(bean.getReadme());
            }
            if (AuditUtils.valueChanged(svb.getVisibility(), bean.getVisibility())) {
                if (bean.getVisibility() == null || bean.getVisibility().isEmpty()) {
                    throw ExceptionFactory.serviceVersionUpdateException(Messages.i18n.format("ServiceVersionHasNoAvailability"));
                }
                //Check if the new visibility doesn't affect existing contracts
                List<ContractBean> contracts = query.getServiceContracts(organizationId, serviceId, version);
                if (!contracts.isEmpty()) {
                    Set<String> visibilities = new HashSet<>();
                    bean.getVisibility().forEach(vis -> visibilities.add(vis.getCode()));
                    for (ContractBean contract : contracts) {
                        if (!visibilities.contains(contract.getApplication().getApplication().getOrganization().getContext())) {
                            throw ExceptionFactory.serviceVersionUpdateException(String.format(Messages.i18n.format("ServiceVersionStillHasContractsInScope", serviceId, version)));
                        }
                    }
                }
                data.addChange("visibility", String.valueOf(svb.getVisibility()), String.valueOf(bean.getVisibility())); //$NON-NLS-1$
                svb.setVisibility(bean.getVisibility());
                log.debug("BEAN VISIBILITY UPDATED");
            }
            //Set auto accept to false no matter what when the service is an admin service
            if (svb.getService().isAdmin()) {
                bean.setAutoAcceptContracts(false);
            }
            if (AuditUtils.valueChanged(svb.getAutoAcceptContracts(), bean.getAutoAcceptContracts())) {
                data.addChange("autoAcceptContracts", svb.getAutoAcceptContracts().toString(), bean.getAutoAcceptContracts().toString());
                svb.setAutoAcceptContracts(bean.getAutoAcceptContracts());
                log.debug("BEAN AUTOACCEPT CONTRACTS UPDATED");
            }
            if (AuditUtils.valueChanged(svb.getPlans(), bean.getPlans())) {
                isServiceVersionPublishedOrDeprecated(svb);
                data.addChange("plans", AuditUtils.asString_ServicePlanBeans(svb.getPlans()), AuditUtils.asString_ServicePlanBeans(bean.getPlans())); //$NON-NLS-1$
                if (svb.getPlans() == null) {
                    svb.setPlans(new HashSet<ServicePlanBean>());
                }
                svb.getPlans().clear();
                if (bean.getPlans() != null) {
                    svb.getPlans().addAll(bean.getPlans());
                }
                log.debug("BEAN PLANS UPDATED");
            }
            if (AuditUtils.valueChanged(svb.getGateways(), bean.getGateways())) {
                isServiceVersionPublishedOrDeprecated(svb);
                data.addChange("gateways", AuditUtils.asString_ServiceGatewayBeans(svb.getGateways()), AuditUtils.asString_ServiceGatewayBeans(bean.getGateways())); //$NON-NLS-1$
                if (svb.getGateways() == null) {
                    svb.setGateways(new HashSet<ServiceGatewayBean>());
                }
                svb.getGateways().clear();
                svb.getGateways().addAll(bean.getGateways());
                log.debug("BEAN GATEWAYS UPDATED");
            }
            if (AuditUtils.valueChanged(svb.getOnlinedoc(), bean.getOnlinedoc())) {
                data.addChange("online doc", svb.getOnlinedoc(), bean.getOnlinedoc());
                svb.setOnlinedoc(bean.getOnlinedoc());
                log.debug("BEAN ONLINE DOCS UPDATED");
            }
            if (AuditUtils.valueChanged(svb.getEndpointProperties(), bean.getEndpointProperties())) {
                isServiceVersionPublishedOrDeprecated(svb);
                if (svb.getEndpointProperties() == null) {
                    svb.setEndpointProperties(new HashMap<String, String>());
                } else {
                    svb.getEndpointProperties().clear();
                }
                if (bean.getEndpointProperties() != null) {
                    svb.getEndpointProperties().putAll(bean.getEndpointProperties());
                }
                log.debug("BEAN ENDPOINT PROPERTIES UPDATED");
            }
            if (AuditUtils.valueChanged(svb.isPublicService(), bean.getPublicService())) {
                isServiceVersionPublishedOrDeprecated(svb);
                data.addChange("publicService", String.valueOf(svb.isPublicService()), String.valueOf(bean.getPublicService())); //$NON-NLS-1$
                svb.setPublicService(bean.getPublicService());
                log.debug("BEAN PUBLICITY UPDATED");
            }
            if (AuditUtils.valueChanged(svb.getTermsAgreementRequired(), bean.getTermsAgreementRequired())) {
                data.addChange("termsAgreementRequired", String.valueOf(svb.getTermsAgreementRequired()), String.valueOf(bean.getTermsAgreementRequired()));
                svb.setTermsAgreementRequired(bean.getTermsAgreementRequired());
            }
            if (svb.getStatus() != ServiceStatus.Published) {
                try {
                    if (svb.getGateways() == null || svb.getGateways().isEmpty()) {
                        GatewaySummaryBean gateway = getSingularGateway();
                        if (gateway != null && svb.getGateways() == null) {
                            svb.setGateways(new HashSet<>());
                            ServiceGatewayBean sgb = new ServiceGatewayBean();
                            sgb.setGatewayId(gateway.getId());
                            svb.getGateways().add(sgb);
                        }
                        log.debug("BEAN GATEWAYS FILLED IN IF EMPTY");
                    }
                    if (serviceValidator.isReady(svb)) {
                        log.debug("validService:{}", true);
                        svb.setStatus(ServiceStatus.Ready);
                    } else {
                        log.debug("validService:{}", false);
                        svb.setStatus(ServiceStatus.Created);
                    }
                } catch (Exception e) {
                    throw new SystemErrorException(e);
                }
            }
            try {
                encryptEndpointProperties(svb);
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
                svb.setModifiedBy(securityContext.getCurrentUser());
                svb.setModifiedOn(new Date());
                storage.updateServiceVersion(svb);
                AuditEntryBean entry = AuditUtils.serviceVersionUpdated(svb, data, securityContext);
                if (entry != null) {
                    storage.createAuditEntry(entry);
                }
                if ((svb.getStatus() == ServiceStatus.Deprecated || svb.getStatus() == ServiceStatus.Published) && gwValueChanged) {
                    updateServiceVersionOnGateway(svb);
                }
                log.debug(String.format("Successfully updated Service Version: %s", svb)); //$NON-NLS-1$
                decryptEndpointProperties(svb);
                return svb;
            } catch (AbstractRestException e) {
                throw e;
            } catch (Exception e) {
                throw new SystemErrorException(e);
            }
        } else {
            throw ExceptionFactory.invalidServiceStatusException();
        }
    }

    public ServiceBean createService(String organizationId, NewServiceBean bean) {
        ServiceBean newService = new ServiceBean();
        newService.setName(bean.getName());
        newService.setDescription(bean.getDescription());
        newService.setId(BeanUtils.idFromName(bean.getName()));
        newService.setBasepaths(bean.getBasepaths().stream().map(URIUtils::uriLeadingSlashPrepender).collect(Collectors.toSet()));
        newService.setCategories(bean.getCategories());
        newService.setBase64logo(bean.getBase64logo());
        newService.setCreatedOn(new Date());
        if (bean.isAdmin() != null && bean.isAdmin() && !securityContext.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        newService.setAdmin(bean.isAdmin() == null ? false : bean.isAdmin());
        newService.setCreatedBy(securityContext.getCurrentUser());
        try {
            GatewaySummaryBean gateway = getSingularGateway();
            OrganizationBean orgBean = storage.getOrganization(organizationId);
            if (orgBean == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            if (storage.getService(orgBean.getId(), newService.getId()) != null) {
                throw ExceptionFactory.serviceAlreadyExistsException(bean.getName());
            }
            validateServiceBasepaths(orgBean, newService.getBasepaths());
            newService.setBrandings(validateServiceBrandings(newService, bean.getBrandings()));
            newService.setOrganization(orgBean);
            // Store/persist the new service
            storage.createService(newService);
            storage.createAuditEntry(AuditUtils.serviceCreated(newService, securityContext));

            if (bean.getInitialVersion() != null) {
                NewServiceVersionBean newServiceVersion = new NewServiceVersionBean();
                newServiceVersion.setVersion(bean.getInitialVersion());
                createDefaultServicePolicies(createServiceVersionInternal(newServiceVersion, newService, gateway), false);
            }
            return newService;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    private void validateServiceBasepaths(OrganizationBean org, Set<String> basepaths) throws StorageException {
        for (String basepath : basepaths) {
            if (query.getServiceByBasepath(org.getId(), basepath) != null) {
                throw ExceptionFactory.serviceBasepathAlreadyInUseException(org.getName(), basepath.substring(1));
            }
        }
    }

    private ServiceBrandingBean validateServiceBranding(ServiceBean service, String branding) {
        ServiceBrandingBean rval = null;
        Set<ServiceBrandingBean> validatedBranding = validateServiceBrandings(service, new HashSet<>(Collections.singleton(branding)));
        if (!validatedBranding.isEmpty()) {
            rval = validatedBranding.iterator().next();
        }
        return rval;
    }

    private Set<ServiceBrandingBean> validateServiceBrandings(ServiceBean service, Set<String> brandings) {
        Set<ServiceBrandingBean> rval = new HashSet<>();
        log.info("brandings:{}", brandings);
        if (brandings != null && !brandings.isEmpty()) {
            for (String branding : brandings) {
                ServiceBrandingBean sbb = brandingFacade.getServiceBranding(branding);
                log.info("branding:{}", sbb);
                if (sbb.getServices() != null) {
                    for (ServiceBean sb : sbb.getServices()) {
                        if (sb.getId().equals(service.getId()) && !sb.equals(service)) {
                            throw ExceptionFactory.brandingNotAvailableException("ServiceBrandingNotAvailable", service.getId(), sbb.getId());
                        }
                    }
                }
                rval.add(sbb);
            }
        }
        return rval;
    }

    private void isServiceVersionPublishedOrDeprecated(ServiceVersionBean svb) {
        if (svb.getStatus() == ServiceStatus.Deprecated || svb.getStatus() == ServiceStatus.Published) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
    }

    public ServiceVersionBean createServiceVersion(String organizationId, String serviceId, NewServiceVersionBean bean) {
        log.info("newservice:{}", bean);
        ServiceVersionBean newVersion = null;
        try {
            //adds the default gateway - service can be updated to add another gateway
            GatewaySummaryBean gateway = getSingularGateway();
            ServiceBean service = storage.getService(organizationId, serviceId);
            if (service == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }

            if (storage.getServiceVersion(organizationId, serviceId, bean.getVersion()) != null) {
                throw ExceptionFactory.serviceVersionAlreadyExistsException(serviceId, bean.getVersion());
            }
            newVersion = createServiceVersionInternal(bean, service, gateway);
            log.debug("new serviceversion before cloning:{}", newVersion);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        if (bean.isClone() && bean.getCloneVersion() != null) {
            try {
                ServiceVersionBean cloneSource = getServiceVersionInternal(organizationId, serviceId, bean.getCloneVersion());

                // Clone primary attributes of the service version
                UpdateServiceVersionBean updatedService = new UpdateServiceVersionBean();
                updatedService.setReadme(cloneSource.getReadme());
                updatedService.setTermsAgreementRequired(cloneSource.getTermsAgreementRequired());
                updatedService.setUpstreamScheme(cloneSource.getUpstreamScheme());
                updatedService.setUpstreamPath(cloneSource.getUpstreamPath());
                updatedService.setEndpointType(cloneSource.getEndpointType());
                updatedService.setEndpointProperties(cloneSource.getEndpointProperties());
                updatedService.setHostnames(cloneSource.getHostnames());
                updatedService.setUpstreamConnectTimeout(cloneSource.getUpstreamConnectTimeout());
                updatedService.setUpstreamReadTimeout(cloneSource.getUpstreamReadTimeout());
                updatedService.setUpstreamSendTimeout(cloneSource.getUpstreamSendTimeout());

                updatedService.setOnlinedoc(cloneSource.getOnlinedoc());
                updatedService.setPublicService(cloneSource.isPublicService());
                updatedService.setAutoAcceptContracts(cloneSource.getAutoAcceptContracts());

                //create new sets in order to avoid persistence errors
                updatedService.setGateways(new HashSet<>(cloneSource.getGateways()));
                updatedService.setPlans(new HashSet<>(cloneSource.getPlans()));
                updatedService.setVisibility(new HashSet<>(cloneSource.getVisibility()));

                newVersion = updateServiceVersion(organizationId, serviceId, bean.getVersion(), updatedService);
                log.debug("new serviceversion post cloning:{}", newVersion);

                // Clone the service definition document
                try {
                    InputStream definition = getServiceDefinition(organizationId, serviceId, bean.getCloneVersion());
                    if (definition != null)
                        storeServiceDefinition(organizationId, serviceId, newVersion.getVersion(), cloneSource.getDefinitionType(), definition);
                } catch (Exception sdnfe) {
                    log.error("Unable to create response", sdnfe); //$NON-NLS-1$
                }
                // Clone all service policies
                List<PolicySummaryBean> policies = listServicePolicies(organizationId, serviceId, bean.getCloneVersion());
                for (PolicySummaryBean policySummary : policies) {
                    PolicyBean policy = getServicePolicyInternal(organizationId, serviceId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration(), generateServiceUniqueName(newVersion)), PolicyType.Service, generateServiceUniqueName(organizationId, serviceId, bean.getCloneVersion())).getPolicyJsonConfig());
                    createServicePolicy(organizationId, serviceId, newVersion.getVersion(), npb);
                }
            } catch (Exception e) {
                //it's ok if the clone fails - we did our best
                if (e != null) {
                    Throwable t = e;
                    e = (Exception) t;
                }
            }
        } else {
            createDefaultServicePolicies(newVersion, false);
        }
        return newVersion;
    }

    public InputStream getServiceDefinition(String organizationId, String serviceId, String version) {
        try {
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            if (serviceVersion.getDefinitionType() == ServiceDefinitionType.None || serviceVersion.getDefinitionType() == null) {
                return null;
                //throw ExceptionFactory.serviceDefinitionNotFoundException(serviceId, version);
            }
            InputStream definition = storage.getServiceDefinition(serviceVersion);
            if (definition == null) {
                return null;
                //throw ExceptionFactory.serviceDefinitionNotFoundException(serviceId, version);
            }
            return definition;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void updateServiceDefinition(String organizationId, String serviceId, String version, String contentType, InputStream data) {
        ServiceDefinitionType newDefinitionType = null;
        if (contentType.toLowerCase().contains("application/json")) { //$NON-NLS-1$
            newDefinitionType = ServiceDefinitionType.SwaggerJSON;
        } else if (contentType.toLowerCase().contains("application/x-yaml")) { //$NON-NLS-1$
            newDefinitionType = ServiceDefinitionType.SwaggerYAML;
/*        } else if (contentType.toLowerCase().contains("application/wsdl+xml")) { //$NON-NLS-1$
            newDefinitionType = ServiceDefinitionType.WSDL;*/
        } else {
            throw new SystemErrorException(Messages.i18n.format("InvalidServiceDefinitionContentType", contentType)); //$NON-NLS-1$
        }
        storeServiceDefinition(organizationId, serviceId, version, newDefinitionType, data);
        log.debug(String.format("Updated service definition for %s", serviceId)); //$NON-NLS-1$
    }

    private void updateServiceVersionOnGateway(ServiceVersionBean svb) {
        if (svb.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        if (svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Deprecated) {
            svb.getGateways().forEach(svcGateway -> {
                IGatewayLink gateway = createGatewayLink(svcGateway.getGatewayId());
                gateway.updateServiceVersionOnGateway(svb);
            });
        }
    }

    public List<PolicySummaryBean> listServicePolicies(String organizationId, String serviceId, String version) {
        // Try to get the service first - will throw an exception if not found.
        getServiceVersionInternal(organizationId, serviceId, version);
        try {
            return query.getPolicies(organizationId, serviceId, version, PolicyType.Service);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<AuditEntryBean> getAppVersionActivity(String organizationId, String applicationId, String version, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, applicationId, version, ApplicationBean.class, getPagingBean(page, pageSize));
            return rval;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public AppUsagePerServiceBean getAppUsagePerService(String organizationId, String applicationId, String version, String fromDate, String toDate) {
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        AppUsagePerServiceBean appUsagePerService = new AppUsagePerServiceBean();
        Map<ServiceVersionSummaryBean, ServiceMetricsBean> data = new HashMap<>();
        //get App contracts
        try {
            List<ServiceVersionBean> contractedServices = query.getApplicationVersionContracts(avb).stream().map(contract -> contract.getService()).collect(Collectors.toList());
            //getid from kong
            IGatewayLink gateway = gatewayFacade.getDefaultGatewayLink();
            KongConsumer consumer = gateway.getConsumer(ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version));
            if (consumer != null && !StringUtils.isEmpty(consumer.getCustomId())) {
                String consumerId = consumer.getId();
                List<ApplicationVersionSummaryBean> avsbs = Collections.singletonList(DtoFactory.createApplicationVersionSummarBeanWithConsumerId(avb, consumerId));
                for (ServiceVersionBean svb : contractedServices) {
                    ServiceMetricsBean serviceMetrics = metrics.getServiceMetrics(svb, avsbs, from, to);
                    data.put(DtoFactory.createServiceVersionSummaryBean(svb), serviceMetrics);
                }
            }
        } catch (StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
        appUsagePerService.setData(data);
        return appUsagePerService;
    }

    private void serviceHasMetricsPolicy(ServiceVersionBean serviceVersion) throws StorageException {
        boolean serviceHasMetricsPolicy = !query.getEntityPoliciesByDefinitionId(serviceVersion.getService().getOrganization().getId(), serviceVersion.getService().getId(), serviceVersion.getVersion(), PolicyType.Service, Policies.DATADOG).isEmpty();
        // Add checks for other metrics policies here
        if (!serviceHasMetricsPolicy) {
            throw ExceptionFactory.noMetricsEnabledException();
        }
    }

    public ServiceMetricsBean getServiceUsage(String organizationId, String serviceId, String version, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        try {
            IGatewayLink gw = gatewayFacade.getDefaultGatewayLink();
            List<ApplicationVersionSummaryBean> consumers = query.getServiceContracts(organizationId, serviceId, version).stream().map(contract -> {
                KongConsumer consumer = gw.getConsumer(ConsumerConventionUtil.createAppUniqueId(contract.getApplication()));
                if (consumer != null && StringUtils.isNotEmpty(consumer.getId())) {
                    return DtoFactory.createApplicationVersionSummarBeanWithConsumerId(contract.getApplication(), consumer.getId());
                } else return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            log.info("Number of applications available for metrics: {}", consumers.size());
            ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
            serviceHasMetricsPolicy(svb);
            ServiceMetricsBean serviceMetrics = metrics.getServiceMetrics(svb, consumers, from, to);
            if (serviceMetrics != null && serviceMetrics.getException() == null) {
                return serviceMetrics;
            } else {
                if (serviceMetrics.getException() != null) {
                    throw serviceMetrics.getException();
                }
                log.info("Service metrics are null");
                throw ExceptionFactory.metricsUnavailableException();
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public ServiceMarketInfoBean getMarketInfo(String organizationId, String serviceId, String version) {
        try {
            ServiceVersionBean svb = storage.getServiceVersion(organizationId, serviceId, version);
            if (svb == null) throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            ServiceMarketInfoBean marketInfo = new ServiceMarketInfoBean();
            marketInfo.setUptime(metrics.getServiceUptime(getServiceVersion(organizationId, serviceId, version)));
            marketInfo.setFollowers(svb.getService().getFollowers().size());
            marketInfo.setDistinctUsers(query.getServiceContracts(organizationId, serviceId, version).size());
            return marketInfo;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public List<ApplicationVersionSummaryBean> listAppVersions(String organizationId, String applicationId) {
        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getApp(organizationId, applicationId);
        try {
            return query.getApplicationVersions(organizationId, applicationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public ContractBean getContract(String organizationId, String applicationId, String version, Long contractId) {
        boolean hasPermission = securityContext.hasPermission(PermissionType.appView, organizationId);
        try {
            ContractBean contract = storage.getContract(contractId);
            if (contract == null)
                throw ExceptionFactory.contractNotFoundException(contractId);
            // Hide some data if the user doesn't have the appView permission
            if (!hasPermission) {
                contract.getApplication().setApikey(null);
            }
            log.debug(String.format("Got contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$
            return contract;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteAllContracts(String organizationId, String applicationId, String version) {
        List<ContractSummaryBean> contracts = getApplicationVersionContracts(organizationId, applicationId, version);
        for (ContractSummaryBean contract : contracts) {
            deleteContract(organizationId, applicationId, version, contract.getContractId());
        }
    }

    /**
     * Deletes a contract from an application with a service:
     * <ul>
     * <li>Status Created/Ready:</li>
     * </ul>
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param contractId
     */
    public void deleteContract(String organizationId, String applicationId, String version, Long contractId) {
        try {
            ContractBean contract = getContract(organizationId, applicationId, version, contractId);
            ApplicationVersionBean avb;
            avb = storage.getApplicationVersion(organizationId, applicationId, version);
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

            //Revoke application's contract plugins

            gatewayFacade.getApplicationVersionGatewayLinks(avb).forEach(gw -> {
                try {
                    query.getApplicationVersionContractPolicies(organizationId, applicationId, version, contractId)
                            .stream().filter(policy -> policy.getGatewayId().equals(gw.getGatewayId()))
                            .forEach(policy -> {
                                try {
                                    deleteContractPolicy(contract, policy, gw);
                                } catch (StorageException ex) {
                                    throw ExceptionFactory.systemErrorException(ex);
                                }
                            });
                } catch (StorageException ex) {
                    throw ExceptionFactory.systemErrorException(ex);
                }
            });
            //Revoke admin priviledges if contract was with an admin service
            if (contract.getService().getService().isAdmin() && query.getApplicationVersionContracts(avb).stream().filter(c -> c.getService().getService().isAdmin()).collect(Collectors.toList()).size() == 1) {
                ManagedApplicationBean mab = query.resolveManagedApplicationByAPIKey(contract.getApplication().getApikey());
                mab.getApiKeys().remove(contract.getApplication().getApikey());
                storage.updateManagedApplication(mab);
            }

            //remove contract
            storage.deleteContract(contract);

            //validate application state
            // Validate the state of the application.
            if (avb.getStatus() != ApplicationStatus.Registered && !applicationValidator.isReady(avb)) {
                avb.setStatus(ApplicationStatus.Created);
            }
            storage.createAuditEntry(AuditUtils.contractBrokenFromApp(contract, securityContext));
            storage.createAuditEntry(AuditUtils.contractBrokenToService(contract, securityContext));
            log.debug(String.format("Deleted contract: %s", contract));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    private void deleteContractPolicy(ContractBean c, PolicyBean p, IGatewayLink gateway) throws StorageException {
        boolean deleted = false;
        switch (Policies.valueOf(p.getDefinition().getId().toUpperCase())) {
            case ACL:
                gateway.deleteConsumerACLPlugin(ConsumerConventionUtil.createAppUniqueId(c.getApplication()), p.getKongPluginId());
                deleted = true;
                break;
            case IPRESTRICTION:
            case REQUESTSIZELIMITING:
            case RATELIMITING:
                gateway.deleteApiPlugin(generateServiceUniqueName(c.getService()), p.getKongPluginId());
                deleted = true;
                break;
            default:
                break;
        }
        if (deleted) {
            storage.deletePolicy(p);
        }
    }

    public ApiRegistryBean getApiRegistryJSON(String organizationId, String applicationId, String version) {
        return getApiRegistry(organizationId, applicationId, version);
    }

    public ApiRegistryBean getApiRegistryXML(String organizationId, String applicationId, String version) {
        return getApiRegistry(organizationId, applicationId, version);
    }

    public void updateAppPolicy(String organizationId, String applicationId, String version, long policyId, UpdatePolicyBean bean) {
        // Make sure the app version exists.
        getAppVersion(organizationId, applicationId, version);
        try {
            PolicyBean policy = this.storage.getPolicy(PolicyType.Application, organizationId, applicationId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                String appId = ConsumerConventionUtil.createAppUniqueId(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion());
                policy.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration(), appId), PolicyType.Application).getPolicyJsonConfig());
            }
            policy.setModifiedOn(new Date());
            policy.setModifiedBy(this.securityContext.getCurrentUser());
            storage.updatePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyUpdated(policy, PolicyType.Application, null, securityContext));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteAppPolicy(String organizationId, String applicationId, String version, long policyId) {
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

    public void reorderApplicationPolicies(String organizationId, String applicationId, String version, PolicyChainBean policyChain) {
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

    public ServiceBean updateServiceTerms(String organizationId, String serviceId, UpdateServiceTermsBean serviceTerms) {
        try {
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            bean.setTerms(serviceTerms.getTerms());
            storage.updateService(bean);
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ServiceBean getService(String organizationId, String serviceId) {
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

    public void deletePlan(String organizationId, String planId) {
        if (!securityContext.hasPermission(PermissionType.planAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            //Get Plan info
            PlanBean plan = storage.getPlan(organizationId, planId);
            if (plan == null) throw ExceptionFactory.planNotFoundException(planId);
            //Get all plan versions
            List<PlanVersionBean> allPlanVersionBeans = query.findAllPlanVersionBeans(organizationId, plan.getId());
            //verify if planverions have running contracts
            for (PlanVersionBean pvb : allPlanVersionBeans) {
                deletePlanVersionInternal(pvb);
            }
            storage.deletePlan(plan);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    public void deletePlanVersion(String organizationId, String planId, String version) {
        PlanVersionBean pvb = getPlanVersion(organizationId, planId, version);
        try {
            if (query.getPlanVersions(organizationId, planId).size() == 1) {
                deletePlan(organizationId, planId);
            } else {
                deletePlanVersionInternal(pvb);
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deletePlanVersionInternal(PlanVersionBean pvb) throws StorageException {
        List<ContractBean> planVersionContracts = query.getPlanVersionContracts(pvb.getId());
        //for existing contract throw exception
        if (planVersionContracts != null && planVersionContracts.size() > 0) {
            throw ExceptionFactory.planCannotBeDeleted("Plan still has contracts linked");
        }
        //Delete plan policies
        for (PolicyBean policy : query.getPlanPolicies(pvb)) {
            storage.deletePolicy(policy);
        }
        storage.deletePlanVersion(pvb);
    }

    public void deleteService(String organizationId, String serviceId, boolean force) {
        if (!securityContext.hasPermission(PermissionType.svcAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            // Get Service
            ServiceBean serviceBean = storage.getService(organizationId, serviceId);
            if (serviceBean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }

            // Remove support entries
            List<SupportBean> supportTickets = listServiceSupportTickets(organizationId, serviceId);
            supportTickets.stream().forEach(ticket -> {
                deleteSupportTicket(organizationId, serviceId, ticket.getId());
            });

            // Remove service announcements
            List<AnnouncementBean> announcements = getServiceAnnouncements(organizationId, serviceId);
            announcements.stream().forEach(announcement -> {
                deleteServiceAnnouncement(organizationId, serviceId, announcement.getId());
            });

            // Get Service versions
            List<ServiceVersionSummaryBean> svsbs = query.getServiceVersions(serviceBean.getOrganization().getId(), serviceBean.getId());
            for (ServiceVersionSummaryBean svsb : svsbs) {
                deleteServiceVersionInternal(getServiceVersionInternal(svsb.getOrganizationId(), svsb.getId(), svsb.getVersion()), force);
            }


            //Service announcement events have a organizationid.serviceid nomenclature, so delete those events
            query.deleteAllEventsForEntity(new StringBuilder(organizationId).append(".").append(serviceId).toString());

            // Finally, delete the Service from API Engine
            storage.deleteService(serviceBean);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteServiceVersion(String organizationId, String serviceId, String version) {
        try {
            if (query.getServiceVersions(organizationId, serviceId).size() == 1) {
                deleteService(organizationId, serviceId, false);
            } else {
                ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
                deleteServiceVersionInternal(svb, false);
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deleteServiceVersionInternal(ServiceVersionBean svb, boolean force) throws StorageException {
        String organizationId = svb.getService().getOrganization().getId();
        String serviceId = svb.getService().getId();
        String version = svb.getVersion();
        List<ContractBean> contracts = query.getServiceContracts(organizationId, serviceId, version);
        //Check for existing contrasts, throw error if there still are any unless force deletion is true
        if (CollectionUtils.isNotEmpty(contracts)) {
            if (!force) {
                throw ExceptionFactory.serviceCannotDeleteException("Service version still has contracts");
            }
            else {
                for (ContractBean contract : contracts) {
                    deleteContract(contract.getApplication().getApplication().getOrganization().getId(), contract.getApplication().getApplication().getId(), contract.getApplication().getVersion(), contract.getId());
                }
            }
        }

        // Remove service definition if found
        InputStream definitionStream = getServiceDefinition(organizationId, serviceId, version);
        if (definitionStream != null) {
            deleteServiceDefinition(organizationId, serviceId, version);
        }

        // Remove service policies
        for (PolicySummaryBean policy : listServicePolicies(organizationId, serviceId, version)) {
            PolicyBean policyBean = storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policy.getId());
            storage.deletePolicy(policyBean);
        }
        //Perform the cleanup on the serviceversion's various gateways
        for (ServiceGatewayBean gwb : svb.getGateways()) {
            IGatewayLink gateway = gatewayFacade.createGatewayLink(gwb.getGatewayId());
            //Clean up the Managed App ACL(s)
            List<PolicyBean> policies = query.getManagedAppACLPolicies(organizationId, serviceId, version);
            for (PolicyBean policy : policies) {
                gateway.deleteConsumerACLPlugin(ConsumerConventionUtil.createAppUniqueId(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion()), policy.getKongPluginId());
                storage.deletePolicy(policy);
            }
            //Verify the status: if Published or deprecated we need to remove the API from Kong
            if (svb.getStatus().equals(ServiceStatus.Published) || svb.getStatus().equals(ServiceStatus.Deprecated)) {
                Service service = new Service(organizationId, serviceId, version);
                try {
                    gateway.retireService(service);
                } catch (Exception e) {
                    throw ExceptionFactory.actionException(Messages.i18n.format("RetireError"), e); //$NON-NLS-1$
                }
            }
            gateway.close();
        }
        // Remove events
        query.deleteAllEventsForEntity(generateServiceUniqueName(svb));

        // Remove gateway config & plan configuration for service version
        svb.getGateways().clear();
        svb.getPlans().clear();
        storage.updateServiceVersion(svb);

        // Now we can finally delete the version itself
        storage.deleteServiceVersion(svb);
    }

    public SearchResultsBean<AuditEntryBean> getServiceActivity(String organizationId, String serviceId, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, serviceId, null, ServiceBean.class, getPagingBean(page, pageSize));
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServiceSummaryBean> listServices(String organizationId) {
        // make sure the org exists
        get(organizationId);
        try {
            return query.getServicesInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updateService(String organizationId, String serviceId, UpdateServiceBean bean) {
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
            if (AuditUtils.valueChanged(serviceForUpdate.getCategories(), bean.getCategories())) {
                auditData.addChange("categories", serviceForUpdate.getCategories().toString(), bean.getCategories().toString()); //$NON-NLS-1$
                serviceForUpdate.setCategories(bean.getCategories());
            }
            if (AuditUtils.valueChanged(serviceForUpdate.getBase64logo(), bean.getBase64logo())) {
                auditData.addChange("logo", serviceForUpdate.getBase64logo(), bean.getBase64logo()); //$NON-NLS-1$
                serviceForUpdate.setBase64logo(bean.getBase64logo());
            }
            if (AuditUtils.valueChanged(serviceForUpdate.getName(), bean.getName())) {
                auditData.addChange("name", serviceForUpdate.getName(), bean.getName());
                if (query.getServiceByName(bean.getName()) != null) {
                    throw ExceptionFactory.serviceAlreadyExistsException(bean.getName());
                }
                serviceForUpdate.setName(bean.getName());
            }
            if (AuditUtils.valueChanged(serviceForUpdate.isAdmin(), bean.isAdmin())) {
                auditData.addChange("admin", serviceForUpdate.isAdmin().toString(), bean.isAdmin().toString());
                if (!securityContext.isAdmin()) {
                    throw ExceptionFactory.notAuthorizedException();
                }
                if (!query.getServiceVersionByStatusForService(new HashSet<>(Arrays.asList(ServiceStatus.Published, ServiceStatus.Deprecated)), serviceForUpdate).isEmpty()) {
                    throw ExceptionFactory.invalidServiceStatusException();
                }
                serviceForUpdate.setAdmin(bean.isAdmin());
            }
            if (AuditUtils.valueChanged(serviceForUpdate.getBasepaths(), bean.getBasepaths())) {
                validateServiceBasepaths(serviceForUpdate.getOrganization(), bean.getBasepaths());
                auditData.addChange("basepaths", serviceForUpdate.getBasepaths().toString(), bean.getBasepaths().toString());
                serviceForUpdate.setBasepaths(bean.getBasepaths());
                updateServiceBasepaths(serviceForUpdate);
            }
            storage.updateService(serviceForUpdate);
            storage.createAuditEntry(AuditUtils.serviceUpdated(serviceForUpdate, auditData, securityContext));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ServiceBean updateServiceBasepaths(String organizationId, String serviceId) {
        try {
            return updateServiceBasepaths(storage.getService(organizationId, serviceId));
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public ServiceBean updateServiceBasepaths(ServiceBean service) {
        try {
            Set<ServiceStatus> presentOnGateway = new HashSet<>();
            presentOnGateway.add(ServiceStatus.Deprecated);
            presentOnGateway.add(ServiceStatus.Published);
            List<ServiceVersionBean> gwServiceVersions = query.getServiceVersionByStatusForService(presentOnGateway, service);
            for (ServiceVersionBean svb : gwServiceVersions) {
                for (ServiceGatewayBean gwsvb : svb.getGateways()) {
                    gatewayFacade.createGatewayLink(gwsvb.getGatewayId()).updateServiceVersionOnGateway(svb);
                }
            }
            return service;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void addServiceBranding(String organizationId, String serviceId, String brandingId) {
        try {
            ServiceBean service = storage.getService(organizationId, serviceId);
            if (service == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            ServiceBrandingBean newBranding = validateServiceBranding(service, brandingId);
            if (newBranding != null) {
                List<ServiceVersionBean> gatewaySvbs = query.getServiceVersionByStatusForService(new HashSet<>(Arrays.asList(ServiceStatus.Published, ServiceStatus.Deprecated)), service);
                if (!gatewaySvbs.isEmpty()) {
                    for (ServiceVersionBean svb : gatewaySvbs) {
                        Service svc = new Service();
                        svc.setServiceId(serviceId);
                        svc.setOrganizationId(organizationId);
                        svc.setVersion(svb.getVersion());
                        svc.setBasepaths(service.getBasepaths());
                        for (ServiceGatewayBean svcGw : svb.getGateways()) {
                            gatewayFacade.createGatewayLink(svcGw.getGatewayId()).createServiceBranding(svc, newBranding);
                        }
                    }
                }
                if (service.getBrandings() == null) {
                    service.setBrandings(new HashSet<>());
                }
                String originalSet = service.getBrandings().toString();
                service.getBrandings().add(newBranding);
                EntityUpdatedData data = new EntityUpdatedData();
                data.addChange("brandings", originalSet, service.getBrandings().toString());
                storage.updateService(service);
                storage.createAuditEntry(AuditUtils.serviceUpdated(service, data, securityContext));
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void removeServiceBranding(String organizationId, String serviceId, String brandingId) {
        try {
            ServiceBean service = storage.getService(organizationId, serviceId);
            if (service == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            ServiceBrandingBean sbb = brandingFacade.getServiceBranding(brandingId);
            if (service.getBrandings() == null || !service.getBrandings().contains(sbb)) {
                throw ExceptionFactory.brandingNotFoundException(sbb.getId());
            }
            List<ServiceVersionBean> gatewaySvbs = query.getServiceVersionByStatusForService(new HashSet<>(Arrays.asList(ServiceStatus.Published, ServiceStatus.Deprecated)), service);
            if (!gatewaySvbs.isEmpty()) {
                for (ServiceVersionBean svb : gatewaySvbs) {
                    for (ServiceGatewayBean gwBean : svb.getGateways()) {
                        gatewayFacade.createGatewayLink(gwBean.getGatewayId()).deleteApi(generateServiceUniqueName(brandingId, serviceId, svb.getVersion()));
                    }
                }
            }
            String originalSet = service.getBrandings().toString();
            service.getBrandings().remove(sbb);
            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("brandings", originalSet, service.getBrandings().toString());
            storage.updateService(service);
            storage.createAuditEntry(AuditUtils.serviceUpdated(service, data, securityContext));
        } catch (StorageException ex) {

        }

    }

    public ServiceVersionEndpointSummaryBean getServiceVersionEndpointInfo(String organizationId, String serviceId, String version) {
        try {
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            GatewayBean gateway = gatewayFacade.get(getSingularGateway().getId());
            if (StringUtils.isEmpty(gateway.getEndpoint())) {
                throw new GatewayNotFoundException("no default gateway configured");
            }
            //path starts always with '\'
            String gatewayEndpoint = ((gateway.getEndpoint().endsWith("\\") ? gateway.getEndpoint().substring(0, gateway.getEndpoint().length() - 1) : gateway.getEndpoint()));
            ServiceVersionEndpointSummaryBean rval = new ServiceVersionEndpointSummaryBean();
            rval.setManagedEndpoints(GatewayPathUtilities.generateGatewayContextPath(organizationId, serviceVersion.getService().getBasepaths(), version).stream().map(contextPath -> gatewayEndpoint + contextPath).collect(Collectors.toList()));
            if (serviceVersion.getService().getBrandings() != null && !serviceVersion.getService().getBrandings().isEmpty()) {
                rval.setBrandingEndpoints(serviceVersion.getService().getBrandings().stream().map(branding -> new ServiceVersionEndpointSummaryBean().withManagedEndpoints(GatewayPathUtilities.generateGatewayContextPath(branding.getId(), serviceVersion.getService().getBasepaths(), version).stream().map(contextPath -> gatewayEndpoint + contextPath).collect(Collectors.toList()))).collect(Collectors.toSet()));
            }
            //get oauth endpoints if needed
            if (!StringUtils.isEmpty(serviceVersion.getProvisionKey())) {
                //construct the target url
                List<String> oauthAuths = new ArrayList<>();
                List<String> oauthTokens = new ArrayList<>();
                GatewayPathUtilities.generateGatewayContextPath(organizationId, serviceVersion.getService().getBasepaths(), version).forEach(basePath -> {
                    StringBuilder targetURI = new StringBuilder("").append(URIUtils.uriFinalslashRemover(gateway.getEndpoint()))
                            .append(URIUtils.uriFinalslashAppender(basePath))
                            .append(KongConstants.KONG_OAUTH_ENDPOINT + "/");
                    oauthAuths.add(targetURI.append(KongConstants.KONG_OAUTH2_ENDPOINT_AUTH).toString());
                    oauthTokens.add(targetURI.append(KongConstants.KONG_OAUTH2_ENDPOINT_TOKEN).toString());
                });

                rval.setOauth2AuthorizeEndpoints(oauthAuths);
                rval.setOauth2TokenEndpoints(oauthTokens);
                if (rval.getBrandingEndpoints() != null && !rval.getBrandingEndpoints().isEmpty()) {
                    rval.getBrandingEndpoints().forEach(endpoint -> {
                        List<String> brOauthAuths = new ArrayList<>();
                        List<String> brOauthTokens = new ArrayList<>();
                        endpoint.getManagedEndpoints().forEach(basePath -> {
                            StringBuilder brandedURI = new StringBuilder(URIUtils.uriFinalslashAppender(basePath))
                                    .append(KongConstants.KONG_OAUTH_ENDPOINT + "/");
                            brOauthAuths.add(brandedURI.append(KongConstants.KONG_OAUTH2_ENDPOINT_AUTH).toString());
                            brOauthTokens.add(brandedURI.append(KongConstants.KONG_OAUTH2_ENDPOINT_TOKEN).toString());
                        });
                        endpoint.setOauth2AuthorizeEndpoints(brOauthAuths);
                        endpoint.setOauth2TokenEndpoints(brOauthTokens);
                    });
                }
            } else {
                rval.setOauth2AuthorizeEndpoints(Collections.emptyList());
                rval.setOauth2TokenEndpoints(Collections.emptyList());
                if (rval.getBrandingEndpoints() != null && !rval.getBrandingEndpoints().isEmpty()) {
                    rval.getBrandingEndpoints().forEach(endpoint -> {
                        endpoint.setOauth2TokenEndpoints(Collections.emptyList());
                        endpoint.setOauth2AuthorizeEndpoints(Collections.emptyList());
                    });
                }
            }
            return rval;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public Map<String, VisibilityBean> getServiceVersionAvailabilityInfo(String organizationId, String serviceId, String version) {
        try {
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            //enrich visibility with the name of the marketplace based on the persisted prefix.
            for (VisibilityBean vb : serviceVersion.getVisibility()) {
                //get the name of the marketplace
                final ManagedApplicationBean managedApplication = query.findManagedApplication(vb.getCode());
                vb.setName(managedApplication.getName());
            }
            Map<String, VisibilityBean> serviceVisibilities = serviceVersion.getVisibility().stream().collect(Collectors.toMap(VisibilityBean::getCode, Function.identity()));
            return serviceVisibilities;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public KongPluginConfigList getServicePlugins(String organizationId, String serviceId, String version) {
        try {
            KongPluginConfigList servicePlugins;
            servicePlugins = null;
            String serviceKongId = generateServiceUniqueName(organizationId, serviceId, version);
            try {
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                servicePlugins = gateway.getServicePlugins(serviceKongId);
            } catch (StorageException e) {
                throw new ApplicationNotFoundException(e.getMessage());
            }
            return servicePlugins;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    //Superfluous now that enabling and disabling service are done through the updateservice method

    /*public KongPluginConfig changeEnabledStateServicePlugin(String organizationId, String serviceId, String version, String pluginId, boolean enable) {
        String serviceKongId = ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version);
        try {
            IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            KongPluginConfigList pluginConfigList = gateway.getServicePlugin(serviceKongId, pluginId);
            if (pluginConfigList != null && pluginConfigList.getData().size() > 0) {
                KongPluginConfig pluginConfig = pluginConfigList.getData().get(0);
                pluginConfig.setEnabled(enable);
                pluginConfig = gateway.updateServicePlugin(serviceKongId, pluginConfig);
                return pluginConfig;
            } else {
                return null;
            }
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }*/

    public SearchResultsBean<AuditEntryBean> getServiceVersionActivity(String organizationId, String serviceId, String version, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, serviceId, version, ServiceBean.class, getPagingBean(page, pageSize));
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServiceVersionSummaryBean> listServiceVersions(String organizationId, String serviceId) {
        // Try to get the service first - will throw a ServiceNotFoundException if not found.
        getService(organizationId, serviceId);
        try {
            return query.getServiceVersions(organizationId, serviceId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId, String version) {
        // Ensure the version exists first.
        getServiceVersionInternal(organizationId, serviceId, version);
        try {
            return query.getServiceVersionPlans(organizationId, serviceId, version);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public EnrichedPolicySummaryBean updateAndEnrichServicePolicy(String organizationId, String serviceId, String version, long policyId, UpdatePolicyBean bean) {
        return PolicyUtil.createEnrichedPolicySummary(updateServicePolicy(organizationId, serviceId, version, policyId, bean));
    }

    public PolicyBean updateServicePolicy(String organizationId, String serviceId, String version, long policyId, UpdatePolicyBean bean) {
        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
        if (svb.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        try {
            PolicyBean policy = storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            KongPluginConfig plugin = null;
            IGatewayLink gw = null;
            if ((svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Deprecated)
                    && !StringUtils.isEmpty(policy.getKongPluginId()) && !StringUtils.isEmpty(policy.getGatewayId())) {
                gw = gatewayFacade.createGatewayLink(policy.getGatewayId());
                plugin = gw.getPlugin(policy.getKongPluginId());
            }
            EntityUpdatedData data = new EntityUpdatedData();
            //We do not audit policy data because it may contain sensitive information
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                log.info("policy old_config:{}", policy.getConfiguration());
                String svcId = generateServiceUniqueName(organizationId, serviceId, version);
                policy.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), bean.getConfiguration(), svcId), PolicyType.Service, svcId).getPolicyJsonConfig());
                if (plugin != null) {
                    log.info("policy new_config:{}", bean.getConfiguration());
                    plugin.setConfig(new Gson().fromJson(policy.getConfiguration(), Policies.valueOf(policy.getDefinition().getId().toUpperCase()).getClazz()));
                }
                data.addChange("config", policy.getConfiguration(), bean.getConfiguration());
            }
            if (bean.isEnabled() != null && AuditUtils.valueChanged(policy.isEnabled(), bean.isEnabled())) {
                policy.setEnabled(bean.isEnabled());
                data.addChange("enabled", policy.isEnabled().toString(), bean.isEnabled().toString());
                if (plugin != null) {
                    plugin.setEnabled(bean.isEnabled());
                }
            }
            if (plugin != null && gw != null && !data.getChanges().isEmpty()) {
                gw.updateServicePlugin(generateServiceUniqueName(svb), plugin);
            }
            policy.setModifiedOn(new Date());
            policy.setModifiedBy(securityContext.getCurrentUser());
            storage.updatePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyUpdated(policy, PolicyType.Service, data, securityContext));
            log.debug(String.format("Updated service policy %s", policy)); //$NON-NLS-1$
            return policy;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteServicePolicy(String organizationId, String serviceId, String version, long policyId) {
        // Make sure the service exists
        ServiceVersionBean service = getServiceVersionInternal(organizationId, serviceId, version);
        if (service.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        try {
            PolicyBean policy = this.storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (service.getStatus() == ServiceStatus.Published || service.getStatus() == ServiceStatus.Deprecated
                    && !StringUtils.isEmpty(policy.getGatewayId()) && !StringUtils.isEmpty(policy.getKongPluginId())) {
                IGatewayLink gw = gatewayFacade.createGatewayLink(policy.getGatewayId());
                gw.deleteApiPlugin(generateServiceUniqueName(service), policy.getKongPluginId());
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

    public void deleteServiceDefinition(String organizationId, String serviceId, String version) {
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

    public void reorderServicePolicies(String organizationId, String serviceId, String version, PolicyChainBean policyChain) {
        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
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

    public PolicyChainBean getServicePolicyChain(String organizationId, String serviceId, String version, String planId) {
        // Try to get the service first - will throw an exception if not found.
        ServiceVersionBean svb = getServiceVersionInternal(organizationId, serviceId, version);
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

    public List<ContractSummaryBean> getServiceVersionContracts(String organizationId, String serviceId, String version, int page, int pageSize) {
        int pg = page;
        int pgSize = pageSize;
        if (pg <= 1) {
            pg = 1;
        }
        if (pgSize == 0) {
            pgSize = 200;
        }
        // Try to get the service first - will throw an exception if not found.
        getServiceVersionInternal(organizationId, serviceId, version);
        try {
            List<ContractSummaryBean> contracts = query.getServiceContracts(organizationId, serviceId, version, pg, pgSize);

            log.debug(String.format("Got service %s version %s contracts: %s", serviceId, version, contracts)); //$NON-NLS-1$
            return contracts;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public Set<ApplicationBean> listServiceConsumers(String orgId, String serviceId) {
        //get all service versions
        final List<ServiceVersionSummaryBean> serviceVersions = listServiceVersions(orgId, serviceId);
        Set<ApplicationBean> apps = new TreeSet<>();
        for (ServiceVersionSummaryBean svb : serviceVersions) {
            try {
                final List<ContractBean> serviceContracts = query.getServiceContracts(svb.getOrganizationId(), svb.getId(), svb.getVersion());
                for (ContractBean contract : serviceContracts) {
                    //contract means: service published and application registeredf
                    apps.add(contract.getApplication().getApplication());
                }

            } catch (StorageException e) {
                throw new SystemErrorException(e);
            }
        }
        return apps;
    }


    public PlanBean createPlan(String organizationId, NewPlanBean bean) {
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

    public PlanBean getPlan(String organizationId, String planId) {
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

    public SearchResultsBean<AuditEntryBean> getPlanActivity(String organizationId, String planId, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, planId, null, PlanBean.class, getPagingBean(page, pageSize));
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<PlanSummaryBean> listPlans(String organizationId) {
        get(organizationId);
        try {
            return query.getPlansInOrg(organizationId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updatePlan(String organizationId, String planId, UpdatePlanBean bean) {
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

    public PlanVersionBean createPlanVersion(String organizationId, String planId, NewPlanVersionBean bean) {
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
                    String planUniquerId = new StringBuilder(organizationId)
                            .append(".")
                            .append(planId)
                            .append(".")
                            .append(bean.getVersion())
                            .toString();
                    npb.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration(), planUniquerId), PolicyType.Plan).getPolicyJsonConfig());
                    createPlanPolicy(organizationId, planId, newVersion.getVersion(), npb);
                }
            } catch (Exception e) {
                // TODO it's ok if the clone fails - we did our best
            }
        }
        log.debug(String.format("Created plan %s version: %s", planId, newVersion)); //$NON-NLS-1$
        return newVersion;
    }

    public List<PolicySummaryBean> listPlanPolicies(String organizationId, String planId, String version) {
        // Try to get the plan first - will throw an exception if not found.
        getPlanVersion(organizationId, planId, version);
        try {
            return query.getPolicies(organizationId, planId, version, PolicyType.Plan);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<AuditEntryBean> getPlanVersionActivity(String organizationId, String planId, String version, int page, int pageSize) {
        try {
            SearchResultsBean<AuditEntryBean> rval;
            rval = query.auditEntity(organizationId, planId, version, PlanBean.class, getPagingBean(page, pageSize));
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<PlanVersionSummaryBean> listPlanVersions(String organizationId, String planId) {
        // Try to get the plan first - will throw a PlanNotFoundException if not found.
        getPlan(organizationId, planId);
        try {
            return query.getPlanVersions(organizationId, planId);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updatePlanPolicy(String organizationId, String planId, String version, long policyId, UpdatePolicyBean bean) {
        // Make sure the plan version exists
        getPlanVersion(organizationId, planId, version);
        try {
            PolicyBean policy = storage.getPolicy(PolicyType.Plan, organizationId, planId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                String planUniquerId = new StringBuilder(organizationId)
                        .append(".")
                        .append(planId)
                        .append(".")
                        .append(version)
                        .toString();
                policy.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), bean.getConfiguration(), planUniquerId), PolicyType.Plan).getPolicyJsonConfig());
                // Note: we do not audit the policy configuration since it may have sensitive data
            }
            policy.setModifiedOn(new Date());
            policy.setModifiedBy(this.securityContext.getCurrentUser());
            storage.updatePolicy(policy);
            storage.createAuditEntry(AuditUtils.policyUpdated(policy, PolicyType.Plan, null, securityContext));
            log.debug(String.format("Updated plan policy %s", policy)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deletePlanPolicy(String organizationId, String planId, String version, long policyId) {
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

    public void reorderPlanPolicies(String organizationId, String planId, String version, PolicyChainBean policyChain) {
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

    public void grant(String organizationId, GrantRoleBean bean) {
        // Verify that the references are valid.
        OrganizationBean org = get(organizationId);
        UserBean user = userFacade.get(bean.getUserId());
        roleFacade.get(bean.getRoleId());

        // If user had a pending membership request,
        MembershipData auditData = new MembershipData();
        auditData.setUserId(bean.getUserId());
        try {
            if (!idmStorage.getUserMemberships(bean.getUserId(), organizationId).isEmpty()) {
                String message = new StringBuilder(StringUtils.isEmpty(user.getFullName()) ? user.getUsername() : user.getFullName())
                        .append(" is already a member of ")
                        .append(org.getName())
                        .toString();
                throw ExceptionFactory.membershipAlreadyExists(message);
            }
            RoleMembershipBean membership = RoleMembershipBean.create(bean.getUserId(), bean.getRoleId(), organizationId);
            membership.setCreatedOn(new Date());
            // If the membership already exists, throw an exception to let the user know that person is already a member
            if (idmStorage.getMembership(bean.getUserId(), bean.getRoleId(), organizationId) == null) {
                idmStorage.createMembership(membership);
            }
            auditData.addRole(bean.getRoleId());
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
        try {
            storage.createAuditEntry(AuditUtils.membershipGrantedImplicit(organizationId, auditData, securityContext, true));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        //Trigger new event for accepted membership request

        fireEvent(organizationId, bean.getUserId(), EventType.MEMBERSHIP_GRANTED, null);
    }

    public void revoke(String organizationId, String roleId, String userId) {
        get(organizationId);
        userFacade.get(userId);
        roleFacade.get(roleId);
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
        fireEvent(organizationId, userId, EventType.MEMBERSHIP_REVOKED_ROLE, roleId);
    }

    public void updateMembership(String organizationId, String userId, GrantRoleBean bean) {
        get(organizationId);
        userFacade.get(userId);
        MembershipData auditData = new MembershipData();
        auditData.setUserId(userId);
        try {
            idmStorage.getRole(bean.getRoleId());
            idmStorage.deleteMemberships(userId, organizationId);
            RoleMembershipBean rmb = RoleMembershipBean.create(userId, bean.getRoleId(), organizationId);
            rmb.setCreatedOn(new Date());
            idmStorage.createMembership(rmb);
            auditData.addRole(bean.getRoleId());
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
        try {
            storage.createAuditEntry(AuditUtils.membershipUpdated(organizationId, auditData, securityContext));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        fireEvent(organizationId, userId, EventType.MEMBERSHIP_UPDATED, bean.getRoleId());
    }

    public void revokeAll(String organizationId, String userId) {
        get(organizationId);
        userFacade.get(userId);
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
        fireEvent(organizationId, userId, EventType.MEMBERSHIP_REVOKED, null);
    }

    public void transferOrgOwnership(String organizationId, TransferOwnershipBean bean) {
        get(organizationId);
        String currentOwnerId = bean.getCurrentOwnerId();
        String newOwnerId = bean.getNewOwnerId();
        try {
            // Remove current as Owner
            RoleMembershipBean currentOwnerBean = idmStorage.getMembership(currentOwnerId, "Owner", organizationId);
            if (currentOwnerBean == null) throw new MemberNotFoundException(currentOwnerId);
            idmStorage.deleteMembership(currentOwnerId, "Owner", organizationId);

            // And re-add as Developer
            RoleMembershipBean developerBean = RoleMembershipBean.create(currentOwnerId, "Developer", organizationId);
            developerBean.setCreatedOn(new Date());
            idmStorage.createMembership(developerBean);

            // Add new as Owner and remove other memberships
            Set<RoleMembershipBean> newOwnerMemberships = idmStorage.getUserMemberships(newOwnerId, organizationId);
            if (newOwnerMemberships.size() == 0) throw new MemberNotFoundException(newOwnerId);
            idmStorage.deleteMemberships(newOwnerId, organizationId);
            RoleMembershipBean newOwnerBean = RoleMembershipBean.create(newOwnerId, "Owner", organizationId);
            newOwnerBean.setCreatedOn(new Date());
            idmStorage.createMembership(newOwnerBean);

            // Add audit entry
            OwnershipTransferData auditData = new OwnershipTransferData();
            auditData.setPreviousOwnerId(currentOwnerId);
            auditData.setNewOwnerId(newOwnerId);
            storage.createAuditEntry(AuditUtils.ownershipTransferred(organizationId, auditData, securityContext));
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        fireEvent(organizationId, newOwnerId, EventType.MEMBERSHIP_TRANSFER, currentOwnerId);
    }

    public List<MemberBean> listMembers(String organizationId) {
        get(organizationId);
        try {
            Set<RoleMembershipBean> memberships = idmStorage.getOrgMemberships(organizationId);
            TreeMap<String, MemberBean> members = new TreeMap<>();
            for (RoleMembershipBean membershipBean : memberships) {
                UserBean user = idmStorage.getUser(membershipBean.getUserId());
                if (user != null) {
                    String userId = membershipBean.getUserId();
                    MemberBean member = members.get(userId);
                    if (member == null) {
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
                }
            }
            return new ArrayList<>(members.values());
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public boolean isMember(String userId, String organizationId) {
        List<MemberBean> members = listMembers(organizationId);
        for (MemberBean member : members) {
            if (member.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    /*********************************************UTILITIES**********************************************/
    /**
     * Creates a new application version.
     *
     * @param bean
     * @param application
     * @throws StorageException
     */
    protected ApplicationVersionBean createAppVersionInternal(NewApplicationVersionBean bean, ApplicationBean application) throws StorageException {
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
        newVersion.setApikey(apiKeyGenerator.generate());
        String appConsumerName = ConsumerConventionUtil.createAppUniqueId(newVersion);
        newVersion.setOauthClientRedirects(new HashSet<>(Collections.singletonList(PLACEHOLDER_CALLBACK_URI)));

        //create consumer on gateway
        //We create the new application version consumer
        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());

        gateway.createConsumer(appConsumerName, appConsumerName);
        KongPluginJWTResponse jwtCred = gateway.addConsumerJWT(appConsumerName, gatewayFacade.getDefaultGatewayPublicKey());
        //newVersion.setJwtKey(jwtCred.getKey());
        //newVersion.setJwtSecret(jwtCred.getSecret());
        gateway.addConsumerKeyAuth(appConsumerName, newVersion.getApikey());
        KongPluginOAuthConsumerResponse response = gateway.enableConsumerForOAuth(appConsumerName, new KongPluginOAuthConsumerRequest()
                .withClientId(appConsumerName)
                .withClientSecret(newVersion.getOauthClientSecret())
                .withName(application.getName())
                .withRedirectUri(new HashSet<>(Collections.singletonList(PLACEHOLDER_CALLBACK_URI))));
        newVersion.setOauthCredentialId(response.getId());

        storage.createApplicationVersion(newVersion);
        storage.createAuditEntry(AuditUtils.applicationVersionCreated(newVersion, securityContext));
        log.debug(String.format("Created new application version %s: %s", newVersion.getApplication().getName(), newVersion)); //$NON-NLS-1$
        return newVersion;
    }

    /**
     * Creates a contract.
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param bean
     * @throws StorageException
     * @throws Exception
     */
    protected ContractBean createContractInternal(String organizationId, String applicationId, String version, NewContractBean bean) throws StorageException, Exception {
        ContractBean contract;
        ApplicationVersionBean avb;
        avb = storage.getApplicationVersion(organizationId, applicationId, version);
        if (avb == null) {
            throw ExceptionFactory.applicationNotFoundException(applicationId);
        }
        if (avb.getStatus() == ApplicationStatus.Retired) {
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
        contract.setTermsAgreed(bean.getTermsAgreed());
        contract.setCreatedBy(securityContext.getCurrentUser());
        contract.setCreatedOn(new Date());
        storage.createContract(contract);
        storage.createAuditEntry(AuditUtils.contractCreatedFromApp(contract, securityContext));
        storage.createAuditEntry(AuditUtils.contractCreatedToService(contract, securityContext));
        // Validate the state of the application.
        if (!avb.getStatus().equals(ApplicationStatus.Registered) && applicationValidator.isReady(avb)) {
            avb.setStatus(ApplicationStatus.Ready);
        }
        // Update the version with new meta-data (e.g. modified-by)
        avb.setModifiedBy(securityContext.getCurrentUser());
        avb.setModifiedOn(new Date());
        storage.updateApplicationVersion(avb);

        return contract;
    }

    /**
     * Gets a policy by its id.  Also verifies that the policy really does belong to
     * the entity indicated.
     *
     * @param type
     * @param organizationId
     * @param entityId
     * @param entityVersion
     * @param policyId
     * @return a policy bean
     * @throws PolicyNotFoundException
     */
    protected PolicyBean doGetPolicy(PolicyType type, String organizationId, String entityId, String entityVersion, long policyId) throws PolicyNotFoundException {
        try {
            PolicyBean policy = storage.getPolicy(type, organizationId, entityId, entityVersion, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
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
            //PolicyTemplateUtil.generatePolicyDescription(policy);
            return policy;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
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
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PolicyBean doCreatePolicy(String organizationId, String entityId, String entityVersion, NewPolicyBean bean, PolicyType type) throws PolicyDefinitionNotFoundException {
        if (bean.getDefinitionId() == null) {
            ExceptionFactory.policyDefNotFoundException("null"); //$NON-NLS-1$
        }
        PolicyDefinitionBean def = null;
        try {
            def = storage.getPolicyDefinition(bean.getDefinitionId());
            if (def == null) {
                throw ExceptionFactory.policyDefNotFoundException(bean.getDefinitionId());
            }
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
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
            //Enable the policy if the value hasn't been set
            policy.setEnabled(bean.isEnabled() != null ? bean.isEnabled() : true);
            policy.setId(null);
            policy.setDefinition(def);
            policy.setName(def.getName());
            //validate (remove null values) and apply custom implementation for the policy
            String entityUniqueId = new StringBuilder(organizationId)
                    .append(".")
                    .append(entityId)
                    .append(".")
                    .append(entityVersion)
                    .toString();
            String policyJsonConfig = gatewayValidation.validate(new Policy(def.getId(), bean.getConfiguration(), entityUniqueId), type, generateServiceUniqueName(organizationId, entityId, entityVersion)).getPolicyJsonConfig();
            policy.setConfiguration(policyJsonConfig);
            policy.setCreatedBy(securityContext.getCurrentUser());
            policy.setCreatedOn(new Date());
            policy.setModifiedBy(securityContext.getCurrentUser());
            policy.setModifiedOn(new Date());
            policy.setOrganizationId(organizationId);
            policy.setEntityId(entityId);
            policy.setEntityVersion(entityVersion);
            policy.setType(type);
            policy.setOrderIndex(newIdx);
            policy.setKongPluginId(bean.getKongPluginId());
            policy.setContractId(bean.getContractId());
            policy.setGatewayId(bean.getGatewayId() == null ? gatewayFacade.getDefaultGateway().getId() : gatewayFacade.get(bean.getGatewayId()).getId());
            storage.createPolicy(policy);
            storage.createAuditEntry(AuditUtils.policyAdded(policy, type, securityContext));
            //PolicyTemplateUtil.generatePolicyDescription(policy);
            log.debug(String.format("Created app policy: %s", policy)); //$NON-NLS-1$
            return policy;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            log.debug("Failed to create policy {} for {}: {}", def.getId(), generateServiceUniqueName(organizationId, entityId, entityVersion), bean);
            throw new SystemErrorException(e);
        }
    }

    /**
     * @param svb
     */
    private void decryptEndpointProperties(ServiceVersionBean svb) {
        Map<String, String> endpointProperties = svb.getEndpointProperties();
        if (endpointProperties != null) {
            for (Map.Entry<String, String> entry : endpointProperties.entrySet()) {
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
            for (Map.Entry<String, String> entry : endpointProperties.entrySet()) {
                entry.setValue(AesEncrypter.encrypt(entry.getValue()));
            }
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
     * Creates a service version.
     *
     * @param bean
     * @param service
     * @param gateway
     * @throws Exception
     * @throws StorageException
     */
    protected ServiceVersionBean createServiceVersionInternal(NewServiceVersionBean bean, ServiceBean service, GatewaySummaryBean gateway) throws Exception, StorageException {
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
        //If the service is designated as an admin service, do not enable auto contract acceptance
        newVersion.setAutoAcceptContracts(service.isAdmin() != null && !service.isAdmin());
        if (gateway != null && newVersion.getGateways() == null) {
            newVersion.setGateways(new HashSet<>());
            ServiceGatewayBean sgb = new ServiceGatewayBean();
            sgb.setGatewayId(gateway.getId());
            newVersion.getGateways().add(sgb);
        }
        //TODO add path information to endpoint properties
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
     * create the default service policies
     *
     * @param svb
     */
    public void createDefaultServicePolicies(ServiceVersionBean svb, boolean checkForConflicts) {
        try {
            Set<PolicyDefinitionBean> defPolDefs = query.getDefaultServicePolicyDefs();

            for (PolicyDefinitionBean polDef : defPolDefs) {
                Policies type = Policies.valueOf(polDef.getId().toUpperCase());
                if (query.getEntityPoliciesByDefinitionId(svb.getService().getOrganization().getId(), svb.getService().getId(), svb.getVersion(), PolicyType.Service, type).isEmpty()) {

                    if (checkForConflicts &&
                            type == Policies.KEYAUTHENTICATION &&
                            !query.getEntityPoliciesByDefinitionId(svb.getService().getOrganization().getId(), svb.getService().getId(), svb.getVersion(), PolicyType.Service, Policies.OAUTH2).isEmpty()) {
                        continue;
                    }

                    String policyJsonConfig;
                    Gson gson = new Gson();
                    switch (type) {
                        case ACL:
                            policyJsonConfig = gson.toJson(new KongPluginACL()
                                    .withWhitelist(Collections.singletonList(generateServiceUniqueName(svb))));
                            break;
                        default:
                            policyJsonConfig = polDef.getDefaultConfig();
                            break;
                    }
                    String[] ids = ServiceConventionUtil.getOrgSvcVersionIds(svb);

                    int newIdx = 0;
                    newIdx = query.getMaxPolicyOrderIndex(ids[0], ids[1], ids[2], PolicyType.Service) + 1;

                    PolicyBean policy = new PolicyBean();

                    policy.setEnabled(true);
                    policy.setDefinition(polDef);
                    policy.setName(polDef.getName());
                    policy.setConfiguration(policyJsonConfig);
                    policy.setCreatedBy(securityContext.getCurrentUser());
                    policy.setCreatedOn(new Date());
                    policy.setModifiedBy(securityContext.getCurrentUser());
                    policy.setModifiedOn(new Date());
                    policy.setOrganizationId(ids[0]);
                    policy.setEntityId(ids[1]);
                    policy.setEntityVersion(ids[2]);
                    policy.setType(PolicyType.Service);
                    policy.setOrderIndex(newIdx);

                    storage.createPolicy(policy);
                    storage.createAuditEntry(AuditUtils.policyAdded(policy, PolicyType.Service, securityContext));
                }
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    /**
     * @param organizationId
     * @param serviceId
     * @param version
     * @param data
     */
    protected void storeServiceDefinition(String organizationId, String serviceId, String version, ServiceDefinitionType definitionType, InputStream data) {
        InputStream localData = data;
        if (!securityContext.hasPermission(PermissionType.svcEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);
            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            if (serviceVersion.getDefinitionType() != definitionType) {
                serviceVersion.setDefinitionType(definitionType);
                storage.updateServiceVersion(serviceVersion);
            }
            storage.createAuditEntry(AuditUtils.serviceDefinitionUpdated(serviceVersion, securityContext));
            List<String> svPath = GatewayPathUtilities.generateGatewayContextPath(organizationId, serviceVersion.getService().getBasepaths(), serviceVersion.getVersion());
            localData = transformJSONObjectDef(localData, serviceVersion, svPath.get(0));
            //safety check
            if (localData == null) throw new DefinitionException("The Swagger data returned is invalid");
            storage.updateServiceDefinition(serviceVersion, localData);
            log.debug(String.format("Stored service definition %s: %s", serviceId, serviceVersion)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }


    /**
     * Because Swagger validation is too strict - see method transformSwaggerDef() - a custom json updater has been implemented.
     *
     * @param data
     * @param serviceVersionBean
     * @param serviceVersionPath
     * @return
     * @throws StorageException
     * @throws IOException
     */
    private InputStream transformJSONObjectDef(InputStream data, ServiceVersionBean serviceVersionBean, String serviceVersionPath) throws StorageException, IOException {
        if (data != null) {
            String jsonTxt = IOUtils.toString(data);
            JSONObject json = new JSONObject(jsonTxt);

            //set base path
            json.remove("basePath");
            json.put("basePath", serviceVersionPath);

            //empty host
            json.remove("host");
            json.put("host", gatewayFacade.getDefaultGatewayHost());

            //add only https schema
            json.remove("schemes");
            JSONArray schemesArray = new JSONArray();
            schemesArray.put("https");
            json.put("schemes", schemesArray);

            //set online doc if present
            try {
                //get doc link
                JSONObject externalDocs = json.getJSONObject("externalDocs");
                String docUrl = externalDocs.getString("url");
                //update service
                serviceVersionBean.setOnlinedoc(docUrl);
            } catch (JSONException jsonex) {
                //continue::don't do anything -> no external doc present
            }
            //serialize to calling method
            return new ByteArrayInputStream(json.toString().getBytes());
        } else return null;
    }

    /**
     * Parse the to date query param.
     *
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
     *
     * @param toDate
     */
    private DateTime parseToDate(String toDate) {
        // Default to now
        return parseDate(toDate, DateTime.now().withZone(DateTimeZone.UTC), false);
    }

    /**
     * Parses a query param representing a date into an actual date object.
     *
     * @param dateStr
     * @param defaultDate
     * @param floor
     */
    private DateTime parseDate(String dateStr, DateTime defaultDate, boolean floor) {
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
     *
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
     *
     * @param from
     * @param to
     * @param interval
     */
    /*private void validateTimeSeriesMetric(DateTime from, DateTime to, HistogramIntervalType interval) throws InvalidMetricCriteriaException {
        long millis = to.getMillis() - from.getMillis();
        long divBy = interval.getMillis();
        long totalDataPoints = millis / divBy;
        if (totalDataPoints > 5000) {
            throw ExceptionFactory.invalidMetricCriteriaException(Messages.i18n.format("OrganizationResourceImpl.MetricDataSetTooLarge")); //$NON-NLS-1$
        }
    }*/

    /**
     * Gets the API registry.
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @throws ApplicationNotFoundException
     * @throws NotAuthorizedException
     */
    protected ApiRegistryBean getApiRegistry(String organizationId, String applicationId, String version) throws ApplicationNotFoundException, NotAuthorizedException {
        // Try to get the application first - will throw a ApplicationNotFoundException if not found.
        getAppVersion(organizationId, applicationId, version);
        Map<String, IGatewayLink> gatewayLinks = new HashMap<>();
        Map<String, GatewayBean> gateways = new HashMap<>();
        boolean txStarted = false;
        try {
            ApiRegistryBean apiRegistry = query.getApiRegistry(organizationId, applicationId, version);

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
                ServiceBean service = storage.getService(api.getServiceOrgId(), api.getServiceId());
                ServiceEndpoint se = link.getServiceEndpoint(service.getBasepaths(), api.getServiceOrgId(), api.getServiceId(), api.getServiceVersion());
                String apiEndpoint = se.getEndpoint();
                api.setHttpEndpoint(apiEndpoint);
            }
            return apiRegistry;
        } catch (StorageException | GatewayAuthenticationException e) {
            throw new SystemErrorException(e);
        } finally {
            if (txStarted) {
                for (IGatewayLink link : gatewayLinks.values()) {
                    link.close();
                }
            }
        }
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


    /**
     * FOLLOWERS
     */
    public ServiceBean addServiceFollower(String organizationId, String serviceId, String userId) {
        try {
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            if (!bean.getFollowers().contains(userId)) bean.getFollowers().add(userId);
            storage.updateService(bean);
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ServiceBean removeServiceFollower(String organizationId, String serviceId, String userId) {
        try {
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            if (bean.getFollowers().contains(userId)) bean.getFollowers().remove(userId);
            storage.updateService(bean);
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ServiceFollowers getServiceFollowers(String organizationId, String serviceId) {
        try {
            ServiceFollowers followers = new ServiceFollowers();
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            if (bean.getFollowers() != null) {
                followers.setFollowers(bean.getFollowers());
                followers.setTotal(bean.getFollowers().size());
            }
            storage.updateService(bean);
            return followers;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * ANNOUNCEMENTS
     */

    public AnnouncementBean createServiceAnnouncement(String organizationId, String serviceId, NewAnnouncementBean newAnnouncementBean) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            AnnouncementBean ann = new AnnouncementBean();
            ann.setTitle(newAnnouncementBean.getTitle());
            ann.setDescription(newAnnouncementBean.getDescription());
            ann.setCreatedBy(securityContext.getCurrentUser());
            ann.setCreatedOn(new Date());
            ann.setOrganizationId(bean.getOrganization().getId());
            ann.setServiceId(bean.getId());
            storage.createServiceAnnouncement(ann);
            //Fire off new event in order to notify those that need to be notified
            announcement.fire(ann);
            return ann;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteServiceAnnouncement(String organizationId, String serviceId, Long id) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            AnnouncementBean ann = storage.getServiceAnnouncement(id);
            if (ann != null) {
                if (ann.getOrganizationId().equals(organizationId) && ann.getServiceId().equals(serviceId)) {
                    storage.deleteServiceAnnouncement(ann);
                }
                //Delete new announcement notifications/events
                query.deleteAllEventsForAnnouncement(id);
            }
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public AnnouncementBean getServiceAnnouncement(String organizationId, String serviceId, Long id) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            AnnouncementBean ann = storage.getServiceAnnouncement(id);
            //verify the announcement is for given service
            if (ann.getOrganizationId().equals(organizationId) && ann.getServiceId().equals(serviceId)) return ann;
            else return null;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<AnnouncementBean> getServiceAnnouncements(String organizationId, String serviceId) {
        List<AnnouncementBean> announcementBeans = null;
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            announcementBeans = query.listServiceAnnouncements(organizationId, serviceId);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        return announcementBeans;
    }

    public void requestMembership(String orgId) {
        OrganizationBean org = get(orgId);
        if (org == null) {
            throw ExceptionFactory.organizationNotFoundException(orgId);
        }
        if (org.isOrganizationPrivate()) {
            throw ExceptionFactory.membershipRequestFailedException("Organization is private");
        }
        if (securityContext.getCurrentUser() == null || securityContext.getCurrentUser().isEmpty()) {
            throw ExceptionFactory.userNotFoundException(securityContext.getCurrentUser());
        }
        if (isMember(securityContext.getCurrentUser(), orgId)) {
            throw ExceptionFactory.membershipRequestFailedException("Already a member");
        }
        try {
            EventBean event = query.getEventByOriginDestinationAndType(securityContext.getCurrentUser(), org.getId(), EventType.MEMBERSHIP_PENDING);
            if (event != null) {
                throw ExceptionFactory.membershipRequestFailedException("Membership already requested, still pending");
            }
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
        fireEvent(securityContext.getCurrentUser(), org.getId(), EventType.MEMBERSHIP_PENDING, null);
    }

    public void rejectMembershipRequest(String organizationId, String userId) {
        fireEvent(get(organizationId).getId(), userFacade.get(userId).getUsername(), EventType.MEMBERSHIP_REJECTED, null);
    }

    /**
     * SUPPORT FUNCTIONALITY
     */
    public SupportBean createServiceSupportTicket(String organizationId, String serviceId, NewSupportBean supportBean) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            SupportBean sb = new SupportBean();
            sb.setCreatedBy(securityContext.getCurrentUser());
            sb.setCreatedOn(new Date());
            sb.setOrganizationId(organizationId);
            sb.setServiceId(serviceId);
            sb.setTitle(supportBean.getTitle());
            sb.setDescription(supportBean.getDescription());
            sb.setStatus(SupportStatus.OPEN.toString());
            sb.setTotalComments(0);//start with no comments
            storage.createServiceSupport(sb);
            return sb;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public SupportBean updateServiceSupportTicket(String organizationId, String serviceId, Long supportId, UpdateSupportBean updateSupportBean) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            SupportBean sb = storage.getServiceSupport(supportId);
            //verify that org and service are correct
            if (sb.getOrganizationId().equals(organizationId) && sb.getServiceId().equals(serviceId)) {
                //perform update
                sb.setTitle(updateSupportBean.getTitle());
                sb.setDescription(updateSupportBean.getDescription());
                sb.setStatus(updateSupportBean.getStatus().toString());
                storage.updateServiceSupport(sb);
                return sb;
            } else return null;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public SupportBean getServiceSupportTicket(String organizationId, String serviceId, Long supportId) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            SupportBean sb = storage.getServiceSupport(supportId);
            //verify that org and service are correct
            if (sb.getOrganizationId().equals(organizationId) && sb.getServiceId().equals(serviceId)) {
                return sb;
            } else return null;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteSupportTicket(String organizationId, String serviceId, Long supportId) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            SupportBean sb = storage.getServiceSupport(supportId);
            //verify that org and service are correct
            if (sb.getOrganizationId().equals(organizationId) && sb.getServiceId().equals(serviceId)) {
                storage.deleteServiceSupport(sb);
            }
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public List<SupportBean> listServiceSupportTickets(String organizationId, String serviceId) {
        try {
            //verify that service exists - else will throw an exception
            ServiceBean bean = storage.getService(organizationId, serviceId);
            if (bean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            return query.listServiceSupportTickets(organizationId, serviceId);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public SupportComment addServiceSupportComment(Long supportBeanId, NewSupportComment comment) {
        try {
            SupportBean sb = storage.getServiceSupport(supportBeanId);
            SupportComment sc = new SupportComment();
            sc.setCreatedBy(securityContext.getCurrentUser());
            sc.setCreatedOn(new Date());
            sc.setComment(comment.getComment());
            sc.setSupportId(supportBeanId);
            storage.createServiceSupportComment(sc);
            sb.setTotalComments((sb.getTotalComments() + 1));
            storage.updateServiceSupport(sb);
            return sc;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public SupportComment updateServiceSupportComment(Long supportId, Long supportCommentId, UpdateSupportComment updateSupportComment) {
        try {
            SupportComment sc = storage.getServiceSupportComment(supportCommentId);
            sc.setComment(updateSupportComment.getComment());
            storage.updateServiceSupportComment(sc);
            return sc;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void deleteServiceSupportComment(Long supportBeanId, Long supportCommentId) {
        try {
            SupportBean sb = storage.getServiceSupport(supportBeanId);
            SupportComment sc = storage.getServiceSupportComment(supportCommentId);
            storage.deleteServiceSupportComment(sc);
            sb.setTotalComments((sb.getTotalComments() - 1));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public SupportComment getServiceSupportComment(Long supportId, Long supportCommentId) {
        try {
            return storage.getServiceSupportComment(supportCommentId);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public List<SupportComment> listServiceSupportTicketComments(Long supportId) {
        try {
            return query.listServiceSupportComment(supportId);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * This implementation deletes an organization only when no services or applications are present,
     * All plans must be removed as well.
     * The method will remove the organization and the existing memberships from users.
     * <p>
     * TODO implement method that cleansup an organization when no contracts are present.
     *
     * @param orgId
     */
    public void deleteOrganization(String orgId, boolean force) {
        //remove memberships
        //leave audit and add closure
        //remove organization
        OrganizationBean org = get(orgId);
        // If the user is an administrator, force delete everything
        if (securityContext.isAdmin()) {
            try {

                // Start by deleting applications if applicable
                deleteApplicationsInOrganization(orgId);

                // Start service removal
                List<ServiceSummaryBean> orgServices = query.getServicesInOrg(orgId);
                for (ServiceSummaryBean svcSummary : orgServices) {
                    deleteService(orgId, svcSummary.getId(), force);
                }

                // Start plan removal
                List<PlanSummaryBean> plans = query.getPlansInOrg(orgId);
                for (PlanSummaryBean planSummary : plans) {
                    // At this point, the plans should no longer have any contracts
                    deletePlan(orgId, planSummary.getId());
                }

                // Delete the organization
                deleteOrganizationInternal(org);
            }
            catch (StorageException ex) {
                throw ExceptionFactory.systemErrorException(ex);
            }
        }

        else try {
            List<ServiceSummaryBean> services = query.getServicesInOrg(orgId);
            if (!services.isEmpty()) {
                if (!query.getServiceVersionsInOrgByStatus(orgId, ServiceStatus.Published).isEmpty() || !query.getServiceVersionsInOrgByStatus(orgId, ServiceStatus.Deprecated).isEmpty()) {
                    for (ServiceSummaryBean svcSummary : services) {
                        ServiceBean service = getService(svcSummary.getOrganizationId(), svcSummary.getId());
                        if (!query.getServiceContracts(service).isEmpty()) {
                            throw ExceptionFactory.orgCannotBeDeleted("The organization's services still have contracts");
                        }
                    }
                    //TODO - delete the services that don't have any contracts
                    throw ExceptionFactory.orgCannotBeDeleted("The organization still has published and/or deprecated service versions");
                }
                //If the organization doesn't have any published or deprecated services, those services should be safe to delete
                else {
                    //TODO - delete all unpublished services in one go
                    throw ExceptionFactory.orgCannotBeDeleted("The organization still has services");
                }
            }
            //If the organization doesn't have services, check if it has plans
            if (!query.getPlansInOrg(org.getId()).isEmpty()) {
                //TODO - Delete all plans if the org doesn't have services
                throw ExceptionFactory.orgCannotBeDeleted("The organization still has plans");
            }
            //By now we can assume that either an exception has been thrown or the organization doesnt't have any services left
            deleteApplicationsInOrganization(orgId);
            deleteOrganizationInternal(org);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deleteApplicationsInOrganization(String orgId) throws StorageException {
        List<ApplicationSummaryBean> apps = query.getApplicationsInOrg(orgId, false);

        if (!apps.isEmpty()) {
            for (ApplicationSummaryBean appSumm : apps) {
                deleteApp(orgId, appSumm.getId());
            }
        }
    }

    private void deleteOrganizationInternal(OrganizationBean org) throws StorageException {
        //This assumes the preliminary work of deleting services, plans and applications has already been done
        //in the methods calling this method
        for (RoleMembershipBean member : idmStorage.getOrgMemberships(org.getId())) {
            idmStorage.deleteMemberships(member.getUserId(), org.getId());
        }
        //Delete all related events
        List<EventBean> events = query.getAllEventsRelatedToOrganization(org.getId());
        for (EventBean event : events) {
            storage.deleteEvent(event);
        }
        storage.deleteOrganization(org);
    }

    public NewApiKeyBean reissueApplicationVersionApiKey(String organizationId, String applicationId, String version) {
        return reissueApplicationVersionApiKey(getAppVersion(organizationId, applicationId, version));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public NewApiKeyBean reissueApplicationVersionApiKey(ApplicationVersionBean avb) {
        try {
            String organizationId = avb.getApplication().getOrganization().getId();
            String applicationId = avb.getApplication().getId();
            String version = avb.getVersion();
            //Get list of contracts associated with application
            List<ContractSummaryBean> contractSummaries = query.getApplicationContracts(organizationId, applicationId, version);
            //Generate new API key for contracts
            String newApiKey = apiKeyGenerator.generate();

            //Keep old API key for auditing purposes and retrieve & delete correct plugin on gateway
            String revokedKey = avb.getApikey();
            //If the application is registered, change the API key on all relevant gateways
            String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
            if (avb.getStatus() == ApplicationStatus.Registered) {
                try {
                    Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(contractSummaries);
                    for (IGatewayLink gatewayLink : gateways.values()) {
                        try {
                            gatewayLink.updateConsumerKeyAuthCredentials(appConsumerName, revokedKey, newApiKey);
                        } catch (Exception e) {
                            throw ExceptionFactory.apiKeyAlreadyExistsException(newApiKey);
                        }
                        gatewayLink.close();
                    }
                } catch (Exception e) {
                    throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
                }
            } else {
                //If the application isn't retired, it exists only on the default gateway
                if (avb.getStatus() != ApplicationStatus.Retired) {
                    try {
                        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                        gateway.updateConsumerKeyAuthCredentials(appConsumerName, revokedKey, newApiKey);
                    } catch (Exception ex) {
                        throw ExceptionFactory.apiKeyAlreadyExistsException(newApiKey);
                    }
                } else {
                    throw ExceptionFactory.invalidApplicationStatusException();
                }
            }
            //Update managed app keys if any
            ManagedApplicationBean mab = query.resolveManagedApplicationByAPIKey(revokedKey);
            if (mab != null) {
                mab.getApiKeys().remove(revokedKey);
                mab.getApiKeys().add(newApiKey);
                storage.updateManagedApplication(mab);
            }

            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("apikey", revokedKey, newApiKey);
            query.updateApplicationVersionApiKey(avb, newApiKey);
            storage.createAuditEntry(AuditUtils.credentialsReissue(avb, data, AuditEntryType.KeyAuthReissuance, securityContext));
            return new NewApiKeyBean(organizationId, applicationId, version, revokedKey, newApiKey);
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public NewOAuthCredentialsBean reissueApplicationVersionOAuthCredentials(String organizationId, String applicationId, String version) {
        return reissueApplicationVersionOAuthCredentials(getAppVersion(organizationId, applicationId, version));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public NewOAuthCredentialsBean reissueApplicationVersionOAuthCredentials(ApplicationVersionBean avb) {
        try {
            //Check if the app version has Oauthcredentials
            if (avb.getOauthClientSecret() == null) {
                return null;
            }
            //Check if the app has a callback uri, if it doesn't it isn't a consumer on the gateway(s), but we still want to
            //reissue the OAuth2 credentials
            NewOAuthCredentialsBean rval = new NewOAuthCredentialsBean();
            rval.setOrganizationId(avb.getApplication().getOrganization().getId());
            rval.setApplicationId(avb.getApplication().getId());
            rval.setVersion(avb.getVersion());
            rval.setRevokedClientSecret(avb.getOauthClientSecret());
            avb.setOauthClientSecret(apiKeyGenerator.generate());
            rval.setNewClientSecret(avb.getOauthClientSecret());
            String appConsumerName = ConsumerConventionUtil.createAppUniqueId(avb);
            if (!(avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().stream().filter(ValidationUtils::isValidAbsoluteURI).collect(Collectors.toSet()).isEmpty())) {
                KongPluginOAuthConsumerRequest oAuthConsumerRequest = new KongPluginOAuthConsumerRequest()
                        .withClientId(appConsumerName)
                        .withClientSecret(avb.getOauthClientSecret())
                        .withRedirectUri(avb.getOauthClientRedirects())
                        .withName(avb.getApplication().getName())
                        .withId(avb.getOauthCredentialId());
                if (avb.getStatus() == ApplicationStatus.Registered) {
                    try {
                        List<ContractSummaryBean> contractSummaries = query.getApplicationContracts(rval.getOrganizationId(), rval.getApplicationId(), rval.getVersion());
                        Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(contractSummaries);
                        for (IGatewayLink gatewayLink : gateways.values()) {
                            try {
                                gatewayLink.updateConsumerOAuthCredentials(ConsumerConventionUtil.createAppUniqueId(avb), oAuthConsumerRequest);
                            } catch (Exception e) {
                                throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e);
                            }
                            gatewayLink.close();
                        }
                    } catch (Exception e) {
                        throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
                    }
                } else {
                    if (avb.getStatus() != ApplicationStatus.Retired) {
                        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                        try {
                            gateway.updateConsumerOAuthCredentials(ConsumerConventionUtil.createAppUniqueId(avb), oAuthConsumerRequest);
                        } catch (Exception e) {
                            throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e);
                        }
                    } else {
                        throw ExceptionFactory.invalidApplicationStatusException();
                    }
                }
            }
            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("OAuth2 Client Secret", rval.getRevokedClientSecret(), rval.getNewClientSecret());
            storage.updateApplicationVersion(avb);
            storage.createAuditEntry(AuditUtils.credentialsReissue(avb, data, AuditEntryType.OAuth2Reissuance, securityContext));
            return rval;
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public Map<String, IGatewayLink> getApplicationGatewayLinks(List<ContractSummaryBean> contractSummaries) {
        try {
            Map<String, IGatewayLink> links = new HashMap<>();
            for (ContractSummaryBean contract : contractSummaries) {
                ServiceVersionBean svb = storage.getServiceVersion(contract.getServiceOrganizationId(), contract.getServiceId(), contract.getServiceVersion());
                Set<ServiceGatewayBean> gateways = svb.getGateways();
                for (ServiceGatewayBean serviceGatewayBean : gateways) {
                    if (!links.containsKey(serviceGatewayBean.getGatewayId())) {
                        IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                        links.put(serviceGatewayBean.getGatewayId(), gatewayLink);
                    }
                }
            }
            return links;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public Map<String, IGatewayLink> getApplicationContractGatewayLinks(List<ContractBean> contracts) {
        Map<String, IGatewayLink> links = new HashMap<>();
        for (ContractBean contract : contracts) {
            Set<ServiceGatewayBean> gateways = contract.getService().getGateways();
            for (ServiceGatewayBean serviceGatewayBean : gateways) {
                if (!links.containsKey(serviceGatewayBean.getGatewayId())) {
                    IGatewayLink gateway = createGatewayLink(serviceGatewayBean.getGatewayId());
                    links.put(serviceGatewayBean.getGatewayId(), gateway);
                }
            }
        }
        return links;
    }

    public ServiceTagsBean getServiceTags(String organizationId, String serviceId) {
        ServiceBean service = getService(organizationId, serviceId);
        return new ServiceTagsBean(organizationId, serviceId, service.getCategories());
    }

    public void updateServiceTags(String organizationId, String serviceId, ServiceTagsBean bean) {
        ServiceBean service = getService(organizationId, serviceId);
        updateServiceTagsInternal(service, bean.getTags());
    }

    public void addServiceTag(String organizationId, String serviceId, TagBean tag) {
        ServiceBean service = getService(organizationId, serviceId);
        Set<String> newTags = new TreeSet<>(service.getCategories());
        newTags.add(tag.getTag());
        updateServiceTagsInternal(service, newTags);
    }

    public void deleteServiceTag(String organizationId, String serviceId, TagBean tag) {
        ServiceBean service = getService(organizationId, serviceId);
        Set<String> newTags = new TreeSet<>(service.getCategories());
        newTags.remove(tag.getTag());
        updateServiceTagsInternal(service, newTags);
    }

    private void updateServiceTagsInternal(ServiceBean service, Set<String> newTags) {
        EntityUpdatedData data = new EntityUpdatedData();
        if (AuditUtils.valueChanged(service.getCategories(), newTags)) {
            data.addChange("tags", service.getCategories().toString(), newTags.toString());
            AuditEntryBean entry = AuditUtils.serviceUpdated(service, data, securityContext);
            service.setCategories(newTags);
            try {
                storage.updateService(service);
                storage.createAuditEntry(entry);
            } catch (StorageException ex) {
                throw ExceptionFactory.systemErrorException(ex);
            }
        }
    }

    private ServiceVersionBean filterServiceVersionByAppPrefix(ServiceVersionBean svb) {
        String prefix = appContext.getApplicationPrefix();
        try {
            Set<String> publisherAndConsentPrefixes = query.getManagedAppPrefixesForTypes(Arrays.asList(ManagedApplicationTypes.Consent, ManagedApplicationTypes.Publisher, ManagedApplicationTypes.Admin));
            Set<String> allowedPrefixes = new HashSet<>(publisherAndConsentPrefixes);
            svb.getVisibility().forEach(vis -> {
                allowedPrefixes.add(vis.getCode());
            });
            log.debug("allowedPrefixes:{}", allowedPrefixes);
            if (!allowedPrefixes.contains(prefix)) {
                throw ExceptionFactory.serviceVersionNotAvailableException(svb.getService().getId(), svb.getVersion());
            }
            //TODO - Remove the provision key if the appcontext is not a consent app or a publisher
            /*if (!publisherAndConsentPrefixes.contains(prefix)) {
                svb.setProvisionKey(null);
            }*/
            return svb;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }


    public OAuth2TokenPaginationBean getApplicationVersionOAuthTokens(String organizationId, String applicationId, String version, String offset) {
        OAuth2TokenPaginationBean rval = new OAuth2TokenPaginationBean();
        Set<OAuth2Token> tmpResults = new HashSet<>();
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        try {
            Map<String, Set<String>> credentialIds = new HashMap<>();
            Map<String, String> offsets = StringUtils.isEmpty(offset) ? new HashMap<>() : GatewayPaginationUtil.decodeOffsets(offset);
            Map<String, String> nextOffsets = new HashMap<>();
            //create gatewayclients for every gateway the application is registered on
            Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(query.getApplicationContracts(organizationId, applicationId, version));
            //retrieve the oauth2 consumer credential ids so that we can retrieve the actual tokens
            gateways.values().forEach(gateway -> credentialIds.put(gateway.getGatewayId(), gateway.getConsumerOAuthCredentials(ConsumerConventionUtil.createAppUniqueId(avb)).getData().stream().map(KongPluginOAuthConsumerResponse::getId).collect(Collectors.toSet())));
            final Long[] totalResults = {0L};
            credentialIds.keySet().forEach(gwId ->
                    credentialIds.get(gwId)
                            .forEach(credId -> {
                                KongOAuthTokenList gwResult = null;
                                if (!offsets.isEmpty()) {
                                    if (offsets.containsKey(gwId))
                                        gwResult = gateways.get(gwId).getConsumerOAuthTokenList(credId, offsets.get(gwId));
                                } else {
                                    gwResult = gateways.get(gwId).getConsumerOAuthTokenList(credId, null);
                                }
                                if (gwResult != null) {
                                    totalResults[0] += gwResult.getTotal();
                                    if (!StringUtils.isEmpty(gwResult.getOffset())) {
                                        nextOffsets.put(gwId, gwResult.getOffset());
                                    }
                                    gwResult.getData().forEach(tkn ->
                                            tmpResults.add(new OAuth2Token(tkn, gwId, avb)));
                                }
                            }));
            rval.setTotal(totalResults[0]);

            if (!tmpResults.isEmpty()) {
                rval.setCurrentPage(offsets.isEmpty() ? null : offset);
                rval.setNextPage(nextOffsets.isEmpty() ? null : GatewayPaginationUtil.encodeOffsets(nextOffsets));
            }
            rval.setData(tmpResults);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
        return rval;
    }

    public void revokeApplicationVersionOAuthToken(OAuth2TokenRevokeBean token) {
        IGatewayLink gateway = gatewayFacade.createGatewayLink(token.getGatewayId());
        List<KongPluginOAuthConsumerResponse> appTokens = gateway.getConsumerOAuthCredentials(ConsumerConventionUtil.createAppUniqueId(token.getOrganizationId(), token.getApplicationId(), token.getVersion())).getData();
        if (appTokens.stream().filter(appToken -> appToken.getId().equals(token.getCredentialId())).collect(Collectors.toList()).isEmpty()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        gateway.revokeOAuthToken(token.getId());
    }

    public void cancelMembershipRequest(String orgId) {
        fireEvent(securityContext.getCurrentUser(), get(orgId).getId(), EventType.MEMBERSHIP_REQUEST_CANCELLED, null);
    }

    public void cancelContractRequest(String svcOrgId, String svcId, String svcVersion, String appOrgId, String appId, String appVersion) {
        fireEvent(ConsumerConventionUtil.createAppUniqueId(getAppVersion(appOrgId, appId, appVersion)),
                generateServiceUniqueName(getServiceVersion(svcOrgId, svcId, svcVersion)),
                EventType.CONTRACT_REQUEST_CANCELLED,
                null);
    }

    private void fireEvent(String origin, String destination, EventType type, String body) {
        NewEventBean neb = new NewEventBean()
                .withOriginId(origin)
                .withDestinationId(destination)
                .withType(type)
                .withBody(body);
        event.fire(neb);
    }

    public void deleteContractsForSummaries(List<ContractSummaryBean> contractBeans) {
        for (ContractSummaryBean contractSumBean : contractBeans) {
            ContractBean contract = null;
            try {
                contract = storage.getContract(contractSumBean.getContractId());
                if (contract.getService().getService().isAdmin()) {
                    ManagedApplicationBean mab = query.resolveManagedApplicationByAPIKey(contract.getApplication().getApikey());
                    mab.getApiKeys().remove(contract.getApplication().getApikey());
                    storage.updateManagedApplication(mab);
                }
                storage.createAuditEntry(AuditUtils.contractBrokenFromApp(contract, securityContext));
                storage.createAuditEntry(AuditUtils.contractBrokenToService(contract, securityContext));
                storage.deleteContract(contract);
                log.debug(String.format("Deleted contract: %s", contract));
            } catch (StorageException e) {
                throw new SystemErrorException(e);
            }
        }
    }

    public ApplicationVersionBean getApplicationVersionByUniqueId(String UID) {
        String[] split = splitUID(UID);
        return split == null ? null : getAppVersion(split[0], split[1], split[2]);
    }

    public ServiceVersionBean getServiceVersionByUniqueId(String UID) {
        String[] split = splitUID(UID);
        return split == null ? null : getServiceVersion(split[0], split[1], split[2]);
    }

    public JWT getApplicationJWT(ServiceAccountTokenRequest request) {
        try {
            log.info("Non-managed app context:{}", appContext.getNonManagedApplication());
            if (StringUtils.isNotEmpty(appContext.getNonManagedApplication())) {
                ApplicationVersionBean avb = getApplicationVersionByUniqueId(appContext.getNonManagedApplication());
                JWT rval = new JWT();
                GatewayBean gatewayBean = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId());
                IGatewayLink gateway = gatewayFacade.getDefaultGatewayLink();
                JwtClaims claims = new JwtClaims();
                claims.setClaim(IJWT.SERVICE_ACCOUNT, true);
                if (StringUtils.isNotEmpty(avb.getApplication().getEmail())) {
                    claims.setClaim(IJWT.EMAIL, avb.getApplication().getEmail());
                }
                //If the managad application has admin rights, allow the impersonation of users
                if (query.getManagedAppPrefixesForTypes(Arrays.asList(ManagedApplicationTypes.Admin)).contains(appContext.getApplicationPrefix()) && request != null && StringUtils.isNotBlank(request.getImpersonateUser())) {
                    claims.setStringClaim(IJWT.IMPERSONATE_USER, request.getImpersonateUser());
                    try {
                        UserBean user = userFacade.get(request.getImpersonateUser());
                    } catch (UserNotFoundException ex) {
                        log.info("User to impersonate not found, creating now: {}", request.getImpersonateUser());
                        userFacade.initNewUser(new NewUserBean().withAdmin(false).withUsername((request.getImpersonateUser())));
                    }
                }
                claims.setSubject(appContext.getNonManagedApplication());
                claims.setIssuer(gateway.getConsumerJWT(appContext.getNonManagedApplication()).getData().stream().filter(cred -> cred.getRsaPublicKey().equals(gatewayBean.getJWTPubKey())).map(KongPluginJWTResponse::getKey).collect(CustomCollectors.getFirstResult()));
                rval.setToken(JWTUtils.getJwtWithExpirationTime(claims, gatewayBean.getJWTExpTime(), KeyUtils.getPrivateKey(gatewayBean.getJWTPrivKey()), gatewayBean.getEndpoint() + gatewayBean.getJWTPubKeyEndpoint(), JWTUtils.JWT_RS256));
                return rval;
            } else {
                throw ExceptionFactory.actionException(Messages.i18n.format("ResolveApiKeyError"));
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public JWT getDomainCorrelatedToken(ServiceVersionSummaryBean svcSummary, String domain) {
        try {
            JWT returnValue = new JWT();
            ServiceVersionBean svc = getServiceVersionInternal(svcSummary.getOrganizationId(), svcSummary.getId(), svcSummary.getVersion());
            // This is the application version making the request
            ApplicationVersionBean requestingApp = getApplicationVersionByUniqueId(appContext.getNonManagedApplication());
            // This is the application version that correlates to the domain for which a token is being requested
            ApplicationVersionBean correlatedDomainAppVersion = query.getApplicationVersionForDomain(domain);
            if (correlatedDomainAppVersion == null) {
                throw ExceptionFactory.applicationVersionNotFoundException(Messages.i18n.format(ErrorCodes.MSG_DOMAIN_NOT_FOUND, domain));
            }
            // Both the requesting application version and the application version that correlates to the requested domain must have a
            // contract with the service version specified in the request.
            Set<ApplicationVersionBean> serviceContractHolders = query.getAppVersionContractHoldersForServiceVersion(svc);
            if (!serviceContractHolders.contains(requestingApp) || !serviceContractHolders.contains(correlatedDomainAppVersion)) {
                throw ExceptionFactory.notAuthorizedException(Messages.i18n.format(ErrorCodes.MSG_DOMAIN_CORRELATION_ERROR, domain));
            }
            // If both conditions are met then, and only then, issue a token for the correlated application version
            String correlatedDomainAppVersionId = ConsumerConventionUtil.createAppUniqueId(correlatedDomainAppVersion);
            JwtClaims claims = new JwtClaims();
            claims.setClaim(IJWT.SERVICE_ACCOUNT, true);
            claims.setSubject(correlatedDomainAppVersionId);
            claims.setIssuer(correlatedDomainAppVersionId);
            if (StringUtils.isNotEmpty(correlatedDomainAppVersion.getApplication().getEmail())) {
                claims.setClaim(IJWT.EMAIL, correlatedDomainAppVersion.getApplication().getEmail());
            }
            GatewayBean gatewayBean = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId());
            returnValue.setToken(JWTUtils.getJwtWithExpirationTime(claims, gatewayBean.getJWTExpTime(), KeyUtils.getPrivateKey(gatewayBean.getJWTPrivKey()), gatewayBean.getEndpoint() + gatewayBean.getJWTPubKeyEndpoint(), JWTUtils.JWT_RS256));
            return returnValue;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private String[] splitUID(String UID) {
        String[] split = UID.split("\\.");
        if (split.length != 3) {
            return null;
        } else {
            return split;
        }
    }

    private PagingBean getPagingBean(int page, int pageSize) {
        int pg = page;
        int pgSize = pageSize;
        if (pg <= 1) {
            pg = 1;
        }
        if (pgSize == 0) {
            pgSize = 20;
        }
        PagingBean paging = new PagingBean();
        paging.setPage(pg);
        paging.setPageSize(pgSize);
        return paging;
    }

    public void toggleRateLimitForApplicationVersion(String serviceOrganizationId, String serviceId, String serviceVersion, RateLimitToggleRequest request) {
        ServiceVersionBean svb = getServiceVersion(serviceOrganizationId, serviceId, serviceVersion);
        ApplicationVersionBean avb = getAppVersion(request.getOrganizationId(), request.getApplicationId(), request.getApplicationVersion());
        String apiId = ServiceConventionUtil.generateServiceUniqueName(svb);
        String consumerId = ConsumerConventionUtil.createAppUniqueId(avb);
        try {
            PolicyDefinitionBean polDef = storage.getPolicyDefinition(Policies.RATELIMITING.getPolicyDefId());
            PolicyBean rateLimitPolicy = query.getContractPolicyForServiceVersionByDefinition(svb, avb, polDef);
            if (rateLimitPolicy != null) {
                svb.getGateways().stream().map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId())).forEach(gw -> {
                    Boolean enabled = gw.togglePlugin(rateLimitPolicy.getKongPluginId());
                    if (enabled == null) {
                        log.warn("Plugin was not found on gateway, created now: {}", rateLimitPolicy.getKongPluginId());
                        KongConsumer consumer = gw.getConsumer(consumerId);
                        KongApi api = gw.getApi(apiId);
                        if (consumer != null) {
                            if (api != null) {
                                KongPluginConfig newPlugin = new KongPluginConfig()
                                        .withConsumerId(consumer.getId())
                                        .withId(rateLimitPolicy.getKongPluginId())
                                        .withName(Policies.RATELIMITING.getKongIdentifier())
                                        .withApiId(api.getId())
                                        .withEnabled(!rateLimitPolicy.isEnabled())
                                        .withConfig(new Gson().fromJson(rateLimitPolicy.getConfiguration(), Policies.RATELIMITING.getClazz()));
                                gw.createApiPlugin(apiId, newPlugin);
                            } else {
                                log.error("API missing on gateway: {}", apiId);
                            }
                        } else {
                            log.error("Consumer missing on gateway: {}", consumerId);
                        }
                    }
                });
                rateLimitPolicy.setEnabled(!rateLimitPolicy.isEnabled());
                storage.updatePolicy(rateLimitPolicy);
            }
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public ApplicationVersionBean updateAppVersionDomains(String orgId, String appId, String version, UpdateApplicationVersionDomainsBean request) {
        try {
            ApplicationVersionBean avb = getAppVersion(orgId, appId, version);
            Set<String> previousValues = avb.getDomains();
            for (String domain : request.getDomains()) {
                ApplicationVersionBean existingAssociation = query.getApplicationVersionForDomain(domain);
                if (existingAssociation != null && !existingAssociation.equals(avb)) {
                    throw ExceptionFactory.domainAlreadyAssociated(domain, existingAssociation.getApplication().getName(), existingAssociation.getVersion());
                }
            }
            avb.setDomains(request.getDomains());
            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("Updated application version domains", String.valueOf(previousValues), String.valueOf(request.getDomains()));
            storage.updateApplicationVersion(avb);
            storage.createAuditEntry(AuditUtils.applicationVersionUpdated(avb, data, securityContext));
            return avb;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }

    }
}
