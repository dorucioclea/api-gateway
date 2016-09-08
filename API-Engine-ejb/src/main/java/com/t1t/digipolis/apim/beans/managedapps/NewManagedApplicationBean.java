package com.t1t.digipolis.apim.beans.managedapps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewManagedApplicationBean implements Serializable {

    private ManagedApplicationTypes type;
    private String gatewayId;
    private String appId;
    private String prefix;
    private String name;
    private String version;
    private String gatewayUsername;
    private String apiKey;
    private Boolean restricted;
    private Boolean activated;

    public NewManagedApplicationBean() {
    }

    public NewManagedApplicationBean(ManagedApplicationTypes type, String gatewayId, String appId, String prefix, String name, String version, String gatewayUsername, String apiKey, Boolean restricted, Boolean activated) {
        this.type = type;
        this.gatewayId = gatewayId;
        this.appId = appId;
        this.prefix = prefix;
        this.name = name;
        this.version = version;
        this.gatewayUsername = gatewayUsername;
        this.apiKey = apiKey;
        this.restricted = restricted;
        this.activated = activated;
    }

    public ManagedApplicationTypes getType() {
        return type;
    }

    public void setType(ManagedApplicationTypes type) {
        this.type = type;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGatewayUsername() {
        return gatewayUsername;
    }

    public void setGatewayUsername(String gatewayUsername) {
        this.gatewayUsername = gatewayUsername;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}