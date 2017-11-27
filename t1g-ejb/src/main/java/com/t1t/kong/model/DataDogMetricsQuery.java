
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
public class DataDogMetricsQuery {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("res_type")
    @Expose
    private String resType;
    @SerializedName("series")
    @Expose
    private List<DataDogMetricsSeries> series = new ArrayList<DataDogMetricsSeries>();
    @SerializedName("from_date")
    @Expose
    private Double fromDate;
    @SerializedName("group_by")
    @Expose
    private List<Object> groupBy = new ArrayList<Object>();
    @SerializedName("to_date")
    @Expose
    private Double toDate;
    @SerializedName("query")
    @Expose
    private String query;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public DataDogMetricsQuery withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * 
     * @return
     *     The resType
     */
    public String getResType() {
        return resType;
    }

    /**
     * 
     * @param resType
     *     The res_type
     */
    public void setResType(String resType) {
        this.resType = resType;
    }

    public DataDogMetricsQuery withResType(String resType) {
        this.resType = resType;
        return this;
    }

    /**
     * 
     * @return
     *     The series
     */
    public List<DataDogMetricsSeries> getSeries() {
        return series;
    }

    /**
     * 
     * @param series
     *     The series
     */
    public void setSeries(List<DataDogMetricsSeries> series) {
        this.series = series;
    }

    public DataDogMetricsQuery withSeries(List<DataDogMetricsSeries> series) {
        this.series = series;
        return this;
    }

    /**
     * 
     * @return
     *     The fromDate
     */
    public Double getFromDate() {
        return fromDate;
    }

    /**
     * 
     * @param fromDate
     *     The from_date
     */
    public void setFromDate(Double fromDate) {
        this.fromDate = fromDate;
    }

    public DataDogMetricsQuery withFromDate(Double fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    /**
     * 
     * @return
     *     The groupBy
     */
    public List<Object> getGroupBy() {
        return groupBy;
    }

    /**
     * 
     * @param groupBy
     *     The group_by
     */
    public void setGroupBy(List<Object> groupBy) {
        this.groupBy = groupBy;
    }

    public DataDogMetricsQuery withGroupBy(List<Object> groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    /**
     * 
     * @return
     *     The toDate
     */
    public Double getToDate() {
        return toDate;
    }

    /**
     * 
     * @param toDate
     *     The to_date
     */
    public void setToDate(Double toDate) {
        this.toDate = toDate;
    }

    public DataDogMetricsQuery withToDate(Double toDate) {
        this.toDate = toDate;
        return this;
    }

    /**
     * 
     * @return
     *     The query
     */
    public String getQuery() {
        return query;
    }

    /**
     * 
     * @param query
     *     The query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public DataDogMetricsQuery withQuery(String query) {
        this.query = query;
        return this;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public DataDogMetricsQuery withMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(status).append(resType).append(series).append(fromDate).append(groupBy).append(toDate).append(query).append(message).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DataDogMetricsQuery) == false) {
            return false;
        }
        DataDogMetricsQuery rhs = ((DataDogMetricsQuery) other);
        return new EqualsBuilder().append(status, rhs.status).append(resType, rhs.resType).append(series, rhs.series).append(fromDate, rhs.fromDate).append(groupBy, rhs.groupBy).append(toDate, rhs.toDate).append(query, rhs.query).append(message, rhs.message).isEquals();
    }

}
