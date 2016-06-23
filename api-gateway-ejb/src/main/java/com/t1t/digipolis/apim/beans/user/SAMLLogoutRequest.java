package com.t1t.digipolis.apim.beans.user;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class SAMLLogoutRequest implements Serializable{
    private String idpUrl;
    private String spName;
    private String username;

    public SAMLLogoutRequest() {
    }

    public SAMLLogoutRequest(String idpUrl, String spName, String username) {
        this.idpUrl = idpUrl;
        this.spName = spName;
        this.username = username;
    }

    public String getIdpUrl() {
        return idpUrl;
    }

    public void setIdpUrl(String idpUrl) {
        this.idpUrl = idpUrl;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SAMLLogoutRequest{" +
                "idpUrl='" + idpUrl + '\'' +
                ", spName='" + spName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
