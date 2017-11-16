package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceUpstreamTargetBean implements Serializable {

    @Column(name = "target", nullable = false)
    private String target;
    @Column(name = "port")
    private Long port;
    @Column(name = "weight", nullable = false)
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
        if (!(o instanceof ServiceUpstreamTargetBean)) return false;

        ServiceUpstreamTargetBean that = (ServiceUpstreamTargetBean) o;

        if (!target.equals(that.target)) return false;
        if (!port.equals(that.port)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = 31 * result + port.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ServiceUpstreamTargetBean{" +
                "target='" + target + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}
