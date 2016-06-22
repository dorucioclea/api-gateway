package com.t1t.digipolis.apim.beans.jwt;

/**
 * Created by michallispashidis on 26/11/15.
 */
public class JWTFormBean {
    private Boolean claims_to_verify;

    public Boolean getClaims_to_verify() {
        return claims_to_verify;
    }

    public void setClaims_to_verify(Boolean claims_to_verify) {
        this.claims_to_verify = claims_to_verify;
    }

    @Override
    public String toString() {
        return "JWTFormBean{" +
                "claims_to_verify=" + claims_to_verify +
                '}';
    }
}
