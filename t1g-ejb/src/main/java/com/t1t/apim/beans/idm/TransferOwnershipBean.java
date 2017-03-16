package com.t1t.apim.beans.idm;

import java.io.Serializable;

/**
 * Bean used to transfer organization ownership from one organization member
 * to another.
 *
 */
public class TransferOwnershipBean implements Serializable {

    private static final long serialVersionUID = -5523271481693998667L;

    private String currentOwnerId;
    private String newOwnerId;

    /**
     * Constructor.
     */
    public TransferOwnershipBean() {

    }

    /**
     * @return the userId of the current organization owner
     */
    public String getCurrentOwnerId() {
        return currentOwnerId;
    }

    /**
     * @param currentOwnerId the userId of the current organization owner
     */
    public void setCurrentOwnerId(String currentOwnerId) {
        this.currentOwnerId = currentOwnerId;
    }

    /**
     * @return the userId of the new organization owner
     */
    public String getNewOwnerId() {
        return newOwnerId;
    }

    /**
     * @param newOwnerId the userId of the new organization owner
     */
    public void setNewOwnerId(String newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    /* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "TransferOwnershipBean [currentOwnerId=" + currentOwnerId + ", newOwnerId=" + newOwnerId + "]";
    }
}