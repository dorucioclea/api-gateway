package com.t1t.digipolis.apim.beans.authorization;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by michallispashidis on 14/10/15.
 */
public class ProxyAuthRequest implements Serializable{
    private String username;
    private String password;
    private Map<String,String> optionalClaimset;
    private String expectedAudience;

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

    public Map<String, String> getOptionalClaimset() {
        return optionalClaimset;
    }

    public void setOptionalClaimset(Map<String, String> optionalClaimset) {
        this.optionalClaimset = optionalClaimset;
    }

    public String getExpectedAudience() {
        return expectedAudience;
    }

    public void setExpectedAudience(String expectedAudience) {
        this.expectedAudience = expectedAudience;
    }

    @Override
    public String toString() {
        return "ProxyAuthRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", optionalClaimset=" + optionalClaimset +
                ", expectedAudience='" + expectedAudience + '\'' +
                '}';
    }
}
