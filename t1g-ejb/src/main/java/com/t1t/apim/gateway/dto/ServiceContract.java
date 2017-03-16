package com.t1t.apim.gateway.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal representation of a service contract.  Ties together a service and
 * an application by its contract (api key).
 *
 */
public class ServiceContract implements Serializable {

    private static final long serialVersionUID = -4264090614804457252L;

    private String apikey;
    private Service service;
    private Application application;
    private String plan;
    private List<Policy> policies = new ArrayList<>();

    /**
     * Constructor.
     */
    public ServiceContract() {
    }

    /**
     * Constructor.
     * @param apikey the api key
     * @param service the service
     * @param application the application
     * @param policies the list of policies
     */
    public ServiceContract(String apikey, Service service, Application application, String plan, List<Policy> policies) {
        setApikey(apikey);
        setService(service);
        setApplication(application);
        setPlan(plan);
        setPolicies(policies);
    }

    /**
     * @return the apikey
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * @param apikey the apikey to set
     */
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    /**
     * @return the service
     */
    public Service getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * @return the policies
     */
    public List<Policy> getPolicies() {
        return policies;
    }

    /**
     * @param policies the policies to set
     */
    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apikey == null) ? 0 : apikey.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceContract other = (ServiceContract) obj;
        if (apikey == null) {
            if (other.apikey != null)
                return false;
        } else if (!apikey.equals(other.apikey))
            return false;
        return true;
    }

    /**
     * @return the plan
     */
    public String getPlan() {
        return plan;
    }

    /**
     * @param plan the plan to set
     */
    public void setPlan(String plan) {
        this.plan = plan;
    }

}
