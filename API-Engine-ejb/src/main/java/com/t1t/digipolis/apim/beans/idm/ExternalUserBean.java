package com.t1t.digipolis.apim.beans.idm;

import java.util.List;

/**
 * Created by michallispashidis on 23/11/15.
 * This user bean is provide for capturing external user information.
 * The IUserExternalInfoService is implemented using SCIM protocol, but this can be any arbitrary user provisioning protocol.
 */
public class ExternalUserBean {
    private String username;
    private String name;
    private String givenname;
    private String surname;
    private String accountId;//can serve as JWT subject
    private List<String> emails;
    private String createdon;
    private String lastModified;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "ExternalUserBean{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", givenname='" + givenname + '\'' +
                ", surname='" + surname + '\'' +
                ", accountId='" + accountId + '\'' +
                ", emails=" + emails +
                ", createdon='" + createdon + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}
