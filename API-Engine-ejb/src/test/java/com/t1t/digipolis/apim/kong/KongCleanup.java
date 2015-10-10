package com.t1t.digipolis.apim.kong;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongApiList;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongConsumerList;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

/**
 * Created by michallispashidis on 09/09/15.
 */
@Ignore
public class KongCleanup {
    private static Logger log = LoggerFactory.getLogger(KongCleanup.class.getName());
    private static KongClient kongClient;
    private static Gson gson;
    //TODO make configurable in maven test profile
    private static final String KONG_UNDER_TEST_URL = "http://apim.t1t.be:8001";//should point to the admin url:port
    //private static final String KONG_UNDER_TEST_URL = "http://localhost:8001";//should point to the admin url:port
    private static final String API_NAME = "newapi";
    private static final String API_PATH = "/testpath";
    private static final String API_URL = "http://domain.com/app/rest/v1";

    @BeforeClass
    public static void setUp() throws Exception {
        RestGatewayConfigBean restConfig = new RestGatewayConfigBean();
        restConfig.setEndpoint(KONG_UNDER_TEST_URL);
        kongClient = new RestServiceBuilder().getService(restConfig, KongClient.class);
        assertNotNull(kongClient);
        gson = new Gson();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        //nothing to do at the moment :-)
    }

    @After
    public void cleanupApi() throws Exception {
        //nothing at the moment
    }

    @Test
    public void cleanAll() throws Exception {
        //remove all consumers
        KongConsumerList consumers = kongClient.getConsumers();
        for (KongConsumer cons : consumers.getData()) {
            kongClient.deleteConsumer(cons.getId());
        }

        //remove all apis
        KongApiList apilist = kongClient.listApis();
        for (KongApi api : apilist.getData()) {
            kongClient.deleteApi(api.getId());
        }
    }
}
