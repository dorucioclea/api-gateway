package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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
    @Inject private IGatewayLinkFactory gatewayLinkFactory;
    @Inject private UserFacade userFacade;
    @Inject private RoleFacade roleFacade;
    @Inject private OrganizationFacade organizationFacade;

    public AuthConsumerBean createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria){
        //get application version
        ApplicationVersionBean avb = organizationFacade.getAppVersion(criteria.getOrgId(), criteria.getAppId(), criteria.getAppVersion());
        //verify API key
        //create consumer with optional key
        //add consumer to Appversion -> API ACLs
        //or enforce policies for consumer (IPrestriction - RateLimit - RequestSizeLimit)
        //issue apikey/existing optional key
        //return consumer
        return null;
    }

    public AuthConsumerBean getKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria){
        String gatewayId = getDefaultGateway().getId();
        IGatewayLink gatewayLink = createGatewayLink(gatewayId);

        return null;
    }
}
