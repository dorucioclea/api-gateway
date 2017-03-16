package com.t1t.apim.beans.gateways;

import java.io.Serializable;

/**
 * Created by michallispashidis on 6/04/16.
 * Gateway DTO used for example in OAuth endpoint publication.
 *
 */
public class Gateway implements Serializable {
    private String id;
    private String oauthBasePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOauthBasePath() {
        return oauthBasePath;
    }

    public void setOauthBasePath(String oauthBasePath) {
        this.oauthBasePath = oauthBasePath;
    }

    @Override
    public String toString() {
        return "Gateway{" +
                "id='" + id + '\'' +
                ", oauthBasePath='" + oauthBasePath + '\'' +
                '}';
    }
}
