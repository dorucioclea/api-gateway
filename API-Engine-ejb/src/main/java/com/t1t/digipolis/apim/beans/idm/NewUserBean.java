package com.t1t.digipolis.apim.beans.idm;

/**
 * Created by michallispashidis on 28/02/16.
 */
public class NewUserBean {
    private String username;
    private Boolean isAdmin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public NewUserBean withAdmin(Boolean admin) {
        setAdmin(admin);
        return this;
    }

    public NewUserBean withUsername(String username) {
        setUsername(username);
        return this;
    }

    @Override
    public String toString() {
        return "NewUserBean{" +
                "username='" + username + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
