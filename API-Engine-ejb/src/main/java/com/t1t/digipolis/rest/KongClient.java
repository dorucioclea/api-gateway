package com.t1t.digipolis.rest;

import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongStatus;
import retrofit.http.GET;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongClient {
    @GET("/") KongInfo getInfo();
    @GET("/status") KongStatus getStatus();
}
