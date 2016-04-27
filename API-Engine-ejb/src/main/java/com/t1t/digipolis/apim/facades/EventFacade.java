package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.events.EventStatus;
import com.t1t.digipolis.apim.beans.events.EventType;
import com.t1t.digipolis.apim.beans.events.NewEventBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequest;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequestAccepted;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequestRejected;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
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
    private static final String MEMBERSHIP = "Membership";
    private static final String CONTRACT = "Contract";
    private static final String PENDING = "Pending";
    private static final String ACCEPTED = "Accepted";
    private static final String REJECTED = "Rejected";

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

    public List<EventBean> getCurrentUserAllIncomingEvents() {
        String userId = securityContext.getCurrentUser();
        try {
            return query.getAllIncomingEventsForEntity(userId);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getCurrentUserIncomingEventsByTypeAndStatus(String type, String status) {
        EventType eType = validateEventType(type);
        EventStatus eStatus = validateEventStatus(status);
        String userId = securityContext.getCurrentUser();
        try {
            return query.getIncomingEventsForEntityByTypeAndStatus(userId, eType, eStatus);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getCurrentUserAllOutgoingEvents() {
        String userId = securityContext.getCurrentUser();
        try {
            return query.getAllOutgoingEventsForEntity(userId);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getCurrentUserOutgoingEventsByTypeAndStatus(String type, String status) {
        EventType eType = validateEventType(type);
        EventStatus eStatus = validateEventStatus(status);
        String userId = securityContext.getCurrentUser();
        try {
            return query.getOutgoingEventsForEntityByTypeAndStatus(userId, eType, eStatus);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getOrganizationIncomingEvents(String organizationId) {
        OrganizationBean org = getOrg(organizationId);
        try {
            return query.getAllIncomingEventsForEntity(org.getId());
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getOrganizationIncomingEventsByTypeAndStatus(String organizationId, String type, String status) {
        EventType eType = validateEventType(type);
        EventStatus eStatus = validateEventStatus(status);
        OrganizationBean org = getOrg(organizationId);
        try {
            return query.getIncomingEventsForEntityByTypeAndStatus(org.getId(), eType, eStatus);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getOrganizationOutgoingEvents(String organizationId) {
        OrganizationBean org = getOrg(organizationId);
        try {
            return query.getAllOutgoingEventsForEntity(org.getId());
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getOrganizationOutgoingEventsByTypeAndStatus(String organizationId, String type, String status) {
        EventType eType = validateEventType(type);
        EventStatus eStatus = validateEventStatus(status);
        OrganizationBean org = getOrg(organizationId);
        try {
            return query.getIncomingEventsForEntityByTypeAndStatus(org.getId(), eType, eStatus);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public void deleteCurrentUserEvent(Long id) {
        String userId = securityContext.getCurrentUser();
        try {
            EventBean event = storage.getEvent(id);
            if (event == null) {
                throw ExceptionFactory.eventNotFoundException();
            }
            if (!event.getDestination().equals(userId)) {
                throw ExceptionFactory.notAuthorizedException();
            }
            if (event.getStatus() == EventStatus.Pending) {
                throw ExceptionFactory.invalidEventStatusException(PENDING);
            }
            storage.deleteEvent(event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public void deleteOrganizationEvent(String organizationId, Long id) {
        OrganizationBean org = getOrg(organizationId);
        try {
            EventBean event = storage.getEvent(id);
            if (event == null) {
                throw ExceptionFactory.eventNotFoundException();
            }
            if (!event.getDestination().equals(org.getId())) {
                throw ExceptionFactory.notAuthorizedException();
            }
            if (event.getStatus() == EventStatus.Pending) {
                throw ExceptionFactory.invalidEventStatusException(PENDING);
            }
            storage.deleteEvent(event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    /*****************/
    /* Evenlisteners */
    /*****************/

    public void onMemberShipRequest(@Observes @MembershipRequest NewEventBean bean) {
        createMembershipRequest(bean);
    }

    public void onMembershipRequestRejected(@Observes @MembershipRequestRejected NewEventBean bean) {
        rejectMembershipRequest(bean);
    }

    public void onMembershipRequestAccepted(@Observes @MembershipRequestAccepted NewEventBean bean) {
        acceptMembershipRequest(bean);
    }

    /*******************/
    /* Private Methods */
    /*******************/

    private EventBean acceptMembershipRequest(NewEventBean bean) {
        EventBean newEvent = new EventBean();
        newEvent.setOrigin(bean.getOrigin());
        newEvent.setDestination(bean.getDestination());
        newEvent.setType(EventType.Membership);
        newEvent.setStatus(EventStatus.Accepted);
        newEvent.setCreatedOn(new Date());
        try {
            EventBean pendingEvent = query.getEvent(bean.getDestination(), bean.getOrigin(), EventType.Membership);
            if (pendingEvent != null) {
                storage.deleteEvent(pendingEvent);
            }
            storage.createEvent(newEvent);
            _LOG.debug("Pending request deleted, accepted event created:{}", newEvent);
            return newEvent;
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
        newRequest.setOrigin(bean.getOrigin());
        newRequest.setDestination(bean.getDestination());
        newRequest.setCreatedOn(new Date());
        try {
            //In case there still is an extant event marking the request as refused, delete it
            //Here origin and destination are reversed, because rejection event is from organization to user
            EventBean event = query.getEvent(bean.getDestination(), bean.getOrigin(), bean.getType());
            if (event != null && event.getStatus() == EventStatus.Rejected) {
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

    private void rejectMembershipRequest(NewEventBean bean) {
        EventBean event = new EventBean();
        event.setOrigin(bean.getOrigin());
        event.setDestination(bean.getDestination());
        event.setType(bean.getType());
        event.setStatus(EventStatus.Rejected);
        event.setCreatedOn(new Date());
        try {
            //delete the pending request
            EventBean request = query.getEvent(bean.getDestination(), bean.getOrigin(), EventType.Membership);
            storage.deleteEvent(request);
            storage.createEvent(event);
            _LOG.debug("Rejected membership request deleted, response created:{}", event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private EventType validateEventType(String type) {
        switch (capitalizeString(type)) {
            case MEMBERSHIP:
                return EventType.Membership;
            case CONTRACT:
                return EventType.Contract;
            default:
                throw ExceptionFactory.invalidEventException("Invalid event type");
        }
    }

    private EventStatus validateEventStatus(String status) {
        switch (capitalizeString(status)) {
            case PENDING:
                return EventStatus.Pending;
            case ACCEPTED:
                return EventStatus.Accepted;
            case REJECTED:
                return EventStatus.Rejected;
            default:
                throw ExceptionFactory.invalidEventStatusException("Invalid event status");
        }
    }

    private String capitalizeString(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

    private OrganizationBean getOrg(String organizationId) {
        try {
            OrganizationBean org = storage.getOrganization(organizationId);
            if (org == null) {
                throw ExceptionFactory.organizationNotFoundException(organizationId);
            }
            return org;
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }
}