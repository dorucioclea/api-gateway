
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
public class KongPluginDataDog implements KongConfigValue
{

    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("timeout")
    @Expose
    private Long timeout;
    @SerializedName("port")
    @Expose
    private Long port;
    @SerializedName("metrics")
    @Expose
    private List<Metric> metrics = new ArrayList<Metric>();
    @SerializedName("tags")
    @Expose
    private Tags tags;

    /**
     * 
     * @return
     *     The host
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * @param host
     *     The host
     */
    public void setHost(String host) {
        this.host = host;
    }

    public KongPluginDataDog withHost(String host) {
        this.host = host;
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

    public KongPluginDataDog withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 
     * @return
     *     The port
     */
    public Long getPort() {
        return port;
    }

    /**
     * 
     * @param port
     *     The port
     */
    public void setPort(Long port) {
        this.port = port;
    }

    public KongPluginDataDog withPort(Long port) {
        this.port = port;
        return this;
    }

    /**
     * 
     * @return
     *     The metrics
     */
    public List<Metric> getMetrics() {
        return metrics;
    }

    /**
     * 
     * @param metrics
     *     The metrics
     */
    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public KongPluginDataDog withMetrics(List<Metric> metrics) {
        this.metrics = metrics;
        return this;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public Tags getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public KongPluginDataDog withTags(Tags tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(host).append(timeout).append(port).append(metrics).append(tags).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginDataDog) == false) {
            return false;
        }
        KongPluginDataDog rhs = ((KongPluginDataDog) other);
        return new EqualsBuilder().append(host, rhs.host).append(timeout, rhs.timeout).append(port, rhs.port).append(metrics, rhs.metrics).append(tags, rhs.tags).isEquals();
    }

}
