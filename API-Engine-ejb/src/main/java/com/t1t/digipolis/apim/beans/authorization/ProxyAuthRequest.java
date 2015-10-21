package com.t1t.digipolis.apim.beans.authorization;

import java.io.Serializable;

/**
 * Created by michallispashidis on 14/10/15.
 */
public class ProxyAuthRequest implements Serializable{
    private String username;
    private String password;

    public ProxyAuthRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
