
package com.t1t.kong.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginOAuthConsumerResponse implements KongConfigValue
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("consumer_id")
    @Expose
    private String consumerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("client_secret")
    @Expose
    private String clientSecret;
    @SerializedName("redirect_uri")
    @Expose
    private Set<String> redirectUri = new LinkedHashSet<String>();
    @SerializedName("created_at")
    @Expose
    private Double createdAt;

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

    public KongPluginOAuthConsumerResponse withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The consumerId
     */
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * 
     * @param consumerId
     *     The consumer_id
     */
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public KongPluginOAuthConsumerResponse withConsumerId(String consumerId) {
        this.consumerId = consumerId;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public KongPluginOAuthConsumerResponse withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 
     * @param clientId
     *     The client_id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public KongPluginOAuthConsumerResponse withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * 
     * @return
     *     The clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * 
     * @param clientSecret
     *     The client_secret
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public KongPluginOAuthConsumerResponse withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * 
     * @return
     *     The redirectUri
     */
    public Set<String> getRedirectUri() {
        return redirectUri;
    }

    /**
     * 
     * @param redirectUri
     *     The redirect_uri
     */
    public void setRedirectUri(Set<String> redirectUri) {
        this.redirectUri = redirectUri;
    }

    public KongPluginOAuthConsumerResponse withRedirectUri(Set<String> redirectUri) {
        this.redirectUri = redirectUri;
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

    public KongPluginOAuthConsumerResponse withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(consumerId).append(name).append(clientId).append(clientSecret).append(redirectUri).append(createdAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginOAuthConsumerResponse) == false) {
            return false;
        }
        KongPluginOAuthConsumerResponse rhs = ((KongPluginOAuthConsumerResponse) other);
        return new EqualsBuilder().append(id, rhs.id).append(consumerId, rhs.consumerId).append(name, rhs.name).append(clientId, rhs.clientId).append(clientSecret, rhs.clientSecret).append(redirectUri, rhs.redirectUri).append(createdAt, rhs.createdAt).isEquals();
    }

}
