package com.t1t.digipolis.apim.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michallispashidis on 29/04/16.
 * Contains all data implicitly used for mail templates.
 */
public enum MailTopic {
    MEMBERSHIP_REQUEST("MEMBERSHIP_REQUEST"),
    MEMBERSHIP_REQUEST_CANCEL("MEMBERSHIP_REQUEST_CANCEL"),
    MEMBERSHIP_APPROVE("MEMBERSHIP_APPROVE"),
    MEMBERSHIP_REJECT("MEMBERSHIP_REJECT"),
    MEMBERSHIP_UPDATE_ROLE("MEMBERSHIP_UPDATE_ROLE"),
    MEMBERSHIP_GRANTED("MEMBERSHIP_GRANTED"),
    MEMBERSHIP_NEW("MEMBERSHIP_NEW"),
    MEMBERSHIP_DELETED("MEMBERSHIP_DELETED"),
    MEMBERSHIP_ADMIN_NEW("MEMBERSHIP_ADMIN_NEW"),
    MEMBERSHIP_ADMIN_DELETED("MEMBERSHIP_ADMIN_DELETED"),
    ORGANIZATION_TRANSFER("ORGANIZATION_TRANSFER"),
    CONTRACT_REQUEST("CONTRACT_REQUEST"),
    CONTRACT_APPROVE("CONTRACT_APPROVE"),
    CONTRACT_REJECT("CONTRACT_REJECT"),
    CONTRACT_REQUEST_CANCEL("CONTRACT_REQUEST_CANCEL"),
    STATUS("STATUS"),
    TEST("TEST");

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
