package com.t1t.digipolis.apim.beans.scim;


/**
 * Models the configuration of a REST gateway.
 *
 */
public class SCIMConfigBean {
    private String endpoint;
    private String username;
    private String password;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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

    @Override
    public String toString() {
        return "SCIMConfigBean{" +
                "endpoint='" + endpoint + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
