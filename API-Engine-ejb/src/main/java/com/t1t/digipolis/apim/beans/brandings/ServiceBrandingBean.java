package com.t1t.digipolis.apim.beans.brandings;

import com.t1t.digipolis.apim.beans.services.ServiceBean;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Entity
@Table(name = "branding_domains")
public class ServiceBrandingBean implements Serializable {

    @Id
    @GeneratedValue
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "base_path")
    private String basePath;
    @ManyToMany(mappedBy = "brandings")
    private Set<ServiceBean> services;

    public ServiceBrandingBean() {
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

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Set<ServiceBean> getServices() {
        return services;
    }

    public void setServices(Set<ServiceBean> services) {
        this.services = services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceBrandingBean that = (ServiceBrandingBean) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServiceBrandingBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", basePath='" + basePath + '\'' +
                ", services=" + services +
                '}';
    }
}