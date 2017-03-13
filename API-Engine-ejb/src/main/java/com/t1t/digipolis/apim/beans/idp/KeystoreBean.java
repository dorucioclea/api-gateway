package com.t1t.digipolis.apim.beans.idp;

import javax.persistence.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "keystores")
public class KeystoreBean {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "encrypted_keystore_password")
    private String encryptedKeystorePassword;
    @Column(name = "encrypted_key_password")
    private String encryptedKeyPassword;
    @Column(name = "default_keystore")
    private boolean defaultKeystore;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDefaultKeystore() {
        return defaultKeystore;
    }

    public void setDefaultKeystore(boolean defaultKeystore) {
        this.defaultKeystore = defaultKeystore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeystoreBean)) return false;

        KeystoreBean that = (KeystoreBean) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "KeystoresBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", encryptedKeystorePassword='********'" +
                ", encryptedKeyPassword='********'" +
                ", defaultKeystore=" + defaultKeystore +
                '}';
    }
}