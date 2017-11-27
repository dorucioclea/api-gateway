
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginBasicAuth implements KongConfigValue
{

    @SerializedName("hide_credentials")
    @Expose
    private Boolean hideCredentials = false;

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

    public KongPluginBasicAuth withHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(hideCredentials).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginBasicAuth) == false) {
            return false;
        }
        KongPluginBasicAuth rhs = ((KongPluginBasicAuth) other);
        return new EqualsBuilder().append(hideCredentials, rhs.hideCredentials).isEquals();
    }

}
