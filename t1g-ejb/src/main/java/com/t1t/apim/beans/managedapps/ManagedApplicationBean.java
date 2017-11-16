package com.t1t.apim.beans.managedapps;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@Entity
@Table(name = "managed_applications")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagedApplicationBean implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ManagedApplicationTypes type;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "prefix", unique = true)
    private String prefix;
    @Column(name = "name")
    private String name;
    @Column(name = "version")
    private String version;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "managed_application_keys", joinColumns = @JoinColumn(name = "managed_app_id"))
    @Column(name = "api_key")
    private Set<String> apiKeys;
    @Column(name = "restricted")
    private Boolean restricted;
    @Column(name = "activated")
    private Boolean activated;

    public ManagedApplicationBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ManagedApplicationTypes getType() {
        return type;
    }

    public void setType(ManagedApplicationTypes type) {
        this.type = type;
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

    public Set<String> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Set<String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManagedApplicationBean)) return false;

        ManagedApplicationBean that = (ManagedApplicationBean) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ManagedApplicationBean{" +
                "id=" + id +
                ", type=" + type +
                ", appId='" + appId + '\'' +
                ", prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", apiKeys=" + apiKeys +
                ", restricted=" + restricted +
                ", activated=" + activated +
                '}';
    }
}