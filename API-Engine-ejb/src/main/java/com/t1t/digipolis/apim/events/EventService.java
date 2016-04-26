package com.t1t.digipolis.apim.events;

import com.t1t.digipolis.apim.beans.events.NewEventBean;
import com.t1t.digipolis.apim.beans.idm.Role;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.mail.RequestMembershipMailBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequest;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.facades.EventFacade;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.facades.UserFacade;
import com.t1t.digipolis.apim.mail.MailProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.member;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EventService implements DefaultEventProvider {

    private static final Logger _LOG = LoggerFactory.getLogger(EventService.class);

    @Inject
    private MailProvider mailProvider;
    @Inject
    private EventFacade eventFacade;
    @Inject
    private OrganizationFacade organizationFacade;
    @Inject
    private UserFacade userFacade;

    @Override
    public void onMemberShipRequest(@Observes @MembershipRequest NewEventBean bean) {
        //get organization
        OrganizationBean organizationBean = organizationFacade.get(bean.getRequestDestination());
        eventFacade.create(bean);
        UserBean userBean = userFacade.get(bean.getRequestOrigin());
        organizationFacade.listMembers(bean.getRequestDestination()).forEach(member -> {
            member.getRoles().forEach(role -> {
                if(role.getRoleName().toLowerCase().equals(Role.OWNER.toString().toLowerCase())){
                    //send email
                    try{
                        if(member.getUserId()!=null && !StringUtils.isEmpty(member.getEmail())){
                            RequestMembershipMailBean requestMembershipMailBean = new RequestMembershipMailBean();
                            requestMembershipMailBean.setTo(member.getEmail());
                            requestMembershipMailBean.setUserId(bean.getRequestOrigin());
                            requestMembershipMailBean.setUserMail(userBean.getEmail());
                            requestMembershipMailBean.setOrgName(organizationBean.getName());
                            requestMembershipMailBean.setOrgFriendlyName(organizationBean.getFriendlyName());
                            mailProvider.sendRequestMembership(requestMembershipMailBean);
                        }
                    }catch(Exception e){
                        _LOG.error("Error sending mail:{}",e.getMessage());
                    }
                }
            });
        });
    }
}