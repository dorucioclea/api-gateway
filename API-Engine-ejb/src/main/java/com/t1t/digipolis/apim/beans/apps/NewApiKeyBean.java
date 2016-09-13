package com.t1t.digipolis.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewApiKeyBean implements Serializable {

    private String organizationId;

    private String applicationId;

    private String version;

    private String revokedKey;

    private String newKey;

    public NewApiKeyBean() {}

    public NewApiKeyBean(String organizationId, String applicationId, String version, String revokedKey, String newKey) {
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewApiKeyBean that = (NewApiKeyBean) o;

        if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
            return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (revokedKey != null ? !revokedKey.equals(that.revokedKey) : that.revokedKey != null) return false;
        return newKey != null ? newKey.equals(that.newKey) : that.newKey == null;

    }

    @Override
    public int hashCode() {
        int result = organizationId != null ? organizationId.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (revokedKey != null ? revokedKey.hashCode() : 0);
        result = 31 * result + (newKey != null ? newKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewApiKeyBean{" +
                "organizationId='" + organizationId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", version='" + version + '\'' +
                ", revokedKey='" + revokedKey + '\'' +
                ", newKey='" + newKey + '\'' +
                '}';
    }
}