package com.t1t.apim.beans.events;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public abstract class Event implements Serializable {
    private Long id;
    private EventType type;
    private Date createdOn;
    private String body;

    public Event() {}

    public Event(EventBean event) {
        this.setId(event.getId());
        this.setBody(event.getBody());
        this.setType(event.getType());
        this.setCreatedOn(event.getCreatedOn());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}