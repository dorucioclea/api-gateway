package com.t1t.digipolis.apim.kong;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongConstants {
    String KONG_SCHEME = "apiapp.kong.scheme";
    String KONG_URI = "apiapp.kong.url";
    String KONG_PORT_MGT = "apiapp.kong.ports.management";
    String KONG_PORT_GTW = "apiapp.kong.ports.gateway";
    String KONG_OAUTH_ENDPOINT = "oauth2";
    String KONG_OAUTH2_ENDPOINT_AUTH = "authorize";
    String KONG_OAUTH2_ENDPOINT_TOKEN = "token";
}
