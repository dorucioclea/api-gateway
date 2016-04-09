package com.t1t.digipolis.apim.mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.annotation.Resource;
import javax.mail.Transport;
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

    public void sendMail(){
        try   {
            _LOG.info("compose email");
            mailSession.setDebug(true);
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("myappsenderemailaddress.com");
            Address[] to = new InternetAddress[] {new InternetAddress("michallis@trust1team.com") };
            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("Java EE mail example");
            m.setSentDate(new java.util.Date());
            m.setContent("Plain text example","text/plain");
            _LOG.info("start sending mail");
            Transport.send(m);
            _LOG.info("Mail sent!");
        }
        catch (javax.mail.MessagingException e)
        {
            e.printStackTrace();
            _LOG.info("Error in Sending Mail: {}"+e.getMessage());
        }
    }
}
