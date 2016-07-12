package com.t1t.digipolis.apim.beans.categories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceTagsBean {

    private String organizationId;
    private String serviceId;
    private Set<String> tags;

    public ServiceTagsBean() {
    }

    public ServiceTagsBean(String organizationId, String serviceId, Set<String> tags) {
        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.tags = tags;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceTagsBean that = (ServiceTagsBean) o;

        if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
            return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;

    }

    @Override
    public int hashCode() {
        int result = organizationId != null ? organizationId.hashCode() : 0;
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceTagsBean{" +
                "organizationId='" + organizationId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", tags=" + tags +
                '}';
    }
}