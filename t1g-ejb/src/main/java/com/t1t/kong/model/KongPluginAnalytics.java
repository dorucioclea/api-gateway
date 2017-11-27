
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginAnalytics implements KongConfigValue
{

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("environment")
    @Expose
    private String environment;
    /**
     * 
     */
    @SerializedName("retry_count")
    @Expose
    private Long retryCount;
    /**
     * 
     */
    @SerializedName("queue_size")
    @Expose
    private Long queueSize;
    /**
     * 
     */
    @SerializedName("flush_timeout")
    @Expose
    private Long flushTimeout;
    /**
     * 
     */
    @SerializedName("log_bodies")
    @Expose
    private Boolean logBodies;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("service_token")
    @Expose
    private String serviceToken;
    /**
     * 
     */
    @SerializedName("connection_timeout")
    @Expose
    private Long connectionTimeout;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("host")
    @Expose
    private String host = "collector.galileo.mashape.com";
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("port")
    @Expose
    private Long port = 443L;
    /**
     * 
     */
    @SerializedName("https")
    @Expose
    private Boolean https = true;
    /**
     * 
     */
    @SerializedName("https_verify")
    @Expose
    private Boolean httpsVerify = false;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * 
     * (Required)
     * 
     * @param environment
     *     The environment
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public KongPluginAnalytics withEnvironment(String environment) {
        this.environment = environment;
        return this;
    }

    /**
     * 
     * @return
     *     The retryCount
     */
    public Long getRetryCount() {
        return retryCount;
    }

    /**
     * 
     * @param retryCount
     *     The retry_count
     */
    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    public KongPluginAnalytics withRetryCount(Long retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    /**
     * 
     * @return
     *     The queueSize
     */
    public Long getQueueSize() {
        return queueSize;
    }

    /**
     * 
     * @param queueSize
     *     The queue_size
     */
    public void setQueueSize(Long queueSize) {
        this.queueSize = queueSize;
    }

    public KongPluginAnalytics withQueueSize(Long queueSize) {
        this.queueSize = queueSize;
        return this;
    }

    /**
     * 
     * @return
     *     The flushTimeout
     */
    public Long getFlushTimeout() {
        return flushTimeout;
    }

    /**
     * 
     * @param flushTimeout
     *     The flush_timeout
     */
    public void setFlushTimeout(Long flushTimeout) {
        this.flushTimeout = flushTimeout;
    }

    public KongPluginAnalytics withFlushTimeout(Long flushTimeout) {
        this.flushTimeout = flushTimeout;
        return this;
    }

    /**
     * 
     * @return
     *     The logBodies
     */
    public Boolean getLogBodies() {
        return logBodies;
    }

    /**
     * 
     * @param logBodies
     *     The log_bodies
     */
    public void setLogBodies(Boolean logBodies) {
        this.logBodies = logBodies;
    }

    public KongPluginAnalytics withLogBodies(Boolean logBodies) {
        this.logBodies = logBodies;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The serviceToken
     */
    public String getServiceToken() {
        return serviceToken;
    }

    /**
     * 
     * (Required)
     * 
     * @param serviceToken
     *     The service_token
     */
    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    public KongPluginAnalytics withServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionTimeout
     */
    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * 
     * @param connectionTimeout
     *     The connection_timeout
     */
    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public KongPluginAnalytics withConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The host
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * (Required)
     * 
     * @param host
     *     The host
     */
    public void setHost(String host) {
        this.host = host;
    }

    public KongPluginAnalytics withHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The port
     */
    public Long getPort() {
        return port;
    }

    /**
     * 
     * (Required)
     * 
     * @param port
     *     The port
     */
    public void setPort(Long port) {
        this.port = port;
    }

    public KongPluginAnalytics withPort(Long port) {
        this.port = port;
        return this;
    }

    /**
     * 
     * @return
     *     The https
     */
    public Boolean getHttps() {
        return https;
    }

    /**
     * 
     * @param https
     *     The https
     */
    public void setHttps(Boolean https) {
        this.https = https;
    }

    public KongPluginAnalytics withHttps(Boolean https) {
        this.https = https;
        return this;
    }

    /**
     * 
     * @return
     *     The httpsVerify
     */
    public Boolean getHttpsVerify() {
        return httpsVerify;
    }

    /**
     * 
     * @param httpsVerify
     *     The https_verify
     */
    public void setHttpsVerify(Boolean httpsVerify) {
        this.httpsVerify = httpsVerify;
    }

    public KongPluginAnalytics withHttpsVerify(Boolean httpsVerify) {
        this.httpsVerify = httpsVerify;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(environment).append(retryCount).append(queueSize).append(flushTimeout).append(logBodies).append(serviceToken).append(connectionTimeout).append(host).append(port).append(https).append(httpsVerify).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginAnalytics) == false) {
            return false;
        }
        KongPluginAnalytics rhs = ((KongPluginAnalytics) other);
        return new EqualsBuilder().append(environment, rhs.environment).append(retryCount, rhs.retryCount).append(queueSize, rhs.queueSize).append(flushTimeout, rhs.flushTimeout).append(logBodies, rhs.logBodies).append(serviceToken, rhs.serviceToken).append(connectionTimeout, rhs.connectionTimeout).append(host, rhs.host).append(port, rhs.port).append(https, rhs.https).append(httpsVerify, rhs.httpsVerify).isEquals();
    }

}
