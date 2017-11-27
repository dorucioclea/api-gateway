
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
public class KongUpstream {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slots")
    @Expose
    private Long slots;
    @SerializedName("orderlist")
    @Expose
    private List<Long> orderlist = new ArrayList<Long>();
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

    public KongUpstream withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public KongUpstream withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The slots
     */
    public Long getSlots() {
        return slots;
    }

    /**
     * 
     * @param slots
     *     The slots
     */
    public void setSlots(Long slots) {
        this.slots = slots;
    }

    public KongUpstream withSlots(Long slots) {
        this.slots = slots;
        return this;
    }

    /**
     * 
     * @return
     *     The orderlist
     */
    public List<Long> getOrderlist() {
        return orderlist;
    }

    /**
     * 
     * @param orderlist
     *     The orderlist
     */
    public void setOrderlist(List<Long> orderlist) {
        this.orderlist = orderlist;
    }

    public KongUpstream withOrderlist(List<Long> orderlist) {
        this.orderlist = orderlist;
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

    public KongUpstream withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(slots).append(orderlist).append(createdAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongUpstream) == false) {
            return false;
        }
        KongUpstream rhs = ((KongUpstream) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(slots, rhs.slots).append(orderlist, rhs.orderlist).append(createdAt, rhs.createdAt).isEquals();
    }

}
