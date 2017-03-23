package com.t1t.apim.beans.contracts;

import java.io.Serializable;

/**
 * The bean used to create a new contract.
 *
 */
public class NewContractBean implements Serializable {

    private static final long serialVersionUID = -2326957716478467884L;

    private String serviceOrgId;
    private String serviceId;
    private String serviceVersion;
    private Boolean termsAgreed;

    private String planId;

    /**
     * Constructor.
     */
    public NewContractBean() {
    }

    /**
     * @return the serviceOrgId
     */
    public String getServiceOrgId() {
        return serviceOrgId;
    }

    /**
     * @param serviceOrgId the serviceOrgId to set
     */
    public void setServiceOrgId(String serviceOrgId) {
        this.serviceOrgId = serviceOrgId;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the serviceVersion
     */
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @param serviceVersion the serviceVersion to set
     */
    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    /**
     * @return the planId
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * @param planId the planId to set
     */
    public void setPlanId(String planId) {
        this.planId = planId;
    }

    /**
     * @return the value of termsAgreed
     */
    public Boolean getTermsAgreed() {
        return termsAgreed;
    }

    /**
     * @param termsAgreed the value to set
     */
    public void setTermsAgreed(Boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = serviceOrgId != null ? serviceOrgId.hashCode() : 0;
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (serviceVersion != null ? serviceVersion.hashCode() : 0);
        result = 31 * result + (termsAgreed != null ? termsAgreed.hashCode() : 0);
        result = 31 * result + (planId != null ? planId.hashCode() : 0);
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewContractBean that = (NewContractBean) o;

        if (serviceOrgId != null ? !serviceOrgId.equals(that.serviceOrgId) : that.serviceOrgId != null) return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;
        if (serviceVersion != null ? !serviceVersion.equals(that.serviceVersion) : that.serviceVersion != null)
            return false;
        if (termsAgreed != null ? !termsAgreed.equals(that.termsAgreed) : that.termsAgreed != null) return false;
        return planId != null ? planId.equals(that.planId) : that.planId == null;

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewContractBean{" +
                "serviceOrgId='" + serviceOrgId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", termsAgreed=" + termsAgreed +
                ", planId='" + planId + '\'' +
                '}';
    }
}
