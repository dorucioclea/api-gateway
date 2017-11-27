
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
public class KongPluginKeyAuth implements KongConfigValue
{

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("key_names")
    @Expose
    private List<String> keyNames = new ArrayList<String>();
    @SerializedName("hide_credentials")
    @Expose
    private Boolean hideCredentials = false;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The keyNames
     */
    public List<String> getKeyNames() {
        return keyNames;
    }

    /**
     * 
     * (Required)
     * 
     * @param keyNames
     *     The key_names
     */
    public void setKeyNames(List<String> keyNames) {
        this.keyNames = keyNames;
    }

    public KongPluginKeyAuth withKeyNames(List<String> keyNames) {
        this.keyNames = keyNames;
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

    public KongPluginKeyAuth withHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(keyNames).append(hideCredentials).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginKeyAuth) == false) {
            return false;
        }
        KongPluginKeyAuth rhs = ((KongPluginKeyAuth) other);
        return new EqualsBuilder().append(keyNames, rhs.keyNames).append(hideCredentials, rhs.hideCredentials).isEquals();
    }

}
