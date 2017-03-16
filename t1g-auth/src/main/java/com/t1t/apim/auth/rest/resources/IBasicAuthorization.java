package com.t1t.apim.auth.rest.resources;

import com.t1t.apim.beans.authorization.AuthConsumerRequestBasicAuthBean;
import com.t1t.apim.exceptions.ApplicationNotFoundException;

import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 16/09/15.
 */
public interface IBasicAuthorization {
    /* Creates a new consumer for the application with given version. The consumer will be added to the correct ACL on all apis in the application.
    * The provisioned credentials for a consumer will be
    * @param criteria
    * @return*/
    Response createBasicAuthConsumer(AuthConsumerRequestBasicAuthBean criteria)throws ApplicationNotFoundException;
    Response getBasicAuthConsumer (String apiKey,String orgId,String appId,String version,String customId)throws ApplicationNotFoundException;
    Response deleteBasicAuthConsumer(String apiKey,String orgId,String appId,String version,String customId)throws ApplicationNotFoundException;
}
