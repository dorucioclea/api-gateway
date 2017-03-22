package com.t1t.util;

import com.t1t.apim.beans.apps.AppIdentifier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        assertTrue("org.app.v.user".equals(appConsumerUnqiueId));
    }

    @Test
    public void testCreateAppUniqueId() throws Exception {
        String appOrg = "org";
        String appApp = "app";
        String appVersions = "v";
        String appUniqueName = ConsumerConventionUtil.createAppUniqueId(appOrg, appApp, appVersions);
        assertTrue("org.app.v".equals(appUniqueName));
    }

    @Test
    public void testCreateUserUniqueId() throws Exception {
        String username = "ex01234";
        String formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue("ex01234".equals(formattedUsername));

        username = "EX01234";
        formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue("ex01234".equals(formattedUsername));

        username = "Ex01234";
        formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue("ex01234".equals(formattedUsername));

        username = "eX01234";
        formattedUsername = ConsumerConventionUtil.createUserUniqueId(username);
        assertTrue("ex01234".equals(formattedUsername));
    }

    @Test
    public void testParseApplicationIdentifier() throws Exception {
        String tobeParsed = "int.name.version";
        AppIdentifier appExpectedId = new AppIdentifier();
        appExpectedId.setPrefix("int");
        appExpectedId.setAppId("name");
        appExpectedId.setVersion("version");
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(tobeParsed);
        assertEquals(appExpectedId,appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierNullValue() throws Exception {
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(null);
        assertNull(appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierEmptyValue() throws Exception {
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier("");
        assertNull(appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierWrongFormatValue() throws Exception {
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier("org.org.app.app.v.v");
        assertNull(appIdentifier);
    }
}