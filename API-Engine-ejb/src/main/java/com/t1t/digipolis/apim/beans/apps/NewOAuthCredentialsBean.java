package com.t1t.digipolis.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewOAuthCredentialsBean implements Serializable {
    private String revokedClientId;
    private String revokedClientSecret;
    private String newClientId;
    private String newClientSecret;
    private String organizationId;
    private String applicationId;
    private String version;

    public NewOAuthCredentialsBean() {
    }

    public NewOAuthCredentialsBean(String revokedClientId, String revokedClientSecret, String newClientId, String newClientSecret, String organizationId, String applicationId, String version) {
        this.revokedClientId = revokedClientId;
        this.revokedClientSecret = revokedClientSecret;
        this.newClientId = newClientId;
        this.newClientSecret = newClientSecret;
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.version = version;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        if (newClientSecret != null ? !newClientSecret.equals(that.newClientSecret) : that.newClientSecret != null)
            return false;
        if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
            return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        return version != null ? version.equals(that.version) : that.version == null;

    }

    @Override
    public int hashCode() {
        int result = revokedClientId != null ? revokedClientId.hashCode() : 0;
        result = 31 * result + (revokedClientSecret != null ? revokedClientSecret.hashCode() : 0);
        result = 31 * result + (newClientId != null ? newClientId.hashCode() : 0);
        result = 31 * result + (newClientSecret != null ? newClientSecret.hashCode() : 0);
        result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewOAuthCredentialsBean{" +
                "revokedClientId='" + revokedClientId + '\'' +
                ", revokedClientSecret='" + revokedClientSecret + '\'' +
                ", newClientId='" + newClientId + '\'' +
                ", newClientSecret='" + newClientSecret + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}