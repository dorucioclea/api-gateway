package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.beans.actions.ActionBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.plans.PlanStatus;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
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
import com.t1t.digipolis.apim.core.logging.ApimanLogger;
import com.t1t.digipolis.apim.core.logging.IApimanLogger;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.rest.impl.audit.AuditUtils;
import com.t1t.digipolis.apim.rest.impl.i18n.Messages;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IOrganizationResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Implementation of the Action API.
 */
@Api(value = "/actions", description = "The Action API.  This API allows callers to perform actions on various entities - actions other than the standard REST crud actions.")
@Path("/actions")
@ApplicationScoped
public class ActionResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    IOrganizationResource orgs;

    @Inject
    IServiceValidator serviceValidator;
    @Inject
    IApplicationValidator applicationValidator;

    @Inject
    ISecurityContext securityContext;

    @Inject
    @ApimanLogger(ActionResource.class)
    IApimanLogger log;

    /**
     * Constructor.
     */
    public ActionResource() {
    }

    @ApiOperation(value = "Execute an Entity Action",
            notes = "Call this endpoint in order to execute actions for apiman entities such" +
                    " as Plans, Services, or Applications.  The type of the action must be" +
                    " included in the request payload.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void performAction(ActionBean action) throws ActionException {
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
            versionBean = orgs.getServiceVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
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
        gatewaySvc.setVersion(versionBean.getVersion());
        gatewaySvc.setPublicService(versionBean.isPublicService());
        boolean hasTx = false;
        try {
            if (versionBean.isPublicService()) {
                List<Policy> policiesToPublish = new ArrayList<>();
                List<PolicySummaryBean> servicePolicies = query.getPolicies(action.getOrganizationId(),
                        action.getEntityId(), action.getEntityVersion(), PolicyType.Service);
                storage.beginTx();
                hasTx = true;
                for (PolicySummaryBean policySummaryBean : servicePolicies) {
                    PolicyBean servicePolicy = storage.getPolicy(PolicyType.Service, action.getOrganizationId(),
                            action.getEntityId(), action.getEntityVersion(), policySummaryBean.getId());
                    Policy policyToPublish = new Policy();
                    policyToPublish.setPolicyJsonConfig(servicePolicy.getConfiguration());
                    policyToPublish.setPolicyImpl(servicePolicy.getDefinition().getPolicyImpl());
                    policiesToPublish.add(policyToPublish);
                }
                gatewaySvc.setServicePolicies(policiesToPublish);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        } finally {
            if (hasTx) {
                storage.rollbackTx();
            }
        }

        // Publish the service to all relevant gateways
        try {
            storage.beginTx();
            Set<ServiceGatewayBean> gateways = versionBean.getGateways();
            if (gateways == null) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            for (ServiceGatewayBean serviceGatewayBean : gateways) {
                IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                gatewayLink.publishService(gatewaySvc);
                gatewayLink.close();
            }

            versionBean.setStatus(ServiceStatus.Published);
            versionBean.setPublishedOn(new Date());

            storage.updateServiceVersion(versionBean);
            storage.createAuditEntry(AuditUtils.servicePublished(versionBean, securityContext));
            storage.commitTx();
        } catch (PublishingException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        } catch (Exception e) {
            storage.rollbackTx();
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
            versionBean = orgs.getServiceVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
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
            storage.beginTx();
            Set<ServiceGatewayBean> gateways = versionBean.getGateways();
            if (gateways == null) {
                throw new PublishingException("No gateways specified for service!"); //$NON-NLS-1$
            }
            for (ServiceGatewayBean serviceGatewayBean : gateways) {
                IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                gatewayLink.retireService(gatewaySvc);
                gatewayLink.close();
            }

            versionBean.setStatus(ServiceStatus.Retired);
            versionBean.setRetiredOn(new Date());

            storage.updateServiceVersion(versionBean);
            storage.createAuditEntry(AuditUtils.serviceRetired(versionBean, securityContext));
            storage.commitTx();
        } catch (PublishingException e) {
            storage.rollbackTx();
            throw ExceptionFactory.actionException(Messages.i18n.format("RetireError"), e); //$NON-NLS-1$
        } catch (Exception e) {
            storage.rollbackTx();
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

        ApplicationVersionBean versionBean = null;
        List<ContractSummaryBean> contractBeans = null;
        try {
            versionBean = orgs.getAppVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
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
            storage.beginTx();
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
                gatewayLink.registerApplication(application);
                gatewayLink.close();
            }
            storage.commitTx();
        } catch (Exception e) {
            storage.rollbackTx();
            throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
        }

        versionBean.setStatus(ApplicationStatus.Registered);
        versionBean.setPublishedOn(new Date());

        try {
            storage.beginTx();
            storage.updateApplicationVersion(versionBean);
            storage.createAuditEntry(AuditUtils.applicationRegistered(versionBean, securityContext));
            storage.commitTx();
        } catch (Exception e) {
            storage.rollbackTx();
            throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
        }

        log.debug(String.format("Successfully registered Application %s on specified gateways: %s", //$NON-NLS-1$
                versionBean.getApplication().getName(), versionBean.getApplication()));
    }

    /**
     * Aggregates the service, app, and plan policies into a single ordered list.
     *
     * @param contractBean
     */
    private List<Policy> aggregateContractPolicies(ContractSummaryBean contractBean) {
        try {
            List<Policy> policies = new ArrayList<>();
            PolicyType[] types = new PolicyType[3];
            types[0] = PolicyType.Application;
            types[1] = PolicyType.Plan;
            types[2] = PolicyType.Service;
            for (PolicyType policyType : types) {
                String org, id, ver;
                switch (policyType) {
                    case Application: {
                        org = contractBean.getAppOrganizationId();
                        id = contractBean.getAppId();
                        ver = contractBean.getAppVersion();
                        break;
                    }
                    case Plan: {
                        org = contractBean.getServiceOrganizationId();
                        id = contractBean.getPlanId();
                        ver = contractBean.getPlanVersion();
                        break;
                    }
                    case Service: {
                        org = contractBean.getServiceOrganizationId();
                        id = contractBean.getServiceId();
                        ver = contractBean.getServiceVersion();
                        break;
                    }
                    default: {
                        throw new RuntimeException("Missing case for switch!"); //$NON-NLS-1$
                    }
                }
                List<PolicySummaryBean> appPolicies = query.getPolicies(org, id, ver, policyType);
                storage.beginTx();
                try {
                    for (PolicySummaryBean policySummaryBean : appPolicies) {
                        PolicyBean policyBean = storage.getPolicy(policyType, org, id, ver, policySummaryBean.getId());
                        Policy policy = new Policy();
                        policy.setPolicyJsonConfig(policyBean.getConfiguration());
                        policy.setPolicyImpl(policyBean.getDefinition().getPolicyImpl());
                        policies.add(policy);
                    }
                } finally {
                    storage.commitTx();
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
            versionBean = orgs.getAppVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
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
            storage.beginTx();
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
            storage.commitTx();
            for (IGatewayLink gatewayLink : links.values()) {
                gatewayLink.unregisterApplication(application);
                gatewayLink.close();
            }
        } catch (Exception e) {
            storage.rollbackTx();
            throw ExceptionFactory.actionException(Messages.i18n.format("UnregisterError"), e); //$NON-NLS-1$
        }

        versionBean.setStatus(ApplicationStatus.Retired);
        versionBean.setRetiredOn(new Date());

        try {
            storage.beginTx();
            storage.updateApplicationVersion(versionBean);
            storage.createAuditEntry(AuditUtils.applicationUnregistered(versionBean, securityContext));
            storage.commitTx();
        } catch (Exception e) {
            storage.rollbackTx();
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
            versionBean = orgs.getPlanVersion(action.getOrganizationId(), action.getEntityId(), action.getEntityVersion());
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
            storage.beginTx();
            storage.updatePlanVersion(versionBean);
            storage.createAuditEntry(AuditUtils.planLocked(versionBean, securityContext));
            storage.commitTx();
        } catch (Exception e) {
            storage.rollbackTx();
            throw ExceptionFactory.actionException(Messages.i18n.format("LockError"), e); //$NON-NLS-1$
        }

        log.debug(String.format("Successfully locked Plan %s: %s", //$NON-NLS-1$
                versionBean.getPlan().getName(), versionBean.getPlan()));
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
     * @return the orgs
     */
    public IOrganizationResource getOrgs() {
        return orgs;
    }

    /**
     * @param orgs the orgs to set
     */
    public void setOrgs(IOrganizationResource orgs) {
        this.orgs = orgs;
    }

    /**
     * @return the gatewayLinkFactory
     */
    public IGatewayLinkFactory getGatewayLinkFactory() {
        return gatewayLinkFactory;
    }

    /**
     * @param gatewayLinkFactory the gatewayLinkFactory to set
     */
    public void setGatewayLinkFactory(IGatewayLinkFactory gatewayLinkFactory) {
        this.gatewayLinkFactory = gatewayLinkFactory;
    }

}
