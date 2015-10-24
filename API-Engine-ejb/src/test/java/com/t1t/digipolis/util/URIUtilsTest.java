package com.t1t.digipolis.util;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 7/10/15.
 */
public class URIUtilsTest {
    private final String urlWithoutEndBackslash = "http://www.google.com/something/all";
    private final String urlWithEndBackslash = urlWithoutEndBackslash+"/";

    @Test
    public void testUriBackslashAppender() throws Exception {
        assertEquals(urlWithEndBackslash.trim(),URIUtils.uriBackslashAppender(urlWithEndBackslash).trim());
        assertEquals(urlWithEndBackslash.trim(), URIUtils.uriBackslashAppender(urlWithoutEndBackslash).trim());
    }

    @Test
    public void testUriBackslashRemover() throws Exception {
        assertEquals(urlWithoutEndBackslash.trim(),URIUtils.uriBackslashRemover(urlWithEndBackslash).trim());
        assertEquals(urlWithoutEndBackslash.trim(),URIUtils.uriBackslashRemover(urlWithoutEndBackslash).trim());
    }
}