package com.t1t.digipolis.apim.beans.idm;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Models a single user.
 *
 */
@Entity
@Table(name = "users")
public class UserBean implements Serializable {

    private static final long serialVersionUID = 865765107251347714L;

    /**
     * username is unique and is the SAML2 id claim from authentication server - and subject for JWT token - must be used by the front-end applications.
     * Be carefull the SAML2 Subject ID is used for single logout and is persisted in a distributed cache.
     */
    @Id
    @Column(updatable=false, nullable=false)
    private String username;
    @Column(name = "kong_username")
    private String kongUsername;//to retrieve consumer from Kong
    @Column(name = "full_name")
    private String fullName;
    private String email;
    @Column(name = "joined_on", updatable=false)
    private Date joinedOn;
    @Column(name="admin")
    private Boolean admin=false;//default
    @Column(name="company")
    private String company;
    @Column(name="location")
    private String location;
    @Column(name="website")
    private String website;
    @Lob
    @Column(name="bio")
    @Type(type = "org.hibernate.type.TextType")
    private String bio;
    @Column(name = "pic")
    @Lob
    @Basic(fetch=FetchType.EAGER)
    private byte[] base64pic;
    @Column(name="jwt_key")
    private String jwtKey;
    @Column(name="jwt_secret")
    private String jwtSecret;


    /**
     * Constructor.
     */
    public UserBean() {
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the joinedOn
     */
    public Date getJoinedOn() {
        return joinedOn;
    }

    /**
     * @param joinedOn the joinedOn to set
     */
    public void setJoinedOn(Date joinedOn) {
        this.joinedOn = joinedOn;
    }

    public String getBase64pic() {
        if(base64pic!=null) return Base64.encodeBase64String(base64pic);
        else return "";
    }

    public void setBase64pic(String base64pic) {
        if(!StringUtils.isEmpty(base64pic)) this.base64pic = Base64.decodeBase64(base64pic.getBytes());
        else this.base64pic = null;
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

    public String getKongUsername() {
        return kongUsername;
    }

    public void setKongUsername(String kongUsername) {
        this.kongUsername = kongUsername;
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

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserBean other = (UserBean) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "UserBean{" +
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
                ", base64pic=" + Arrays.toString(base64pic) +
                ", jwtKey='" + jwtKey + '\'' +
                ", jwtSecret='" + jwtSecret + '\'' +
                '}';
    }
}
