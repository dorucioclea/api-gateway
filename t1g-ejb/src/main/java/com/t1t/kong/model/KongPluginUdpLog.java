
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginUdpLog implements KongConfigValue
{

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("timeout")
    @Expose
    private Long timeout = 10000L;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("port")
    @Expose
    private Long port;

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

    public KongPluginUdpLog withHost(String host) {
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

    public KongPluginUdpLog withTimeout(Long timeout) {
        this.timeout = timeout;
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

    public KongPluginUdpLog withPort(Long port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(host).append(timeout).append(port).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginUdpLog) == false) {
            return false;
        }
        KongPluginUdpLog rhs = ((KongPluginUdpLog) other);
        return new EqualsBuilder().append(host, rhs.host).append(timeout, rhs.timeout).append(port, rhs.port).isEquals();
    }

}
