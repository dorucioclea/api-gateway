package com.t1t.apim.beans.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainCorrelationRequest {

    private String serviceId;
    private String domain;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "DomainCorrelationRequest{" +
                "serviceId='" + serviceId + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}