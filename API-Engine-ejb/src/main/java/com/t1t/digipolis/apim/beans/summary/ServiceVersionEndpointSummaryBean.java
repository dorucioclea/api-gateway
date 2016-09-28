package com.t1t.digipolis.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Set;

/**
 * Returns managed endpoint information.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceVersionEndpointSummaryBean implements Serializable {

    private static final long serialVersionUID = -4655383228161917800L;

    private String managedEndpoint;
    private String oauth2AuthorizeEndpoint;
    private String oauth2TokenEndpoint;
    private Set<ServiceVersionEndpointSummaryBean> brandingEndpoints;

    /**
     * Constructor.
     */
    public ServiceVersionEndpointSummaryBean() {
    }

    public String getOauth2AuthorizeEndpoint() {
        return oauth2AuthorizeEndpoint;
    }

    public void setOauth2AuthorizeEndpoint(String oauth2AuthorizeEndpoint) {
        this.oauth2AuthorizeEndpoint = oauth2AuthorizeEndpoint;
    }

    public String getOauth2TokenEndpoint() {
        return oauth2TokenEndpoint;
    }

    public void setOauth2TokenEndpoint(String oauth2TokenEndpoint) {
        this.oauth2TokenEndpoint = oauth2TokenEndpoint;
    }

    public Set<ServiceVersionEndpointSummaryBean> getBrandingEndpoints() {
        return brandingEndpoints;
    }

    public void setBrandingEndpoints(Set<ServiceVersionEndpointSummaryBean> brandingEndpoints) {
        this.brandingEndpoints = brandingEndpoints;
    }

    public ServiceVersionEndpointSummaryBean withManagedEndpoint(String managedEndpoint) {
        setManagedEndpoint(managedEndpoint);
        return this;
    }

    public ServiceVersionEndpointSummaryBean withOauth2AuthorizeEndpoint(String oauth2AuthorizeEndpoint) {
        setManagedEndpoint(managedEndpoint);
        return this;
    }

    public ServiceVersionEndpointSummaryBean withOauth2TokenEndpoint(String oauth2TokenEndpoint) {
        setManagedEndpoint(managedEndpoint);
        return this;
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

    @Override
    public String toString() {
        return "ServiceVersionEndpointSummaryBean{" +
                "managedEndpoint='" + managedEndpoint + '\'' +
                ", oauth2AuthorizeEndpoint='" + oauth2AuthorizeEndpoint + '\'' +
                ", oauth2TokenEndpoint='" + oauth2TokenEndpoint + '\'' +
                ", brandingEndpoints=" + brandingEndpoints +
                '}';
    }
}
