package com.t1t.digipolis.apim.beans.policies;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * The bean/model used when updating a new policy definition.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePolicyDefinitionBean implements Serializable {

    private static final long serialVersionUID = 350049376316732992L;

    private String name;
    private String description;
    private String icon;

    /**
     * Constructor.
     */
    public UpdatePolicyDefinitionBean() {
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
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UpdatePolicyDefinitionBean [name=" + name + ", description=" + description + ", icon=" + icon
                + "]";
    }

}
