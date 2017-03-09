package com.t1t.digipolis.apim.beans.user;

/**
 * Created by michallispashidis on 07/09/15.
 */
public class SAMLResponseRedirect {
    private String clientUrl;
    private String token;

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

    @Override
    public String toString() {
        return "SAMLResponseRedirect{" +
                "clientUrl='" + clientUrl + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
