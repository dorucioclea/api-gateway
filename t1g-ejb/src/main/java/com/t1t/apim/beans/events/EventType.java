package com.t1t.apim.beans.events;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public enum EventType {
    MEMBERSHIP_PENDING,
    MEMBERSHIP_GRANTED,
    MEMBERSHIP_REJECTED,
    MEMBERSHIP_REQUEST_CANCELLED,
    MEMBERSHIP_REVOKED_ROLE,
    MEMBERSHIP_REVOKED,
    MEMBERSHIP_UPDATED,
    MEMBERSHIP_TRANSFER,
    CONTRACT_PENDING,
    CONTRACT_ACCEPTED,
    CONTRACT_REJECTED,
    CONTRACT_REQUEST_CANCELLED,
    ANNOUNCEMENT_NEW,
    ADMIN_GRANTED,
    ADMIN_REVOKED
}
