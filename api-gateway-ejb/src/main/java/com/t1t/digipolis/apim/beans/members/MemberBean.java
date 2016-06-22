package com.t1t.digipolis.apim.beans.members;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Models a user as a member of an organization.
 *
 */
public class MemberBean implements Serializable {

    private static final long serialVersionUID = -6731054525814345766L;

    private String userId;
    private String userName;
    private String email;
    private Date joinedOn;
    private List<MemberRoleBean> roles;

    /**
     * Constructor.
     */
    public MemberBean() {
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * @return the roles
     */
    public List<MemberRoleBean> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<MemberRoleBean> roles) {
        this.roles = roles;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "MemberBean [userId=" + userId + ", userName=" + userName + ", email=" + email + ", joinedOn="
                + joinedOn + ", roles=" + roles + "]";
    }

}
