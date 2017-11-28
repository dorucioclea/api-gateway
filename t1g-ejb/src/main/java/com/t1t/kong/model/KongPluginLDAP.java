
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginLDAP implements KongConfigValue
{

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("ldap_port")
    @Expose
    private Long ldapPort;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("cache_ttl")
    @Expose
    private Long cacheTtl = 60L;
    @SerializedName("timeout")
    @Expose
    private Long timeout = 10000L;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("start_tls")
    @Expose
    private Boolean startTls = false;
    @SerializedName("hide_credentials")
    @Expose
    private Boolean hideCredentials = false;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("ldap_host")
    @Expose
    private String ldapHost;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("base_dn")
    @Expose
    private String baseDn;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("verify_ldap_host")
    @Expose
    private Boolean verifyLdapHost = false;
    @SerializedName("keepalive")
    @Expose
    private Long keepalive = 60000L;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("attribute")
    @Expose
    private String attribute;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The ldapPort
     */
    public Long getLdapPort() {
        return ldapPort;
    }

    /**
     * 
     * (Required)
     * 
     * @param ldapPort
     *     The ldap_port
     */
    public void setLdapPort(Long ldapPort) {
        this.ldapPort = ldapPort;
    }

    public KongPluginLDAP withLdapPort(Long ldapPort) {
        this.ldapPort = ldapPort;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The cacheTtl
     */
    public Long getCacheTtl() {
        return cacheTtl;
    }

    /**
     * 
     * (Required)
     * 
     * @param cacheTtl
     *     The cache_ttl
     */
    public void setCacheTtl(Long cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    public KongPluginLDAP withCacheTtl(Long cacheTtl) {
        this.cacheTtl = cacheTtl;
        return this;
    }

    /**
     * 
     * @return
     *     The timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * 
     * @param timeout
     *     The timeout
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public KongPluginLDAP withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The startTls
     */
    public Boolean getStartTls() {
        return startTls;
    }

    /**
     * 
     * (Required)
     * 
     * @param startTls
     *     The start_tls
     */
    public void setStartTls(Boolean startTls) {
        this.startTls = startTls;
    }

    public KongPluginLDAP withStartTls(Boolean startTls) {
        this.startTls = startTls;
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

    public KongPluginLDAP withHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The ldapHost
     */
    public String getLdapHost() {
        return ldapHost;
    }

    /**
     * 
     * (Required)
     * 
     * @param ldapHost
     *     The ldap_host
     */
    public void setLdapHost(String ldapHost) {
        this.ldapHost = ldapHost;
    }

    public KongPluginLDAP withLdapHost(String ldapHost) {
        this.ldapHost = ldapHost;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The baseDn
     */
    public String getBaseDn() {
        return baseDn;
    }

    /**
     * 
     * (Required)
     * 
     * @param baseDn
     *     The base_dn
     */
    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public KongPluginLDAP withBaseDn(String baseDn) {
        this.baseDn = baseDn;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The verifyLdapHost
     */
    public Boolean getVerifyLdapHost() {
        return verifyLdapHost;
    }

    /**
     * 
     * (Required)
     * 
     * @param verifyLdapHost
     *     The verify_ldap_host
     */
    public void setVerifyLdapHost(Boolean verifyLdapHost) {
        this.verifyLdapHost = verifyLdapHost;
    }

    public KongPluginLDAP withVerifyLdapHost(Boolean verifyLdapHost) {
        this.verifyLdapHost = verifyLdapHost;
        return this;
    }

    /**
     * 
     * @return
     *     The keepalive
     */
    public Long getKeepalive() {
        return keepalive;
    }

    /**
     * 
     * @param keepalive
     *     The keepalive
     */
    public void setKeepalive(Long keepalive) {
        this.keepalive = keepalive;
    }

    public KongPluginLDAP withKeepalive(Long keepalive) {
        this.keepalive = keepalive;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * 
     * (Required)
     * 
     * @param attribute
     *     The attribute
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public KongPluginLDAP withAttribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ldapPort).append(cacheTtl).append(timeout).append(startTls).append(hideCredentials).append(ldapHost).append(baseDn).append(verifyLdapHost).append(keepalive).append(attribute).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginLDAP) == false) {
            return false;
        }
        KongPluginLDAP rhs = ((KongPluginLDAP) other);
        return new EqualsBuilder().append(ldapPort, rhs.ldapPort).append(cacheTtl, rhs.cacheTtl).append(timeout, rhs.timeout).append(startTls, rhs.startTls).append(hideCredentials, rhs.hideCredentials).append(ldapHost, rhs.ldapHost).append(baseDn, rhs.baseDn).append(verifyLdapHost, rhs.verifyLdapHost).append(keepalive, rhs.keepalive).append(attribute, rhs.attribute).isEquals();
    }

}
