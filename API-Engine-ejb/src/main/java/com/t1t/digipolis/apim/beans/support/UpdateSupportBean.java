package com.t1t.digipolis.apim.beans.support;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/10/15.
 */
public class UpdateSupportBean implements Serializable {
    private String title;
    private String description;
    private SupportStatus status;

    public UpdateSupportBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SupportStatus getStatus() {
        return status;
    }

    public void setStatus(SupportStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateSupportBean{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
