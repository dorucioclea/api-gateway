package com.t1t.digipolis.apim.kong;

import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongApiList;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongExtraInfo;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongInstalledPlugins;
import com.t1t.digipolis.kong.model.KongOAuthTokenList;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
import com.t1t.digipolis.kong.model.KongPluginJWTRequest;
import com.t1t.digipolis.kong.model.KongPluginJWTResponse;
import com.t1t.digipolis.kong.model.KongPluginJWTResponseList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.kong.model.KongPluginACLRequest;
import com.t1t.digipolis.kong.model.KongPluginACLResponseList;
import retrofit.http.*;

import java.util.Collection;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface KongClient {
    /*********************   GENERAL   *******************/
    @GET("/")
    KongExtraInfo getInfo();
    @GET("/")
    KongInfo getParsedInfo();
    @GET("/status")
    Object getStatus();
    @GET("/cluster")
    Object getCluster();
    @GET("/oauth2_tokens")
    Object getOAuth2Tokens();

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
    @GET("/consumers") KongConsumerList getConsumers(@Query("offset") String offset);
    /*@PATCH("/consumers/{id}") KongConsumer updateConsumer(@Path("id")String id,@Body KongConsumer consumer);*/
    @PUT("/consumers/")KongConsumer updateOrCreateConsumer(@Body KongConsumer consumer);
    @PATCH("/consumers/{consumerId}")
    KongConsumer updateConsumer(@Path("consumerId") String consumerId, @Body KongConsumer consumer);
    @DELETE("/consumers/{id}")Object deleteConsumer(@Path("id")String id);
    @POST("/consumers/{id}/key-auth")
    KongPluginKeyAuthResponse createConsumerKeyAuthCredentials(@Path("id") String id, @Body KongPluginKeyAuthRequest kongPluginKeyAuthRequest);
    @GET("/consumers/{id}/key-auth")
    KongPluginKeyAuthResponseList getConsumerKeyAuthCredentials(@Path("id")String id);
    @POST("/consumers/{id}/basic-auth")
    KongPluginBasicAuthResponse createConsumerBasicAuthCredentials(@Path("id") String id, @Body KongPluginBasicAuthRequest kongPluginBasicAuthRequest);
    @GET("/consumers/{id}/basic-auth")
    KongPluginBasicAuthResponseList getConsumerBasicAuthCredentials(@Path("id")String id);
    @DELETE("/consumers/{consumerId}/key-auth/{keyAuthId}")
    Object deleteConsumerKeyAuthCredential(@Path("consumerId")String consumerId,@Path("keyAuthId")String keyAuthId);
    @POST("/consumers/{id}/jwt")
    KongPluginJWTResponse createConsumerJWTCredentials(@Path("id") String id, @Body KongPluginJWTRequest kongPluginJWTRequest);
    @GET("/consumers/{id}/jwt")
    KongPluginJWTResponseList getConsumerJWTCredentials(@Path("id")String id);
    @DELETE("/consumers/{consumerId}/jwt/{credentialId}")
    Object deleteConsumerJwtCredential(@Path("consumerId") String consumerId, @Path("credentialId") String credentialId);
    @POST("/consumers/{id}/acls")
    KongPluginACLResponse addConsumerToACL(@Path("id") String id, @Body  KongPluginACLRequest request);
    @GET("/consumers/{id}/acls")
    KongPluginACLResponseList getConsumerACL(@Path("id") String id);
    @DELETE("/consumers/{id}/acls/{pluginId}")
    Object deleteConsumerACLEntry(@Path("id") String id, @Path("pluginId") String pluginId);

    /*********************   PLUGINS   *******************/
    @GET("/plugins/enabled")KongInstalledPlugins getInstalledPlugins();
    /*@GET("/plugins/{pluginname}/schema")KongPluginSchema getPluginSchema(@Path("pluginname") String pluginName);*/
    @POST("/apis/{apinameorid}/plugins/")KongPluginConfig createPluginConfig(@Path("apinameorid")String apiNameOrId,@Body KongPluginConfig pluginConfig);
    @GET("/apis/{apinameorid}/plugins/")KongPluginConfigList getKongPluginConfigList(@Path("apinameorid")String apiNameOrId);
    @GET("/apis/{apinameorid}/plugins/")KongPluginConfigList getKongPluginConfig(@Path("apinameorid")String apiNameOrId,@Query("name") String pluginId);
    @PUT("/apis/{apinameorid}/plugins/") KongPluginConfig updateKongPluginConfig(@Path("apinameorid")String apiNameOrId,@Body KongPluginConfig config);
    @GET("/plugins/")KongPluginConfigList getAllPlugins();
    /*@PATCH("/apis/{apinameorid}/plugins/{id}")KongPluginConfig updatePlugin(@Path("apinameorid")String apiNameOrId,@Path("id")String pluginId,@Body KongPluginConfig pluginConfig);*/
    @PUT("/apis/{apinameorid}/plugins/")KongPluginConfig updateOrCreatePluginConfig(@Path("apinameorid")String apiNameOrId,@Body KongPluginConfig pluginConfig);
    @DELETE("/apis/{apinameorid}/plugins/{id}")Object deletePlugin(@Path("apinameorid")String apiNameOrId, @Path("id") String pluginId);
    @GET("/plugins/{pluginId}") KongPluginConfig getPlugin(@Path("pluginId") String pluginId);

    /*********************   OAUTH   *******************/
    @FormUrlEncoded
    @POST("/consumers/{consumerId}/oauth2") KongPluginOAuthConsumerResponse enableOAuthForConsumer(@Path(value = "consumerId", encode = false)String consumerId,@Field("name") String name, @Field("client_id")String clientId, @Field("client_secret")String clientSecret,@Field("redirect_uri")Iterable<String> redirectURL);
    @GET("/consumers/{consumerId}/oauth2") KongPluginOAuthConsumerResponseList getConsumerOAuthCredentials(@Path(value = "consumerId", encode = false)String consumerId);
    @PUT("/consumers/{consumerId}/oauth2") KongPluginOAuthConsumerResponse updateConsumerOAuthCredentials(@Path(value = "consumerId", encode = false) String consumerId, @Body KongPluginOAuthConsumerRequest request);
    @GET("/oauth2")KongPluginOAuthConsumerResponseList getApplicationOAuthInformation(@Query("client_id")String clientId);
    @GET("/oauth2")KongPluginOAuthConsumerResponseList getApplicationOAuthInformationByCredentialId(@Query("id") String credentialId);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokens();
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokens(@Query("offset") String offset);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokensByCredentialId(@Query("credential_id")String credentialId);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokensByCredentialId(@Query("credential_id")String credentialId, @Query("offset") String offset);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokensByAuthenticatedUser(@Query("authenticated_userid") String authenticatedUserId);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokensByAuthenticatedUser(@Query("authenticated_userid") String authenticatedUserId, @Query("offset") String offset);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthTokensByAccessToken(@Query("access_token") String accessToken);
    @GET("/oauth2_tokens")KongOAuthTokenList getOAuthToken(@Query("id") String tokenId);
    @POST("/oauth2_tokens") KongOAuthToken createOAuthToken(@Body KongOAuthToken token);
    @DELETE("/oauth2_tokens/{tokenId}") Object revokeOAuthToken(@Path("tokenId") String tokenId);
    @DELETE("/consumers/{consumerId}/oauth2/{pluginId}")Object deleteOAuth2Credential(@Path(value = "consumerId", encode = false)String consumerId, @Path("pluginId")String pluginId);

    /*********************   JWT   *******************/
}
