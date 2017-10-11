package com.t1t.apim.mail;

import com.t1t.apim.AppConfigBean;
import com.t1t.apim.T1G;
import com.t1t.apim.beans.mail.BaseMailBean;
import com.t1t.apim.exceptions.MailProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by michallispashidis on 29/04/16.
 */
@ApplicationScoped
@Default
public class DefaultMailProvider implements MailProvider {
    private final static Logger _LOG = LoggerFactory.getLogger(DefaultMailProvider.class.getName());
    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;
    @Inject
    @T1G
    private AppConfigBean config;

    @PostConstruct
    public void init() {
        mailSession.setDebug(config.getNotificationsEnableDebug());
    }

    @Override
    public <K extends MimeMessage> void sendMail(K m) throws MailProviderException {
        try {
            if (m != null) {
                Transport.send(m);
                _LOG.debug("Mail sent: {}", m);
            }
        } catch (MessagingException e) {
            _LOG.error("Error sending email:{}", e.getMessage());
        }
    }

    @Override
    public <K extends MimeMessage> K composeMessage(BaseMailBean mailContent) throws MailProviderException {
        String subject = mailContent.getSubject();
        String content = mailContent.getContent();
        String toAddress = mailContent.getTo();
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress(config.getNotificationMailFrom());
            Address[] to = new InternetAddress[]{new InternetAddress(toAddress)};
            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(content, "text/html; charset=utf-8");
            return (K) m;
        } catch (MessagingException e) {
            _LOG.error("Error sending email:{}", e.getMessage());
            return null;
        }
    }
}
