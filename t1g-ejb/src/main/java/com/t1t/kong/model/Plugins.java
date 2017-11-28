
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Plugins {

    @SerializedName("available_on_server")
    @Expose
    private AvailableOnServer availableOnServer;

    /**
     * 
     * @return
     *     The availableOnServer
     */
    public AvailableOnServer getAvailableOnServer() {
        return availableOnServer;
    }

    /**
     * 
     * @param availableOnServer
     *     The available_on_server
     */
    public void setAvailableOnServer(AvailableOnServer availableOnServer) {
        this.availableOnServer = availableOnServer;
    }

    public Plugins withAvailableOnServer(AvailableOnServer availableOnServer) {
        this.availableOnServer = availableOnServer;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(availableOnServer).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Plugins) == false) {
            return false;
        }
        Plugins rhs = ((Plugins) other);
        return new EqualsBuilder().append(availableOnServer, rhs.availableOnServer).isEquals();
    }

}
