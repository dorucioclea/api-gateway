
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
public class DataDogMetricsSeries {

    @SerializedName("metric")
    @Expose
    private String metric;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("unit")
    @Expose
    private List<Object> unit = new ArrayList<Object>();
    @SerializedName("pointlist")
    @Expose
    private List<List<Double>> pointlist = new ArrayList<List<Double>>();
    @SerializedName("end")
    @Expose
    private Double end;
    @SerializedName("interval")
    @Expose
    private Double interval;
    @SerializedName("start")
    @Expose
    private Double start;
    @SerializedName("length")
    @Expose
    private Double length;
    @SerializedName("aggr")
    @Expose
    private String aggr;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("expression")
    @Expose
    private String expression;

    /**
     * 
     * @return
     *     The metric
     */
    public String getMetric() {
        return metric;
    }

    /**
     * 
     * @param metric
     *     The metric
     */
    public void setMetric(String metric) {
        this.metric = metric;
    }

    public DataDogMetricsSeries withMetric(String metric) {
        this.metric = metric;
        return this;
    }

    /**
     * 
     * @return
     *     The attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public DataDogMetricsSeries withAttributes(Attributes attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * 
     * @return
     *     The unit
     */
    public List<Object> getUnit() {
        return unit;
    }

    /**
     * 
     * @param unit
     *     The unit
     */
    public void setUnit(List<Object> unit) {
        this.unit = unit;
    }

    public DataDogMetricsSeries withUnit(List<Object> unit) {
        this.unit = unit;
        return this;
    }

    /**
     * 
     * @return
     *     The pointlist
     */
    public List<List<Double>> getPointlist() {
        return pointlist;
    }

    /**
     * 
     * @param pointlist
     *     The pointlist
     */
    public void setPointlist(List<List<Double>> pointlist) {
        this.pointlist = pointlist;
    }

    public DataDogMetricsSeries withPointlist(List<List<Double>> pointlist) {
        this.pointlist = pointlist;
        return this;
    }

    /**
     * 
     * @return
     *     The end
     */
    public Double getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    public void setEnd(Double end) {
        this.end = end;
    }

    public DataDogMetricsSeries withEnd(Double end) {
        this.end = end;
        return this;
    }

    /**
     * 
     * @return
     *     The interval
     */
    public Double getInterval() {
        return interval;
    }

    /**
     * 
     * @param interval
     *     The interval
     */
    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public DataDogMetricsSeries withInterval(Double interval) {
        this.interval = interval;
        return this;
    }

    /**
     * 
     * @return
     *     The start
     */
    public Double getStart() {
        return start;
    }

    /**
     * 
     * @param start
     *     The start
     */
    public void setStart(Double start) {
        this.start = start;
    }

    public DataDogMetricsSeries withStart(Double start) {
        this.start = start;
        return this;
    }

    /**
     * 
     * @return
     *     The length
     */
    public Double getLength() {
        return length;
    }

    /**
     * 
     * @param length
     *     The length
     */
    public void setLength(Double length) {
        this.length = length;
    }

    public DataDogMetricsSeries withLength(Double length) {
        this.length = length;
        return this;
    }

    /**
     * 
     * @return
     *     The aggr
     */
    public String getAggr() {
        return aggr;
    }

    /**
     * 
     * @param aggr
     *     The aggr
     */
    public void setAggr(String aggr) {
        this.aggr = aggr;
    }

    public DataDogMetricsSeries withAggr(String aggr) {
        this.aggr = aggr;
        return this;
    }

    /**
     * 
     * @return
     *     The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * 
     * @param scope
     *     The scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public DataDogMetricsSeries withScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * 
     * @return
     *     The expression
     */
    public String getExpression() {
        return expression;
    }

    /**
     * 
     * @param expression
     *     The expression
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    public DataDogMetricsSeries withExpression(String expression) {
        this.expression = expression;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(metric).append(attributes).append(unit).append(pointlist).append(end).append(interval).append(start).append(length).append(aggr).append(scope).append(expression).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DataDogMetricsSeries) == false) {
            return false;
        }
        DataDogMetricsSeries rhs = ((DataDogMetricsSeries) other);
        return new EqualsBuilder().append(metric, rhs.metric).append(attributes, rhs.attributes).append(unit, rhs.unit).append(pointlist, rhs.pointlist).append(end, rhs.end).append(interval, rhs.interval).append(start, rhs.start).append(length, rhs.length).append(aggr, rhs.aggr).append(scope, rhs.scope).append(expression, rhs.expression).isEquals();
    }

}
