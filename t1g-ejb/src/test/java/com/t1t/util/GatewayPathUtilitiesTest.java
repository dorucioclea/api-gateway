package com.t1t.util;

import com.t1t.apim.gateway.dto.Service;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by michallispashidis on 8/09/15.
 */

public class GatewayPathUtilitiesTest {
    private static final String PATH = "/orgid/servicebasepath/v1";
    private static final String ORGID = "orgid";
    private static final String BASEPATH = "servicebasepath";
    private static final String VERSION = "v1";

    @Test
    public void testGenerateGatewayContextPathByService() throws Exception {
        Service tempService = new Service();
        tempService.setOrganizationId(ORGID);
        tempService.setBasepaths(Collections.singleton(BASEPATH));
        tempService.setVersion(VERSION);
        assertEquals(PATH,GatewayPathUtilities.generateGatewayContextPath(tempService).get(0));
    }

    @Test
    public void testGenerateGatewayContextPathByStringParams() throws Exception {
        assertEquals(PATH,GatewayPathUtilities.generateGatewayContextPath(ORGID, Collections.singleton(BASEPATH), VERSION).get(0));
    }
}