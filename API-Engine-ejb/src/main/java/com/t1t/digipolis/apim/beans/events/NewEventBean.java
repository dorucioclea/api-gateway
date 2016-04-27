package com.t1t.digipolis.apim.beans.events;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewEventBean {
    private String origin;
    private String destination;
    private EventType type;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

        if (!origin.equals(that.origin)) return false;
        if (!destination.equals(that.destination)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = origin.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}