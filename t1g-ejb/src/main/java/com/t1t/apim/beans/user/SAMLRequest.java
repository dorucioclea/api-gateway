package com.t1t.apim.beans.user;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class SAMLRequest implements Serializable {
    private String idpUrl;
    private String spUrl;
    private String spName;
    private String clientAppRedirect;//only used as audience claim for JWT - optional for other tokentypes
    private ClientTokeType token;
    private Map<String, String> optionalClaimMap;

    public SAMLRequest() {
    }

    public SAMLRequest(String idpUrl, String spUrl, String spName, String clientAppRedirect, ClientTokeType token) {
        this.idpUrl = idpUrl;
        this.spUrl = spUrl;
        this.spName = spName;
        this.clientAppRedirect = clientAppRedirect;
        this.token = token;
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

    public Map<String, String> getOptionalClaimMap() {
        return optionalClaimMap;
    }

    public void setOptionalClaimMap(Map<String, String> optionalClaimMap) {
        this.optionalClaimMap = optionalClaimMap;
    }

    @Override
    public String toString() {
        return "SAMLRequest{" +
                "idpUrl='" + idpUrl + '\'' +
                ", spUrl='" + spUrl + '\'' +
                ", spName='" + spName + '\'' +
                ", clientAppRedirect='" + clientAppRedirect + '\'' +
                ", token=" + token +
                ", optionalClaimMap=" + optionalClaimMap +
                '}';
    }
}
