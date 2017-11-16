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
    private static AppConfigBean config;

    @Rule
    public Timeout globalTimeout = new Timeout(1000);

    @BeforeClass
    public static void init() throws Exception {
        ConfigBean configBean = new ConfigBean();
        configBean.setConfigPath("src/test/resources/application-test.conf");
        AppConfig conf = new AppConfig();
        conf.initConfig(configBean);
        config = conf.getConfig();
    }

    @Test
    public void testGetEnvironment() throws Exception {
        assertThat(config.getEnvironment().trim(), is(not("")));
        assertFalse(StringUtils.isEmpty(config.getEnvironment()));
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
    public void testGetDataDogMetricsApiKey() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDataDogMetricsApiKey()));
    }

    @Test
    public void testGetDataDogMetricsURI() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDataDogMetricsUri()));
    }

    @Test
    public void testGetDataDogMetricsApplicationKey() throws Exception {
        assertFalse(StringUtils.isEmpty(config.getDataDogMetricsApplicationKey()));
    }
}