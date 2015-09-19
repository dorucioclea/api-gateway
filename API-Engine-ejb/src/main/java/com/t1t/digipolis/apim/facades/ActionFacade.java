package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.actions.ActionBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.plans.PlanStatus;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
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
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ActionFacade {
    @Inject
    @APIEngineContext
    private Logger log;
    @Inject
    @APIEngineContext
    private EntityManager em;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private OrganizationFacade orgFacade;
    @Inject
    private IServiceValidator serviceValidator;
    @Inject
    private IApplicationValidator applicationValidator;

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
            default:
                throw ExceptionFactory.actionException("Action type not supported: " + action.getType().toString()); //$NON-NLS-1$
        }
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
            versionBean = orgFacade.getServiceVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
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
        boolean hasTx = false;
        try {
            //we don't restrict the application of service policies for only the public services
            /*if (versionBean.isPublicService()) {*/
            List<Policy> policiesToPublish = new ArrayList<>();
            List<PolicySummaryBean> servicePolicies = query.getPolicies(action.getOrganizationId(),
                    action.getEntityId(), action.getEntityVersion(), PolicyType.Service);
            hasTx = true;
            for (PolicySummaryBean policySummaryBean : servicePolicies) {
                PolicyBean servicePolicy = storage.getPolicy(PolicyType.Service, action.getOrganizationId(),
                        action.getEntityId(), action.getEntityVersion(), policySummaryBean.getId());
                Policy policyToPublish = new Policy();
                policyToPublish.setPolicyJsonConfig(servicePolicy.getConfiguration());
                policyToPublish.setPolicyImpl(servicePolicy.getDefinition().getId());
                //policyToPublish.setPolicyImpl(servicePolicy.getDefinition().getPolicyImpl());
                policiesToPublish.add(policyToPublish);
            }
            gatewaySvc.setServicePolicies(policiesToPublish);
            /*}*/
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        }
        // Publish the service to all relevant gateways
        try {
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            if (gatewayId == null || StringUtils.isEmpty(gatewayId)) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
            gatewayLink.publishService(gatewaySvc);
            gatewayLink.close();

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
            versionBean = orgFacade.getServiceVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
        } catch (ServiceVersionNotFoundException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("ServiceNotFound")); //$NON-NLS-1$
        }

        // Validate that it's ok to perform this action - service must be Ready.
        if (versionBean.getStatus() != ServiceStatus.Published) {
            throw ExceptionFactory.actionException(Messages.i18n.format("InvalidServiceStatus")); //$NON-NLS-1$
        }

        Service gatewaySvc = new Service();
        gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
        gatewaySvc.setServiceId(versionBean.getService().getId());
        gatewaySvc.setVersion(versionBean.getVersion());

        // Retire the service from all relevant gateways
        try {
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            if (gatewayId == null || StringUtils.isEmpty(gatewayId)) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
            gatewayLink.retireService(gatewaySvc);
            gatewayLink.close();

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

    /**
     * Registers an application (along with all of its contracts) to the gateway.
     *
     * @param action
     */
    private void registerApplication(ActionBean action) throws ActionException {
        if (!securityContext.hasPermission(PermissionType.appAdmin, action.getOrganizationId()))
            throw ExceptionFactory.notAuthorizedException();

        //TODO validate if consumer wit given consumer name exists

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

        Application application = new Application();
        application.setOrganizationId(versionBean.getApplication().getOrganization().getId());
        application.setApplicationId(versionBean.getApplication().getId());
        application.setVersion(versionBean.getVersion());
        application.setApplicationName(versionBean.getApplication().getName());

        Set<Contract> contracts = new HashSet<>();
        for (ContractSummaryBean contractBean : contractBeans) {
            Contract contract = new Contract();
            contract.setApiKey(contractBean.getApikey());
            contract.setPlan(contractBean.getPlanId());
            contract.setServiceId(contractBean.getServiceId());
            contract.setServiceOrgId(contractBean.getServiceOrganizationId());
            contract.setServiceVersion(contractBean.getServiceVersion());
            contract.getPolicies().addAll(aggregateContractPolicies(contractBean));
            contracts.add(contract);
        }
        application.setContracts(contracts);

        // Next, register the application with *all* relevant gateways.  This is done by
        // looking up all referenced services and getting the gateway information for them.
        // Each of those gateways must be told about the application.
        try {
            for (Contract contract : application.getContracts()) {
                //get the default gateway
                String gatewayId = gatewayFacade.getDefaultGateway().getId();
                if (gatewayId == null || StringUtils.isEmpty(gatewayId)) {
                    throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
                }
                IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
                gatewayLink.registerApplication(application);
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
                        org = contractBean.getAppOrganizationId();
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
            throw ExceptionFactory.actionException(Messages.i18n.format("PolicyPublishError", contractBean.getApikey()), e); //$NON-NLS-1$
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

        // Validate that it's ok to perform this action - application must be Ready.
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
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            if (gatewayId == null || StringUtils.isEmpty(gatewayId)) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
            gatewayLink.unregisterApplication(application);
            gatewayLink.close();
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("UnregisterError"), e); //$NON-NLS-1$
        }

        versionBean.setStatus(ApplicationStatus.Retired);
        versionBean.setRetiredOn(new Date());

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
