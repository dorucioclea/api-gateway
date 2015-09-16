package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestBasicAuth;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestOAuth;

/**
 * Created by michallispashidis on 9/09/15.
 * The IAuthorizationResource provides authorization utility endpoints.
 */
public interface IAuthorizationResource {
    /**
     * Creates a new consumer for the application with given version. The consumer will be added to the correct ACL on all apis in the application.
     * The provisioned credentials for a consumer will be
     * @param criteria
     * @return
     */
    AuthConsumerBean createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria);
    AuthConsumerBean createBasicAuthConsumer(AuthConsumerRequestBasicAuth criteria);
    AuthConsumerBean createOAuthConsumer(AuthConsumerRequestOAuth criteria);
}
