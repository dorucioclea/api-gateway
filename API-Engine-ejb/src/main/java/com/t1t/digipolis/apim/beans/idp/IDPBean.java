package com.t1t.digipolis.apim.beans.idp;

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
    @Column(name = "username")
    private String user;
    @Column(name = "password")
    private String password;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_secret")
    private String clientSecret;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
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
                ", user='" + user + '\'' +
                ", password='XXXXX'" +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='XXXXX'" +
                ", defaultIdp=" + defaultIdp +
                '}';
    }
}