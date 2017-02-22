package com.t1t.digipolis.apim.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.digipolis.apim.beans.gateways.GatewayType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayDtoBean implements Serializable {

    private String id;
    private String name;
    private String endpoint;
    private String description;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private Integer OAuthExpTime;
    private Integer JWTExpTime;
    private String JWTPubKey;
    private String JWTPubKeyEndpoint;
    private String JWTPrivKey;
    private GatewayType type;
    private String configuration;

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
        return "GatewayDtoBean{" +
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
                ", JWTPrivKey='" + JWTPrivKey + '\'' +
                ", type=" + type +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
