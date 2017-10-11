package com.t1t.apim.beans.jwt;

/**
 * Created by michallispashidis on 18/01/16.
 */
public class JWT {

    private String token;

    public JWT() {
    }

    public JWT(String token) {
        this.token = token;
    }

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
