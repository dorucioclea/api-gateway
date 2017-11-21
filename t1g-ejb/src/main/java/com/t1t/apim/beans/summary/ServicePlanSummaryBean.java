package com.t1t.apim.beans.summary;

import java.io.Serializable;

/**
 * Models a plan+version that is available for use with a particular Service.  Also
 * includes extra information such as the plan name.
 */
public class ServicePlanSummaryBean implements Serializable {

    private static final long serialVersionUID = 2380693193992732580L;

    private String planId;
    private String planName;
    private String planDescription;
    private String version;

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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the planName
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * @param planName the planName to set
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((planId == null) ? 0 : planId.hashCode());
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
        ServicePlanSummaryBean other = (ServicePlanSummaryBean) obj;
        if (planId == null) {
            if (other.planId != null)
                return false;
        } else if (!planId.equals(other.planId))
            return false;
        return true;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return getPlanName() + "(" + version + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @return the planDescription
     */
    public String getPlanDescription() {
        return planDescription;
    }

    /**
     * @param planDescription the planDescription to set
     */
    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

}
