package com.t1t.digipolis.apim.beans.jwt;
import java.util.*;
/**
 * Created by michallispashidis on 23/11/15.
 */
public class JWTRequestBean {
    //JWT fields
    private String issuer;
    private String audience;
    private Date experationTime;
    private Date issuanceTime;
    private String subject;
    private Integer expirationTimeMinutes;

    //custom fields
    private String plan;
    private String name;
    private String givenName;
    private String surname;
    private String email;

    public Integer getExpirationTimeMinutes() {
        return expirationTimeMinutes;
    }

    public void setExpirationTimeMinutes(Integer expirationTimeMinutes) {
        this.expirationTimeMinutes = expirationTimeMinutes;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Date getExperationTime() {
        return experationTime;
    }

    public void setExperationTime(Date experationTime) {
        this.experationTime = experationTime;
    }

    public Date getIssuanceTime() {
        return issuanceTime;
    }

    public void setIssuanceTime(Date issuanceTime) {
        this.issuanceTime = issuanceTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "JWTRequestBean{" +
                "issuer='" + issuer + '\'' +
                ", audience='" + audience + '\'' +
                ", experationTime=" + experationTime +
                ", issuanceTime=" + issuanceTime +
                ", subject='" + subject + '\'' +
                ", expirationTimeMinutes=" + expirationTimeMinutes +
                ", plan='" + plan + '\'' +
                ", name='" + name + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
