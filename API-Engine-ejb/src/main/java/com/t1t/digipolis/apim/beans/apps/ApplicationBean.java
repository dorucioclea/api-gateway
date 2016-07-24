package com.t1t.digipolis.apim.beans.apps;

import com.t1t.digipolis.apim.beans.orgs.OrganizationBasedCompositeId;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * Models an application.
 *
 */
@Entity
@Table(name = "applications")
@IdClass(OrganizationBasedCompositeId.class)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ApplicationBean implements Comparable<ApplicationBean>,Serializable {

    private static final long serialVersionUID = -197129444021040365L;

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="organization_id", referencedColumnName="id")
    })
    private OrganizationBean organization;
    @Id
    @Column(nullable=false)
    private String id;
    @Column(nullable=false)
    private String name;
    @Column(updatable=true, nullable=true, length=512)
    private String description;
    private String context;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Column(name = "logo")
    @Lob
    @Basic(fetch=FetchType.EAGER)
    private byte[] base64logo;

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
     * @return the organization
     */
    public OrganizationBean getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(OrganizationBean organization) {
        this.organization = organization;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBase64logo() {
        return Base64.encodeBase64String(base64logo);
    }

    public void setBase64logo(String base64logo) {
        this.base64logo = Base64.decodeBase64(base64logo.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationBean)) return false;

        ApplicationBean that = (ApplicationBean) o;

        if (!organization.equals(that.organization)) return false;
        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        int result = organization.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ApplicationBean{" +
                "organization=" + organization +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", context='" + context + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    @Override
    public int compareTo(ApplicationBean o) {
        return this.getId().compareTo(o.getId());
    }
}
