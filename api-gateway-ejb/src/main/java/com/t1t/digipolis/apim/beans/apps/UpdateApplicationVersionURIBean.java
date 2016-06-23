package com.t1t.digipolis.apim.beans.apps;

import java.io.Serializable;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class UpdateApplicationVersionURIBean implements Serializable {

    private static final long serialVersionUID = 3793145550563328842L;
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
