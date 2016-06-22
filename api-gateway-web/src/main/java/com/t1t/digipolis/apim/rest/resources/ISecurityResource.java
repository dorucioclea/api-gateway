package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.security.OAuthExpTimeRequest;

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
}
