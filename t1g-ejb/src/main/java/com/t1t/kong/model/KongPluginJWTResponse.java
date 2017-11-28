
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginJWTResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("consumer_id")
    @Expose
    private String consumerId;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("created_at")
    @Expose
    private Double createdAt;
    @SerializedName("rsa_public_key")
    @Expose
    private String rsaPublicKey;
    @SerializedName("algorithm")
    @Expose
    private String algorithm;

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

    public KongPluginJWTResponse withId(String id) {
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

    public KongPluginJWTResponse withConsumerId(String consumerId) {
        this.consumerId = consumerId;
        return this;
    }

    /**
     * 
     * @return
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    public KongPluginJWTResponse withKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * 
     * @return
     *     The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 
     * @param secret
     *     The secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public KongPluginJWTResponse withSecret(String secret) {
        this.secret = secret;
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

    public KongPluginJWTResponse withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 
     * @return
     *     The rsaPublicKey
     */
    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    /**
     * 
     * @param rsaPublicKey
     *     The rsa_public_key
     */
    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public KongPluginJWTResponse withRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
        return this;
    }

    /**
     * 
     * @return
     *     The algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * 
     * @param algorithm
     *     The algorithm
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public KongPluginJWTResponse withAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(consumerId).append(key).append(secret).append(createdAt).append(rsaPublicKey).append(algorithm).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginJWTResponse) == false) {
            return false;
        }
        KongPluginJWTResponse rhs = ((KongPluginJWTResponse) other);
        return new EqualsBuilder().append(id, rhs.id).append(consumerId, rhs.consumerId).append(key, rhs.key).append(secret, rhs.secret).append(createdAt, rhs.createdAt).append(rsaPublicKey, rhs.rsaPublicKey).append(algorithm, rhs.algorithm).isEquals();
    }

}
