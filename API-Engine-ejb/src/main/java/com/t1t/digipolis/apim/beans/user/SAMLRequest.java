package com.t1t.digipolis.apim.beans.user;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class SAMLRequest implements Serializable{
    private String idpUrl;
    private String spUrl;
    private String spName;

    public SAMLRequest() {
    }

    public SAMLRequest(String idpUrl, String spUrl, String spName) {
        this.idpUrl = idpUrl;
        this.spUrl = spUrl;
        this.spName = spName;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpUrl() {
        return spUrl;
    }

    public void setSpUrl(String spUrl) {
        this.spUrl = spUrl;
    }

    public String getIdpUrl() {
        return idpUrl;
    }

    public void setIdpUrl(String idpUrl) {
        this.idpUrl = idpUrl;
    }

    @Override
    public String toString() {
        return "SAMLRequest{" +
                "idpUrl='" + idpUrl + '\'' +
                ", spUrl='" + spUrl + '\'' +
                ", spName='" + spName + '\'' +
                '}';
    }
}
