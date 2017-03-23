package com.t1t.apim.beans.audit.data;

import java.io.Serializable;

/**
 * The data saved along with the audit entry when ownership is transferred
 * for an organization.
 */
public class OwnershipTransferData implements Serializable {

    private static final long serialVersionUID = 2139252302476513914L;

    private String previousOwnerId;
    private String newOwnerId;

    /**
     * @param previousOwnerId the userId of the previous organization owner
     */
    public void setPreviousOwnerId(String previousOwnerId) {
        this.previousOwnerId = previousOwnerId;
    }

    /**
     * @return the userId of the previous organization owner
     */
    public String getPreviousOwnerId() {
        return previousOwnerId;
    }

    /**
     * @param newOwnerId the userId of the new organization owner
     */
    public void setNewOwnerId(String newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    /**
     * @return the userId of the new organization owner
     */
    public String getNewOwnerId() {
        return newOwnerId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "OwnershipTransferData [previousOwnerId=" + previousOwnerId + ", newOwnerId="
                + newOwnerId + "]";
    }
}