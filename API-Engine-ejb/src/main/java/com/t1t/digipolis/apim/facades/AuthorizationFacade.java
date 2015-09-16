package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.core.*;
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


}
