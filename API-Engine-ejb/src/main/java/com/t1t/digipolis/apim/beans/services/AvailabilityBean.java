package com.t1t.digipolis.apim.beans.services;

import javax.persistence.Column;

/**
 * Created by michallispashidis on 25/06/16.
 */
public class AvailabilityBean {
    private String code;
    private String name;

    public AvailabilityBean(String code, String name) {
        this.code = code;
        this.name = name;
    }

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
