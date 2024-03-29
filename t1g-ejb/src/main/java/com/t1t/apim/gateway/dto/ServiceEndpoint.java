package com.t1t.apim.gateway.dto;

import java.io.Serializable;

/**
 * Service endpoint.
 */
public class ServiceEndpoint implements Serializable {

    private static final long serialVersionUID = -7892423118281500532L;

    private String endpoint;

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}
