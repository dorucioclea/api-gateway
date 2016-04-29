package com.t1t.digipolis.apim.beans.contracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewContractRequestBean implements Serializable {

    private String applicationId;
    private String applicationOrg;
    private String applicationVersion;

    private String planId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationOrg() {
        return applicationOrg;
    }

    public void setApplicationOrg(String applicationOrg) {
        this.applicationOrg = applicationOrg;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewContractRequestBean that = (NewContractRequestBean) o;

        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (applicationOrg != null ? !applicationOrg.equals(that.applicationOrg) : that.applicationOrg != null)
            return false;
        if (applicationVersion != null ? !applicationVersion.equals(that.applicationVersion) : that.applicationVersion != null)
            return false;
        return planId != null ? planId.equals(that.planId) : that.planId == null;

    }

    @Override
    public int hashCode() {
        int result = applicationId != null ? applicationId.hashCode() : 0;
        result = 31 * result + (applicationOrg != null ? applicationOrg.hashCode() : 0);
        result = 31 * result + (applicationVersion != null ? applicationVersion.hashCode() : 0);
        result = 31 * result + (planId != null ? planId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewContractRequestBean{" +
                "applicationId='" + applicationId + '\'' +
                ", applicationOrg='" + applicationOrg + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", planId='" + planId + '\'' +
                '}';
    }
}