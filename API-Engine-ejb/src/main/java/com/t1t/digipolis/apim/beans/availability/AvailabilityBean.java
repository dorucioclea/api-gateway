package com.t1t.digipolis.apim.beans.availability;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by michallispashidis on 12/02/16.
 */
@Entity
@Table(name = "availabilities")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AvailabilityBean implements Serializable {
    @Id
    @Column(nullable=false)
    private String code;
    @Column(nullable=false)
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilityBean)) return false;

        AvailabilityBean that = (AvailabilityBean) o;

        if (!code.equals(that.code)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AvailabilityBean{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
