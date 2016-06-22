package com.t1t.digipolis.apim.beans.authorization;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by michallispashidis on 24/09/15.
 */
@Entity
@Table(name = "oauth_apps")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class OAuthAppBean implements Serializable {
    private static final long serialVersionUID = -8535544608508756791L;
    @Id @GeneratedValue
    private Long id;
    @Column(name = "oauth_svc_orgid",nullable = false)
    private String serviceOrgId;
    @Column(name = "oauth_svc_id", nullable = false)
    private String serviceId;
    @Column(name = "oauth_svc_version", nullable = false)
    private String serviceVersion;
    @Column(name = "oauth_client_id",nullable = false)
    private String clientId;
    @Column(name = "oauth_client_secret", nullable = false)
    private String clientSecret;
    @Column(name = "oauth_client_redirect")
    private String clientRedirect;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    private ApplicationVersionBean app;

    public OAuthAppBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceOrgId() {
        return serviceOrgId;
    }

    public void setServiceOrgId(String serviceOrgId) {
        this.serviceOrgId = serviceOrgId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientRedirect() {
        return clientRedirect;
    }

    public void setClientRedirect(String clientRedirect) {
        this.clientRedirect = clientRedirect;
    }

    public ApplicationVersionBean getApp() {
        return app;
    }

    public void setApp(ApplicationVersionBean appVersion) {
        this.app = appVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuthAppBean)) return false;

        OAuthAppBean that = (OAuthAppBean) o;

        if (!serviceOrgId.equals(that.serviceOrgId)) return false;
        if (!serviceId.equals(that.serviceId)) return false;
        if (!serviceVersion.equals(that.serviceVersion)) return false;
        return app.equals(that.app);

    }

    @Override
    public int hashCode() {
        int result = serviceOrgId.hashCode();
        result = 31 * result + serviceId.hashCode();
        result = 31 * result + serviceVersion.hashCode();
        result = 31 * result + app.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OAuthAppBean{" +
                "id=" + id +
                ", serviceOrgId='" + serviceOrgId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", clientRedirect='" + clientRedirect + '\'' +
                ", appVersion=" + app +
                '}';
    }
}
