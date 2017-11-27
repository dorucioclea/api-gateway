
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
public class KongPluginConfigList {

    @SerializedName("data")
    @Expose
    private List<KongPluginConfig> data = new ArrayList<KongPluginConfig>();
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("total")
    @Expose
    private Long total;
    @SerializedName("offset")
    @Expose
    private String offset;

    /**
     * 
     * @return
     *     The data
     */
    public List<KongPluginConfig> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<KongPluginConfig> data) {
        this.data = data;
    }

    public KongPluginConfigList withData(List<KongPluginConfig> data) {
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

    public KongPluginConfigList withNext(String next) {
        this.next = next;
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

    public KongPluginConfigList withTotal(Long total) {
        this.total = total;
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

    public KongPluginConfigList withOffset(String offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).append(next).append(total).append(offset).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginConfigList) == false) {
            return false;
        }
        KongPluginConfigList rhs = ((KongPluginConfigList) other);
        return new EqualsBuilder().append(data, rhs.data).append(next, rhs.next).append(total, rhs.total).append(offset, rhs.offset).isEquals();
    }

}
