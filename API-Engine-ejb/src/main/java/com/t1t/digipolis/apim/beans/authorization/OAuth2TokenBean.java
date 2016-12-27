package com.t1t.digipolis.apim.beans.authorization;

import com.t1t.digipolis.kong.model.KongOAuthToken;

import javax.persistence.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Entity
@Table(name = "oauth2_tokens")
public class OAuth2TokenBean {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "credential_id")
    private String credentialId;
    @Column(name = "token_type")
    private String tokenType;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "expires_in")
    private Long expiresIn;
    @Column(name = "authenticated_userid")
    private String authenticatedUserId;
    @Column(name = "scope")
    private String scope;
    @Column(name = "gateway_id")
    private String gatewayId;

    public OAuth2TokenBean() {
    }

    public OAuth2TokenBean(String id, String credentialId, String tokenType, String accessToken, String refreshToken, Long expiresIn, String authenticatedUserId, String scope, String gatewayId) {
        this.id = id;
        this.credentialId = credentialId;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.authenticatedUserId = authenticatedUserId;
        this.scope = scope;
        this.gatewayId = gatewayId;
    }

    public OAuth2TokenBean(KongOAuthToken token, String gatewayId) {
        this.id = token.getId();
        this.credentialId = token.getCredentialId();
        this.tokenType = token.getTokenType();
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.expiresIn = token.getExpiresIn().longValue();
        this.authenticatedUserId = token.getAuthenticatedUserid();
        this.scope = token.getScope();
        this.gatewayId = gatewayId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(String authenticatedUserId) {
        this.authenticatedUserId = authenticatedUserId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuth2TokenBean that = (OAuth2TokenBean) o;

        if (!id.equals(that.id)) return false;
        if (!credentialId.equals(that.credentialId)) return false;
        if (!tokenType.equals(that.tokenType)) return false;
        if (!accessToken.equals(that.accessToken)) return false;
        if (refreshToken != null ? !refreshToken.equals(that.refreshToken) : that.refreshToken != null) return false;
        if (!expiresIn.equals(that.expiresIn)) return false;
        if (!authenticatedUserId.equals(that.authenticatedUserId)) return false;
        if (!scope.equals(that.scope)) return false;
        return gatewayId.equals(that.gatewayId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + credentialId.hashCode();
        result = 31 * result + tokenType.hashCode();
        result = 31 * result + accessToken.hashCode();
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + expiresIn.hashCode();
        result = 31 * result + authenticatedUserId.hashCode();
        result = 31 * result + scope.hashCode();
        result = 31 * result + gatewayId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OAuth2TokenBean{" +
                "id='" + id + '\'' +
                ", credentialId='" + credentialId + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", authenticatedUserId='" + authenticatedUserId + '\'' +
                ", scope='" + scope + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                '}';
    }
}