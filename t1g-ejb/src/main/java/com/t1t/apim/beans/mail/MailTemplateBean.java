package com.t1t.apim.beans.mail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by michallispashidis on 29/04/16.
 */
@Entity
@Table(name = "mail_templates")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailTemplateBean extends BaseEntity implements Serializable {
    @Id
    @Column(name = "topic", nullable = false)
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
