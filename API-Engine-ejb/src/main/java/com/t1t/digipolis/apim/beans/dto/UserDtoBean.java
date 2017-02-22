package com.t1t.digipolis.apim.beans.dto;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UserDtoBean {

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

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (kongUsername != null ? !kongUsername.equals(that.kongUsername) : that.kongUsername != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (joinedOn != null ? !joinedOn.equals(that.joinedOn) : that.joinedOn != null) return false;
        if (admin != null ? !admin.equals(that.admin) : that.admin != null) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (website != null ? !website.equals(that.website) : that.website != null) return false;
        if (bio != null ? !bio.equals(that.bio) : that.bio != null) return false;
        if (base64pic != null ? !base64pic.equals(that.base64pic) : that.base64pic != null) return false;
        if (jwtKey != null ? !jwtKey.equals(that.jwtKey) : that.jwtKey != null) return false;
        return jwtSecret != null ? jwtSecret.equals(that.jwtSecret) : that.jwtSecret == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (kongUsername != null ? kongUsername.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (joinedOn != null ? joinedOn.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (bio != null ? bio.hashCode() : 0);
        result = 31 * result + (base64pic != null ? base64pic.hashCode() : 0);
        result = 31 * result + (jwtKey != null ? jwtKey.hashCode() : 0);
        result = 31 * result + (jwtSecret != null ? jwtSecret.hashCode() : 0);
        return result;
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