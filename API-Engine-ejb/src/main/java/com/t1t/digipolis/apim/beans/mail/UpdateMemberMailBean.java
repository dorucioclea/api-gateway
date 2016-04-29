package com.t1t.digipolis.apim.beans.mail;

/**
 * Created by michallispashidis on 10/04/16.
 */
public class UpdateMemberMailBean extends BaseMailBean {
    private MembershipAction membershipAction;
    private String orgName;
    private String orgFriendlyName;
    private String role;

    public MembershipAction getMembershipAction() {
        return membershipAction;
    }

    public void setMembershipAction(MembershipAction membershipAction) {
        this.membershipAction = membershipAction;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgFriendlyName() {
        return orgFriendlyName;
    }

    public void setOrgFriendlyName(String orgFriendlyName) {
        this.orgFriendlyName = orgFriendlyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
