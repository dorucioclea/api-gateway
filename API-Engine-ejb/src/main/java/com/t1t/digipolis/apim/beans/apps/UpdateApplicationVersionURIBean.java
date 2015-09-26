package com.t1t.digipolis.apim.beans.apps;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class UpdateApplicationVersionURIBean {
    private String uri;

    public UpdateApplicationVersionURIBean() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "UpdateApplicationVersionURIBean{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
