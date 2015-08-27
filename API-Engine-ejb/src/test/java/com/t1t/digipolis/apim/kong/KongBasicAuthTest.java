package com.t1t.digipolis.apim.kong;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
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
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.io.IOException;

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
    private static final String API_PATH = "/testbasicauthpath";
    private static final String API_URL = "http://domain.com/app/rest/v1";

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(wireMockConfig().port(8089).httpsPort(8489));
    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

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

    @Test
    public void exampleTest() {
        stubFor(get(urlEqualTo("/some/thing"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8089/some/thing");
        request.setHeader(new BasicHeader("Accept", "text/xml"));
        try {
            HttpResponse response = client.execute(request);
            log.info("Response:{}", response);
            assertTrue(response.getStatusLine().getStatusCode()==200);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void print(Object obj) {
        log.info(gson.toJson(obj));
    }
}
