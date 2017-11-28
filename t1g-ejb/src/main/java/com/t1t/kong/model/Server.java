
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Server {

    @SerializedName("total_requests")
    @Expose
    private Long totalRequests;
    @SerializedName("connections_active")
    @Expose
    private Long connectionsActive;
    @SerializedName("connections_accepted")
    @Expose
    private Long connectionsAccepted;
    @SerializedName("connections_handled")
    @Expose
    private Long connectionsHandled;
    @SerializedName("connections_reading")
    @Expose
    private Long connectionsReading;
    @SerializedName("connections_writing")
    @Expose
    private Long connectionsWriting;
    @SerializedName("connections_waiting")
    @Expose
    private Long connectionsWaiting;

    /**
     * 
     * @return
     *     The totalRequests
     */
    public Long getTotalRequests() {
        return totalRequests;
    }

    /**
     * 
     * @param totalRequests
     *     The total_requests
     */
    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Server withTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionsActive
     */
    public Long getConnectionsActive() {
        return connectionsActive;
    }

    /**
     * 
     * @param connectionsActive
     *     The connections_active
     */
    public void setConnectionsActive(Long connectionsActive) {
        this.connectionsActive = connectionsActive;
    }

    public Server withConnectionsActive(Long connectionsActive) {
        this.connectionsActive = connectionsActive;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionsAccepted
     */
    public Long getConnectionsAccepted() {
        return connectionsAccepted;
    }

    /**
     * 
     * @param connectionsAccepted
     *     The connections_accepted
     */
    public void setConnectionsAccepted(Long connectionsAccepted) {
        this.connectionsAccepted = connectionsAccepted;
    }

    public Server withConnectionsAccepted(Long connectionsAccepted) {
        this.connectionsAccepted = connectionsAccepted;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionsHandled
     */
    public Long getConnectionsHandled() {
        return connectionsHandled;
    }

    /**
     * 
     * @param connectionsHandled
     *     The connections_handled
     */
    public void setConnectionsHandled(Long connectionsHandled) {
        this.connectionsHandled = connectionsHandled;
    }

    public Server withConnectionsHandled(Long connectionsHandled) {
        this.connectionsHandled = connectionsHandled;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionsReading
     */
    public Long getConnectionsReading() {
        return connectionsReading;
    }

    /**
     * 
     * @param connectionsReading
     *     The connections_reading
     */
    public void setConnectionsReading(Long connectionsReading) {
        this.connectionsReading = connectionsReading;
    }

    public Server withConnectionsReading(Long connectionsReading) {
        this.connectionsReading = connectionsReading;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionsWriting
     */
    public Long getConnectionsWriting() {
        return connectionsWriting;
    }

    /**
     * 
     * @param connectionsWriting
     *     The connections_writing
     */
    public void setConnectionsWriting(Long connectionsWriting) {
        this.connectionsWriting = connectionsWriting;
    }

    public Server withConnectionsWriting(Long connectionsWriting) {
        this.connectionsWriting = connectionsWriting;
        return this;
    }

    /**
     * 
     * @return
     *     The connectionsWaiting
     */
    public Long getConnectionsWaiting() {
        return connectionsWaiting;
    }

    /**
     * 
     * @param connectionsWaiting
     *     The connections_waiting
     */
    public void setConnectionsWaiting(Long connectionsWaiting) {
        this.connectionsWaiting = connectionsWaiting;
    }

    public Server withConnectionsWaiting(Long connectionsWaiting) {
        this.connectionsWaiting = connectionsWaiting;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalRequests).append(connectionsActive).append(connectionsAccepted).append(connectionsHandled).append(connectionsReading).append(connectionsWriting).append(connectionsWaiting).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Server) == false) {
            return false;
        }
        Server rhs = ((Server) other);
        return new EqualsBuilder().append(totalRequests, rhs.totalRequests).append(connectionsActive, rhs.connectionsActive).append(connectionsAccepted, rhs.connectionsAccepted).append(connectionsHandled, rhs.connectionsHandled).append(connectionsReading, rhs.connectionsReading).append(connectionsWriting, rhs.connectionsWriting).append(connectionsWaiting, rhs.connectionsWaiting).isEquals();
    }

}
