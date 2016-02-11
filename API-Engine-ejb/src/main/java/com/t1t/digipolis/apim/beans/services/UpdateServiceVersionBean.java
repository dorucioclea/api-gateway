package com.t1t.digipolis.apim.beans.services;

import com.t1t.digipolis.apim.beans.visibility.VisibilityBean;
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
    private String onlinedoc;
    private Set<ServicePlanBean> plans;
    private Set<VisibilityBean> visibility;

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

    public String getOnlinedoc() {
        return onlinedoc;
    }

    public void setOnlinedoc(String onlinedoc) {
        this.onlinedoc = onlinedoc;
    }

    public Set<VisibilityBean> getVisibility() {
        return visibility;
    }

    public void setVisibility(Set<VisibilityBean> visibility) {
        this.visibility = visibility;
    }

    /**
     * @param endpointProperties the endpointProperties to set
     */
    public void setEndpointProperties(Map<String, String> endpointProperties) {
        this.endpointProperties = endpointProperties;
    }

    @Override
    public String toString() {
        return "UpdateServiceVersionBean{" +
                "endpoint='" + endpoint + '\'' +
                ", endpointType=" + endpointType +
                ", endpointProperties=" + endpointProperties +
                ", gateways=" + gateways +
                ", publicService=" + publicService +
                ", onlinedoc='" + onlinedoc + '\'' +
                ", plans=" + plans +
                ", visibility=" + visibility +
                '}';
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
