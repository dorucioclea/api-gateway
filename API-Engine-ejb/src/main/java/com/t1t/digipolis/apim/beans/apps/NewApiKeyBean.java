package com.t1t.digipolis.apim.beans.apps;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewApiKeyBean implements Serializable {

    private String revokedKey;

    private String newKey;

    public NewApiKeyBean() {}

    public NewApiKeyBean(String revokedKey, String newKey) {
        this.revokedKey = revokedKey;
        this.newKey = newKey;
    }

    public String getNewKey() {
        return newKey;
    }

    public void setNewKey(String newKey) {
        this.newKey = newKey;
    }

    public String getRevokedKey() {
        return revokedKey;
    }

    public void setRevokedKey(String revokedKey) {
        this.revokedKey = revokedKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewApiKeyBean that = (NewApiKeyBean) o;

        if (revokedKey != null ? !revokedKey.equals(that.revokedKey) : that.revokedKey != null) return false;
        return newKey != null ? newKey.equals(that.newKey) : that.newKey == null;

    }

    @Override
    public String toString() {
        return "NewApiKeyBean{" +
                "revokedKey='" + revokedKey + '\'' +
                ", newKey='" + newKey + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = revokedKey != null ? revokedKey.hashCode() : 0;
        result = 31 * result + (newKey != null ? newKey.hashCode() : 0);
        return result;
    }
}