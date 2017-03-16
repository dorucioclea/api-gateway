package com.t1t.apim.facades;

import com.google.gson.Gson;
import com.t1t.apim.beans.announcements.AnnouncementBean;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.events.*;
import com.t1t.apim.beans.idm.CurrentUserBean;
import com.t1t.apim.beans.idm.PermissionType;
import com.t1t.apim.beans.idm.Role;
import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.beans.mail.*;
import com.t1t.apim.beans.members.MemberBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.services.ServiceBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.PlanVersionSummaryBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.mail.MailService;
import com.t1t.apim.security.ISecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.t1t.apim.beans.events.EventType.*;

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
    @Inject
    private MailService mailService;

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

    public EventBean get(String origin, String dest, EventType type) {
        try {
            EventBean rval = query.getEventByOriginDestinationAndType(origin, dest, type);
            if (rval != null) {
                return rval;
            }
            else {
                throw ExceptionFactory.eventNotFoundException();
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
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
            case MEMBERSHIP_REVOKED_ROLE:
                return convertToMembershipRequests(events);
            default:
                return null;
        }
    }

    public <T> List<T> getCurrentUserOutgoingEventsByType(String type) {
        List<EventBean> events = getOutgoingEventsByType(securityContext.getCurrentUser(), type);
        switch (getEventType(type)) {
            case CONTRACT_PENDING:
                return convertToContractRequests(events);
            case MEMBERSHIP_PENDING:
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
            case MEMBERSHIP_REQUEST_CANCELLED:
                return convertToMembershipRequests(events);
            case CONTRACT_ACCEPTED:
            case CONTRACT_PENDING:
            case CONTRACT_REJECTED:
            case CONTRACT_REQUEST_CANCELLED:
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
        deleteEvent(event);
    }

    public void deleteOrgEvent(String orgId, Long id) {
        EventBean event = get(id);
        //The only notifications an organization gets is of type CONTRACT_ACCEPTED or REJECTED, so we check if that's the type.
        //If it is, we can safely assume the destination follows org.serv.version convention
        switch (event.getType()) {
            case CONTRACT_ACCEPTED:
            case CONTRACT_REJECTED:
            case CONTRACT_REQUEST_CANCELLED:
            case MEMBERSHIP_REQUEST_CANCELLED:
                String destinationOrg = event.getDestinationId().split("\\.", 2)[0];
                if (!orgId.equals(destinationOrg)) {
                    throw ExceptionFactory.notAuthorizedException();
                }
                deleteEvent(event);
                break;
            default:
                throw ExceptionFactory.invalidEventException(event.getType().toString());
        }
    }

    //In order to prevent users from deleting organization-wide event notifications, we'll need to check if the user is
    //the intended destination
    public void deleteEvent(EventBean event) {
        if (event.getType() == MEMBERSHIP_PENDING || event.getType() == CONTRACT_PENDING) {
            throw ExceptionFactory.invalidEventException("Pending events cannot be deleted");
        }
        deleteEventInternal(event);
    }

    private void deleteEventInternal(EventBean event) {
        try {
            storage.deleteEvent(event);
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
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

    public void deleteAllEvents(CurrentUserBean currentUser) {
        List<String> orgIds = new ArrayList<>();
        currentUser.getPermissions().forEach(permissionBean -> {
            if (permissionBean.getName() == PermissionType.orgAdmin) {
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
            for (EventBean event : events) {
                deleteEvent(event);
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
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

            //Get the original body so we know what plan the contract was accepted under
            String origin = null;
            String dest = null;
            switch (event.getType()) {
                case CONTRACT_ACCEPTED:
                case CONTRACT_REJECTED:
                    origin = bean.getDestinationId();
                    dest = bean.getOriginId();
                    break;
                case CONTRACT_REQUEST_CANCELLED:
                    origin = bean.getOriginId();
                    dest = bean.getDestinationId();
                    break;
                default:
                    //do nothing
                    break;
            }
            if (origin != null && dest != null) {
                event.setBody(query.getEventByOriginDestinationAndType(origin, dest, CONTRACT_PENDING).getBody());
            }

            eventCleanup(event);

            verifyAndCreate(event);
            _LOG.debug("Event created:{}", event);
            sendMail(event);
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    public void onNewAnnouncement(@Observes AnnouncementBean announcement) {
        //Workaround for WELD-2019 issue
        handleNewAnnouncement(announcement);
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

    /*******************/
    /* Private Methods */
    /*******************/

    private void eventCleanup(EventBean event) {
        String origin = null;
        String destination = null;
        EventType type = null;
        try {
            switch (event.getType()) {
                case MEMBERSHIP_REJECTED:
                case MEMBERSHIP_GRANTED:
                    origin = event.getDestinationId();
                    destination = event.getOriginId();
                    type = MEMBERSHIP_PENDING;
                    break;
                case MEMBERSHIP_REQUEST_CANCELLED:
                    origin = event.getOriginId();
                    destination = event.getDestinationId();
                    type = MEMBERSHIP_PENDING;
                    break;
                case MEMBERSHIP_PENDING:
                    origin = event.getDestinationId();
                    destination = event.getOriginId();
                    type = MEMBERSHIP_REJECTED;
                    break;
                case CONTRACT_ACCEPTED:
                case CONTRACT_REJECTED:
                    origin = event.getDestinationId();
                    destination = event.getOriginId();
                    type = CONTRACT_PENDING;
                    break;
                case CONTRACT_REQUEST_CANCELLED:
                    origin = event.getOriginId();
                    destination = event.getDestinationId();
                    type = CONTRACT_PENDING;
                    break;
                case CONTRACT_PENDING:
                    origin = event.getDestinationId();
                    destination = event.getOriginId();
                    type = CONTRACT_REJECTED;
                    break;
                default:
                    //Do nothing
                    break;
            }
            if (origin != null && destination != null && type != null) {
                deleteEventByOriginDestinationAndType(origin, destination, type);
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void deleteEventByOriginDestinationAndType(String origin, String destination, EventType type) throws StorageException {
        EventBean event = query.getEventByOriginDestinationAndType(origin, destination, type);
        if (event != null) {
            storage.deleteEvent(event);
        }
    }

    private void handleNewAnnouncement(AnnouncementBean announcement) {
        try {
            ServiceBean service = orgFacade.getService(announcement.getOrganizationId(), announcement.getServiceId());
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
                event.setBody(announcement.getId().toString());
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

    private EventType getEventType(String type) {
        try {
            return valueOf(type.toUpperCase());
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
                default:
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
                default:
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
                case MEMBERSHIP_REVOKED:
                case MEMBERSHIP_REVOKED_ROLE:
                case MEMBERSHIP_TRANSFER:
                case MEMBERSHIP_UPDATED:
                case ADMIN_GRANTED:
                case ADMIN_REVOKED:
                    UserBean user = userFacade.get(event.getDestinationId());
                    eab.setUserId(event.getDestinationId());
                    eab.setFullName(user.getFullName());
                    switch (event.getType()) {
                        case ADMIN_GRANTED:
                        case ADMIN_REVOKED:
                            eab.setAdminUserId(event.getOriginId());
                            break;
                        case MEMBERSHIP_GRANTED:
                        case MEMBERSHIP_REJECTED:
                        case MEMBERSHIP_REVOKED:
                        case MEMBERSHIP_REVOKED_ROLE:
                        case MEMBERSHIP_TRANSFER:
                        case MEMBERSHIP_UPDATED:
                            OrganizationBean org = getOrg(event.getOriginId());
                            eab.setOrganizationName(org.getName());
                            eab.setFriendlyName(org.getFriendlyName());
                            eab.setOrganizationId(org.getId());
                            switch (event.getType()) {
                                case MEMBERSHIP_TRANSFER:
                                    eab.setCurrentOwnerId(event.getBody());
                                    break;
                                default:
                                    eab.setRole(event.getBody());
                                    break;
                            }
                            break;
                    }
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
                            eabs.add(finishEventAggregateBeanCreation(eab, avb, svb, null));
                        }
                    }
                    catch (StorageException ex) {
                        throw new SystemErrorException(ex);
                    }
                    break;
                case CONTRACT_PENDING:
                case CONTRACT_REQUEST_CANCELLED:
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

                            eabs.add(finishEventAggregateBeanCreation(eab, avb, svb, null));
                        }
                    }
                    catch (StorageException ex) {
                        throw new SystemErrorException(ex);
                    }
                    break;
                case MEMBERSHIP_PENDING:
                case MEMBERSHIP_REQUEST_CANCELLED:
                    user = userFacade.get(event.getOriginId());
                    eab.setUserId(event.getOriginId());
                    eab.setFullName(user.getFullName());
                    OrganizationBean org = getOrg(event.getDestinationId());
                    eab.setOrganizationName(org.getName());
                    eab.setFriendlyName(org.getFriendlyName());
                    eab.setOrganizationId(org.getId());
                    eabs.add(eab);
                    break;
                case ANNOUNCEMENT_NEW:
                    user = userFacade.get(event.getDestinationId());
                    eab.setUserId(event.getDestinationId());
                    eab.setFullName(user.getFullName());
                    String[] split = event.getOriginId().split("\\.");
                    if (split.length == 2) {
                        try {
                            ServiceBean service = storage.getService(split[0], split[1]);
                            eab.setServiceId(service.getId());
                            eab.setServiceName(service.getName());
                            eab.setServiceOrgId(service.getOrganization().getId());
                            eab.setServiceOrgName(service.getOrganization().getName());
                            eab.setServiceOrgFriendlyName(service.getOrganization().getFriendlyName());
                            eab.setAnnouncementId(Long.valueOf(event.getBody()));
                            eabs.add(finishEventAggregateBeanCreation(eab, null, null, service));
                        }
                        catch (StorageException ex) {
                            throw ExceptionFactory.systemErrorException(ex);
                        }
                    }
                    break;
                    //TODO - events for new announcements
            }
        });
        return eabs;
    }

    private EventAggregateBean finishEventAggregateBeanCreation(EventAggregateBean eab, ApplicationVersionBean avb, ServiceVersionBean svb, ServiceBean sb) {

        if (avb != null) {
            eab.setApplicationOrgId(avb.getApplication().getOrganization().getId());
            eab.setApplicationOrgName(avb.getApplication().getOrganization().getName());
            eab.setApplicationOrgFriendlyName(avb.getApplication().getOrganization().getFriendlyName());
            eab.setApplicationId(avb.getApplication().getId());
            eab.setApplicationName(avb.getApplication().getName());
            eab.setApplicationVersion(avb.getVersion());
        }

        if (svb != null) {
            eab.setServiceOrgId(svb.getService().getOrganization().getId());
            eab.setServiceOrgName(svb.getService().getOrganization().getName());
            eab.setServiceOrgFriendlyName(svb.getService().getOrganization().getFriendlyName());
            eab.setServiceId(svb.getService().getId());
            eab.setServiceName(svb.getService().getName());
            eab.setServiceVersion(svb.getVersion());
        }

        if (sb != null) {
            eab.setServiceOrgId(sb.getOrganization().getId());
            eab.setServiceOrgName(sb.getOrganization().getName());
            eab.setServiceOrgFriendlyName(sb.getOrganization().getFriendlyName());
            eab.setServiceId(sb.getId());
            eab.setServiceName(sb.getName());
        }

        return eab;
    }

    private String getOrgIdFromUniqueId(String UID) {
        String[] temp = UID.split("\\.");
        if (temp.length != 3) {
            throw ExceptionFactory.invalidNameException("Not a valid UID");
        }
        return temp[0];
    }

    private void sendMail(EventBean event) {
        try {
            switch (event.getType()) {
                case CONTRACT_PENDING:
                case CONTRACT_REQUEST_CANCELLED:
                case CONTRACT_ACCEPTED:
                case CONTRACT_REJECTED:
                    sendMailToOwners(getOrgIdFromUniqueId(event.getDestinationId()), event);
                    break;
                case MEMBERSHIP_PENDING:
                case MEMBERSHIP_REQUEST_CANCELLED:
                    sendMailToOwners(event.getDestinationId(), event);
                    break;
                case MEMBERSHIP_GRANTED:
                case MEMBERSHIP_REJECTED:
                case MEMBERSHIP_REVOKED_ROLE:
                case MEMBERSHIP_UPDATED:
                case MEMBERSHIP_REVOKED:
                case MEMBERSHIP_TRANSFER:
                case ADMIN_GRANTED:
                case ADMIN_REVOKED:
                    sendMailToUser(event.getDestinationId(), event);
                    break;
                case ANNOUNCEMENT_NEW:
                    //TODO - send mail when a new announcement is created
                    break;
                default:
                    //Do nothing
                    break;
            }
        }
        catch (Exception ex) {
            _LOG.error("Error sending mail:{}", ex.getMessage());
        }
    }

    private void sendMailToOwners(String destinationOrgId, EventBean event) {
        orgFacade.listMembers(destinationOrgId).forEach(member -> member.getRoles().forEach(role -> {
            if (role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase()) && member.getUserId() != null && !StringUtils.isEmpty(member.getEmail())) {//only owners
                switch (event.getType()) {
                    case MEMBERSHIP_PENDING:
                    case MEMBERSHIP_REQUEST_CANCELLED:
                        OrganizationBean org = orgFacade.get(destinationOrgId);
                        MembershipRequestMailBean membershipRequestMailBean = new MembershipRequestMailBean();
                        membershipRequestMailBean.setTo(member.getEmail());
                        membershipRequestMailBean.setUserId(securityContext.getCurrentUser());
                        membershipRequestMailBean.setUserMail(securityContext.getEmail());
                        membershipRequestMailBean.setOrgName(org.getName());
                        membershipRequestMailBean.setOrgFriendlyName(org.getFriendlyName());
                        switch (event.getType()) {
                            case MEMBERSHIP_REQUEST_CANCELLED:
                                mailService.cancelMembershipRequest(membershipRequestMailBean);
                                break;
                            case MEMBERSHIP_PENDING:
                                mailService.sendRequestMembership(membershipRequestMailBean);
                                break;
                        }
                        break;
                    case CONTRACT_PENDING:
                    case CONTRACT_REQUEST_CANCELLED:
                    case CONTRACT_REJECTED:
                    case CONTRACT_ACCEPTED:
                        ContractMailBean contractMailBean = new ContractMailBean();
                        contractMailBean.setTo(member.getEmail());
                        contractMailBean.setUserId(securityContext.getCurrentUser());
                        contractMailBean.setUserMail(securityContext.getEmail());

                        ApplicationVersionBean avb = null;
                        ServiceVersionBean svb = null;

                        switch (event.getType()) {
                            case CONTRACT_PENDING:
                            case CONTRACT_REQUEST_CANCELLED:
                                avb = orgFacade.getApplicationVersionByUniqueId(event.getOriginId());
                                svb = orgFacade.getServiceVersionByUniqueId(event.getDestinationId());
                                break;
                            case CONTRACT_ACCEPTED:
                            case CONTRACT_REJECTED:
                                avb = orgFacade.getApplicationVersionByUniqueId(event.getDestinationId());
                                svb = orgFacade.getServiceVersionByUniqueId(event.getOriginId());
                                break;
                        }
                        contractMailBean.setAppOrgName(avb.getApplication().getOrganization().getName());
                        contractMailBean.setAppName(avb.getApplication().getName());
                        contractMailBean.setAppVersion(avb.getVersion());
                        contractMailBean.setServiceOrgName(svb.getService().getOrganization().getName());
                        contractMailBean.setServiceName(svb.getService().getName());
                        contractMailBean.setServiceVersion(svb.getVersion());
                        Gson gson = new Gson();
                        PlanVersionSummaryBean pvsb = gson.fromJson(event.getBody(), PlanVersionSummaryBean.class);
                        contractMailBean.setPlanName(pvsb.getName());
                        contractMailBean.setPlanVersion(pvsb.getVersion());
                        switch (event.getType()) {
                            case CONTRACT_PENDING:
                                mailService.sendContractRequest(contractMailBean);
                                break;
                            case CONTRACT_REQUEST_CANCELLED:
                                mailService.cancelContractRequest(contractMailBean);
                                break;
                            case CONTRACT_REJECTED:
                                mailService.rejectContractRequest(contractMailBean);
                                break;
                            case CONTRACT_ACCEPTED:
                                mailService.approveContractRequest(contractMailBean);
                                break;
                        }
                        break;
                    default:
                        //Do nothing
                        break;
                }
            }
        }));
    }

    private void sendMailToUser(String userId, EventBean event) {
        OrganizationBean org = null;
        switch (event.getType()) {
            case ADMIN_GRANTED:
            case ADMIN_REVOKED:
                break;
            default:
                org = orgFacade.get(event.getOriginId());
        }
        UserBean user = userFacade.get(userId);
        if (user != null && !StringUtils.isEmpty(user.getEmail())) {
            switch (event.getType()) {
                case MEMBERSHIP_GRANTED:
                case MEMBERSHIP_REJECTED:
                    MembershipRequestMailBean bean =  new MembershipRequestMailBean();
                    bean.setTo(user.getEmail());
                    bean.setOrgFriendlyName(org.getFriendlyName());
                    bean.setOrgName(org.getName());
                    switch (event.getType()) {
                        case MEMBERSHIP_GRANTED:
                            mailService.approveRequestMembership(bean);
                            break;
                        case MEMBERSHIP_REJECTED:
                            mailService.rejectRequestMembership(bean);
                            break;
                    }
                    break;
                case MEMBERSHIP_REVOKED:
                case MEMBERSHIP_REVOKED_ROLE:
                case MEMBERSHIP_UPDATED:
                case MEMBERSHIP_TRANSFER:
                    /*String roleName = null;
                    if (!StringUtils.isEmpty(event.getBody())) {
                        roleName = roleFacade.get(event.getBody()).getName();
                    }*/
                    UpdateMemberMailBean updateMemberMailBean = new UpdateMemberMailBean();
                    updateMemberMailBean.setTo(user.getEmail());
                    updateMemberMailBean.setOrgName(org.getName());
                    updateMemberMailBean.setOrgFriendlyName(org.getFriendlyName());
                    updateMemberMailBean.setRole(Role.OWNER.toString().toLowerCase());
                    switch (event.getType()) {
                        case MEMBERSHIP_REVOKED_ROLE:
                        case MEMBERSHIP_REVOKED:
                            updateMemberMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                            break;
                        case MEMBERSHIP_UPDATED:
                            updateMemberMailBean.setMembershipAction(MembershipAction.UPDATE_ROLE);
                            break;
                        case MEMBERSHIP_TRANSFER:
                            updateMemberMailBean.setMembershipAction(MembershipAction.TRANSFER);
                            break;
                    }
                    mailService.sendUpdateMember(updateMemberMailBean);
                    break;
                case ADMIN_GRANTED:
                case ADMIN_REVOKED:
                    UpdateAdminMailBean updateAdminMailBean = new UpdateAdminMailBean();
                    updateAdminMailBean.setTo(user.getEmail());
                    switch (event.getType()) {
                        case ADMIN_GRANTED:
                            updateAdminMailBean.setMembershipAction(MembershipAction.NEW_MEMBERSHIP);
                            break;
                        case ADMIN_REVOKED:
                            updateAdminMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                            break;
                    }
                    mailService.sendUpdateAdmin(updateAdminMailBean);
                    break;
                default:
                    //Do nothing
                    break;
            }
        }
    }
}