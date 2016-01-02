package com.t1t.digipolis.apim.beans.cache;

import com.t1t.digipolis.apim.beans.user.ClientTokeType;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by michallispashidis on 23/11/15.
 */
public class WebClientCacheBean implements Serializable{
    private Integer tokenExpirationTimeMinutes;
    private String clientAppRedirect;//only used as audience claim for JWT - optional for other tokentypes
    private ClientTokeType token;
    private Map<String, String> optionalClaimset;

    public Integer getTokenExpirationTimeMinutes() {
        return tokenExpirationTimeMinutes;
    }

    public void setTokenExpirationTimeMinutes(Integer tokenExpirationTimeMinutes) {
        this.tokenExpirationTimeMinutes = tokenExpirationTimeMinutes;
    }

    public String getClientAppRedirect() {
        return clientAppRedirect;
    }

    public void setClientAppRedirect(String clientAppRedirect) {
        this.clientAppRedirect = clientAppRedirect;
    }

    public ClientTokeType getToken() {
        return token;
    }

    public void setToken(ClientTokeType token) {
        this.token = token;
    }

    public Map<String, String> getOptionalClaimset() {
        return optionalClaimset;
    }

    public void setOptionalClaimset(Map<String, String> optionalClaimset) {
        this.optionalClaimset = optionalClaimset;
    }

    @Override
    public String toString() {
        return "WebClientCacheBean{" +
                "tokenExpirationTimeMinutes=" + tokenExpirationTimeMinutes +
                ", clientAppRedirect='" + clientAppRedirect + '\'' +
                ", token=" + token +
                ", optionalClaimset=" + optionalClaimset +
                '}';
    }
}
