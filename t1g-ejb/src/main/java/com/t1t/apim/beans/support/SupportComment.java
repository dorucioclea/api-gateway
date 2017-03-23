package com.t1t.apim.beans.support;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by michallispashidis on 3/10/15.
 */
@Entity
@Table(name = "support_comments")
public class SupportComment implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "support_id")
    private Long supportId;
    @Lob
    @Column(name = "comment")
    @Type(type = "org.hibernate.type.TextType")
    private String comment;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupportId() {
        return supportId;
    }

    public void setSupportId(Long supportId) {
        this.supportId = supportId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "SupportComment{" +
                "id=" + id +
                ", supportId=" + supportId +
                ", comment='" + comment + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
