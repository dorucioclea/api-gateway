package com.t1t.digipolis.apim.idp;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by michallispashidis on 15/10/15.
 */
public interface IDPClient {
    /*********************   IDP::OAUTH-Token Endpoint for user authentication   *******************/
    @POST("/")
    @FormUrlEncoded
    Response authenticateUser(@Field("grant_type")String grantType, @Field("username")String username, @Field("password")String password, @Field("scope")String scope);
}
