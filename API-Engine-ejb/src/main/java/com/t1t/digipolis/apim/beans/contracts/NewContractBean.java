package com.t1t.digipolis.apim.beans.contracts;

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
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((planId == null) ? 0 : planId.hashCode());
        result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
        result = prime * result + ((serviceOrgId == null) ? 0 : serviceOrgId.hashCode());
        result = prime * result + ((serviceVersion == null) ? 0 : serviceVersion.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NewContractBean other = (NewContractBean) obj;
        if (planId == null) {
            if (other.planId != null)
                return false;
        } else if (!planId.equals(other.planId))
            return false;
        if (serviceId == null) {
            if (other.serviceId != null)
                return false;
        } else if (!serviceId.equals(other.serviceId))
            return false;
        if (serviceOrgId == null) {
            if (other.serviceOrgId != null)
                return false;
        } else if (!serviceOrgId.equals(other.serviceOrgId))
            return false;
        if (serviceVersion == null) {
            if (other.serviceVersion != null)
                return false;
        } else if (!serviceVersion.equals(other.serviceVersion))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewContractBean [serviceOrgId=" + serviceOrgId + ", serviceId=" + serviceId
                + ", serviceVersion=" + serviceVersion + ", planId=" + planId + "]";
    }

}
