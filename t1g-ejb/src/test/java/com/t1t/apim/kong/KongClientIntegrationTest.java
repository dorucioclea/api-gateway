package com.t1t.apim.kong;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.t1t.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.apim.kong.adapters.KongSafeTypeAdapterFactory;
import com.t1t.kong.model.*;
import com.t1t.util.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.util.*;

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
@Ignore("Integration test in order to test Kong gateway directly on compatibility.")
public class KongClientIntegrationTest {
    private static Logger log = LoggerFactory.getLogger(KongClientIntegrationTest.class.getName());
    private static KongClient kongClient;
    private static Gson gson;
    //TODO make configurable in maven test profile
    private static final String KONG_UNDER_TEST_URL = "http://localhost:8001";//should point to the admin url:port
    private static final String API_NAME = "newapi";
    private static final String API_PATH = "/testpath";
    private static final String API_URL = "http://domain.com/app/rest/v1";
    private static final String API_URL_OAUTH_A = "http://servicea.com/endpoint";
    private static final String API_URL_OAUTH_B = "http://serviceb.com/endpoint";


    private static final String JWT_RSA_PUB_KEY_PEM = "-----BEGIN PUBLIC KEY-----\n" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjmrg7sFxRdobSZHI2Zjk\n" +
            "nrpFT/QrXDpYzUU8IMa4TOkgERtZ3OBlZdmcbyufpBn52fX9XEeH9TuB919cPxBE\n" +
            "zJ7CsReS+Wqpy9PSw+pmCiCfjHflud2uw50neX8eJxYtzHC7UN8+uA8oCKjw0I3P\n" +
            "+ECa7aW/DmMcI/5Osrixe7fPzv8CEzhTbw7A96nK2+VI/UqFWf1oDswlX8POhzLE\n" +
            "iuj7xBiubjl6N4DZnQyao8S2EgfPONJ4mrIn6TD071/tOMh1GYAwJpVCv3agRQWG\n" +
            "8MilaayrC4Z53k6dKWQS6IfU7w5bgB1+hgIzph+NMo7VY4NbJX96uoD7AoiB4o66\n" +
            "rS1jCKKyDqL0M90C1Hh7+R+yMhIkFdEGCKFGh3fl9UDGJ4FDTmo4du0CqnmwmjoV\n" +
            "fRdtyn+61ADxP6zd7n1LAqyPB4EkxukQ77K/ONLpRv2trft9oSUR1jWMq7w12WYr\n" +
            "hYVxOGSo5N4EGBJjHAyQgMCS8PXgm7N9XaNukes0YCyAL9XSBCE3n4T4BOWG9D2B\n" +
            "CD/zUvn5CFywJhug9Rw4LWt0o2GayiN3yH0pdXAsjSFTb7VivpOsW0/y6iGf0BjK\n" +
            "T8yXJEo8oPp4H2IuL4xL48mntBnVjPsItnziGCjqgHB7lqb7qyu/6+xtHgLlFoc2\n" +
            "0KBvSaDFYbbEtO4NFVrMuIECAwEAAQ==\n" +
            "-----END PUBLIC KEY-----";

    @BeforeClass
    public static void setUp() throws Exception {
        RestGatewayConfigBean restConfig = new RestGatewayConfigBean();
        restConfig.setEndpoint(KONG_UNDER_TEST_URL);
        kongClient = new KongServiceBuilder().getService(restConfig,KongClient.class);
        assertNotNull(kongClient);
        gson = new GsonBuilder().registerTypeAdapterFactory(new KongSafeTypeAdapterFactory()).create();
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
    public void testJWTHSCert()throws Exception{
        KongConsumer cons = new KongConsumer();
        cons.setUsername("malakaxyz");
        cons.setCustomId("malakaxyz");
        KongConsumer regCons = kongClient.createConsumer(cons);
        assertNotNull(regCons);
        assertEquals(cons.getUsername(), regCons.getUsername());
        assertEquals(cons.getCustomId(), regCons.getCustomId());
        //create token
        KongPluginJWTRequest jwtRequest = new KongPluginJWTRequest();
        jwtRequest.setAlgorithm(JWTUtils.JWT_HS256);
        final KongPluginJWTResponse issuedCredentials = kongClient.createConsumerJWTCredentials("malakaxyz", jwtRequest);
        assertTrue(issuedCredentials.getKey()!=null);
        assertTrue(issuedCredentials.getSecret()!=null);
        //cleanup
        kongClient.deleteConsumer(regCons.getId());
    }

    @Test
    public void testJWTRSCert()throws Exception{
        KongConsumer cons = new KongConsumer();
        cons.setUsername("malakaxyz");
        cons.setCustomId("malakaxyz");
        KongConsumer regCons = kongClient.createConsumer(cons);
        assertNotNull(regCons);
        assertEquals(cons.getUsername(), regCons.getUsername());
        assertEquals(cons.getCustomId(), regCons.getCustomId());
        //create token
        KongPluginJWTRequest jwtRequest = new KongPluginJWTRequest();
        jwtRequest.setAlgorithm(JWTUtils.JWT_RS256);
        jwtRequest.setRsaPublicKey(JWT_RSA_PUB_KEY_PEM);
        final KongPluginJWTResponse issuedCredentials = kongClient.createConsumerJWTCredentials("malakaxyz", jwtRequest);
        assertTrue(issuedCredentials.getKey()!=null);
        assertTrue(issuedCredentials.getSecret()!=null);
        //cleanup
        kongClient.deleteConsumer(regCons.getId());
    }

    @Test
    public void testGetInfo() throws Exception {
        KongInfo kongInfo = kongClient.getParsedInfo();
        assertNotNull(kongInfo);
        print(kongInfo);
        //verify all properties are not empty
        assertFalse(StringUtils.isEmpty(kongInfo.getHostname()));
        assertFalse(StringUtils.isEmpty(kongInfo.getLuaVersion()));
        assertFalse(StringUtils.isEmpty(kongInfo.getVersion()));
        assertFalse(StringUtils.isEmpty(kongInfo.getTagline()));
        assertNotNull(kongInfo.getTimers());
        assertNotNull(kongInfo.getConfiguration());
        Plugins plugins = kongInfo.getPlugins();
        //normally minimum 1 plugin should be available
        assertNotNull(plugins.getAvailableOnServer());
    }

    @Test
    public void testGetStatus() throws Exception {
        Object kongStatus = kongClient.getStatus();
        assertNotNull(kongStatus);
        print(kongStatus);
    }

    @Test
    public void testGetCluster() throws Exception {
        Object kongCluster = kongClient.getCluster();
        assertNotNull(kongCluster);
        print(kongCluster);
    }

    @Test
    public void testGetOauthTokenEnpoint()throws Exception{
        Object kongOAuth2TokenEndpoint = kongClient.getOAuth2Tokens();
        assertNotNull(kongOAuth2TokenEndpoint);
        print(kongOAuth2TokenEndpoint);
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
        regApi.getUris().forEach(uri -> assertFalse(StringUtils.isEmpty(uri)));
        assertFalse(StringUtils.isEmpty(regApi.getUpstreamUrl()));
        assertTrue(regApi.getStripUri());
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
        log.info("actual api count already configured on Kong:" + apiList.getData().size());
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
        String randomUrl = "http://www.google.com";
        KongApi api = createTestApi();
        KongApi updatedApi = kongClient.addApi(api);
        print(updatedApi);
        updatedApi.setName(randomName);
        updatedApi.getUris().add(randomPath);
        updatedApi.setUpstreamUrl(randomUrl);
        updatedApi = kongClient.updateOrCreateApi(updatedApi);
        assertNotEquals(api.getName(), updatedApi.getName());
        assertNotEquals(new TreeSet(api.getUris()), new TreeSet(updatedApi.getUris()));
        assertNotEquals(api.getUpstreamUrl(), updatedApi.getUpstreamUrl());
        assertEquals(updatedApi.getName(), randomName);
        assertTrue(updatedApi.getUris().contains(randomPath));
        assertEquals(updatedApi.getUpstreamUrl(), randomUrl);
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
        cons.setUsername("michallisxyz");
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
        log.info("actual consumer count already registered for Kong: " + consList.getData().size());
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
        log.info("Installed plugins:{}",installedPlugins);
        assertNotNull(installedPlugins);
    }

    @Test
    public void testCreatePlugin() throws Exception {
        //in order to create a plugin you should have an api registered and a consumer
        KongApi apic = createDummyApi("apic","/apic",API_URL);
        KongConsumer consumer = createDummyConsumer("123", "apicuser");
        apic = kongClient.addApi(apic);
        consumer = kongClient.createConsumer(consumer);
        //create a ratelimitation for the consumer and apply it for the api
        KongPluginConfig pluginConfig = createTestPlugin(consumer);
        pluginConfig = kongClient.createPluginConfig(apic.getId(), pluginConfig);
        print(pluginConfig);
        kongClient.deleteApi(apic.getId());
        kongClient.deleteConsumer(consumer.getId());
        //is deleted automatically => kongClient.deletePlugin(pluginConfig.getId());
    }

    @Test
    public void testCreateCorsPlugin() throws Exception {
        //in order to create a plugin you should have an api registered and a consumer
        KongApi apicors = createDummyApi("apicors","/apicors",API_URL);
        apicors = kongClient.addApi(apicors);
        //create a ratelimitation for the consumer and apply it for the api
        List<String> headers = Arrays.asList("Accept", "Accept-Version", "Content-Length", "Content-MD5", "Content-Type", "Date", "apikey");
        KongPluginCors corsConfig = new KongPluginCors()
                .withHeaders(headers);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("cors")//as an example
                .withConfig(corsConfig);
        print(pluginConfig);
        kongClient.createPluginConfig(apicors.getId(), pluginConfig);
        kongClient.deleteApi(apicors.getId());
    }

    @Test
    public void testGetKongPluginConfigList() throws Exception {
        //create api, consumer and plugin; verify list returns 1 configured plugin, add another user and config; verify total
        KongApi apic = createDummyApi("apic","/apic",API_URL);
        KongConsumer consumerA = createDummyConsumer("123", "apicusera");
        KongConsumer consumerB = createDummyConsumer("234", "apicuserb");
        apic = kongClient.addApi(apic);
        consumerA = kongClient.createConsumer(consumerA);
        consumerB = kongClient.createConsumer(consumerB);
        //create a ratelimitation for the consumer and apply it for the api
        KongPluginConfig pluginConfigA = createTestPlugin(consumerA);
        KongPluginConfig pluginConfigB = createTestPlugin(consumerB);
        pluginConfigA = kongClient.createPluginConfig(apic.getId(), pluginConfigA);
        print(pluginConfigA);
        //verify amount of plugins
        KongPluginConfigList configList = kongClient.getKongPluginConfigList(apic.getId());
        print(configList);
        assertTrue(configList.getData().size()==1);
        //add second pluginconfig
        pluginConfigB = kongClient.createPluginConfig(apic.getId(), pluginConfigB);
        print(pluginConfigB);
        configList = kongClient.getKongPluginConfigList(apic.getId());
        assertTrue(configList.getData().size() == 2);
        kongClient.deleteApi(apic.getId());
        kongClient.deleteConsumer(consumerA.getId());
        kongClient.deleteConsumer(consumerB.getId());
    }

    @Test
    public void testGetAllPlugins() throws Exception {
        KongPluginConfigList confList = kongClient.getAllPlugins();
        int initSize = confList.getTotal();
        print(confList);
        //add some api config
        KongApi apie = createDummyApi("apiexx", "/apiexx", API_URL);
        KongConsumer consumer = createDummyConsumer("123456xx", "apicuserexx");
        apie = kongClient.addApi(apie);
        consumer = kongClient.createConsumer(consumer);
        //create a ratelimitation for the consumer and apply it for the api
        KongPluginConfig pluginConfig = createTestPlugin(consumer);
        pluginConfig = kongClient.createPluginConfig(apie.getId(), pluginConfig);
        print(pluginConfig);
        //verify one has been added
        confList = kongClient.getAllPlugins();
        int sizeAfter = confList.getTotal();
        log.info("size before:{}",initSize);
        log.info("size after:{}", sizeAfter);
        kongClient.deleteApi(apie.getId());
        kongClient.deleteConsumer(consumer.getId());
        assertTrue(sizeAfter == (initSize + 1));
    }

    @Test
    public void testGetPluginInfoBack(){
        //kongClient.deleteApi("testapi1");
        //kongClient.deleteConsumer("customidrandom1");
        KongApi api1 = createDummyApi("testapi1","/testapi1",API_URL);
        KongConsumer consumer = createDummyConsumer("customidrandom1", "customidrandom1");
        api1 = kongClient.addApi(api1);
        consumer = kongClient.createConsumer(consumer);
        KongPluginConfig pluginConfig = createTestPlugin(consumer);
        pluginConfig = kongClient.createPluginConfig(api1.getId(),pluginConfig);
        print(pluginConfig);
        print(pluginConfig.getConfig().toString());
        kongClient.deleteApi(api1.getId());
        kongClient.deleteConsumer(consumer.getId());
        //my little nasty trick
        KongPluginRateLimiting resultConfig =  gson.fromJson(pluginConfig.getConfig().toString(), KongPluginRateLimiting.class);
        log.info("Resulting config:{}", resultConfig);
    }

    @Test
    public void testCreateOrUpdatePlugin() throws Exception {
        //create plugin config; read initConfig value; update value and re-read to verify
        KongApi apif = createDummyApi("apif", "/apif", API_URL);
        KongConsumer consumer = createDummyConsumer("123f", "apifuser");
        apif = kongClient.addApi(apif);
        consumer = kongClient.createConsumer(consumer);
        //create a ratelimitation for the consumer and apply it for the api
        KongPluginConfig pluginConfig = createTestPlugin(consumer);
        pluginConfig = kongClient.createPluginConfig(apif.getId(), pluginConfig);
        print(pluginConfig);
        //update
        KongPluginRateLimiting rateConfig = new KongPluginRateLimiting().withMinute(5);
        pluginConfig.setConfig(rateConfig);
        print(pluginConfig);
        kongClient.updateOrCreatePluginConfig(apif.getId(), pluginConfig);
        KongPluginConfigList configList = kongClient.getKongPluginConfigList(apif.getId());
        configList.getData().get(0);
        //TODO use generics in KongPluginConfig for value field in order to parse automatically upon receiving the configuration from kong
        kongClient.deleteApi(apif.getId());
        kongClient.deleteConsumer(consumer.getId());

    }

    @Test
    public void testDeletePlugin() throws Exception {
        //after creating api, consumer and pluginconfig, we remove to verify if the list returns empty
        KongApi apid = createDummyApi("apid","/apid",API_URL);
        KongConsumer consumer = createDummyConsumer("12345", "apicuserd");
        apid = kongClient.addApi(apid);
        consumer = kongClient.createConsumer(consumer);
        //create a ratelimitation for the consumer and apply it for the api
        KongPluginConfig pluginConfig = createTestPlugin(consumer);
        pluginConfig = kongClient.createPluginConfig(apid.getId(), pluginConfig);
        print(pluginConfig);
        KongPluginConfigList configList = kongClient.getKongPluginConfigList(apid.getId());
        print(configList);
        assertTrue(configList.getData().size() == 1);
        //delete the plugin
        kongClient.deletePlugin(pluginConfig.getApiId(), pluginConfig.getId());
        configList = kongClient.getKongPluginConfigList(apid.getId());
        print(configList);
        assertTrue(configList.getData().isEmpty());
        kongClient.deleteApi(apid.getId());
        kongClient.deleteConsumer(consumer.getId());
        //is deleted automatically => kongClient.deletePlugin(pluginConfig.getId());
    }

    @Test
    public void testDeleteApiWithPlugins()throws Exception{
        //when an api is deleted the plugin config for that API shouldn't exist anymore
        KongApi apie = createDummyApi("apif","/apif",API_URL);
        KongConsumer consumer = createDummyConsumer("1234567", "apicuserf");
        apie = kongClient.addApi(apie);
        consumer = kongClient.createConsumer(consumer);
        //create a ratelimitation for the consumer and apply it for the api
        KongPluginConfig pluginConfig = createTestPlugin(consumer);
        pluginConfig = kongClient.createPluginConfig(apie.getId(), pluginConfig);
        print(pluginConfig);
        KongPluginConfigList configList = kongClient.getKongPluginConfigList(apie.getId());
        print(configList);
        assertTrue(configList.getData().size() == 1);
        kongClient.deleteApi(apie.getId());
        //verify the config id is not in general list of plugins
        KongPluginConfigList allPolicyConfs = kongClient.getAllPlugins();
        for(KongPluginConfig config:allPolicyConfs.getData()){
            assertFalse(config.getId().equals(pluginConfig.getId()));
        }
        kongClient.deleteConsumer(consumer.getId());
    }

    @Test
    public void createConsumerKeyAuth()throws Exception{
        KongConsumer consumer = new KongConsumer().withUsername("someid");
        consumer = kongClient.createConsumer(consumer);
        KongPluginKeyAuthResponse response = kongClient.createConsumerKeyAuthCredentials(consumer.getId(), new KongPluginKeyAuthRequest());
        //get key auth and compare
        KongPluginKeyAuthResponseList responseList = kongClient.getConsumerKeyAuthCredentials(consumer.getId());
        assertTrue(responseList.getData().get(0).getKey().equals(response.getKey()));
        kongClient.deleteConsumer(consumer.getId());
    }

    @Test
    public void createConsumerMultiKeyAuth()throws Exception{
        KongConsumer consumer = new KongConsumer().withUsername("someid");
        consumer = kongClient.createConsumer(consumer);
        KongPluginKeyAuthResponse response = kongClient.createConsumerKeyAuthCredentials(consumer.getId(), new KongPluginKeyAuthRequest());
        KongPluginKeyAuthResponse response2 = kongClient.createConsumerKeyAuthCredentials(consumer.getId(), new KongPluginKeyAuthRequest().withKey("ABCABCX"));
        //get key auth and compare
        KongPluginKeyAuthResponseList responseList = kongClient.getConsumerKeyAuthCredentials(consumer.getId());
        String responseKey = responseList.getData().get(0).getKey();
        kongClient.deleteConsumerKeyAuthCredential(consumer.getId(), response.getId());
        kongClient.deleteConsumerKeyAuthCredential(consumer.getId(),response2.getId());
        kongClient.deleteConsumer(consumer.getId());
        assertTrue(responseKey.equalsIgnoreCase(response.getKey())||responseKey.equalsIgnoreCase(response2.getKey()));
    }

    @Test
    public void registerOAuthService()throws Exception{
        KongApi apioauth = createDummyApi("apioat","/apioat",API_URL);
        KongConsumer oauthConsumer = createDummyConsumer("123456789","apiuseroauth");
        apioauth = kongClient.addApi(apioauth);
        oauthConsumer = kongClient.createConsumer(oauthConsumer);
        //create an oauth config
        KongPluginConfig pluginConfig = createTestOAuthPlugin();
        pluginConfig = kongClient.createPluginConfig(apioauth.getId(),pluginConfig);
        KongPluginOAuthEnhanced enhancedOAuthValue = gson.fromJson(gson.toJson(pluginConfig.getConfig()), KongPluginOAuthEnhanced.class);
        kongClient.deleteConsumer(oauthConsumer.getId());
        kongClient.deleteApi(apioauth.getId());
        //verify the provision key is not null!
        assertNotNull(enhancedOAuthValue);
        //assertTrue(!StringUtils.isEmpty(enhancedOAuthValue.getProvisionKey()));//is not automatically generated in some versions
        assertTrue(enhancedOAuthValue.getEnableAuthorizationCode());
        assertTrue(enhancedOAuthValue.getEnableClientCredentials());
        assertTrue(enhancedOAuthValue.getEnableImplicitGrant());
        assertTrue(enhancedOAuthValue.getEnablePasswordGrant());
        assertTrue(enhancedOAuthValue.getMandatoryScope());
    }

    /**
     * Can be usefull in the near future
     * @throws Exception
     */
    @Test
    @Ignore
    public void registerOAuthCentralService()throws Exception{
        //create service identifiers
        String serviceAId = "orgA.serviceA.v1";
        String serviceBId = "orgA.serviceB.v1";

        //create 2 services for the same organization
        KongApi apiServiceA = createDummyApi(serviceAId,"/orga/servicea/v1",API_URL_OAUTH_A);
        KongApi apiServiceB = createDummyApi(serviceBId,"/orga/serviceb/v1",API_URL_OAUTH_B);
        KongApi apiOrgAuthEndpoint = createDummyApi("orgA","/orga",API_URL_OAUTH_B);
        apiServiceA = kongClient.addApi(apiServiceA);
        apiServiceB = kongClient.addApi(apiServiceB);

        //create organization oauth endpoint
        apiOrgAuthEndpoint = kongClient.addApi(apiOrgAuthEndpoint);

        //add oauth policy to services
        KongPluginConfig pluginConfigA = createTestOAuthPluginPrefixedSet(serviceAId);
        pluginConfigA = kongClient.createPluginConfig(apiServiceA.getId(),pluginConfigA);
        KongPluginConfig pluginConfigB = createTestOAuthPluginPrefixedSet(serviceBId);
        pluginConfigB = kongClient.createPluginConfig(apiServiceB.getId(),pluginConfigB);

        //create org oauth policy with consolidated scopes
        KongPluginConfig pluginConfigOrg = createTestOAuthPluginConsolidatedScopes(new ArrayList<>(Arrays.asList(serviceAId,serviceBId)));
        pluginConfigOrg = kongClient.createPluginConfig(apiOrgAuthEndpoint.getId(),pluginConfigOrg);

        //get provision keys
        gson.fromJson(gson.toJson(pluginConfigA.getConfig()),KongPluginOAuthEnhanced.class);
        gson.fromJson(gson.toJson(pluginConfigB.getConfig()),KongPluginOAuthEnhanced.class);
        gson.fromJson(gson.toJson(pluginConfigOrg.getConfig()),KongPluginOAuthEnhanced.class);

        //register consumer
        KongConsumer oauthConsumer = createDummyConsumer("someoauthapp","apimultiuserscope");
        oauthConsumer = kongClient.createConsumer(oauthConsumer);
        assertNotNull(oauthConsumer.getId());

        //create application to use oauth2 for service A and B
        KongPluginOAuthConsumerResponse kongPluginOAuthConsumerResponse = kongClient.enableOAuthForConsumer(oauthConsumer.getId(), oauthConsumer.getId(), "", "", Collections.singleton("http://localhost:5000"));

        //keys should be generated
        String client_id = kongPluginOAuthConsumerResponse.getClientId();
        assertNotNull(client_id);
        String client_secret = kongPluginOAuthConsumerResponse.getClientSecret();
        assertNotNull(client_secret);

        kongClient.deleteConsumer(oauthConsumer.getId());
        kongClient.deleteApi(apiServiceA.getId());
        kongClient.deleteApi(apiServiceB.getId());
        kongClient.deleteApi(apiOrgAuthEndpoint.getId());
    }

    @Test
    public void registerOAuthServiceWithManyScopes()throws Exception{
        KongApi apioauth = createDummyApi("apioatx","/apioatx",API_URL);
        KongConsumer oauthConsumer = createDummyConsumer("123456789x","apiuseroauthx");
        apioauth = kongClient.addApi(apioauth);
        oauthConsumer = kongClient.createConsumer(oauthConsumer);
        //create an oauth config
        KongPluginConfig pluginConfig = createTestOAuthPluginWithManyScopes();
        pluginConfig = kongClient.createPluginConfig(apioauth.getId(),pluginConfig);
        KongPluginOAuthEnhanced enhancedOAuthValue = gson.fromJson(gson.toJson(pluginConfig.getConfig()),KongPluginOAuthEnhanced.class);
        kongClient.deleteConsumer(oauthConsumer.getId());
        kongClient.deleteApi(apioauth.getId());
        //verify the provision key is not null!
        assertNotNull(enhancedOAuthValue);
        //assertTrue(!StringUtils.isEmpty(enhancedOAuthValue.getProvisionKey()));//is not automatically generated in some versions
        assertTrue(enhancedOAuthValue.getEnableAuthorizationCode());
        assertTrue(enhancedOAuthValue.getEnableClientCredentials());
        assertTrue(enhancedOAuthValue.getEnableImplicitGrant());
        assertTrue(enhancedOAuthValue.getEnablePasswordGrant());
        assertTrue(enhancedOAuthValue.getMandatoryScope());
    }

    @Test
    public void enableOAuthForConsumer()throws Exception{
        KongConsumer consumer = new KongConsumer().withUsername("oauthconsumer1");
        consumer = kongClient.createConsumer(consumer);
        KongPluginOAuthConsumerResponse response = kongClient.enableOAuthForConsumer(consumer.getId(),"TestApplication","ABCCLIENTID","ABCCLIENTSECRET", Collections.singleton("http://localhost:4000/"));
        assertTrue(response!=null);
        kongClient.deleteConsumer(consumer.getId());
    }

    @Test
    public void testACLPluginLifecycle() throws Exception {
        //Create API you'll add ACL plugin to and consumer you'll add to group
        KongApi api = createDummyApi(API_NAME, API_PATH, API_URL);
        KongConsumer consumer = new KongConsumer().withUsername("aclconsumer");
        api = kongClient.addApi(api);
        consumer = kongClient.createConsumer(consumer);
        //Create plugin
        KongPluginConfig config = kongClient.createPluginConfig(api.getId(), createTestACLPlugin());
        //Apply to consumer
        KongPluginACLResponse response = kongClient.addConsumerToACL(consumer.getId(), new KongPluginACLRequest()
                .withGroup("org.serv.version"));
        //Delete consumer plugin
        Object deleteConsumerPluginResponse = kongClient.deleteConsumerACLEntry(consumer.getId(), response.getId());
        //Delete API plugin
        Object deleteAPIPluginResponse = kongClient.deletePlugin(api.getId(), config.getId());
        //Clean up
        kongClient.deleteConsumer(consumer.getId());
        kongClient.deleteApi(api.getId());
        //Assertions
        assertNotNull(config);
        assertNotNull(response);
        assertNull(deleteConsumerPluginResponse);
        assertNull(deleteAPIPluginResponse);
    }

    @Test
    public void addConsumerTo1000ACLs() {
        KongConsumer consumer = new KongConsumer().withUsername("misterpopular");
        consumer = kongClient.createConsumer(consumer);
        String acl = "orgid.archid.v%d";
        List<KongPluginACLResponse> responses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            responses.add(kongClient.addConsumerToACL(consumer.getId(), new KongPluginACLRequest().withGroup(String.format(acl, i))));
        }
        kongClient.deleteConsumer(consumer.getId());
        for (KongPluginACLResponse response : responses) {
            assertNotNull(response);
        }
    }

    @Test(expected = RetrofitError.class)
    public void getNonExistingConsumer(){
        kongClient.getConsumer("nonexistingid");
    }

    /**
     * Utility method, if used the api is automatically removed after the test.
     * @return
     */
    private KongApi createTestApi(){
        return createDummyApi(API_NAME, API_PATH, API_URL);
    }

    private KongApi createDummyApi(String name, String path, String url){
        //create new api
        KongApi api = new KongApi();
        api.setName(name);
        api.setUris(Collections.singletonList(path));
        api.setStripUri(true);
        api.setUpstreamUrl(url);
        print(api);
        return api;
    }

    private KongConsumer createTestConsumer(){
        KongConsumer cons = new KongConsumer();
        cons.setUsername("michallisxyw");
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

    private KongPluginConfig createTestPlugin(KongConsumer consumer) {
        //create config value - 1 request/minute
        KongPluginRateLimiting rateLimitingConfig = new KongPluginRateLimiting()
                .withMinute(1);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withConsumerId(consumer.getId())
                .withName("rate-limiting")//as an example
                .withConfig(rateLimitingConfig);
        print(pluginConfig);
        return pluginConfig;
    }

    private KongPluginConfig createTestACLPlugin() {
        KongPluginACL kongPluginACL = new KongPluginACL()
                .withWhitelist(Arrays.asList("orgid.serv.version"));
        KongPluginConfig config = new KongPluginConfig()
                .withName("acl")
                .withConfig(kongPluginACL);
        print(config);
        return config;
    }

    private KongPluginConfig createTestOAuthPlugin(){
        List<Object> scopes = new ArrayList<>(Arrays.asList("basic","extended","full"));
        KongPluginOAuthEnhanced oAuthEnhancedConfig = new KongPluginOAuthEnhanced()
                .withEnableAuthorizationCode(true)
                .withEnableClientCredentials(true)
                .withEnableImplicitGrant(true)
                .withEnablePasswordGrant(true)
                .withHideCredentials(false)
                .withMandatoryScope(true)
                .withScopes(scopes);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("oauth2")//as an example
                .withConfig(oAuthEnhancedConfig);
        print(pluginConfig);
        return pluginConfig;
    }

    private KongPluginConfig createTestOAuthPluginWithManyScopes(){
        String[] randomScopes = generateRandomWords(10000);
        List<Object> scopes = new ArrayList<>(Arrays.asList(randomScopes));
        KongPluginOAuthEnhanced oAuthEnhancedConfig = new KongPluginOAuthEnhanced()
                .withEnableAuthorizationCode(true)
                .withEnableClientCredentials(true)
                .withEnableImplicitGrant(true)
                .withEnablePasswordGrant(true)
                .withHideCredentials(false)
                .withMandatoryScope(true)
                .withScopes(scopes);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("oauth2")//as an example
                .withConfig(oAuthEnhancedConfig);
        print(pluginConfig);
        return pluginConfig;
    }

    private KongPluginConfig createTestOAuthPluginPrefixedSet(String serviceprefix){
        List<Object> scopes = new ArrayList<>(Arrays.asList(serviceprefix+".basic",serviceprefix+".extended",serviceprefix+".full"));
        KongPluginOAuthEnhanced oAuthEnhancedConfig = new KongPluginOAuthEnhanced()
                .withEnableAuthorizationCode(true)
                .withEnableClientCredentials(true)
                .withEnableImplicitGrant(true)
                .withEnablePasswordGrant(true)
                .withHideCredentials(false)
                .withMandatoryScope(true)
                .withScopes(scopes);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("oauth2")//as an example
                .withConfig(oAuthEnhancedConfig);
        print(pluginConfig);
        return pluginConfig;
    }

    private KongPluginConfig createTestOAuthPluginConsolidatedScopes(List<String> prefixList){
        List<Object> scopes = new ArrayList<>();
        prefixList.stream().forEach(prefix -> {
            scopes.add(prefix+".basic");
            scopes.add(prefix+".extended");
            scopes.add(prefix+".full");
        });
        KongPluginOAuthEnhanced oAuthEnhancedConfig = new KongPluginOAuthEnhanced()
                .withEnableAuthorizationCode(true)
                .withEnableClientCredentials(true)
                .withEnableImplicitGrant(true)
                .withEnablePasswordGrant(true)
                .withHideCredentials(false)
                .withMandatoryScope(true)
                .withScopes(scopes);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("oauth2")//as an example
                .withConfig(oAuthEnhancedConfig);
        print(pluginConfig);
        return pluginConfig;
    }


    @Test
    public void testCreateIPRestrictionPluginWL() throws Exception {
        //in order to create a plugin you should have an api registered and a consumer
        KongApi apiipr = createDummyApi("apiiprwl","/apiiprwl",API_URL);
        apiipr = kongClient.addApi(apiipr);
        //create a IPRestrcition policy
        List<String> whitelistrecords = new ArrayList<>();
        whitelistrecords.add("127.0.0.0/24");
        whitelistrecords.add("128.0.0.0/24");
        List<String> blacklistrecords = new ArrayList<>();
        blacklistrecords.add("32.0.0.0/24");
        blacklistrecords.add("33.0.0.0/24");
        KongPluginIPRestriction iprConfig = new KongPluginIPRestriction()
                .withWhitelist(whitelistrecords);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("ip-restriction")//as an example
                .withConfig(iprConfig);
        log.info(gson.toJson(pluginConfig));
        kongClient.createPluginConfig(apiipr.getId(), pluginConfig);
        kongClient.deleteApi(apiipr.getId());
    }

    @Test
    public void testCreateIPRestrictionPluginBL() throws Exception {
        //in order to create a plugin you should have an api registered and a consumer
        KongApi apiipr = createDummyApi("apiiprb","/apiiprb",API_URL);
        apiipr = kongClient.addApi(apiipr);
        //create a IPRestrcition policy
        List<String> whitelistrecords = new ArrayList<>();
        whitelistrecords.add("127.0.0.0/24");
        whitelistrecords.add("128.0.0.0/24");
        List<String> blacklistrecords = new ArrayList<>();
        blacklistrecords.add("32.0.0.0/24");
        blacklistrecords.add("33.0.0.0/24");
        KongPluginIPRestriction iprConfig = new KongPluginIPRestriction()
                .withWhitelist(blacklistrecords);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("ip-restriction")//as an example
                .withConfig(iprConfig);
        log.info(gson.toJson(pluginConfig));
        kongClient.createPluginConfig(apiipr.getId(), pluginConfig);
        kongClient.deleteApi(apiipr.getId());
    }

    @Test
    public void testCreateIPRestrictionPluginWithJSONConvertionWL() throws Exception {
        //in order to create a plugin you should have an api registered and a consumer
        KongApi apiipr = createDummyApi("apiiprjsonw","/apiiprjsonw",API_URL);
        String iprestrictionValues = "{\"whitelist\":[\"127.0.0.0/24\"]}";
        KongPluginIPRestriction kongPluginIPRestriction = gson.fromJson(iprestrictionValues, KongPluginIPRestriction.class);
        apiipr = kongClient.addApi(apiipr);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("ip-restriction")//as an example
                .withConfig(kongPluginIPRestriction);
        print(pluginConfig);
        kongClient.createPluginConfig(apiipr.getId(), pluginConfig);
        kongClient.deleteApi(apiipr.getId());
    }

    @Test
    public void testCreateIPRestrictionPluginWithJSONConvertionBL() throws Exception {
        //in order to create a plugin you should have an api registered and a consumer
        KongApi apiipr = createDummyApi("apiiprjsonb","/apiiprjsonb",API_URL);
        String iprestrictionValues = "{\"blacklist\":[\"127.0.0.0/24\"]}";
        KongPluginIPRestriction kongPluginIPRestriction = gson.fromJson(iprestrictionValues, KongPluginIPRestriction.class);
        apiipr = kongClient.addApi(apiipr);
        KongPluginConfig pluginConfig = new KongPluginConfig()
                .withName("ip-restriction")//as an example
                .withConfig(kongPluginIPRestriction);
        print(pluginConfig);
        kongClient.createPluginConfig(apiipr.getId(), pluginConfig);
        kongClient.deleteApi(apiipr.getId());
    }

    @Test
    public void testGetKongParsedInfo() throws Exception {
        KongInfo parsedInfo = kongClient.getParsedInfo();
        assertNotNull(parsedInfo);
    }

    @Test
    public void testKongInfo() throws Exception {
        KongExtraInfo extraInfo = kongClient.getInfo();
        assertNotNull(extraInfo);
    }

    public static String[] generateRandomWords(int numberOfWords) {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    private void print(Object obj){log.info(gson.toJson(obj));};
}