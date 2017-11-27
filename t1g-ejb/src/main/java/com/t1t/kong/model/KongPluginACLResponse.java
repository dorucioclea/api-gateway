
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginACLResponse {

    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("consumer_id")
    @Expose
    private String consumerId;
    @SerializedName("created_at")
    @Expose
    private Double createdAt;
    @SerializedName("id")
    @Expose
    private String id;

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

    public KongPluginACLResponse withGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * 
     * @return
     *     The consumerId
     */
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * 
     * @param consumerId
     *     The consumer_id
     */
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public KongPluginACLResponse withConsumerId(String consumerId) {
        this.consumerId = consumerId;
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

    public KongPluginACLResponse withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

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

    public KongPluginACLResponse withId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(group).append(consumerId).append(createdAt).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginACLResponse) == false) {
            return false;
        }
        KongPluginACLResponse rhs = ((KongPluginACLResponse) other);
        return new EqualsBuilder().append(group, rhs.group).append(consumerId, rhs.consumerId).append(createdAt, rhs.createdAt).append(id, rhs.id).isEquals();
    }

}
