package com.t1t.apim.beans.services;


/**
 * Models the configuration of a REST gateway.
 */
public class RestServiceConfig {

    private String endpoint;
    private String username;
    private String password;

    public RestServiceConfig() {
    }

    public RestServiceConfig(String endpoint) {
        this.endpoint = endpoint;
    }

    public RestServiceConfig(String endpoint, String username, String password) {
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "RestGatewayConfigBean [endpoint=" + endpoint + ", username=" + username + ", password="
                + password + "]";
    }

}
