package com.t1t.apim.mail;

import com.t1t.apim.beans.mail.BaseMailBean;
import com.t1t.apim.exceptions.MailProviderException;

import javax.mail.internet.MimeMessage;

/**
 * Created by michallispashidis on 29/04/16.
 */
public interface MailProvider {
    <K extends MimeMessage> void sendMail(K message) throws MailProviderException;
    <K extends MimeMessage> K composeMessage(BaseMailBean mailContent)throws MailProviderException;
}
