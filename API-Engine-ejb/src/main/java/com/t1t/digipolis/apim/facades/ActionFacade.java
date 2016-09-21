package com.t1t.digipolis.apim.facades;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.actions.ActionBean;
import com.t1t.digipolis.apim.beans.actions.SwaggerDocBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.digipolis.apim.beans.plans.PlanStatus;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceGatewayBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicySummaryBean;
import com.t1t.digipolis.apim.core.IApplicationValidator;
import com.t1t.digipolis.apim.core.IServiceValidator;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.facades.audit.AuditUtils;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.GatewayUtils;
import com.t1t.digipolis.util.KeyUtils;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URL;
import java.util.*;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ActionFacade {
    private static Logger log = LoggerFactory.getLogger(ActionFacade.class.getName());
    @PersistenceContext private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private IGatewayLinkFactory gatewayLinkFactory;
    @Inject private OrganizationFacade orgFacade;
    @Inject private IServiceValidator serviceValidator;
    @Inject private IApplicationValidator applicationValidator;

    public void performAction(ActionBean action){
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
        gatewaySvc.setEndpoint(versionBean.getEndpoint());
        gatewaySvc.setEndpointType(versionBean.getEndpointType().toString());
        gatewaySvc.setEndpointProperties(versionBean.getEndpointProperties());
        gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
        gatewaySvc.setServiceId(versionBean.getService().getId());
        gatewaySvc.setBasepath(versionBean.getService().getBasepath());
        gatewaySvc.setVersion(versionBean.getVersion());
        gatewaySvc.setPublicService(versionBean.isPublicService());
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
                IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
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
                    }
                    else {
                        NewPolicyBean npb = new NewPolicyBean();
                        npb.setGatewayId(serviceGatewayBean.getGatewayId());
                        npb.setKongPluginId(policy.getKongPluginId());
                        npb.setDefinitionId(policy.getPolicyImpl());
                        npb.setEnabled(true);
                        npb.setConfiguration(policy.getPolicyJsonConfig());
                        orgFacade.doCreatePolicy(gatewaySvc.getOrganizationId(), gatewaySvc.getServiceId(), gatewaySvc.getVersion(), npb, PolicyType.Service);
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
        //Checks if service still has contracts
        try {
            if (!query.getServiceContracts(gatewaySvc.getOrganizationId(), gatewaySvc.getServiceId(), gatewaySvc.getVersion(), 1, 1000).isEmpty()) {
                throw ExceptionFactory.serviceCannotDeleteException("Service still has contracts");
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }

        // Retire the service from all relevant gateways
        try {
            Set<ServiceGatewayBean> gateways = versionBean.getGateways();
            if (gateways == null) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            for (ServiceGatewayBean serviceGatewayBean : gateways) {
                IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
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
        } catch (PublishingException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("RetireError"), e); //$NON-NLS-1$
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
        }
        catch (StorageException ex) {
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

        //TODO validate if consumer with given consumer name exists?

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

        // Validate that it's ok to perform this action - application must be Ready.
        try {
            if (!applicationValidator.isReady(versionBean)) {
                throw ExceptionFactory.actionException(Messages.i18n.format("InvalidApplicationStatus")); //$NON-NLS-1$
            }
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidApplicationStatus"), e); //$NON-NLS-1$
        }

        // Validate that all apikeys are equal for the scope of one application
        if(versionBean.getApikey() == null) throw ExceptionFactory.actionException(Messages.i18n.format("MissingAPIKey"));

        //application should have contracts when accessed directly from api.
        String appApiKey;
        if(contractBeans==null||contractBeans.size()==0)throw ExceptionFactory.actionException(Messages.i18n.format("InvalidContractCount"));
        else{
            //we are sure the contracts are not empty and that all apikeys must be equal.
            appApiKey = versionBean.getApikey();
        }

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

        // Next, register the application with *all* relevant gateways.  This is done by
        // looking up all referenced services and getting the gateway information for them.
        // Each of those gateways must be told about the application.
        try {
            Map<String, IGatewayLink> links = new HashMap<>();
            for (Contract contract : application.getContracts()) {
                ServiceVersionBean svb = storage.getServiceVersion(contract.getServiceOrgId(), contract.getServiceId(), contract.getServiceVersion());
                Set<ServiceGatewayBean> gateways = svb.getGateways();
                if (gateways == null) {
                    throw new PublishingException("No gateways specified for service: " + svb.getService().getName()); //$NON-NLS-1$
                }
                for (ServiceGatewayBean serviceGatewayBean : gateways) {
                    if (!links.containsKey(serviceGatewayBean.getGatewayId())) {
                        IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                        links.put(serviceGatewayBean.getGatewayId(), gatewayLink);
                    }
                }
            }
            for (IGatewayLink gatewayLink : links.values()) {
                // Validate that the application has a key-auth apikey available on the gateway - fallback scenario
                try {
                    String appConsumerName = ConsumerConventionUtil.createAppUniqueId(application.getOrganizationId(), application.getApplicationId(), application.getVersion());
                    gatewayLink.addConsumerKeyAuth(appConsumerName, appApiKey);
                } catch (Exception e) {
                    //apikey for consumer already exists
                }
                Map<Contract, KongPluginConfigList> response = gatewayLink.registerApplication(application);
                for (Map.Entry<Contract, KongPluginConfigList> entry : response.entrySet()) {
                    for (KongPluginConfig config : entry.getValue().getData()) {
                        NewPolicyBean npb = new NewPolicyBean();
                        npb.setGatewayId(gatewayLink.getGatewayId());
                        npb.setConfiguration(new Gson().toJson(config.getConfig()));
                        npb.setContractId(entry.getKey().getId());
                        npb.setKongPluginId(config.getId());
                        npb.setDefinitionId(GatewayUtils.convertKongPluginNameToPolicy(config.getName()).getPolicyDefId());
                        //save the policy as a contract policy on the service
                        orgFacade.doCreatePolicy(application.getOrganizationId(), application.getApplicationId(), application.getVersion(), npb, PolicyType.Contract);
                    }
                }
                gatewayLink.close();
            }
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
        }

        versionBean.setStatus(ApplicationStatus.Registered);
        versionBean.setPublishedOn(new Date());

        try {
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
                String org, id, ver;
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
                        throw new RuntimeException("Missing case for switch!"); //$NON-NLS-1$
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
                        IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
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
        for(ContractSummaryBean contractSumBean:contractBeans){
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
        if (!securityContext.hasPermission(PermissionType.planAdmin, action.getOrganizationId())) throw ExceptionFactory.notAuthorizedException();

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