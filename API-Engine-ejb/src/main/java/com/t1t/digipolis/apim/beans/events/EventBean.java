package com.t1t.digipolis.apim.beans.events;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Entity
@Table(name = "events")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class EventBean implements Serializable {

    @Id
    @Column(name = "request_origin")
    private String requestOrigin;
    @Id
    @Column(name = "request_destination")
    private String requestDestination;
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

    public String getRequestOrigin() {
        return requestOrigin;
    }

    public void setRequestOrigin(String requestOrigin) {
        this.requestOrigin = requestOrigin;
    }

    public String getRequestDestination() {
        return requestDestination;
    }

    public void setRequestDestination(String requestDestination) {
        this.requestDestination = requestDestination;
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

        if (!requestOrigin.equals(eventBean.requestOrigin)) return false;
        if (!requestDestination.equals(eventBean.requestDestination)) return false;
        return type == eventBean.type;

    }

    @Override
    public int hashCode() {
        int result = requestOrigin.hashCode();
        result = 31 * result + requestDestination.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "requestOrigin='" + requestOrigin + '\'' +
                ", requestDestination='" + requestDestination + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                '}';
    }
}