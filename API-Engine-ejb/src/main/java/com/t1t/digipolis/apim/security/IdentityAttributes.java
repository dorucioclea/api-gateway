package com.t1t.digipolis.apim.security;

/**
 * Created by michallispashidis on 2/12/15.
 */
public class IdentityAttributes {
    private String subjectId;
    private String id;
    private String familyName;
    private String userName;
    private String emails;
    private String givenName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "IdentityAttributes{" +
                "subjectId='" + subjectId + '\'' +
                ", id='" + id + '\'' +
                ", familyName='" + familyName + '\'' +
                ", userName='" + userName + '\'' +
                ", emails='" + emails + '\'' +
                ", givenName='" + givenName + '\'' +
                '}';
    }
}
