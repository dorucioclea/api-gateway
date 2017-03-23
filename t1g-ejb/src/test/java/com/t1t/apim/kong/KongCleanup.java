package com.t1t.apim.kong;

import com.t1t.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.kong.model.*;
import org.junit.Ignore;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by michallispashidis on 09/09/15.
 */
@Ignore("Clean up script for Kong apis and consumers - the script does not delete already generated keys. Use with care.")
public class KongCleanup {
    private static KongClient kongClient;

    //TODO make configurable in maven test profile
    private static final String KONG_UNDER_TEST_URL = "http://devapim.t1t.be:8001";//should point to the admin url:port
    //private static final String KONG_UNDER_TEST_URL = "http://localhost:8001";//should point to the admin url:port
    //private static final String API_NAME = "newapi";
    //private static final String API_PATH = "/testpath";
    //private static final String API_URL = "http://domain.com/app/rest/v1";

    public static void main(String []args){
        KongCleanup instance = new KongCleanup();
        try {
            instance.setUp();
            instance.cleanAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp() throws Exception {
        RestGatewayConfigBean restConfig = new RestGatewayConfigBean();
        restConfig.setEndpoint(KONG_UNDER_TEST_URL);
        kongClient = new KongServiceBuilder().getService(restConfig, KongClient.class);
        assertNotNull(kongClient);
    }

    public void cleanAll() throws Exception {
        //remove all consumers
        KongConsumerList consumers = kongClient.getConsumers();
        for (KongConsumer cons : consumers.getData()) {
            kongClient.deleteConsumer(cons.getId());
        }

        //remove all plugins
        final KongPluginConfigList allPlugins = kongClient.getAllPlugins();
        final List<KongPluginConfig> plugins = allPlugins.getData();
        for(KongPluginConfig kpc:plugins){
            kongClient.deletePlugin(kpc.getApiId(),kpc.getId());
        }

        //remove all apis
        KongApiList apilist = kongClient.listApis();
        List<KongApi> apis = apilist.getData();
        for(KongApi api:apis){
            kongClient.deleteApi(api.getId());
        }
    }
}
