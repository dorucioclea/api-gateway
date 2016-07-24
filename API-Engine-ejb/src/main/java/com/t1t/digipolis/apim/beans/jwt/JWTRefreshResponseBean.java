package com.t1t.digipolis.apim.beans.jwt;

/**
 * Created by michallispashidis on 26/11/15.
 */
public class JWTRefreshResponseBean {
    String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "JWTRefreshResponse{" +
                "jwt='" + jwt + '\'' +
                '}';
    }
}
