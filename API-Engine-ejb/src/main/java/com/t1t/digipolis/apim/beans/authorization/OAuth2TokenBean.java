package com.t1t.digipolis.apim.beans.authorization;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.kong.model.KongOAuthToken;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class OAuth2TokenBean implements Serializable {

    private String scope;
    private String accessToken;
    private String authenticatedUserid;
    private String id;
    private String tokenType;
    private String credentialId;
    private Date createdAt;
    private Integer expiresIn;
    private String refreshToken;
    private String gatewayId;
    private String organizationId;
    private String applicationId;
    private String version;

    public OAuth2TokenBean() {
    }

    public OAuth2TokenBean(String scope, String accessToken, String authenticatedUserid, String id, String tokenType, String credentialId, Date createdAt, Integer expiresIn, String refreshToken, String gatewayId, String organizationId, String applicationId, String version) {
        this.scope = scope;
        this.accessToken = accessToken;
        this.authenticatedUserid = authenticatedUserid;
        this.id = id;
        this.tokenType = tokenType;
        this.credentialId = credentialId;
        this.createdAt = createdAt;
        this.expiresIn = expiresIn;
        this.gatewayId = gatewayId;
        this.refreshToken = refreshToken;
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.version = version;
    }

    public OAuth2TokenBean(KongOAuthToken token, String gatewayId, ApplicationVersionBean avb) {
        this.scope = token.getScope();
        this.accessToken = token.getAccessToken();
        this.authenticatedUserid = token.getAuthenticatedUserid();
        this.id = token.getId();
        this.tokenType = token.getTokenType();
        this.credentialId = token.getCredentialId();
        this.createdAt = new Date(token.getCreatedAt().longValue());
        this.expiresIn = token.getExpiresIn();
        this.refreshToken = token.getRefreshToken();
        this.gatewayId = gatewayId;
        this.organizationId = avb.getApplication().getOrganization().getId();
        this.applicationId = avb.getApplication().getId();
        this.version = avb.getVersion();
    }

    public OAuth2TokenBean(KongOAuthToken token, String gatewayId, String organizationId, String applicationId, String version) {
        this.scope = token.getScope();
        this.accessToken = token.getAccessToken();
        this.authenticatedUserid = token.getAuthenticatedUserid();
        this.id = token.getId();
        this.tokenType = token.getTokenType();
        this.credentialId = token.getCredentialId();
        this.createdAt = new Date(token.getCreatedAt().longValue());
        this.expiresIn = token.getExpiresIn();
        this.refreshToken = token.getRefreshToken();
        this.gatewayId = gatewayId;
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAuthenticatedUserid() {
        return authenticatedUserid;
    }

    public void setAuthenticatedUserid(String authenticatedUserid) {
        this.authenticatedUserid = authenticatedUserid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuth2TokenBean that = (OAuth2TokenBean) o;

        if (!id.equals(that.id)) return false;
        return gatewayId.equals(that.gatewayId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + gatewayId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OAuth2TokenBean{" +
                "scope='" + scope + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", authenticatedUserid='" + authenticatedUserid + '\'' +
                ", id='" + id + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", credentialId='" + credentialId + '\'' +
                ", createdAt=" + createdAt +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}