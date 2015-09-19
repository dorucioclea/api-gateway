package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

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

    public AuthConsumerBean createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria){
        //get application version
        ApplicationVersionBean avb = organizationFacade.getAppVersion(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion());
        //verify API key and select contract
        List<ContractSummaryBean> appContracts = organizationFacade.getApplicationVersionContracts(criteria.getOrgId(),criteria.getAppId(),criteria.getAppVersion());
        boolean apiValid = false;
        ContractBean selectedContract = null;
        for (ContractSummaryBean contract:appContracts){
            if(contract.getApikey().equals(criteria.getContractApiKey())){
                apiValid=true;
                try {
                    selectedContract = storage.getContract(contract.getContractId());
                } catch (StorageException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!apiValid || selectedContract==null)throw new NotAuthorizedException("wrong API key");
        //create consumer with optional key - and verify the consumer doesn't exist
        String consumerUniqueId = ConsumerConventionUtil.createAppConsumerUnqiueId(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion(), criteria.getUserId());
        //add consumer to Appversion -> API ACLs for all services used in application - at the moment only providing key auth and applying plans
        //or enforce plan policies for consumer (IPrestriction - RateLimit - RequestSizeLimit)
        //issue apikey/existing optional key
        //return consumer
        return null;
    }

    public AuthConsumerBean getKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria){

        return null;
    }

    private IGatewayLink getGateway(){
        //create the gateway
        IGatewayLink gatewayLink = null;
        try {
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        if(gatewayLink==null)throw new GatewayNotFoundException("Default gateway not found");
        else return gatewayLink;
    }
}
