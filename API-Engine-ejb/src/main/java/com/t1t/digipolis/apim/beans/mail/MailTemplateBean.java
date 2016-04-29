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
@IdClass(OrganizationBasedCompositeId.class)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MailTemplateBean extends BaseEntity implements Serializable {
    @Id
    @Column(name="topic", nullable = false)
    private String id;

    @Id
    @ManyToOne
    @JoinColumns({@JoinColumn(name = "org_id", referencedColumnName = "id")})
    private OrganizationBean organization;

    @Column(name = "template")
    private String template;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrganizationBean getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationBean organization) {
        this.organization = organization;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        MailTemplateBean that = (MailTemplateBean) o;

        return id.equals(that.id) && organization.equals(that.organization);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + organization.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MailTemplateBean{" +
                "topic='" + id + '\'' +
                ", organization=" + organization +
                ", template='" + template + '\'' +
                '}';
    }
}
