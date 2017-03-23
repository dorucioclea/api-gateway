package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.visibility.VisibilityBean;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Models a single version of a service "impl".  Every service in
 * APIEngine has basic meta-data stored in {@link ServiceBean}.  All
 * other specifics of the service, such as endpoint information
 * and configured policies are associated with a particular version
 * of that Service.  This class represents that version.
 *
 */
@Entity
@Table(name = "service_versions", uniqueConstraints = { @UniqueConstraint(columnNames = { "service_id", "service_org_id", "version" }) })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceVersionBean implements Serializable {

    private static final long serialVersionUID = -2218697175049442690L;
    //key for endpoint properties
    public static final String PROP_PATH = "service_path";

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="service_id", referencedColumnName="id"),
        @JoinColumn(name="service_org_id", referencedColumnName="organization_id")
    })
    private ServiceBean service;
    @Column(updatable=true, nullable=false)
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    private String endpoint;
    @Column(name = "endpoint_type")
    @Enumerated(EnumType.STRING)
    private EndpointType endpointType;
    @ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="endpoint_properties", joinColumns=@JoinColumn(name="service_version_id"))
    private Map<String, String> endpointProperties = new HashMap<>();
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="svc_gateways", joinColumns=@JoinColumn(name="service_version_id"))
    private Set<ServiceGatewayBean> gateways;
    @Column(name = "public_service", updatable=true, nullable=false)
    private boolean publicService;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="svc_plans", joinColumns=@JoinColumn(name="service_version_id"))
    private Set<ServicePlanBean> plans;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="svc_visibility", joinColumns=@JoinColumn(name="service_version_id"))
    private Set<VisibilityBean> visibility;
    @Column(updatable=false)
    private String version;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Column(name = "modified_by", updatable=true, nullable=false)
    private String modifiedBy;
    @Column(name = "modified_on", updatable=true, nullable=false)
    private Date modifiedOn;
    @Column(name = "published_on")
    private Date publishedOn;
    @Column(name = "retired_on")
    private Date retiredOn;
    @Column(name = "deprecated_on")
    private Date deprecatedOn;
    @Column(name = "definition_type")
    @Enumerated(EnumType.STRING)
    private ServiceDefinitionType definitionType;
    @Column(name = "provision_key")
    private String provisionKey;
    @Column(name = "onlinedoc")
    private String onlinedoc;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="oauth_scopes")
    @MapKeyColumn(name="oauth_scopes")
    @Column(name="oauth_scopes_desc")
    private Map<String,String> oauthScopes;
    @Column(name = "auto_accept_contracts")
    private Boolean autoAcceptContracts;
    @Lob
    @Column(name="readme")
    @Type(type = "org.hibernate.type.TextType")
    private String readme;
    @Column(name = "terms_agreement_required")
    private Boolean termsAgreementRequired;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the service
     */
    public ServiceBean getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(ServiceBean service) {
        this.service = service;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the status
     */
    public ServiceStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ServiceStatus status) {
        this.status = status;
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
     * @return the publishedOn
     */
    public Date getPublishedOn() {
        return publishedOn;
    }

    /**
     * @param publishedOn the publishedOn to set
     */
    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    /**
     * @return the retiredOn
     */
    public Date getRetiredOn() {
        return retiredOn;
    }

    /**
     * @param retiredOn the retiredOn to set
     */
    public void setRetiredOn(Date retiredOn) {
        this.retiredOn = retiredOn;
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
     * @param plan the plan
     */
    public void addPlan(ServicePlanBean plan) {
        this.plans.add(plan);
    }

    /**
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the publicService
     */
    public boolean isPublicService() {
        return publicService;
    }

    /**
     * @param publicService the publicService to set
     */
    public void setPublicService(boolean publicService) {
        this.publicService = publicService;
    }

    /**
     * @return the definitionType
     */
    public ServiceDefinitionType getDefinitionType() {
        return definitionType;
    }

    /**
     * @param definitionType the definitionType to set
     */
    public void setDefinitionType(ServiceDefinitionType definitionType) {
        this.definitionType = definitionType;
    }

    public Set<VisibilityBean> getVisibility() {
        return visibility;
    }

    public void setVisibility(Set<VisibilityBean> visibility) {
        this.visibility = visibility;
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

    public String getProvisionKey() {
        return provisionKey;
    }

    public void setProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
    }

    public Map<String, String> getOauthScopes() {
        return oauthScopes;
    }

    public void setOauthScopes(Map<String, String> oauthScopes) {
        this.oauthScopes = oauthScopes;
    }

    public String getOnlinedoc() {
        return onlinedoc;
    }

    public void setOnlinedoc(String onlinedoc) {
        this.onlinedoc = onlinedoc;
    }

    public Date getDeprecatedOn() {
        return deprecatedOn;
    }

    public void setDeprecatedOn(Date deprecatedOn) {
        this.deprecatedOn = deprecatedOn;
    }

    public Boolean getAutoAcceptContracts() {
        return autoAcceptContracts;
    }

    public void setAutoAcceptContracts(Boolean autoAcceptContracts) {
        this.autoAcceptContracts = autoAcceptContracts;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    public Boolean getTermsAgreementRequired() {
        return termsAgreementRequired;
    }

    public void setTermsAgreementRequired(Boolean termsAgreementRequired) {
        this.termsAgreementRequired = termsAgreementRequired;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        ServiceVersionBean other = (ServiceVersionBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServiceVersionBean{" +
                "id=" + id +
                ", service=" + service +
                ", status=" + status +
                ", endpoint='" + endpoint + '\'' +
                ", endpointType=" + endpointType +
                ", endpointProperties=" + endpointProperties +
                ", gateways=" + gateways +
                ", publicService=" + publicService +
                ", plans=" + plans +
                ", visibility=" + visibility +
                ", version='" + version + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", publishedOn=" + publishedOn +
                ", retiredOn=" + retiredOn +
                ", deprecatedOn=" + deprecatedOn +
                ", definitionType=" + definitionType +
                ", provisionKey='" + provisionKey + '\'' +
                ", onlinedoc='" + onlinedoc + '\'' +
                ", oauthScopes=" + oauthScopes +
                ", autoAcceptContracts=" + autoAcceptContracts +
                ", readme='" + readme + '\'' +
                ", termsAgreementRequired=" + termsAgreementRequired +
                '}';
    }
}
