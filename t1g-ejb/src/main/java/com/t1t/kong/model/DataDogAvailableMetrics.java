
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
public class DataDogAvailableMetrics {

    @SerializedName("metrics")
    @Expose
    private List<String> metrics = new ArrayList<String>();
    @SerializedName("from")
    @Expose
    private Double from;

    /**
     * 
     * @return
     *     The metrics
     */
    public List<String> getMetrics() {
        return metrics;
    }

    /**
     * 
     * @param metrics
     *     The metrics
     */
    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }

    public DataDogAvailableMetrics withMetrics(List<String> metrics) {
        this.metrics = metrics;
        return this;
    }

    /**
     * 
     * @return
     *     The from
     */
    public Double getFrom() {
        return from;
    }

    /**
     * 
     * @param from
     *     The from
     */
    public void setFrom(Double from) {
        this.from = from;
    }

    public DataDogAvailableMetrics withFrom(Double from) {
        this.from = from;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(metrics).append(from).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DataDogAvailableMetrics) == false) {
            return false;
        }
        DataDogAvailableMetrics rhs = ((DataDogAvailableMetrics) other);
        return new EqualsBuilder().append(metrics, rhs.metrics).append(from, rhs.from).isEquals();
    }

}
