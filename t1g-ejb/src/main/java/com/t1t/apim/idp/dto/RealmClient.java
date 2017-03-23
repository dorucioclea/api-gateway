package com.t1t.apim.idp.dto;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RealmClient {

    private String id;
    private String secret;
    private List<String> redirectUris;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RealmClient)) return false;

        RealmClient that = (RealmClient) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RealmClient{" +
                "id='" + id + '\'' +
                ", secret='" + secret + '\'' +
                ", redirectUris=" + redirectUris +
                '}';
    }
}