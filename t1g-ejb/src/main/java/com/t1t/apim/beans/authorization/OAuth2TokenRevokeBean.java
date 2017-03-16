package com.t1t.apim.beans.authorization;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuth2TokenRevokeBean implements Serializable {

    private String organizationId;
    private String applicationId;
    private String version;
    private String id;
    private String gatewayId;
    private String authenticatedUserId;
    private String credentialId;

    public OAuth2TokenRevokeBean(String organizationId, String applicationId, String version, String id, String gatewayId, String authenticatedUserId, String credentialId) {
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.version = version;
        this.id = id;
        this.gatewayId = gatewayId;
        this.authenticatedUserId = authenticatedUserId;
        this.credentialId = credentialId;
    }

    public String getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(String authenticatedUserId) {
        this.authenticatedUserId = authenticatedUserId;
    }

    public OAuth2TokenRevokeBean() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuth2TokenRevokeBean that = (OAuth2TokenRevokeBean) o;

        if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
            return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (gatewayId != null ? !gatewayId.equals(that.gatewayId) : that.gatewayId != null) return false;
        if (authenticatedUserId != null ? !authenticatedUserId.equals(that.authenticatedUserId) : that.authenticatedUserId != null)
            return false;
        return credentialId != null ? credentialId.equals(that.credentialId) : that.credentialId == null;

    }

    @Override
    public int hashCode() {
        int result = organizationId != null ? organizationId.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
        result = 31 * result + (authenticatedUserId != null ? authenticatedUserId.hashCode() : 0);
        result = 31 * result + (credentialId != null ? credentialId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuth2TokenRevokeBean{" +
                "organizationId='" + organizationId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", version='" + version + '\'' +
                ", id='" + id + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", authenticatedUserId='" + authenticatedUserId + '\'' +
                ", credentialId='" + credentialId + '\'' +
                '}';
    }
}