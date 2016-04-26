package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.events.EventStatus;
import com.t1t.digipolis.apim.beans.events.EventType;
import com.t1t.digipolis.apim.beans.events.NewEventBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EventFacade {

    private static final Logger _LOG = LoggerFactory.getLogger(EventFacade.class);
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private ISecurityContext securityContext;

    public EventBean create(NewEventBean bean) {
        switch (bean.getType()) {
            case Membership:
                return createMembershipRequest(bean);
            default:
                return null;
        }
    }

    public List<EventBean> getMembershipRequests(String organizationId) {
        try {
            OrganizationBean org = storage.getOrganization(organizationId);
            if (org == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            return query.getMembershipRequests(org.getId());
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private EventBean createMembershipRequest(NewEventBean bean) {
        //Create a new EventBean for the current request
        EventBean newRequest = new EventBean();
        newRequest.setType(EventType.Membership);
        newRequest.setStatus(EventStatus.Pending);
        newRequest.setRequestOrigin(bean.getRequestOrigin());
        newRequest.setRequestDestination(bean.getRequestDestination());
        newRequest.setCreatedOn(new Date());
        newRequest.setModifiedOn(newRequest.getCreatedOn());
        try {
            //In case there still is an extant event marking the request as refused, delete it
            EventBean event = query.getEvent(bean.getRequestOrigin(), bean.getRequestDestination(), bean.getType());
            if (event != null && event.getStatus() == EventStatus.Refused) {
                storage.deleteEvent(event);
            }
            storage.createEvent(newRequest);
            _LOG.debug("new membershiprequest created:{}", newRequest);
            return newRequest;
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }
}