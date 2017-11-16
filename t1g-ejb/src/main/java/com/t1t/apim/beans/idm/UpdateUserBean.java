package com.t1t.apim.beans.idm;

import java.io.Serializable;

/**
 * The bean used when updating a user.
 */
public class UpdateUserBean implements Serializable {

    private static final long serialVersionUID = 7773886494093983234L;

    private String fullName;
    private String email;
    private String pic;
    private String company;
    private String location;
    private String website;
    private String bio;

    /**
     * Constructor.
     */
    public UpdateUserBean() {
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    @Override
    public String toString() {
        return "UpdateUserBean{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", website='" + website + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
