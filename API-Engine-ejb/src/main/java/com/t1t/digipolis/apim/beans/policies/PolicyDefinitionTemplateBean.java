package com.t1t.digipolis.apim.beans.policies;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Models a policy definition description template.  A policy definition
 * template is an MVEL template used to dynamically generate a description
 * of a policy instance.  This allows policies to have different descriptions
 * depending on their configuration information.
 *
 */
@Embeddable
public class PolicyDefinitionTemplateBean {

    private String language;
    @Column(length=2048)
    private String template;

    /**
     * Constructor.
     */
    public PolicyDefinitionTemplateBean() {
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "PolicyDefinitionTemplateBean [language=" + language + ", template=" + template + "]";
    }

}
