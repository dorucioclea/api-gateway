package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.*;
import com.t1t.digipolis.apim.exceptions.OAuthException;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;

import javax.ws.rs.core.Response;
import java.util.List;

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
     * Get redirect URL for user authorization using Authorization Grant/Implicit Grant.
     *
     * @param oauthClientId
     * @param orgId
     * @param serviceId
     * @param version
     * @param responseType
     * @param authenticatedUserId
     * @param requestScopes
     * @return
     * @throws OAuthException
     */
    public String getAuthorizationRedirect(OAuthResponseType responseType,String authenticatedUserId, String oauthClientId,String orgId,String serviceId,String version,OAuthServiceScopeRequest requestScopes) throws OAuthException;

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
    public OAuthServiceScopeResponse getServiceVersionScopes(String oauthClientId,String orgId,String serviceId,String version) throws OAuthException;

    /**
     * Authenticates a user, through the application service provider proxy.
     * (using oauth client credential for the application and basic auth for the end user.
     *
     * @param request
     * @return
     * @throws OAuthException
     */
    public String ipdClientCredGrantForUserAuthentication(ProxyAuthRequest request)throws OAuthException;

}
