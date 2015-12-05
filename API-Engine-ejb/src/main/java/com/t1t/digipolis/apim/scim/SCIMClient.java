package com.t1t.digipolis.apim.scim;

import com.t1t.digipolis.kong.model.SCIMUser;
import com.t1t.digipolis.kong.model.SCIMUserList;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by michallispashidis on 10/10/15.
 */
public interface SCIMClient {
    @Headers("Accept: application/json")
    @GET("/Users")
    SCIMUserList getUserInformation(@Query("filter") String userFilter);

    @GET("/Users/{userid}")
    SCIMUser getUserInforamation(@Path("userid") String userid);//unique userID sometimes::upn or samaccount
}
