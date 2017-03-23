package com.t1t.apim.beans.authorization;

import com.t1t.kong.model.KongOAuthToken;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
        if (!(o instanceof OAuth2TokenBean)) return false;

        OAuth2TokenBean that = (OAuth2TokenBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (credentialId != null ? !credentialId.equals(that.credentialId) : that.credentialId != null) return false;
        if (tokenType != null ? !tokenType.equals(that.tokenType) : that.tokenType != null) return false;
        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null) return false;
        if (refreshToken != null ? !refreshToken.equals(that.refreshToken) : that.refreshToken != null) return false;
        if (expiresIn != null ? !expiresIn.equals(that.expiresIn) : that.expiresIn != null) return false;
        if (authenticatedUserId != null ? !authenticatedUserId.equals(that.authenticatedUserId) : that.authenticatedUserId != null)
            return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;
        return gatewayId != null ? gatewayId.equals(that.gatewayId) : that.gatewayId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (credentialId != null ? credentialId.hashCode() : 0);
        result = 31 * result + (tokenType != null ? tokenType.hashCode() : 0);
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + (expiresIn != null ? expiresIn.hashCode() : 0);
        result = 31 * result + (authenticatedUserId != null ? authenticatedUserId.hashCode() : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
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