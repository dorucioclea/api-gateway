package com.t1t.apim.beans.services;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Models a gateway that a service should be published to.
 */
@Embeddable
public class ServiceGatewayBean implements Serializable {

    private static final long serialVersionUID = 3128363408009800282L;

    @Column(name = "gateway_id", nullable = false)
    private String gatewayId;

    /**
     * @return the gatewayId
     */
    public String getGatewayId() {
        return gatewayId;
    }

    /**
     * @param gatewayId the gatewayId to set
     */
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gatewayId == null) ? 0 : gatewayId.hashCode());
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
        ServiceGatewayBean other = (ServiceGatewayBean) obj;
        if (gatewayId == null) {
            if (other.gatewayId != null)
                return false;
        } else if (!gatewayId.equals(other.gatewayId))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ServiceGatewayBean [gatewayId=" + gatewayId + "]";
    }

}
