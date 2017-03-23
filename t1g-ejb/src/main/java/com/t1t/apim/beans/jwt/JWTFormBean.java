package com.t1t.apim.beans.jwt;

import java.util.Set;

/**
 * Created by michallispashidis on 26/11/15.
 */
public class JWTFormBean {
    private Set<String> claims_to_verify;

    public Set<String> getClaims_to_verify() {
        return claims_to_verify;
    }

    public void setClaims_to_verify(Set<String> claims_to_verify) {
        this.claims_to_verify = claims_to_verify;
    }

    @Override
    public String toString() {
        return "JWTFormBean{" +
                "claims_to_verify=" + claims_to_verify +
                '}';
    }
}
