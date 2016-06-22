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
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "origin_id")
    private String originId;
    @Column(name = "destination_id")
    private String destinationId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Column(name = "created_on")
    private Date createdOn;
    @Column(name = "body")
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String userId) {
        this.originId = userId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationVersion) {
        this.destinationId = destinationVersion;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventBean eventBean = (EventBean) o;

        return id != null ? id.equals(eventBean.id) : eventBean.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "id=" + id +
                ", originId='" + originId + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", type=" + type +
                ", createdOn=" + createdOn +
                ", body='" + body + '\'' +
                '}';
    }
}