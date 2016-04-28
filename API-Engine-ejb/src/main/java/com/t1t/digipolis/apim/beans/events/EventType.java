package com.t1t.digipolis.apim.beans.events;

import com.t1t.digipolis.apim.gateway.dto.Contract;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public enum EventType {
    MEMBERSHIP_PENDING, MEMBERSHIP_GRANTED, MEMBERSHIP_REJECTED, CONTRACT_PENDING, CONTRACT_ACCEPTED, CONTRACT_REJECTED
}
