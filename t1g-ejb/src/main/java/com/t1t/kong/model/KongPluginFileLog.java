
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginFileLog implements KongConfigValue
{

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("path")
    @Expose
    private String path;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The path
     */
    public String getPath() {
        return path;
    }

    /**
     * 
     * (Required)
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    public KongPluginFileLog withPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginFileLog) == false) {
            return false;
        }
        KongPluginFileLog rhs = ((KongPluginFileLog) other);
        return new EqualsBuilder().append(path, rhs.path).isEquals();
    }

}
