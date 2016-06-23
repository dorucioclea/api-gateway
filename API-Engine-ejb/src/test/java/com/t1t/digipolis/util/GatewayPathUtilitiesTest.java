package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.gateway.dto.Service;
import org.junit.Test;

import static org.junit.Assert.*;

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
        tempService.setBasepath(BASEPATH);
        tempService.setVersion(VERSION);
        assertEquals(GatewayPathUtilities.generateGatewayContextPath(tempService), PATH);
    }

    @Test
    public void testGenerateGatewayContextPathByStringParams() throws Exception {
        assertEquals(GatewayPathUtilities.generateGatewayContextPath(ORGID,BASEPATH,VERSION),PATH);
    }
}