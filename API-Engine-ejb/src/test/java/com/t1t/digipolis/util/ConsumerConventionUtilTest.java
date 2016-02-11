package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
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
        appExpectedId.setScope("int");
        appExpectedId.setAppId("name");
        appExpectedId.setVersion("version");
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(tobeParsed,availableScopes);
        assertEquals(appExpectedId,appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierNullValue() throws Exception {
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier(null,availableScopes);
        assertNull(appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierEmptyValue() throws Exception {
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier("",availableScopes);
        assertNull(appIdentifier);
    }

    @Test
    public void testParseApplicationIdentifierWrongFormatValue() throws Exception {
        List<String> availableScopes = new ArrayList<>();
        availableScopes.add("int");
        AppIdentifier appIdentifier = ConsumerConventionUtil.parseApplicationIdentifier("org.org.app.app.v.v",availableScopes);
        assertNull(appIdentifier);
    }
}