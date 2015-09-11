package com.t1t.digipolis.apim.beans.user;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class SAMLRequest implements Serializable{
    private String idpUrl;
    private String spUrl;
    private String spName;
    private String clientAppRedirect;
    private ClientTokeType token;

    public SAMLRequest() {
    }

    public SAMLRequest(String idpUrl, String spUrl, String spName, String clientAppRedirect,ClientTokeType token) {
        this.idpUrl = idpUrl;
        this.spUrl = spUrl;
        this.spName = spName;
        this.clientAppRedirect = clientAppRedirect;
        this.token=token;
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

    public String getClientAppRedirect() {
        return clientAppRedirect;
    }

    public void setClientAppRedirect(String clientAppRedirect) {
        this.clientAppRedirect = clientAppRedirect;
    }

    public ClientTokeType getToken() {
        return token;
    }

    public void setToken(ClientTokeType token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SAMLRequest{" +
                "idpUrl='" + idpUrl + '\'' +
                ", spUrl='" + spUrl + '\'' +
                ", spName='" + spName + '\'' +
                ", clientAppRedirect='" + clientAppRedirect + '\'' +
                ", token=" + token +
                '}';
    }
}
