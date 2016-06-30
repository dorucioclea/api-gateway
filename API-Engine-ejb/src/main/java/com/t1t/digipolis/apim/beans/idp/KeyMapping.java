package com.t1t.digipolis.apim.beans.idp;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by michallispashidis on 30/06/16.
 */

@Entity
@Table(name = "key_mapping", uniqueConstraints = { @UniqueConstraint(columnNames = { "from_spec_type", "to_spec_type", "from_spec_claim" }) })
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class KeyMapping implements Serializable{
    @Id @Column(name="from_spec_type", nullable=false)
    private String fromSpecType;

    @Id @Column(name="to_spec_type", nullable=false)
    private String toSpecType;

    @Id @Column(name="from_spec_claim", nullable=false)
    private String fromSpecClaim;

    @Id @Column(name="to_spec_claim", nullable=false)
    private String toSpecClaim;

    public String getFromSpecType() {
        return fromSpecType;
    }

    public void setFromSpecType(String fromSpecType) {
        this.fromSpecType = fromSpecType;
    }

    public String getToSpecType() {
        return toSpecType;
    }

    public void setToSpecType(String toSpecType) {
        this.toSpecType = toSpecType;
    }

    public String getFromSpecClaim() {
        return fromSpecClaim;
    }

    public void setFromSpecClaim(String fromSpecClaim) {
        this.fromSpecClaim = fromSpecClaim;
    }

    public String getToSpecClaim() {
        return toSpecClaim;
    }

    public void setToSpecClaim(String toSpecClaim) {
        this.toSpecClaim = toSpecClaim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyMapping that = (KeyMapping) o;

        if (!fromSpecType.equals(that.fromSpecType)) return false;
        if (!toSpecType.equals(that.toSpecType)) return false;
        if (!fromSpecClaim.equals(that.fromSpecClaim)) return false;
        return toSpecClaim != null ? toSpecClaim.equals(that.toSpecClaim) : that.toSpecClaim == null;

    }

    @Override
    public int hashCode() {
        int result = fromSpecType.hashCode();
        result = 31 * result + toSpecType.hashCode();
        result = 31 * result + fromSpecClaim.hashCode();
        result = 31 * result + (toSpecClaim != null ? toSpecClaim.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KeyMapping{" +
                "fromSpecType='" + fromSpecType + '\'' +
                ", toSpecType='" + toSpecType + '\'' +
                ", fromSpecClaim='" + fromSpecClaim + '\'' +
                ", toSpecClaim='" + toSpecClaim + '\'' +
                '}';
    }
}
