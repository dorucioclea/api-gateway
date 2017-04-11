package com.t1t.apim.beans.idp;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "idps")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IDPBean implements Serializable {

    @Id
    @Column(updatable=false, nullable=false)
    private String id;
    @Column(name = "server_url")
    private String serverUrl;
    @Column(name = "master_realm")
    private String masterRealm;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "encrypted_client_secret")
    private String encryptedClientSecret;
    @Column(name = "default_login_theme_id")
    private String defaultLoginThemeId;
    @Column(name = "default_realm")
    private String defaultRealm;
    @Column(name = "default_client")
    private String defaultClient;
    @Column(name = "default_idp")
    private boolean defaultIdp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getMasterRealm() {
        return masterRealm;
    }

    public void setMasterRealm(String masterRealm) {
        this.masterRealm = masterRealm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEncryptedClientSecret() {
        return encryptedClientSecret;
    }

    public void setEncryptedClientSecret(String clientSecret) {
        this.encryptedClientSecret = clientSecret;
    }

    public String getDefaultLoginThemeId() {
        return defaultLoginThemeId;
    }

    public void setDefaultLoginThemeId(String defaultLoginThemeId) {
        this.defaultLoginThemeId = defaultLoginThemeId;
    }

    public String getDefaultRealm() {
        return defaultRealm;
    }

    public void setDefaultRealm(String defaultRealm) {
        this.defaultRealm = defaultRealm;
    }

    public String getDefaultClient() {
        return defaultClient;
    }

    public void setDefaultClient(String defaultClient) {
        this.defaultClient = defaultClient;
    }

    public boolean isDefaultIdp() {
        return defaultIdp;
    }

    public void setDefaultIdp(boolean defaultIdp) {
        this.defaultIdp = defaultIdp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDPBean)) return false;

        IDPBean idpBean = (IDPBean) o;

        return id.equals(idpBean.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "IDPBean{" +
                "id='" + id + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", masterRealm='" + masterRealm + '\'' +
                ", clientId='" + clientId + '\'' +
                ", encryptedClientSecret='" + encryptedClientSecret + '\'' +
                ", defaultLoginThemeId='" + defaultLoginThemeId + '\'' +
                ", defaultRealm='" + defaultRealm + '\'' +
                ", defaultClient='" + defaultClient + '\'' +
                ", defaultIdp=" + defaultIdp +
                '}';
    }
}