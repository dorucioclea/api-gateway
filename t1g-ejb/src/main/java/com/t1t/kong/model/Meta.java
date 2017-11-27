
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Meta {

    @SerializedName("lastModified")
    @Expose
    private String lastModified;
    @SerializedName("created")
    @Expose
    private String created;

    /**
     * 
     * @return
     *     The lastModified
     */
    public String getLastModified() {
        return lastModified;
    }

    /**
     * 
     * @param lastModified
     *     The lastModified
     */
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Meta withLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    /**
     * 
     * @return
     *     The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * 
     * @param created
     *     The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    public Meta withCreated(String created) {
        this.created = created;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lastModified).append(created).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meta) == false) {
            return false;
        }
        Meta rhs = ((Meta) other);
        return new EqualsBuilder().append(lastModified, rhs.lastModified).append(created, rhs.created).isEquals();
    }

}
