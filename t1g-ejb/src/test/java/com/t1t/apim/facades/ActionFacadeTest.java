package com.t1t.apim.facades;

import com.t1t.apim.beans.actions.ActionBean;
import com.t1t.apim.beans.actions.ActionType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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