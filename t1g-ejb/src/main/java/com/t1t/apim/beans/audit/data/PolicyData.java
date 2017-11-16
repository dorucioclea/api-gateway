package com.t1t.apim.beans.audit.data;

import java.io.Serializable;

/**
 * The data saved along with the audit entry when a policy is added to
 * an entity.
 */
public class PolicyData implements Serializable {

    private static final long serialVersionUID = 2160450121494494714L;

    private String policyDefId;
    private EntityUpdatedData data;

    /**
     * @return the policyDefId
     */
    public String getPolicyDefId() {
        return policyDefId;
    }

    /**
     * @param policyDefId the policyDefId to set
     */
    public void setPolicyDefId(String policyDefId) {
        this.policyDefId = policyDefId;
    }

    /**
     * @return the changed data
     */
    public EntityUpdatedData getData() {
        return data;
    }

    /**
     * @param data the changed data to set
     */
    public void setData(EntityUpdatedData data) {
        this.data = data;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "PolicyData{" +
                "policyDefId='" + policyDefId + '\'' +
                ", data=" + data +
                '}';
    }
}
