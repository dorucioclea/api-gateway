package com.t1t.apim.beans.policies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.summary.PolicySummaryBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models the list of policies that would get applied if a service were invoked
 * via a particular plan.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyChainBean implements Serializable {

    private static final long serialVersionUID = -497197512733345793L;

    private List<PolicySummaryBean> policies = new ArrayList<>();

    /**
     * Constructor.
     */
    public PolicyChainBean() {
    }

    /**
     * @return the policies
     */
    public List<PolicySummaryBean> getPolicies() {
        return policies;
    }

    /**
     * @param policies the policies to set
     */
    public void setPolicies(List<PolicySummaryBean> policies) {
        this.policies = policies;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        final int maxLen = 10;
        return "PolicyChainBean [policies="
                + (policies != null ? policies.subList(0, Math.min(policies.size(), maxLen)) : null) + "]";
    }

}
