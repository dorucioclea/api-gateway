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
 * Created by michallispashidis on 8/04/16.
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
        //TODO set conditionally (appconfig)
        mailSession.setDebug(true);
    }

    public void sendTestMail(){
        sendMail(composeMessage("michallis@trust1team.com","API Engine mail test","This is a test message - you can ignore the content."));
    }

    @Override
    public void sendStatusMail(StatusMailBean statusMailBean) {

    }

    @Override
    public void sendRequestMembership(RequestMembershipMailBean requestMembershipMailBean) {

    }

    @Override
    public void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean) {

    }

    @Override
    public void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean) {

    }

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
}
