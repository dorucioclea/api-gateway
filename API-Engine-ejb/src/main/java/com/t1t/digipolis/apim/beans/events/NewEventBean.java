package com.t1t.digipolis.apim.beans.events;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewEventBean {

    private String originId;
    private String destinationId;
    private EventType type;
    private String body;

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NewEventBean withOriginId(String originId) {
        this.setOriginId(originId);
        return this;
    }

    public NewEventBean withDestinationId(String destinationId) {
        this.setDestinationId(destinationId);
        return this;
    }

    public NewEventBean withBody(String body) {
        this.setBody(body);
        return this;
    }

    public NewEventBean withType(EventType type) {
        this.setType(type);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewEventBean that = (NewEventBean) o;

        if (originId != null ? !originId.equals(that.originId) : that.originId != null) return false;
        if (destinationId != null ? !destinationId.equals(that.destinationId) : that.destinationId != null)
            return false;
        if (type != that.type) return false;
        return body != null ? body.equals(that.body) : that.body == null;

    }

    @Override
    public int hashCode() {
        int result = originId != null ? originId.hashCode() : 0;
        result = 31 * result + (destinationId != null ? destinationId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewEventBean{" +
                "originId='" + originId + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", type=" + type +
                ", body='" + body + '\'' +
                '}';
    }
}