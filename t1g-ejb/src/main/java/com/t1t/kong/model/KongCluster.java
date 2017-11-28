
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
public class KongCluster {

    @SerializedName("data")
    @Expose
    private List<KongClusterItem> data = new ArrayList<KongClusterItem>();
    @SerializedName("total")
    @Expose
    private Long total;

    /**
     * 
     * @return
     *     The data
     */
    public List<KongClusterItem> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<KongClusterItem> data) {
        this.data = data;
    }

    public KongCluster withData(List<KongClusterItem> data) {
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

    public KongCluster withTotal(Long total) {
        this.total = total;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).append(total).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongCluster) == false) {
            return false;
        }
        KongCluster rhs = ((KongCluster) other);
        return new EqualsBuilder().append(data, rhs.data).append(total, rhs.total).isEquals();
    }

}
