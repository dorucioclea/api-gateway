package com.t1t.digipolis.apim.beans.idp;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "keystores")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeystoreBean implements Serializable {

    @Id
    private String kid;
    @Column(name = "name")
    private String name;
    @Column(name = "path")
    private String path;
    @Column(name = "encrypted_keystore_password")
    private String encryptedKeystorePassword;
    @Column(name = "encrypted_key_password")
    private String encryptedKeyPassword;
    @Column(name = "private_key_alias")
    private String privateKeyAlias;
    @Column(name = "priority")
    private Long priority;
    @Column(name = "default_keystore")
    private boolean defaultKeystore;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncryptedKeystorePassword() {
        return encryptedKeystorePassword;
    }

    public void setEncryptedKeystorePassword(String encryptedKeystorePassword) {
        this.encryptedKeystorePassword = encryptedKeystorePassword;
    }

    public String getEncryptedKeyPassword() {
        return encryptedKeyPassword;
    }

    public void setEncryptedKeyPassword(String encryptedKeyPassword) {
        this.encryptedKeyPassword = encryptedKeyPassword;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public boolean isDefaultKeystore() {
        return defaultKeystore;
    }

    public void setDefaultKeystore(boolean defaultKeystore) {
        this.defaultKeystore = defaultKeystore;
    }

    public String getPrivateKeyAlias() {
        return privateKeyAlias;
    }

    public void setPrivateKeyAlias(String privateKeyAlias) {
        this.privateKeyAlias = privateKeyAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeystoreBean)) return false;

        KeystoreBean that = (KeystoreBean) o;

        return kid != null ? kid.equals(that.kid) : that.kid == null;
    }

    @Override
    public int hashCode() {
        return kid != null ? kid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "KeystoreBean{" +
                "kid='" + kid + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", encryptedKeystorePassword='*******'" +
                ", encryptedKeyPassword='*******'" +
                ", privateKeyAlias='" + privateKeyAlias + '\'' +
                ", priority=" + priority +
                ", defaultKeystore=" + defaultKeystore +
                '}';
    }
}