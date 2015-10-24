package com.t1t.digipolis.apim.beans.support;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/10/15.
 */
public class NewSupportBean implements Serializable {
    private String title;
    private String description;

    public NewSupportBean() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NewSupportBean{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
