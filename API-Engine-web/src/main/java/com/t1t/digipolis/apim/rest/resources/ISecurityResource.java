package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.apps.NewApiKeyBean;
import com.t1t.digipolis.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.security.OAuthExpTimeRequest;

import javax.ws.rs.PathParam;
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

    public Set<NewApiKeyBean> reissueAllApiKeys() throws NotAuthorizedException;

    public Set<NewOAuthCredentialsBean> reissueAllOAuth2Credentials() throws NotAuthorizedException;

    /**
     * Revoke an application version's OAuth token
     * @param token
     * @throws NotAuthorizedException
     */
    public void revokeApplicationVersionOAuthToken(OAuth2TokenBean token) throws NotAuthorizedException;
}
