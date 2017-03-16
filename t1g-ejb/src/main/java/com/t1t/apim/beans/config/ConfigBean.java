package com.t1t.apim.beans.config;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by michallispashidis on 25/07/16.
 */
@Entity
@Table(name = "config")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigBean implements Serializable {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(name = "config_path",nullable = false)
    private String configPath;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "id=" + id +
                ", configPath='" + configPath + '\'' +
                '}';
    }
}
