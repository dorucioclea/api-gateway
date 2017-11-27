
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongOAuthToken {

    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("authenticated_userid")
    @Expose
    private String authenticatedUserid;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("credential_id")
    @Expose
    private String credentialId;
    @SerializedName("created_at")
    @Expose
    private Double createdAt;
    @SerializedName("expires_in")
    @Expose
    private Long expiresIn;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    /**
     * 
     * @return
     *     The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * 
     * @param scope
     *     The scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public KongOAuthToken withScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * 
     * @return
     *     The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public KongOAuthToken withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * 
     * @return
     *     The authenticatedUserid
     */
    public String getAuthenticatedUserid() {
        return authenticatedUserid;
    }

    /**
     * 
     * @param authenticatedUserid
     *     The authenticated_userid
     */
    public void setAuthenticatedUserid(String authenticatedUserid) {
        this.authenticatedUserid = authenticatedUserid;
    }

    public KongOAuthToken withAuthenticatedUserid(String authenticatedUserid) {
        this.authenticatedUserid = authenticatedUserid;
        return this;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    public KongOAuthToken withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * 
     * @param tokenType
     *     The token_type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public KongOAuthToken withTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    /**
     * 
     * @return
     *     The credentialId
     */
    public String getCredentialId() {
        return credentialId;
    }

    /**
     * 
     * @param credentialId
     *     The credential_id
     */
    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public KongOAuthToken withCredentialId(String credentialId) {
        this.credentialId = credentialId;
        return this;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public Double getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
    }

    public KongOAuthToken withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 
     * @return
     *     The expiresIn
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * 
     * @param expiresIn
     *     The expires_in
     */
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public KongOAuthToken withExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    /**
     * 
     * @return
     *     The refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 
     * @param refreshToken
     *     The refresh_token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public KongOAuthToken withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scope).append(accessToken).append(authenticatedUserid).append(id).append(tokenType).append(credentialId).append(createdAt).append(expiresIn).append(refreshToken).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongOAuthToken) == false) {
            return false;
        }
        KongOAuthToken rhs = ((KongOAuthToken) other);
        return new EqualsBuilder().append(scope, rhs.scope).append(accessToken, rhs.accessToken).append(authenticatedUserid, rhs.authenticatedUserid).append(id, rhs.id).append(tokenType, rhs.tokenType).append(credentialId, rhs.credentialId).append(createdAt, rhs.createdAt).append(expiresIn, rhs.expiresIn).append(refreshToken, rhs.refreshToken).isEquals();
    }

}
