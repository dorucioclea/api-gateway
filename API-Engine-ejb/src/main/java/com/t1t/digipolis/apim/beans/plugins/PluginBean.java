package com.t1t.digipolis.apim.beans.plugins;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Models a single plugin configured by an admin.
 *
 */
@Entity
@Table(name = "plugins", uniqueConstraints = { @UniqueConstraint(columnNames = { "group_id", "artifact_id" }) })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PluginBean implements Serializable {

    private static final long serialVersionUID = 2932636903455749308L;

    @Id @GeneratedValue
    private Long id;
    @Column(name = "group_id", updatable=false, nullable=false)
    private String groupId;
    @Column(name = "artifact_id", updatable=false, nullable=false)
    private String artifactId;
    @Column(updatable=false, nullable=false)
    private String version;
    @Column(updatable=false, nullable=true)
    private String classifier;
    @Column(updatable=false, nullable=true)
    private String type;

    @Column(nullable=false)
    private String name;
    @Column(updatable=true, nullable=true, length=512)
    private String description;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;

    /**
     * Constructor.
     */
    public PluginBean() {
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
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
        return "PluginBean [id=" + id + ", groupId=" + groupId + ", artifactId=" + artifactId + ", version="
                + version + ", classifier=" + classifier + ", type=" + type + ", name=" + name
                + ", description=" + description + ", createdBy=" + createdBy + ", createdOn=" + createdOn
                + "]";
    }

}
