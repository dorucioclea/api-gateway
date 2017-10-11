package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.apps.*;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.brandings.ServiceBrandingSummaryBean;
import com.t1t.apim.beans.categories.ServiceTagsBean;
import com.t1t.apim.beans.categories.TagBean;
import com.t1t.apim.beans.contracts.ContractBean;
import com.t1t.apim.beans.contracts.ContractCancellationBean;
import com.t1t.apim.beans.contracts.NewContractBean;
import com.t1t.apim.beans.contracts.NewContractRequestBean;
import com.t1t.apim.beans.events.EventBean;
import com.t1t.apim.beans.idm.GrantRoleBean;
import com.t1t.apim.beans.idm.TransferOwnershipBean;
import com.t1t.apim.beans.members.MemberBean;
import com.t1t.apim.beans.metrics.AppUsagePerServiceBean;
import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.orgs.NewOrganizationBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.apim.beans.pagination.OAuth2TokenPaginationBean;
import com.t1t.apim.beans.plans.*;
import com.t1t.apim.beans.policies.NewPolicyBean;
import com.t1t.apim.beans.policies.PolicyChainBean;
import com.t1t.apim.beans.policies.RateLimitToggleRequest;
import com.t1t.apim.beans.policies.UpdatePolicyBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.services.*;
import com.t1t.apim.beans.summary.*;
import com.t1t.apim.beans.support.*;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.*;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The Organization API.
 */
public interface IOrganizationResource {

    /**
     * Use this endpoint to create a new Organization.
     *
     * @param bean Information about the new Organization.
     * @return Full details about the Organization that was created.
     * @throws OrganizationAlreadyExistsException when trying to create an Organization that already exists
     * @throws NotAuthorizedException             when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException               when the user attempts to create an Organization with an invalid name
     * @summary Create Organization
     * @statuscode 200 If the Organization was successfully created.
     */
    public OrganizationBean create(NewOrganizationBean bean) throws OrganizationAlreadyExistsException,
            NotAuthorizedException, InvalidNameException, StorageException;

    /**
     * Use this endpoint to get information about a single Organization
     * by its ID.
     *
     * @param organizationId The Organization id.
     * @return The Organization.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Organization By ID
     * @statuscode 200 If the Organization was successfully returned.
     * @statuscode 404 If the Organization does not exist.
     */
    public OrganizationBean get(String organizationId) throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Updates meta-information about a single Organization.
     *
     * @param organizationId The Organization ID.
     * @param bean           Updated Organization information.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Organization By ID
     * @statuscode 200 If the Organization meta-data is successfully updated.
     * @statuscode 404 If the Organization does not exist.
     */
    public void update(String organizationId, UpdateOrganizationBean bean)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Returns audit activity information for a single Organization.  The audit
     * information that is returned represents all of the activity associated
     * with this Organization (i.e. an audit log for everything in the Organization).
     *
     * @param organizationId The Organization ID.
     * @param page           Which page of activity results to return.
     * @param pageSize       The number of entries per page.
     * @return List of audit/activity entries.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Organization Activity
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     */
    public SearchResultsBean<AuditEntryBean> activity(String organizationId, int page, int pageSize) throws OrganizationNotFoundException, NotAuthorizedException;

    /*
     * APPLICATIONS
     */

    /**
     * Use this endpoint to create a new Application.  Note that it is important to also
     * create an initial version of the Application (e.g. 1.0).  This can either be done
     * by including the 'initialVersion' property in the request, or by immediately following
     * up with a call to "Create Application Version".  If the former is done, then a first
     * Application version will be created automatically by this endpoint.
     *
     * @param organizationId The Organization ID.
     * @param bean           Information about the new Application.
     * @return Full details about the newly created Application.
     * @throws OrganizationNotFoundException     when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationAlreadyExistsException when trying to create an Application that already exists
     * @throws NotAuthorizedException            when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException              when the user attempts the create with an invalid name
     * @summary Create Application
     * @statuscode 200 If the Application is successfully created.
     * @statuscode 404 If the Organization does not exist.
     */
    public ApplicationBean createApp(String organizationId, NewApplicationBean bean) throws OrganizationNotFoundException, ApplicationAlreadyExistsException,
            NotAuthorizedException, InvalidNameException;

    /**
     * Use this endpoint to retrieve information about a single Application by ID.  Note
     * that this only returns information about the Application, not about any particular
     * *version* of the Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @return An Application.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist when trying to get, update, or remove an application that does not exist.
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Application By ID
     * @statuscode 200 If the Application is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Application does not exist.
     */
    public ApplicationBean getApp(String organizationId, String applicationId) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to delete a single Application by ID.  Note
     * that this is *irreversible* and should therefore be used with caution!
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @throws OrganizationNotFoundException when specifying an application to delete in an organization that does not exist
     * @throws ApplicationNotFoundException  when trying to remove an application that does not exist.
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Delete Application By ID
     * @statuscode 204 If the Application is successfully deleted.
     */
    public void deleteApp(String organizationId, String applicationId) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * This endpoint returns audit activity information about the Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param page           Which page of activity should be returned.
     * @param pageSize       The number of entries per page to return.
     * @return A list of audit activity entries.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Application Activity
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Application does not exist.
     */
    public SearchResultsBean<AuditEntryBean> getAppActivity(
            String organizationId, String applicationId, int page, int pageSize) throws ApplicationNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Applications in the Organization.
     *
     * @param organizationId The Organization ID.
     * @return A list of Applications.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Applications
     * @statuscode 200 If the list of Applications is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     */
    public List<ApplicationSummaryBean> listApps(String organizationId)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update information about an Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param bean           Updated Application information.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Application
     * @statuscode 204 If the Application is updated successfully.
     * @statuscode 404 If the Application does not exist.
     */
    public void updateApp(String organizationId, String applicationId, UpdateApplicationBean bean) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to create a new version of the Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param bean           Initial information about the new Application version.
     * @return Full details about the newly created Application version.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidVersionException      when the user attempts to use an invalid version value
     * @summary Create Application Version
     * @statuscode 200 If the Application version is created successfully.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 409 If the Application version already exists.
     */
    public ApplicationVersionBean createAppVersion(String organizationId, String applicationId, NewApplicationVersionBean bean)
            throws ApplicationNotFoundException, NotAuthorizedException, InvalidVersionException,
            ApplicationVersionAlreadyExistsException;

    /**
     * Use this endpoint to list all of the versions of an Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @return A list of Applications.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Application Versions
     * @statuscode 200 If the list of Application versions is successfully returned.
     */
    public List<ApplicationVersionSummaryBean> listAppVersions(String organizationId, String applicationId) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get detailed information about a single version of
     * an Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @return An Application version.
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Application Version
     * @statuscode 200 If the Application version is successfully returned.
     * @statuscode 404 If the Application version does not exist.
     */
    public ApplicationVersionBean getAppVersion(String organizationId, String applicationId, String version)
            throws ApplicationVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get audit activity information for a single version of the
     * Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param page           Which page of activity data to return.
     * @param pageSize       The number of entries per page to return.
     * @return A list of audit entries.
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Application Version Activity
     * @statuscode 200 If the audit activity entries are successfully returned.
     * @statuscode 404 If the Application version does not exist.
     */
    public SearchResultsBean<AuditEntryBean> getAppVersionActivity(
            String organizationId, String applicationId,
            String version, int page,
            int pageSize) throws ApplicationVersionNotFoundException, NotAuthorizedException;

    /**
     * Retrieves metrics/analytics information for a specific application.  This will
     * return request count data broken down by service.  It basically answers
     * the question "which services is my app really using?".
     *
     * @param organizationId The organization ID.
     * @param applicationId  The application ID.
     * @param version        The application version.
     * @param fromDate       The start of a valid date range.
     * @param toDate         The end of a valid date range.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get App Usage Metrics (per Service)
     * @statuscode 200 If the metrics data is successfully returned.
     */
    public AppUsagePerServiceBean getAppUsagePerService(
            String organizationId, String applicationId, String fromDate, String version,
            String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;

//    /**
//     * Use this endpoint to create a Contract between the Application and a Service.  In order
//     * to create a Contract, the caller must specify the Organization, ID, and Version of the
//     * Service.  Additionally the caller must specify the ID of the Plan it wished to use for
//     * the Contract with the Service.
//     * @summary Create a Service Contract
//     * @param organizationId The Organization ID.
//     * @param applicationId The Application ID.
//     * @param version The Application version.
//     * @param bean Required information about the new Contract.
//     * @statuscode 200 If the Contract is successfully created.
//     * @statuscode 404 If the Application version does not exist.
//     * @return Full details about the newly created Contract.
//     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
//     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
//     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
//     * when trying to get, update, or remove an plan that does not exist
//     * @throws PlanNotFoundException when trying to get, update, or remove an plan that does not exist
//     * @throws ContractAlreadyExistsException when trying to create an Contract that already exists
//     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
//     */
//    public ContractBean createContract(String organizationId,
//                                       String applicationId, String version,
//                                       NewContractBean bean) throws OrganizationNotFoundException, ApplicationNotFoundException,
//            ServiceNotFoundException, PlanNotFoundException, ContractAlreadyExistsException,
//            NotAuthorizedException;

    /**
     * Use this endpoint to retrieve detailed information about a single Service Contract
     * for an Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param contractId     The ID of the Contract.
     * @return Details about a single Contract.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws ContractNotFoundException    when trying to get, update, or remove a contract that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Contract
     * @statuscode 200 If the Contract is successfully returned.
     * @statuscode 404 If the Application version does not exist.
     * @statuscode 404 If the Contract is not found.
     */
    public ContractBean getContract(String organizationId, String applicationId, String version, Long contractId) throws ApplicationNotFoundException,
            ContractNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Contracts for an Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @return A list of Contracts.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List All Contracts for an Application
     * @statuscode 200 If the list of Contracts is successfully returned.
     * @statuscode 404 If the Application is not found.
     */
    public List<ContractSummaryBean> getApplicationVersionContracts(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get registry style information about all Services that this
     * Application consumes.  This is a useful endpoint to invoke in order to retrieve
     * a summary of every Service consumed by the application.  The information returned
     * by this endpoint could potentially be included directly in a client application
     * as a way to lookup endpoint information for the APIs it wishes to consume.  This
     * variant of the API Registry is formatted as JSON data.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @return API Registry information.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get API Registry (JSON)
     * @statuscode 200 If the API Registry information is successfully returned.
     * @statuscode 404 If the Application does not exist.
     */
    public ApiRegistryBean getApiRegistryJSON(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get registry style information about all Services that this
     * Application consumes.  This is a useful endpoint to invoke in order to retrieve
     * a summary of every Service consumed by the application.  The information returned
     * by this endpoint could potentially be included directly in a client application
     * as a way to lookup endpoint information for the APIs it wishes to consume.  This
     * variant of the API Registry is formatted as XML data.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @return API Registry information.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get API Registry (XML)
     * @statuscode 200 If the API Registry information is successfully returned.
     * @statuscode 404 If the Application does not exist.
     */
    public ApiRegistryBean getApiRegistryXML(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to break all contracts between this application and its services.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Break All Contracts
     * @statuscode 200 If the operation is successful.
     * @statuscode 404 If the Application does not exist.
     */
    public void deleteAllContracts(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to break a Contract with a Service.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param contractId     The Contract ID.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws ContractNotFoundException    when trying to get, update, or remove a contract that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Break Contract
     * @statuscode 200 If the Contract is successfully broken.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 404 If the Contract does not exist.
     */
    public void deleteContract(String organizationId, String applicationId, String version, Long contractId) throws ApplicationNotFoundException,
            ContractNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add a new Policy to the Application version.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param bean           Information about the new Policy.
     * @return Full details about the newly added Policy.
     * @throws OrganizationNotFoundException       when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Add Application Policy
     * @statuscode 200 If the Policy is successfully added.
     * @statuscode 404 If the Application does not exist.
     */
    public EnrichedPolicySummaryBean createAppPolicy(String organizationId, String applicationId, String version, NewPolicyBean bean) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get information about a single Policy in the Application version.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param policyId       The Policy ID.
     * @return Full information about the Policy.
     * @throws OrganizationNotFoundException       when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws PolicyNotFoundException             when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Application Policy
     * @statuscode 200 If the Policy is successfully returned.
     * @statuscode 404 If the Application does not exist.
     */
    public EnrichedPolicySummaryBean getAppPolicy(String organizationId, String applicationId, String version, long policyId) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the meta-data or configuration of a single Application Policy.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param policyId       The Policy ID.
     * @param bean           New meta-data and/or configuration for the Policy.
     * @throws OrganizationNotFoundException       when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws PolicyNotFoundException             when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Application Policy
     * @statuscode 204 If the Policy was successfully updated.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 404 If the Policy does not exist.
     */
    public void updateAppPolicy(String organizationId, String applicationId, String version, long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ApplicationVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Policy from the Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param policyId       The Policy ID.
     * @throws OrganizationNotFoundException       when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws PolicyNotFoundException             when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Remove Application Policy
     * @statuscode 204 If the Policy was successfully deleted.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 404 If the Policy does not exist.
     */
    public void deleteAppPolicy(String organizationId, String applicationId, String version, long policyId) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to list all of the Policies configured for the Application.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @return A List of Policies.
     * @throws OrganizationNotFoundException       when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List All Application Policies
     * @statuscode 200 If the list of Policies is successfully returned.
     * @statuscode 404 If the Application does not exist.
     */
    public List<PolicySummaryBean> listAppPolicies(String organizationId, String applicationId, String version) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to change the order of Policies for an Application.  When a
     * Policy is added to the Application, it is added as the last Policy in the list
     * of Application Policies.  Sometimes the order of Policies is important, so it
     * is often useful to re-order the Policies by invoking this endpoint.  The body
     * of the request should include all of the Policies for the Application, in the
     * new desired order.  Note that only the IDs of each of the Policies is actually
     * required in the request, at a minimum.
     *
     * @param organizationId The Organization ID.
     * @param applicationId  The Application ID.
     * @param version        The Application version.
     * @param policyChain    The Policies in the desired order.
     * @throws OrganizationNotFoundException       when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException              when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Re-Order Application Policies
     * @statuscode 204 If the re-ordering of Policies was successful.
     * @statuscode 404 If the Application does not exist.
     */
    public void reorderApplicationPolicies(String organizationId, String applicationId, String version, PolicyChainBean policyChain) throws OrganizationNotFoundException,
            ApplicationVersionNotFoundException, NotAuthorizedException;

    /*
     * SERVICES
     */

    /**
     * Use this endpoint to create a new Service.  Note that it is important to also
     * create an initial version of the Service (e.g. 1.0).  This can either be done
     * by including the 'initialVersion' property in the request, or by immediately following
     * up with a call to "Create Service Version".  If the former is done, then a first
     * Service version will be created automatically by this endpoint.
     *
     * @param organizationId The Organization ID.
     * @param bean           Information about the new Service.
     * @return Full details about the newly created Service.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceAlreadyExistsException when trying to create an Service that already exists
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException          when the user attempts the create with an invalid name
     * @summary Create Service
     * @statuscode 200 If the Service is successfully created.
     * @statuscode 404 If the Organization does not exist.
     */
    public ServiceBean createService(String organizationId, NewServiceBean bean)
            throws OrganizationNotFoundException, ServiceAlreadyExistsException, NotAuthorizedException,
            InvalidNameException;

    /**
     * Use this endpoint to retrieve information about a single Service by ID.  Note
     * that this only returns information about the Service, not about any particular
     * *version* of the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @return A Service.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException   when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service By ID
     * @statuscode 200 If the Service is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Service does not exist.
     */
    public ServiceBean getService(String organizationId, String serviceId) throws ServiceNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to delete a single Service by ID.  Note
     * that this is *irreversible* and should therefore be used with caution!
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @throws OrganizationNotFoundException when specifying a service to delete in an organization that does not exist
     * @throws ServiceNotFoundException      when trying to remove a service that does not exist.
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Delete Service By ID
     * @statuscode 204 If the Application is successfully deleted.
     */
    public void deleteService(String organizationId, String serviceId) throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Services in the Organization.
     *
     * @param organizationId The Organization ID.
     * @return A list of Services.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Services
     * @statuscode 200 If the list of Services is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     */
    public List<ServiceSummaryBean> listServices(String organizationId)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update information about a Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param bean           Updated Service information.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException   when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Service
     * @statuscode 204 If the Service is updated successfully.
     * @statuscode 404 If the Service does not exist.
     */
    public void updateService(String organizationId, String serviceId, UpdateServiceBean bean)
            throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * This endpoint returns audit activity information about the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param page           Which page of activity should be returned.
     * @param pageSize       The number of entries per page to return.
     * @return A list of audit activity entries.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException   when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Activity
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Service does not exist.
     */
    public SearchResultsBean<AuditEntryBean> getServiceActivity(
            String organizationId, String serviceId,
            int page, int pageSize) throws ServiceNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to create a new version of the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param bean           Initial information about the new Service version.
     * @return Full details about the newly created Service version.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException   when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidVersionException  when the user attempts to use an invalid version value
     * @summary Create Service Version
     * @statuscode 200 If the Service version is created successfully.
     * @statuscode 404 If the Service does not exist.
     * @statuscode 409 If the Service version already exists.
     */
    public ServiceVersionBean createServiceVersion(String organizationId, String serviceId, NewServiceVersionBean bean)
            throws ServiceNotFoundException, NotAuthorizedException, InvalidVersionException,
            ServiceVersionAlreadyExistsException;

    /**
     * Use this endpoint to list all of the versions of a Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @return A list of Services.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException   when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Service Versions
     * @statuscode 200 If the list of Service versions is successfully returned.
     */
    public List<ServiceVersionSummaryBean> listServiceVersions(String organizationId,
                                                               String serviceId) throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get detailed information about a single version of
     * a Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @return A Service version.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Version
     * @statuscode 200 If the Service version is successfully returned.
     * @statuscode 404 If the Service version does not exist.
     */
    public ServiceVersionBean getServiceVersion(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to retrieve a service versions upstream targets.
     *
     * @param organizationId the organization ID.
     * @param serviceId      the service ID.
     * @param version        the service version.
     * @return service upstreams
     * @throws ServiceVersionNotFoundException
     * @throws NotAuthorizedException
     */
    public Response getServiceVersionUpstreams(String organizationId, String serviceId, String version) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add an upstream target to the service version.
     *
     * @param organizationId the organization ID.
     * @param serviceId      the service ID.
     * @param version        the service version.
     * @param request        the upstream to add
     * @return service upstreams
     * @throws ServiceVersionNotFoundException
     * @throws NotAuthorizedException
     */
    public Response addServiceVersionUpstream(String organizationId, String serviceId, String version, ServiceUpstreamRequest request) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove an upstream target from the service version.
     *
     * @param organizationId the organization ID.
     * @param serviceId      the service ID.
     * @param version        the service version.
     * @param request        the upstream to remove
     * @return response
     * @throws ServiceVersionNotFoundException
     * @throws NotAuthorizedException
     */
    public Response removeServiceVersionUpstream(String organizationId, String serviceId, String version, ServiceUpstreamRequest request) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to retrieve the Service's definition document.  A service
     * definition document can be several different types, depending on the Service
     * type and technology used to define the service.  For example, this endpoint
     * might return a WSDL document, or a Swagger JSON document.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @return The Service Definition document (e.g. a Swagger JSON file).
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Definition
     * @statuscode 200 If the Service definition is successfully returned.
     * @statuscode 404 If the Service version does not exist.
     */
    public Response getServiceDefinition(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get information about the Managed Service's gateway
     * endpoint.  In other words, this returns the actual live endpoint on the
     * API Gateway - the endpoint that a client should use when invoking the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @return The live Service endpoint information.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws InvalidServiceStatusException   when the user attempts some action on the service when it is not in an appropriate state/status
     * @throws GatewayNotFoundException        when trying to get, update, or remove a gateay that does not exist
     * @summary Get Service Endpoint
     * @statuscode 200 If the endpoint information is successfully returned.
     * @statuscode 404 If the Service does not exist.
     */
    public ServiceVersionEndpointSummaryBean getServiceVersionEndpointInfo(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException;

    /**
     * Use this endpoint to update information about a single version of a Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param bean           Updated information about the Service version.
     * @return The updated Service Version.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidServiceStatusException   when the user attempts some action on the service when it is not in an appropriate state/status
     * @summary Update Service Version
     * @statuscode 204 If the Service version information was successfully updated.
     * @statuscode 404 If the Service does not exist.
     */
    public ServiceVersionBean updateServiceVersion(String organizationId, String serviceId, String version, UpdateServiceVersionBean bean) throws ServiceVersionNotFoundException, NotAuthorizedException,
            InvalidServiceStatusException, StorageException;

    /**
     * Use this endpoint to update the Service's definition document.  A service
     * definition will vary depending on the type of service, and the type of
     * definition used.  For example, it might be a Swagger document or a WSDL file.
     * To use this endpoint, simply PUT the updated Service Definition document
     * in its entirety, making sure to set the Content-Type appropriately for the
     * type of definition document.  The content will be stored and the Service's
     * "Definition Type" field will be updated.
     * <br />
     * Whenever a service's definition is updated, the "definitionType" property of
     * that service is automatically set based on the request Content-Type.  There
     * is no other way to set the service's definition type property.  The following
     * is a map of Content-Type to service definition type.
     * <p>
     * <table>
     * <thead>
     * <tr><th>Content Type</th><th>Service Definition Type</th></tr>
     * </thead>
     * <tbody>
     * <tr><td>application/json</td><td>SwaggerJSON</td></tr>
     * <tr><td>application/x-yaml</td><td>SwaggerYAML</td></tr>
     * <tr><td>application/wsdl+xml</td><td>WSDL</td></tr>
     * </tbody>
     * </table>
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidServiceStatusException   when the user attempts some action on the service when it is not in an appropriate state/status
     * @summary Update Service Definition
     * @statuscode 204 If the Service definition was successfully updated.
     * @statuscode 404 If the Service does not exist.
     */
    public void updateServiceDefinition(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException, InvalidServiceStatusException;

    /**
     * Use this endpoint to get audit activity information for a single version of the
     * Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param page           Which page of activity data to return.
     * @param pageSize       The number of entries per page to return.
     * @return A list of audit entries.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Version Activity
     * @statuscode 200 If the audit activity entries are successfully returned.
     * @statuscode 404 If the Service version does not exist.
     */
    public SearchResultsBean<AuditEntryBean> getServiceVersionActivity(
            String organizationId, String serviceId,
            String version, int page,
            int pageSize) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to list the Plans configured for the given Service version.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @return A list of Service plans.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Service Plans
     * @statuscode 200 If the Service plans are successfully returned.
     * @statuscode 404 If the Service cannot be found.
     */
    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add a new Policy to the Service version.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param bean           Information about the new Policy.
     * @return Full details about the newly added Policy.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Add Service Policy
     * @statuscode 200 If the Policy is successfully added.
     * @statuscode 404 If the Service does not exist.
     */

    public EnrichedPolicySummaryBean createServicePolicy(String organizationId,
                                                         String serviceId, String version,
                                                         NewPolicyBean bean) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get information about a single Policy in the Service version.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param policyId       The Policy ID.
     * @return Full information about the Policy.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws PolicyNotFoundException         when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Policy
     * @statuscode 200 If the Policy is successfully returned.
     * @statuscode 404 If the Service does not exist.
     */
    public EnrichedPolicySummaryBean getServicePolicy(String organizationId, String serviceId, String version, long policyId) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the meta-data or configuration of a single Service Policy.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param policyId       The Policy ID.
     * @param bean           New meta-data and/or configuration for the Policy.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws PolicyNotFoundException         when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Service Policy
     * @statuscode 204 If the Policy was successfully updated.
     * @statuscode 404 If the Service does not exist.
     * @statuscode 404 If the Policy does not exist.
     */

    public EnrichedPolicySummaryBean updateServicePolicy(String organizationId, String serviceId, String version, long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Policy from the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param policyId       The Policy ID.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws PolicyNotFoundException         when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Remove Service Policy
     * @statuscode 204 If the Policy was successfully deleted.
     * @statuscode 404 If the Service does not exist.
     * @statuscode 404 If the Policy does not exist.
     */
    public void deleteServicePolicy(String organizationId, String serviceId, String version, long policyId) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Service's definition document.  When this
     * is done, the "definitionType" field on the Service will be set to None.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Remove Service Definition
     * @statuscode 204 If the Service definition was successfully deleted.
     * @statuscode 404 If the Service does not exist.
     */
    public void deleteServiceDefinition(String organizationId, String serviceId, String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to list all of the Policies configured for the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @return A List of Policies.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List All Service Policies
     * @statuscode 200 If the list of Policies is successfully returned.
     * @statuscode 404 If the Service does not exist.
     */
    public List<PolicySummaryBean> listServicePolicies(String organizationId, String serviceId, String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to change the order of Policies for a Service.  When a
     * Policy is added to the Service, it is added as the last Policy in the list
     * of Service Policies.  Sometimes the order of Policies is important, so it
     * is often useful to re-order the Policies by invoking this endpoint.  The body
     * of the request should include all of the Policies for the Service, in the
     * new desired order.  Note that only the IDs of each of the Policies is actually
     * required in the request, at a minimum.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param policyChain    The Policies in the desired order.
     * @throws OrganizationNotFoundException   when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Re-Order Service Policies
     * @statuscode 204 If the re-ordering of Policies was successful.
     * @statuscode 404 If the Service does not exist.
     */
    public void reorderServicePolicies(String organizationId, String serviceId, String version, PolicyChainBean policyChain) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a Policy Chain for the specific Service version.  A
     * Policy Chain is a useful summary to better understand which Policies would be
     * executed for a request to this Service through a particular Plan offered by the
     * Service.  Often this information is interesting prior to create a Contract with
     * the Service.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param planId         The Plan ID.
     * @return A Policy Chain.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Policy Chain
     * @statuscode 200 If the Policy Chain is successfully returned.
     * @statuscode 404 If the Service does not exist.
     */
    public PolicyChainBean getServicePolicyChain(String organizationId, String serviceId, String version, String planId) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Contracts created with this Service.  This
     * will return Contracts created by between any Application and through any Plan.
     *
     * @param organizationId The Organization ID.
     * @param serviceId      The Service ID.
     * @param version        The Service version.
     * @param page           Which page of Contracts to return.
     * @param pageSize       The number of Contracts per page to return.
     * @return A list of Contracts.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException          when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Service Contracts
     * @statuscode 200 If the list of Contracts is successfully returned.
     * @statuscode 404 If the Service does not exist.
     */
    public List<ContractSummaryBean> getServiceVersionContracts(String organizationId, String serviceId, String version, int page, int pageSize) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /*
     * PLANS
     */

    /**
     * Use this endpoint to create a new Plan.  Note that it is important to also
     * create an initial version of the Plan (e.g. 1.0).  This can either be done
     * by including the 'initialVersion' property in the request, or by immediately following
     * up with a call to "Create Plan Version".  If the former is done, then a first
     * Plan version will be created automatically by this endpoint.
     *
     * @param organizationId The Organization ID.
     * @param bean           Information about the new Plan.
     * @return Full details about the newly created Plan.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanAlreadyExistsException    when trying to create an Plan that already exists
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException          when the user attempts the create with an invalid name
     * @summary Create Plan
     * @statuscode 200 If the Plan is successfully created.
     * @statuscode 404 If the Organization does not exist.
     */
    public PlanBean createPlan(String organizationId, NewPlanBean bean)
            throws OrganizationNotFoundException, PlanAlreadyExistsException, NotAuthorizedException,
            InvalidNameException;

    /**
     * Use this endpoint to retrieve information about a single Plan by ID.  Note
     * that this only returns information about the Plan, not about any particular
     * *version* of the Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @return An Plan.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException  when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Plan By ID
     * @statuscode 200 If the Plan is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Plan does not exist.
     */
    public PlanBean getPlan(String organizationId, String planId) throws PlanNotFoundException,
            NotAuthorizedException;

    /**
     * This endpoint returns audit activity information about the Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param page           Which page of activity should be returned.
     * @param pageSize       The number of entries per page to return.
     * @return A list of audit activity entries.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException  when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Plan Activity
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Plan does not exist.
     */
    public SearchResultsBean<AuditEntryBean> getPlanActivity(String organizationId, String planId, int page, int pageSize) throws PlanNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Plans in the Organization.
     *
     * @param organizationId The Organization ID.
     * @return A list of Plans.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Plans
     * @statuscode 200 If the list of Plans is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     */
    public List<PlanSummaryBean> listPlans(String organizationId)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update information about a Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param bean           Updated Plan information.
     * @throws PlanNotFoundException  when trying to get, update, or remove a plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Plan
     * @statuscode 204 If the Plan is updated successfully.
     * @statuscode 404 If the Plan does not exist.
     * when trying to get, update, or remove an plan that does not exist
     */
    public void updatePlan(String organizationId, String planId, UpdatePlanBean bean)
            throws PlanNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to create a new version of the Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param bean           Initial information about the new Plan version.
     * @return Full details about the newly created Plan version.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException   when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException  when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidVersionException when the user attempts to use an invalid version value
     * @summary Create Plan Version
     * @statuscode 200 If the Plan version is created successfully.
     * @statuscode 404 If the Plan does not exist.
     * @statuscode 409 If the Plan version already exists.
     */
    public PlanVersionBean createPlanVersion(String organizationId, String planId, NewPlanVersionBean bean) throws PlanNotFoundException,
            NotAuthorizedException, InvalidVersionException, PlanVersionAlreadyExistsException;

    /**
     * Use this endpoint to list all of the versions of a Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @return A list of Plans.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException  when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Plan Versions
     * @statuscode 200 If the list of Plan versions is successfully returned.
     */
    public List<PlanVersionSummaryBean> listPlanVersions(String organizationId, String planId) throws PlanNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get detailed information about a single version of
     * a Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @return An Plan version.
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Plan Version
     * @statuscode 200 If the Plan version is successfully returned.
     * @statuscode 404 If the Plan version does not exist.
     */
    public PlanVersionBean getPlanVersion(String organizationId, String planId, String version)
            throws PlanVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get audit activity information for a single version of the
     * Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @param page           Which page of activity data to return.
     * @param pageSize       The number of entries per page to return.
     * @return A list of audit entries.
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException       when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Plan Version Activity
     * @statuscode 200 If the audit activity entries are successfully returned.
     * @statuscode 404 If the Plan version does not exist.
     */
    public SearchResultsBean<AuditEntryBean> getPlanVersionActivity(String organizationId, String planId, String version, int page, int pageSize)
            throws PlanVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add a new Policy to the Plan version.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @param bean           Information about the new Policy.
     * @return Full details about the newly added Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException  when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Add Plan Policy
     * @statuscode 200 If the Policy is successfully added.
     * @statuscode 404 If the Plan does not exist.
     */
    public EnrichedPolicySummaryBean createPlanPolicy(String organizationId, String planId, String version, NewPolicyBean bean) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get information about a single Policy in the Plan version.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @param policyId       The Policy ID.
     * @return Full information about the Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException  when trying to get, update, or remove a plan version that does not exist
     * @throws PolicyNotFoundException       when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Plan Policy
     * @statuscode 200 If the Policy is successfully returned.
     * @statuscode 404 If the Plan does not exist.
     */
    public EnrichedPolicySummaryBean getPlanPolicy(String organizationId, String planId, String version, long policyId) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the meta-data or configuration of a single Plan Policy.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @param policyId       The Policy ID.
     * @param bean           New meta-data and/or configuration for the Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException  when trying to get, update, or remove a plan version that does not exist
     * @throws PolicyNotFoundException       when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Update Plan Policy
     * @statuscode 204 If the Policy was successfully updated.
     * @statuscode 404 If the Plan does not exist.
     * @statuscode 404 If the Policy does not exist.
     */
    public void updatePlanPolicy(String organizationId, String planId, String version, long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Policy from the Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @param policyId       The Policy ID.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException  when trying to get, update, or remove a plan version that does not exist
     * @throws PolicyNotFoundException       when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Remove Plan Policy
     * @statuscode 204 If the Policy was successfully deleted.
     * @statuscode 404 If the Plan does not exist.
     * @statuscode 404 If the Policy does not exist.
     */
    public void deletePlanPolicy(String organizationId, String planId, String version, long policyId) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to list all of the Policies configured for the Plan.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @return A List of Policies.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException  when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List All Plan Policies
     * @statuscode 200 If the list of Policies is successfully returned.
     * @statuscode 404 If the Plan does not exist.
     */
    public List<PolicySummaryBean> listPlanPolicies(String organizationId, String planId, String version)
            throws OrganizationNotFoundException, PlanVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to change the order of Policies for a Plan.  When a
     * Policy is added to the Plan, it is added as the last Policy in the list
     * of Plan Policies.  Sometimes the order of Policies is important, so it
     * is often useful to re-order the Policies by invoking this endpoint.  The body
     * of the request should include all of the Policies for the Plan, in the
     * new desired order.  Note that only the IDs of each of the Policies is actually
     * required in the request, at a minimum.
     *
     * @param organizationId The Organization ID.
     * @param planId         The Plan ID.
     * @param version        The Plan version.
     * @param policyChain    The Policies in the desired order.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException  when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Re-Order Plan Policies
     * @statuscode 204 If the re-ordering of Policies was successful.
     * @statuscode 404 If the Plan does not exist.
     */
    public void reorderPlanPolicies(String organizationId, String planId, String version, PolicyChainBean policyChain) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, NotAuthorizedException;

    /*
     * MEMBERS
     */

    /**
     * Grant membership in a role to a user.
     *
     * @param organizationId The Organization ID.
     * @param bean           Role to grant, and the ID of the user.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws RoleNotFoundException         when a request is sent for a role that does not exist
     * @throws UserNotFoundException         when a request is sent for a user who does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Grant Membership(s)
     * @statuscode 204 If the membership was successfully granted.
     */
    public void grant(String organizationId, GrantRoleBean bean) throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException, NotAuthorizedException;

    /**
     * Revoke membership in a role.
     *
     * @param organizationId The organization ID.
     * @param roleId         The role ID.
     * @param userId         The user ID.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws RoleNotFoundException         when a request is sent for a role that does not exist
     * @throws UserNotFoundException         when a request is sent for a user who does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Revoke Single Membership
     * @statuscode 204 If the membership was successfully revoked.
     */
    public void revoke(String organizationId, String roleId, String userId)
            throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException,
            NotAuthorizedException;

    /**
     * Revoke all of a user's role memberships from the org.
     *
     * @param organizationId The organization ID.
     * @param userId         The user ID.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws RoleNotFoundException         when a request is sent for a role that does not exist
     * @throws UserNotFoundException         when a request is sent for a user who does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Revoke All Memberships
     * @statuscode 204 If the user's memberships were successfully revoked.
     * @statuscode 404 If the user does not exist.
     */
    public void revokeAll(String organizationId, String userId) throws OrganizationNotFoundException, RoleNotFoundException,
            UserNotFoundException, NotAuthorizedException;

    /**
     * Transfer ownership of an organization to another member.
     *
     * @param organizationId The Organization ID.
     * @param bean           UserIds of the current owner and new owner.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws MemberNotFoundException       when attempting to transfer ownership to or from a member that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Grant Membership(s)
     * @statuscode 204 If the ownership was successfully transferred.
     */
    public void transferOrgOwnership(String organizationId, TransferOwnershipBean bean) throws OrganizationNotFoundException, MemberNotFoundException, NotAuthorizedException;

    /**
     * Lists all members of the organization.
     *
     * @param organizationId The organization ID.
     * @return List of members.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException        when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary List Organization Members
     * @statuscode 200 If the list of members is returned successfully.
     */
    public List<MemberBean> listMembers(String organizationId) throws OrganizationNotFoundException, NotAuthorizedException;


    /* -----------------------------------------------------------------
     *                             Metrics
     * ----------------------------------------------------------------- */

    /**
     * Retrieves metrics/analytics information for a specific service.
     *
     * @param organizationId The organization ID.
     * @param serviceId      The service ID.
     * @param version        The service version.
     * @param fromDate       The start of a valid date range.
     * @param toDate         The end of a valid date range.
     * @return Response statistics metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @summary Get Service Response Statistics (Histogram)
     * @statuscode 200 If the metrics data is successfully returned.
     */
    public ServiceMetricsBean getServiceUsage(String organizationId,
                                              String serviceId, String version, String fromDate,
                                              String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;

    /**
     * Retrieves the marketplace information for a service version.
     * The information contians:
     * <ul>
     * <li>Service uptime for the last month</li>
     * <li>Service distinct active consumers - aggregated from the metrics endpoint</li>
     * <li>Service followers</li>
     * </ul>
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @return
     * @throws NotAuthorizedException
     * @throws InvalidMetricCriteriaException
     */
    public ServiceMarketInfoBean getServiceMarketInfo(String organizationId, String serviceId, String version) throws NotAuthorizedException, InvalidMetricCriteriaException;

    /**
     * Reject a user's request for membership
     *
     * @param organizationId The organization's ID
     * @param userId         The user's id
     * @throws NotAuthorizedException
     */
    public void rejectMembershipRequest(String organizationId, String userId) throws NotAuthorizedException;

    /**
     * ANNOUNCEMENTS
     **/
    public ServiceBean addServiceFollower(String organizationId, String serviceId, String userId) throws ServiceNotFoundException, NotAuthorizedException;

    public ServiceBean removeServiceFollower(String organizationId, String serviceId, String userId) throws ServiceNotFoundException, NotAuthorizedException;

    public ServiceFollowers getServiceFollowers(String organizationId, String serviceId) throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * SUPPORT
     **/
    public SupportBean createServiceSupportTicket(String organizationId, String serviceId, NewSupportBean bean) throws ServiceNotFoundException, NotAuthorizedException;

    public SupportBean updateServiceSupportTicket(String organizationId, String serviceId, String supportId, UpdateSupportBean bean) throws ServiceNotFoundException, NotAuthorizedException;

    public SupportBean getServiceSupportTicket(String organizationId, String serviceId, String supportId) throws ServiceNotFoundException, NotAuthorizedException;

    public void deleteServiceSupportTicket(String organizationId, String serviceId, String supportId) throws ServiceNotFoundException, NotAuthorizedException;

    public List<SupportBean> listServiceSupportTickets(String organizationId, String serviceId) throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * SUPPORT COMMENTS
     **/
    public SupportComment addServiceSupportComment(String supportId, NewSupportComment bean) throws NotAuthorizedException;

    public SupportComment updateServiceSupportComment(String supportId, String commentId, UpdateSupportComment bean) throws NotAuthorizedException;

    public void deleteServiceSupportComment(String supportId, String commentId) throws NotAuthorizedException;

    public SupportComment getServiceSupportComment(String supportId, String commentId) throws NotAuthorizedException;

    public List<SupportComment> listServiceSupportComments(String supportId) throws NotAuthorizedException;

    /**
     * Retrieve all events with a given organization as destination
     *
     * @param organizationId The organization's ID
     * @return
     * @throws NotAuthorizedException
     */
    public List<EventBean> getOrganizationAllIncomingEvents(String organizationId) throws NotAuthorizedException;

    //TODO- Javadocs for new endpoints

    /**
     * Get all outgoing event for an organization
     *
     * @param organizationId
     * @return
     * @throws NotAuthorizedException
     */
    public List<EventBean> getOrganizationAllOutgoingEvents(String organizationId) throws NotAuthorizedException;

    /**
     * Get an organization's outgoing events by type and status
     *
     * @param organizationId
     * @param type
     * @param <T>
     * @return
     * @throws NotAuthorizedException
     * @throws InvalidEventException
     */
    public <T> List<T> getOrganizationOutgoingEventsByTypeAndStatus(String organizationId, String type) throws NotAuthorizedException, InvalidEventException;

    /**
     * Get an organization's incoming events by type and status
     *
     * @param organizationId
     * @param type
     * @param <T>
     * @return
     * @throws NotAuthorizedException
     * @throws InvalidEventException
     */
    public <T> List<T> getOrganizationIncomingEventsByTypeAndStatus(String organizationId, String type) throws NotAuthorizedException, InvalidEventException;

    /**
     * Delete an event by id
     *
     * @param organizationId
     * @param id
     * @throws NotAuthorizedException
     * @throws InvalidEventException
     * @throws EventNotFoundException
     */
    public void deleteEvent(String organizationId, Long id) throws NotAuthorizedException, InvalidEventException, EventNotFoundException;

    /**
     * Cancel a pending contract request
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @param request
     * @throws NotAuthorizedException
     * @throws InvalidEventException
     * @throws EventNotFoundException
     */
    public void cancelContractRequest(String organizationId, String serviceId, String version, ContractCancellationBean request) throws NotAuthorizedException, InvalidEventException, EventNotFoundException;

    /**
     * Cancel a pending membership request
     *
     * @param organizationId
     * @throws InvalidEventException
     * @throws EventNotFoundException
     */
    public void cancelMembershipRequest(String organizationId) throws InvalidEventException, EventNotFoundException;

    /**
     * Request a contract between an application version and a service version
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param bean
     * @return
     * @throws OrganizationNotFoundException
     * @throws ApplicationNotFoundException
     * @throws ServiceNotFoundException
     * @throws PlanNotFoundException
     * @throws ContractAlreadyExistsException
     * @throws NotAuthorizedException
     */
    public ContractBean requestContract(String organizationId,
                                        String applicationId, String version,
                                        NewContractRequestBean bean) throws OrganizationNotFoundException, ApplicationNotFoundException,
            ServiceNotFoundException, PlanNotFoundException, ContractAlreadyExistsException,
            NotAuthorizedException;

    /**
     * Reject a contract request
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param response
     * @throws NotAuthorizedException
     */
    public void rejectContractRequest(String organizationId, String applicationId, String version, NewContractBean response) throws NotAuthorizedException;

    /**
     * Accept a contract request
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param response
     * @return
     * @throws NotAuthorizedException
     */
    public ContractBean acceptContractRequest(String organizationId, String applicationId, String version, NewContractBean response) throws NotAuthorizedException;

    /**
     * Reissue an application version's OAuth2 credentials
     *
     * @param orgId
     * @param appId
     * @param version
     * @return
     */
    public NewOAuthCredentialsBean reissueAppVersionOAuthCredentials(String orgId, String appId, String version);

    /**
     * Reissue an application version's API key
     *
     * @param orgId
     * @param appId
     * @param version
     * @return
     */
    public NewApiKeyBean reissueAppVersionApiKey(String orgId, String appId, String version);

    /**
     * Delete a service version
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @throws NotAuthorizedException
     */
    public void deleteServiceVersion(String organizationId, String serviceId, String version) throws NotAuthorizedException;

    /**
     * Get a service's tags
     *
     * @param organizationId
     * @param serviceId
     * @return
     */
    public ServiceTagsBean getTags(String organizationId, String serviceId);

    /**
     * update a service's tags
     *
     * @param organizationId
     * @param serviceId
     * @param tags
     * @throws NotAuthorizedException
     */
    public void updateTags(String organizationId, String serviceId, ServiceTagsBean tags) throws NotAuthorizedException;

    /**
     * Delete a service's tag
     *
     * @param organizationId
     * @param serviceId
     * @param tag
     * @throws NotAuthorizedException
     */
    public void deleteTag(String organizationId, String serviceId, TagBean tag) throws NotAuthorizedException;

    /**
     * Add a tag to a service
     *
     * @param organizationId
     * @param serviceId
     * @param tag
     * @throws NotAuthorizedException
     */
    public void addTag(String organizationId, String serviceId, TagBean tag) throws NotAuthorizedException;

    /**
     * Retrieve an application version's OAuth tokens
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @return
     * @throws NotAuthorizedException
     */
    public OAuth2TokenPaginationBean getApplicationVersionOAuthTokens(String organizationId, String applicationId, String version, String offset) throws NotAuthorizedException;

    /**
     * Add a branding to a service
     *
     * @param organizationId
     * @param serviceId
     * @param branding
     * @throws NotAuthorizedException
     */
    public void addBrandingToService(String organizationId, String serviceId, ServiceBrandingSummaryBean branding) throws NotAuthorizedException;

    /**
     * Remove a branding from a service
     *
     * @param organizationId
     * @param serviceId
     * @param brandingId
     * @throws NotAuthorizedException
     */
    public void removeBrandingFromService(String organizationId, String serviceId, String brandingId) throws NotAuthorizedException;

    /**
     * Toggle the rate limit on a service, if present, for an application
     *
     * @param serviceOrganizationId
     * @param serviceId
     * @param serviceVersion
     * @param applicationOrganizationId
     * @param applicationId
     * @param applicationVersion
     * @throws NotAuthorizedException
     */
    public void toggleRateLimitForApplicationVersion(String serviceOrganizationId, String serviceId, String serviceVersion, RateLimitToggleRequest request) throws NotAuthorizedException;
}
