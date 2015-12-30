package com.t1t.digipolis.apim.beans.user;

/**
 * Created by michallispashidis on 29/12/15.
 */
public class UserSession {
    private String subjectId;
    private String sessionIndex;

    public UserSession(String subjectId, String sessionIndex) {
        this.subjectId = subjectId;
        this.sessionIndex = sessionIndex;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(String sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "subjectId='" + subjectId + '\'' +
                ", sessionIndex='" + sessionIndex + '\'' +
                '}';
    }
}
