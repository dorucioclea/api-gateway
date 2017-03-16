package com.t1t.apim.gateway.dto;

import java.util.Map;

/**
 * Represents common elements of {@link ServiceRequest} and {@link ServiceResponse}.
 *
 */
public interface IServiceObject {

    /**
     * @return the headers
     */
    Map<String, String> getHeaders();

    /**
     * @param headers the headers to set
     */
    void setHeaders(Map<String, String> headers);
}
