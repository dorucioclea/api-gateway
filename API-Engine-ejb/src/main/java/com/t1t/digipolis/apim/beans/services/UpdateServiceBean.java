package com.t1t.digipolis.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.digipolis.apim.beans.brandings.ServiceBrandingBean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Bean used when updating a service.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateServiceBean implements Serializable {

    private static final long serialVersionUID = 8811488441452291116L;

    private String name;
    private String description;
    private String base64logo;
    private Set<String> categories;
    private Boolean admin;
    private Set<ServiceBrandingBean> brandings;

    /**
     * Constructor.
     */
    public UpdateServiceBean() {
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
        return base64logo;
    }

    /**
     * @param base64logo the base64-encoded logo to set
     */
    public void setBase64logo(String base64logo) {
        this.base64logo = base64logo;
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
     * @return the service brandings
     */
    public Set<ServiceBrandingBean> getBrandings() {
        return brandings;
    }

    /**
     * @param brandings the service brandings to set
     */
    public void setBrandings(Set<ServiceBrandingBean> brandings) {
        this.brandings = brandings;
    }

    @Override
    public String toString() {
        return "UpdateServiceBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", base64logo='" + base64logo + '\'' +
                ", categories=" + categories +
                ", admin=" + admin +
                '}';
    }
}
