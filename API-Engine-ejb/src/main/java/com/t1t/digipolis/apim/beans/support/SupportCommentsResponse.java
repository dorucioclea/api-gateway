package com.t1t.digipolis.apim.beans.support;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michallispashidis on 4/10/15.
 */
public class SupportCommentsResponse implements Serializable {
    List<SupportComment> comments;

    public SupportCommentsResponse() {
    }

    @Override
    public String toString() {
        return "SupportCommentsResponse{" +
                "comments=" + comments +
                '}';
    }

    public List<SupportComment> getComments() {
        return comments;
    }

    public void setComments(List<SupportComment> comments) {
        this.comments = comments;
    }
}
