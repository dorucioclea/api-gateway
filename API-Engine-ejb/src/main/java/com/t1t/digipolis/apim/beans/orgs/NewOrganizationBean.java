package com.t1t.digipolis.apim.beans.orgs;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Bean used when creating a new organization.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewOrganizationBean implements Serializable {

    private static final long serialVersionUID = 6967624347251134433L;

    private String name;
    private String description;

    /**
     * Constructor.
     */
    public NewOrganizationBean() {
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

}
