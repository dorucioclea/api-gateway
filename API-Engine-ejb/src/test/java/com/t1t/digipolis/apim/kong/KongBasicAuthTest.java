package com.t1t.digipolis.apim.kong;

import com.google.gson.Gson;
import com.t1t.digipolis.kong.model.KongPluginBasicAuth;
import com.t1t.digipolis.kong.model.KongPluginKeyAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuth;
import com.t1t.digipolis.kong.model.KongPluginRateLimiting;

/**
 * Created by michallispashidis on 20/08/15.
 */
public class KongBasicAuthTest {
    public static void main(String []args){
        Gson gson = new Gson();
        KongPluginKeyAuth basicAuth = gson.fromJson("{\"key_names\":[],\"hide_credentials\":false}", KongPluginKeyAuth.class);

        System.out.println(basicAuth);
    }
}
