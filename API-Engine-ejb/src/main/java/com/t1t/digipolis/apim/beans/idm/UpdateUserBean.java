package com.t1t.digipolis.apim.beans.idm;

import java.io.Serializable;

/**
 * The bean used when updating a user.
 *
 */
public class UpdateUserBean implements Serializable {

    private static final long serialVersionUID = 7773886494093983234L;

    private String fullName;
    private String email;
    private String pic;

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

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UpdateUserBean [fullName=" + fullName + ", email=" + email + "]";
    }
}
