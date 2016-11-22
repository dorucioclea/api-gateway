package com.t1t.digipolis.apim.beans.contracts;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A Contract links an application version to a service version through
 * a plan version.  :)
 *
 * This is how application owners/developers configure their application
 * to allow it to invoke managed services.
 *
 */
@Entity
@Table(name = "contracts",
       uniqueConstraints = { @UniqueConstraint(columnNames = { "appv_id", "svcv_id", "planv_id" }) })
public class ContractBean implements Serializable {

    private static final long serialVersionUID = -8534463608508756791L;

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="appv_id", referencedColumnName="id")
    })
    private ApplicationVersionBean application;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="svcv_id", referencedColumnName="id")
    })
    private ServiceVersionBean service;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="planv_id", referencedColumnName="id")
    })
    private PlanVersionBean plan;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Column(name = "terms_agreed")
    private Boolean termsAgreed;

    /**
     * Constructor.
     */
    public ContractBean() {
    }

    /**
     * @return the application
     */
    public ApplicationVersionBean getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(ApplicationVersionBean application) {
        this.application = application;
    }

    /**
     * @return the service
     */
    public ServiceVersionBean getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(ServiceVersionBean service) {
        this.service = service;
    }

    /**
     * @return the plan
     */
    public PlanVersionBean getPlan() {
        return plan;
    }

    /**
     * @param plan the plan to set
     */
    public void setPlan(PlanVersionBean plan) {
        this.plan = plan;
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
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the value of termsAgreed
     */
    public Boolean getTermsAgreed() {
        return termsAgreed;
    }

    /**
     * @param termsAgreed the value to set
     */
    public void setTermsAgreed(Boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ContractBean [id=" + id + ", application=" + application + ", service=" + service + ", plan="
                + plan + ", createdBy=" + createdBy + ", createdOn=" + createdOn + "]";
    }

}
