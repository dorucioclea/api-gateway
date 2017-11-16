package com.t1t.apim.beans.jwt;

import java.io.Serializable;

/**
 * Created by michallispashidis on 25/11/15.
 */
public class JWTRefreshRequestBean implements Serializable {

    private String originalJWT;

    public String getOriginalJWT() {
        return originalJWT;
    }

    public void setOriginalJWT(String originalJWT) {
        this.originalJWT = originalJWT;
    }

    @Override
    public String toString() {
        return "JWTRefreshRequestBean{" +
                ", originalJWT='" + originalJWT + '\'' +
                '}';
    }
}
