package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.apps.NewApiKeyBean;
import com.t1t.digipolis.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.security.OAuthExpTimeRequest;

import java.util.Set;

/**
 * Created by michallispashidis on 10/04/16.
 */
public interface ISecurityResource {
    /**
     * Sets the cental OAuth token expiration time for issued tokens.
     * @param request
     * @throws NotAuthorizedException
     */
    void setOAuthExpTime(OAuthExpTimeRequest request) throws NotAuthorizedException;

    /**
     * Reissue all API keys
     * @return
     * @throws NotAuthorizedException
     */
    Set<NewApiKeyBean> reissueAllApiKeys() throws NotAuthorizedException;

    /**
     * Reissue all OAuth2 credentials
     * @return
     * @throws NotAuthorizedException
     */
    Set<NewOAuthCredentialsBean> reissueAllOAuth2Credentials() throws NotAuthorizedException;

    /**
     * Revoke an application version's OAuth token
     * @param token
     * @throws NotAuthorizedException
     */
    void revokeApplicationVersionOAuthToken(OAuth2TokenRevokeBean token) throws NotAuthorizedException;

    /**
     *
     * @param token
     */
    void revokeOAuthToken(String token);
}
