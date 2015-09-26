package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.exceptions.OAuthException;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;

/**
 * Created by michallispashidis on 16/09/15.
 */
public interface IOAuth2Authorization {
    /**
     * Enables OAuth for a given consumer, when it's not yet the case.
     *
     * @param request
     * @return
     */
    KongPluginOAuthConsumerResponse enableOAuthForConsumer(KongPluginOAuthConsumerRequest request) throws OAuthException;


}
