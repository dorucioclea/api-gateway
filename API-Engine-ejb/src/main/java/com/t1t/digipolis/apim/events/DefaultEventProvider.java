package com.t1t.digipolis.apim.events;

import com.t1t.digipolis.apim.beans.events.NewEventBean;
import com.t1t.digipolis.apim.events.qualifiers.MembershipRequest;

import javax.enterprise.event.Observes;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface DefaultEventProvider {

    public void onMemberShipRequest(@Observes @MembershipRequest NewEventBean bean);
}
