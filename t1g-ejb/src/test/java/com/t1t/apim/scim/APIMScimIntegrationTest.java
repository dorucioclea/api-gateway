package com.t1t.apim.scim;

import com.t1t.apim.beans.scim.SCIMConfigBean;
import com.t1t.kong.model.SCIMUserList;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by michallispashidis on 9/10/15.
 */
@Ignore
//TODO certificate issue - run manually
public class APIMScimIntegrationTest {
    private static final Logger _LOG = LoggerFactory.getLogger(APIMScimIntegrationTest.class);
    @Test
    public void testSCIMRequest(){
        SCIMConfigBean scimConfigBean = new SCIMConfigBean();
        scimConfigBean.setEndpoint("https://devidp.t1t.be:9443/wso2/scim");
        scimConfigBean.setUsername("admin");
        scimConfigBean.setPassword("admin");
        SCIMServiceBuilder scimServiceBuilder = new SCIMServiceBuilder();
        SCIMClient scimClient = scimServiceBuilder.getService(scimConfigBean,SCIMClient.class);
        SCIMUserList userNameEqMichallis = scimClient.getUserInformation("userNameEqMichallis");
        _LOG.debug("User returned:{}",userNameEqMichallis);
    }
}
