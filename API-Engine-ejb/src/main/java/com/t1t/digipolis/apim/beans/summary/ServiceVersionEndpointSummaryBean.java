package com.t1t.digipolis.apim.beans.summary;

import java.io.Serializable;

/**
 * Returns managed endpoint information.
 *
 */
public class ServiceVersionEndpointSummaryBean implements Serializable {

    private static final long serialVersionUID = -4655383228161917800L;

    private String managedEndpoint;

    /**
     * Constructor.
     */
    public ServiceVersionEndpointSummaryBean() {
    }

    /**
     * @return the managedEndpoint
     */
    public String getManagedEndpoint() {
        return managedEndpoint;
    }

    /**
     * @param managedEndpoint the managedEndpoint to set
     */
    public void setManagedEndpoint(String managedEndpoint) {
        this.managedEndpoint = managedEndpoint;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ServiceVersionEndpointSummaryBean [managedEndpoint=" + managedEndpoint + "]";
    }

}
