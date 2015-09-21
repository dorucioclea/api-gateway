package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.exceptions.ApplicationNotFoundException;

import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 16/09/15.
 */
public interface IBasicAuthorization {
    /* Creates a new consumer for the application with given version. The consumer will be added to the correct ACL on all apis in the application.
    * The provisioned credentials for a consumer will be
    * @param criteria
    * @return*/
/*    Response createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria)throws ApplicationNotFoundException;
    Response getKeyAuthConsumer (String apiKey,String orgId,String appId,String version,String customId)throws ApplicationNotFoundException;
    //Response updateKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria);
    Response deleteKeyAuthConsumer(String apiKey,String orgId,String appId,String version,String customId)throws ApplicationNotFoundException;*/
}
