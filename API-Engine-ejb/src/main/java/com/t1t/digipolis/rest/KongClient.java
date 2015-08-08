package com.t1t.digipolis.rest;

import com.t1t.digipolis.kong.model.KongAPIList;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongStatus;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongClient {
    @GET("/") KongInfo getInfo();
    @GET("/status") KongStatus getStatus();
    @POST("/apis") KongApi addApi(@Body KongApi api);
    @GET("/apis/{id}") KongApi getApi(@Path("id")String id);
    @GET("/apis") KongAPIList getApiDefinitions();
}
