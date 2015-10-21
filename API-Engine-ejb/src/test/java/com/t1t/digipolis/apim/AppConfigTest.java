package com.t1t.digipolis.apim;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 21/10/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppConfigTest {
    private static AppConfig config;
    @BeforeClass public static void init()throws Exception{
        config = new AppConfig();
        config.init();
    }

    @Test
    public void testGetEnvironment() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getEnvironment()));
    }

    @Test
    public void testGetKongEndpoint() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getKongEndpoint()));
    }

    @Test
    public void testGetVersion() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getVersion()));
    }

    @Test
    public void testGetBuildDate() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getBuildDate()));
    }

    @Test
    public void testGetConfigurationFile() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getConfigurationFile()));
    }

    @Test
    public void testGetKongManagementEndpoint() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getKongManagementEndpoint()));
    }

    @Test
    public void testGetIDPSAMLEndpoint() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getIDPSAMLEndpoint()));
    }

    @Test
    public void testGetIDPSAMLNameIdFormat() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getIDPSAMLNameIdFormat()));
    }

    @Test
    public void testGetIDPSCIMEndpoint() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getIDPSCIMEndpoint()));
    }

    @Test
    public void testGetIDPOAuthTokenEndpoint() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getIDPOAuthTokenEndpoint()));
    }

    @Test
    public void testGetIDPOAuthClientId() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getIDPOAuthClientId()));
    }

    @Test
    public void testGetIDPOAuthClientSecret() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getIDPOAuthClientSecret()));
    }

    @Test
    public void testGetMetricsScheme() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getMetricsScheme()));
    }

    @Test
    public void testGetMetricsURI() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getMetricsURI()));
    }

    @Test
    public void testGetMetricsPort() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getMetricsURI()));
    }

    @Test
    public void testGetDefaultOrganization() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getDefaultOrganization()));
    }

    @Test
    public void testGetDefaultUserRoles() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getDefaultUserRoles()));
    }

    @Test
    public void testGetOAuthConsentURI() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getOAuthConsentURI()));
    }

    @Test
    public void testGetSecurityRestResource() throws Exception {
        assertTrue(!StringUtils.isEmpty(config.getOAuthConsentURI()));
    }
}