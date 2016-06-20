package com.t1t.digipolis.apim.beans.apps;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewOAuthCredentialsBean implements Serializable {
    private String revokedClientId;
    private String revokedClientSecret;
    private String newClientId;
    private String newClientSecret;

    public NewOAuthCredentialsBean() {
    }

    public NewOAuthCredentialsBean(String revokedClientId, String revokedClientSecret, String newClientId, String newClientSecret) {
        this.revokedClientId = revokedClientId;
        this.revokedClientSecret = revokedClientSecret;
        this.newClientId = newClientId;
        this.newClientSecret = newClientSecret;
    }

    public String getRevokedClientId() {
        return revokedClientId;
    }

    public void setRevokedClientId(String revokedClientId) {
        this.revokedClientId = revokedClientId;
    }

    public String getRevokedClientSecret() {
        return revokedClientSecret;
    }

    public void setRevokedClientSecret(String revokedClientSecret) {
        this.revokedClientSecret = revokedClientSecret;
    }

    public String getNewClientId() {
        return newClientId;
    }

    public void setNewClientId(String newClientId) {
        this.newClientId = newClientId;
    }

    public String getNewClientSecret() {
        return newClientSecret;
    }

    public void setNewClientSecret(String newClientSecret) {
        this.newClientSecret = newClientSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewOAuthCredentialsBean that = (NewOAuthCredentialsBean) o;

        if (revokedClientId != null ? !revokedClientId.equals(that.revokedClientId) : that.revokedClientId != null)
            return false;
        if (revokedClientSecret != null ? !revokedClientSecret.equals(that.revokedClientSecret) : that.revokedClientSecret != null)
            return false;
        if (newClientId != null ? !newClientId.equals(that.newClientId) : that.newClientId != null) return false;
        return newClientSecret != null ? newClientSecret.equals(that.newClientSecret) : that.newClientSecret == null;

    }

    @Override
    public int hashCode() {
        int result = revokedClientId != null ? revokedClientId.hashCode() : 0;
        result = 31 * result + (revokedClientSecret != null ? revokedClientSecret.hashCode() : 0);
        result = 31 * result + (newClientId != null ? newClientId.hashCode() : 0);
        result = 31 * result + (newClientSecret != null ? newClientSecret.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewOAuthCredentialsBean{" +
                "revokedClientId='" + revokedClientId + '\'' +
                ", revokedClientSecret='" + revokedClientSecret + '\'' +
                ", newClientId='" + newClientId + '\'' +
                ", newClientSecret='" + newClientSecret + '\'' +
                '}';
    }
}