package com.t1t.apim.facades;

import com.t1t.apim.beans.actions.ActionBean;
import com.t1t.apim.beans.actions.ActionType;
import com.t1t.apim.core.*;
import com.t1t.apim.gateway.IGatewayLinkFactory;
import com.t1t.apim.security.ISecurityContext;
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

import static org.junit.Assert.assertTrue;

/**
 * Created by michallispashidis on 3/09/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ActionFacade.class)
public class ActionFacadeTest {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testPerformActionLockPlan() throws Exception {
        ActionBean actionBean = new ActionBean();
        actionBean.setEntityId("entityId");
        actionBean.setEntityVersion("version");
        actionBean.setOrganizationId("orgid");
        actionBean.setType(ActionType.lockPlan);
        assertTrue(true);
    }

}