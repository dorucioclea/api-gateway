package com.t1t.digipolis.apim.beans.services;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Bean used when updating a service.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UpdateServiceBean implements Serializable {

    private static final long serialVersionUID = 8811488441452291116L;

    private String description;
    private String base64logo;
    private Set<String> categories;

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
        return "UpdateServiceBean{" +
                "description='" + description + '\'' +
                ", categories=" + categories +
                '}';
    }
}
