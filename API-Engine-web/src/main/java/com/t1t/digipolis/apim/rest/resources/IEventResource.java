package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IEventResource {
    public List<EventBean> getMembershipRequests(String organizationId) throws NotAuthorizedException;
}
