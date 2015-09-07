package com.t1t.digipolis.apim.beans.idm;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Models a single user.
 *
 */
@Entity
@Table(name = "users")
public class UserBean implements Serializable {

    private static final long serialVersionUID = 865765107251347714L;

    @Id
    @Column(updatable=false, nullable=false)
    private String username;
    @Column(name = "full_name")
    private String fullName;
    private String email;
    @Column(name = "joined_on", updatable=false)
    private Date joinedOn;
    @Column(name="admin")
    private Boolean admin=false;//default

    // Used only when returning information about the current user
/*    @Transient
    private boolean admin;*/

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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UserBean [username=" + username + ", fullName=" + fullName + ", email=" + email
                + ", joinedOn=" + joinedOn + ", admin=" + admin + "]";
    }
}
