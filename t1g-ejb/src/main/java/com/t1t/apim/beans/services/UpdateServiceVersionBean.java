package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.visibility.VisibilityBean;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Bean used when updating a version of a service.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateServiceVersionBean implements Serializable {

    private static final long serialVersionUID = 4126848584932708146L;

    private SchemeType upstreamScheme;
    private String upstreamPath;
    private EndpointType endpointType;
    private Map<String, String> endpointProperties;
    private Set<ServiceGatewayBean> gateways;
    private Boolean publicService;
    private String onlinedoc;
    private Set<ServicePlanBean> plans;
    private Set<VisibilityBean> visibility;
    private Boolean autoAcceptContracts;
    private Boolean termsAgreementRequired;
    private String readme;
    private Set<String> hostnames;
    private Long upstreamConnectTimeout;
    private Long upstreamSendTimeout;
    private Long upstreamReadTimeout;

    /**
     * @return the upstream scheme
     */
    public SchemeType getUpstreamScheme() {
        return upstreamScheme;
    }

    /**
     * @param upstreamScheme the upstream scheme to set
     */
    public void setUpstreamScheme(SchemeType upstreamScheme) {
        this.upstreamScheme = upstreamScheme;
    }

    /**
     * @return the upstream path
     */
    public String getUpstreamPath() {
        return upstreamPath;
    }

    /**
     * @param upstreamPath the upstream path to set
     */
    public void setUpstreamPath(String upstreamPath) {
        this.upstreamPath = upstreamPath;
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

    /**
     * @return the online doc
     */
    public String getOnlinedoc() {
        return onlinedoc;
    }

    /**
     * @param onlinedoc the online doc to set
     */
    public void setOnlinedoc(String onlinedoc) {
        this.onlinedoc = onlinedoc;
    }

    /**
     * @return the visibility
     */
    public Set<VisibilityBean> getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibilty to set
     */
    public void setVisibility(Set<VisibilityBean> visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the auto accept contracts values
     */
    public Boolean getAutoAcceptContracts() {
        return autoAcceptContracts;
    }

    /**
     * @param autoAcceptContracts the auto accept contracts value to set
     */
    public void setAutoAcceptContracts(Boolean autoAcceptContracts) {
        this.autoAcceptContracts = autoAcceptContracts;
    }

    /**
     * @return the terms agreement required value
     */
    public Boolean getTermsAgreementRequired() {
        return termsAgreementRequired;
    }

    /**
     * @param termsAgreementRequired the terms agreement required value to set
     */
    public void setTermsAgreementRequired(Boolean termsAgreementRequired) {
        this.termsAgreementRequired = termsAgreementRequired;
    }

    /**
     * @return the read me
     */
    public String getReadme() {
        return readme;
    }

    /**
     * @param readme the read me to set
     */
    public void setReadme(String readme) {
        this.readme = readme;
    }

    /**
     * @return the hostnames
     */
    public Set<String> getHostnames() {
        return hostnames;
    }

    /**
     * @param hostnames the hostnames to set
     */
    public void setHostnames(Set<String> hostnames) {
        this.hostnames = hostnames;
    }

    /**
     * @return the upstream connect timeout
     */
    public Long getUpstreamConnectTimeout() {
        return upstreamConnectTimeout;
    }

    /**
     * @param upstreamConnectTimeout the upstream connect timeout to set
     */
    public void setUpstreamConnectTimeout(Long upstreamConnectTimeout) {
        this.upstreamConnectTimeout = upstreamConnectTimeout;
    }

    /**
     * @return the upstream send timeout
     */
    public Long getUpstreamSendTimeout() {
        return upstreamSendTimeout;
    }

    /**
     * @param upstreamConnectTimeout the upstream send timeout to set
     */
    public void setUpstreamSendTimeout(Long upstreamSendTimeout) {
        this.upstreamSendTimeout = upstreamSendTimeout;
    }

    /**
     * @return the upstream read timeout
     */
    public Long getUpstreamReadTimeout() {
        return upstreamReadTimeout;
    }

    /**
     * @param upstreamConnectTimeout the upstream read timeout to set
     */
    public void setUpstreamReadTimeout(Long upstreamReadTimeout) {
        this.upstreamReadTimeout = upstreamReadTimeout;
    }

    @Override
    public String toString() {
        return "UpdateServiceVersionBean{" +
                "upstreamScheme=" + upstreamScheme +
                ", upstreamPath='" + upstreamPath + '\'' +
                ", endpointType=" + endpointType +
                ", endpointProperties=" + endpointProperties +
                ", gateways=" + gateways +
                ", publicService=" + publicService +
                ", onlinedoc='" + onlinedoc + '\'' +
                ", plans=" + plans +
                ", visibility=" + visibility +
                ", autoAcceptContracts=" + autoAcceptContracts +
                ", termsAgreementRequired=" + termsAgreementRequired +
                ", readme='" + readme + '\'' +
                ", hostnames=" + hostnames +
                ", upstreamConnectTimeout=" + upstreamConnectTimeout +
                ", upstreamSendTimeout=" + upstreamSendTimeout +
                ", upstreamReadTimeout=" + upstreamReadTimeout +
                '}';
    }
}
