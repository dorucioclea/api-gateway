package com.t1t.apim;

import com.t1t.apim.beans.config.ConfigBean;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by michallispashidis on 21/10/15.
 * Tests the configuration file compliancy depending the chosen profile.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppConfigTest {
    private static AppConfig config;

    @Rule
    public Timeout globalTimeout =  new Timeout(1000);

    @BeforeClass
    public static void init() throws Exception {
        ConfigBean configBean = new ConfigBean();
        configBean.setConfigPath("src/test/resources/application-test.conf");
        config = new AppConfig();
        config.initConfig(configBean);
    }

    @Test
    public void testGetEnvironment() throws Exception {
        assertThat(config.getEnvironment().trim(), is(not("")));
        assertFalse(StringUtils.isEmpty(config.getEnvironment()));
    }

    @Test
    public void testGetKongEndpoint() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getKongEndpoint()));
    }

    @Test
    public void testGetVersion() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getVersion()));
    }

    @Test
    public void testGetBuildDate() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getBuildDate()));
    }

    @Test
    public void testGetConfigurationFile() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getConfigurationFile()));
    }

    @Test
    public void testGetKongManagementEndpoint() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getKongManagementEndpoint()));
    }

    @Test
    public void testGetIDPSAMLEndpoint() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPSAMLEndpoint()));
    }

    @Test
    public void testGetIDPSAMLNameIdFormat() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPSAMLNameIdFormat()));
    }

    @Test
    public void testGetIDPSCIMEndpoint() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPSCIMEndpoint()));
    }

    @Test
    public void testGetSCIMUserLogin() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPSCIMUserLogin()));
    }

    @Test
    public void testGetSCIMUserPassword()throws Exception{
        assertFalse(StringUtils.isEmpty(config.getIDPSCIMUserPassword()));
    }

    @Test
    public void testGetIDPOAuthTokenEndpoint() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPOAuthTokenEndpoint()));
    }

    @Test
    public void testGetIDPOAuthClientId() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPOAuthClientId()));
    }

    @Test
    public void testGetIDPOAuthClientSecret() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getIDPOAuthClientSecret()));
    }

    @Test
    public void testGetDataDogMetricsApiKey() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDataDogMetricsApiKey()));
    }

    @Test
    public void testGetDataDogMetricsURI() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDataDogMetricsURI()));
    }

    @Test
    public void testGetDataDogMetricsApplicationKey() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDataDogMetricsURI()));
    }

    @Test
    public void testGetDefaultOrganization() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDefaultOrganization()));
    }

    @Test
    public void testGetDefaultUserRoles() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDefaultUserRoles()));
    }

    @Test
    public void testGetOAuthConsentURI() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getOAuthConsentURI()));
    }

    @Test
    public void testGetSecurityRestResource() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getOAuthConsentURI()));
    }
}