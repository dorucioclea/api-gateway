package com.t1t.apim.auth.rest.resources;

import com.t1t.apim.beans.authorization.OAuthApplicationResponse;
import com.t1t.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.apim.beans.authorization.OAuthServiceScopeResponse;
import com.t1t.apim.exceptions.OAuthException;
import com.t1t.kong.model.KongPluginOAuthConsumerResponse;

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
    KongPluginOAuthConsumerResponse enableOAuthForConsumer(OAuthConsumerRequestBean request) throws OAuthException;

    /**
     * Retrieve the application information, based on the OAuth2 clientId.
     *
     * @param oauthClientId
     * @return
     * @throws OAuthException
     */
    OAuthApplicationResponse getApplicationInfo(String oauthClientId, String orgId, String serviceId, String version) throws OAuthException;

    /**
     * Returns a list of scopes for a given service version.
     *
     * @param oauthClientId
     * @param orgId
     * @param serviceId
     * @param version
     * @return
     * @throws OAuthException
     */
    public OAuthServiceScopeResponse getServiceVersionScopes(String oauthClientId, String orgId, String serviceId, String version) throws OAuthException;
}
