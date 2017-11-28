
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
public class KongPluginOAuth implements KongConfigValue
{

    @SerializedName("scopes")
    @Expose
    private List<KongPluginOAuthScope> scopes = new ArrayList<KongPluginOAuthScope>();
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
    @SerializedName("hide_credentials")
    @Expose
    private Boolean hideCredentials = false;
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
    @SerializedName("enable_implicit_grant")
    @Expose
    private Boolean enableImplicitGrant = false;
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
    @SerializedName("enable_password_grant")
    @Expose
    private Boolean enablePasswordGrant = false;
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
     * @return
     *     The scopes
     */
    public List<KongPluginOAuthScope> getScopes() {
        return scopes;
    }

    /**
     * 
     * @param scopes
     *     The scopes
     */
    public void setScopes(List<KongPluginOAuthScope> scopes) {
        this.scopes = scopes;
    }

    public KongPluginOAuth withScopes(List<KongPluginOAuthScope> scopes) {
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

    public KongPluginOAuth withMandatoryScope(Boolean mandatoryScope) {
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

    public KongPluginOAuth withTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
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

    public KongPluginOAuth withHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
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

    public KongPluginOAuth withEnableAuthorizationCode(Boolean enableAuthorizationCode) {
        this.enableAuthorizationCode = enableAuthorizationCode;
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

    public KongPluginOAuth withEnableImplicitGrant(Boolean enableImplicitGrant) {
        this.enableImplicitGrant = enableImplicitGrant;
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

    public KongPluginOAuth withEnableClientCredentials(Boolean enableClientCredentials) {
        this.enableClientCredentials = enableClientCredentials;
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

    public KongPluginOAuth withEnablePasswordGrant(Boolean enablePasswordGrant) {
        this.enablePasswordGrant = enablePasswordGrant;
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

    public KongPluginOAuth withProvisionKey(String provisionKey) {
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

    public KongPluginOAuth withAcceptHttpIfAlreadyTerminated(Boolean acceptHttpIfAlreadyTerminated) {
        this.acceptHttpIfAlreadyTerminated = acceptHttpIfAlreadyTerminated;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scopes).append(mandatoryScope).append(tokenExpiration).append(hideCredentials).append(enableAuthorizationCode).append(enableImplicitGrant).append(enableClientCredentials).append(enablePasswordGrant).append(provisionKey).append(acceptHttpIfAlreadyTerminated).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginOAuth) == false) {
            return false;
        }
        KongPluginOAuth rhs = ((KongPluginOAuth) other);
        return new EqualsBuilder().append(scopes, rhs.scopes).append(mandatoryScope, rhs.mandatoryScope).append(tokenExpiration, rhs.tokenExpiration).append(hideCredentials, rhs.hideCredentials).append(enableAuthorizationCode, rhs.enableAuthorizationCode).append(enableImplicitGrant, rhs.enableImplicitGrant).append(enableClientCredentials, rhs.enableClientCredentials).append(enablePasswordGrant, rhs.enablePasswordGrant).append(provisionKey, rhs.provisionKey).append(acceptHttpIfAlreadyTerminated, rhs.acceptHttpIfAlreadyTerminated).isEquals();
    }

}
