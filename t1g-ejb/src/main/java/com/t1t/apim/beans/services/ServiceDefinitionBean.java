package com.t1t.apim.beans.services;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Bean used to store a service definition.
 *
 */
@Entity
@Table(name = "service_defs")
public class ServiceDefinitionBean implements Serializable {

    private static final long serialVersionUID = 7744514362366320690L;

    @Id
    private long id;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="service_version_id")
    private ServiceVersionBean serviceVersion;
    @Lob
    private byte[] data;

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the serviceVersion
     */
    public ServiceVersionBean getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @param serviceVersion the serviceVersion to set
     */
    public void setServiceVersion(ServiceVersionBean serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        final int maxLen = 10;
        return "ServiceDefinitionBean [id=" + id + ", serviceVersion=" + serviceVersion + ", data="
                + (data != null ? Arrays.toString(Arrays.copyOf(data, Math.min(data.length, maxLen))) : null)
                + "]";
    }
}
