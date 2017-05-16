package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceUpstreamRequest implements Serializable {

    private String target;
    private Long port;
    private Long weight;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceUpstreamRequest)) return false;

        ServiceUpstreamRequest that = (ServiceUpstreamRequest) o;

        if (!target.equals(that.target)) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceUpstreamRequest{" +
                "target='" + target + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}