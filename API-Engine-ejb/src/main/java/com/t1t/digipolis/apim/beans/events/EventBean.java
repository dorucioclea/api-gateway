package com.t1t.digipolis.apim.beans.events;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Entity
@Table(name = "events")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class EventBean implements Serializable {

    @Id
    @Column(name = "origin")
    private String origin;
    @Id
    @Column(name = "destination")
    private String destination;
    @Id
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @Column(name = "created_on")
    private Date createdOn;
    @Column(name = "modified_on")
    private Date modifiedOn;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String requestOrigin) {
        this.origin = requestOrigin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String requestDestination) {
        this.destination = requestDestination;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventBean eventBean = (EventBean) o;

        if (!origin.equals(eventBean.origin)) return false;
        if (!destination.equals(eventBean.destination)) return false;
        return type == eventBean.type;

    }

    @Override
    public int hashCode() {
        int result = origin.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "requestOrigin='" + origin + '\'' +
                ", requestDestination='" + destination + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                '}';
    }
}