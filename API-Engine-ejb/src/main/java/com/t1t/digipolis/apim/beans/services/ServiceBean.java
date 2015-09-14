package com.t1t.digipolis.apim.beans.services;

import com.t1t.digipolis.apim.beans.orgs.OrganizationBasedCompositeId;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Models an service.
 *
 */
@Entity
@Table(name = "services")
@IdClass(OrganizationBasedCompositeId.class)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ServiceBean implements Serializable {

    private static final long serialVersionUID = 1526742536153467539L;

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
    @Column(nullable=false)
    private String basepath;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="categories")
    @Column(name="category")
    private Set<String> categories;
    @Column(updatable=true, nullable=true, length=512)
    private String description;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Column(name = "logo")
    @Lob
    private String base64logo;

    /**
     * Constructor.
     */
    public ServiceBean() {
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

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basePath) {
        this.basepath = basePath;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getBase64logo() {
        return base64logo;
    }

    public void setBase64logo(String base64logo) {
        this.base64logo = base64logo;
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "organization=" + organization +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", basepath='" + basepath + '\'' +
                ", categories=" + categories +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
