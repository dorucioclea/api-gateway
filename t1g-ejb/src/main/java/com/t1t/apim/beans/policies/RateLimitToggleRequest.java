package com.t1t.apim.beans.policies;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RateLimitToggleRequest implements Serializable {

    private String organizationId;
    private String applicationId;
    private String applicationVersion;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitToggleRequest)) return false;

        RateLimitToggleRequest that = (RateLimitToggleRequest) o;

        if (!organizationId.equals(that.organizationId)) return false;
        if (!applicationId.equals(that.applicationId)) return false;
        if (!applicationVersion.equals(that.applicationVersion)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + applicationId.hashCode();
        result = 31 * result + applicationVersion.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitResetBean{" +
                "organizationId='" + organizationId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                '}';
    }
}