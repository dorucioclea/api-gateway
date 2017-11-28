
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
public class Plugins__ {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enabled_in_cluster")
    @Expose
    private List<String> enabledInCluster = new ArrayList<String>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("available_on_server")
    @Expose
    private AvailableOnServer_ availableOnServer;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The enabledInCluster
     */
    public List<String> getEnabledInCluster() {
        return enabledInCluster;
    }

    /**
     * 
     * (Required)
     * 
     * @param enabledInCluster
     *     The enabled_in_cluster
     */
    public void setEnabledInCluster(List<String> enabledInCluster) {
        this.enabledInCluster = enabledInCluster;
    }

    public Plugins__ withEnabledInCluster(List<String> enabledInCluster) {
        this.enabledInCluster = enabledInCluster;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The availableOnServer
     */
    public AvailableOnServer_ getAvailableOnServer() {
        return availableOnServer;
    }

    /**
     * 
     * (Required)
     * 
     * @param availableOnServer
     *     The available_on_server
     */
    public void setAvailableOnServer(AvailableOnServer_ availableOnServer) {
        this.availableOnServer = availableOnServer;
    }

    public Plugins__ withAvailableOnServer(AvailableOnServer_ availableOnServer) {
        this.availableOnServer = availableOnServer;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(enabledInCluster).append(availableOnServer).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Plugins__) == false) {
            return false;
        }
        Plugins__ rhs = ((Plugins__) other);
        return new EqualsBuilder().append(enabledInCluster, rhs.enabledInCluster).append(availableOnServer, rhs.availableOnServer).isEquals();
    }

}
