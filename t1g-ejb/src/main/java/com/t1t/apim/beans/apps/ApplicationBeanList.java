package com.t1t.apim.beans.apps;

import java.util.Set;

/**
 * Created by michallispashidis on 24/04/16.
 */
public class ApplicationBeanList {
    private Set<ApplicationBean> apps;

    private Integer length = 0;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Set<ApplicationBean> getApps() {
        return apps;
    }

    public void setApps(Set<ApplicationBean> apps) {
        this.apps = apps;
        if (apps != null && apps.size() > 0) this.length = apps.size();
    }
}
