package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IEventResource {

    /**
     * Use this endpoint to retrieve all pending membership requests for a given organization
     * @param organizationId
     * @return
     * @throws NotAuthorizedException
     */
    public List<EventBean> getMembershipRequests(String organizationId) throws NotAuthorizedException;

    public List<EventBean> listAllEvents(String organizationId) throws NotAuthorizedException;

    public void rejectMembershipRequest(String organizationId, String userId) throws NotAuthorizedException;
}
