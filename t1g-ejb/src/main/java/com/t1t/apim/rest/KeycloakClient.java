package com.t1t.apim.rest;

import com.t1t.kong.model.KeycloakRealm;
import retrofit.http.GET;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface KeycloakClient {

    @GET("/")
    KeycloakRealm get();

}