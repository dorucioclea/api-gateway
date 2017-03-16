package com.t1t.apim.beans.mail;

/**
 * Created by michallispashidis on 10/04/16.
 */
public class MembershipRequestMailBean extends BaseMailBean {
    private String orgName;
    private String orgFriendlyName;
    private String userId;
    private String userMail;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgFriendlyName() {
        return orgFriendlyName == null || orgFriendlyName.trim().isEmpty() ? orgName : orgFriendlyName;
    }

    public void setOrgFriendlyName(String orgFriendlyName) {
        this.orgFriendlyName = orgFriendlyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public String toString() {
        return "MembershipRequestMailBean{" +
                "orgName='" + orgName + '\'' +
                ", orgFriendlyName='" + orgFriendlyName + '\'' +
                ", userId='" + userId + '\'' +
                ", userMail='" + userMail + '\'' +
                '}';
    }
}
