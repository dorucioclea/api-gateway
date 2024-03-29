package com.t1t.apim.facades;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.t1t.apim.beans.actions.ActionBean;
import com.t1t.apim.beans.actions.SwaggerDocBean;
import com.t1t.apim.beans.apps.ApplicationStatus;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.beans.idm.PermissionType;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.beans.plans.PlanStatus;
import com.t1t.apim.beans.plans.PlanVersionBean;
import com.t1t.apim.beans.policies.NewPolicyBean;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.policies.PolicyBean;
import com.t1t.apim.beans.policies.PolicyType;
import com.t1t.apim.beans.services.ServiceGatewayBean;
import com.t1t.apim.beans.services.ServiceStatus;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ContractSummaryBean;
import com.t1t.apim.beans.summary.PolicySummaryBean;
import com.t1t.apim.core.IApplicationValidator;
import com.t1t.apim.core.IServiceValidator;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.*;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.util.AuditUtils;
import com.t1t.apim.exceptions.GatewayAuthenticationException;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.gateway.dto.Application;
import com.t1t.apim.gateway.dto.Contract;
import com.t1t.apim.gateway.dto.Policy;
import com.t1t.apim.gateway.dto.Service;
import com.t1t.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.kong.model.KongConsumer;
import com.t1t.kong.model.KongPluginACLResponse;
import com.t1t.kong.model.KongPluginConfigList;
import com.t1t.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.GatewayUtils;
import com.t1t.util.ServiceConventionUtil;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ActionFacade {
    private static Logger log = LoggerFactory.getLogger(ActionFacade.class.getName());
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private OrganizationFacade orgFacade;
    @Inject
    private IServiceValidator serviceValidator;
    @Inject
    private IApplicationValidator applicationValidator;
    @Inject
    private GatewayFacade gatewayFacade;

    public void performAction(ActionBean action) {
        switch (action.getType()) {
            case publishService:
                publishService(action);
                return;
            case retireService:
                retireService(action);
                return;
            case registerApplication:
                registerApplication(action);
                return;
            case unregisterApplication:
                unregisterApplication(action);
                return;
            case lockPlan:
                lockPlan(action);
                return;
            case deprecateService:
                deprecateService(action);
                return;
            default:
                throw ExceptionFactory.actionException("Action type not supported: " + action.getType().toString()); //$NON-NLS-1$
        }
    }

    /**
     * Fetches a remote Swagger Documentation file.
     *
     * @param swaggerDocBean Bean containing the URI to fetch
     */
    public SwaggerDocBean fetchSwaggerDoc(SwaggerDocBean swaggerDocBean) throws ActionException {
        String data;
        try {
            URL url = new URL(swaggerDocBean.getSwaggerURI());
            data = Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        swaggerDocBean.setSwaggerDoc(data);
        return swaggerDocBean;
    }

    /**
     * Publishes a service to the gateway.
     *
     * @param action
     */
    private void publishService(ActionBean action) throws ActionException {
        if (!securityContext.hasPermission(PermissionType.svcAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();
        ServiceVersionBean versionBean = null;
        try {
            versionBean = orgFacade.getServiceVersionInternal(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (ServiceVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ServiceNotFound")); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - service must be Ready.
        try {
            if (!serviceValidator.isReady(versionBean)) {
                throw ExceptionFactory.actionException(Messages.i18n.format("InvalidServiceStatus")); //$NON-NLS-1$
            }
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidServiceStatus"), e); //$NON-NLS-1$
        }

        Service gatewaySvc = new Service();
        //gatewaySvc.setEndpoint(versionBean.getEndpoint());
        gatewaySvc.setUpstreamScheme(versionBean.getUpstreamScheme());
        gatewaySvc.setUpstreamPath(versionBean.getUpstreamPath());
        gatewaySvc.setEndpointType(versionBean.getEndpointType().toString());
        gatewaySvc.setEndpointProperties(versionBean.getEndpointProperties());
        gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
        gatewaySvc.setServiceId(versionBean.getService().getId());
        gatewaySvc.setBasepaths(versionBean.getService().getBasepaths());
        gatewaySvc.setVersion(versionBean.getVersion());
        gatewaySvc.setPublicService(versionBean.isPublicService());
        gatewaySvc.setBrandings(versionBean.getService().getBrandings().stream().map(ServiceBrandingBean::getId).collect(Collectors.toSet()));
        gatewaySvc.setUpstreamTargets(new ArrayList<>(versionBean.getUpstreamTargets()));
        gatewaySvc.setCustomLoadBalancing(versionBean.getCustomLoadBalancing() == null ? false : versionBean.getCustomLoadBalancing());

        try {
            //we don't restrict the application of service policies for only the public services
            /*if (versionBean.isPublicService()) {*/
            List<Policy> policiesToPublish = new ArrayList<>();
            List<PolicySummaryBean> servicePolicies = query.getPolicies(action.getOrganizationId(),
                    action.getEntityId(), action.getEntityVersion(), PolicyType.Service);
            for (PolicySummaryBean policySummaryBean : servicePolicies) {
                PolicyBean servicePolicy = storage.getPolicy(PolicyType.Service, action.getOrganizationId(),
                        action.getEntityId(), action.getEntityVersion(), policySummaryBean.getId());
                Policy policyToPublish = new Policy();
                policyToPublish.setPolicyJsonConfig(servicePolicy.getConfiguration());
                policyToPublish.setPolicyImpl(servicePolicy.getDefinition().getId());
                //policyToPublish.setPolicyImpl(servicePolicy.getDefinition().getPolicyImpl());
                policyToPublish.setPolicyId(policySummaryBean.getId());
                policiesToPublish.add(policyToPublish);
            }
            gatewaySvc.setServicePolicies(policiesToPublish);
            /*}*/
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        }
        // Publish the service to all relevant gateways
        try {
            Set<ServiceGatewayBean> gateways = versionBean.getGateways();
            if (gateways == null) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            List<ManagedApplicationBean> marketplaces = query.findManagedApplication(ManagedApplicationTypes.Consent);
            for (ServiceGatewayBean serviceGatewayBean : gateways) {
                IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(serviceGatewayBean.getGatewayId());
                gatewaySvc = gatewayLink.publishService(gatewaySvc);
                //Here we add the various marketplaces to the Service's ACL, otherwise try-out in marketplace won't work
                for (ManagedApplicationBean marketplace : marketplaces) {
                    KongPluginACLResponse response = gatewayLink.addConsumerToACL(
                            ConsumerConventionUtil.createManagedApplicationConsumerName(marketplace),
                            ServiceConventionUtil.generateServiceUniqueName(gatewaySvc));
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(Policies.ACL.name());
                    npb.setConfiguration(new Gson().toJson(response));
                    npb.setKongPluginId(response.getId());
                    npb.setGatewayId(gatewayLink.getGatewayId());
                    orgFacade.createManagedApplicationPolicy(marketplace, npb);
                }
                //update the policies with the Kong plugin id
                for (Policy policy : gatewaySvc.getServicePolicies()) {
                    if (policy.getPolicyId() != null) {
                        PolicyBean pb = storage.getPolicy(PolicyType.Service, gatewaySvc.getOrganizationId(), gatewaySvc.getServiceId(), gatewaySvc.getVersion(), policy.getPolicyId());
                        pb.setGatewayId(gatewayLink.getGatewayId());
                        pb.setKongPluginId(policy.getKongPluginId());
                        storage.updatePolicy(pb);
                    } else {
                        log.error("Plugin present on service {} but no corresponding policy/missing policy id:{}", ServiceConventionUtil.generateServiceUniqueName(versionBean), policy);
                    }
                }
                gatewayLink.close();
            }

            versionBean.setStatus(ServiceStatus.Published);
            versionBean.setPublishedOn(new Date());
            storage.updateServiceVersion(versionBean);
            storage.createAuditEntry(AuditUtils.servicePublished(versionBean, securityContext));
        } catch (PublishingException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        }
        log.debug(String.format("Successfully published Service %s on specified gateways: %s", //$NON-NLS-1$
                versionBean.getService().getName(), versionBean.getService()));
    }

    /**
     * Retires a service that is currently published to the Gateway.
     *
     * @param action
     */
    private void retireService(ActionBean action) throws ActionException {
        if (!securityContext.hasPermission(PermissionType.svcAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();

        ServiceVersionBean versionBean = null;
        try {
            versionBean = orgFacade.getServiceVersionInternal(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (ServiceVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ServiceNotFound")); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - service must be Ready.
        if (!(versionBean.getStatus() == ServiceStatus.Published || versionBean.getStatus() == ServiceStatus.Deprecated)) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidServiceStatus")); //$NON-NLS-1$
        }
        Service gatewaySvc = new Service();
        gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
        gatewaySvc.setServiceId(versionBean.getService().getId());
        gatewaySvc.setVersion(versionBean.getVersion());
        gatewaySvc.setBrandings(versionBean.getService().getBrandings().stream().map(ServiceBrandingBean::getId).collect(Collectors.toSet()));
        //Checks if service still has contracts
        try {
            if (!query.getServiceContracts(gatewaySvc.getOrganizationId(), gatewaySvc.getServiceId(), gatewaySvc.getVersion(), 1, 1000).isEmpty()) {
                throw ExceptionFactory.serviceCannotDeleteException("Service still has contracts");
            }
        } catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }

        // Retire the service from all relevant gateways
        try {
            Set<ServiceGatewayBean> gateways = versionBean.getGateways();
            if (gateways == null) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            for (ServiceGatewayBean serviceGatewayBean : gateways) {
                IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(serviceGatewayBean.getGatewayId());
                gatewayLink.retireService(gatewaySvc);
                //Revoke marketplace ACL memberships
                List<PolicyBean> aclPolicies = query.getManagedAppACLPolicies(gatewaySvc.getOrganizationId(), gatewaySvc.getServiceId(), gatewaySvc.getVersion());
                for (PolicyBean policy : aclPolicies) {
                    gatewayLink.deleteConsumerACLPlugin(ConsumerConventionUtil.createAppUniqueId(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion()), policy.getKongPluginId());
                    storage.deletePolicy(policy);
                }
                gatewayLink.close();
            }

            versionBean.setStatus(ServiceStatus.Retired);
            versionBean.setRetiredOn(new Date());

            storage.updateServiceVersion(versionBean);
            storage.createAuditEntry(AuditUtils.serviceRetired(versionBean, securityContext));
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("RetireError"), e); //$NON-NLS-1$
        }
        log.debug(String.format("Successfully retired Service %s on specified gateways: %s", //$NON-NLS-1$
                versionBean.getService().getName(), versionBean.getService()));
    }

    private void deprecateService(ActionBean action) {
        if (!securityContext.hasPermission(PermissionType.svcAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();

        ServiceVersionBean versionBean = null;
        try {
            versionBean = orgFacade.getServiceVersionInternal(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (ServiceVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ServiceNotFound")); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - service must be Ready.
        if (versionBean.getStatus() != ServiceStatus.Published) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidServiceStatus")); //$NON-NLS-1$
        }
        versionBean.setStatus(ServiceStatus.Deprecated);
        versionBean.setDeprecatedOn(new Date());
        try {
            storage.updateServiceVersion(versionBean);
            storage.createAuditEntry(AuditUtils.serviceRetired(versionBean, securityContext));
        } catch (StorageException ex) {
            throw ExceptionFactory.actionException(Messages.i18n.format("DeprecateError"), ex); //$NON-NLS-1$
        }
    }

    /**
     * Registers an application (along with all of its contracts) to the gateway.
     *
     * @param action
     */
    private void registerApplication(ActionBean action) throws ActionException {
        if (!securityContext.hasPermission(PermissionType.appAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();

        ApplicationVersionBean versionBean;
        List<ContractSummaryBean> contractBeans;
        try {
            versionBean = orgFacade.getAppVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (ApplicationVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ApplicationNotFound")); //$NON-NLS-1$
        }

        try {
            contractBeans = query.getApplicationContracts(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ApplicationNotFound"), e); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - application must be Ready.
        try {
            if (!applicationValidator.isReady(versionBean)) {
                throw ExceptionFactory.actionException(Messages.i18n.format("InvalidApplicationStatus")); //$NON-NLS-1$
            }
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidApplicationStatus"), e); //$NON-NLS-1$
        }

        /*// Validate that all apikeys are equal for the scope of one application
        if(versionBean.getApikey() == null) throw ExceptionFactory.actionException(Messages.i18n.format("MissingAPIKey"));

        //application should have contracts when accessed directly from api.
        String appApiKey;
        if(contractBeans==null||contractBeans.size()==0)throw ExceptionFactory.actionException(Messages.i18n.format("InvalidContractCount"));
        else{
            //we are sure the contracts are not empty and that all apikeys must be equal.
            appApiKey = versionBean.getApikey();
        }*/

        Application application = new Application();
        application.setOrganizationId(versionBean.getApplication().getOrganization().getId());
        application.setApplicationId(versionBean.getApplication().getId());
        application.setVersion(versionBean.getVersion());
        application.setApplicationName(versionBean.getApplication().getName());

        Set<Contract> contracts = new HashSet<>();
        for (ContractSummaryBean contractBean : contractBeans) {
            Contract contract = new Contract(contractBean);
            contract.getPolicies().addAll(aggregateContractPolicies(contractBean));
            contracts.add(contract);
        }
        application.setContracts(contracts);

        String appConsumerName = ConsumerConventionUtil.createAppUniqueId(application.getOrganizationId(), application.getApplicationId(), application.getVersion());

        // Next, register the application with *all* relevant gateways.  This is done by
        // looking up all referenced services and getting the gateway information for them.
        // Each of those gateways must be told about the application.
        try {
            //Set the application status to registered in order to get all the relevant gateway links
            versionBean.setStatus(ApplicationStatus.Registered);
            Set<String> gatewayIds = query.getGatewayIdsForApplicationVersionContracts(versionBean);
            if (gatewayIds == null || gatewayIds.isEmpty()) {
                throw new PublishingException("Contracted services have no defined gateways for application: " + versionBean.getApplication().getName()); //$NON-NLS-1$
            }

            gatewayIds.stream().map(gatewayFacade::createGatewayLink).forEach(gw -> {
                try {
                    //First, make sure the Application exists on the various gateways and create it, with the necessary credentials, if not
                    KongConsumer gwApp = gw.getConsumer(appConsumerName);
                    if (gwApp == null) {
                        try {
                            gw.createConsumer(appConsumerName);
                            gw.addConsumerKeyAuth(appConsumerName, versionBean.getApikey());
                            gw.enableConsumerForOAuth(appConsumerName, new KongPluginOAuthConsumerRequest()
                                    .withClientId(appConsumerName)
                                    .withClientSecret(versionBean.getOauthClientSecret())
                                    .withName(versionBean.getApplication().getName())
                                    .withId(versionBean.getOauthCredentialId())
                                    .withRedirectUri(versionBean.getOauthClientRedirects()));
                            String publicKey = gatewayFacade.getDefaultGatewayPublicKey();
                            gw.addConsumerJWT(appConsumerName, publicKey);
                        } catch (Exception ex) {
                            //Delete the consumer on the gateway so the gateway and engine remain in sync
                            gw.deleteConsumer(appConsumerName);
                            throw ex;
                        }
                    }
                    Map<Contract, KongPluginConfigList> response = gw.registerApplication(application);
                    response.forEach((key, value) -> value.getData().forEach(plugin -> {
                        try {
                            if (query.getPolicyByKongPluginId(plugin.getId()) == null) {
                                NewPolicyBean npb = new NewPolicyBean();
                                npb.setGatewayId(gw.getGatewayId());
                                npb.setConfiguration(new Gson().toJson(plugin.getConfig()));
                                npb.setContractId(key.getId());
                                npb.setKongPluginId(plugin.getId());
                                npb.setDefinitionId(GatewayUtils.convertKongPluginNameToPolicy(plugin.getName()).getPolicyDefId());
                                //save the policy as a contract policy on the service
                                orgFacade.doCreatePolicy(application.getOrganizationId(), application.getApplicationId(), application.getVersion(), npb, PolicyType.Contract);
                            }
                        } catch (StorageException ex) {
                            throw ExceptionFactory.systemErrorException(ex);
                        }
                    }));
                } catch (GatewayAuthenticationException ex) {
                    throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), ex); //$NON-NLS-1$
                }
            });

            versionBean.setStatus(ApplicationStatus.Registered);
            versionBean.setPublishedOn(new Date());

            storage.updateApplicationVersion(versionBean);
            storage.createAuditEntry(AuditUtils.applicationRegistered(versionBean, securityContext));
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
        }

        log.debug(String.format("Successfully registered Application %s on specified gateways: %s", //$NON-NLS-1$
                versionBean.getApplication().getName(), versionBean.getApplication()));
    }

    /**
     * Aggregates the service, app, and plan policies into a single ordered list.
     * Application policies can be supported in the future.
     * Service policies are already enforced for the service at publication.
     * At the moment only Plan policies are important to aggregate
     *
     * @param contractBean
     */
    private List<Policy> aggregateContractPolicies(ContractSummaryBean contractBean) {
        try {

            List<Policy> policies = new ArrayList<>();
            PolicyType[] types = new PolicyType[1];
            types[0] = PolicyType.Plan;
/*            types[1] = PolicyType.Application;
            types[2] = PolicyType.Service;*/
            for (PolicyType policyType : types) {
                String org;
                String id;
                String ver;
                switch (policyType) {
/*                    case Application: {
                        org = contractBean.getOrganizationId();
                        id = contractBean.getAppId();
                        ver = contractBean.getAppVersion();
                        break;
                    }*/
                    case Plan: {
                        org = contractBean.getServiceOrganizationId();
                        id = contractBean.getPlanId();
                        ver = contractBean.getPlanVersion();
                        break;
                    }
/*                    case Service: {
                        org = contractBean.getServiceOrganizationId();
                        id = contractBean.getServiceId();
                        ver = contractBean.getServiceVersion();
                        break;
                    }*/
                    default: {
                        throw new IllegalArgumentException("Missing case for switch!"); //$NON-NLS-1$
                    }
                }
                List<PolicySummaryBean> appPolicies = query.getPolicies(org, id, ver, policyType);
                for (PolicySummaryBean policySummaryBean : appPolicies) {
                    PolicyBean policyBean = storage.getPolicy(policyType, org, id, ver, policySummaryBean.getId());
                    Policy policy = new Policy();
                    policy.setPolicyJsonConfig(policyBean.getConfiguration());
                    policy.setPolicyImpl(policyBean.getDefinition().getId());
                    policies.add(policy);
                }
            }
            return policies;
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PolicyPublishError", contractBean.getPlanId()), e); //$NON-NLS-1$
        }
    }

    /**
     * De-registers an application that is currently registered with the gateway.
     *
     * @param action
     */
    private void unregisterApplication(ActionBean action) throws ActionException {
        if (!securityContext.hasPermission(PermissionType.appAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();

        ApplicationVersionBean versionBean = null;
        List<ContractSummaryBean> contractBeans = null;
        try {
            versionBean = orgFacade.getAppVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (ApplicationVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ApplicationNotFound")); //$NON-NLS-1$
        }
        try {
            contractBeans = query.getApplicationContracts(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ApplicationNotFound"), e); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - application must be Registered.
        if (versionBean.getStatus() != ApplicationStatus.Registered) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidApplicationStatus")); //$NON-NLS-1$
        }

        Application application = new Application();
        application.setOrganizationId(versionBean.getApplication().getOrganization().getId());
        application.setApplicationId(versionBean.getApplication().getId());
        application.setVersion(versionBean.getVersion());

        // Next, unregister the application from *all* relevant gateways.  This is done by
        // looking up all referenced services and getting the gateway information for them.
        // Each of those gateways must be told about the application.
        try {
            Map<String, IGatewayLink> links = new HashMap<>();
            for (ContractSummaryBean contractBean : contractBeans) {
                ServiceVersionBean svb = storage.getServiceVersion(contractBean.getServiceOrganizationId(),
                        contractBean.getServiceId(), contractBean.getServiceVersion());
                Set<ServiceGatewayBean> gateways = svb.getGateways();
                if (gateways == null) {
                    throw new PublishingException("No gateways specified for service: " + svb.getService().getName()); //$NON-NLS-1$
                }
                for (ServiceGatewayBean serviceGatewayBean : gateways) {
                    if (!links.containsKey(serviceGatewayBean.getGatewayId())) {
                        IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(serviceGatewayBean.getGatewayId());
                        links.put(serviceGatewayBean.getGatewayId(), gatewayLink);
                    }
                }
            }
            for (IGatewayLink gatewayLink : links.values()) {
                gatewayLink.unregisterApplication(application);
                gatewayLink.close();
            }
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("UnregisterError"), e); //$NON-NLS-1$
        }

        versionBean.setStatus(ApplicationStatus.Retired);
        versionBean.setRetiredOn(new Date());

        // delete all contracts
        orgFacade.deleteContractsForSummaries(contractBeans);

        try {
            storage.updateApplicationVersion(versionBean);
            storage.createAuditEntry(AuditUtils.applicationUnregistered(versionBean, securityContext));
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("UnregisterError"), e); //$NON-NLS-1$
        }

        log.debug(String.format("Successfully registered Application %s on specified gateways: %s", //$NON-NLS-1$
                versionBean.getApplication().getName(), versionBean.getApplication()));
    }

    /**
     * Locks the plan.
     *
     * @param action
     */
    private void lockPlan(ActionBean action) throws ActionException {
        if (!securityContext.hasPermission(PermissionType.planAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();

        PlanVersionBean versionBean = null;
        try {
            versionBean = orgFacade.getPlanVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (PlanVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PlanNotFound")); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - plan must not already be locked
        if (versionBean.getStatus() == PlanStatus.Locked) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidPlanStatus")); //$NON-NLS-1$
        }

        versionBean.setStatus(PlanStatus.Locked);
        versionBean.setLockedOn(new Date());

        try {
            storage.updatePlanVersion(versionBean);
            storage.createAuditEntry(AuditUtils.planLocked(versionBean, securityContext));
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("LockError"), e); //$NON-NLS-1$
        }

        log.debug(String.format("Successfully locked Plan %s: %s", //$NON-NLS-1$
                versionBean.getPlan().getName(), versionBean.getPlan()));
    }
}