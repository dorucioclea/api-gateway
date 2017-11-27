
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Name {

    @SerializedName("familyName")
    @Expose
    private String familyName;
    @SerializedName("givenName")
    @Expose
    private String givenName;

    /**
     * 
     * @return
     *     The familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * 
     * @param familyName
     *     The familyName
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Name withFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    /**
     * 
     * @return
     *     The givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * 
     * @param givenName
     *     The givenName
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Name withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(familyName).append(givenName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Name) == false) {
            return false;
        }
        Name rhs = ((Name) other);
        return new EqualsBuilder().append(familyName, rhs.familyName).append(givenName, rhs.givenName).isEquals();
    }

}
