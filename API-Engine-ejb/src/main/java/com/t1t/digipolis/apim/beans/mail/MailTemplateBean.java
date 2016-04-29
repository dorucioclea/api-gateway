package com.t1t.digipolis.apim.beans.mail;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.t1t.digipolis.apim.beans.BaseEntity;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBasedCompositeId;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by michallispashidis on 29/04/16.
 */
@Entity
@Table(name = "mail_templates")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MailTemplateBean extends BaseEntity implements Serializable {
    @Id
    @Column(name="topic", nullable = false)
    private String id;

    @Column(name = "content")
    private String template;

    @Column(name = "subject")
    private String subject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailTemplateBean that = (MailTemplateBean) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MailTemplateBean{" +
                "id='" + id + '\'' +
                ", template='" + template + '\'' +
                '}';
    }
}
