package com.t1t.digipolis.apim.beans;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by michallispashidis on 29/04/16.
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", insertable = true, updatable = true)
    private Date updatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", insertable = true, updatable = false)
    private Date createdOn;

    @PrePersist
    void onCreate() {
        this.setCreatedOn(new Date());
    }

    @PreUpdate
    void onUpdate() {
        this.setUpdatedOn(new Date());
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "updatedOn=" + updatedOn +
                ", createdOn=" + createdOn +
                '}';
    }
}
