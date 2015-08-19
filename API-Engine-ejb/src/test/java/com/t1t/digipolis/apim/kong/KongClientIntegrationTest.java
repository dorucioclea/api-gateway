package com.t1t.digipolis.apim.kong;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.Plugins;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 18/08/15.
 * This test has been provide to verify the impact of a new Kong version towards the implemented Kong client for the API Engine.
 * If all tests succeeds, the Kong gateway version tested complies with the API Engine implementation.
 * It is adviced to run the tests even for a minor version upgrade.
 * The integration tests have been build to test the Kong objects generated (based upon the included schemas) and NOT on the json responses
 * from the Kong gateway. We want to be sure that the deserialized json responses are valid for the API Engine.
 *
 * It is possible that some json schema's should be updated, you can find the json schema's in the main/resources/schema folder.
 */
public class KongClientIntegrationTest {
    private static Logger log = LoggerFactory.getLogger(KongClientIntegrationTest.class.getName());
    private static KongClient kongClient;
    private static Gson gson;
    //TODO make configurable in maven test profile
    private static final String KONG_UNDER_TEST_URL = "http://apim.t1t.be:8001";//should point to the admin url:port

    @BeforeClass
    public static void setUp() throws Exception {
        RestGatewayConfigBean restConfig = new RestGatewayConfigBean();
        restConfig.setEndpoint(KONG_UNDER_TEST_URL);
        kongClient = new RestServiceBuilder().getService(restConfig,KongClient.class);
        assertNotNull(kongClient);
        gson = new Gson();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        //nothing to do at the moment :-)
    }

    @Test
    public void testGetInfo() throws Exception {
        KongInfo kongInfo = kongClient.getInfo();
        assertNotNull(kongInfo);
        print(kongInfo);
        //verify all properties are not empty
        assertTrue(!StringUtils.isEmpty(kongInfo.getHostname()));
        assertTrue(!StringUtils.isEmpty(kongInfo.getLuaVersion()));
        assertTrue(!StringUtils.isEmpty(kongInfo.getVersion()));
        assertTrue(!StringUtils.isEmpty(kongInfo.getTagline()));
        Plugins plugins = kongInfo.getPlugins();
        //normally minimum 1 plugin should be available
        assertNotNull(plugins.getAvailableOnServer());
        assertTrue(plugins.getAvailableOnServer().size()>0);
    }

    @Test
    public void testAddApi() throws Exception {
        //create new api
        KongApi api = new KongApi();
        api.setName("newapi");
        api.setPath("/testpath");
        api.setStripPath(true);
        api.setTargetUrl("http://trust1team.com");
        print(api);
        //registered api
        KongApi regApi = kongClient.addApi(api);
        assertNotNull(regApi);
        print(regApi);
        assertFalse(StringUtils.isEmpty(regApi.getId()));
        assertFalse(StringUtils.isEmpty(regApi.getName()));
        assertFalse(StringUtils.isEmpty(regApi.getPath()));
        assertFalse(StringUtils.isEmpty(regApi.getTargetUrl()));
        assertTrue(regApi.getStripPath());

        //remove api
        kongClient.deleteApi("newapi");
        try{
            kongClient.getApi("newapi");
        }catch (RetrofitError re){
            assertNotNull(re);
        }
    }

    @Test
    public void testGetApi() throws Exception {

    }

    @Test
    public void testListApis() throws Exception {

    }

    @Test
    public void testUpdateApi() throws Exception {

    }

    @Test
    public void testUpdateOrCreateApi() throws Exception {

    }

    @Test
    public void testDeleteApi() throws Exception {

    }

    @Test
    public void testCreateConsumer() throws Exception {

    }

    @Test
    public void testGetConsumer() throws Exception {

    }

    @Test
    public void testGetConsumers() throws Exception {

    }

    @Test
    public void testUpdateConsumer() throws Exception {

    }

    @Test
    public void testUpdateOrCreateConsumer() throws Exception {

    }

    @Test
    public void testDeleteConsumer() throws Exception {

    }

    @Test
    public void testGetEnabledPlugins() throws Exception {

    }

    @Test
    public void testGetPluginSchema() throws Exception {

    }

    @Test
    public void testCreatePlugin() throws Exception {

    }

    @Test
    public void testGetApiPluginList() throws Exception {

    }

    @Test
    public void testGetAllPlugins() throws Exception {

    }

    @Test
    public void testUpdatePlugin() throws Exception {

    }

    @Test
    public void testCreateOrUpdatePlugin() throws Exception {

    }

    @Test
    public void testDeletePlugin() throws Exception {

    }

    private void print(Object obj){System.out.println(gson.toJson(obj));}
}