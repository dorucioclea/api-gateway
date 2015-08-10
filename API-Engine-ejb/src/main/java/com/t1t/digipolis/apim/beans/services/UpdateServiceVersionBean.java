package com.t1t.digipolis.apim.beans.services;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Bean used when updating a version of a service.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpdateServiceVersionBean implements Serializable {

    private static final long serialVersionUID = 4126848584932708146L;

    private String endpoint;
    private EndpointType endpointType;
    private Map<String, String> endpointProperties;
    private Set<ServiceGatewayBean> gateways;
    private Boolean publicService;
    private Set<ServicePlanBean> plans;

    /**
     * Constructor.
     */
    public UpdateServiceVersionBean() {
    }

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

    /**
     * @return the endpointType
     */
    public EndpointType getEndpointType() {
        return endpointType;
    }

    /**
     * @param endpointType the endpointType to set
     */
    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    /**
     * @return the gateways
     */
    public Set<ServiceGatewayBean> getGateways() {
        return gateways;
    }

    /**
     * @param gateways the gateways to set
     */
    public void setGateways(Set<ServiceGatewayBean> gateways) {
        this.gateways = gateways;
    }

    /**
     * @return the publicService
     */
    public Boolean getPublicService() {
        return publicService;
    }

    /**
     * @param publicService the publicService to set
     */
    public void setPublicService(Boolean publicService) {
        this.publicService = publicService;
    }

    /**
     * @return the plans
     */
    public Set<ServicePlanBean> getPlans() {
        return plans;
    }

    /**
     * @param plans the plans to set
     */
    public void setPlans(Set<ServicePlanBean> plans) {
        this.plans = plans;
    }

    /**
     * @return the endpointProperties
     */
    public Map<String, String> getEndpointProperties() {
        return endpointProperties;
    }

    /**
     * @param endpointProperties the endpointProperties to set
     */
    public void setEndpointProperties(Map<String, String> endpointProperties) {
        this.endpointProperties = endpointProperties;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        final int maxLen = 10;
        return "UpdateServiceVersionBean [endpoint=" + endpoint + ", endpointType=" + endpointType
                + ", endpointProperties=" + toString(this.endpointProperties.entrySet(), maxLen)
                + ", gateways=" + (gateways != null ? toString(gateways, maxLen) : null) + ", publicService="
                + publicService + ", plans=" + (plans != null ? toString(plans, maxLen) : null) + "]";
    }

    @SuppressWarnings("nls")
    private String toString(Collection<?> collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
            if (i > 0)
                builder.append(", ");
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

}
