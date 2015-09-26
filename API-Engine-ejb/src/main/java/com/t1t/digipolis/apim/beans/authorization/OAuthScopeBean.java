package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class OAuthScopeBean {
    private String scopeName;
    private String scopeDescription;

    public OAuthScopeBean() {
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getScopeDescription() {
        return scopeDescription;
    }

    public void setScopeDescription(String scopeDescription) {
        this.scopeDescription = scopeDescription;
    }

    @Override
    public String toString() {
        return "OAuthScopeBean{" +
                "scopeName='" + scopeName + '\'' +
                ", scopeDescription='" + scopeDescription + '\'' +
                '}';
    }
}
