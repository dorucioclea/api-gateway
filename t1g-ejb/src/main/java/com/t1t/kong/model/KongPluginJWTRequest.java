
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginJWTRequest {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("algorithm")
    @Expose
    private String algorithm;
    @SerializedName("rsa_public_key")
    @Expose
    private String rsaPublicKey;
    @SerializedName("secret")
    @Expose
    private String secret;

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

    public KongPluginJWTRequest withKey(String key) {
        this.key = key;
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

    public KongPluginJWTRequest withAlgorithm(String algorithm) {
        this.algorithm = algorithm;
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

    public KongPluginJWTRequest withRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
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

    public KongPluginJWTRequest withSecret(String secret) {
        this.secret = secret;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(key).append(algorithm).append(rsaPublicKey).append(secret).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginJWTRequest) == false) {
            return false;
        }
        KongPluginJWTRequest rhs = ((KongPluginJWTRequest) other);
        return new EqualsBuilder().append(key, rhs.key).append(algorithm, rhs.algorithm).append(rsaPublicKey, rhs.rsaPublicKey).append(secret, rhs.secret).isEquals();
    }

}
