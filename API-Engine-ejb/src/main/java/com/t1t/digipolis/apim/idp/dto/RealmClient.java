package com.t1t.digipolis.apim.idp.dto;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RealmClient {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                '}';
    }
}