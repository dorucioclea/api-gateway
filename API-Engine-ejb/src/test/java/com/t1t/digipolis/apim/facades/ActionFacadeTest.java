package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.actions.ActionBean;
import com.t1t.digipolis.apim.beans.actions.ActionType;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 3/09/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ActionFacade.class)
public class ActionFacadeTest {
    private static final Logger _LOG = LoggerFactory.getLogger(OrganizationFacadeTest.class.getName());
    @Rule public ExpectedException thrown = ExpectedException.none();
    @Mock EntityManager em;
    @Mock ISecurityContext securityContext;
    @Mock IStorage storage;
    @Mock IStorageQuery query;
    @Mock IIdmStorage idmStorage;
    @Mock IApiKeyGenerator apiKeyGenerator;
    @Mock IApplicationValidator applicationValidator;
    @Mock IServiceValidator serviceValidator;
    @Mock IMetricsAccessor metrics;
    @Mock GatewayFacade gatewayFacade;
    @Mock IGatewayLinkFactory gatewayLinkFactory;
    @Mock UserFacade userFacade;
    @Mock RoleFacade roleFacade;
    @InjectMocks ActionFacade actionFacade;
    @Mock OrganizationFacade orgFacade;

    @Test
    public void testPerformActionLockPlan() throws Exception {
        ActionBean actionBean = new ActionBean();
        actionBean.setEntityId("entityId");
        actionBean.setEntityVersion("version");
        actionBean.setOrganizationId("orgid");
        actionBean.setType(ActionType.lockPlan);

    }

}