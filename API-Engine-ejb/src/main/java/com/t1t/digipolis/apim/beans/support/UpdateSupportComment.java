package com.t1t.digipolis.apim.beans.support;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/10/15.
 */
public class UpdateSupportComment implements Serializable{
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "UpdateSupportComment{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
