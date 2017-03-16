package com.t1t.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when creating an application.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewApplicationBean implements Serializable {

    private static final long serialVersionUID = 5833699118241379535L;

    private String name;
    private String description;
    private String initialVersion;
    private String base64logo;
    private String email;

    /**
     * Constructor.
     */
    public NewApplicationBean() {
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

    /**
     * @return the base 64 logo
     */
    public String getBase64logo() {
        return base64logo;
    }

    /**
     * @param base64logo the base 64 logo to set
     */
    public void setBase64logo(String base64logo) {
        this.base64logo = base64logo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NewApplicationBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", initialVersion='" + initialVersion + '\'' +
                ", base64logo='" + base64logo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
