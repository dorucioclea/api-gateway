package com.t1t.digipolis.apim.kong;

import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongApiList;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongInstalledPlugins;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongStatus;
import retrofit.http.*;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongClient {
    /*********************   GENERAL   *******************/
    @GET("/")
    KongInfo getInfo();
    @GET("/status")
    KongStatus getStatus();

    /*********************   API   ***********************/
    @POST("/apis") KongApi addApi(@Body KongApi api);
    @GET("/apis/{id}") KongApi getApi(@Path("id")String id);
    @GET("/apis")
    KongApiList listApis();
    /*@PATCH("/apis/{id}")KongApi updateApi(@Path("id")String id, @Body KongApi api);*/
    @PUT("/apis")KongApi updateOrCreateApi(@Body KongApi api);
    @DELETE("/apis/{id}")Object deleteApi(@Path("id")String id);

    /*********************   CONSUMER   ******************/
    @POST("/consumers/")
    KongConsumer createConsumer(@Body KongConsumer consumer);
    @GET("/consumers/{id}") KongConsumer getConsumer(@Path("id")String id);
    @GET("/consumers/")
    KongConsumerList getConsumers();
    /*@PATCH("/consumers/{id}") KongConsumer updateConsumer(@Path("id")String id,@Body KongConsumer consumer);*/
    @PUT("/consumers/")KongConsumer updateOrCreateConsumer(@Body KongConsumer consumer);
    @DELETE("/consumers/{id}")Object deleteConsumer(@Path("id")String id);
    @POST("/consumers/{id}/keyauth")
    KongPluginKeyAuthResponse createConsumerKeyAuthCredentials(@Path("id") String id, @Body KongPluginKeyAuthRequest kongPluginKeyAuthRequest);
    @GET("/consumers/{id}/keyauth")
    KongPluginKeyAuthResponseList getConsumerKeyAuthCredentials(@Path("id")String id);
    @POST("/consumers/{id}/basicauth")
    KongPluginBasicAuthResponse createConsumerBasicAuthCredentials(@Path("id") String id, @Body KongPluginBasicAuthRequest kongPluginBasicAuthRequest);
    @GET("/consumers/{id}/basicauth")
    KongPluginBasicAuthResponseList getConsumerBasicAuthCredentials(@Path("id")String id);

    /*********************   PLUGINS   *******************/
    @GET("/plugins/")KongInstalledPlugins getInstalledPlugins();
    /*@GET("/plugins/{pluginname}/schema")KongPluginSchema getPluginSchema(@Path("pluginname") String pluginName);*/
    @POST("/apis/{apinameorid}/plugins/")KongPluginConfig createPluginConfig(@Path("apinameorid")String apiNameOrId,@Body KongPluginConfig pluginConfig);
    @GET("/apis/{apinameorid}/plugins/")KongPluginConfigList getKongPluginConfigList(@Path("apinameorid")String apiNameOrId);
    @GET("/plugins_configurations/")KongPluginConfigList getAllPlugins();
    /*@PATCH("/apis/{apinameorid}/plugins/{id}")KongPluginConfig updatePlugin(@Path("apinameorid")String apiNameOrId,@Path("id")String pluginId,@Body KongPluginConfig pluginConfig);*/
    @PUT("/apis/{apinameorid}/plugins/")KongPluginConfig updateOrCreatePluginConfig(@Path("apinameorid")String apiNameOrId,@Body KongPluginConfig pluginConfig);
    @DELETE("/apis/{apinameorid}/plugins/{id}")Object deletePlugin(@Path("apinameorid")String apiNameOrId, @Path("id") String pluginId);
}
