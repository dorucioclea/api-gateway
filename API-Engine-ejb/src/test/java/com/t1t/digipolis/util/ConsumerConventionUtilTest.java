package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 29/11/15.
 */
public class ConsumerConventionUtilTest {

    @Test
    public void testCreateAppConsumerUnqiueId() throws Exception {
        String appOrg = "org";
        String appApp = "app";
        String appVersions = "v";
        String appConsumer = "user";
        String appConsumerUnqiueId = ConsumerConventionUtil.createAppConsumerUnqiueId(appOrg, appApp, appVersions, appConsumer);
        assertTrue(appConsumerUnqiueId.equals("org.app.v.user"));
    }

    @Test
    public void testCreateAppUniqueId() throws Exception {
        String appOrg = "org";
        String appApp = "app";
        String appVersions = "v";
        String appUniqueName = ConsumerConventionUtil.createAppUniqueId(appOrg,appApp,appVersions);
        assertTrue(appUniqueName.equals("org.app.v"));
    }

    @Test
    public void testParseApplicationIdentifier() throws Exception {
        String tobeParsed = "org.name.version";
        AppIdentifier appExpectedId = new AppIdentifier();
        appExpectedId.setOrgId("org");
        appExpectedId.setAppId("name");
        appExpectedId.setVersion("version");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(tobeParsed);
        assertEquals(appExpectedId,appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierNullValue() throws Exception {
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(null);
        assertNull(appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierEmptyValue() throws Exception {
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier("");
        assertNull(appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierWrongFormatValue() throws Exception {
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier("org.org.app.app.v.v");
        assertNull(appIdentifier);
    }
}