package com.t1t.digipolis.apim.beans.authorization;

import java.util.List;

/**
 * Created by michallispashidis on 2/10/15.
 */
public class OAuthServiceScopeRequest {
    private List<String> scopes;

    public OAuthServiceScopeRequest() {
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return "OAuthServiceScopeRequest{" +
                "scopes=" + scopes +
                '}';
    }
}
