
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class PhoneNumber {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public PhoneNumber withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public PhoneNumber withType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(value).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PhoneNumber) == false) {
            return false;
        }
        PhoneNumber rhs = ((PhoneNumber) other);
        return new EqualsBuilder().append(value, rhs.value).append(type, rhs.type).isEquals();
    }

}
