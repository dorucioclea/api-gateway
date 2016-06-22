package com.t1t.digipolis.apim.beans.support;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michallispashidis on 4/10/15.
 */
public class SupportTicketsResponse implements Serializable {
    private List<SupportBean> supportTickets;

    public SupportTicketsResponse() {
    }

    public List<SupportBean> getSupportTickets() {
        return supportTickets;
    }

    public void setSupportTickets(List<SupportBean> supportTickets) {
        this.supportTickets = supportTickets;
    }

    @Override
    public String toString() {
        return "SupportTicketsResponse{" +
                "supportTickets=" + supportTickets +
                '}';
    }
}
