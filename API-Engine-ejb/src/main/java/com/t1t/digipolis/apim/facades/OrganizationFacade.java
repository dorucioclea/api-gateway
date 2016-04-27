package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.events.NewEventBean;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequest;
import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.announcements.NewAnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.digipolis.apim.beans.audit.data.MembershipData;
import com.t1t.digipolis.apim.beans.audit.data.OwnershipTransferData;
import com.t1t.digipolis.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.digipolis.apim.beans.availability.AvailabilityBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.events.EventStatus;
import com.t1t.digipolis.apim.beans.events.EventType;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.iprestriction.IPRestrictionFlavor;
import com.t1t.digipolis.apim.beans.mail.MembershipAction;
import com.t1t.digipolis.apim.beans.mail.RequestMembershipMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateMemberMailBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
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
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequestAccepted;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequestRejected;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.facades.audit.AuditUtils;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.rest.GatewayValidation;
import com.t1t.digipolis.apim.kong.KongConstants;
import com.t1t.digipolis.apim.mail.MailProvider;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsUsageList;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginIPRestriction;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
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
import javax.enterprise.util.AnnotationLiteral;
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
    private MailProvider mailProvider;
    @Inject
    private Event<NewEventBean> event;


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

    private static final long ONE_MINUTE_MILLIS = 1 * 60 * 1000;
    private static final long ONE_HOUR_MILLIS = 1 * 60 * 60 * 1000;
    private static final long ONE_DAY_MILLIS = 1 * 24 * 60 * 60 * 1000;
    private static final long ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000;
    private static final long ONE_MONTH_MILLIS = 30 * 24 * 60 * 60 * 1000;

    //craete organization
    public OrganizationBean create(NewOrganizationBean bean) {
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
        OrganizationBean orgBean = new OrganizationBean();
        orgBean.setName(bean.getName());
        orgBean.setDescription(bean.getDescription());
        orgBean.setId(BeanUtils.idFromName(bean.getName()));
        orgBean.setCreatedOn(new Date());
        orgBean.setCreatedBy(securityContext.getCurrentUser());
        orgBean.setModifiedOn(new Date());
        orgBean.setModifiedBy(securityContext.getCurrentUser());
        orgBean.setOrganizationPrivate(true);
        if (bean.getFriendlyName() == null) {
            orgBean.setFriendlyName(bean.getName());
        }
        else {
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
        newApp.setContext(appContext.getApplicationIdentifier().getScope());
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
                    npb.setConfiguration(GatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration())).getPolicyJsonConfig());
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
            avb.setOauthClientRedirect(uri.getUri());
            storage.updateApplicationVersion(avb);
            //register application credentials for OAuth2
            //create OAuth2 application credentials on the application consumer - should only been done once for this application
            if (avb != null && !StringUtils.isEmpty(avb.getoAuthClientId())) {
                String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId,applicationId,version);
                //String uniqueUserId = securityContext.getCurrentUser();
                OAuthConsumerRequestBean requestBean = new OAuthConsumerRequestBean();
                requestBean.setUniqueUserName(appConsumerName);
                requestBean.setAppOAuthId(avb.getoAuthClientId());
                requestBean.setAppOAuthSecret(avb.getOauthClientSecret());
                enableOAuthForConsumer(requestBean);
            }
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
            case Marketplace:
                type = PolicyType.Marketplace;
                break;
            case Consent:
                type = PolicyType.Consent;
                break;
            default:
                throw ExceptionFactory.invalidPolicyException("Invalid policy type");
        }
        return doCreatePolicy(managedApp.getAvailability().getCode(), managedApp.getName(), managedApp.getVersion(), bean, type);
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

    public ContractBean createContract(String organizationId, String applicationId, String version, NewContractBean bean) {
        try {
            //add OAuth2 consumer default to the application
            ContractBean contract = createContractInternal(organizationId, applicationId, version, bean);
            log.debug(String.format("Created new contract %s: %s", contract.getId(), contract)); //$NON-NLS-1$
            //for contract add keyauth to application consumer

                //We create the new application version consumer
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                if (contract != null) {
                    String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
                    try {
                        gateway.addConsumerKeyAuth(appConsumerName, contract.getApikey());
                    } catch (Exception e) {
                        //apikey for consumer already exists
                    }
                    //Add ACL group membership by default on gateway
                    KongPluginACLResponse response = gateway.addConsumerToACL(appConsumerName,
                            ServiceConventionUtil.generateServiceUniqueName(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion()));
                    //Persist the unique Kong plugin id in a new policy associated with the app.
                    NewPolicyBean npb = new NewPolicyBean();
                    KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
                    npb.setDefinitionId(Policies.ACL.name());
                    npb.setKongPluginId(response.getId());
                    npb.setContractId(contract.getId());
                    npb.setConfiguration(new Gson().toJson(conf));
                    createAppPolicy(organizationId, applicationId, version, npb);
                }
            //verify if the contracting service has OAuth enabled
            List<PolicySummaryBean> policySummaryBeans = listServicePolicies(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion());
            for (PolicySummaryBean summaryBean : policySummaryBeans) {
                if (summaryBean.getPolicyDefinitionId().toLowerCase().equals(Policies.OAUTH2.getKongIdentifier())) {
                    ApplicationVersionBean avb;
                    avb = storage.getApplicationVersion(organizationId, applicationId, version);
                    if (StringUtils.isEmpty(avb.getoAuthClientId())) {
                        //create client_id and client_secret for the application - the same client_id/secret must be used for all services
                        //upon publication the application credentials will be enabled for the current user.
                        avb.setoAuthClientId(apiKeyGenerator.generate());
                        avb.setOauthClientSecret(apiKeyGenerator.generate());
                        avb.setOauthClientRedirect("");
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
            //we asume it already exists
            throw ExceptionFactory.contractAlreadyExistsException();
/*            if (contractAlreadyExists(organizationId, applicationId, version, bean)) {
                throw ExceptionFactory.contractAlreadyExistsException();
            } else {
                throw new SystemErrorException(e);
            }*/
        }
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
            if (StringUtils.isEmpty(avb.getOauthClientRedirect()))
                throw new OAuthException("The application must provide an OAuth2 redirect URL");
            oauthRequest.setRedirectUri(avb.getOauthClientRedirect());
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
            // For each version, we will clean up the database and the gateway
            IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            for(ApplicationVersionSummaryBean version:versions){
                // Note that contract must be remove before deleting app - this is by using 'unregister' app in the action facade - but for some use cases, you can delete app while still having contract
                List<ContractSummaryBean> contractBeans = query.getApplicationContracts(version.getOrganizationId(), version.getId(), version.getVersion());
                // delete all contracts
                for(ContractSummaryBean contractSumBean:contractBeans){
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
                // Verify the status: if Created, Ready or Registered we need to remove the consumer from Kong
                ApplicationStatus status = version.getStatus();
                if (status.equals(ApplicationStatus.Created) || status.equals(ApplicationStatus.Ready) || status.equals(ApplicationStatus.Registered)) {
                    Application application = new Application();
                    application.setOrganizationId(version.getOrganizationId());
                    application.setApplicationId(version.getId());
                    application.setVersion(version.getVersion());
                    try {
                        gateway.unregisterApplication(application);
                    } catch (Exception e) {
                        throw ExceptionFactory.actionException(Messages.i18n.format("UnregisterError"), e); //$NON-NLS-1$
                    }
                }
            }
            gateway.close();
            // Remove all application versions from API Engine
            versions.stream().forEach(version -> {
                try {
                    ApplicationVersionBean appVersion = storage.getApplicationVersion(version.getOrganizationId(), version.getId(), version.getVersion());
                    storage.deleteApplicationVersion(appVersion);
                } catch (AbstractRestException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SystemErrorException(e);
                }
            });
            // Finally, delete the application from API Engine
            storage.deleteApplication(applicationBean);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
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
            return serviceVersion;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
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
        if (svb.getStatus() == ServiceStatus.Published || svb.getStatus() == ServiceStatus.Retired || svb.getStatus() == ServiceStatus.Deprecated) {
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
        if (AuditUtils.valueChanged(svb.getOnlinedoc(), bean.getOnlinedoc())) {
            data.addChange("online doc", svb.getOnlinedoc(), bean.getOnlinedoc());
            svb.setOnlinedoc(bean.getOnlinedoc());
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
        if (AuditUtils.valueChanged(svb.getVisibility(), bean.getVisibility())) {
            data.addChange("visibility", String.valueOf(svb.getVisibility()), String.valueOf(bean.getVisibility())); //$NON-NLS-1$
            svb.setVisibility(bean.getVisibility());
            //add implicitly the IP Restriction when: External available and hide = false
            KongPluginIPRestriction defaultIPRestriction = PolicyUtil.createDefaultIPRestriction(IPRestrictionFlavor.WHITELIST, query.listWhitelistRecords());
            boolean enableIPR = ServiceImplicitPolicies.verifyIfIPRestrictionShouldBeSet(svb);
            if(defaultIPRestriction !=null && enableIPR){
                Gson gson = new Gson();
                NewPolicyBean npb = new NewPolicyBean();
                npb.setDefinitionId("IPRestriction");//TODO == definition id in the DB - should not be hardcoded -> but addes to the Policies class
                npb.setConfiguration(gson.toJson(defaultIPRestriction));
                try{
                    createServicePolicy(organizationId,serviceId,version,npb);
                }catch(PolicyDefinitionAlreadyExistsException pdex){;}//ignore if policy already exists
            }else{
                //remove eventual policies already added
                List<PolicySummaryBean> policies = listServicePolicies(organizationId, serviceId, version);
                for(PolicySummaryBean psb:policies){
                    psb.getPolicyDefinitionId().equalsIgnoreCase("IPRestriction");
                    deleteServicePolicy(organizationId,serviceId,version,psb.getId());
                }
            }
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
            log.debug(String.format("Successfully updated Service Version: %s", svb)); //$NON-NLS-1$
            decryptEndpointProperties(svb);
            return svb;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
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
                updatedService.setEndpoint(cloneSource.getEndpoint());
                updatedService.setEndpointType(cloneSource.getEndpointType());
                updatedService.setEndpointProperties(cloneSource.getEndpointProperties());
                updatedService.setGateways(cloneSource.getGateways());
                updatedService.setOnlinedoc(cloneSource.getOnlinedoc());
                updatedService.setPlans(cloneSource.getPlans());
                updatedService.setPublicService(cloneSource.isPublicService());
                newVersion = updateServiceVersion(organizationId, serviceId, bean.getVersion(), updatedService);
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
                    npb.setConfiguration(GatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration()),ServiceConventionUtil.generateServiceUniqueName(organizationId,serviceId,bean.getCloneVersion())).getPolicyJsonConfig());
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
                    data.put(ServiceConventionUtil.generateServiceUniqueName(app.getServiceOrganizationId(), app.getServiceId(), app.getServiceVersion()),
                            metrics.getAppUsageForService(app.getServiceOrganizationId(), app.getServiceId(), app.getServiceVersion(), interval, from, to, consumerId));
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
        return metrics.getUsage(organizationId, serviceId, version, interval, from, to);
    }

    public ServiceMarketInfo getMarketInfo(String organizationId, String serviceId, String version) {
        return metrics.getServiceMarketInfo(organizationId, serviceId, version);
    }

    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        if (interval == null) {
            interval = HistogramIntervalType.day;
        }
        validateMetricRange(from, to);
        validateTimeSeriesMetric(from, to, interval);
        return metrics.getResponseStats(organizationId, serviceId, version, interval, from, to);
    }

    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, String fromDate, String toDate) {
        DateTime from = parseFromDate(fromDate);
        DateTime to = parseToDate(toDate);
        validateMetricRange(from, to);
        return metrics.getResponseStatsSummary(organizationId, serviceId, version, from, to);
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

    public void deleteContract(String organizationId, String applicationId, String version, Long contractId) {
        try {
            ContractBean contract = storage.getContract(contractId);
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
            //remove keyauth credentials for application consumer
            try {
                //We create the new application version consumer
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                if (!avb.getStatus().equals(ApplicationStatus.Retired)) {
                    String appConsumerName = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
                    gateway.deleteConsumerKeyAuth(appConsumerName, contract.getApikey());
                }
            } catch (StorageException e) {
                throw new ApplicationNotFoundException(e.getMessage());
            }
            //Revoke application's ACL membership
            try {
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                PolicyBean policy = query.getApplicationACLPolicy(organizationId, applicationId, version, contractId);
                if (policy == null) {
                    throw ExceptionFactory.policyNotFoundException(0);//TODO-> this indicated inconsistency in code! shouldnt be there
                }
                gateway.deleteConsumerACLPlugin(ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version), policy.getKongPluginId());
                deleteAppPolicy(organizationId, applicationId, version, policy.getId());
            }
            catch (StorageException ex) {
                throw new SystemErrorException(ex);
            }
            //remove contract
            storage.deleteContract(contract);
            //validate application state
            // Validate the state of the application.
            if (!applicationValidator.isReady(avb)) {
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
                    if (contract != null && !StringUtils.isEmpty(avb.getOauthClientRedirect())) {
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
                avb.setOauthClientRedirect("");
                storage.updateApplicationVersion(avb);
            }
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
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
                policy.setConfiguration(GatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration())).getPolicyJsonConfig());
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

    public ServiceBean updateServiceTerms(String organizationId, String serviceId, UpdateServiceTearmsBean serviceTerms) {
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

    public void deleteService(String organizationId, String serviceId) {
        if (!securityContext.hasPermission(PermissionType.svcAdmin, organizationId))
            throw ExceptionFactory.notAuthorizedException();
        try {
            // Get Service
            ServiceBean serviceBean = storage.getService(organizationId, serviceId);
            if (serviceBean == null) {
                throw ExceptionFactory.serviceNotFoundException(serviceId);
            }

            // Get Service versions
            List<ServiceVersionSummaryBean> versions = query.getServiceVersions(serviceBean.getOrganization().getId(), serviceBean.getId());
            IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            versions.stream().forEach(version -> {

                // (Remove any existing contracts) Instead of removing existing contrasts, throw error if there still are any.
                try {
                    List<ContractSummaryBean> contracts = query.getServiceContracts(version.getOrganizationId(), version.getId(), version.getVersion(), 1, 1000);
                    if (contracts.size() > 0) {
                        throw ExceptionFactory.serviceCannotDeleteException("Service still has contracts");
                    }
                    /*contracts.stream().forEach(contract -> {
                        try {
                            ContractBean contractBean = storage.getContract(contract.getContractId());
                            storage.deleteContract(contractBean);
                        } catch (StorageException e) {
                            throw new SystemErrorException(e);
                        }
                    });*/
                } catch (StorageException e) {
                    throw new SystemErrorException(e);
                }

                // Remove service definition if found
                InputStream definitionStream = getServiceDefinition(version.getOrganizationId(), version.getId(), version.getVersion());
                if (definitionStream != null) {
                    deleteServiceDefinition(version.getOrganizationId(), version.getId(), version.getVersion());
                }

                // Remove service policies
                List<PolicySummaryBean> policies = listServicePolicies(version.getOrganizationId(), version.getId(), version.getVersion());
                policies.stream().forEach(policy -> {
                    try {
                        PolicyBean policyBean = storage.getPolicy(PolicyType.Service, version.getOrganizationId(), version.getId(), version.getVersion(), policy.getId());
                        storage.deletePolicy(policyBean);
                    } catch (AbstractRestException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new SystemErrorException(e);
                    }
                });

                //Revoke marketplace ACL memberships
                try {
                    List<PolicyBean> aclPolicies = query.getManagedAppACLPolicies(organizationId, serviceId, version.getVersion());
                    for (PolicyBean policy : aclPolicies) {
                        gateway.deleteConsumerACLPlugin(ConsumerConventionUtil.createAppUniqueId(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion()), policy.getKongPluginId());
                        storage.deletePolicy(policy);
                    }
                }
                catch (StorageException ex) {
                    throw new SystemErrorException(ex);
                }

                // Remove gateway config & plan configuration for service version
                ServiceVersionBean svb = getServiceVersion(version.getOrganizationId(), version.getId(), version.getVersion());
                svb.getGateways().clear();
                svb.getPlans().clear();
                try {
                    storage.updateServiceVersion(svb);
                } catch (AbstractRestException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SystemErrorException(e);
                }

                // For each version, verify the status: if Published we need to remove the API from Kong
                ServiceStatus status = version.getStatus();
                if (status.equals(ServiceStatus.Published)) {
                    Service service = new Service();
                    service.setOrganizationId(version.getOrganizationId());
                    service.setServiceId(version.getId());
                    service.setVersion(version.getVersion());
                    try {
                        gateway.retireService(service);
                    } catch (Exception e) {
                        throw ExceptionFactory.actionException(Messages.i18n.format("RetireError"), e); //$NON-NLS-1$
                    }
                }
            });
            gateway.close();
            // Remove all service versions from API Engine
            versions.stream().forEach(version -> {
                try {
                    ServiceVersionBean svcVersion = storage.getServiceVersion(version.getOrganizationId(), version.getId(), version.getVersion());
                    //TODO break contracts in order to clean up the oauth2 stuff
                    storage.deleteServiceVersion(svcVersion);
                } catch (AbstractRestException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SystemErrorException(e);
                }
            });

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
            // Finally, delete the Service from API Engine
            storage.deleteService(serviceBean);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
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
            //enrich visibility with name (coming from other entity)
            Map<String, AvailabilityBean> availableMarkets = query.listAvailableMarkets();
            for(VisibilityBean vb: serviceVersion.getVisibility()){
                if(availableMarkets.containsKey(vb.getCode())){
                    vb.setName(availableMarkets.get(vb.getCode()).getName());
                }
            }
            Map<String,VisibilityBean> serviceVisibilities = serviceVersion.getVisibility().stream().collect(Collectors.toMap(VisibilityBean::getCode, Function.identity()));
            return serviceVisibilities;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public KongPluginConfigList getServicePlugins(String organizationId, String serviceId, String version) {
        try {
            KongPluginConfigList servicePlugins = null;
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
            if(pluginConfigList!=null && pluginConfigList.getData().size()>0){
                KongPluginConfig pluginConfig = pluginConfigList.getData().get(0);
                pluginConfig.setEnabled(enable);
                pluginConfig = gateway.updateServicePlugin(serviceKongId,pluginConfig);
                return pluginConfig;
            }else{
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
        getServiceVersion(organizationId, serviceId, version);
        try {
            PolicyBean policy = storage.getPolicy(PolicyType.Service, organizationId, serviceId, version, policyId);
            if (policy == null) {
                throw ExceptionFactory.policyNotFoundException(policyId);
            }
            // TODO capture specific change values when auditing policy updates
            if (AuditUtils.valueChanged(policy.getConfiguration(), bean.getConfiguration())) {
                policy.setConfiguration(GatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration()),ServiceConventionUtil.generateServiceUniqueName(organizationId,serviceId,version)).getPolicyJsonConfig());
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

    public Set<ApplicationBean> listServiceConsumers(String orgId, String serviceId){
        //get all service versions
        final List<ServiceVersionSummaryBean> serviceVersions = listServiceVersions(orgId, serviceId);
        Set<ApplicationBean> apps = new TreeSet<>();
        for(ServiceVersionSummaryBean svb:serviceVersions){
            try {
                final List<ContractBean> serviceContracts = query.getServiceContracts(svb.getOrganizationId(), svb.getId(), svb.getVersion());
                for(ContractBean contract:serviceContracts){
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
                    npb.setConfiguration(GatewayValidation.validate(new Policy(policy.getDefinition().getId(), policy.getConfiguration())).getPolicyJsonConfig());
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

                policy.setConfiguration(GatewayValidation.validate(new Policy(policy.getDefinition().getId(), bean.getConfiguration())).getPolicyJsonConfig());
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
        get(organizationId);
        userFacade.get(bean.getUserId());
        roleFacade.get(bean.getRoleId());
        // If user had a pending membership request,
        MembershipData auditData = new MembershipData();
        auditData.setUserId(bean.getUserId());
        try {
            RoleMembershipBean membership = RoleMembershipBean.create(bean.getUserId(), bean.getRoleId(), organizationId);
            membership.setCreatedOn(new Date());
            // If the membership already exists, that's fine!
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
        NewEventBean newEvent = new NewEventBean(organizationId, bean.getUserId(), EventType.Membership);
        event.select(new AnnotationLiteral<MembershipRequestAccepted>(){}).fire(newEvent);
        //send email
        try{
            final RoleBean roleBean = roleFacade.get(bean.getRoleId());
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(bean.getUserId());
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.NEW_MEMBERSHIP);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole(roleBean.getName());
                mailProvider.sendUpdateMember(updateMemberMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
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
        try{
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(userId);
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole("");
                mailProvider.sendUpdateMember(updateMemberMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
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
        try{
            final RoleBean roleBean = roleFacade.get(bean.getRoleId());
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(userId);
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.UPDATE_ROLE);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole(roleBean.getName());
                mailProvider.sendUpdateMember(updateMemberMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
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
        try{
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(userId);
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                updateMemberMailBean.setRole("");
                mailProvider.sendUpdateMember(updateMemberMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
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
        try{
            final OrganizationBean organizationBean = get(organizationId);
            final UserBean userBean = userFacade.get(bean.getNewOwnerId());
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                updateMemberMailBean.setTo(userBean.getEmail());
                updateMemberMailBean.setMembershipAction(MembershipAction.TRANSFER);
                updateMemberMailBean.setOrgName(organizationBean.getName());
                updateMemberMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                mailProvider.sendUpdateMember(updateMemberMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
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
                gateway.createConsumer(appConsumerName,appConsumerName);
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
        //verify contracts - reuse key if multiple
        final List<ContractSummaryBean> applicationVersionContracts = getApplicationVersionContracts(organizationId, applicationId, version);
        contract = new ContractBean();
        contract.setApplication(avb);
        contract.setService(svb);
        contract.setPlan(pvb);
        contract.setCreatedBy(securityContext.getCurrentUser());
        contract.setCreatedOn(new Date());
        if(applicationVersionContracts.size()>0){
            contract.setApikey(applicationVersionContracts.get(0).getApikey());//use same apikey when already a contract
        }else {
            contract.setApikey(apiKeyGenerator.generate());
        }
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
    protected PolicyBean doCreatePolicy(String organizationId, String entityId, String entityVersion, NewPolicyBean bean, PolicyType type) throws PolicyDefinitionNotFoundException {
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
            String policyJsonConfig = GatewayValidation.validate(new Policy(def.getId(), bean.getConfiguration()),ServiceConventionUtil.generateServiceUniqueName(organizationId,entityId,entityVersion)).getPolicyJsonConfig();
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

    public void requestMembership(String orgId){
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
        try {
            EventBean event = query.getEvent(securityContext.getCurrentUser(), org.getId(), EventType.Membership);
            if (event != null && event.getStatus() == EventStatus.Pending) {
                throw ExceptionFactory.membershipRequestFailedException("Membership already requested, still pending");
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
        listMembers(orgId).forEach(member -> {
            member.getRoles().forEach(role -> {
                if(role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase())){
                    //send email
                    try{
                        if(member.getUserId()!=null && !StringUtils.isEmpty(member.getEmail())){
                            RequestMembershipMailBean requestMembershipMailBean = new RequestMembershipMailBean();
                            requestMembershipMailBean.setTo(member.getEmail());
                            requestMembershipMailBean.setUserId(securityContext.getCurrentUser());
                            requestMembershipMailBean.setUserMail(securityContext.getEmail());
                            requestMembershipMailBean.setOrgName(org.getName());
                            requestMembershipMailBean.setOrgFriendlyName(org.getFriendlyName());
                            mailProvider.sendRequestMembership(requestMembershipMailBean);
                        }
                    }catch(Exception e){
                        log.error("Error sending mail:{}",e.getMessage());
                    }
                }
            });
        });
        NewEventBean newEvent = new NewEventBean(securityContext.getCurrentUser(), org.getId(), EventType.Membership);
        event.select(new AnnotationLiteral<MembershipRequest>(){}).fire(newEvent);
    }

    public void rejectMembershipRequest(String organizationId, String userId) {
        OrganizationBean org = get(organizationId);
        UserBean user = userFacade.get(userId);
        NewEventBean newEvent = new NewEventBean(org.getId(), user.getUsername(), EventType.Membership);
        event.select(new AnnotationLiteral<MembershipRequestRejected>() {}).fire(newEvent);
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

    /* Test utilities */
    public boolean deleteOrganization(String orgId) {
        try {
            OrganizationBean organization = storage.getOrganization(orgId);
            deleteOrganization(orgId);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        //get all services
        //get all service versions
        //get all applications
        //get all application versions
        //get all contracts
        //remove all contracts
        //get all oauth apps
        //get all plans
        //get all plan versions
        //get all plan policies
        //remove all plan policies
        //remove all plans versions
        //remove all plans

        return true;
    }

}
