
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginJWTUp implements KongConfigValue
{

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("x5u_url")
    @Expose
    private String x5uUrl = "not.set";
    @SerializedName("jwt_in_key_claim_name")
    @Expose
    private String jwtInKeyClaimName = "iss";
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("issuer_url")
    @Expose
    private String issuerUrl = "not.set";
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("token_expiration")
    @Expose
    private Long tokenExpiration = 60L;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The x5uUrl
     */
    public String getX5uUrl() {
        return x5uUrl;
    }

    /**
     * 
     * (Required)
     * 
     * @param x5uUrl
     *     The x5u_url
     */
    public void setX5uUrl(String x5uUrl) {
        this.x5uUrl = x5uUrl;
    }

    public KongPluginJWTUp withX5uUrl(String x5uUrl) {
        this.x5uUrl = x5uUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The jwtInKeyClaimName
     */
    public String getJwtInKeyClaimName() {
        return jwtInKeyClaimName;
    }

    /**
     * 
     * @param jwtInKeyClaimName
     *     The jwt_in_key_claim_name
     */
    public void setJwtInKeyClaimName(String jwtInKeyClaimName) {
        this.jwtInKeyClaimName = jwtInKeyClaimName;
    }

    public KongPluginJWTUp withJwtInKeyClaimName(String jwtInKeyClaimName) {
        this.jwtInKeyClaimName = jwtInKeyClaimName;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The issuerUrl
     */
    public String getIssuerUrl() {
        return issuerUrl;
    }

    /**
     * 
     * (Required)
     * 
     * @param issuerUrl
     *     The issuer_url
     */
    public void setIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
    }

    public KongPluginJWTUp withIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
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

    public KongPluginJWTUp withTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(x5uUrl).append(jwtInKeyClaimName).append(issuerUrl).append(tokenExpiration).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginJWTUp) == false) {
            return false;
        }
        KongPluginJWTUp rhs = ((KongPluginJWTUp) other);
        return new EqualsBuilder().append(x5uUrl, rhs.x5uUrl).append(jwtInKeyClaimName, rhs.jwtInKeyClaimName).append(issuerUrl, rhs.issuerUrl).append(tokenExpiration, rhs.tokenExpiration).isEquals();
    }

}
