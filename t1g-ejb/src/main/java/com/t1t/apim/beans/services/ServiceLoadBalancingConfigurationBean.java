package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceLoadBalancingConfigurationBean implements Serializable {

    private Boolean customLoadBalancing;
    private String endpoint;
    private Set<ServiceUpstreamTargetBean> upstreamTargets;

    public Boolean getCustomLoadBalancing() {
        return customLoadBalancing;
    }

    public void setCustomLoadBalancing(Boolean customLoadBalancing) {
        this.customLoadBalancing = customLoadBalancing;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Set<ServiceUpstreamTargetBean> getUpstreamTargets() {
        return upstreamTargets;
    }

    public void setUpstreamTargets(Set<ServiceUpstreamTargetBean> upstreamTargets) {
        this.upstreamTargets = upstreamTargets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceLoadBalancingConfigurationBean)) return false;

        ServiceLoadBalancingConfigurationBean that = (ServiceLoadBalancingConfigurationBean) o;

        if (customLoadBalancing != null ? !customLoadBalancing.equals(that.customLoadBalancing) : that.customLoadBalancing != null)
            return false;
        if (endpoint != null ? !endpoint.equals(that.endpoint) : that.endpoint != null) return false;
        if (upstreamTargets != null ? !upstreamTargets.equals(that.upstreamTargets) : that.upstreamTargets != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = customLoadBalancing != null ? customLoadBalancing.hashCode() : 0;
        result = 31 * result + (endpoint != null ? endpoint.hashCode() : 0);
        result = 31 * result + (upstreamTargets != null ? upstreamTargets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceLoadBalancingConfigurationBean{" +
                "customLoadBalancing=" + customLoadBalancing +
                ", endpoint='" + endpoint + '\'' +
                ", upstreamTargets=" + upstreamTargets +
                '}';
    }
}