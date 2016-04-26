package com.t1t.digipolis.apim.beans.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewEventBean {
    private String requestOrigin;
    private String requestDestination;
    private EventType type;

    public String getRequestOrigin() {
        return requestOrigin;
    }

    public void setRequestOrigin(String requestOrigin) {
        this.requestOrigin = requestOrigin;
    }

    public String getRequestDestination() {
        return requestDestination;
    }

    public void setRequestDestination(String requestDestination) {
        this.requestDestination = requestDestination;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewEventBean that = (NewEventBean) o;

        if (!requestOrigin.equals(that.requestOrigin)) return false;
        if (!requestDestination.equals(that.requestDestination)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = requestOrigin.hashCode();
        result = 31 * result + requestDestination.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}