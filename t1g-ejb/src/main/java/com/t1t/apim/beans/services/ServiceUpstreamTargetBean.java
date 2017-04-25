package com.t1t.apim.beans.services;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ServiceUpstreamTargetBean implements Serializable {

    @Column(name = "target", nullable=false)
    private String target;
    @Column(name = "weight", nullable=false)
    private Long weight;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = target != null ? target.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceUpstreamTargetBean{" +
                "target='" + target + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
