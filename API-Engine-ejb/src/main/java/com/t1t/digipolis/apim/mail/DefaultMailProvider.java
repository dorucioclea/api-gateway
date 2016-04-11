package com.t1t.digipolis.apim.mail;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.mail.RequestMembershipMailBean;
import com.t1t.digipolis.apim.beans.mail.StatusMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateAdminMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateMemberMailBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.mail.*;
import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Simple mailprovider, no templating and no dynamic substitutions.
 * For a straight forward impl, only text messages supported, with inline replacements.
 * For this first implementation the mail beans, doesn't carry content with them. The content is provided hard-coded in
 * the provided methods.
 * TODO: introduce string replacement, templating etc.
 */
@ApplicationScoped
@Default
public class DefaultMailProvider implements MailProvider {
    private final static Logger _LOG = LoggerFactory.getLogger(DefaultMailProvider.class.getName());
    @Resource(mappedName="java:jboss/mail/Default")
    private Session mailSession;
    @Inject private AppConfig config;

    @PostConstruct
    public void init(){
        mailSession.setDebug(config.getNotificationsEnableDebug());
    }

    public void sendTestMail(){
        sendMail(composeMessage(config.getNotificationStartupMail(),"API Engine mail test","This is a test message - you can ignore the content."));
    }

    @Override
    public void sendStatusMail(StatusMailBean statusMailBean) {
        statusMailBean.setSubject("API Engine - status mail");
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("Not yet implemented");

        //set content
        statusMailBean.setContent(sContent.toString());
        sendMail(composeMessage(statusMailBean.getTo(),statusMailBean.getSubject(),statusMailBean.getContent()));
    }

    @Override
    public void sendRequestMembership(RequestMembershipMailBean requestMembershipMailBean) {
        requestMembershipMailBean.setSubject("API Engine - request membership for "+requestMembershipMailBean.getOrgFriendlyName());
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("The following user requests membership for your organization: ");
        sContent.append(requestMembershipMailBean.getOrgFriendlyName());
        sContent.append("("+requestMembershipMailBean.getOrgName()+")");
        sContent.append("\n- Username: "+ requestMembershipMailBean.getUserId());
        sContent.append("\n- Email   : " + requestMembershipMailBean.getUserMail());
        sContent.append("\n\nYou can add the user in the 'Members'-tab of your organization.");
        sContent.append(getMailSignature());
        //set content
        requestMembershipMailBean.setContent(sContent.toString());
        sendMail(composeMessage(requestMembershipMailBean.getTo(),requestMembershipMailBean.getSubject(),requestMembershipMailBean.getContent()));
    }

    @Override
    public void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean) {
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("Your organization profile has been updated for organization: ");
        sContent.append(updateMemberMailBean.getOrgFriendlyName());
        sContent.append(" ("+updateMemberMailBean.getOrgName()+")");
        switch(updateMemberMailBean.getMembershipAction()){
            case NEW_MEMBERSHIP:{
                updateMemberMailBean.setSubject("API Engine - welcome to "+updateMemberMailBean.getOrgFriendlyName());
                sContent.append("\nYou have been added as a new member for the organization.");
                sContent.append("\nYou have been assigned with the role '"+updateMemberMailBean.getRole()+"'");
                break;
            }
            case TRANSFER:{
                updateMemberMailBean.setSubject("API Engine - transfer ownership "+updateMemberMailBean.getOrgFriendlyName());
                sContent.append("\nYou have been assigned as owner of the organization.");
                break;
            }
            case DELETE_MEMBERSHIP:{
                updateMemberMailBean.setSubject("API Engine - membership deleted");
                sContent.append("\nYou have been removed from the organization.");
                break;
            }
            case UPDATE_ROLE:{
                updateMemberMailBean.setSubject("API Engine - updated role");
                sContent.append("\nYour role has been changed to '"+updateMemberMailBean.getRole()+"'.");
                break;
            }
            default:{
                updateMemberMailBean.setSubject("API Engine - verify account");
                sContent.append("Verify your account.");
            }
        }
        sContent.append(getMailSignature());
        //set content
        updateMemberMailBean.setContent(sContent.toString());
        sendMail(composeMessage(updateMemberMailBean.getTo(),updateMemberMailBean.getSubject(),updateMemberMailBean.getContent()));
    }

    @Override
    public void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean) {
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("Your admin profile has been updated: ");
        switch(updateAdminMailBean.getMembershipAction()){
            case NEW_MEMBERSHIP:{
                updateAdminMailBean.setSubject("API Engine - admin rights assigned");
                sContent.append("\nYou have been added as administrator for the API Engine.");
                break;
            }
            case DELETE_MEMBERSHIP:{
                updateAdminMailBean.setSubject("API Engine - admin rights revoked");
                sContent.append("\nYou have been removed as administrator for the API Engine.");
                break;
            }
            default:{
                updateAdminMailBean.setSubject("API Engine - verify account");
                sContent.append("Verify your account.");
            }
        }
        sContent.append(getMailSignature());

        //set content
        updateAdminMailBean.setContent(sContent.toString());
        sendMail(composeMessage(updateAdminMailBean.getTo(),updateAdminMailBean.getSubject(),updateAdminMailBean.getContent()));
    }

    /**
     * Utility method to compose the Mime message.
     *
     * @param toAddress
     * @param subject
     * @param content
     * @return
     */
    private MimeMessage composeMessage(String toAddress, String subject, String content){
        try{
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("apiengine@digipolis.be");
            Address[] to = new InternetAddress[] {new InternetAddress(toAddress) };
            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(content,"text/plain");
            return m;
        }catch(MessagingException e){
            _LOG.error("Error sending email:{}",e.getMessage());
            return null;
        }
    }

    /**
     * Utility method to send a composed mime message
     * @param m
     */
    private void sendMail(MimeMessage m){
        try{
            if(m!=null){
                Transport.send(m);
                _LOG.debug("Mail sent: {}",m);
            }
        }catch(MessagingException e){
            _LOG.error("Error sending email:{}",e.getMessage());
        }
    }

    private String getMailSignature(){
        StringBuilder sContent = new StringBuilder();
        sContent.append("\n\n\nGreetings from the APIe Team.");
        sContent.append("\n\n");
        sContent.append("\n  /~\\");
        sContent.append("\n C oo");
        sContent.append("\n _( ^)");
        sContent.append("\n/   ~\\");
        return sContent.toString();
    }
}
