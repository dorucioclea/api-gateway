package com.t1t.digipolis.apim.beans.services;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Bean used when creating a service.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewServiceBean implements Serializable {

    private static final long serialVersionUID = 8811488441452291116L;

    private String name;
    private String description;
    private String initialVersion;
    private String basepath;
    private Set<String> categories;
    private String base64logo;

    /**
     * Constructor.
     */
    public NewServiceBean() {
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
     * @return the initialVersion
     */
    public String getInitialVersion() {
        return initialVersion;
    }

    /**
     * @param initialVersion the initialVersion to set
     */
    public void setInitialVersion(String initialVersion) {
        this.initialVersion = initialVersion;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
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
        return "NewServiceBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", initialVersion='" + initialVersion + '\'' +
                ", basepath='" + basepath + '\'' +
                ", categories=" + categories +
                '}';
    }
}
