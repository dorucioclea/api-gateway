package com.t1t.digipolis.apim.beans.services;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Models a plan+version that is available for use with a particular Service.  This
 * makes the Plan available when forming a Contract between an app and a Service.
 *
 */
@Embeddable
public class ServicePlanBean implements Serializable {

    private static final long serialVersionUID = 7972763768594076697L;

    @Column(name = "plan_id", nullable=false)
    private String planId;
    @Column(nullable=false)
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
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return planId + "(" + version + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((planId == null) ? 0 : planId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        ServicePlanBean other = (ServicePlanBean) obj;
        if (planId == null) {
            if (other.planId != null)
                return false;
        } else if (!planId.equals(other.planId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
