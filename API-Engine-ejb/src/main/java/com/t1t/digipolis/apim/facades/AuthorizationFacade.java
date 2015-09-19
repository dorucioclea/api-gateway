package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicySummaryBean;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.qualifier.APIEngineContext;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by michallispashidis on 09/09/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AuthorizationFacade {
    @Inject
    @APIEngineContext
    private Logger log;
    @Inject @APIEngineContext private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private IIdmStorage idmStorage;
    @Inject private IApiKeyGenerator apiKeyGenerator;
    @Inject private IApplicationValidator applicationValidator;
    @Inject private IServiceValidator serviceValidator;
    @Inject private IMetricsAccessor metrics;
    @Inject private GatewayFacade gatewayFacade;
    @Inject private UserFacade userFacade;
    @Inject private RoleFacade roleFacade;
    @Inject private OrganizationFacade organizationFacade;
    private static IGatewayLink gatewayLink;

    public AuthConsumerBean createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria){
        //get application version
        ApplicationVersionBean avb = organizationFacade.getAppVersion(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion());
        //verify API key and select contract
        List<ContractSummaryBean> appContracts = organizationFacade.getApplicationVersionContracts(criteria.getOrgId(),criteria.getAppId(),criteria.getAppVersion());
        if(!isApiKeyValid(appContracts,criteria.getContractApiKey()))throw new NotAuthorizedException("wrong API key");
        //create consumer with optional key - and verify the consumer doesn't exist
        String consumerUniqueId = ConsumerConventionUtil.createAppConsumerUnqiueId(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion(), criteria.getCustomId());
        KongConsumer appConsumer = getGateway().createConsumer(consumerUniqueId,criteria.getCustomId());
        //create apikey
        KongPluginKeyAuthResponse authResponse = getGateway().addConsumerKeyAuth(appConsumer.getId(),criteria.getOptionalKey());//optional key = app generated appConsumer API key
        //add consumer to Appversion -> API ACLs for all services used in application - at the moment only providing key auth and applying plans
        //or enforce plan policies for consumer (IPrestriction - RateLimit - RequestSizeLimit)
        Application gtwApp = new Application();
        gtwApp.setOrganizationId(criteria.getOrgId());
        gtwApp.setApplicationId(criteria.getAppId());
        gtwApp.setApplicationName(avb.getApplication().getName());
        gtwApp.setVersion(criteria.getAppVersion());

        Set<Contract> contracts = new HashSet<>();
        for (ContractSummaryBean contractBean : appContracts) {
            Contract contract = new Contract();
            contract.setApiKey(contractBean.getApikey());
            contract.setPlan(contractBean.getPlanId());
            contract.setServiceId(contractBean.getServiceId());
            contract.setServiceOrgId(contractBean.getServiceOrganizationId());
            contract.setServiceVersion(contractBean.getServiceVersion());
            contract.getPolicies().addAll(aggregateContractPolicies(contractBean));
            contracts.add(contract);
        }
        gtwApp.setContracts(contracts);
        try {
            getGateway().registerAppConsumer(gtwApp,appConsumer);
        } catch (GatewayAuthenticationException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("RegisterError"), e); //$NON-NLS-1$
        }
        //return consumer
        AuthConsumerBean resConsumer = new AuthConsumerBean();
        resConsumer.setCustomId(appConsumer.getCustomId());
        resConsumer.setUserId(appConsumer.getUsername());
        resConsumer.setToken(authResponse.getKey());
        return resConsumer;
    }

    public AuthConsumerBean getKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria){
        //verify API key and select contract
        List<ContractSummaryBean> appContracts = organizationFacade.getApplicationVersionContracts(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion());
        if(!isApiKeyValid(appContracts,criteria.getContractApiKey()))throw new NotAuthorizedException("wrong API key");
        //generate unique id
        String consumerUniqueId = ConsumerConventionUtil.createAppConsumerUnqiueId(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion(), criteria.getCustomId());
        KongConsumer appConsumer = getGateway().getConsumer(consumerUniqueId);
        KongPluginKeyAuthResponseList keyAuthList = getGateway().getConsumerKeyAuth(consumerUniqueId);
        //consumer is registered for all services with the same API Key - thus get one example service for this consumer and retrieve the key.
        //KongApi api = getGateway().getApi(ServiceConventionUtil.generateServiceUniqueName(criteria.getOrgId(),appContracts.get(0).getServiceId(),appContracts.get(0).getServiceVersion()));
        AuthConsumerBean resConsumer = new AuthConsumerBean();
        if(keyAuthList!=null&&keyAuthList.getData().size()>0){
            resConsumer.setCustomId(criteria.getCustomId());
            resConsumer.setUserId(appConsumer.getUsername());
            resConsumer.setToken(keyAuthList.getData().get(0).getKey());
        }
        return resConsumer;
    }

    private IGatewayLink getGateway(){
        //create the gateway
        if(gatewayLink == null){
            try {
                String gatewayId = gatewayFacade.getDefaultGateway().getId();
                gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
            } catch (StorageException e) {
                e.printStackTrace();
            }
            if(gatewayLink==null)throw new GatewayNotFoundException("Default gateway not found");
        }
        return gatewayLink;
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

    private boolean isApiKeyValid(List<ContractSummaryBean> contracts,String apiKey){
        boolean apiValid = false;
        ContractBean selectedContract = null;
        for (ContractSummaryBean contract:contracts){
            if(contract.getApikey().equals(apiKey)){
                apiValid = true;
            }
        }
        return apiValid;
    }
}
