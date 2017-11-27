
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginRequestSizeLimiting implements KongConfigValue
{

    @SerializedName("allowed_payload_size")
    @Expose
    private Double allowedPayloadSize = 128.0D;

    /**
     * 
     * @return
     *     The allowedPayloadSize
     */
    public Double getAllowedPayloadSize() {
        return allowedPayloadSize;
    }

    /**
     * 
     * @param allowedPayloadSize
     *     The allowed_payload_size
     */
    public void setAllowedPayloadSize(Double allowedPayloadSize) {
        this.allowedPayloadSize = allowedPayloadSize;
    }

    public KongPluginRequestSizeLimiting withAllowedPayloadSize(Double allowedPayloadSize) {
        this.allowedPayloadSize = allowedPayloadSize;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(allowedPayloadSize).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginRequestSizeLimiting) == false) {
            return false;
        }
        KongPluginRequestSizeLimiting rhs = ((KongPluginRequestSizeLimiting) other);
        return new EqualsBuilder().append(allowedPayloadSize, rhs.allowedPayloadSize).isEquals();
    }

}
