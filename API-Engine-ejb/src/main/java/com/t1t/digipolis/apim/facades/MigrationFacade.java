package com.t1t.digipolis.apim.facades;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.apps.ApplicationBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.audit.AuditEntityType;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.digipolis.apim.beans.events.Event;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.RoleMembershipBean;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceGatewayBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionWithMarketInfoBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationVersionSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicySummaryBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.AbstractRestException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.GatewayUtils;
import com.t1t.digipolis.util.ObjectCloner;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MigrationFacade {

    private static final Logger _LOG = LoggerFactory.getLogger(MigrationFacade.class);
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private SearchFacade searchFacade;
    @Inject
    private OrganizationFacade orgFacade;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;

    public MigrationFacade() {
    }

    public void migrateToAcl() {
        List<ServiceVersionWithMarketInfoBean> publishedServices = searchFacade.searchServicesByStatus(ServiceStatus.Published);
        enableAclOnPublishedServices(publishedServices);
        enableAclOnApplications();
        _LOG.info("Migration ACL finished");
    }

    public void renameApplicationCustomIds() {
        try {
            IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            gateway.getConsumers().getData().forEach(consumer -> {
                if (!StringUtils.isEmpty(consumer.getUsername())) {
                    if (!consumer.getUsername().equals(consumer.getCustomId())) {
                        consumer.setCustomId(consumer.getUsername());
                        gateway.updateOrCreateConsumer(consumer);
                    }
                }
            });
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    /**
     * Enables ACL plugin on every published service
     */
    @SuppressWarnings("Duplicates")
    private void enableAclOnPublishedServices(List<ServiceVersionWithMarketInfoBean> publishedServices) {
        try {
            IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            List<ManagedApplicationBean> managedApps = query.getManagedApps();
            for (ServiceVersionBean versionBean : publishedServices) {
                Service gatewaySvc = new Service();
                gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
                gatewaySvc.setServiceId(versionBean.getService().getId());
                gatewaySvc.setVersion(versionBean.getVersion());
                //Create plugin on Kong api
                try {
                    _LOG.info("ACL plugin:{}", gateway.createACLPlugin(gatewaySvc));
                } catch (Exception ex) {
                    ;//ignore
                }
                //Add marketplaces to Service ACL
                for (ManagedApplicationBean managedApp : managedApps) {
                    try {
                        KongPluginACLResponse response = gateway.addConsumerToACL(
                                ConsumerConventionUtil.createManagedApplicationConsumerName(managedApp),
                                ServiceConventionUtil.generateServiceUniqueName(gatewaySvc));
                        _LOG.info("Marketplace ACL:{}", response);
                        NewPolicyBean npb = new NewPolicyBean();
                        npb.setDefinitionId(Policies.ACL.name());
                        npb.setConfiguration(new Gson().toJson(response));
                        npb.setKongPluginId(response.getId());
                        npb.setGatewayId(gateway.getGatewayId());
                        _LOG.info("Marketplace policy:{}", orgFacade.createManagedApplicationPolicy(managedApp, npb));
                    } catch (Exception ex) {
                        //ignore
                    }
                }
            }
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private void enableAclOnApplications() {
        try {
            IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            List<ApplicationVersionBean> applications = query.findAllApplicationVersions();
            for (ApplicationVersionBean appVersion : applications) {
                if (appVersion.getStatus() != ApplicationStatus.Retired) {
                    String applicationName = ConsumerConventionUtil.createAppUniqueId(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                    List<ContractSummaryBean> contractSummaries = query.getApplicationContracts(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                    for (ContractSummaryBean summary : contractSummaries) {
                        String serviceName = ServiceConventionUtil.generateServiceUniqueName(summary.getServiceOrganizationId(), summary.getServiceId(), summary.getServiceVersion());
                        try {
                            KongPluginACLResponse response = gateway.addConsumerToACL(applicationName, serviceName);
                            //Persist the unique Kong plugin id in a new policy associated with the app.
                            NewPolicyBean npb = new NewPolicyBean();
                            KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
                            npb.setDefinitionId(Policies.ACL.name());
                            npb.setKongPluginId(response.getId());
                            npb.setContractId(summary.getContractId());
                            npb.setConfiguration(new Gson().toJson(conf));
                            npb.setGatewayId(gateway.getGatewayId());
                            _LOG.info("Policy " + applicationName + ":{}", orgFacade.doCreatePolicy(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion(), npb, PolicyType.Contract));
                        } catch (Exception ex) {
                            ;//ignore
                        }
                    }
                }
            }
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private IGatewayLink createGatewayLink(String gatewayId) throws AbstractRestException {
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

    /**
     * TODO this first implementation will sync all apikeys for all applications.
     */
    public void syncBusinessModel() throws AbstractRestException {
        //Sync Services
        //Sync Applications
        //Sync apikeys
        try {
            final List<ApplicationVersionBean> applicationVersions = query.findAllApplicationVersions();
            _LOG.info("=== SYNC::Applications Apikeys(" + applicationVersions.size() + ")");
            for (ApplicationVersionBean avb : applicationVersions) {
                //Keep track of service gateways
                Map<String, IGatewayLink> links = new HashMap<>();
                //Get contracts
                final List<ContractSummaryBean> applicationContracts = query.getApplicationContracts(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion());
                String appApiKey = null;
                for (ContractSummaryBean contractSummaryBean : applicationContracts) {
                    //all apikeys should be the same
                    if (StringUtils.isEmpty(appApiKey) && StringUtils.isNotEmpty(contractSummaryBean.getApikey())) {
                        appApiKey = contractSummaryBean.getApikey();
                    }
                    //get all gateways where application should be registered
                    ServiceVersionBean svb = storage.getServiceVersion(contractSummaryBean.getServiceOrganizationId(), contractSummaryBean.getServiceId(), contractSummaryBean.getServiceVersion());
                    Set<ServiceGatewayBean> gateways = svb.getGateways();
                    for (ServiceGatewayBean serviceGatewayBean : gateways) {
                        if (!links.containsKey(serviceGatewayBean.getGatewayId())) {
                            IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                            links.put(serviceGatewayBean.getGatewayId(), gatewayLink);
                        }
                    }
                }
                //log info
                _LOG.info("Sync application orgId:" + avb.getApplication().getOrganization().getId());
                _LOG.info("Sync application appId:" + avb.getApplication().getId());
                _LOG.info("Sync application version:" + avb.getVersion());
                _LOG.info("===> apikey:" + appApiKey);
                _LOG.info("===> gateways:" + links);
                //sync apikey
                if (StringUtils.isNotEmpty(appApiKey)) {
                    for (IGatewayLink gatewayLink : links.values()) {
                        // Validate that the application has a key-auth apikey available on the gateway - fallback scenario
                        try {
                            String appConsumerName = ConsumerConventionUtil.createAppUniqueId(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion());
                            gatewayLink.addConsumerKeyAuth(appConsumerName, appApiKey);
                        } catch (Exception e) {
                            //apikey for consumer already exists
                        }
                        gatewayLink.close();
                    }
                }
            }
            _LOG.info("=== FINISHED SYNC::Applications Apikeys(" + applicationVersions.size() + ")");
        } catch (StorageException e) {
            throw new SystemErrorException("Error accessing datastore");
        }
    }


    //TODO: execute init script
    //sync users
    //republish services
    //initialize unregistered consumer
    //register consumers
    //TODO: impact service request events?
    public void rebuildGtw() {
        _LOG.info("====MIGRATION-START====");
        removeACLsFromDB();
        removeContractPoliciesFromDB();
        syncUsers();
        republishServices();
        syncApplications();
        //MigrateToAcl is not a sync endpoint. Do not use unless you're migrating a gateway that doesn't have acl policies to a version of the API Engine that does
        //migrateToAcl();
        _LOG.info("====MIGRATION-END======");
    }

    /**
     * Synchronizes existing users from db to gateway.
     * No plugins are applied on users
     * All users must have JWT credentials generated.
     */
    private void syncUsers() {
        _LOG.info("Synchronize Users::START");
        try {
            //get all users
            final List<UserBean> allUsers = idmStorage.getAllUsers();

            //create gateway
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
            IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);

            for (UserBean user : allUsers) {
                _LOG.info("->sync user with kong id {} and username {}...", user.getKongUsername(), user.getUsername());
                //verify a kong user id exists - if not we don't need to create it on Kong, this will happen upon next login (user init)
                if (!StringUtils.isEmpty(user.getKongUsername())) {
                    try {
                        //create user on gateway using existing kong_username
                        gatewayLink.createConsumerWithKongId(user.getKongUsername(), ConsumerConventionUtil.createUserUniqueId(user.getUsername()));
                        Thread.sleep(100);
                        //create jwt token
                        gatewayLink.addConsumerJWT(user.getKongUsername());
                    } catch (RetrofitError rte) {
                        _LOG.error("-->no sync executed for kong id {} and username {}", user.getKongUsername(), user.getUsername());
                        continue;
                    }
                } else _LOG.info("no sync needed - kong username missing in db");
                _LOG.info("-->sync end");
            }
        } catch (InterruptedException | StorageException e) {
            _LOG.error("Synchronize Users failed due to:" + e.getMessage());
            e.printStackTrace();
        }
        _LOG.info("Synchronize Users::END");
    }

    /**
     * All published services must be provisioned to the gateway, we reuse the action facade for all know
     * services with state published.
     * We pay attention to:
     * - existing provisionkey for oauth plugin
     * - verification of default policies
     */
    private void republishServices() {
        _LOG.info("Publish Services::START");
        try {
            //get all published services, including the deprecated ones
            final List<ServiceVersionBean> services = query.findGatewayServiceVersions();
            for (ServiceVersionBean svb : services) {
                _LOG.info("-->sync org:{} service:{} version:{} ...",
                        svb.getService().getOrganization().getId(),
                        svb.getService().getId(),
                        svb.getVersion());
                if (svb.getStatus() != ServiceStatus.Retired) {
                    //prepare service object for gateway
                    Service gatewaySvc = new Service();
                    gatewaySvc.setEndpoint(svb.getEndpoint());
                    gatewaySvc.setEndpointType(svb.getEndpointType().toString());
                    gatewaySvc.setEndpointProperties(svb.getEndpointProperties());
                    gatewaySvc.setOrganizationId(svb.getService().getOrganization().getId());
                    gatewaySvc.setServiceId(svb.getService().getId());
                    gatewaySvc.setBasepath(svb.getService().getBasepath());
                    gatewaySvc.setVersion(svb.getVersion());
                    gatewaySvc.setPublicService(svb.isPublicService());

                    //enrich with policies to publish
                    List<Policy> policiesToPublish = new ArrayList<>();
                    List<PolicySummaryBean> servicePolicies = query.getPolicies(
                            svb.getService().getOrganization().getId(),
                            svb.getService().getId(),
                            svb.getVersion(),
                            PolicyType.Service);
                    for (PolicySummaryBean policySummaryBean : servicePolicies) {
                        PolicyBean servicePolicy = storage.getPolicy(PolicyType.Service,
                                svb.getService().getOrganization().getId(),
                                svb.getService().getId(),
                                svb.getVersion(),
                                policySummaryBean.getId());
                        Policy policyToPublish = new Policy();
                        policyToPublish.setPolicyJsonConfig(servicePolicy.getConfiguration());
                        policyToPublish.setPolicyImpl(servicePolicy.getDefinition().getId());
                        policiesToPublish.add(policyToPublish);
                    }
                    gatewaySvc.setServicePolicies(policiesToPublish);

                    // Publish the service to all relevant gateways
                    Set<ServiceGatewayBean> gateways = svb.getGateways();
                    if (gateways != null) {
                        List<ManagedApplicationBean> marketplaces = query.findManagedApplication(ManagedApplicationTypes.Consent);
                        for (ServiceGatewayBean serviceGatewayBean : gateways) {
                            try {
                                IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                                try {
                                    gatewayLink.publishService(gatewaySvc);
                                } catch (GatewayAuthenticationException e) {
                                    continue;//next loop cycle
                                }
                                //Here we add the various marketplaces to the Service's ACL, otherwise try-out in marketplace won't work
                                for (ManagedApplicationBean managedApp : marketplaces) {
                                    try {
                                        KongPluginACLResponse response = gatewayLink.addConsumerToACL(
                                                ConsumerConventionUtil.createManagedApplicationConsumerName(managedApp),
                                                ServiceConventionUtil.generateServiceUniqueName(gatewaySvc));
                                        NewPolicyBean npb = new NewPolicyBean();
                                        npb.setDefinitionId(Policies.ACL.name());
                                        npb.setConfiguration(new Gson().toJson(response));
                                        npb.setKongPluginId(response.getId());
                                        npb.setGatewayId(gatewayLink.getGatewayId());
                                        orgFacade.createManagedApplicationPolicy(managedApp, npb);
                                    } catch (Exception ex) {
                                        //ignore
                                    }
                                }
                                gatewayLink.close();
                            } catch (RetrofitError |  SystemErrorException ex) {
                                _LOG.error("-->no sync executed for org:{} service:{} version:{} ...",
                                        svb.getService().getOrganization().getId(),
                                        svb.getService().getId(),
                                        svb.getVersion());
                                continue;
                            }
                        }
                    }
                    _LOG.info("-->sync end");
                } else {
                    _LOG.info("-->sync end (nothing done - service retired)");
                }
            }
        } catch (StorageException e) {
            _LOG.error("Synchronize Users failed due to:" + e.getMessage());
            e.printStackTrace();
        }
        _LOG.info("Publish Services::END");
    }

    private void removeACLsFromDB() {
        _LOG.info("Remove DB ACLs::BEGIN");
        try {
            query.deleteAclPolicies();
        } catch (StorageException e) {
            _LOG.error("Delete ACL policies failed:" + e.getMessage());
            e.printStackTrace();
        }
        _LOG.info("Remove DB ACL::END");
    }

    private void removeContractPoliciesFromDB() {
        _LOG.info("Remove Contract policies::BEGIN");
        try {
            query.deleteContractPolicies();
        } catch (StorageException e) {
            _LOG.error("Delete Contract policies failed:" + e.getMessage());
            e.printStackTrace();
        }
        _LOG.info("Remove Contract policies::END");
    }

    /**
     * Unregisterd applications are initialized with:
     * - applicable apikey (from contract)
     * - correct acl's (based on contracted services)
     * - jwt credentials (future jwt-up)
     * - oauth credentials + callback url (+ oauth name)
     */
    private void syncApplications() {
        _LOG.info("Synchronize Consumers::START");
        try {
            final List<ApplicationVersionBean> appversions = query.findAllApplicationVersions();
            //for all contracted applications create the consumer with key-auth and jwt values
            for (ApplicationVersionBean avb : appversions) {
                //verify application is not retired
                if (avb.getStatus() != ApplicationStatus.Retired) {
                    String appConsumerName = ConsumerConventionUtil.createAppUniqueId(avb.getApplication().getOrganization().getId(),
                            avb.getApplication().getId(),
                            avb.getVersion());
                    //verify if has contracts
                    final List<ContractSummaryBean> avbContracts = query.getApplicationContracts(
                            avb.getApplication().getOrganization().getId(),
                            avb.getApplication().getId(),
                            avb.getVersion());
                    //update gateway
                    try {
                        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                        //create consumer
                        gateway.createConsumer(appConsumerName, appConsumerName);
                        //when one or more contract exists - apply apikey and ACLs else no apikey or ACL is set
                        if (avbContracts != null && avbContracts.size() > 0) {
                            String apikey = avbContracts.get(0).getApikey();//same apikey for all contracts
                            gateway.addConsumerKeyAuth(appConsumerName, apikey);
                        }
                        //create jwt token
                        gateway.addConsumerJWT(appConsumerName);
                        //sync oauth info
                        if (!StringUtils.isEmpty(avb.getoAuthClientId()) && !StringUtils.isEmpty(avb.getOauthClientSecret())) {//redirect may be empty
                            if (StringUtils.isEmpty(avb.getOauthClientRedirect()))
                                avb.setOauthClientRedirect(OrganizationFacade.PLACEHOLDER_CALLBACK_URI);
                            //apply oauth
                            OAuthConsumerRequestBean requestBean = new OAuthConsumerRequestBean();
                            requestBean.setUniqueUserName(appConsumerName);
                            requestBean.setAppOAuthId(avb.getoAuthClientId());
                            requestBean.setAppOAuthSecret(avb.getOauthClientSecret());
                            //build oauth request
                            KongPluginOAuthConsumerRequest oauthRequest = new KongPluginOAuthConsumerRequest()
                                    .withClientId(requestBean.getAppOAuthId())
                                    .withClientSecret(requestBean.getAppOAuthSecret());
                            oauthRequest.setName(avb.getApplication().getName());
                            oauthRequest.setRedirectUri(avb.getOauthClientRedirect());
                            try {

                            } catch (Exception e) {
                                _LOG.info("OAuth sync skipped for {}", avb);
                                continue;//don't do anything
                            }
                            gateway.enableConsumerForOAuth(appConsumerName, oauthRequest);
                        }
                        //if app registered - apply additionally plugins
                        gateway.close();
                    } catch (RetrofitError rte) {
                        _LOG.error("-->no sync executed for org:{} app:{} version:{} ...",
                                avb.getApplication().getOrganization().getId(),
                                avb.getApplication().getId(),
                                avb.getVersion());
                        //continue;
                    }

                    if (avb.getStatus() != ApplicationStatus.Retired) {
                        IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                        for (ContractSummaryBean contractBean : avbContracts) {
                            Gson gson = new Gson();
                            String serviceVersionId = ServiceConventionUtil.generateServiceUniqueName(
                                    contractBean.getServiceOrganizationId(),
                                    contractBean.getServiceId(),
                                    contractBean.getServiceVersion());
                            //Add ACL group membership by default on gateway
                            KongPluginACLResponse response = gateway.addConsumerToACL(appConsumerName, serviceVersionId);
                            //Persist the unique Kong plugin id in a new policy associated with the app.
                            NewPolicyBean npb = new NewPolicyBean();
                            KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
                            npb.setDefinitionId(Policies.ACL.name());
                            npb.setKongPluginId(response.getId());
                            npb.setContractId(contractBean.getContractId());
                            npb.setConfiguration(gson.toJson(conf));
                            npb.setGatewayId(gateway.getGatewayId());
                            orgFacade.doCreatePolicy(
                                    avb.getApplication().getOrganization().getId(),
                                    avb.getApplication().getId(),
                                    avb.getVersion(), npb, PolicyType.Contract);
                        }
                    }

                    //apply additional policies for registered applications
                    if (avb.getStatus() == ApplicationStatus.Registered) {
                        Application application = new Application();
                        application.setOrganizationId(avb.getApplication().getOrganization().getId());
                        application.setApplicationId(avb.getApplication().getId());
                        application.setVersion(avb.getVersion());
                        application.setApplicationName(avb.getApplication().getName());

                        Set<Contract> contracts = new HashSet<>();
                        for (ContractSummaryBean contractBean : avbContracts) {
                            Contract contract = new Contract(contractBean);
                            contract.getPolicies().addAll(aggregateContractPolicies(contractBean));
                            contracts.add(contract);
                        }
                        application.setContracts(contracts);

                        try {
                            Map<String, IGatewayLink> gateways = getApplicationGatewayLinks(avbContracts);
                            for (IGatewayLink gateway : gateways.values()) {
                                Map<Contract, KongPluginConfigList> response = gateway.registerApplication(application);
                                for (Map.Entry<Contract, KongPluginConfigList> entry : response.entrySet()) {
                                    for (KongPluginConfig config : entry.getValue().getData()) {
                                        NewPolicyBean npb = new NewPolicyBean();
                                        npb.setGatewayId(gateway.getGatewayId());
                                        npb.setConfiguration(new Gson().toJson(config.getConfig()));
                                        npb.setContractId(entry.getKey().getId());
                                        npb.setKongPluginId(config.getId());
                                        npb.setDefinitionId(GatewayUtils.convertKongPluginNameToPolicy(config.getName()).getPolicyDefId());
                                        //save the policy as a contract policy on the service
                                        orgFacade.doCreatePolicy(application.getOrganizationId(), application.getApplicationId(), application.getVersion(), npb, PolicyType.Contract);
                                    }
                                }
                                gateway.close();
                            }
                        } catch (Exception e) {
                            _LOG.error("-->no sync for additional policies applied for org:{} app:{} version:{} ...",
                                    avb.getApplication().getOrganization().getId(),
                                    avb.getApplication().getId(),
                                    avb.getVersion());
                            continue;
                        }
                    }
                    _LOG.info("-->sync end");
                } else {
                    _LOG.info("-->sync end (nothing done - application retired)");
                }
            }
        } catch (StorageException e) {
            _LOG.error("Synchronize Consumers failed due to:" + e.getMessage());
            e.printStackTrace();
        }
        //contract
        //contract policies
        _LOG.info("Synchronize Consumers::END");
    }

    /**
     * Helper for Application sync towards gateway, the method aggregates Contract summ beans into a list of policy objects.
     *
     * @param contractBean
     * @return
     */
    private List<Policy> aggregateContractPolicies(ContractSummaryBean contractBean) {
        try {
            List<Policy> policies = new ArrayList<>();
            PolicyType[] types = new PolicyType[1];
            types[0] = PolicyType.Plan;
            for (PolicyType policyType : types) {
                String org, id, ver;
                switch (policyType) {
                    case Plan: {
                        org = contractBean.getServiceOrganizationId();
                        id = contractBean.getPlanId();
                        ver = contractBean.getPlanVersion();
                        break;
                    }
                    default: {
                        throw new RuntimeException("Missing case for switch!");
                    }
                }
                List<PolicySummaryBean> appPolicies = query.getPolicies(org, id, ver, policyType);
                try {
                    for (PolicySummaryBean policySummaryBean : appPolicies) {
                        PolicyBean policyBean = storage.getPolicy(policyType, org, id, ver, policySummaryBean.getId());
                        Policy policy = new Policy();
                        policy.setPolicyJsonConfig(policyBean.getConfiguration());
                        policy.setPolicyImpl(policyBean.getDefinition().getId());
                        policies.add(policy);
                    }
                } finally {
                }
            }
            return policies;
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PolicyPublishError", contractBean.getApikey()), e);
        }
    }

    /**
     * The migration method will perform the following steps:
     * <ul>
     *  <li>get all applications</li>
     *  <li>make sure the context has been provided, default using 'int'</li>
     *  <li>get application context</li>
     *  <li>for context == int: create org with prefix from internal marketplace</li>
     *  <li>for context == ext: create org with prefix from external marketplace</li>
     *  <li>for context == empty: don't do anything should be removed later when cleaning up</li>
     *  <li>update memberships</li>
     *  <li>assign the new organisation to the application</li>
     *  <li>assign the application versions to the updated application (composite key tx)</li>
     *  <li>update kong consumers (APIs remains intact because pub orgs not renamed)</li>
     * </ul>
     *
     * This script can not be executed twice! We don't know when an organization has been split.
     *
     * @throws StorageException
     */
    public void splitOrgs() throws Exception {
        _LOG.info("Split Orgs::START");
        final String APP_DEF_CONTEXT = "int";
        //get all applications
        List<ApplicationBean> allApplications = query.findAllApplications();
        for (ApplicationBean app : allApplications) {
            //make sure context has been provided
            if(StringUtils.isEmpty(app.getContext()))app.setContext(APP_DEF_CONTEXT);
            OrganizationBean originalOrg = app.getOrganization();
            String destOrgId = splitUsingApp(originalOrg,app);
            final OrganizationBean destOrganization = storage.getOrganization(destOrgId);
            ApplicationBean newApp = (ApplicationBean)ObjectCloner.deepCopy(app);
            newApp.setOrganization(destOrganization);
            storage.createApplication(newApp);
            //reassign application version before tx commit
            final List<ApplicationVersionSummaryBean> applicationVersions = query.getApplicationVersions(originalOrg.getId(), app.getId());
            for(ApplicationVersionSummaryBean asb:applicationVersions){
                final ApplicationVersionBean appVersion = storage.getApplicationVersion(originalOrg.getId(), app.getId(), asb.getVersion());
                appVersion.setApplication(newApp);
                //update Kong consumer
                String appConsumerName = ConsumerConventionUtil.createAppUniqueId(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                String originalConsumerName = ConsumerConventionUtil.createAppUniqueId(originalOrg.getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                final KongConsumer consumer = gateway.getConsumer(appConsumerName);
                KongConsumer originalConsumer = gateway.getConsumer(originalConsumerName);
                if(consumer==null && originalConsumer != null){
                    //update
                    originalConsumer.setUsername(appConsumerName);
                    originalConsumer.setCustomId(appConsumerName);
                    gateway.updateConsumer(originalConsumer.getId(), originalConsumer);
                }
            }
            storage.deleteApplication(app);
        }
        migrateToSplitOrgs();
        _LOG.info("Split Orgs::END");
    }

    //MEMBERSHIPS UPDATE

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private String splitUsingApp(OrganizationBean originalOrg,ApplicationBean app) throws Exception {
        _LOG.info("Split {}", app.getName());
        ManagedApplicationBean marketplaceManagedApp = query.findManagedApplication(app.getContext());
        if (marketplaceManagedApp != null) {
            _LOG.info("App context {} with prefix ({})", marketplaceManagedApp.getName(), marketplaceManagedApp.getPrefix());
            //create org name - already formatted, just append the prefix
            String newOrgId = marketplaceManagedApp.getPrefix() + OrganizationFacade.MARKET_SEPARATOR + originalOrg.getId();
            //verify if org exists, if non-existant -> create new org with deep copy of original
            OrganizationBean destOrg = verifyAndCreate(originalOrg, newOrgId,app.getContext());
            //attach detached entity - does the trick
            destOrg = storage.getOrganization(destOrg.getId());
            //update memberships
            final Set<RoleMembershipBean> orgMemberships = idmStorage.getOrgMemberships(originalOrg.getId());
            for(RoleMembershipBean rmb:orgMemberships){
                final RoleMembershipBean existingMembership = idmStorage.getMembership(rmb.getUserId(), rmb.getRoleId(), newOrgId);
                if(existingMembership==null){
                    RoleMembershipBean newRmb = (RoleMembershipBean) ObjectCloner.deepCopy(rmb);
                    newRmb.setOrganizationId(newOrgId);
                    newRmb.setId(null);
                    idmStorage.createMembership(newRmb);
                    _LOG.info("-->member created:{} - '{}' with role '{}'", destOrg.getId(),newRmb.getUserId(),newRmb.getRoleId());
                }
            }
            return destOrg.getId();
        } else {
            _LOG.info("Ignored Application (must be publisher or consent app):{}", app);
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private OrganizationBean verifyAndCreate(OrganizationBean originalOrg, String newOrgId,String context) throws Exception {
        OrganizationBean targetOrg = null;
        targetOrg = storage.getOrganization(newOrgId);
        if (targetOrg == null) {
            //create org
            OrganizationBean tobecreatedOrg = new OrganizationBean();
            tobecreatedOrg.setDescription(originalOrg.getDescription());
            tobecreatedOrg.setFriendlyName(originalOrg.getFriendlyName());
            tobecreatedOrg.setName(originalOrg.getName());
            tobecreatedOrg.setOrganizationPrivate(originalOrg.isOrganizationPrivate());
            tobecreatedOrg.setContext(context);
            tobecreatedOrg.setCreatedBy(originalOrg.getCreatedBy());
            tobecreatedOrg.setCreatedOn(originalOrg.getCreatedOn());
            tobecreatedOrg.setModifiedBy(originalOrg.getModifiedBy());
            tobecreatedOrg.setModifiedOn(originalOrg.getModifiedOn());
            tobecreatedOrg.setId(newOrgId);
            storage.createOrganization(tobecreatedOrg);
            _LOG.info("-->org created:{}", tobecreatedOrg.getId());
            return tobecreatedOrg;
        } else return targetOrg;
    }

    private void migrateToSplitOrgs() {
        try{
            List<ApplicationVersionBean> apps = query.findAllApplicationVersions();
            for (ApplicationVersionBean avb : apps) {
                //Update policies with new org ids
                String oldOrgName = avb.getApplication().getOrganization().getId().split(OrganizationFacade.MARKET_SEPARATOR)[1];
                List<PolicyBean> policies = query.listPoliciesForEntity(oldOrgName, avb.getApplication().getId(), avb.getVersion(), PolicyType.Application);
                for (PolicyBean policy : policies) {
                    policy.setOrganizationId(avb.getApplication().getOrganization().getId());
                    storage.updatePolicy(policy);
                }
                //Update audit entries with new org ids
                List<AuditEntryBean> entries = query.listAuditEntriesForEntity(oldOrgName, avb.getApplication().getId(), avb.getVersion(), AuditEntityType.Application);
                for (AuditEntryBean entry : entries) {
                    entry.setOrganizationId(avb.getApplication().getOrganization().getId());
                    storage.updateAuditEntry(entry);
                }
            }
            for (OrganizationBean org : getOrgsByContext()) {
                String oldOrgName = org.getId().split(OrganizationFacade.MARKET_SEPARATOR)[1];
                //update events with new org id's
                List<EventBean> events = query.getAllEventsRelatedToOrganization(oldOrgName);
                for (EventBean event : events) {
                    switch (event.getType()) {
                        case MEMBERSHIP_PENDING:
                            event.setDestinationId(org.getId());
                            storage.updateEvent(event);
                            break;
                        case CONTRACT_ACCEPTED:
                        case CONTRACT_REJECTED:
                            String[] dest = event.getDestinationId().split(".");
                            if (dest.length != 3) {
                                continue;
                            }
                            event.setDestinationId(new StringBuilder(org.getId())
                                                        .append(".")
                                                        .append(dest[1])
                                                        .append(".")
                                                        .append(dest[2])
                                                        .toString());
                            storage.updateEvent(event);
                            break;
                    }
                }
            }
            ManagedApplicationBean intMarket = query.findManagedApplication("int");
            ManagedApplicationBean extMarket = query.findManagedApplication("ext");
            List<PolicyBean> mktPlacePol = query.listPoliciesForEntity(intMarket.getPrefix(), intMarket.getAppId(), intMarket.getVersion(), PolicyType.Marketplace);
            mktPlacePol.addAll(query.listPoliciesForEntity(extMarket.getPrefix(), extMarket.getAppId(), extMarket.getVersion(), PolicyType.Marketplace));
            for (PolicyBean pol : mktPlacePol) {
                if (pol.getDefinition().getId().equals(Policies.ACL.name())) {
                    IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                    gateway.deleteConsumerACLPlugin(ConsumerConventionUtil.createAppUniqueId(pol.getOrganizationId(), pol.getEntityId(), pol.getEntityVersion()), pol.getKongPluginId());
                    storage.deletePolicy(pol);
                }
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private List<OrganizationBean> getOrgsByContext() throws StorageException {
        List<OrganizationBean> rval = new ArrayList<>();
        List<OrganizationBean> orgs = query.getAllOrgs();
        for (OrganizationBean org : orgs) {
            if (org.getContext().equals("int") || org.getContext().equals("ext")) {
                rval.add(org);
            }
        }
        return rval;
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
}