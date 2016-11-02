package com.t1t.digipolis.apim.beans.brandings;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceBrandingSummaryBean implements Serializable{

    private String id;
    private String name;

    public ServiceBrandingSummaryBean() {
    }

    public ServiceBrandingSummaryBean(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public ServiceBrandingSummaryBean(ServiceBrandingBean bean) {
        this.name = bean.getName();
        this.id = bean.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (o == null || getClass() != o.getClass()) return false;

        ServiceBrandingSummaryBean that = (ServiceBrandingSummaryBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceBrandingSummaryBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}