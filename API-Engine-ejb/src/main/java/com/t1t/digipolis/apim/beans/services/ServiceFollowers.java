package com.t1t.digipolis.apim.beans.services;

import java.util.Set;

/**
 * Created by michallispashidis on 3/10/15.
 */
public class ServiceFollowers {
    private Set<String> followers;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ServiceFollowers() {
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
    }
}
