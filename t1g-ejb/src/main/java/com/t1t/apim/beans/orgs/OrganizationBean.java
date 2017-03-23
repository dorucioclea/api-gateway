package com.t1t.apim.beans.orgs;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * An APIMan Organization.  This is an important top level entity in the APIMan
 * data model.  It contains the Services, Plans, etc.
 *
 */
@Entity
@Table(name = "organizations")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationBean implements Serializable {

    private static final long serialVersionUID = -506427154633682906L;

    @Id
    @Column(nullable=false)
    private String id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String context;
    @Column(updatable=true, nullable=true, length=512)
    private String description;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Column(name = "modified_by", updatable=true, nullable=false)
    private String modifiedBy;
    @Column(name = "modified_on", updatable=true, nullable=false)
    private Date modifiedOn;
    @Column(name = "friendly_name", updatable = true)
    private String friendlyName;
    @Column(name = "private")
    private boolean organizationPrivate;
    @Column(name = "mail_provider_id")
    private Long mailProviderId;
    @Column(name = "keystore_kid")
    private String keystoreKid;

    /**
     * Constructor.
     */
    public OrganizationBean() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the user-friendly name
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * @param friendlyName the user-friendly name to set
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * @return the organization privacy boolean
     */
    public boolean isOrganizationPrivate() {
        return organizationPrivate;
    }

    /**
     * @param organizationPrivate the organization privacy to set
     */
    public void setOrganizationPrivate(boolean organizationPrivate) {
        this.organizationPrivate = organizationPrivate;
    }

    /**
     * @return the organization context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the organization context to set
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return the mail provider ID
     */
    public Long getMailProviderId() {
        return mailProviderId;
    }

    /**
     * @param mailProviderId the mail provider ID to set
     */
    public void setMailProviderId(Long mailProviderId) {
        this.mailProviderId = mailProviderId;
    }

    /**
     * @return the keystore ID
     */
    public String getKeystoreKid() {
        return keystoreKid;
    }

    /**
     * @param keystoreId the keystore ID to set
     */
    public void setKeystoreKid(String keystoreId) {
        this.keystoreKid = keystoreId;
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
        OrganizationBean other = (OrganizationBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrganizationBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", context='" + context + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", friendlyName='" + friendlyName + '\'' +
                ", organizationPrivate=" + organizationPrivate +
                ", mailProviderId=" + mailProviderId +
                ", keystoreKid=" + keystoreKid +
                '}';
    }
}