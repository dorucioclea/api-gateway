
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
public class Tags {

    @SerializedName("latency")
    @Expose
    private List<String> latency = new ArrayList<String>();
    @SerializedName("request_per_user")
    @Expose
    private List<String> requestPerUser = new ArrayList<String>();
    @SerializedName("unique_users")
    @Expose
    private List<String> uniqueUsers = new ArrayList<String>();
    @SerializedName("request_count")
    @Expose
    private List<String> requestCount = new ArrayList<String>();
    @SerializedName("request_size")
    @Expose
    private List<String> requestSize = new ArrayList<String>();
    @SerializedName("upstream_latency")
    @Expose
    private List<String> upstreamLatency = new ArrayList<String>();
    @SerializedName("response_size")
    @Expose
    private List<String> responseSize = new ArrayList<String>();
    @SerializedName("status_count")
    @Expose
    private List<String> statusCount = new ArrayList<String>();

    /**
     * 
     * @return
     *     The latency
     */
    public List<String> getLatency() {
        return latency;
    }

    /**
     * 
     * @param latency
     *     The latency
     */
    public void setLatency(List<String> latency) {
        this.latency = latency;
    }

    public Tags withLatency(List<String> latency) {
        this.latency = latency;
        return this;
    }

    /**
     * 
     * @return
     *     The requestPerUser
     */
    public List<String> getRequestPerUser() {
        return requestPerUser;
    }

    /**
     * 
     * @param requestPerUser
     *     The request_per_user
     */
    public void setRequestPerUser(List<String> requestPerUser) {
        this.requestPerUser = requestPerUser;
    }

    public Tags withRequestPerUser(List<String> requestPerUser) {
        this.requestPerUser = requestPerUser;
        return this;
    }

    /**
     * 
     * @return
     *     The uniqueUsers
     */
    public List<String> getUniqueUsers() {
        return uniqueUsers;
    }

    /**
     * 
     * @param uniqueUsers
     *     The unique_users
     */
    public void setUniqueUsers(List<String> uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public Tags withUniqueUsers(List<String> uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
        return this;
    }

    /**
     * 
     * @return
     *     The requestCount
     */
    public List<String> getRequestCount() {
        return requestCount;
    }

    /**
     * 
     * @param requestCount
     *     The request_count
     */
    public void setRequestCount(List<String> requestCount) {
        this.requestCount = requestCount;
    }

    public Tags withRequestCount(List<String> requestCount) {
        this.requestCount = requestCount;
        return this;
    }

    /**
     * 
     * @return
     *     The requestSize
     */
    public List<String> getRequestSize() {
        return requestSize;
    }

    /**
     * 
     * @param requestSize
     *     The request_size
     */
    public void setRequestSize(List<String> requestSize) {
        this.requestSize = requestSize;
    }

    public Tags withRequestSize(List<String> requestSize) {
        this.requestSize = requestSize;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamLatency
     */
    public List<String> getUpstreamLatency() {
        return upstreamLatency;
    }

    /**
     * 
     * @param upstreamLatency
     *     The upstream_latency
     */
    public void setUpstreamLatency(List<String> upstreamLatency) {
        this.upstreamLatency = upstreamLatency;
    }

    public Tags withUpstreamLatency(List<String> upstreamLatency) {
        this.upstreamLatency = upstreamLatency;
        return this;
    }

    /**
     * 
     * @return
     *     The responseSize
     */
    public List<String> getResponseSize() {
        return responseSize;
    }

    /**
     * 
     * @param responseSize
     *     The response_size
     */
    public void setResponseSize(List<String> responseSize) {
        this.responseSize = responseSize;
    }

    public Tags withResponseSize(List<String> responseSize) {
        this.responseSize = responseSize;
        return this;
    }

    /**
     * 
     * @return
     *     The statusCount
     */
    public List<String> getStatusCount() {
        return statusCount;
    }

    /**
     * 
     * @param statusCount
     *     The status_count
     */
    public void setStatusCount(List<String> statusCount) {
        this.statusCount = statusCount;
    }

    public Tags withStatusCount(List<String> statusCount) {
        this.statusCount = statusCount;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(latency).append(requestPerUser).append(uniqueUsers).append(requestCount).append(requestSize).append(upstreamLatency).append(responseSize).append(statusCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tags) == false) {
            return false;
        }
        Tags rhs = ((Tags) other);
        return new EqualsBuilder().append(latency, rhs.latency).append(requestPerUser, rhs.requestPerUser).append(uniqueUsers, rhs.uniqueUsers).append(requestCount, rhs.requestCount).append(requestSize, rhs.requestSize).append(upstreamLatency, rhs.upstreamLatency).append(responseSize, rhs.responseSize).append(statusCount, rhs.statusCount).isEquals();
    }

}
