package com.t1t.digipolis.apim.beans.apps;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class UpdateApplicationVersionURIBean implements Serializable {

    private static final long serialVersionUID = 3793145550563328842L;
    private Set<String> uris;

    public UpdateApplicationVersionURIBean() {
    }

    public Set<String> getUris() {
        return uris;
    }

    public void setUris(Set<String> uris) {
        this.uris = uris;
    }

    @Override
    public String toString() {
        return "UpdateApplicationVersionURIBean{" +
                "uris='" + uris + '\'' +
                '}';
    }
}
