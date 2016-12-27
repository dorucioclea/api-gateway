package com.t1t.digipolis.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Models a single version of a application "impl".  Every application in
 * APIMan has basic meta-data stored in {@link ApplicationBean}.  All
 * other specifics of the application, such as endpoint information
 * and configured policies are associated with a particular version
 * of that Application.  This class represents that version.
 *
 */
@Entity
@Table(name = "application_versions",
       uniqueConstraints = { @UniqueConstraint(columnNames = { "app_id", "app_org_id", "version" }) })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationVersionBean implements Serializable {

    private static final long serialVersionUID = -2218697175049442690L;

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="app_id", referencedColumnName="id"),
        @JoinColumn(name="app_org_id", referencedColumnName="organization_id")
    })
    private ApplicationBean application;
    @Column(updatable=true, nullable=false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Column(updatable=false, nullable=false)
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
    @Column(name = "oauth_client_id")
    private String oAuthClientId;
    @Column(name = "oauth_client_secret")
    private String oauthClientSecret;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="app_oauth_redirect_uris", joinColumns=@JoinColumn(name="application_version_id"))
    @Column(name = "oauth_client_redirect")
    private Set<String> oauthClientRedirects;
    @Column(name = "apikey")
    private String apikey;
    @Column(name = "oauth_credential_id")
    private String oauthCredentialId;
    @Column(name="jwt_key")
    private String jwtKey;
    @Column(name="jwt_secret")
    private String jwtSecret;


    /**
     * Constructor.
     */
    public ApplicationVersionBean() {
    }

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
     * @return the application
     */
    public ApplicationBean getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(ApplicationBean application) {
        this.application = application;
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
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
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
     * @return the OAuth client id
     */
    public String getoAuthClientId() {
        return oAuthClientId;
    }

    /**
     * @param oAuthClientId the OAuth client id to set
     */
    public void setoAuthClientId(String oAuthClientId) {
        this.oAuthClientId = oAuthClientId;
    }

    /**
     * @return the OAuth client secret
     */
    public String getOauthClientSecret() {
        return oauthClientSecret;
    }

    /**
     * @param oauthClientSecret the OAuth client secret to set
     */
    public void setOauthClientSecret(String oauthClientSecret) {
        this.oauthClientSecret = oauthClientSecret;
    }

    /**
     * @return the OAuth client redirects
     */
    public Set<String> getOauthClientRedirects() {
        return oauthClientRedirects;
    }

    /**
     * @param oauthClientRedirect the OAuth client redirects to set
     */
    public void setOauthClientRedirects(Set<String> oauthClientRedirect) {
        this.oauthClientRedirects = oauthClientRedirect;
    }

    /**
     * @return the oauth credential id
     */
    public String getOauthCredentialId() {
        return oauthCredentialId;
    }

    /**
     * @param oauthCredentialId the oauth credential id to set
     */
    public void setOauthCredentialId(String oauthCredentialId) {
        this.oauthCredentialId = oauthCredentialId;
    }

    /**
     * @return the API key
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * @param apikey the API key to set
     */
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getJwtKey() {
        return jwtKey;
    }

    public void setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
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
        ApplicationVersionBean other = (ApplicationVersionBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ApplicationVersionBean{" +
                "id=" + id +
                ", application=" + application +
                ", status=" + status +
                ", version='" + version + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", publishedOn=" + publishedOn +
                ", retiredOn=" + retiredOn +
                ", oAuthClientId='" + oAuthClientId + '\'' +
                ", oauthClientSecret='" + oauthClientSecret + '\'' +
                ", oauthClientRedirects=" + oauthClientRedirects +
                ", apikey='" + apikey + '\'' +
                ", oauthCredentialId='" + oauthCredentialId + '\'' +
                ", jwtKey='" + jwtKey + '\'' +
                ", jwtSecret='" + jwtSecret + '\'' +
                '}';
    }
}
