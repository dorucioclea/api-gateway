package com.t1t.digipolis.apim.gateway.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The response sent back to a caller when a managed service is
 * invoked.
 *
 */
public class ServiceResponse implements IServiceObject, Serializable {

    private static final long serialVersionUID = -7245095046846226241L;

    private int code;
    private String message;
    private Map<String, String> headers = new HeaderHashMap();
    @JsonIgnore
    private transient Map<String, Object> attributes = new HashMap<>();

    /**
     * Constructor.
     */
    public ServiceResponse() {
    }

    /**
     * @see io.apiman.gateway.engine.beans.IServiceObject#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @see io.apiman.gateway.engine.beans.IServiceObject#setHeaders(Map)
     */
    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param name Name of attribute
     * @param value Value of attribute
     */
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    /**
     * @param name Name of attribute
     * @return Attribute if present; else null.
     */
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }
}
