package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.events.*;
import com.t1t.digipolis.apim.beans.idm.CurrentUserBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.members.MemberBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.services.ServiceBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.PlanVersionSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.mail.MailService;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.t1t.digipolis.apim.beans.events.EventType.*;
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
    @Inject
    private UserFacade userFacade;
    @Inject
    private OrganizationFacade orgFacade;

    public EventBean get(Long id) {
        try {
            EventBean event = storage.getEvent(id);
            if (event != null) {
                return event;
            }
            else {
                throw ExceptionFactory.eventNotFoundException();
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventBean> getCurrentUserAllIncomingEvents() {
        return getIncomingEvents(securityContext.getCurrentUser());
    }

    public List<EventBean> getCurrentUserAllOutgoingEvents() {
        return getOutgoingEvents(securityContext.getCurrentUser());
    }

    public <T> List<T> getCurrentUserIncomingEventsByType(String type) {
        List<EventBean> events = getIncomingEventsByType(securityContext.getCurrentUser(), type);
        switch (getEventType(type)) {
            case MEMBERSHIP_GRANTED:
            case MEMBERSHIP_REJECTED:
                return convertToMembershipRequests(events);
            default:
                return null;
        }
    }

    public <T> List<T> getCurrentUserOutgoingEventsByType(String type) {
        List<EventBean> events = getOutgoingEventsByType(securityContext.getCurrentUser(), type);
        switch (getEventType(type)) {
            case CONTRACT_PENDING:
                return convertToMembershipRequests(events);
            default:
                return null;
        }
    }

    public List<EventBean> getOrganizationIncomingEvents(String organizationId) {
        return filterIncomingOrganizationResults(getIncomingEvents(validateOrgId(organizationId)), organizationId);
    }

    public List<EventBean> getOrganizationOutgoingEvents(String organizationId) {
        return filterOutgoingOrganizationResults(getOutgoingEvents(validateOrgId(organizationId)), organizationId);
    }

    public <T> List<T> getOrganizationIncomingEventsByType(String organizationId, String type) {
        List<EventBean> events = filterIncomingOrganizationResults(getIncomingEventsByType(validateOrgId(organizationId), type), organizationId);
        switch (getEventType(type)) {
            case MEMBERSHIP_PENDING:
                return convertToMembershipRequests(events);
            case CONTRACT_ACCEPTED:
            case CONTRACT_PENDING:
            case CONTRACT_REJECTED:
                return convertToContractRequests(events);
            default:
                return null;
        }
    }

    public <T> List<T> getOrganizationOutgoingEventsByType(String organizationId, String type) {
        List<EventBean> events = filterOutgoingOrganizationResults(getOutgoingEventsByType(validateOrgId(organizationId), type), organizationId);
        switch (getEventType(type)) {
            case MEMBERSHIP_GRANTED:
            case MEMBERSHIP_REJECTED:
                return convertToMembershipRequests(events);
            case CONTRACT_ACCEPTED:
            case CONTRACT_PENDING:
            case CONTRACT_REJECTED:
                return convertToContractRequests(events);
            default:
                return null;
        }
    }

    public void deleteUserEvent(Long id) {
        EventBean event = get(id);
        if (!event.getDestinationId().equals(securityContext.getCurrentUser())) {
            throw ExceptionFactory.notAuthorizedException();
        }
        deleteEventInternal(event);
    }

    public void deleteOrgEvent(String orgId, Long id) {
        EventBean event = get(id);
        //The only notifications an organization gets is of type CONTRACT_ACCEPTED or REJECTED, so we check if that's the type.
        //If it is, we can safely assume the destination follows org.serv.version convention
        if (event.getType() != CONTRACT_ACCEPTED || event.getType() != CONTRACT_REJECTED) {
            throw ExceptionFactory.invalidEventException(event.getType().toString());
        }
        String destinationOrg = event.getDestinationId().split(".", 2)[0];
        if (!orgId.equals(destinationOrg)) {
            throw ExceptionFactory.notAuthorizedException();
        }
        deleteEventInternal(event);
    }

    //In order to prevent users from deleting organization-wide event notifications, we'll need to check if the user is
    //the intended destination
    public void deleteEventInternal(EventBean event) {
        try {
            if (event.getType() == MEMBERSHIP_PENDING || event.getType() == EventType.CONTRACT_PENDING) {
                throw ExceptionFactory.invalidEventException("Pending events cannot be deleted");
            }
            storage.deleteEvent(event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventAggregateBean> getAllNonActionEvents(CurrentUserBean currentUser) {
        List<String> orgIds = new ArrayList<>();
        currentUser.getPermissions().forEach(permissionBean -> {
            if (permissionBean.getName() == PermissionType.orgEdit) {
                orgIds.add(permissionBean.getOrganizationId());
            }
        });
        try {

            List<EventBean> events = query.getAllIncomingNonActionEvents(currentUser.getUsername());
            orgIds.forEach(orgId -> {
                try {
                    events.addAll(filterIncomingOrganizationResults(query.getAllIncomingNonActionEvents(validateOrgId(orgId)), orgId));
                }
                catch (StorageException ex) {
                    throw new SystemErrorException(ex);
                }
            });
            return convertToAggregateBeans(events);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public List<EventAggregateBean> getAllIncomingActionEvents(CurrentUserBean currentUser) {
        List<String> orgIds = new ArrayList<>();
        currentUser.getPermissions().forEach(permissionBean -> {
            if (permissionBean.getName() == PermissionType.orgEdit) {
                orgIds.add(permissionBean.getOrganizationId());
            }
        });
        List<EventBean> events = new ArrayList<>();
        orgIds.forEach(orgId -> {
            try {
                events.addAll(filterIncomingOrganizationResults(query.getAllIncomingActionEvents(validateOrgId(orgId)), orgId));
            }
            catch (StorageException ex) {
                throw new SystemErrorException(ex);
            }
        });
        return convertToAggregateBeans(events);
    }

    /*****************/
    /* Evenlisteners */
    /*****************/
    //TODO provide mail service methods here insteaf of in facades - as result of an event (instead of implicitly in the facade)
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
                    deleteContractPending(event);
                    break;
                case CONTRACT_PENDING:
                    deleteContractRefusedEvent(event);
                    break;
                case CONTRACT_REJECTED:
                    deleteContractPending(event);
                    break;
            }
            //storage.createEvent(event);//not save - what if event exists already?
            verifyAndCreate(event);
            _LOG.debug("Event created:{}", event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    /**
     * Verifies if event already exists (origin, destination, type), and safely updates existing events.
     *
     * @param event
     */
    public void verifyAndCreate(EventBean event) throws StorageException {
        final EventBean uniqueEvent = query.getUniqueEvent(event);
        if(uniqueEvent!=null){
            event.setId(uniqueEvent.getId());
            storage.updateEvent(event);
        }else storage.createEvent(event);
    }

    public void onNewAnnouncement(@Observes AnnouncementBean announcement) {
        //Workaround for WELD-2019 issue
        handleNewAnnouncement(announcement);
    }

    /*******************/
    /* Private Methods */
    /*******************/

    private void handleNewAnnouncement(AnnouncementBean announcement) {
        try {
            ServiceBean service = storage.getService(announcement.getOrganizationId(), announcement.getServiceId());
            Set<OrganizationBean> contractHolders = query.getServiceContractHolders(service);
            Set<String> members = new HashSet<>();
            //Get members for each organization and add them to the set.
            contractHolders.forEach(org -> members.addAll(orgFacade.listMembers(org.getId()).stream().map(MemberBean::getUserId).collect(Collectors.toList())));
            //Add all of the service followers to the list, and remove the user that created the announcement from that list (if present)
            members.addAll(service.getFollowers());
            members.remove(announcement.getCreatedBy());
            _LOG.debug("listOfMembers:{}", members);
            //For each member in the list, create a new event
            members.forEach(member -> {
                EventBean event = new EventBean();
                event.setOriginId(new StringBuilder(service.getOrganization().getId())
                        .append(".")
                        .append(service.getId())
                        .toString());
                event.setDestinationId(member);
                event.setBody(announcement.getDescription());
                event.setCreatedOn(new Date());
                event.setType(ANNOUNCEMENT_NEW);
                //Check if there still is a previous announcement from that service to that user, and delete it if necessary
                try {
                    verifyAndCreate(event);
                }
                catch (StorageException ex) {
                    throw new SystemErrorException(ex);
                }
            });
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

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

    private void deleteContractPending(EventBean bean) throws StorageException {
        EventBean pendingEvent =  query.getEventByOriginDestinationAndType(bean.getDestinationId(), bean.getOriginId(), CONTRACT_PENDING);
        if (pendingEvent == null) {
            if (bean.getType() == EventType.CONTRACT_REJECTED) {
                throw ExceptionFactory.contractRequestFailedException("Contract never requested");
            }
        }
        else {
            storage.deleteEvent(pendingEvent);
        }
    }

    private void deleteContractRefusedEvent(EventBean bean) throws StorageException {
        EventBean event = query.getEventByOriginDestinationAndType(bean.getDestinationId(), bean.getOriginId(), CONTRACT_REJECTED);
        if (event != null) {
            storage.deleteEvent(event);
        }
    }

    private void deleteMembershipRefusedEvent(EventBean bean) throws StorageException {
        //In case there still is an existant event marking the request as refused, delete it
        //Here origin and destination are reversed, because rejection event is from organization to user
        EventBean event = query.getEventByOriginDestinationAndType(bean.getDestinationId(), bean.getOriginId(), EventType.MEMBERSHIP_REJECTED);
        if (event != null) {
            storage.deleteEvent(event);
        }
    }

    private void deletePendingMembershipRequest(EventBean bean) throws StorageException {
        EventBean pendingRequest = query.getEventByOriginDestinationAndType(bean.getDestinationId(), bean.getOriginId(), MEMBERSHIP_PENDING);
        if (pendingRequest != null) {
            storage.deleteEvent(pendingRequest);
        }
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

    private <T> List<T> convertToMembershipRequests(List<EventBean> events) {
        List<T> membershipRequests =  new ArrayList<>();
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
            membershipRequests.add((T) request);
        });
        return membershipRequests;
    }

    private <T> List<T> convertToContractRequests(List<EventBean> events) {
        List<T> contractRequests = new ArrayList<>();
        events.forEach(event -> {
            ContractRequest request = new ContractRequest(event);
            switch (event.getType()) {
                case CONTRACT_PENDING:
                    contractRequests.add((T) setAppAndService(request, event.getOriginId(), event.getDestinationId()));
                    break;
                case CONTRACT_ACCEPTED:
                case CONTRACT_REJECTED:
                    contractRequests.add((T) setAppAndService(request, event.getDestinationId(), event.getOriginId()));
                    break;
            }
        });
        return contractRequests;
    }

    private ContractRequest setAppAndService(ContractRequest request, String appVersion, String svcVersion) {
        String[] app = appVersion.split("\\.");
        String[] svc = svcVersion.split("\\.");
        request.setAppOrg(app[0]);
        request.setAppId(app[1]);
        request.setAppVersion(app[2]);
        request.setServiceOrg(svc[0]);
        request.setServiceId(svc[1]);
        request.setServiceVersion(svc[2]);
        return request;
    }

    private List<EventAggregateBean> convertToAggregateBeans(List<EventBean> events) {
        List<EventAggregateBean> eabs =  new ArrayList<>();
        Gson gson = new Gson();
        events.forEach(event -> {
            EventAggregateBean eab = new EventAggregateBean();
            eab.setId(event.getId());
            eab.setCreatedOn(event.getCreatedOn());
            eab.setType(event.getType());
            eab.setBody(event.getBody());
            switch (event.getType()) {
                case MEMBERSHIP_GRANTED:
                case MEMBERSHIP_REJECTED:
                    UserBean user = userFacade.get(event.getDestinationId());
                    eab.setUserId(event.getDestinationId());
                    eab.setFullName(user.getFullName());
                    OrganizationBean org = getOrg(event.getOriginId());
                    eab.setOrganizationName(org.getName());
                    eab.setFriendlyName(org.getFriendlyName());
                    eab.setOrganizationId(org.getId());

                    eabs.add(eab);
                    break;
                case CONTRACT_ACCEPTED:
                case CONTRACT_REJECTED:
                    String[] app = event.getDestinationId().split("\\.");
                    String[] svc = event.getOriginId().split("\\.");

                    ApplicationVersionBean avb = null;
                    ServiceVersionBean svb = null;
                    try {
                        avb = storage.getApplicationVersion(app[0], app[1], app[2]);
                        svb = storage.getServiceVersion(svc[0], svc[1], svc[2]);
                        if (avb == null || svb == null) {
                            storage.deleteEvent(event);
                        }
                        else {
                            eabs.add(finishEventAggregateBeanCreation(eab, avb, svb));
                        }
                    }
                    catch (StorageException ex) {
                        throw new SystemErrorException(ex);
                    }
                    break;
                case CONTRACT_PENDING:
                    app = event.getOriginId().split("\\.");
                    svc = event.getDestinationId().split("\\.");

                    avb = null;
                    svb = null;
                    try {
                        avb = storage.getApplicationVersion(app[0], app[1], app[2]);
                        svb = storage.getServiceVersion(svc[0], svc[1], svc[2]);
                        if (avb == null || svb == null) {
                            storage.deleteEvent(event);
                        }
                        else {
                            PlanVersionSummaryBean pvsb = gson.fromJson(eab.getBody(), PlanVersionSummaryBean.class);
                            eab.setPlanId(pvsb.getId());
                            eab.setPlanName(pvsb.getName());
                            eab.setPlanVersion(pvsb.getVersion());

                            eabs.add(finishEventAggregateBeanCreation(eab, avb, svb));
                        }
                    }
                    catch (StorageException ex) {
                        throw new SystemErrorException(ex);
                    }
                    break;
                case MEMBERSHIP_PENDING:
                    user = userFacade.get(event.getOriginId());
                    eab.setUserId(event.getOriginId());
                    eab.setFullName(user.getFullName());
                    org = getOrg(event.getDestinationId());
                    eab.setOrganizationName(org.getName());
                    eab.setFriendlyName(org.getFriendlyName());
                    eab.setOrganizationId(org.getId());
                    eabs.add(eab);
                    break;
            }
        });
        return eabs;
    }

    private EventAggregateBean finishEventAggregateBeanCreation(EventAggregateBean eab, ApplicationVersionBean avb, ServiceVersionBean svb) {

        eab.setApplicationOrgId(avb.getApplication().getOrganization().getId());
        eab.setApplicationOrgName(avb.getApplication().getOrganization().getName());
        eab.setApplicationOrgFriendlyName(avb.getApplication().getOrganization().getFriendlyName());
        eab.setApplicationId(avb.getApplication().getId());
        eab.setApplicationName(avb.getApplication().getName());
        eab.setApplicationVersion(avb.getVersion());

        eab.setServiceOrgId(svb.getService().getOrganization().getId());
        eab.setServiceOrgName(svb.getService().getOrganization().getName());
        eab.setServiceOrgFriendlyName(svb.getService().getOrganization().getFriendlyName());
        eab.setServiceId(svb.getService().getId());
        eab.setServiceName(svb.getService().getName());
        eab.setServiceVersion(svb.getVersion());

        return eab;
    }
}