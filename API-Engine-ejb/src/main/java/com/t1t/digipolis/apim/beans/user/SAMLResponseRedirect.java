package com.t1t.digipolis.apim.beans.user;

/**
 * Created by michallispashidis on 07/09/15.
 */
public class SAMLResponseRedirect {
    private String clientUrl;
    private String token;
    private String ttl;

    public SAMLResponseRedirect() {
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "SAMLResponseRedirect{" +
                "clientUrl='" + clientUrl + '\'' +
                ", token='" + token + '\'' +
                ", ttl='" + ttl + '\'' +

                '}';
    }
}
