package com.t1t.apim.beans.authorization;

import java.util.Map;

/**
 * Created by michallispashidis on 2/10/15.
 */
public class OAuthServiceScopeResponse {
    private Map<String,String> scopes;

    public OAuthServiceScopeResponse() {
    }

    public Map<String, String> getScopes() {
        return scopes;
    }

    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return "OAuthServiceScopes{" +
                "scopes=" + scopes +
                '}';
    }
}
