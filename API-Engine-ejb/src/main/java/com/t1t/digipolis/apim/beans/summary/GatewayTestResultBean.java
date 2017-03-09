package com.t1t.digipolis.apim.beans.summary;

import java.io.Serializable;

/**
 * The result of testing a gateway configuration.
 *
 */
public class GatewayTestResultBean implements Serializable {

    private static final long serialVersionUID = -7750899323194993811L;

    private boolean success;
    private String detail;

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "GatewayTestResultBean [success=" + success + ", detail=" + detail + "]";
    }
}
