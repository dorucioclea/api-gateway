
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongUpstreamTarget {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("weight")
    @Expose
    private Long weight;
    @SerializedName("upstream_id")
    @Expose
    private String upstreamId;
    @SerializedName("created_at")
    @Expose
    private Double createdAt;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    public KongUpstreamTarget withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The target
     */
    public String getTarget() {
        return target;
    }

    /**
     * 
     * @param target
     *     The target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    public KongUpstreamTarget withTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 
     * @return
     *     The weight
     */
    public Long getWeight() {
        return weight;
    }

    /**
     * 
     * @param weight
     *     The weight
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public KongUpstreamTarget withWeight(Long weight) {
        this.weight = weight;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamId
     */
    public String getUpstreamId() {
        return upstreamId;
    }

    /**
     * 
     * @param upstreamId
     *     The upstream_id
     */
    public void setUpstreamId(String upstreamId) {
        this.upstreamId = upstreamId;
    }

    public KongUpstreamTarget withUpstreamId(String upstreamId) {
        this.upstreamId = upstreamId;
        return this;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public Double getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
    }

    public KongUpstreamTarget withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(target).append(weight).append(upstreamId).append(createdAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongUpstreamTarget) == false) {
            return false;
        }
        KongUpstreamTarget rhs = ((KongUpstreamTarget) other);
        return new EqualsBuilder().append(id, rhs.id).append(target, rhs.target).append(weight, rhs.weight).append(upstreamId, rhs.upstreamId).append(createdAt, rhs.createdAt).isEquals();
    }

}
