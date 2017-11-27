
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Timers_ {

    @SerializedName("running")
    @Expose
    private Long running;
    @SerializedName("pending")
    @Expose
    private Long pending;

    /**
     * 
     * @return
     *     The running
     */
    public Long getRunning() {
        return running;
    }

    /**
     * 
     * @param running
     *     The running
     */
    public void setRunning(Long running) {
        this.running = running;
    }

    public Timers_ withRunning(Long running) {
        this.running = running;
        return this;
    }

    /**
     * 
     * @return
     *     The pending
     */
    public Long getPending() {
        return pending;
    }

    /**
     * 
     * @param pending
     *     The pending
     */
    public void setPending(Long pending) {
        this.pending = pending;
    }

    public Timers_ withPending(Long pending) {
        this.pending = pending;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(running).append(pending).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Timers_) == false) {
            return false;
        }
        Timers_ rhs = ((Timers_) other);
        return new EqualsBuilder().append(running, rhs.running).append(pending, rhs.pending).isEquals();
    }

}
