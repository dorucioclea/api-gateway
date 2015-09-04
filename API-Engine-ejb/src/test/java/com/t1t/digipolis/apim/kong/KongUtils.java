package com.t1t.digipolis.apim.kong;

import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by michallispashidis on 20/08/15.
 */
public class KongUtils {
    private static Logger log = LoggerFactory.getLogger(KongUtils.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String []args){
        log.info("basic auth header: "+getBasicAuthHeaderValueEncoded("W6FcDk905p5jT5_C_DDec4hAwBMa","nyu7u2If6XBBcQXxi7M6wfHkoK4a"));
    }
    /**
     * Utility method that encodes a name and password for basic authentication.
     * @param name
     * @param password
     * @return
     */
    public static String getBasicAuthHeaderValueEncoded(String name, String password){
        String authString = name + ":" + password;
        System.out.println("auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        return new String(authEncBytes);
    }
}
