package com.t1t.digipolis.apim.beans.mail;

/**
 * Created by michallispashidis on 29/04/16.
 */
public class TestMailBean extends BaseMailBean{
    String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "TestMailBean{" +
                "environment='" + environment + '\'' +
                '}';
    }
}
