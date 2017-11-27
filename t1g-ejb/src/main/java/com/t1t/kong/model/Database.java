
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Database {

    @SerializedName("oauth2_credentials")
    @Expose
    private Long oauth2Credentials;
    @SerializedName("jwt_secrets")
    @Expose
    private Long jwtSecrets;
    @SerializedName("response_ratelimiting_metrics")
    @Expose
    private Long responseRatelimitingMetrics;
    @SerializedName("keyauth_credentials")
    @Expose
    private Long keyauthCredentials;
    @SerializedName("oauth2_authorization_codes")
    @Expose
    private Long oauth2AuthorizationCodes;
    @SerializedName("acls")
    @Expose
    private Long acls;
    @SerializedName("apis")
    @Expose
    private Long apis;
    @SerializedName("basicauth_credentials")
    @Expose
    private Long basicauthCredentials;
    @SerializedName("consumers")
    @Expose
    private Long consumers;
    @SerializedName("ratelimiting_metrics")
    @Expose
    private Long ratelimitingMetrics;
    @SerializedName("oauth2_tokens")
    @Expose
    private Long oauth2Tokens;
    @SerializedName("nodes")
    @Expose
    private Long nodes;
    @SerializedName("hmacauth_credentials")
    @Expose
    private Long hmacauthCredentials;
    @SerializedName("plugins")
    @Expose
    private Long plugins;

    /**
     * 
     * @return
     *     The oauth2Credentials
     */
    public Long getOauth2Credentials() {
        return oauth2Credentials;
    }

    /**
     * 
     * @param oauth2Credentials
     *     The oauth2_credentials
     */
    public void setOauth2Credentials(Long oauth2Credentials) {
        this.oauth2Credentials = oauth2Credentials;
    }

    public Database withOauth2Credentials(Long oauth2Credentials) {
        this.oauth2Credentials = oauth2Credentials;
        return this;
    }

    /**
     * 
     * @return
     *     The jwtSecrets
     */
    public Long getJwtSecrets() {
        return jwtSecrets;
    }

    /**
     * 
     * @param jwtSecrets
     *     The jwt_secrets
     */
    public void setJwtSecrets(Long jwtSecrets) {
        this.jwtSecrets = jwtSecrets;
    }

    public Database withJwtSecrets(Long jwtSecrets) {
        this.jwtSecrets = jwtSecrets;
        return this;
    }

    /**
     * 
     * @return
     *     The responseRatelimitingMetrics
     */
    public Long getResponseRatelimitingMetrics() {
        return responseRatelimitingMetrics;
    }

    /**
     * 
     * @param responseRatelimitingMetrics
     *     The response_ratelimiting_metrics
     */
    public void setResponseRatelimitingMetrics(Long responseRatelimitingMetrics) {
        this.responseRatelimitingMetrics = responseRatelimitingMetrics;
    }

    public Database withResponseRatelimitingMetrics(Long responseRatelimitingMetrics) {
        this.responseRatelimitingMetrics = responseRatelimitingMetrics;
        return this;
    }

    /**
     * 
     * @return
     *     The keyauthCredentials
     */
    public Long getKeyauthCredentials() {
        return keyauthCredentials;
    }

    /**
     * 
     * @param keyauthCredentials
     *     The keyauth_credentials
     */
    public void setKeyauthCredentials(Long keyauthCredentials) {
        this.keyauthCredentials = keyauthCredentials;
    }

    public Database withKeyauthCredentials(Long keyauthCredentials) {
        this.keyauthCredentials = keyauthCredentials;
        return this;
    }

    /**
     * 
     * @return
     *     The oauth2AuthorizationCodes
     */
    public Long getOauth2AuthorizationCodes() {
        return oauth2AuthorizationCodes;
    }

    /**
     * 
     * @param oauth2AuthorizationCodes
     *     The oauth2_authorization_codes
     */
    public void setOauth2AuthorizationCodes(Long oauth2AuthorizationCodes) {
        this.oauth2AuthorizationCodes = oauth2AuthorizationCodes;
    }

    public Database withOauth2AuthorizationCodes(Long oauth2AuthorizationCodes) {
        this.oauth2AuthorizationCodes = oauth2AuthorizationCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The acls
     */
    public Long getAcls() {
        return acls;
    }

    /**
     * 
     * @param acls
     *     The acls
     */
    public void setAcls(Long acls) {
        this.acls = acls;
    }

    public Database withAcls(Long acls) {
        this.acls = acls;
        return this;
    }

    /**
     * 
     * @return
     *     The apis
     */
    public Long getApis() {
        return apis;
    }

    /**
     * 
     * @param apis
     *     The apis
     */
    public void setApis(Long apis) {
        this.apis = apis;
    }

    public Database withApis(Long apis) {
        this.apis = apis;
        return this;
    }

    /**
     * 
     * @return
     *     The basicauthCredentials
     */
    public Long getBasicauthCredentials() {
        return basicauthCredentials;
    }

    /**
     * 
     * @param basicauthCredentials
     *     The basicauth_credentials
     */
    public void setBasicauthCredentials(Long basicauthCredentials) {
        this.basicauthCredentials = basicauthCredentials;
    }

    public Database withBasicauthCredentials(Long basicauthCredentials) {
        this.basicauthCredentials = basicauthCredentials;
        return this;
    }

    /**
     * 
     * @return
     *     The consumers
     */
    public Long getConsumers() {
        return consumers;
    }

    /**
     * 
     * @param consumers
     *     The consumers
     */
    public void setConsumers(Long consumers) {
        this.consumers = consumers;
    }

    public Database withConsumers(Long consumers) {
        this.consumers = consumers;
        return this;
    }

    /**
     * 
     * @return
     *     The ratelimitingMetrics
     */
    public Long getRatelimitingMetrics() {
        return ratelimitingMetrics;
    }

    /**
     * 
     * @param ratelimitingMetrics
     *     The ratelimiting_metrics
     */
    public void setRatelimitingMetrics(Long ratelimitingMetrics) {
        this.ratelimitingMetrics = ratelimitingMetrics;
    }

    public Database withRatelimitingMetrics(Long ratelimitingMetrics) {
        this.ratelimitingMetrics = ratelimitingMetrics;
        return this;
    }

    /**
     * 
     * @return
     *     The oauth2Tokens
     */
    public Long getOauth2Tokens() {
        return oauth2Tokens;
    }

    /**
     * 
     * @param oauth2Tokens
     *     The oauth2_tokens
     */
    public void setOauth2Tokens(Long oauth2Tokens) {
        this.oauth2Tokens = oauth2Tokens;
    }

    public Database withOauth2Tokens(Long oauth2Tokens) {
        this.oauth2Tokens = oauth2Tokens;
        return this;
    }

    /**
     * 
     * @return
     *     The nodes
     */
    public Long getNodes() {
        return nodes;
    }

    /**
     * 
     * @param nodes
     *     The nodes
     */
    public void setNodes(Long nodes) {
        this.nodes = nodes;
    }

    public Database withNodes(Long nodes) {
        this.nodes = nodes;
        return this;
    }

    /**
     * 
     * @return
     *     The hmacauthCredentials
     */
    public Long getHmacauthCredentials() {
        return hmacauthCredentials;
    }

    /**
     * 
     * @param hmacauthCredentials
     *     The hmacauth_credentials
     */
    public void setHmacauthCredentials(Long hmacauthCredentials) {
        this.hmacauthCredentials = hmacauthCredentials;
    }

    public Database withHmacauthCredentials(Long hmacauthCredentials) {
        this.hmacauthCredentials = hmacauthCredentials;
        return this;
    }

    /**
     * 
     * @return
     *     The plugins
     */
    public Long getPlugins() {
        return plugins;
    }

    /**
     * 
     * @param plugins
     *     The plugins
     */
    public void setPlugins(Long plugins) {
        this.plugins = plugins;
    }

    public Database withPlugins(Long plugins) {
        this.plugins = plugins;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(oauth2Credentials).append(jwtSecrets).append(responseRatelimitingMetrics).append(keyauthCredentials).append(oauth2AuthorizationCodes).append(acls).append(apis).append(basicauthCredentials).append(consumers).append(ratelimitingMetrics).append(oauth2Tokens).append(nodes).append(hmacauthCredentials).append(plugins).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Database) == false) {
            return false;
        }
        Database rhs = ((Database) other);
        return new EqualsBuilder().append(oauth2Credentials, rhs.oauth2Credentials).append(jwtSecrets, rhs.jwtSecrets).append(responseRatelimitingMetrics, rhs.responseRatelimitingMetrics).append(keyauthCredentials, rhs.keyauthCredentials).append(oauth2AuthorizationCodes, rhs.oauth2AuthorizationCodes).append(acls, rhs.acls).append(apis, rhs.apis).append(basicauthCredentials, rhs.basicauthCredentials).append(consumers, rhs.consumers).append(ratelimitingMetrics, rhs.ratelimitingMetrics).append(oauth2Tokens, rhs.oauth2Tokens).append(nodes, rhs.nodes).append(hmacauthCredentials, rhs.hmacauthCredentials).append(plugins, rhs.plugins).isEquals();
    }

}
