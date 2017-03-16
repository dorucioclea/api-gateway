package com.t1t.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when updating an application.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateApplicationBean implements Serializable {

    private static final long serialVersionUID = 5549391329361400489L;

    private String description;
    private String base64logo;
    private String email;

    /**
     * Constructor.
     */
    public UpdateApplicationBean() {
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
        return "UpdateApplicationBean{" +
                "description='" + description + '\'' +
                ", base64logo='" + base64logo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
