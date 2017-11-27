
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
public class KongPluginJWTResponseList {

    @SerializedName("data")
    @Expose
    private List<KongPluginJWTResponse> data = new ArrayList<KongPluginJWTResponse>();
    @SerializedName("total")
    @Expose
    private Long total;
    @SerializedName("next")
    @Expose
    private String next;

    /**
     * 
     * @return
     *     The data
     */
    public List<KongPluginJWTResponse> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<KongPluginJWTResponse> data) {
        this.data = data;
    }

    public KongPluginJWTResponseList withData(List<KongPluginJWTResponse> data) {
        this.data = data;
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

    public KongPluginJWTResponseList withTotal(Long total) {
        this.total = total;
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

    public KongPluginJWTResponseList withNext(String next) {
        this.next = next;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).append(total).append(next).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginJWTResponseList) == false) {
            return false;
        }
        KongPluginJWTResponseList rhs = ((KongPluginJWTResponseList) other);
        return new EqualsBuilder().append(data, rhs.data).append(total, rhs.total).append(next, rhs.next).isEquals();
    }

}
