
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginSchema {

    @SerializedName("fields")
    @Expose
    private Fields fields;

    /**
     * 
     * @return
     *     The fields
     */
    public Fields getFields() {
        return fields;
    }

    /**
     * 
     * @param fields
     *     The fields
     */
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public KongPluginSchema withFields(Fields fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(fields).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginSchema) == false) {
            return false;
        }
        KongPluginSchema rhs = ((KongPluginSchema) other);
        return new EqualsBuilder().append(fields, rhs.fields).isEquals();
    }

}
