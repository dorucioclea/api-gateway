package com.t1t.digipolis.apim.beans.managedapps;

import com.t1t.digipolis.apim.beans.services.ServiceGatewayBean;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@Entity
@Table(name = "managed_applications")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ManagedApplicationBean implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ManagedApplicationTypes type;
    @Column(name = "gateway_id")
    private String gatewayId;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "prefix",unique=true)
    private String prefix;
    @Column(name = "name")
    private String name;
    @Column(name = "version")
    private String version;
    @Column(name = "gateway_username")
    private String gatewayUsername;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="managed_application_keys", joinColumns=@JoinColumn(name="managed_app_id"))
    @Column(name = "api_key")
    private Set<String> apiKeys;
    @Column(name = "restricted")
    private Boolean restricted;
    @Column(name = "activated")
    private Boolean activated;

    public ManagedApplicationBean() {}

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

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getPrefix() {return prefix;}

    public void setPrefix(String prefix) {this.prefix = prefix;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGatewayUsername() {
        return gatewayUsername;
    }

    public void setGatewayUsername(String gatewayUsername) {
        this.gatewayUsername = gatewayUsername;
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

    public String getAppId() {return appId;}

    public void setAppId(String appId) {this.appId = appId;}

    public Boolean getRestricted() {return restricted;}

    public void setRestricted(Boolean restricted) {this.restricted = restricted;}

    public Boolean getActivated() {return activated;}

    public void setActivated(Boolean activated) {this.activated = activated;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManagedApplicationBean)) return false;

        ManagedApplicationBean that = (ManagedApplicationBean) o;

        if (!id.equals(that.id)) return false;
        if (type != that.type) return false;
        if (gatewayId != null ? !gatewayId.equals(that.gatewayId) : that.gatewayId != null) return false;
        if (!appId.equals(that.appId)) return false;
        if (!prefix.equals(that.prefix)) return false;
        if (!name.equals(that.name)) return false;
        if (!version.equals(that.version)) return false;
        if (gatewayUsername != null ? !gatewayUsername.equals(that.gatewayUsername) : that.gatewayUsername != null)
            return false;
        if (!apiKeys.equals(that.apiKeys)) return false;
        if (!restricted.equals(that.restricted)) return false;
        return activated.equals(that.activated);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
        result = 31 * result + appId.hashCode();
        result = 31 * result + prefix.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + (gatewayUsername != null ? gatewayUsername.hashCode() : 0);
        result = 31 * result + apiKeys.hashCode();
        result = 31 * result + restricted.hashCode();
        result = 31 * result + activated.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ManagedApplicationBean{" +
                "id=" + id +
                ", type=" + type +
                ", gatewayId='" + gatewayId + '\'' +
                ", appId='" + appId + '\'' +
                ", prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", gatewayUsername='" + gatewayUsername + '\'' +
                ", apiKeys='" + apiKeys + '\'' +
                ", restricted=" + restricted +
                ", activated=" + activated +
                '}';
    }
}