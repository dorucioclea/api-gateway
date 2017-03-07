package com.t1t.digipolis.apim.beans.events;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MembershipRequest extends Event {
    private String userId;
    private String organizationId;

    public MembershipRequest(EventBean bean) {
        super(bean);
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getOrganizationId() {
        return organizationId;
    }
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "MembershipRequest{" +
                "userId='" + userId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                '}';
    }
}