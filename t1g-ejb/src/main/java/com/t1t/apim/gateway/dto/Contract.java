package com.t1t.apim.gateway.dto;

import com.t1t.apim.beans.contracts.ContractBean;
import com.t1t.apim.beans.summary.ContractSummaryBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a service contract published to the API Management runtime.
 */
public class Contract implements Serializable {

    private static final long serialVersionUID = 8344360785926823601L;

    private Long id;
    private String serviceOrgId;
    private String serviceId;
    private String serviceVersion;
    private String plan;
    private List<Policy> policies = new ArrayList<>();

    /**
     * Constructor.
     */
    public Contract() {
    }

    public Contract(ContractBean contract) {
        this.id = contract.getId();
        this.serviceOrgId = contract.getService().getService().getOrganization().getId();
        this.serviceId = contract.getService().getService().getId();
        this.serviceVersion = contract.getService().getVersion();
        this.plan = contract.getPlan().getPlan().getId();
    }

    public Contract(ContractSummaryBean c) {
        this.id = c.getContractId();
        this.serviceOrgId = c.getServiceOrganizationId();
        this.serviceId = c.getServiceId();
        this.serviceVersion = c.getServiceVersion();
        this.plan = c.getPlanId();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the serviceOrgId
     */
    public String getServiceOrgId() {
        return serviceOrgId;
    }

    /**
     * @param serviceOrgId the serviceOrgId to set
     */
    public void setServiceOrgId(String serviceOrgId) {
        this.serviceOrgId = serviceOrgId;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the serviceVersion
     */
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @param serviceVersion the serviceVersion to set
     */
    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        if (id != null ? !id.equals(contract.id) : contract.id != null) return false;
        if (serviceOrgId != null ? !serviceOrgId.equals(contract.serviceOrgId) : contract.serviceOrgId != null)
            return false;
        if (serviceId != null ? !serviceId.equals(contract.serviceId) : contract.serviceId != null) return false;
        if (serviceVersion != null ? !serviceVersion.equals(contract.serviceVersion) : contract.serviceVersion != null)
            return false;
        if (plan != null ? !plan.equals(contract.plan) : contract.plan != null) return false;
        return policies != null ? policies.equals(contract.policies) : contract.policies == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (serviceOrgId != null ? serviceOrgId.hashCode() : 0);
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (serviceVersion != null ? serviceVersion.hashCode() : 0);
        result = 31 * result + (plan != null ? plan.hashCode() : 0);
        result = 31 * result + (policies != null ? policies.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", serviceOrgId='" + serviceOrgId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", plan='" + plan + '\'' +
                ", policies=" + policies +
                '}';
    }
}
