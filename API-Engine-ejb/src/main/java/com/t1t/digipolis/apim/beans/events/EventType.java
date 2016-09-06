package com.t1t.digipolis.apim.beans.events;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public enum EventType {
    MEMBERSHIP_PENDING,
    MEMBERSHIP_GRANTED,
    MEMBERSHIP_REJECTED,
    MEMBERSHIP_CANCELLED,
    CONTRACT_PENDING,
    CONTRACT_ACCEPTED,
    CONTRACT_REJECTED,
    CONTRACT_CANCELLED,
    ANNOUNCEMENT_NEW
}
