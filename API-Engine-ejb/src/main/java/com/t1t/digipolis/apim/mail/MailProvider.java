package com.t1t.digipolis.apim.mail;

/**
 * Created by michallispashidis on 8/04/16.
 */
public interface MailProvider {
    /**
     * Default endpoint to send email
     */
    void sendMail();

    /**
     * Send email for a server event, this can be a startup event, or shutdown, or can be used for a cron
     * heath check results.
     */
    void sendServerEventMail();

    /**
     * Send email for a membership request.
     */
    void sendRequestMembership();

    /**
     * Send a user notification.
     */
    void sendUserNotification();
}
