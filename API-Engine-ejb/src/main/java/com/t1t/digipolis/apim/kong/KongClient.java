package com.t1t.digipolis.apim.kong;

import com.t1t.digipolis.kong.model.*;
import retrofit.http.*;

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
    @GET("/consumers") KongConsumerList getConsumerByCustomId(@Query("custom_id") String customId);
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
    KongPluginACLResponseList getConsumerACLs(@Path("id") String id);
    @GET("/consumers/{id}/acls")
    KongPluginACLResponseList getConsumerACLs(@Path("id") String id, @Query("offset") String offset);
    @GET("/consumers/{consumerId}/acls/{aclId}")
    KongPluginACLResponse getConsumerAcl(@Path("consumerId") String consumerId, @Path("aclId") String aclId);
    @PUT("/consumers/{id}/acls")
    KongPluginACLResponse updateConsumerAcl(@Path("id") String consumerId, @Body KongPluginACLResponse acl);
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
    @PUT("/plugins") KongPluginConfig updatePlugin(@Body KongPluginConfig pluginId);
    @GET("/plugins") KongPluginConfigList getConsumerPlugins(@Query("consumer_id") String consumerId);
    @GET("/plugins") KongPluginConfigList getConsumerSpecificApiPlugins(@Query("consumer_id") String consumerId, @Query("api_id") String apiId);
    @GET("/plugins") KongPluginConfigList getConsumerSpecificApiPlugins(@Query("consumer_id") String consumerId, @Query("api_id") String apiId, @Query("offset") String offset);

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
