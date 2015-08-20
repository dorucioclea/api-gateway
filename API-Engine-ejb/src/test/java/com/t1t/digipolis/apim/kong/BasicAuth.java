package com.t1t.digipolis.apim.kong;

import org.apache.http.client.HttpClient;
import org.apache.commons.codec.binary.Base64;
/**
 * Created by michallispashidis on 20/08/15.
 */
public class BasicAuth {
    public static void main(String[] args) {
        String authString = "michallis" + ":" + "test";
        System.out.println("auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        System.out.println(authStringEnc);
    }
}
