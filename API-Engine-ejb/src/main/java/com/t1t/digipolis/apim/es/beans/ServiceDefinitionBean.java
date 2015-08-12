package com.t1t.digipolis.apim.es.beans;

/**
 * An elastic search service definition bean.
 */
public class ServiceDefinitionBean {
    
    private String data;
    
    /**
     * Constructor.
     */
    public ServiceDefinitionBean() {
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

}
