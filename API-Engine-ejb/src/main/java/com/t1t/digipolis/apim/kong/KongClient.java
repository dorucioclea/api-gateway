package com.t1t.digipolis.apim.kong;

import com.t1t.digipolis.kong.model.*;
import retrofit.http.*;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongClient {
    /*********************   GENERAL   *******************/
    @GET("/") KongInfo getInfo();
    @GET("/status") KongStatus getStatus();

    /*********************   API   ***********************/
    @POST("/apis") KongApi addApi(@Body KongApi api);
    @GET("/apis/{id}") KongApi getApi(@Path("id")String id);
    @GET("/apis") KongApiList listApis();
    @PATCH("/apis/{id}")KongApi updateApi(@Path("id")String id, @Body KongApi api);
    @PUT("/apis")KongApi updateOrCreateApi(@Body KongApi api);
    @DELETE("/apis/{id}")Object deleteApi(@Path("id")String id);

    /*********************   CONSUMER   ******************/
    @POST("/consumers/") KongConsumer createConsumer(@Body KongConsumer consumer);
    @GET("/consumers/{id}") KongConsumer getConsumer(@Path("id")String id);
    @GET("/consumers/") KongConsumerList getConsumers();
    @PATCH("/consumers/{id}") KongConsumer updateConsumer(@Path("id")String id,@Body KongConsumer consumer);
    @PUT("/consumers/")KongConsumer updateOrCreateConsumer(@Body KongConsumer consumer);
    @DELETE("/consumers/{id}")Object deleteConsumer(@Path("id")String id);

    /*********************   PLUGINS   *******************/
}
