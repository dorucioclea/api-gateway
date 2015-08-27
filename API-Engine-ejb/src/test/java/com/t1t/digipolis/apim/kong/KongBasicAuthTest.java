package com.t1t.digipolis.apim.kong;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.Plugins;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by michallispashidis on 20/08/15.
 * This test performs basic authentication tests for a registered API. This uses a jetty local server in order to setup an endpoint.
 * <ul>
 * <li>Create API</li>
 * <li>Enable Basic auth on API</li>
 * <li>Create consumer</li>
 * <li>Create user name and pwd for user</li>
 * <li>Send request with basic auth header for user towards request</li>
 * </ul>
 * TODO at this moment a user can use his credentials for all api's, ACL will be activated in the 0.5 release of Kong
 */
public class KongBasicAuthTest {
    private static Logger log = LoggerFactory.getLogger(KongBasicAuthTest.class.getName());
    private static KongClient kongClient;
    private static Gson gson;
    //TODO make configurable in maven test profile
    private static final String KONG_UNDER_TEST_URL = "http://apim.t1t.be:8001";//should point to the admin url:port
    //private static final String KONG_UNDER_TEST_URL = "http://localhost:8001";//should point to the admin url:port
    private static final String API_NAME = "newapibasicauth";
    private static final int JETTY_SERVER_HTTP_PORT = 8089;
    private static final int JETTY_SERVER_HTTPS_PORT = 8489;
    private static final String API_PATH = "/testbasicauthpath";
    private static final String API_URL = "http://domain.com/app/rest/v1";

/*  If you want to use the same server instance for all tests:
    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(wireMockConfig()
            .port(JETTY_SERVER_HTTP_PORT)
            .httpsPort(JETTY_SERVER_HTTPS_PORT));
    @Rule
    public WireMockClassRule instanceRule = wireMockRule;*/

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .port(JETTY_SERVER_HTTP_PORT)
            .httpsPort(JETTY_SERVER_HTTPS_PORT));

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
        try {
            kongClient.deleteApi(API_NAME);
        } catch (RetrofitError re) {
            ;
        }
    }

    /**
     * Test if the Kong server is reachable, by requesting basic information.
     * @throws Exception
     */
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
        assertTrue(plugins.getAvailableOnServer().size() > 0);
    }

    /**
     * Test that the jetty server basic test runs fine.
     */
    @Test
    public void exampleTest() {
        stubFor(get(urlEqualTo(API_PATH))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"version\" : \"1.0\"}")));
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:"+JETTY_SERVER_HTTP_PORT+API_PATH);
        request.setHeader(new BasicHeader("Accept", "application/json"));
        try {
            HttpResponse response = client.execute(request);
            log.info("Response:{}", response);
            String resultJson = parseJSONBody(response.getEntity().getContent());
            log.info("Response body:{}", resultJson);
            assertTrue(response.getStatusLine().getStatusCode()==200);
            TestVersion testVersion = gson.fromJson(resultJson, TestVersion.class);
            assertTrue(testVersion.getVersion().equals("1.0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void basicAuthTest(){
        //setup endpoint
        stubFor(get(urlEqualTo(API_PATH))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"version\" : \"1.0\"}")));
        //register api
        //test api without basic auth
        //enable basic auth on api
        //create consumer
        //create login/pwd for consumer
        //access resource with encode Basic Auth header
    }

    /**
     * Print json utility for Kong objects
     * @param obj
     */
    private void print(Object obj) {
        log.info(gson.toJson(obj));
    }

    /**
     * Utility method to parse apache response body inputstream.
     * @param is
     * @return
     */
    private String parseJSONBody(InputStream is){
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuffer result = new StringBuffer();
        String line = "";
        try {
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}

/**
 * Utility Test version object to serialize/deserialize dummy server response.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
class TestVersion{
    private String version;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "TestVersion{" +
                "version='" + version + '\'' +
                '}';
    }
}
