package com.t1t.digipolis.util;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Maarten Somers
 * @since 2015
 */
public class UserConventionUtilTest {

    @Test
    public void testFormatUsername() throws Exception {
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

}
