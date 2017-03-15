package com.t1t.digipolis.apim.beans.mail;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "mail_providers")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailProviderBean implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "host")
    private String host;
    @Column(name = "port")
    private Long port;
    @Column(name = "auth")
    private boolean auth;
    @Column(name = "mail_from")
    private String from;
    @Column(name = "username")
    private String user;
    @Column(name = "encrypted_password")
    private String encryptedPassword;
    @Column(name = "default_mail_provider")
    private boolean defaultMailProvider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isDefaultMailProvider() {
        return defaultMailProvider;
    }

    public void setDefaultMailProvider(boolean defaultMailProvider) {
        this.defaultMailProvider = defaultMailProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailProviderBean)) return false;

        MailProviderBean that = (MailProviderBean) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MailProviderBean{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", auth=" + auth +
                ", from='" + from + '\'' +
                ", user='" + user + '\'' +
                ", encryptedPassword='********'" +
                ", defaultMailProvider=" + defaultMailProvider +
                '}';
    }
}