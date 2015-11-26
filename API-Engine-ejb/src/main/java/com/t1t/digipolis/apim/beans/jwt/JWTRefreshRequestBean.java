package com.t1t.digipolis.apim.beans.jwt;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by michallispashidis on 25/11/15.
 */
public class JWTRefreshRequestBean implements Serializable{
    //optional claims
    private Map<String,String> optionalClaims;
    private String originalJWT;
    private Integer expirationTimeMinutes;

    public Map<String, String> getOptionalClaims() {
        return optionalClaims;
    }

    public void setOptionalClaims(Map<String, String> optionalClaims) {
        this.optionalClaims = optionalClaims;
    }

    public String getOriginalJWT() {
        return originalJWT;
    }

    public void setOriginalJWT(String originalJWT) {
        this.originalJWT = originalJWT;
    }

    public Integer getExpirationTimeMinutes() {
        return expirationTimeMinutes;
    }

    public void setExpirationTimeMinutes(Integer expirationTimeMinutes) {
        this.expirationTimeMinutes = expirationTimeMinutes;
    }

    @Override
    public String toString() {
        return "JWTRefreshRequestBean{" +
                "optionalClaims=" + optionalClaims +
                ", originalJWT='" + originalJWT + '\'' +
                ", expirationTimeMinutes=" + expirationTimeMinutes +
                '}';
    }
}
