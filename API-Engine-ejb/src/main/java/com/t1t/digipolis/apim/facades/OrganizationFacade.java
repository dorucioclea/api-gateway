package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.announcements.NewAnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.audit.AuditEntryType;
import com.t1t.digipolis.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.digipolis.apim.beans.audit.data.MembershipData;
import com.t1t.digipolis.apim.beans.audit.data.OwnershipTransferData;
import com.t1t.digipolis.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.digipolis.apim.beans.categories.ServiceTagsBean;
import com.t1t.digipolis.apim.beans.categories.TagBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractRequestBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.events.EventType;
import com.t1t.digipolis.apim.beans.events.NewEventBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.mail.ContractMailBean;
import com.t1t.digipolis.apim.beans.mail.MembershipAction;
import com.t1t.digipolis.apim.beans.mail.MembershipRequestMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateMemberMailBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.digipolis.apim.beans.members.MemberBean;
import com.t1t.digipolis.apim.beans.members.MemberRoleBean;
import com.t1t.digipolis.apim.beans.metrics.AppUsagePerServiceBean;
import com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType;
import com.t1t.digipolis.apim.beans.metrics.ServiceMarketInfo;
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
import com.t1t.digipolis.apim.beans.support.*;
import com.t1t.digipolis.apim.beans.visibility.VisibilityBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.facades.audit.AuditUtils;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.*;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.rest.GatewayValidation;
import com.t1t.digipolis.apim.kong.KongConstants;
import com.t1t.digipolis.apim.mail.MailService;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.util.*;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.DefinitionException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 15/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrganizationFacade {//extends AbstractFacade<OrganizationBean>
    private static Logger log = LoggerFactory.getLogger(OrganizationFacade.class.getName());
    @PersistenceContext
    private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext appContext;
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
    private IMetricsAccessor metrics;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    private UserFacade userFacade;
    @Inject
    private RoleFacade roleFacade;
    @Inject
    private AppConfig config;
    @Inject
    private MailService mailService;
    @Inject
    private Event<NewEventBean> event;
    @Inject
    private Event<AnnouncementBean> announcement;
    @Inject
    private GatewayValidation gatewayValidation;

    public final static String MARKET_SEPARATOR = "-";

    @SuppressWarnings("nls")
    public static final String[] DATE_FORMATS = {
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
    public static final String PLACEHOLDER_CALLBACK_URI = "http://localhost/";

    private static final long ONE_MINUTE_MILLIS = 1 * 60 * 1000;
    private static final long ONE_HOUR_MILLIS = 1 * 60 * 60 * 1000;
    private static final long ONE_DAY_MILLIS = 1 * 24 * 60 * 60 * 1000;
    private static final long ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000;
    private static final long ONE_MONTH_MILLIS = 30 * 24 * 60 * 60 * 1000;

    //craete organization
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

        if ("true".equals(System.getProperty("apim.manager.require-auto-granted-org", "true"))) {
            if (autoGrantedRoles.isEmpty()) {
                throw new SystemErrorException(Messages.i18n.format("OrganizationResourceImpl.NoAutoGrantRoleAvailable"));
            }
        }
        //determine org id
        String orgUniqueId = bean.getName();
        orgUniqueId = orgUniqueId.replaceAll(MARKET_SEPARATOR, "");
        orgUniqueId = BeanUtils.idFromName(orgUniqueId);

        //verify if organization is created in marketplace (aka not publisher or consent type)
        ManagedApplicationBean managedApp = query.findManagedApplication(appContext.getApplicationPrefix());
        if (managedApp != null && (
                        managedApp.getType().equals(ManagedApplicationTypes.InternalMarketplace) ||
                        managedApp.getType().equals(ManagedApplicationTypes.ExternalMarketplace)
        )) {
            //the request comes from a marketplace => prefix the org
            orgUniqueId = managedApp.getPrefix() + MARKET_SEPARATOR + orgUniqueId;
        }

        OrganizationBean orgBean = new OrganizationBean();
        orgBean.setName(bean.getName());
        orgBean.setContext(appContext.getApplicationPrefix());
        orgBean.setDescription(bean.getDescription());
        orgBean.setId(orgUniqueId);
        orgBean.setCreatedOn(new Date());
        orgBean.setCreatedBy(securityContext.getCurrentUser());
        orgBean.setModifiedOn(new Date());
        orgBean.setModifiedBy(securityContext.getCurrentUser());
        orgBean.setOrganizationPrivate(bean.getOrganizationPrivate() == null ? true : bean.getOrganizationPrivate());
        if (bean.getFriendlyName() != null) {
            if (!securityContext.isAdmin()) {
                throw ExceptionFactory.notAuthorizedException();
            }
            orgBean.setFriendlyName(bean.getFriendlyName());
        }
        try {
            // Store/persist the new organization
            if (storage.getOrganization(orgBean.getId()) != null) {
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
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public ApplicationBean createApp(String organizationId, NewApplicationBean bean) {
        if (!securityContext.hasPermission(PermissionType.appEdit, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        ApplicationBean newApp = new ApplicationBean();
        newApp.setId(BeanUtils.idFromName(bean.getName()));
        newApp.setName(bean.getName());
        newApp.setBase64logo(bean.getBase64logo());
        newApp.setDescription(bean.getDescription());
        newApp.setCreatedBy(securityContext.getCurrentUser());
        newApp.setCreatedOn(new Date());
        newApp.setContext(appContext.getApplicationIdentifier().getPrefix());
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
                    }
                }
                List<PolicySummaryBean> policies = listAppPolicies(organizationId, applicationId, bean.getCloneVersion());
                for (PolicySummaryBean policySummary : policies) {
                    PolicyBean policy = getAppPolicy(organizationId, applicationId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration())).getPolicyJsonConfig());
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
            Set<String> previousURIs = avb.getOauthClientRedirects();
            avb.setOauthClientRedirects(uri.getUris());
            //register application credentials for OAuth2
            //create OAuth2 application credentials on the application consumer - should only been done once for this application
            if (avb != null && !StringUtils.isEmpty(avb.getoAuthClientId())) {
                String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
                //String uniqueUserId = securityContext.getCurrentUser();
                KongPluginOAuthConsumerRequest OAuthRequest = new KongPluginOAuthConsumerRequest()
                        .withClientId(avb.getoAuthClientId())
                        .withClientSecret(avb.getOauthClientSecret())
                        .withName(avb.getApplication().getName())
                        .withRedirectUri(avb.getOauthClientRedirects());
                if (avb.getStatus() == ApplicationStatus.Registered) {
                    List<ContractSummaryBean> csb = query.getApplicationContracts(organizationId, applicationId, version);
                    Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(csb);
                    for (IGatewayLink gateway : gateways.values()) {
                        gateway.updateConsumerOAuthCredentials(appConsumerName, avb.getoAuthClientId(), avb.getOauthClientSecret(), OAuthRequest);
                    }
                } else {
                    if (avb.getStatus() != ApplicationStatus.Retired) {
                        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                        gateway.updateConsumerOAuthCredentials(appConsumerName, avb.getoAuthClientId(), avb.getOauthClientSecret(), OAuthRequest);
                    } else {
                        throw ExceptionFactory.invalidApplicationStatusException();
                    }
                }
            }
            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("OAuth2 Callback URI", String.valueOf(previousURIs), String.valueOf(avb.getOauthClientRedirects()));
            storage.updateApplicationVersion(avb);
            storage.createAuditEntry(AuditUtils.applicationVersionUpdated(avb, data, securityContext));
            return avb;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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
            ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
            String appId = ConsumerConventionUtil.createAppUniqueId(avb);
            String svcId = ServiceConventionUtil.generateServiceUniqueName(svb);
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
            //Check if service allows auto contract creation
            if (!svb.getAutoAcceptContracts()) {

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

                NewEventBean newEvent = new NewEventBean()
                        .withOriginId(appId)
                        .withDestinationId(svcId)
                        .withType(EventType.CONTRACT_PENDING)
                        .withBody(new Gson().toJson(pvsb));
                event.fire(newEvent);
                //send notification to org owners for
                listMembers(organizationId).forEach(member -> {
                    member.getRoles().forEach(role -> {
                        if (role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase())) {//only owners
                            try {
                                if (member.getUserId() != null && !StringUtils.isEmpty(member.getEmail())) {
                                    ContractMailBean contractMailBean = new ContractMailBean();
                                    contractMailBean.setTo(member.getEmail());
                                    contractMailBean.setUserId(securityContext.getCurrentUser());
                                    contractMailBean.setUserMail(securityContext.getEmail());
                                    contractMailBean.setAppOrgName(avb.getApplication().getOrganization().getName());
                                    contractMailBean.setAppName(avb.getApplication().getName());
                                    contractMailBean.setAppVersion(avb.getVersion());
                                    contractMailBean.setServiceOrgName(svb.getService().getOrganization().getName());
                                    contractMailBean.setServiceName(svb.getService().getName());
                                    contractMailBean.setServiceVersion(svb.getVersion());
                                    contractMailBean.setPlanName(pvsb.getName());
                                    contractMailBean.setPlanVersion(pvsb.getVersion());
                                    mailService.sendContractRequest(contractMailBean);
                                }
                            } catch (Exception e) {
                                log.error("Error sending mail:{}", e.getMessage());
                            }
                        }
                    });
                });
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
        ServiceVersionBean svb = getServiceVersion(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion());
        NewEventBean newEvent = new NewEventBean()
                .withOriginId(ServiceConventionUtil.generateServiceUniqueName(svb))
                .withDestinationId(ConsumerConventionUtil.createAppUniqueId(avb))
                .withType(EventType.CONTRACT_REJECTED);
        event.fire(newEvent);
        //send notification to app org owners
        listMembers(organizationId).forEach(member -> {
            member.getRoles().forEach(role -> {
                if (role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase())) {//only owners
                    try {
                        if (member.getUserId() != null && !StringUtils.isEmpty(member.getEmail())) {
                            ContractMailBean contractMailBean = new ContractMailBean();
                            contractMailBean.setTo(member.getEmail());
                            contractMailBean.setUserId(securityContext.getCurrentUser());
                            contractMailBean.setUserMail(securityContext.getEmail());
                            contractMailBean.setAppOrgName(avb.getApplication().getOrganization().getName());
                            contractMailBean.setAppName(avb.getApplication().getName());
                            contractMailBean.setAppVersion(avb.getVersion());
                            contractMailBean.setServiceOrgName(svb.getService().getOrganization().getName());
                            contractMailBean.setServiceName(svb.getService().getName());
                            contractMailBean.setServiceVersion(svb.getVersion());
                            contractMailBean.setPlanName(bean.getPlanId());
                            mailService.rejectContractRequest(contractMailBean);
                        }
                    } catch (Exception e) {
                        log.error("Error sending mail:{}", e.getMessage());
                    }
                }
            });
        });
    }

    public ContractBean acceptContractRequest(String organizationId, String applicationId, String version, NewContractBean bean) {
        //Validate service and app version, and verify if request actually occurred
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, version);
        ServiceVersionBean svb = getServiceVersion(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion());
        ContractBean contract = createContract(organizationId, applicationId, version, bean);
        NewEventBean newEvent = new NewEventBean()
                .withOriginId(ServiceConventionUtil.generateServiceUniqueName(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion()))
                .withDestinationId(ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version))
                .withType(EventType.CONTRACT_ACCEPTED);
        event.fire(newEvent);
        //send notification to app org owners
        listMembers(organizationId).forEach(member -> {
            member.getRoles().forEach(role -> {
                if (role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase())) {//only owners
                    try {
                        if (member.getUserId() != null && !StringUtils.isEmpty(member.getEmail())) {
                            ContractMailBean contractMailBean = new ContractMailBean();
                            contractMailBean.setTo(member.getEmail());
                            contractMailBean.setUserId(securityContext.getCurrentUser());
                            contractMailBean.setUserMail(securityContext.getEmail());
                            contractMailBean.setAppOrgName(avb.getApplication().getOrganization().getName());
                            contractMailBean.setAppName(avb.getApplication().getName());
                            contractMailBean.setAppVersion(avb.getVersion());
                            contractMailBean.setServiceOrgName(svb.getService().getOrganization().getName());
                            contractMailBean.setServiceName(svb.getService().getName());
                            contractMailBean.setServiceVersion(svb.getVersion());
                            contractMailBean.setPlanName(bean.getPlanId());
                            mailService.approveContractRequest(contractMailBean);
                        }
                    } catch (Exception e) {
                        log.error("Error sending mail:{}", e.getMessage());
                    }
                }
            });
        });
        return contract;
    }

    public ContractBean createContract(String organizationId, String applicationId, String version, NewContractBean bean) {
        try {
            //add OAuth2 consumer default to the application
            ContractBean contract = createContractInternal(organizationId, applicationId, version, bean);
            log.debug(String.format("Created new contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$
            //for contract add keyauth to application consumer
            String serviceOrgId = contract.getService().getService().getOrganization().getId();
            String serviceId = contract.getService().getService().getId();
            String svcVersion = contract.getService().getVersion();
            if (contract.getApplication().getStatus() == ApplicationStatus.Registered) {
                Application app = getApplicationForNewContractRegistration(contract);
                Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(query.getApplicationContracts(organizationId, applicationId, version));
                for (IGatewayLink gateway : gateways.values()) {
                    enableContractonGateway(contract, gateway);
                    //persist the new contract policies
                    Map<Contract, KongPluginConfigList> response = gateway.registerApplication(app);
                    for (Map.Entry<Contract, KongPluginConfigList> entry : response.entrySet()) {
                        for (KongPluginConfig config : entry.getValue().getData()) {
                            NewPolicyBean npb = new NewPolicyBean();
                            npb.setGatewayId(gateway.getGatewayId());
                            npb.setConfiguration(new Gson().toJson(config.getConfig()));
                            npb.setContractId(contract.getId());
                            npb.setKongPluginId(config.getId());
                            npb.setDefinitionId(GatewayUtils.convertKongPluginNameToPolicy(config.getName()).getPolicyDefId());
                            doCreatePolicy(organizationId, applicationId, version, npb, PolicyType.Contract);
                        }
                    }
                }
            } else {
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                enableContractonGateway(contract, gateway);
            }
            //verify if the contracting service has OAuth enabled
            List<PolicySummaryBean> policySummaryBeans = listServicePolicies(serviceOrgId, serviceId, svcVersion);
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
            }
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
            try {
                gateway.addConsumerKeyAuth(appConsumerName, contract.getApikey());
            } catch (Exception e) {
                //apikey for consumer already exists, but let's log the exception for debugging purposes
                log.debug("Consumer Key-Auth Exception:{}", e);
            }
            //Add ACL group membership by default on gateway
            KongPluginACLResponse response = gateway.addConsumerToACL(appConsumerName,
                    ServiceConventionUtil.generateServiceUniqueName(contract.getService()));
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
            contractPolicies.add(new Policy(policy.getDefinition().getId(), policy.getConfiguration()));
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
                } catch (Exception e) {
                    log.debug("Error enabling application for oauth:{}", e.getStackTrace());
                    ;//don't do anything
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
            for (ContractSummaryBean contractSumBean : summaries) {
                ContractBean contract = null;
                try {
                    contract = storage.getContract(contractSumBean.getContractId());
                    storage.createAuditEntry(AuditUtils.contractBrokenFromApp(contract, securityContext));
                    storage.createAuditEntry(AuditUtils.contractBrokenToService(contract, securityContext));
                    storage.deleteContract(contract);
                    log.debug(String.format("Deleted contract: %s", contract));
                } catch (StorageException e) {
                    throw new SystemErrorException(e);
                }
            }
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
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ApplicationSummaryBean> listApps(String organizationId) {
        get(organizationId);
        try {
            return query.getApplicationsInOrg(organizationId);
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
                auditData.addChange("logo", appForUpdate.getBase64logo(), bean.getDescription());
                appForUpdate.setBase64logo(bean.getBase64logo());
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

    public PolicyBean getPlanPolicy(String organizationId, String planId, String version, long policyId) {
        boolean hasPermission = securityContext.hasPermission(PermissionType.planView, organizationId);

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

    public PolicyBean createServicePolicy(String organizationId, String serviceId, String version, NewPolicyBean bean) {
        // Make sure the service exists
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        if (svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Retired || svb.getStatus() == ServiceStatus.Deprecated) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        //validate no other policy of the same type has been added for this service - only on policy of the same type is allowed
        List<PolicySummaryBean> policies = listServicePolicies(organizationId, serviceId, version);
        for (PolicySummaryBean polsum : policies) {
            if (polsum.getPolicyDefinitionId().equals(bean.getDefinitionId()))
                throw new PolicyDefinitionAlreadyExistsException("The policy already exists for the service: " + bean.getDefinitionId());
        }
        log.debug(String.format("Created service policy %s", svb)); //$NON-NLS-1$
        return doCreatePolicy(organizationId, serviceId, version, bean, PolicyType.Service);
    }

    public ServiceVersionBean getServiceVersion(String organizationId, String serviceId, String version) {
        try {
            ServiceVersionBean serviceVersion = storage.getServiceVersion(organizationId, serviceId, version);

            if (serviceVersion == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            decryptEndpointProperties(serviceVersion);
            return filterServiceVersionByAppPrefix(serviceVersion);
        } catch (AbstractRestException e) {
            throw e;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public PolicyBean getServicePolicy(String organizationId, String serviceId, String version, long policyId) {
        // Make sure the service exists
        getServiceVersion(organizationId, serviceId, version);
        return doGetPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
    }

    public ServiceVersionBean updateServiceVersion(String organizationId, String serviceId, String version, UpdateServiceVersionBean bean) throws StorageException {
        ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
        EntityUpdatedData data = new EntityUpdatedData();
        if (svb.getStatus() != ServiceStatus.Retired) {
            if (AuditUtils.valueChanged(svb.getEndpoint(), bean.getEndpoint())) {
                data.addChange("endpoint", svb.getEndpoint(), bean.getEndpoint()); //$NON-NLS-1$
                svb.setEndpoint(bean.getEndpoint());
                //If the service is already published, update the upstream URL's on the gateways the service is published on
                updateServiceVersionEndpoint(svb);
                log.debug("BEAN ENDPOINT UPDATED");
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
                //add implicitly the IP Restriction when: External available and hide = false
                //Legacy - we added implicitly an IPRestriction policy, we remove this because OR it should be on a separate gateway, or the load balancer should deal with it.
/*                    KongPluginIPRestriction defaultIPRestriction = PolicyUtil.createDefaultIPRestriction(IPRestrictionFlavor.WHITELIST, query.listWhitelistRecords());
                    boolean enableIPR = ServiceImplicitPolicies.verifyIfIPRestrictionShouldBeSet(svb);
                    if (defaultIPRestriction != null && enableIPR) {
                        Gson gson = new Gson();
                        NewPolicyBean npb = new NewPolicyBean();
                        npb.setDefinitionId("IPRestriction");
                        npb.setConfiguration(gson.toJson(defaultIPRestriction));
                        try {
                            createServicePolicy(organizationId, serviceId, version, npb);
                        } catch (PolicyDefinitionAlreadyExistsException pdex) {
                            ;
                        }//ignore if policy already exists
                    } else {
                        //remove eventual policies already added
                        List<PolicySummaryBean> policies = listServicePolicies(organizationId, serviceId, version);
                        for (PolicySummaryBean psb : policies) {
                            psb.getPolicyDefinitionId().equalsIgnoreCase("IPRestriction");
                            deleteServicePolicy(organizationId, serviceId, version, psb.getId());
                        }
                    }*/
                log.debug("BEAN VISIBILITY UPDATED");
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
                        if (gateway != null) {
                            if (svb.getGateways() == null) {
                                svb.setGateways(new HashSet<ServiceGatewayBean>());
                                ServiceGatewayBean sgb = new ServiceGatewayBean();
                                sgb.setGatewayId(gateway.getId());
                                svb.getGateways().add(sgb);
                            }
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
        newService.setBasepath(bean.getBasepath());
        newService.setCategories(bean.getCategories());
        newService.setBase64logo(bean.getBase64logo());
        newService.setCreatedOn(new Date());
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
            if (query.getServiceByBasepath(organizationId, bean.getBasepath()) != null) {
                throw ExceptionFactory.serviceBasepathAlreadyInUseException(orgBean.getName(), bean.getBasepath().substring(1));
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
            return newService;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
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
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        if (bean.isClone() && bean.getCloneVersion() != null) {
            try {
                ServiceVersionBean cloneSource = getServiceVersion(organizationId, serviceId, bean.getCloneVersion());

                // Clone primary attributes of the service version
                UpdateServiceVersionBean updatedService = new UpdateServiceVersionBean();
                updatedService.setReadme(cloneSource.getReadme());
                updatedService.setTermsAgreementRequired(cloneSource.getTermsAgreementRequired());
                updatedService.setEndpoint(cloneSource.getEndpoint());
                updatedService.setEndpointType(cloneSource.getEndpointType());
                updatedService.setEndpointProperties(cloneSource.getEndpointProperties());
                updatedService.setGateways(cloneSource.getGateways());
                updatedService.setOnlinedoc(cloneSource.getOnlinedoc());
                updatedService.setPublicService(cloneSource.isPublicService());
                updatedService.setAutoAcceptContracts(cloneSource.getAutoAcceptContracts());
                updatedService.setPlans(cloneSource.getPlans());
                updatedService.setVisibility(cloneSource.getVisibility());

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
                    PolicyBean policy = getServicePolicy(organizationId, serviceId, bean.getCloneVersion(), policySummary.getId());
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(policy.getDefinition().getId());
                    npb.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration()), ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, bean.getCloneVersion())).getPolicyJsonConfig());
                    createServicePolicy(organizationId, serviceId, newVersion.getVersion(), npb);
                }
                newVersion = updateServiceVersion(organizationId, serviceId, bean.getVersion(), updatedService);
            } catch (Exception e) {
                // TODO it's ok if the clone fails - we did our best
                // TODO We could try a little harder
                //throw new SystemErrorException(e);
                if (e != null) {
                    Throwable t = e;
                    e = (Exception) t;
                }
            }
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

    private void updateServiceVersionEndpoint(ServiceVersionBean svb) {
        if (svb.getStatus() == ServiceStatus.Retired) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        if (svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Deprecated) {
            svb.getGateways().forEach(svcGateway -> {
                IGatewayLink gateway = createGatewayLink(svcGateway.getGatewayId());
                gateway.updateApiUpstreamURL(svb.getService().getOrganization().getId(), svb.getService().getId(), svb.getVersion(), svb.getEndpoint());
            });
        }
    }

    public List<PolicySummaryBean> listServicePolicies(String organizationId, String serviceId, String version) {
        // Try to get the service first - will throw an exception if not found.
        getServiceVersion(organizationId, serviceId, version);
        try {
            return query.getPolicies(organizationId, serviceId, version, PolicyType.Service);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<AuditEntryBean> getAppVersionActivity(String organizationId, String applicationId, String version, int page, int pageSize) {
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
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public AppUsagePerServiceBean getAppUsagePerService(String organizationId, String applicationId, String version, HistogramIntervalType interval, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        AppUsagePerServiceBean appUsage = new AppUsagePerServiceBean();
        Map<String, MetricsConsumerUsageList> data = new HashMap<>();
        List<ContractSummaryBean> appContracts = null;
        //get App contracts
        try {
            appContracts = query.getApplicationContracts(organizationId, applicationId, version);
            //getid from kong
            IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            KongConsumer consumer = gateway.getConsumer(ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version));
            log.info("Getting AppUsageStats for consumer {}", consumer);
            if (consumer != null && !StringUtils.isEmpty(consumer.getCustomId())) {
                String consumerId = consumer.getId();
                for (ContractSummaryBean app : appContracts) {
                    MetricsConsumerUsageList usageList = metrics.getAppUsageForService(app.getServiceOrganizationId(), app.getServiceId(), app.getServiceVersion(), interval, from, to, consumerId);
                    if (usageList != null) {
                        data.put(ServiceConventionUtil.generateServiceUniqueName(app.getServiceOrganizationId(), app.getServiceId(), app.getServiceVersion()), usageList);
                    } else {
                        throw ExceptionFactory.metricsUnavailableException();
                    }

                }
            }
        } catch (StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
        appUsage.setData(data);
        return appUsage;
    }

    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        if (interval == null) {
            interval = HistogramIntervalType.day;
        }
        validateMetricRange(from, to);
        validateTimeSeriesMetric(from, to, interval);
        MetricsUsageList usageList = metrics.getUsage(organizationId, serviceId, version, interval, from, to);
        if (usageList != null) {
            return usageList;
        } else {
            throw ExceptionFactory.metricsUnavailableException();
        }

    }

    public ServiceMarketInfo getMarketInfo(String organizationId, String serviceId, String version) {
        ServiceMarketInfo marketInfo = metrics.getServiceMarketInfo(organizationId, serviceId, version);
        if (marketInfo != null) {
            return marketInfo;
        } else {
            throw ExceptionFactory.metricsUnavailableException();
        }

    }

    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        if (interval == null) {
            interval = HistogramIntervalType.day;
        }
        validateMetricRange(from, to);
        validateTimeSeriesMetric(from, to, interval);
        MetricsResponseStatsList statsList = metrics.getResponseStats(organizationId, serviceId, version, interval, from, to);
        if (statsList != null) {
            return statsList;
        } else {
            throw ExceptionFactory.metricsUnavailableException();
        }

    }

    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        MetricsResponseSummaryList summList = metrics.getResponseStatsSummary(organizationId, serviceId, version, from, to);
        if (summList != null) {
            return summList;
        } else {
            throw ExceptionFactory.metricsUnavailableException();
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
            Map<String, IGatewayLink> gateways = getApplicationContractGatewayLinks(Collections.singletonList(contract));
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

            //get all application contracts in order to verify if other contracts are present
            List<ContractSummaryBean> contractBeans = null;
            try {
                contractBeans = query.getApplicationContracts(organizationId, applicationId, version);
            } catch (StorageException e) {
                throw ExceptionFactory.actionException(Messages.i18n.format("ApplicationNotFound"), e); //$NON-NLS-1$
            }

            try {
                //We delete only the key-auth when no other contracts with the application - pending contracts must not be taken into consideration
                if (contractBeans.size() == 1) {
                    String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
                    //this can only be done when no other contracts exist
                    if (avb.getStatus() == ApplicationStatus.Registered) {
                        for (IGatewayLink gateway : gateways.values()) {
                            gateway.deleteConsumerKeyAuth(appConsumerName, contract.getApikey());
                        }
                    }
                    else {
                        gateways.get(gatewayFacade.getDefaultGateway().getId()).deleteConsumerKeyAuth(appConsumerName, contract.getApikey());
                    }
                }
            } catch (StorageException e) {
                throw new ApplicationNotFoundException(e.getMessage());
            }

            //Revoke application's contract plugins
            try {
                if (avb.getStatus() == ApplicationStatus.Registered) {
                    for (IGatewayLink gateway : gateways.values()) {
                        List<PolicyBean> policies = query.getApplicationVersionContractPolicies(organizationId, applicationId, version, contractId);
                        for (PolicyBean policy : policies) {
                            if (policy.getGatewayId().equals(gateway.getGatewayId())) {
                                deleteContractPolicy(contract, policy, gateway);
                            }
                        }
                    }
                }
                else {
                    IGatewayLink gateway = gateways.get(gatewayFacade.getDefaultGateway().getId());
                    PolicyBean policy = query.getApplicationACLPolicy(organizationId, applicationId, version, contractId, gateway.getGatewayId());
                    deleteContractPolicy(contract, policy, gateway);
                }
            } catch (StorageException ex) {
                throw new SystemErrorException(ex);
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
            //verify if application still needs OAuth credentials?
            int oauthEnabledServices = 0;
            List<ContractSummaryBean> contracts = query.getApplicationContracts(organizationId, applicationId, version);
            if (contracts != null && contracts.size() > 0) {
                //verify if other contracts still need OAuth properties
                for (ContractSummaryBean ctr : contracts) {
                    List<PolicySummaryBean> policySummaryBeans = listServicePolicies(ctr.getServiceOrganizationId(), ctr.getServiceId(), ctr.getServiceVersion());
                    for (PolicySummaryBean summaryBean : policySummaryBeans)
                        if (summaryBean.getPolicyDefinitionId().toLowerCase().equals(Policies.OAUTH2.getKongIdentifier()))
                            oauthEnabledServices++;
                }
            } else if (contracts != null && contracts.size() == 0) {
                //be sure no oauth data is present
                oauthEnabledServices = 0;
            }
            if (oauthEnabledServices == 0) {
                //remove OAuth credential for consumer
                try {
                    //We create the new application version consumer
                    IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                    //upon filling redirect URI the OAuth credential has been made, check if callback is there, otherwise 405 gateway exception.
                    if (contract != null && !(avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().isEmpty() || avb.getOauthClientRedirects().stream().filter(redirect -> !StringUtils.isEmpty(redirect)).collect(Collectors.toSet()).isEmpty())) {
                        String uniqueUserId = securityContext.getCurrentUser();
                        UserBean user = idmStorage.getUser(uniqueUserId);
                        KongPluginOAuthConsumerResponseList info = gateway.getApplicationOAuthInformation(avb.getoAuthClientId());
                        if (info.getData().size() > 0) {
                            gateway.deleteOAuthConsumerPlugin(user.getKongUsername(), ((KongPluginOAuthConsumerResponse) info.getData().get(0)).getId());
                        }
                    }
                } catch (StorageException e) {
                    throw new ApplicationNotFoundException(e.getMessage());
                }
                //clear application version OAuth information
                avb.setoAuthClientId("");
                avb.setOauthClientSecret("");
                avb.setOauthClientRedirects(Collections.EMPTY_SET);
                storage.updateApplicationVersion(avb);
            }
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
                gateway.deleteApiPlugin(ServiceConventionUtil.generateServiceUniqueName(c.getService()), p.getKongPluginId());
                deleted = true;
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
                policy.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration())).getPolicyJsonConfig());
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
            }
            else {
                deletePlanVersionInternal(pvb);
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deletePlanVersionInternal(PlanVersionBean pvb) throws StorageException {
        List<ContractBean> planVersionContracts = query.getPlanVersionContracts(pvb.getId());
        //for existing contract throw exception
        if (planVersionContracts != null && planVersionContracts.size() > 0) {
            throw ExceptionFactory.planCannotBeDeleted("Plan still has contracts linked");
        }
        storage.deletePlanVersion(pvb);
    }

    public void deleteService(String organizationId, String serviceId) {
        if (!securityContext.hasPermission(PermissionType.svcAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            // Get Service
            ServiceBean serviceBean = storage.getService(organizationId, serviceId);
            if (serviceBean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }
            if (query.getServiceContracts(serviceBean).size() > 0) {
                throw ExceptionFactory.serviceCannotDeleteException("Service still has contracts");
            }
            // Get Service versions
            List<ServiceVersionSummaryBean> svsbs = query.getServiceVersions(serviceBean.getOrganization().getId(), serviceBean.getId());
            for (ServiceVersionSummaryBean svsb : svsbs) {
                deleteServiceVersionInternal(getServiceVersion(svsb.getOrganizationId(), svsb.getId(), svsb.getVersion()));
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
                deleteService(organizationId, serviceId);
            }
            else {
                ServiceVersionBean svb = getServiceVersion(organizationId, serviceId, version);
                deleteServiceVersionInternal(svb);
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deleteServiceVersionInternal(ServiceVersionBean svb) throws StorageException {
        String organizationId = svb.getService().getOrganization().getId();
        String serviceId = svb.getService().getId();
        String version = svb.getVersion();
        //Check for existing contrasts, throw error if there still are any.
        if (query.getServiceContracts(organizationId, serviceId, version).size() > 0) {
            throw ExceptionFactory.serviceCannotDeleteException("Service version still has contracts");
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
        query.deleteAllEventsForEntity(ServiceConventionUtil.generateServiceUniqueName(svb));

        // Remove gateway config & plan configuration for service version
        svb.getGateways().clear();
        svb.getPlans().clear();
        storage.updateServiceVersion(svb);

        // Now we can finally delete the version itself
        storage.deleteServiceVersion(svb);
    }

    public SearchResultsBean<AuditEntryBean> getServiceActivity(String organizationId, String serviceId, int page, int pageSize) {
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
            storage.updateService(serviceForUpdate);
            storage.createAuditEntry(AuditUtils.serviceUpdated(serviceForUpdate, auditData, securityContext));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
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
            rval.setManagedEndpoint(gatewayEndpoint + GatewayPathUtilities.generateGatewayContextPath(organizationId, serviceVersion.getService().getBasepath(), version));
            //get oauth endpoints if needed
            if (!StringUtils.isEmpty(serviceVersion.getProvisionKey())) {
                //construct the target url
                StringBuilder targetURI = new StringBuilder("").append(URIUtils.uriBackslashRemover(gateway.getEndpoint()))
                        .append(URIUtils.uriBackslashAppender(GatewayPathUtilities.generateGatewayContextPath(organizationId, serviceVersion.getService().getBasepath(), version)))
                        .append(KongConstants.KONG_OAUTH_ENDPOINT + "/");
                rval.setOauth2AuthorizeEndpoint(targetURI.toString() + KongConstants.KONG_OAUTH2_ENDPOINT_AUTH);
                rval.setOauth2TokenEndpoint(targetURI.toString() + KongConstants.KONG_OAUTH2_ENDPOINT_TOKEN);
            } else {
                rval.setOauth2AuthorizeEndpoint("");
                rval.setOauth2TokenEndpoint("");
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
            final List<ManagedApplicationBean> availableMarkets = query.listAvailableMarkets();
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
            String serviceKongId = ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version);
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

    public KongPluginConfig changeEnabledStateServicePlugin(String organizationId, String serviceId, String version, String pluginId, boolean enable) {
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
    }

    public SearchResultsBean<AuditEntryBean> getServiceVersionActivity(String organizationId, String serviceId, String version, int page, int pageSize) {
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
        getServiceVersion(organizationId, serviceId, version);
        try {
            return query.getServiceVersionPlans(organizationId, serviceId, version);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updateServicePolicy(String organizationId, String serviceId, String version, long policyId, UpdatePolicyBean bean) {
        // Make sure the service exists
        ServiceStatus svs = getServiceVersion(organizationId, serviceId, version).getStatus();
        if (svs == ServiceStatus.Published || svs == ServiceStatus.Deprecated) {
            throw ExceptionFactory.invalidServiceStatusException();
        }
        try {
            PolicyBean policy = storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            // TODO capture specific change values when auditing policy updates
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                policy.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), bean.getConfiguration()), ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version)).getPolicyJsonConfig());
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

    public void deleteServicePolicy(String organizationId, String serviceId, String version, long policyId) {
        // Make sure the service exists
        ServiceVersionBean service = getServiceVersion(organizationId, serviceId, version);
        if (service.getStatus() == ServiceStatus.Published || service.getStatus() == ServiceStatus.Retired || service.getStatus() == ServiceStatus.Deprecated) {
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

    public PolicyChainBean getServicePolicyChain(String organizationId, String serviceId, String version, String planId) {
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

    public List<ContractSummaryBean> getServiceVersionContracts(String organizationId, String serviceId, String version, int page, int pageSize) {
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
                    npb.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration())).getPolicyJsonConfig());
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

                policy.setConfiguration(gatewayValidation.validate(new Policy(policy.getDefinition().getId(), bean.getConfiguration())).getPolicyJsonConfig());
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
        NewEventBean newEvent = new NewEventBean()
                .withOriginId(organizationId)
                .withDestinationId(bean.getUserId())
                .withType(EventType.MEMBERSHIP_GRANTED);
        event.fire(newEvent);
        //send email
        try {
            final RoleBean roleBean = roleFacade.get(bean.getRoleId());
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(bean.getUserId());
            if (userBean != null && !StringUtils.isEmpty(userBean.getEmail())) {
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.NEW_MEMBERSHIP);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole(roleBean.getName());
                mailService.sendUpdateMember(updateMemberMailBean);
            }
        } catch (Exception e) {
            log.error("Error sending mail:{}", e.getMessage());
        }
    }

    public void grant(String organizationId, GrantRolesBean bean) {
        // Verify that the references are valid.
        get(organizationId);
        userFacade.get(bean.getUserId());
        for (String roleId : bean.getRoleIds()) {
            roleFacade.get(roleId);
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
            storage.createAuditEntry(AuditUtils.membershipGrantedImplicit(organizationId, auditData, securityContext, true));
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
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
        //send email
        try {
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(userId);
            if (userBean != null && !StringUtils.isEmpty(userBean.getEmail())) {
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole("");
                mailService.sendUpdateMember(updateMemberMailBean);
            }
        } catch (Exception e) {
            log.error("Error sending mail:{}", e.getMessage());
        }
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
        //send email
        try {
            final RoleBean roleBean = roleFacade.get(bean.getRoleId());
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(userId);
            if (userBean != null && !StringUtils.isEmpty(userBean.getEmail())) {
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.UPDATE_ROLE);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole(roleBean.getName());
                mailService.sendUpdateMember(updateMemberMailBean);
            }
        } catch (Exception e) {
            log.error("Error sending mail:{}", e.getMessage());
        }
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
        //send email
        try {
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(userId);
            if (userBean != null && !StringUtils.isEmpty(userBean.getEmail())) {
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole("");
                mailService.sendUpdateMember(updateMemberMailBean);
            }
        } catch (Exception e) {
            log.error("Error sending mail:{}", e.getMessage());
        }
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
        //send email
        try {
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(bean.getNewOwnerId());
            if (userBean != null && !StringUtils.isEmpty(userBean.getEmail())) {
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.TRANSFER);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                mailService.sendUpdateMember(updateMemberMailBean);
            }
        } catch (Exception e) {
            log.error("Error sending mail:{}", e.getMessage());
        }
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
        storage.createApplicationVersion(newVersion);
        storage.createAuditEntry(AuditUtils.applicationVersionCreated(newVersion, securityContext));
        //create consumer on gateway
        try {
            //We create the new application version consumer
            IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            if (newVersion != null) {
                String appConsumerName = ConsumerConventionUtil.createAppUniqueId(newVersion.getApplication().getOrganization().getId(), newVersion.getApplication().getId(), newVersion.getVersion());
                //Applications' customId must contain version otherwise only one version of an application can be available on the gateway at one time
                //String appConsumerNameVersionLess = ConsumerConventionUtil.createAppVersionlessId(newVersion.getApplication().getOrganization().getId(), newVersion.getApplication().getId());
                gateway.createConsumer(appConsumerName, appConsumerName);
                gateway.addConsumerJWT(appConsumerName);
            }
        } catch (StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
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

        if (svb.getTermsAgreementRequired() != null && svb.getTermsAgreementRequired() && (bean.getTermsAgreed() == null || !bean.getTermsAgreed())) {
            throw ExceptionFactory.termsAgreementException("Agreement to terms & conditions required for contract creation");
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
        //verify contracts - reuse key if multiple
        final List<ContractSummaryBean> applicationVersionContracts = getApplicationVersionContracts(organizationId, applicationId, version);
        contract = new ContractBean();
        contract.setApplication(avb);
        contract.setService(svb);
        contract.setPlan(pvb);
        contract.setTermsAgreed(bean.getTermsAgreed());
        contract.setCreatedBy(securityContext.getCurrentUser());
        contract.setCreatedOn(new Date());
        if (applicationVersionContracts.size() > 0) {
            contract.setApikey(applicationVersionContracts.get(0).getApikey());//use same apikey when already a contract
        } else {
            contract.setApikey(apiKeyGenerator.generate());
        }
        // Validate the state of the application.
        if (avb.getStatus() != ApplicationStatus.Registered && applicationValidator.isReady(avb, true)) {
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
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param bean
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private boolean contractAlreadyExists(String organizationId, String applicationId, String version, NewContractBean bean) {
        try {
            List<ContractSummaryBean> contracts = query.getApplicationContracts(organizationId, applicationId, version);
            for (ContractSummaryBean contract : contracts) {
                if (contract.getServiceOrganizationId().equals(bean.getServiceOrgId()) &&
                        contract.getServiceId().equals(bean.getServiceId()) &&
                        contract.getServiceVersion().equals(bean.getServiceVersion()) &&
                        contract.getPlanId().equals(bean.getPlanId())) {
                    return true;
                }
            }
            return false;
        } catch (StorageException e) {
            return false;
        }
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
            policy.setId(null);
            policy.setDefinition(def);
            policy.setName(def.getName());
            //validate (remove null values) and apply custom implementation for the policy
            String policyJsonConfig = gatewayValidation.validate(new Policy(def.getId(), bean.getConfiguration()), ServiceConventionUtil.generateServiceUniqueName(organizationId, entityId, entityVersion)).getPolicyJsonConfig();
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
            policy.setGatewayId(bean.getGatewayId());
            storage.createPolicy(policy);
            storage.createAuditEntry(AuditUtils.policyAdded(policy, type, securityContext));
            //PolicyTemplateUtil.generatePolicyDescription(policy);
            log.debug(String.format("Created app policy: %s", policy)); //$NON-NLS-1$
            return policy;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
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
        newVersion.setAutoAcceptContracts(true);
        if (gateway != null) {
            if (newVersion.getGateways() == null) {
                newVersion.setGateways(new HashSet<ServiceGatewayBean>());
                ServiceGatewayBean sgb = new ServiceGatewayBean();
                sgb.setGatewayId(gateway.getId());
                newVersion.getGateways().add(sgb);
            }
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
     * @param organizationId
     * @param serviceId
     * @param version
     * @param data
     */
    protected void storeServiceDefinition(String organizationId, String serviceId, String version, ServiceDefinitionType definitionType, InputStream data) {
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
            String svPath = GatewayPathUtilities.generateGatewayContextPath(organizationId, serviceVersion.getService().getBasepath(), serviceVersion.getVersion());
            data = transformJSONObjectDef(data, serviceVersion, svPath);
            //safety check
            if (data == null) throw new DefinitionException("The Swagger data returned is invalid");
            storage.updateServiceDefinition(serviceVersion, data);
            log.debug(String.format("Stored service definition %s: %s", serviceId, serviceVersion)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * The method parses the json def to an object in order to perform some modificaitons.
     * In the API engine, conventionally all base paths must be set to "/" in the defintion.
     *
     * @param data
     * @return
     * @throws IOException
     */
    private InputStream transformSwaggerDef(InputStream data, ServiceVersionBean serviceVersionBean, String serviceVersionPath) throws StorageException, IOException {
        //set all base paths to "/servicebasepath" because the path is decided by the APi engine
        Swagger swaggerJson = new SwaggerParser().parse(IOUtils.toString(data));//IOUtils.closequietly?
        swaggerJson.setBasePath(serviceVersionPath);
        //available schemes override
        List<Scheme> schemeList = new ArrayList<>();
        schemeList.add(Scheme.HTTPS);
        swaggerJson.setSchemes(schemeList);
        swaggerJson.setHost(null);
        //read documentation and persist if present
        if (swaggerJson != null && swaggerJson.getExternalDocs() != null) {
            String onlineDoc = swaggerJson.getExternalDocs().getUrl();
            if (!StringUtils.isEmpty(onlineDoc)) {
                serviceVersionBean.setOnlinedoc(onlineDoc);
            }
        }
        storage.updateServiceVersion(serviceVersionBean);
        return new ByteArrayInputStream((Json.pretty(swaggerJson)).getBytes());

        //InputStream stream = new ByteArrayInputStream(exampleString.getBytes());//StandardCharsets.UTF_8
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
            json.put("host", config.getKongHost());

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
                ;//continue::don't do anything -> no external doc present
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
    private void validateTimeSeriesMetric(DateTime from, DateTime to, HistogramIntervalType interval) throws InvalidMetricCriteriaException {
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
/*        if (totalDataPoints > 5000) {
            throw ExceptionFactory.invalidMetricCriteriaException(Messages.i18n.format("OrganizationResourceImpl.MetricDataSetTooLarge")); //$NON-NLS-1$
        }*/
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
    protected ApiRegistryBean getApiRegistry(String organizationId, String applicationId, String version) throws ApplicationNotFoundException, NotAuthorizedException {
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
                ServiceBean service = storage.getService(api.getServiceOrgId(), api.getServiceId());
                ServiceEndpoint se = link.getServiceEndpoint(service.getBasepath(), api.getServiceOrgId(), api.getServiceId(), api.getServiceVersion());
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

    public void setEm(EntityManager em) {
        this.em = em;
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
            //announcement.fire(ann);
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
                if (ann.getOrganizationId().equals(organizationId) && ann.getServiceId().equals(serviceId))
                    storage.deleteServiceAnnouncement(ann);
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
        listMembers(orgId).forEach(member -> {
            member.getRoles().forEach(role -> {
                if (role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase())) {
                    //send email
                    try {
                        if (member.getUserId() != null && !StringUtils.isEmpty(member.getEmail())) {
                            MembershipRequestMailBean membershipRequestMailBean = new MembershipRequestMailBean();
                            membershipRequestMailBean.setTo(member.getEmail());
                            membershipRequestMailBean.setUserId(securityContext.getCurrentUser());
                            membershipRequestMailBean.setUserMail(securityContext.getEmail());
                            membershipRequestMailBean.setOrgName(org.getName());
                            membershipRequestMailBean.setOrgFriendlyName(org.getFriendlyName());
                            mailService.sendRequestMembership(membershipRequestMailBean);
                        }
                    } catch (Exception e) {
                        log.error("Error sending mail:{}", e.getMessage());
                    }
                }
            });
        });
        NewEventBean newEvent = new NewEventBean()
                .withOriginId(securityContext.getCurrentUser())
                .withDestinationId(org.getId())
                .withType(EventType.MEMBERSHIP_PENDING);
        event.fire(newEvent);
    }

    public void rejectMembershipRequest(String organizationId, String userId) {
        OrganizationBean org = get(organizationId);
        UserBean user = userFacade.get(userId);
        NewEventBean newEvent = new NewEventBean()
                .withOriginId(org.getId())
                .withDestinationId(user.getUsername())
                .withType(EventType.MEMBERSHIP_REJECTED);
        event.fire(newEvent);
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
     *
     * TODO implement method that cleansup an organization when no contracts are present.
     *
     * @param orgId
     */
    public void deleteOrganization(String orgId) {
        //remove memberships
        //leave audit and add closure
        //remove organization
        OrganizationBean org = get(orgId);
        try {
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
            List<ApplicationSummaryBean> apps = query.getApplicationsInOrg(orgId);
            if (!apps.isEmpty()) {
                for (ApplicationSummaryBean appSumm : apps) {
                    deleteApp(orgId, appSumm.getId());
                }
            }
            deleteOrganizationInternal(org);
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
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
            if (contractSummaries != null && !contractSummaries.isEmpty()) {

                //Generate new API key for contracts
                String newApiKey = apiKeyGenerator.generate();

                //Keep old API key for auditing purposes and retrieve & delete correct plugin on gateway
                String revokedKey = contractSummaries.get(0).getApikey();
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
                EntityUpdatedData data = new EntityUpdatedData();
                data.addChange("apikey", revokedKey, newApiKey);
                query.updateApplicationVersionApiKey(avb, newApiKey);
                storage.createAuditEntry(AuditUtils.credentialsReissue(avb, data, AuditEntryType.KeyAuthReissuance, securityContext));
                return new NewApiKeyBean(organizationId, applicationId, version, revokedKey, newApiKey);
            } else {
                //Application has no contracts, so return null
                return null;
            }
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
            if (avb.getoAuthClientId() == null || avb.getOauthClientSecret() == null) {
                return null;
            }
            //Check if the app has a callback uri, if it doesn't it isn't a consumer on the gateway(s), but we still want to
            //reissue the OAuth2 credentials
            NewOAuthCredentialsBean rval = new NewOAuthCredentialsBean();
            rval.setOrganizationId(avb.getApplication().getOrganization().getId());
            rval.setApplicationId(avb.getApplication().getId());
            rval.setVersion(avb.getVersion());
            rval.setRevokedClientId(avb.getoAuthClientId());
            rval.setRevokedClientSecret(avb.getOauthClientSecret());
            avb.setoAuthClientId(apiKeyGenerator.generate());
            avb.setOauthClientSecret(apiKeyGenerator.generate());
            rval.setNewClientId(avb.getoAuthClientId());
            rval.setNewClientSecret(avb.getOauthClientSecret());
            if (!(avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().stream().filter(uri -> ValidationUtils.isValidURL(uri)).collect(Collectors.toSet()).isEmpty())) {
                KongPluginOAuthConsumerRequest oAuthConsumerRequest = new KongPluginOAuthConsumerRequest()
                        .withClientId(avb.getoAuthClientId())
                        .withClientSecret(avb.getOauthClientSecret())
                        .withRedirectUri(avb.getOauthClientRedirects())
                        .withName(avb.getApplication().getName());
                if (avb.getStatus() == ApplicationStatus.Registered) {
                    try {
                        List<ContractSummaryBean> contractSummaries = query.getApplicationContracts(rval.getOrganizationId(), rval.getApplicationId(), rval.getVersion());
                        Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(contractSummaries);
                        for (IGatewayLink gatewayLink : gateways.values()) {
                            try {
                                gatewayLink.updateConsumerOAuthCredentials(ConsumerConventionUtil.createAppUniqueId(avb), rval.getRevokedClientId(), rval.getRevokedClientSecret(), oAuthConsumerRequest);
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
                            gateway.updateConsumerOAuthCredentials(ConsumerConventionUtil.createAppUniqueId(avb), rval.getRevokedClientId(), rval.getRevokedClientSecret(), oAuthConsumerRequest);
                        } catch (Exception e) {
                            throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e);
                        }
                    } else {
                        throw ExceptionFactory.invalidApplicationStatusException();
                    }
                }
            }
            EntityUpdatedData data = new EntityUpdatedData();
            data.addChange("OAuth2 Client ID", rval.getRevokedClientId(), rval.getNewClientId());
            data.addChange("OAuth2 Client Secret", rval.getRevokedClientSecret(), rval.getNewClientSecret());
            storage.updateApplicationVersion(avb);
            storage.createAuditEntry(AuditUtils.credentialsReissue(avb, data, AuditEntryType.OAuth2Reissuance, securityContext));
            return rval;
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private Map<String, IGatewayLink> getApplicationGatewayLinks(List<ContractSummaryBean> contractSummaries) {
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

    private Map<String, IGatewayLink> getApplicationContractGatewayLinks(List<ContractBean> contracts) {
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

    public List<KongOAuthToken> getApplicationVersionOAuthTokens(String organizationId, String applicationId, String versionId) {
        ApplicationVersionBean avb = getAppVersion(organizationId, applicationId, versionId);

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
            }
            catch (StorageException ex) {
                throw ExceptionFactory.systemErrorException(ex);
            }
        }
    }

    private ServiceVersionBean filterServiceVersionByAppPrefix(ServiceVersionBean svb) throws StorageException {
        String prefix = appContext.getApplicationPrefix();
        Set<String> allowedPrefixes = query.getManagedAppPrefixesForTypes(Arrays.asList(ManagedApplicationTypes.Consent, ManagedApplicationTypes.Publisher));
        svb.getVisibility().forEach(vis -> {
            allowedPrefixes.add(vis.getCode());
        });
        log.debug("allowedPrefixes:{}", allowedPrefixes);
        if (!allowedPrefixes.contains(prefix)) {
            throw ExceptionFactory.serviceVersionNotAvailableException(svb.getService().getId(), svb.getVersion());
        }
        return svb;
    }
}
