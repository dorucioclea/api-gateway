package com.t1t.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateServiceBean that = (UpdateServiceBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (base64logo != null ? !base64logo.equals(that.base64logo) : that.base64logo != null) return false;
        if (categories != null ? !categories.equals(that.categories) : that.categories != null) return false;
        return admin != null ? admin.equals(that.admin) : that.admin == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (base64logo != null ? base64logo.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        return result;
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
