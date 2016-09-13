package com.t1t.digipolis.apim.beans.plugins;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Models a single plugin when creating.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewPluginBean implements Serializable {

    private static final long serialVersionUID = 3246882829241218365L;

    private String groupId;
    private String artifactId;
    private String version;
    private String classifier;
    private String type;

    private String name;
    private String description;

    /**
     * Constructor.
     */
    public NewPluginBean() {
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
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId the artifactId to set
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the classifier
     */
    public String getClassifier() {
        return classifier;
    }

    /**
     * @param classifier the classifier to set
     */
    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewPluginBean [groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version
                + ", classifier=" + classifier + ", type=" + type + ", name=" + name + ", description="
                + description + "]";
    }

}
