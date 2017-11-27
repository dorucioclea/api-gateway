
package com.t1t.kong.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginOAuthEnhanced implements KongConfigValue
{

    /**
     * 
     */
    @SerializedName("scopes")
    @Expose
    private List<Object> scopes = new ArrayList<Object>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("mandatory_scope")
    @Expose
    private Boolean mandatoryScope = false;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("token_expiration")
    @Expose
    private Long tokenExpiration = 7200L;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enable_authorization_code")
    @Expose
    private Boolean enableAuthorizationCode = false;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enable_client_credentials")
    @Expose
    private Boolean enableClientCredentials = false;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enable_implicit_grant")
    @Expose
    private Boolean enableImplicitGrant = false;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enable_password_grant")
    @Expose
    private Boolean enablePasswordGrant = false;
    @SerializedName("hide_credentials")
    @Expose
    private Boolean hideCredentials = false;
    /**
     * 
     */
    @SerializedName("provision_key")
    @Expose
    private String provisionKey;
    /**
     * 
     */
    @SerializedName("accept_http_if_already_terminated")
    @Expose
    private Boolean acceptHttpIfAlreadyTerminated = false;
    /**
     * 
     */
    @SerializedName("anonymous")
    @Expose
    private String anonymous;

    /**
     * 
     * @return
     *     The scopes
     */
    public List<Object> getScopes() {
        return scopes;
    }

    /**
     * 
     * @param scopes
     *     The scopes
     */
    public void setScopes(List<Object> scopes) {
        this.scopes = scopes;
    }

    public KongPluginOAuthEnhanced withScopes(List<Object> scopes) {
        this.scopes = scopes;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The mandatoryScope
     */
    public Boolean getMandatoryScope() {
        return mandatoryScope;
    }

    /**
     * 
     * (Required)
     * 
     * @param mandatoryScope
     *     The mandatory_scope
     */
    public void setMandatoryScope(Boolean mandatoryScope) {
        this.mandatoryScope = mandatoryScope;
    }

    public KongPluginOAuthEnhanced withMandatoryScope(Boolean mandatoryScope) {
        this.mandatoryScope = mandatoryScope;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The tokenExpiration
     */
    public Long getTokenExpiration() {
        return tokenExpiration;
    }

    /**
     * 
     * (Required)
     * 
     * @param tokenExpiration
     *     The token_expiration
     */
    public void setTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public KongPluginOAuthEnhanced withTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The enableAuthorizationCode
     */
    public Boolean getEnableAuthorizationCode() {
        return enableAuthorizationCode;
    }

    /**
     * 
     * (Required)
     * 
     * @param enableAuthorizationCode
     *     The enable_authorization_code
     */
    public void setEnableAuthorizationCode(Boolean enableAuthorizationCode) {
        this.enableAuthorizationCode = enableAuthorizationCode;
    }

    public KongPluginOAuthEnhanced withEnableAuthorizationCode(Boolean enableAuthorizationCode) {
        this.enableAuthorizationCode = enableAuthorizationCode;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The enableClientCredentials
     */
    public Boolean getEnableClientCredentials() {
        return enableClientCredentials;
    }

    /**
     * 
     * (Required)
     * 
     * @param enableClientCredentials
     *     The enable_client_credentials
     */
    public void setEnableClientCredentials(Boolean enableClientCredentials) {
        this.enableClientCredentials = enableClientCredentials;
    }

    public KongPluginOAuthEnhanced withEnableClientCredentials(Boolean enableClientCredentials) {
        this.enableClientCredentials = enableClientCredentials;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The enableImplicitGrant
     */
    public Boolean getEnableImplicitGrant() {
        return enableImplicitGrant;
    }

    /**
     * 
     * (Required)
     * 
     * @param enableImplicitGrant
     *     The enable_implicit_grant
     */
    public void setEnableImplicitGrant(Boolean enableImplicitGrant) {
        this.enableImplicitGrant = enableImplicitGrant;
    }

    public KongPluginOAuthEnhanced withEnableImplicitGrant(Boolean enableImplicitGrant) {
        this.enableImplicitGrant = enableImplicitGrant;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The enablePasswordGrant
     */
    public Boolean getEnablePasswordGrant() {
        return enablePasswordGrant;
    }

    /**
     * 
     * (Required)
     * 
     * @param enablePasswordGrant
     *     The enable_password_grant
     */
    public void setEnablePasswordGrant(Boolean enablePasswordGrant) {
        this.enablePasswordGrant = enablePasswordGrant;
    }

    public KongPluginOAuthEnhanced withEnablePasswordGrant(Boolean enablePasswordGrant) {
        this.enablePasswordGrant = enablePasswordGrant;
        return this;
    }

    /**
     * 
     * @return
     *     The hideCredentials
     */
    public Boolean getHideCredentials() {
        return hideCredentials;
    }

    /**
     * 
     * @param hideCredentials
     *     The hide_credentials
     */
    public void setHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
    }

    public KongPluginOAuthEnhanced withHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
        return this;
    }

    /**
     * 
     * @return
     *     The provisionKey
     */
    public String getProvisionKey() {
        return provisionKey;
    }

    /**
     * 
     * @param provisionKey
     *     The provision_key
     */
    public void setProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
    }

    public KongPluginOAuthEnhanced withProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
        return this;
    }

    /**
     * 
     * @return
     *     The acceptHttpIfAlreadyTerminated
     */
    public Boolean getAcceptHttpIfAlreadyTerminated() {
        return acceptHttpIfAlreadyTerminated;
    }

    /**
     * 
     * @param acceptHttpIfAlreadyTerminated
     *     The accept_http_if_already_terminated
     */
    public void setAcceptHttpIfAlreadyTerminated(Boolean acceptHttpIfAlreadyTerminated) {
        this.acceptHttpIfAlreadyTerminated = acceptHttpIfAlreadyTerminated;
    }

    public KongPluginOAuthEnhanced withAcceptHttpIfAlreadyTerminated(Boolean acceptHttpIfAlreadyTerminated) {
        this.acceptHttpIfAlreadyTerminated = acceptHttpIfAlreadyTerminated;
        return this;
    }

    /**
     * 
     * @return
     *     The anonymous
     */
    public String getAnonymous() {
        return anonymous;
    }

    /**
     * 
     * @param anonymous
     *     The anonymous
     */
    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public KongPluginOAuthEnhanced withAnonymous(String anonymous) {
        this.anonymous = anonymous;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scopes).append(mandatoryScope).append(tokenExpiration).append(enableAuthorizationCode).append(enableClientCredentials).append(enableImplicitGrant).append(enablePasswordGrant).append(hideCredentials).append(provisionKey).append(acceptHttpIfAlreadyTerminated).append(anonymous).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginOAuthEnhanced) == false) {
            return false;
        }
        KongPluginOAuthEnhanced rhs = ((KongPluginOAuthEnhanced) other);
        return new EqualsBuilder().append(scopes, rhs.scopes).append(mandatoryScope, rhs.mandatoryScope).append(tokenExpiration, rhs.tokenExpiration).append(enableAuthorizationCode, rhs.enableAuthorizationCode).append(enableClientCredentials, rhs.enableClientCredentials).append(enableImplicitGrant, rhs.enableImplicitGrant).append(enablePasswordGrant, rhs.enablePasswordGrant).append(hideCredentials, rhs.hideCredentials).append(provisionKey, rhs.provisionKey).append(acceptHttpIfAlreadyTerminated, rhs.acceptHttpIfAlreadyTerminated).append(anonymous, rhs.anonymous).isEquals();
    }

}
