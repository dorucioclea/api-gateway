package com.t1t.apim.beans.jwt;

/**
 * Created by michallispashidis on 18/01/16.
 */
public class JWTRequest {
    private String samlResponse;

    public String getSamlResponse() {
        return samlResponse;
    }

    public void setSamlResponse(String samlResponse) {
        this.samlResponse = samlResponse;
    }

    @Override
    public String toString() {
        return "JWTRequest{" +
                "samlResponse='" + samlResponse + '\'' +
                '}';
    }
}
