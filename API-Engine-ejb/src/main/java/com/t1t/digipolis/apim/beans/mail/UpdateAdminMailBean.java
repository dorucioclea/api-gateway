package com.t1t.digipolis.apim.beans.mail;

/**
 * Created by michallispashidis on 10/04/16.
 */
public class UpdateAdminMailBean  extends AbstractMailBean {
    private MembershipAction membershipAction;

    public MembershipAction getMembershipAction() {
        return membershipAction;
    }

    public void setMembershipAction(MembershipAction membershipAction) {
        this.membershipAction = membershipAction;
    }
}