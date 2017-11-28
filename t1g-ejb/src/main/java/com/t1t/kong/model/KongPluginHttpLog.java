
package com.t1t.kong.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginHttpLog implements KongConfigValue
{

    @SerializedName("keepalive")
    @Expose
    private Long keepalive = 60000L;
    @SerializedName("method")
    @Expose
    private Method method = Method.fromValue("POST");
    @SerializedName("timeout")
    @Expose
    private Long timeout = 10000L;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("http_endpoint")
    @Expose
    private String httpEndpoint;

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

    public KongPluginHttpLog withKeepalive(Long keepalive) {
        this.keepalive = keepalive;
        return this;
    }

    /**
     * 
     * @return
     *     The method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 
     * @param method
     *     The method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    public KongPluginHttpLog withMethod(Method method) {
        this.method = method;
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

    public KongPluginHttpLog withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The httpEndpoint
     */
    public String getHttpEndpoint() {
        return httpEndpoint;
    }

    /**
     * 
     * (Required)
     * 
     * @param httpEndpoint
     *     The http_endpoint
     */
    public void setHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    public KongPluginHttpLog withHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(keepalive).append(method).append(timeout).append(httpEndpoint).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginHttpLog) == false) {
            return false;
        }
        KongPluginHttpLog rhs = ((KongPluginHttpLog) other);
        return new EqualsBuilder().append(keepalive, rhs.keepalive).append(method, rhs.method).append(timeout, rhs.timeout).append(httpEndpoint, rhs.httpEndpoint).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public static enum Method {

        @SerializedName("POST")
        POST("POST"),
        @SerializedName("PUT")
        PUT("PUT"),
        @SerializedName("PATCH")
        PATCH("PATCH");
        private final String value;
        private static Map<String, Method> constants = new HashMap<String, Method>();

        static {
            for (Method c: values()) {
                constants.put(c.value, c);
            }
        }

        private Method(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Method fromValue(String value) {
            Method constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
