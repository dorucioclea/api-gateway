
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
public class KongPluginCors implements KongConfigValue
{

    @SerializedName("methods")
    @Expose
    private List<Method_> methods = new ArrayList<Method_>();
    @SerializedName("credentials")
    @Expose
    private Boolean credentials = false;
    @SerializedName("exposed_headers")
    @Expose
    private List<String> exposedHeaders = new ArrayList<String>();
    @SerializedName("origins")
    @Expose
    private List<String> origins = new ArrayList<String>();
    @SerializedName("max_age")
    @Expose
    private Long maxAge;
    @SerializedName("preflight_continue")
    @Expose
    private Boolean preflightContinue = false;
    @SerializedName("headers")
    @Expose
    private List<String> headers = new ArrayList<String>();

    /**
     * 
     * @return
     *     The methods
     */
    public List<Method_> getMethods() {
        return methods;
    }

    /**
     * 
     * @param methods
     *     The methods
     */
    public void setMethods(List<Method_> methods) {
        this.methods = methods;
    }

    public KongPluginCors withMethods(List<Method_> methods) {
        this.methods = methods;
        return this;
    }

    /**
     * 
     * @return
     *     The credentials
     */
    public Boolean getCredentials() {
        return credentials;
    }

    /**
     * 
     * @param credentials
     *     The credentials
     */
    public void setCredentials(Boolean credentials) {
        this.credentials = credentials;
    }

    public KongPluginCors withCredentials(Boolean credentials) {
        this.credentials = credentials;
        return this;
    }

    /**
     * 
     * @return
     *     The exposedHeaders
     */
    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    /**
     * 
     * @param exposedHeaders
     *     The exposed_headers
     */
    public void setExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public KongPluginCors withExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
        return this;
    }

    /**
     * 
     * @return
     *     The origins
     */
    public List<String> getOrigins() {
        return origins;
    }

    /**
     * 
     * @param origins
     *     The origins
     */
    public void setOrigins(List<String> origins) {
        this.origins = origins;
    }

    public KongPluginCors withOrigins(List<String> origins) {
        this.origins = origins;
        return this;
    }

    /**
     * 
     * @return
     *     The maxAge
     */
    public Long getMaxAge() {
        return maxAge;
    }

    /**
     * 
     * @param maxAge
     *     The max_age
     */
    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public KongPluginCors withMaxAge(Long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /**
     * 
     * @return
     *     The preflightContinue
     */
    public Boolean getPreflightContinue() {
        return preflightContinue;
    }

    /**
     * 
     * @param preflightContinue
     *     The preflight_continue
     */
    public void setPreflightContinue(Boolean preflightContinue) {
        this.preflightContinue = preflightContinue;
    }

    public KongPluginCors withPreflightContinue(Boolean preflightContinue) {
        this.preflightContinue = preflightContinue;
        return this;
    }

    /**
     * 
     * @return
     *     The headers
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * 
     * @param headers
     *     The headers
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public KongPluginCors withHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(methods).append(credentials).append(exposedHeaders).append(origins).append(maxAge).append(preflightContinue).append(headers).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginCors) == false) {
            return false;
        }
        KongPluginCors rhs = ((KongPluginCors) other);
        return new EqualsBuilder().append(methods, rhs.methods).append(credentials, rhs.credentials).append(exposedHeaders, rhs.exposedHeaders).append(origins, rhs.origins).append(maxAge, rhs.maxAge).append(preflightContinue, rhs.preflightContinue).append(headers, rhs.headers).isEquals();
    }

}
