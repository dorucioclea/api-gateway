package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Returns managed endpoint information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceVersionEndpointSummaryBean implements Serializable {

    private static final long serialVersionUID = -4655383228161917800L;

    private List<String> managedEndpoints;
    private List<String> oauth2AuthorizeEndpoints;
    private List<String> oauth2TokenEndpoints;
    private Set<ServiceVersionEndpointSummaryBean> brandingEndpoints;

    public List<String> getManagedEndpoints() {
        return managedEndpoints;
    }

    public void setManagedEndpoints(List<String> managedEndpoints) {
        this.managedEndpoints = managedEndpoints;
    }

    public List<String> getOauth2AuthorizeEndpoints() {
        return oauth2AuthorizeEndpoints;
    }

    public void setOauth2AuthorizeEndpoints(List<String> oauth2AuthorizeEndpoints) {
        this.oauth2AuthorizeEndpoints = oauth2AuthorizeEndpoints;
    }

    public List<String> getOauth2TokenEndpoints() {
        return oauth2TokenEndpoints;
    }

    public void setOauth2TokenEndpoints(List<String> oauth2TokenEndpoints) {
        this.oauth2TokenEndpoints = oauth2TokenEndpoints;
    }

    public Set<ServiceVersionEndpointSummaryBean> getBrandingEndpoints() {
        return brandingEndpoints;
    }

    public void setBrandingEndpoints(Set<ServiceVersionEndpointSummaryBean> brandingEndpoints) {
        this.brandingEndpoints = brandingEndpoints;
    }

    public ServiceVersionEndpointSummaryBean withManagedEndpoints(List<String> managedEndpoints) {
        setManagedEndpoints(managedEndpoints);
        return this;
    }


}
