package com.t1t.digipolis.apim.kong;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.kong.model.*;
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
 * It is advized to run the tests even for a minor version upgrade.
 * The integration tests have been build to test the Kong objects generated (based upon the included schemas) and NOT on the json responses
 * from the Kong gateway. We want to be sure that the serialization/deserialization of json responses are valid for the API Engine.
 *
 * It is possible that some json schema's should be updated, you can find the json schema's in the main/resources/schema folder.
 */
public class KongClientIntegrationTest {
    private static Logger log = LoggerFactory.getLogger(KongClientIntegrationTest.class.getName());
    private static KongClient kongClient;
    private static Gson gson;
    //TODO make configurable in maven test profile
    private static final String KONG_UNDER_TEST_URL = "http://apim.t1t.be:8001";//should point to the admin url:port
    private static final String API_NAME = "newapi";
    private static final String API_PATH = "/testpath";
    private static final String API_URL = "http://trust1team.com";

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

    @After
    public void cleanupApi()throws Exception{
        try{
            kongClient.deleteApi(API_NAME);
        }catch (RetrofitError re){;}
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
    public void testApiFunctionality() throws Exception {
        //create new api
        KongApi api = createTestApi();
        //registered api
        KongApi regApi = kongClient.addApi(api);
        assertNotNull(regApi);
        print(regApi);
        assertFalse(StringUtils.isEmpty(regApi.getId()));
        assertFalse(StringUtils.isEmpty(regApi.getName()));
        assertFalse(StringUtils.isEmpty(regApi.getPath()));
        assertFalse(StringUtils.isEmpty(regApi.getTargetUrl()));
        assertTrue(regApi.getStripPath());
    }

    @Test
    public void testGetApi() throws Exception {
        KongApi api = createTestApi();
        kongClient.addApi(api);
        KongApi resultApi = kongClient.getApi(API_NAME);
        assertNotNull(resultApi);
        assertEquals(resultApi.getName(), api.getName());
    }

    @Test
    public void testListApis() throws Exception {
        //get the actual api count
        KongApiList apiList = kongClient.listApis();
        assertNotNull(apiList);
        print(apiList);
        System.out.println("actual api count already configured on Kong:" + apiList.getData().size());
        KongApi apiA = createDummyApi("apia", "/apia", API_URL);
        KongApi apiB = createDummyApi("apib","/apib",API_URL);
        kongClient.addApi(apiA);
        kongClient.addApi(apiB);
        KongApiList apiUpdatedList = kongClient.listApis();
        assertNotNull(apiUpdatedList);
        print(apiUpdatedList);
        assertTrue(apiUpdatedList.getData().size() == apiList.getData().size() + 2);
        //cleanup
        kongClient.deleteApi(apiA.getName());
        kongClient.deleteApi(apiB.getName());
    }

    @Test
    public void testUpdateOrCreateApi() throws Exception {
        String randomName = "randomname";
        String randomPath="/somerandompath";
        String randomUrl = "http://www.google.com/";
        KongApi api = createTestApi();
        KongApi updatedApi = kongClient.addApi(api);
        print(updatedApi);
        updatedApi.setName(randomName);
        updatedApi.setPath(randomPath);
        updatedApi.setTargetUrl(randomUrl);
        updatedApi = kongClient.updateOrCreateApi(updatedApi);
        assertNotEquals(api.getName(), updatedApi.getName());
        assertNotEquals(api.getPath(), updatedApi.getPath());
        assertNotEquals(api.getTargetUrl(), updatedApi.getTargetUrl());
        assertEquals(updatedApi.getName(), randomName);
        assertEquals(updatedApi.getPath(), randomPath);
        assertEquals(updatedApi.getTargetUrl(), randomUrl);
        //clean up
        kongClient.deleteApi(updatedApi.getName());
    }

    @Test(expected = RetrofitError.class)
    public void testDeleteApi() throws Exception {
        KongApi api = createTestApi();
        KongApi regApi = kongClient.addApi(api);
        assertNotNull(regApi);
        kongClient.deleteApi(regApi.getName());
        regApi = kongClient.getApi(API_NAME);
    }

    @Test
    public void testCreateConsumer() throws Exception {
        KongConsumer cons = new KongConsumer();
        cons.setUsername("michallis");
        cons.setCustomId("extid");
        KongConsumer regCons = kongClient.createConsumer(cons);
        assertNotNull(regCons);
        assertEquals(cons.getUsername(), regCons.getUsername());
        assertEquals(cons.getCustomId(), regCons.getCustomId());
        //cleanup
        kongClient.deleteConsumer(regCons.getId());
    }

    @Test
    public void testGetConsumer() throws Exception {
        KongConsumer cons = createTestConsumer();
        KongConsumer regCons = kongClient.createConsumer(cons);
        assertNotNull(regCons);
        //cleanup
        kongClient.deleteConsumer(regCons.getId());
    }

    @Test
    public void testGetConsumers() throws Exception {
        KongConsumerList consList = kongClient.getConsumers();
        System.out.println("actual consumer count already registered for Kong: " + consList.getData().size());
        KongConsumer consA = createDummyConsumer("1", "ConsumerA");
        KongConsumer consB = createDummyConsumer("2", "ConsumerB");
        consA = kongClient.createConsumer(consA);
        consB = kongClient.createConsumer(consB);
        KongConsumerList updatedList = kongClient.getConsumers();
        assertNotNull(updatedList);
        assertTrue(updatedList.getData().size() == consList.getData().size() + 2);
        //clean up
        kongClient.deleteConsumer(consA.getId());
        kongClient.deleteConsumer(consB.getId());
    }

    @Test
    public void testUpdateConsumer() throws Exception {
        String randomName="randomname";
        String randomExtId="randomExtId";
        KongConsumer cons = createTestConsumer();
        KongConsumer regCons = kongClient.createConsumer(cons);
        regCons.setUsername(randomName);
        regCons.setCustomId(randomExtId);
        KongConsumer updatedCons = kongClient.updateOrCreateConsumer(regCons);
        assertNotNull(updatedCons);
        assertEquals(updatedCons.getCustomId(), randomExtId);
        assertEquals(updatedCons.getUsername(), randomName);
        //cleanup
        kongClient.deleteConsumer(updatedCons.getId());
    }

    @Test(expected = RetrofitError.class)
    public void testDeleteConsumer() throws Exception {
        KongConsumer cons = createTestConsumer();
        KongConsumer regCons = kongClient.createConsumer(cons);
        kongClient.deleteConsumer(regCons.getId());
        kongClient.getConsumer(regCons.getId());
    }

    @Test
    public void testGetInstalledPlugins() throws Exception {
        KongInstalledPlugins installedPlugins = kongClient.getInstalledPlugins();
        assertNotNull(installedPlugins);
        assertTrue(installedPlugins.getEnabledPlugins().size()>0);
    }

    @Test
    public void testCreatePlugin() throws Exception {

    }

    @Test
    public void testGetKongPluginConfigList() throws Exception {

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

    private KongApi createTestApi(){
        //create new api
        KongApi api = new KongApi();
        api.setName(API_NAME);
        api.setPath(API_PATH);
        api.setStripPath(true);
        api.setTargetUrl(API_URL);
        print(api);
        return api;
    }

    private KongApi createDummyApi(String name, String path, String url){
        //create new api
        KongApi api = new KongApi();
        api.setName(name);
        api.setPath(path);
        api.setStripPath(true);
        api.setTargetUrl(url);
        print(api);
        return api;
    }

    private KongConsumer createTestConsumer(){
        KongConsumer cons = new KongConsumer();
        cons.setUsername("michallis");
        cons.setCustomId("extid");
        print(cons);
        return cons;
    }

    private KongConsumer createDummyConsumer(String customId, String customName){
        KongConsumer cons = new KongConsumer();
        cons.setUsername(customName);
        cons.setCustomId(customId);
        print(cons);
        return cons;
    }
    private void print(Object obj){System.out.println(gson.toJson(obj));};
}