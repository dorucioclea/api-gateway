package com.t1t.digipolis.apim.security;

/**
 * Created by michallispashidis on 10/04/16.
 */
public class OAuthExpTimeRequest {
    private Integer expirationTime;

    public Integer getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Integer expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "OAuthExpTimeRequest{" +
                "expirationTime=" + expirationTime +
                '}';
    }
}
