
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginACLRequest {

    @SerializedName("group")
    @Expose
    private String group;

    /**
     * 
     * @return
     *     The group
     */
    public String getGroup() {
        return group;
    }

    /**
     * 
     * @param group
     *     The group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    public KongPluginACLRequest withGroup(String group) {
        this.group = group;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(group).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginACLRequest) == false) {
            return false;
        }
        KongPluginACLRequest rhs = ((KongPluginACLRequest) other);
        return new EqualsBuilder().append(group, rhs.group).isEquals();
    }

}
