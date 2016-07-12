package com.t1t.digipolis.apim.beans.defaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Entity
@Table(name = "defaults")
public class DefaultsBean implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "service_terms")
    private String serviceTerms;

    public DefaultsBean() {
    }

    public DefaultsBean(String id, String serviceTerms) {
        this.id = id;
        this.serviceTerms = serviceTerms;
    }

    public DefaultsBean(String serviceTerms) {
        this.serviceTerms = serviceTerms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceTerms() {
        return serviceTerms;
    }

    public void setServiceTerms(String terms) {
        this.serviceTerms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultsBean that = (DefaultsBean) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultsBean{" +
                "id='" + id + '\'' +
                ", serviceTerms='" + serviceTerms + '\'' +
                '}';
    }
}