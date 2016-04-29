package com.t1t.digipolis.apim.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michallispashidis on 29/04/16.
 * Contains all data implicitly used for mail templates.
 */
public enum MailTopic {
    MEMBERSHIP_REQUEST("MEMBERSHIP_REQUEST"),
    MEMBERSHIP_APPROVE("MEMBERSHIP_APPROVE"),
    MEMBERSHIP_REJECT("MEMBERSHIP_REJECT"),
    MEMBERSHIP_UPDATE("MEMBERSHIP_UPDATE"),
    MEMBERSHIP_ADMIN_UPDATE("MEMBERSHIP_ADMIN_UPDATE"),
    CONTRACT_REQUEST("CONTRACT_REQUEST"),
    CONTRACT_APPROVE("CONTRACT_APPROVE"),
    CONTRACT_REJECT("CONTRACT_REJECT"),
    STATUS("STATUS"),
    TEST("TEST"),
    FOOTER("FOOTER");

    private String topicName;

    MailTopic(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return "MailTopic{" + this.getTopicName() + '}';
    }
}
