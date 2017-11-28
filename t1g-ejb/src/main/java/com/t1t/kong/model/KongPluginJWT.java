
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
public class KongPluginJWT implements KongConfigValue
{

    /**
     * 
     */
    @SerializedName("claims_to_verify")
    @Expose
    private List<String> claimsToVerify = new ArrayList<String>();
    /**
     * 
     */
    @SerializedName("uri_param_names")
    @Expose
    private List<String> uriParamNames = new ArrayList<String>();
    /**
     * 
     */
    @SerializedName("secret_is_base64")
    @Expose
    private Boolean secretIsBase64;
    /**
     * 
     */
    @SerializedName("key_claim_name")
    @Expose
    private String keyClaimName;

    /**
     * 
     * @return
     *     The claimsToVerify
     */
    public List<String> getClaimsToVerify() {
        return claimsToVerify;
    }

    /**
     * 
     * @param claimsToVerify
     *     The claims_to_verify
     */
    public void setClaimsToVerify(List<String> claimsToVerify) {
        this.claimsToVerify = claimsToVerify;
    }

    public KongPluginJWT withClaimsToVerify(List<String> claimsToVerify) {
        this.claimsToVerify = claimsToVerify;
        return this;
    }

    /**
     * 
     * @return
     *     The uriParamNames
     */
    public List<String> getUriParamNames() {
        return uriParamNames;
    }

    /**
     * 
     * @param uriParamNames
     *     The uri_param_names
     */
    public void setUriParamNames(List<String> uriParamNames) {
        this.uriParamNames = uriParamNames;
    }

    public KongPluginJWT withUriParamNames(List<String> uriParamNames) {
        this.uriParamNames = uriParamNames;
        return this;
    }

    /**
     * 
     * @return
     *     The secretIsBase64
     */
    public Boolean getSecretIsBase64() {
        return secretIsBase64;
    }

    /**
     * 
     * @param secretIsBase64
     *     The secret_is_base64
     */
    public void setSecretIsBase64(Boolean secretIsBase64) {
        this.secretIsBase64 = secretIsBase64;
    }

    public KongPluginJWT withSecretIsBase64(Boolean secretIsBase64) {
        this.secretIsBase64 = secretIsBase64;
        return this;
    }

    /**
     * 
     * @return
     *     The keyClaimName
     */
    public String getKeyClaimName() {
        return keyClaimName;
    }

    /**
     * 
     * @param keyClaimName
     *     The key_claim_name
     */
    public void setKeyClaimName(String keyClaimName) {
        this.keyClaimName = keyClaimName;
    }

    public KongPluginJWT withKeyClaimName(String keyClaimName) {
        this.keyClaimName = keyClaimName;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(claimsToVerify).append(uriParamNames).append(secretIsBase64).append(keyClaimName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginJWT) == false) {
            return false;
        }
        KongPluginJWT rhs = ((KongPluginJWT) other);
        return new EqualsBuilder().append(claimsToVerify, rhs.claimsToVerify).append(uriParamNames, rhs.uriParamNames).append(secretIsBase64, rhs.secretIsBase64).append(keyClaimName, rhs.keyClaimName).isEquals();
    }

}
