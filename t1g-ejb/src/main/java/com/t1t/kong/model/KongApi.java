
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
public class KongApi {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private Double createdAt;
    @SerializedName("hosts")
    @Expose
    private List<String> hosts = new ArrayList<String>();
    @SerializedName("uris")
    @Expose
    private List<String> uris = new ArrayList<String>();
    @SerializedName("methods")
    @Expose
    private List<Method> methods = new ArrayList<Method>();
    @SerializedName("strip_uri")
    @Expose
    private Boolean stripUri;
    @SerializedName("preserve_host")
    @Expose
    private Boolean preserveHost;
    @SerializedName("upstream_url")
    @Expose
    private String upstreamUrl;
    @SerializedName("retries")
    @Expose
    private Long retries;
    @SerializedName("upstream_connect_timeout")
    @Expose
    private Long upstreamConnectTimeout;
    @SerializedName("upstream_send_timeout")
    @Expose
    private Long upstreamSendTimeout;
    @SerializedName("upstream_read_timeout")
    @Expose
    private Long upstreamReadTimeout;
    @SerializedName("https_only")
    @Expose
    private Boolean httpsOnly;
    @SerializedName("http_if_terminated")
    @Expose
    private Boolean httpIfTerminated;

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

    public KongApi withId(String id) {
        this.id = id;
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

    public KongApi withName(String name) {
        this.name = name;
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

    public KongApi withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 
     * @return
     *     The hosts
     */
    public List<String> getHosts() {
        return hosts;
    }

    /**
     * 
     * @param hosts
     *     The hosts
     */
    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public KongApi withHosts(List<String> hosts) {
        this.hosts = hosts;
        return this;
    }

    /**
     * 
     * @return
     *     The uris
     */
    public List<String> getUris() {
        return uris;
    }

    /**
     * 
     * @param uris
     *     The uris
     */
    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public KongApi withUris(List<String> uris) {
        this.uris = uris;
        return this;
    }

    /**
     * 
     * @return
     *     The methods
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * 
     * @param methods
     *     The methods
     */
    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public KongApi withMethods(List<Method> methods) {
        this.methods = methods;
        return this;
    }

    /**
     * 
     * @return
     *     The stripUri
     */
    public Boolean getStripUri() {
        return stripUri;
    }

    /**
     * 
     * @param stripUri
     *     The strip_uri
     */
    public void setStripUri(Boolean stripUri) {
        this.stripUri = stripUri;
    }

    public KongApi withStripUri(Boolean stripUri) {
        this.stripUri = stripUri;
        return this;
    }

    /**
     * 
     * @return
     *     The preserveHost
     */
    public Boolean getPreserveHost() {
        return preserveHost;
    }

    /**
     * 
     * @param preserveHost
     *     The preserve_host
     */
    public void setPreserveHost(Boolean preserveHost) {
        this.preserveHost = preserveHost;
    }

    public KongApi withPreserveHost(Boolean preserveHost) {
        this.preserveHost = preserveHost;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamUrl
     */
    public String getUpstreamUrl() {
        return upstreamUrl;
    }

    /**
     * 
     * @param upstreamUrl
     *     The upstream_url
     */
    public void setUpstreamUrl(String upstreamUrl) {
        this.upstreamUrl = upstreamUrl;
    }

    public KongApi withUpstreamUrl(String upstreamUrl) {
        this.upstreamUrl = upstreamUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The retries
     */
    public Long getRetries() {
        return retries;
    }

    /**
     * 
     * @param retries
     *     The retries
     */
    public void setRetries(Long retries) {
        this.retries = retries;
    }

    public KongApi withRetries(Long retries) {
        this.retries = retries;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamConnectTimeout
     */
    public Long getUpstreamConnectTimeout() {
        return upstreamConnectTimeout;
    }

    /**
     * 
     * @param upstreamConnectTimeout
     *     The upstream_connect_timeout
     */
    public void setUpstreamConnectTimeout(Long upstreamConnectTimeout) {
        this.upstreamConnectTimeout = upstreamConnectTimeout;
    }

    public KongApi withUpstreamConnectTimeout(Long upstreamConnectTimeout) {
        this.upstreamConnectTimeout = upstreamConnectTimeout;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamSendTimeout
     */
    public Long getUpstreamSendTimeout() {
        return upstreamSendTimeout;
    }

    /**
     * 
     * @param upstreamSendTimeout
     *     The upstream_send_timeout
     */
    public void setUpstreamSendTimeout(Long upstreamSendTimeout) {
        this.upstreamSendTimeout = upstreamSendTimeout;
    }

    public KongApi withUpstreamSendTimeout(Long upstreamSendTimeout) {
        this.upstreamSendTimeout = upstreamSendTimeout;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamReadTimeout
     */
    public Long getUpstreamReadTimeout() {
        return upstreamReadTimeout;
    }

    /**
     * 
     * @param upstreamReadTimeout
     *     The upstream_read_timeout
     */
    public void setUpstreamReadTimeout(Long upstreamReadTimeout) {
        this.upstreamReadTimeout = upstreamReadTimeout;
    }

    public KongApi withUpstreamReadTimeout(Long upstreamReadTimeout) {
        this.upstreamReadTimeout = upstreamReadTimeout;
        return this;
    }

    /**
     * 
     * @return
     *     The httpsOnly
     */
    public Boolean getHttpsOnly() {
        return httpsOnly;
    }

    /**
     * 
     * @param httpsOnly
     *     The https_only
     */
    public void setHttpsOnly(Boolean httpsOnly) {
        this.httpsOnly = httpsOnly;
    }

    public KongApi withHttpsOnly(Boolean httpsOnly) {
        this.httpsOnly = httpsOnly;
        return this;
    }

    /**
     * 
     * @return
     *     The httpIfTerminated
     */
    public Boolean getHttpIfTerminated() {
        return httpIfTerminated;
    }

    /**
     * 
     * @param httpIfTerminated
     *     The http_if_terminated
     */
    public void setHttpIfTerminated(Boolean httpIfTerminated) {
        this.httpIfTerminated = httpIfTerminated;
    }

    public KongApi withHttpIfTerminated(Boolean httpIfTerminated) {
        this.httpIfTerminated = httpIfTerminated;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(createdAt).append(hosts).append(uris).append(methods).append(stripUri).append(preserveHost).append(upstreamUrl).append(retries).append(upstreamConnectTimeout).append(upstreamSendTimeout).append(upstreamReadTimeout).append(httpsOnly).append(httpIfTerminated).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongApi) == false) {
            return false;
        }
        KongApi rhs = ((KongApi) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(createdAt, rhs.createdAt).append(hosts, rhs.hosts).append(uris, rhs.uris).append(methods, rhs.methods).append(stripUri, rhs.stripUri).append(preserveHost, rhs.preserveHost).append(upstreamUrl, rhs.upstreamUrl).append(retries, rhs.retries).append(upstreamConnectTimeout, rhs.upstreamConnectTimeout).append(upstreamSendTimeout, rhs.upstreamSendTimeout).append(upstreamReadTimeout, rhs.upstreamReadTimeout).append(httpsOnly, rhs.httpsOnly).append(httpIfTerminated, rhs.httpIfTerminated).isEquals();
    }

}
