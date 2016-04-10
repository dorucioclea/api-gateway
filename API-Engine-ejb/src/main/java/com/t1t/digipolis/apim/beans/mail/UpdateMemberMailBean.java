package com.t1t.digipolis.apim.beans.mail;

/**
 * Created by michallispashidis on 10/04/16.
 */
public class UpdateMemberMailBean extends AbstractMailBean{
    private MembershipAction membershipAction;
    private String to;

    public MembershipAction getMembershipAction() {
        return membershipAction;
    }

    public void setMembershipAction(MembershipAction membershipAction) {
        this.membershipAction = membershipAction;
    }
}
