package com.t1t.digipolis.apim.beans.announcements;

import java.io.Serializable;

/**
 * Created by michallispashidis on 3/10/15.
 */
public class NewAnnouncementBean implements Serializable{
    private String title;
    private String description;

    public NewAnnouncementBean() {
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

    @Override
    public String toString() {
        return "NewAnnouncementBean{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
