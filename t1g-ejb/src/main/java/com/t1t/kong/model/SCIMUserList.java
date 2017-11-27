
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
public class SCIMUserList {

    @SerializedName("schemas")
    @Expose
    private List<String> schemas = new ArrayList<String>();
    @SerializedName("totalResults")
    @Expose
    private Long totalResults;
    @SerializedName("Resources")
    @Expose
    private List<SCIMUser> Resources = new ArrayList<SCIMUser>();

    /**
     * 
     * @return
     *     The schemas
     */
    public List<String> getSchemas() {
        return schemas;
    }

    /**
     * 
     * @param schemas
     *     The schemas
     */
    public void setSchemas(List<String> schemas) {
        this.schemas = schemas;
    }

    public SCIMUserList withSchemas(List<String> schemas) {
        this.schemas = schemas;
        return this;
    }

    /**
     * 
     * @return
     *     The totalResults
     */
    public Long getTotalResults() {
        return totalResults;
    }

    /**
     * 
     * @param totalResults
     *     The totalResults
     */
    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public SCIMUserList withTotalResults(Long totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    /**
     * 
     * @return
     *     The Resources
     */
    public List<SCIMUser> getResources() {
        return Resources;
    }

    /**
     * 
     * @param Resources
     *     The Resources
     */
    public void setResources(List<SCIMUser> Resources) {
        this.Resources = Resources;
    }

    public SCIMUserList withResources(List<SCIMUser> Resources) {
        this.Resources = Resources;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(schemas).append(totalResults).append(Resources).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SCIMUserList) == false) {
            return false;
        }
        SCIMUserList rhs = ((SCIMUserList) other);
        return new EqualsBuilder().append(schemas, rhs.schemas).append(totalResults, rhs.totalResults).append(Resources, rhs.Resources).isEquals();
    }

}
