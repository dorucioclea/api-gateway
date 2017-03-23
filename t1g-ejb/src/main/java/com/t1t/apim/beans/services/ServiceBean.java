package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.beans.orgs.OrganizationBasedCompositeId;
import com.t1t.apim.beans.orgs.OrganizationBean;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * Models an service.
 *
 */
@Entity
@Table(name = "services")
@IdClass(OrganizationBasedCompositeId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "service_brandings", joinColumns = {@JoinColumn(name = "organization_id", referencedColumnName = "organization_id"), @JoinColumn(name = "service_id", referencedColumnName = "id")}, inverseJoinColumns = @JoinColumn(name = "branding_id", referencedColumnName = "id"))
    private Set<ServiceBrandingBean> brandings;
    @Column(updatable=true, nullable=true, length=512)
    private String description;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Lob
    @Column(name="terms")
    @Type(type = "org.hibernate.type.TextType")
    private String terms;
    @Column(name = "logo")
    @Lob
    @Basic(fetch=FetchType.EAGER)
    private byte[] base64logo;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="followers")
    @Column(name="user_id")
    private Set<String> followers;
    @Column(name = "admin")
    private Boolean admin;

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

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
    }

    /**
     * @return the organization
     */
    public OrganizationBean getOrganization() {
        return organization;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(OrganizationBean organization) {
        this.organization = organization;
    }

    /**
     * @return the base path value
     */
    public String getBasepath() {
        return basepath;
    }

    /**
     * @param basePath the base path to set
     */
    public void setBasepath(String basePath) {
        this.basepath = basePath;
    }

    /**
     * @return the categories
     */
    public Set<String> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    /**
     * @return the base64-encoded logo
     */
    public String getBase64logo() {
        return Base64.encodeBase64String(base64logo);
    }

    /**
     * @param base64logo the base64-encoded logo to set
     */
    public void setBase64logo(String base64logo) {
        this.base64logo = Base64.decodeBase64(base64logo.getBytes());
    }

    /**
     * @return the admin value
     */
    public Boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin value to set
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * @return the service branding domains
     */
    public Set<ServiceBrandingBean> getBrandings() {
        return brandings;
    }

    /**
     * @param brandings the service branding domains to set
     */
    public void setBrandings(Set<ServiceBrandingBean> brandings) {
        this.brandings = brandings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceBean that = (ServiceBean) o;

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
        return "ServiceBean{" +
                "organization=" + organization +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", basepath='" + basepath + '\'' +
                ", categories=" + categories +
                ", brandings=" + brandings +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", terms='" + terms + '\'' +
                ", base64logo=" + Arrays.toString(base64logo) +
                ", followers=" + followers +
                ", admin=" + admin +
                '}';
    }
}
