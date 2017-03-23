package com.t1t.apim.beans.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDtoBean implements Serializable {

    private String username;
    private String kongUsername;
    private String fullName;
    private String email;
    private Date joinedOn;
    private Boolean admin;
    private String company;
    private String location;
    private String website;
    private String bio;
    private String base64pic;
    private String jwtKey;
    private String jwtSecret;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKongUsername() {
        return kongUsername;
    }

    public void setKongUsername(String kongUsername) {
        this.kongUsername = kongUsername;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(Date joinedOn) {
        this.joinedOn = joinedOn;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBase64pic() {
        return base64pic;
    }

    public void setBase64pic(String base64pic) {
        this.base64pic = base64pic;
    }

    public String getJwtKey() {
        return jwtKey;
    }

    public void setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDtoBean)) return false;

        UserDtoBean that = (UserDtoBean) o;

        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "UserDtoBean{" +
                "username='" + username + '\'' +
                ", kongUsername='" + kongUsername + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", joinedOn=" + joinedOn +
                ", admin=" + admin +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", website='" + website + '\'' +
                ", bio='" + bio + '\'' +
                ", base64pic='" + base64pic + '\'' +
                ", jwtKey='" + jwtKey + '\'' +
                ", jwtSecret='" + jwtSecret + '\'' +
                '}';
    }
}