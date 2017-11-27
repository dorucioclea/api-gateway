
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginSSL implements KongConfigValue
{

    @SerializedName("_cert_der_cache")
    @Expose
    private String CertDerCache;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("cert")
    @Expose
    private String cert;
    /**
     * 
     */
    @SerializedName("only_https")
    @Expose
    private Boolean onlyHttps = false;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("_key_der_cache")
    @Expose
    private String KeyDerCache;

    /**
     * 
     * @return
     *     The CertDerCache
     */
    public String getCertDerCache() {
        return CertDerCache;
    }

    /**
     * 
     * @param CertDerCache
     *     The _cert_der_cache
     */
    public void setCertDerCache(String CertDerCache) {
        this.CertDerCache = CertDerCache;
    }

    public KongPluginSSL withCertDerCache(String CertDerCache) {
        this.CertDerCache = CertDerCache;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The cert
     */
    public String getCert() {
        return cert;
    }

    /**
     * 
     * (Required)
     * 
     * @param cert
     *     The cert
     */
    public void setCert(String cert) {
        this.cert = cert;
    }

    public KongPluginSSL withCert(String cert) {
        this.cert = cert;
        return this;
    }

    /**
     * 
     * @return
     *     The onlyHttps
     */
    public Boolean getOnlyHttps() {
        return onlyHttps;
    }

    /**
     * 
     * @param onlyHttps
     *     The only_https
     */
    public void setOnlyHttps(Boolean onlyHttps) {
        this.onlyHttps = onlyHttps;
    }

    public KongPluginSSL withOnlyHttps(Boolean onlyHttps) {
        this.onlyHttps = onlyHttps;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * (Required)
     * 
     * @param key
     *     The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    public KongPluginSSL withKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * 
     * @return
     *     The KeyDerCache
     */
    public String getKeyDerCache() {
        return KeyDerCache;
    }

    /**
     * 
     * @param KeyDerCache
     *     The _key_der_cache
     */
    public void setKeyDerCache(String KeyDerCache) {
        this.KeyDerCache = KeyDerCache;
    }

    public KongPluginSSL withKeyDerCache(String KeyDerCache) {
        this.KeyDerCache = KeyDerCache;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(CertDerCache).append(cert).append(onlyHttps).append(key).append(KeyDerCache).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginSSL) == false) {
            return false;
        }
        KongPluginSSL rhs = ((KongPluginSSL) other);
        return new EqualsBuilder().append(CertDerCache, rhs.CertDerCache).append(cert, rhs.cert).append(onlyHttps, rhs.onlyHttps).append(key, rhs.key).append(KeyDerCache, rhs.KeyDerCache).isEquals();
    }

}
