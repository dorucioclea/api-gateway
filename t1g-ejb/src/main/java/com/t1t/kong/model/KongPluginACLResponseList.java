
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
public class KongPluginACLResponseList {

    @SerializedName("data")
    @Expose
    private List<KongPluginACLResponse> data = new ArrayList<KongPluginACLResponse>();
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("offset")
    @Expose
    private String offset;
    @SerializedName("total")
    @Expose
    private Long total;

    /**
     * 
     * @return
     *     The data
     */
    public List<KongPluginACLResponse> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<KongPluginACLResponse> data) {
        this.data = data;
    }

    public KongPluginACLResponseList withData(List<KongPluginACLResponse> data) {
        this.data = data;
        return this;
    }

    /**
     * 
     * @return
     *     The next
     */
    public String getNext() {
        return next;
    }

    /**
     * 
     * @param next
     *     The next
     */
    public void setNext(String next) {
        this.next = next;
    }

    public KongPluginACLResponseList withNext(String next) {
        this.next = next;
        return this;
    }

    /**
     * 
     * @return
     *     The offset
     */
    public String getOffset() {
        return offset;
    }

    /**
     * 
     * @param offset
     *     The offset
     */
    public void setOffset(String offset) {
        this.offset = offset;
    }

    public KongPluginACLResponseList withOffset(String offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 
     * @return
     *     The total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    public KongPluginACLResponseList withTotal(Long total) {
        this.total = total;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).append(next).append(offset).append(total).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginACLResponseList) == false) {
            return false;
        }
        KongPluginACLResponseList rhs = ((KongPluginACLResponseList) other);
        return new EqualsBuilder().append(data, rhs.data).append(next, rhs.next).append(offset, rhs.offset).append(total, rhs.total).isEquals();
    }

}
