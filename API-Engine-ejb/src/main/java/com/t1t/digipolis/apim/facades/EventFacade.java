package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.events.*;
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
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
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



    public List<EventBean> getCurrentUserAllIncomingEvents() {
        return getIncomingEvents(securityContext.getCurrentUser());
    }

    public List<EventBean> getCurrentUserIncomingEventsByType(String type) {
        return getIncomingEventsByType(securityContext.getCurrentUser(), type);
    }

    public List<EventBean> getCurrentUserAllOutgoingEvents() {
        return getOutgoingEvents(securityContext.getCurrentUser());
    }

    public List<EventBean> getCurrentUserOutgoingEventsByType(String type) {
        return getOutgoingEventsByType(securityContext.getCurrentUser(), type);
    }

    public List<EventBean> getOrganizationIncomingEvents(String organizationId) {
        return filterIncomingOrganizationResults(getIncomingEvents(validateOrgId(organizationId)), organizationId);
    }

    public List<EventBean> getOrganizationOutgoingEvents(String organizationId) {
        return filterOutgoingOrganizationResults(getOutgoingEvents(validateOrgId(organizationId)), organizationId);
    }

    public List<EventBean> getOrganizationIncomingEventsByType(String organizationId, String type) {
        return filterIncomingOrganizationResults(getIncomingEventsByType(validateOrgId(organizationId), type), organizationId);
    }

    public List<EventBean> getOrganizationOutgoingEventsByType(String organizationId, String type) {
        return filterOutgoingOrganizationResults(getOutgoingEventsByType(validateOrgId(organizationId), type), organizationId);
    }

    public void deleteEvent(String destination, Long id) {
        try {
            EventBean event = storage.getEvent(id);
            if (event == null) {
                throw ExceptionFactory.eventNotFoundException();
            }
            if (!event.getDestinationId().equals(destination)) {
                throw ExceptionFactory.notAuthorizedException();
            }
            if (event.getType() == EventType.MEMBERSHIP_PENDING || event.getType() == EventType.CONTRACT_PENDING) {
                throw ExceptionFactory.invalidEventException(event.getType().toString());
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

    public void onNewEventBean(@Observes NewEventBean bean) {
        EventBean event = new EventBean();
        event.setOriginId(bean.getOriginId());
        event.setDestinationId(bean.getDestinationId());
        event.setType(bean.getType());
        event.setCreatedOn(new Date());
        event.setBody(bean.getBody());
        try {
            switch (bean.getType()) {
                case MEMBERSHIP_GRANTED:
                    deletePendingMembershipRequest(event);
                    break;
                case MEMBERSHIP_PENDING:
                    deleteMembershipRefusedEvent(event);
                    break;
                case MEMBERSHIP_REJECTED:
                    deletePendingMembershipRequest(event);
                    break;
                case CONTRACT_ACCEPTED:
                    break;
                case CONTRACT_PENDING:
                    break;
                case CONTRACT_REJECTED:
                    break;
            }
            storage.createEvent(event);
            _LOG.debug("Event created:{}", event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    /*******************/
    /* Private Methods */
    /*******************/

    private List<EventBean> getOutgoingEvents(String origin) {
        try {
            return query.getAllOutgoingEvents(origin);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private List<EventBean> getIncomingEvents(String destination) {
        try {
            return query.getAllIncomingEvents(destination);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private List<EventBean> getOutgoingEventsByType(String origin, String type) {
        try {
            return query.getOutgoingEventsByType(origin, getEventType(type));
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private List<EventBean> getIncomingEventsByType(String destination, String type) {
        try {
            return query.getIncomingEventsByType(destination, getEventType(type));
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private void deleteMembershipRefusedEvent(EventBean bean) throws StorageException {
        //In case there still is an extant event marking the request as refused, delete it
        //Here origin and destination are reversed, because rejection event is from organization to user
        EventBean event = query.getEventByOriginDestinationAndType(bean.getDestinationId(), bean.getOriginId(), EventType.MEMBERSHIP_REJECTED);
        if (event != null) {
            storage.deleteEvent(event);
        }
    }

    private void deletePendingMembershipRequest(EventBean bean) throws StorageException {
        EventBean pendingRequest = query.getEventByOriginDestinationAndType(bean.getDestinationId(), bean.getOriginId(), EventType.MEMBERSHIP_PENDING);
        if (pendingRequest == null) {
            throw ExceptionFactory.membershipRequestFailedException("Membership was never requested");
        }
        storage.deleteEvent(pendingRequest);
    }

    private EventType getEventType(String type) {
        try {
            return EventType.valueOf(type.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            throw ExceptionFactory.invalidEventException(type);
        }
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

    private String validateOrgId(String organizationId) {
        return new StringBuilder(getOrg(organizationId).getId())
                .append("%")
                .toString();
    }

    private List<EventBean> filterIncomingOrganizationResults(List<EventBean> events, String organizationId) {
        List<EventBean> filteredList = new ArrayList<>();
        events.forEach(event -> {
            if (event.getDestinationId().startsWith(new StringBuilder(organizationId).append(".").toString()) || event.getDestinationId().equals(organizationId)) {
                filteredList.add(event);
            }
        });
        return filteredList;
    }

    private List<EventBean> filterOutgoingOrganizationResults(List<EventBean> events, String organizationId) {
        List<EventBean> filteredList = new ArrayList<>();
        events.forEach(event -> {
            if (event.getOriginId().startsWith(new StringBuilder(organizationId).append(".").toString()) || event.getOriginId().equals(organizationId)) {
                filteredList.add(event);
            }
        });
        return filteredList;
    }
    /*
    private List<MembershipRequest> convertToMembershipRequests(List<EventBean> events) {
        List<MembershipRequest> membershipRequests =  new ArrayList<>();
        events.forEach(event -> {
            MembershipRequest request = new MembershipRequest(event);
            switch (event.getType()) {
                case MEMBERSHIP_PENDING:
                    request.setUserId(event.getOriginId());
                    request.setOrganizationId(event.getDestinationId());
                    break;
                case MEMBERSHIP_GRANTED:
                case MEMBERSHIP_REJECTED:
                    request.setUserId(event.getDestinationId());
                    request.setOrganizationId(event.getOriginId());
                    break;
            }
            membershipRequests.add(request);
        });
        return membershipRequests;
    }
    */
}