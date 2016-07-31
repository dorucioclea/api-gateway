package com.t1t.digipolis.apim.beans.jwt;

/**
 * Created by michallispashidis on 31/07/16.
 */
public class JWTPubKeyResponse {
    private String x5u;

    public JWTPubKeyResponse(String x5u) {
        this.x5u = x5u;
    }

    public String getX5u() {
        return x5u;
    }

    public void setX5u(String x5u) {
        this.x5u = x5u;
    }

    @Override
    public String toString() {
        return "JWTPubKeyResponse{" +
                "x5u='" + x5u + '\'' +
                '}';
    }
}
