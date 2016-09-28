package com.t1t.digipolis.apim.beans.gateways;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @Column(name = "oauth_exp_time", nullable = true)
    private Integer OAuthExpTime;
    @Column(name = "jwt_exp_time", nullable = true)
    private Integer JWTExpTime;
    @Column(name = "jwt_pub_key", nullable = true)
    private String JWTPubKey;
    @Column(name = "jwt_pub_key_endpoint", nullable = true)
    private String JWTPubKeyEndpoint;
    @Column(name = "jwt_priv_key", nullable = true)
    private String JWTPrivKey;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getOAuthExpTime() {
        return OAuthExpTime;
    }

    public void setOAuthExpTime(Integer OAuthExpTime) {
        this.OAuthExpTime = OAuthExpTime;
    }

    public Integer getJWTExpTime() {
        return JWTExpTime;
    }

    public void setJWTExpTime(Integer JWTExpTime) {
        this.JWTExpTime = JWTExpTime;
    }

    public String getJWTPubKey() {
        return JWTPubKey;
    }

    public void setJWTPubKey(String JWTPubKey) {
        this.JWTPubKey = JWTPubKey;
    }

    public GatewayType getType() {
        return type;
    }

    public void setType(GatewayType type) {
        this.type = type;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getJWTPubKeyEndpoint() {
        return JWTPubKeyEndpoint;
    }

    public void setJWTPubKeyEndpoint(String JWTPubKeyEndpoint) {
        this.JWTPubKeyEndpoint = JWTPubKeyEndpoint;
    }

    public String getJWTPrivKey() {return JWTPrivKey;}

    public void setJWTPrivKey(String JWTPrivKeyEndpoint) {this.JWTPrivKey = JWTPrivKeyEndpoint;}

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
                ", OAuthExpTime=" + OAuthExpTime +
                ", JWTExpTime=" + JWTExpTime +
                ", JWTPubKey='" + JWTPubKey + '\'' +
                ", JWTPubKeyEndpoint='" + JWTPubKeyEndpoint + '\'' +
                ", type=" + type +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
