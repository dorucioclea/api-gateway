package com.t1t.digipolis.rest;

import com.t1t.digipolis.kong.model.*;
import retrofit.http.*;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongClient {
    @GET("/") KongInfo getInfo();
    @GET("/status") KongStatus getStatus();
    @POST("/apis") KongApi addApi(@Body KongApi api);
    @GET("/apis/{id}") KongApi getApi(@Path("id")String id);
    @GET("/apis") KongApiList listApis();
    @PATCH("/apis/{id}")KongApi updateApi(@Path("id")String id, @Body KongApi api);
    @PUT("/apis")KongApi updateOrCreateApi(@Body KongApi api);
    @DELETE("/apis/{id}")void deleteApi(@Path("id")String id);
}
