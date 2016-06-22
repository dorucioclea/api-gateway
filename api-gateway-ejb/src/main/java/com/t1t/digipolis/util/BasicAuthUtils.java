package com.t1t.digipolis.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by michallispashidis on 21/09/15.
 */
public class BasicAuthUtils {
    private static Logger LOG = LoggerFactory.getLogger(BasicAuthUtils.class.getName());

    public static String getBasicAuthHeaderValueEncoded(String name, String password){
        String authString = name + ":" + password;
        LOG.info("Basic Auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        return new String(authEncBytes);
    }
}
