package com.t1t.apim.beans.jwt;

/**
 * Created by michallispashidis on 18/01/16.
 */
public class JWTResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JWTResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
