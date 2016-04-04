package com.t1t.digipolis.apim.beans.managedapps;

import com.t1t.digipolis.apim.beans.orgs.OrganizationBasedCompositeId;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

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
    @Column(name = "availability")
    private String availability;
    @Column(name = "name")
    private String name;
    @Column(name = "version")
    private String version;
    @Column(name = "gateway_username")
    private String gatewayUsername;
    @Column(name = "api_key")
    private String apiKey;

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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManagedApplicationBean that = (ManagedApplicationBean) o;

        if (!id.equals(that.id)) return false;
        if (type != that.type) return false;
        if (gatewayId != null ? !gatewayId.equals(that.gatewayId) : that.gatewayId != null) return false;
        if (availability != null ? !availability.equals(that.availability) : that.availability != null) return false;
        if (!name.equals(that.name)) return false;
        if (!version.equals(that.version)) return false;
        if (gatewayUsername != null ? !gatewayUsername.equals(that.gatewayUsername) : that.gatewayUsername != null)
            return false;
        return apiKey != null ? apiKey.equals(that.apiKey) : that.apiKey == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
        result = 31 * result + (availability != null ? availability.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + (gatewayUsername != null ? gatewayUsername.hashCode() : 0);
        result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ManagedApplicationBean{" +
                "id=" + id +
                ", type=" + type +
                ", gatewayId='" + gatewayId + '\'' +
                ", availability='" + availability + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", gatewayUsername='" + gatewayUsername + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}