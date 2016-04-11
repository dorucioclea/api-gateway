package com.t1t.digipolis.apim.beans.gateways;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Models a single gateway configured by an admin.  When publishing services,
 * the user must specific which Gateway to publish to.
 */
@Entity
@Table(name = "gateways")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GatewayBean implements Serializable {

    private static final long serialVersionUID = 388316225715740602L;

    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String endpoint;
    @Column(updatable = true, nullable = true, length = 512)
    private String description;
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;
    @Column(name = "created_on", updatable = false, nullable = false)
    private Date createdOn;
    @Column(name = "modified_by", updatable = true, nullable = false)
    private String modifiedBy;
    @Column(name = "modified_on", updatable = true, nullable = false)
    private Date modifiedOn;
    @Column(name = "oauth_token", nullable = true)
    private String oauthTokenPath;
    @Column(name = "oauth_authorize", nullable = true)
    private String oauthAuthPath;
    @Column(name = "oauth_context", nullable = true)
    private String oauthContext;
    @Column(name = "jwt_exp_time", nullable = true)
    private Integer JWTExpTime;

    @Column(updatable = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private GatewayType type;
    @Lob
    @Column(updatable = true, nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String configuration;

    /**
     * Constructor.
     */
    public GatewayBean() {
    }

/*    @PrePersist @PreUpdate
    protected void encryptData() {
        // Encrypt the endpoint properties.
        configuration = AesEncrypter.encrypt(configuration);
    }

    @PostPersist @PostUpdate @PostLoad
    protected void decryptData() {
        // Encrypt the endpoint properties.
        configuration = AesEncrypter.decrypt(configuration);
    }*/

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the type
     */
    public GatewayType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(GatewayType type) {
        this.type = type;
    }

    /**
     * @return the configuration
     */
    public String getConfiguration() {
        return configuration;
    }

    public String getOauthTokenPath() {
        return oauthTokenPath;
    }

    public void setOauthTokenPath(String oauthTokenPath) {
        this.oauthTokenPath = oauthTokenPath;
    }

    public String getOauthAuthPath() {
        return oauthAuthPath;
    }

    public void setOauthAuthPath(String oauthAuthPath) {
        this.oauthAuthPath = oauthAuthPath;
    }

    public String getOauthContext() {
        return oauthContext;
    }

    public void setOauthContext(String oauthContext) {
        this.oauthContext = oauthContext;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getJWTExpTime() {
        return JWTExpTime;
    }

    public void setJWTExpTime(Integer JWTExpTime) {
        this.JWTExpTime = JWTExpTime;
    }

    @Override
    public String toString() {
        return "GatewayBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", oauthTokenPath='" + oauthTokenPath + '\'' +
                ", oauthAuthPath='" + oauthAuthPath + '\'' +
                ", oauthContext='" + oauthContext + '\'' +
                ", jwtExpTime='" + JWTExpTime + '\'' +
                ", type=" + type +
                '}';
    }
}
