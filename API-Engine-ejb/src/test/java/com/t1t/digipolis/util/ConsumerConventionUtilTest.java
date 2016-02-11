package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 29/11/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConsumerConventionUtil.class)
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
        String appUniqueName = ConsumerConventionUtil.createAppUniqueId(appOrg, appApp, appVersions);
        assertTrue(appUniqueName.equals("org.app.v"));
    }

    @Test
    public void testCreateUserUniqueId() throws Exception {
        String username = "ex01234";
        String formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue(formattedUsername.equals("ex01234"));

        username = "EX01234";
        formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue(formattedUsername.equals("ex01234"));

        username = "Ex01234";
        formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue(formattedUsername.equals("ex01234"));

        username = "eX01234";
        formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue(formattedUsername.equals("ex01234"));
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

    @Test
    public void testCreateAppConsumerUnqiueId1() throws Exception {
        String orgId = "someorg";
        String appId = "someapp";
        String appVersionId = "version";
        String userId = "user";
        assertEquals("someorg.someapp.version.user",ConsumerConventionUtil.createAppConsumerUnqiueId(orgId,appId,appVersionId,userId));
    }

    @Test
    public void testCreateAppUniqueId1() throws Exception {
        String orgId = "someorg";
        String appId = "someapp";
        String appVersionId = "version";
        assertEquals("someorg.someapp.version",ConsumerConventionUtil.createAppUniqueId(orgId,appId,appVersionId));
    }

    @Test
    public void testCreateAppVersionlessId() throws Exception {
        String orgId = "someorg";
        String appId = "someapp";
        String appVersionId = "version";
        assertEquals("someorg.someapp",ConsumerConventionUtil.createAppVersionlessId(orgId,appId));
    }

    @Test
    public void testCreateUserUniqueId1() throws Exception {
        assertEquals("someuser",ConsumerConventionUtil.createUserUniqueId("SoMeUser"));
    }
}