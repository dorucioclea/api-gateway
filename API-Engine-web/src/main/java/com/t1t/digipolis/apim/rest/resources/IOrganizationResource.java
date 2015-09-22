package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.contracts.NewContractBean;
import com.t1t.digipolis.apim.beans.idm.GrantRolesBean;
import com.t1t.digipolis.apim.beans.members.MemberBean;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.digipolis.apim.beans.plans.*;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyChainBean;
import com.t1t.digipolis.apim.beans.policies.UpdatePolicyBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.kong.model.MetricsUsageList;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The Organization API.
 */
public interface IOrganizationResource {

    /**
     * Use this endpoint to create a new Organization.
     * @summary Create Organization
     * @param bean Information about the new Organization.
     * @statuscode 200 If the Organization was successfully created.
     * @return Full details about the Organization that was created.
     * @throws OrganizationAlreadyExistsException when trying to create an Organization that already exists
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException when the user attempts to create an Organization with an invalid name
     */
    public OrganizationBean create(NewOrganizationBean bean) throws OrganizationAlreadyExistsException,
            NotAuthorizedException, InvalidNameException;

    /**
     * Use this endpoint to get information about a single Organization
     * by its ID.
     * @summary Get Organization By ID
     * @param organizationId The Organization id.
     * @statuscode 200 If the Organization was successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @return The Organization.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public OrganizationBean get(String organizationId) throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Updates meta-information about a single Organization.
     * @summary Update Organization By ID
     * @param organizationId The Organization ID.
     * @param bean Updated Organization information.
     * @statuscode 200 If the Organization meta-data is successfully updated.
     * @statuscode 404 If the Organization does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void update(String organizationId, UpdateOrganizationBean bean)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Returns audit activity information for a single Organization.  The audit
     * information that is returned represents all of the activity associated
     * with this Organization (i.e. an audit log for everything in the Organization).
     * @summary Get Organization Activity
     * @param organizationId The Organization ID.
     * @param page Which page of activity results to return.
     * @param pageSize The number of entries per page.
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @return List of audit/activity entries.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Create Application
     * @param organizationId The Organization ID.
     * @param bean Information about the new Application.
     * @statuscode 200 If the Application is successfully created.
     * @statuscode 404 If the Organization does not exist.
     * @return Full details about the newly created Application.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationAlreadyExistsException when trying to create an Application that already exists
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException when the user attempts the create with an invalid name
     */
    public ApplicationBean createApp(String organizationId, NewApplicationBean bean) throws OrganizationNotFoundException, ApplicationAlreadyExistsException,
            NotAuthorizedException, InvalidNameException;

    /**
     * Use this endpoint to retrieve information about a single Application by ID.  Note
     * that this only returns information about the Application, not about any particular
     * *version* of the Application.
     * @summary Get Application By ID
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @statuscode 200 If the Application is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Application does not exist.
     * @return An Application.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist when trying to get, update, or remove an application that does not exist.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ApplicationBean getApp(String organizationId, String applicationId) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * This endpoint returns audit activity information about the Application.
     * @summary Get Application Activity
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param page Which page of activity should be returned.
     * @param pageSize The number of entries per page to return.
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Application does not exist.
     * @return A list of audit activity entries.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public SearchResultsBean<AuditEntryBean> getAppActivity(
            String organizationId, String applicationId, int page, int pageSize) throws ApplicationNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Applications in the Organization.
     * @summary List Applications
     * @param organizationId The Organization ID.
     * @statuscode 200 If the list of Applications is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @return A list of Applications.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<ApplicationSummaryBean> listApps(String organizationId)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update information about an Application.
     * @summary Update Application
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param bean Updated Application information.
     * @statuscode 204 If the Application is updated successfully.
     * @statuscode 404 If the Application does not exist.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void updateApp(String organizationId, String applicationId, UpdateApplicationBean bean) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to create a new version of the Application.
     * @summary Create Application Version
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param bean Initial information about the new Application version.
     * @statuscode 200 If the Application version is created successfully.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 409 If the Application version already exists.
     * @return Full details about the newly created Application version.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidVersionException when the user attempts to use an invalid version value
     */
    public ApplicationVersionBean createAppVersion(String organizationId, String applicationId, NewApplicationVersionBean bean)
            throws ApplicationNotFoundException, NotAuthorizedException, InvalidVersionException,
            ApplicationVersionAlreadyExistsException;

    /**
     * Use this endpoint to list all of the versions of an Application.
     * @summary List Application Versions
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @statuscode 200 If the list of Application versions is successfully returned.
     * @return A list of Applications.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<ApplicationVersionSummaryBean> listAppVersions(String organizationId, String applicationId) throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get detailed information about a single version of
     * an Application.
     * @summary Get Application Version
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @statuscode 200 If the Application version is successfully returned.
     * @statuscode 404 If the Application version does not exist.
     * @return An Application version.
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ApplicationVersionBean getAppVersion(String organizationId, String applicationId, String version)
            throws ApplicationVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get audit activity information for a single version of the
     * Application.
     * @summary Get Application Version Activity
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param page Which page of activity data to return.
     * @param pageSize The number of entries per page to return.
     * @statuscode 200 If the audit activity entries are successfully returned.
     * @statuscode 404 If the Application version does not exist.
     * @return A list of audit entries.
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Get App Usage Metrics (per Service)
     * @param organizationId The organization ID.
     * @param applicationId The application ID.
     * @param version The application version.
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public AppUsagePerServiceBean getAppUsagePerService(
            String organizationId,  String applicationId,
            String version, String fromDate,
             String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;


    /**
     * Use this endpoint to create a Contract between the Application and a Service.  In order
     * to create a Contract, the caller must specify the Organization, ID, and Version of the
     * Service.  Additionally the caller must specify the ID of the Plan it wished to use for
     * the Contract with the Service.
     * @summary Create a Service Contract
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param bean Required information about the new Contract.
     * @statuscode 200 If the Contract is successfully created.
     * @statuscode 404 If the Application version does not exist.
     * @return Full details about the newly created Contract.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException when trying to get, update, or remove an plan that does not exist
     * @throws ContractAlreadyExistsException when trying to create an Contract that already exists
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ContractBean createContract(String organizationId,
                                       String applicationId, String version,
                                       NewContractBean bean) throws OrganizationNotFoundException, ApplicationNotFoundException,
            ServiceNotFoundException, PlanNotFoundException, ContractAlreadyExistsException,
            NotAuthorizedException;

    /**
     * Use this endpoint to retrieve detailed information about a single Service Contract
     * for an Application.
     * @summary Get Service Contract
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param contractId The ID of the Contract.
     * @statuscode 200 If the Contract is successfully returned.
     * @statuscode 404 If the Application version does not exist.
     * @statuscode 404 If the Contract is not found.
     * @return Details about a single Contract.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws ContractNotFoundException when trying to get, update, or remove a contract that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ContractBean getContract(String organizationId, String applicationId, String version, Long contractId) throws ApplicationNotFoundException,
            ContractNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Contracts for an Application.
     * @summary List All Contracts for an Application
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @statuscode 200 If the list of Contracts is successfully returned.
     * @statuscode 404 If the Application is not found.
     * @return A list of Contracts.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<ContractSummaryBean> getApplicationVersionContracts(String organizationId, String applicationId,  String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get registry style information about all Services that this
     * Application consumes.  This is a useful endpoint to invoke in order to retrieve
     * a summary of every Service consumed by the application.  The information returned
     * by this endpoint could potentially be included directly in a client application
     * as a way to lookup endpoint information for the APIs it wishes to consume.  This
     * variant of the API Registry is formatted as JSON data.
     * @summary Get API Registry (JSON)
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @statuscode 200 If the API Registry information is successfully returned.
     * @statuscode 404 If the Application does not exist.
     * @return API Registry information.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ApiRegistryBean getApiRegistryJSON(String organizationId, String applicationId,  String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get registry style information about all Services that this
     * Application consumes.  This is a useful endpoint to invoke in order to retrieve
     * a summary of every Service consumed by the application.  The information returned
     * by this endpoint could potentially be included directly in a client application
     * as a way to lookup endpoint information for the APIs it wishes to consume.  This
     * variant of the API Registry is formatted as XML data.
     * @summary Get API Registry (XML)
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @statuscode 200 If the API Registry information is successfully returned.
     * @statuscode 404 If the Application does not exist.
     * @return API Registry information.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ApiRegistryBean getApiRegistryXML(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to break all contracts between this application and its services.
     * @summary Break All Contracts
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @statuscode 200 If the operation is successful.
     * @statuscode 404 If the Application does not exist.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void deleteAllContracts(String organizationId, String applicationId, String version)
            throws ApplicationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to break a Contract with a Service.
     * @summary Break Contract
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param contractId The Contract ID.
     * @statuscode 200 If the Contract is successfully broken.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 404 If the Contract does not exist.
     * @throws ApplicationNotFoundException when trying to get, update, or remove an application that does not exist
     * @throws ContractNotFoundException when trying to get, update, or remove a contract that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void deleteContract(String organizationId, String applicationId, String version, Long contractId) throws ApplicationNotFoundException,
            ContractNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add a new Policy to the Application version.
     * @summary Add Application Policy
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param bean Information about the new Policy.
     * @statuscode 200 If the Policy is successfully added.
     * @statuscode 404 If the Application does not exist.
     * @return Full details about the newly added Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PolicyBean createAppPolicy(String organizationId, String applicationId, String version, NewPolicyBean bean) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get information about a single Policy in the Application version.
     * @summary Get Application Policy
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param policyId The Policy ID.
     * @statuscode 200 If the Policy is successfully returned.
     * @statuscode 404 If the Application does not exist.
     * @return Full information about the Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PolicyBean getAppPolicy(String organizationId, String applicationId,  String version, long policyId) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the meta-data or configuration of a single Application Policy.
     * @summary Update Application Policy
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param policyId The Policy ID.
     * @param bean New meta-data and/or configuration for the Policy.
     * @statuscode 204 If the Policy was successfully updated.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 404 If the Policy does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void updateAppPolicy(String organizationId, String applicationId, String version, long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ApplicationVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Policy from the Application.
     * @summary Remove Application Policy
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param policyId The Policy ID.
     * @statuscode 204 If the Policy was successfully deleted.
     * @statuscode 404 If the Application does not exist.
     * @statuscode 404 If the Policy does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void deleteAppPolicy(String organizationId, String applicationId, String version, long policyId) throws OrganizationNotFoundException, ApplicationVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to list all of the Policies configured for the Application.
     * @summary List All Application Policies
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @statuscode 200 If the list of Policies is successfully returned.
     * @statuscode 404 If the Application does not exist.
     * @return A List of Policies.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Re-Order Application Policies
     * @param organizationId The Organization ID.
     * @param applicationId The Application ID.
     * @param version The Application version.
     * @param policyChain The Policies in the desired order.
     * @statuscode 204 If the re-ordering of Policies was successful.
     * @statuscode 404 If the Application does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ApplicationVersionNotFoundException when trying to get, update, or remove a application version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Create Service
     * @param organizationId The Organization ID.
     * @param bean Information about the new Service.
     * @statuscode 200 If the Service is successfully created.
     * @statuscode 404 If the Organization does not exist.
     * @return Full details about the newly created Service.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceAlreadyExistsException when trying to create an Service that already exists
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException when the user attempts the create with an invalid name
     */
    public ServiceBean createService(String organizationId, NewServiceBean bean)
            throws OrganizationNotFoundException, ServiceAlreadyExistsException, NotAuthorizedException,
            InvalidNameException;

    /**
     * Use this endpoint to retrieve information about a single Service by ID.  Note
     * that this only returns information about the Service, not about any particular
     * *version* of the Service.
     * @summary Get Service By ID
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @statuscode 200 If the Service is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Service does not exist.
     * @return A Service.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ServiceBean getService(String organizationId, String serviceId) throws ServiceNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Services in the Organization.
     * @summary List Services
     * @param organizationId The Organization ID.
     * @statuscode 200 If the list of Services is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @return A list of Services.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<ServiceSummaryBean> listServices(String organizationId)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update information about a Service.
     * @summary Update Service
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param bean Updated Service information.
     * @statuscode 204 If the Service is updated successfully.
     * @statuscode 404 If the Service does not exist.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void updateService(String organizationId, String serviceId, UpdateServiceBean bean)
            throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * This endpoint returns audit activity information about the Service.
     * @summary Get Service Activity
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param page Which page of activity should be returned.
     * @param pageSize The number of entries per page to return.
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Service does not exist.
     * @return A list of audit activity entries.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public SearchResultsBean<AuditEntryBean> getServiceActivity(
            String organizationId, String serviceId,
            int page, int pageSize) throws ServiceNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to create a new version of the Service.
     * @summary Create Service Version
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param bean Initial information about the new Service version.
     * @statuscode 200 If the Service version is created successfully.
     * @statuscode 404 If the Service does not exist.
     * @statuscode 409 If the Service version already exists.
     * @return Full details about the newly created Service version.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidVersionException when the user attempts to use an invalid version value
     */
    public ServiceVersionBean createServiceVersion(String organizationId, String serviceId, NewServiceVersionBean bean)
            throws ServiceNotFoundException, NotAuthorizedException, InvalidVersionException,
            ServiceVersionAlreadyExistsException;

    /**
     * Use this endpoint to list all of the versions of a Service.
     * @summary List Service Versions
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @statuscode 200 If the list of Service versions is successfully returned.
     * @return A list of Services.
     * @throws ServiceNotFoundException when trying to get, update, or remove an service that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<ServiceVersionSummaryBean> listServiceVersions(String organizationId,
                                                               String serviceId) throws ServiceNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get detailed information about a single version of
     * a Service.
     * @summary Get Service Version
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 200 If the Service version is successfully returned.
     * @statuscode 404 If the Service version does not exist.
     * @return A Service version.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ServiceVersionBean getServiceVersion(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to retrieve the Service's definition document.  A service
     * definition document can be several different types, depending on the Service
     * type and technology used to define the service.  For example, this endpoint
     * might return a WSDL document, or a Swagger JSON document.
     * @summary Get Service Definition
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 200 If the Service definition is successfully returned.
     * @statuscode 404 If the Service version does not exist.
     * @return The Service Definition document (e.g. a Swagger JSON file).
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public Response getServiceDefinition(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get information about the Managed Service's gateway
     * endpoint.  In other words, this returns the actual live endpoint on the
     * API Gateway - the endpoint that a client should use when invoking the Service.
     * @summary Get Service Endpoint
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 200 If the endpoint information is successfully returned.
     * @statuscode 404 If the Service does not exist.
     * @return The live Service endpoint information.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws InvalidServiceStatusException when the user attempts some action on the service when it is not in an appropriate state/status
     * @throws GatewayNotFoundException when trying to get, update, or remove a gateay that does not exist
     */
    public ServiceVersionEndpointSummaryBean getServiceVersionEndpointInfo(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException;

    /**
     * Use this endpoint to update information about a single version of a Service.
     * @summary Update Service Version
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param bean Updated information about the Service version.
     * @return The updated Service Version.
     * @statuscode 204 If the Service version information was successfully updated.
     * @statuscode 404 If the Service does not exist.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidServiceStatusException when the user attempts some action on the service when it is not in an appropriate state/status
     */
    public ServiceVersionBean updateServiceVersion(String organizationId, String serviceId, String version, UpdateServiceVersionBean bean) throws ServiceVersionNotFoundException, NotAuthorizedException,
            InvalidServiceStatusException;

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
     *
     * <table>
     *   <thead>
     *     <tr><th>Content Type</th><th>Service Definition Type</th></tr>
     *   </thead>
     *   <tbody>
     *     <tr><td>application/json</td><td>SwaggerJSON</td></tr>
     *     <tr><td>application/x-yaml</td><td>SwaggerYAML</td></tr>
     *     <tr><td>application/wsdl+xml</td><td>WSDL</td></tr>
     *   </tbody>
     * </table>
     * @summary Update Service Definition
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 204 If the Service definition was successfully updated.
     * @statuscode 404 If the Service does not exist.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidServiceStatusException when the user attempts some action on the service when it is not in an appropriate state/status
     */
    public void updateServiceDefinition(String organizationId, String serviceId,String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException, InvalidServiceStatusException;

    /**
     * Use this endpoint to get audit activity information for a single version of the
     * Service.
     * @summary Get Service Version Activity
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param page Which page of activity data to return.
     * @param pageSize The number of entries per page to return.
     * @statuscode 200 If the audit activity entries are successfully returned.
     * @statuscode 404 If the Service version does not exist.
     * @return A list of audit entries.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public SearchResultsBean<AuditEntryBean> getServiceVersionActivity(
            String organizationId, String serviceId,
            String version,  int page,
            int pageSize) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to list the Plans configured for the given Service version.
     * @summary List Service Plans
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 200 If the Service plans are successfully returned.
     * @statuscode 404 If the Service cannot be found.
     * @return A list of Service plans.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId, String version)
            throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add a new Policy to the Service version.
     * @summary Add Service Policy
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param bean Information about the new Policy.
     * @statuscode 200 If the Policy is successfully added.
     * @statuscode 404 If the Service does not exist.
     * @return Full details about the newly added Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */

    public PolicyBean createServicePolicy(String organizationId,
                                          String serviceId, String version,
                                          NewPolicyBean bean) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get information about a single Policy in the Service version.
     * @summary Get Service Policy
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param policyId The Policy ID.
     * @statuscode 200 If the Policy is successfully returned.
     * @statuscode 404 If the Service does not exist.
     * @return Full information about the Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PolicyBean getServicePolicy(String organizationId, String serviceId, String version, long policyId) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the meta-data or configuration of a single Service Policy.
     * @summary Update Service Policy
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param policyId The Policy ID.
     * @param bean New meta-data and/or configuration for the Policy.
     * @statuscode 204 If the Policy was successfully updated.
     * @statuscode 404 If the Service does not exist.
     * @statuscode 404 If the Policy does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */

    public void updateServicePolicy(String organizationId, String serviceId, String version, long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Policy from the Service.
     * @summary Remove Service Policy
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param policyId The Policy ID.
     * @statuscode 204 If the Policy was successfully deleted.
     * @statuscode 404 If the Service does not exist.
     * @statuscode 404 If the Policy does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void deleteServicePolicy(String organizationId, String serviceId, String version, long policyId) throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Service's definition document.  When this
     * is done, the "definitionType" field on the Service will be set to None.
     * @summary Remove Service Definition
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 204 If the Service definition was successfully deleted.
     * @statuscode 404 If the Service does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void deleteServiceDefinition(String organizationId, String serviceId, String version)
            throws OrganizationNotFoundException, ServiceVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to list all of the Policies configured for the Service.
     * @summary List All Service Policies
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @statuscode 200 If the list of Policies is successfully returned.
     * @statuscode 404 If the Service does not exist.
     * @return A List of Policies.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Re-Order Service Policies
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param policyChain The Policies in the desired order.
     * @statuscode 204 If the re-ordering of Policies was successful.
     * @statuscode 404 If the Service does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void reorderServicePolicies(String organizationId, String serviceId, String version, PolicyChainBean policyChain) throws OrganizationNotFoundException,
            ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a Policy Chain for the specific Service version.  A
     * Policy Chain is a useful summary to better understand which Policies would be
     * executed for a request to this Service through a particular Plan offered by the
     * Service.  Often this information is interesting prior to create a Contract with
     * the Service.
     * @summary Get Service Policy Chain
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param planId The Plan ID.
     * @statuscode 200 If the Policy Chain is successfully returned.
     * @statuscode 404 If the Service does not exist.
     * @return A Policy Chain.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PolicyChainBean getServicePolicyChain(String organizationId, String serviceId, String version, String planId) throws ServiceVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Contracts created with this Service.  This
     * will return Contracts created by between any Application and through any Plan.
     * @summary List Service Contracts
     * @param organizationId The Organization ID.
     * @param serviceId The Service ID.
     * @param version The Service version.
     * @param page Which page of Contracts to return.
     * @param pageSize The number of Contracts per page to return.
     * @statuscode 200 If the list of Contracts is successfully returned.
     * @statuscode 404 If the Service does not exist.
     * @return A list of Contracts.
     * @throws ServiceVersionNotFoundException when trying to get, update, or remove a service version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Create Plan
     * @param organizationId The Organization ID.
     * @param bean Information about the new Plan.
     * @statuscode 200 If the Plan is successfully created.
     * @statuscode 404 If the Organization does not exist.
     * @return Full details about the newly created Plan.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanAlreadyExistsException when trying to create an Plan that already exists
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidNameException when the user attempts the create with an invalid name
     */
    public PlanBean createPlan(String organizationId, NewPlanBean bean)
            throws OrganizationNotFoundException, PlanAlreadyExistsException, NotAuthorizedException,
            InvalidNameException;

    /**
     * Use this endpoint to retrieve information about a single Plan by ID.  Note
     * that this only returns information about the Plan, not about any particular
     * *version* of the Plan.
     * @summary Get Plan By ID
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @statuscode 200 If the Plan is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Plan does not exist.
     * @return An Plan.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PlanBean getPlan(String organizationId, String planId) throws PlanNotFoundException,
            NotAuthorizedException;

    /**
     * This endpoint returns audit activity information about the Plan.
     * @summary Get Plan Activity
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param page Which page of activity should be returned.
     * @param pageSize The number of entries per page to return.
     * @statuscode 200 If the audit information is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @statuscode 404 If the Plan does not exist.
     * @return A list of audit activity entries.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public SearchResultsBean<AuditEntryBean> getPlanActivity(String organizationId, String planId, int page, int pageSize) throws PlanNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get a list of all Plans in the Organization.
     * @summary List Plans
     * @param organizationId The Organization ID.
     * @statuscode 200 If the list of Plans is successfully returned.
     * @statuscode 404 If the Organization does not exist.
     * @return A list of Plans.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<PlanSummaryBean> listPlans(String organizationId)
            throws OrganizationNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update information about a Plan.
     * @summary Update Plan
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param bean Updated Plan information.
     * @throws PlanNotFoundException  when trying to get, update, or remove a plan that does not exist
     * @statuscode 204 If the Plan is updated successfully.
     * @statuscode 404 If the Plan does not exist.
     * when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void updatePlan(String organizationId, String planId, UpdatePlanBean bean)
            throws PlanNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to create a new version of the Plan.
     * @summary Create Plan Version
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param bean Initial information about the new Plan version.
     * @statuscode 200 If the Plan version is created successfully.
     * @statuscode 404 If the Plan does not exist.
     * @statuscode 409 If the Plan version already exists.
     * @return Full details about the newly created Plan version.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     * @throws InvalidVersionException when the user attempts to use an invalid version value
     */
    public PlanVersionBean createPlanVersion(String organizationId, String planId, NewPlanVersionBean bean) throws PlanNotFoundException,
            NotAuthorizedException, InvalidVersionException, PlanVersionAlreadyExistsException;

    /**
     * Use this endpoint to list all of the versions of a Plan.
     * @summary List Plan Versions
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @statuscode 200 If the list of Plan versions is successfully returned.
     * @return A list of Plans.
     * when trying to get, update, or remove an plan that does not exist
     * @throws PlanNotFoundException when trying to get, update, or remove an plan that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<PlanVersionSummaryBean> listPlanVersions(String organizationId, String planId) throws PlanNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get detailed information about a single version of
     * a Plan.
     * @summary Get Plan Version
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @statuscode 200 If the Plan version is successfully returned.
     * @statuscode 404 If the Plan version does not exist.
     * @return An Plan version.
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PlanVersionBean getPlanVersion( String organizationId, String planId, String version)
            throws PlanVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to get audit activity information for a single version of the
     * Plan.
     * @summary Get Plan Version Activity
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @param page Which page of activity data to return.
     * @param pageSize The number of entries per page to return.
     * @statuscode 200 If the audit activity entries are successfully returned.
     * @statuscode 404 If the Plan version does not exist.
     * @return A list of audit entries.
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public SearchResultsBean<AuditEntryBean> getPlanVersionActivity(String organizationId, String planId, String version, int page, int pageSize)
            throws PlanVersionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to add a new Policy to the Plan version.
     * @summary Add Plan Policy
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @param bean Information about the new Policy.
     * @statuscode 200 If the Policy is successfully added.
     * @statuscode 404 If the Plan does not exist.
     * @return Full details about the newly added Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PolicyBean createPlanPolicy(String organizationId, String planId, String version, NewPolicyBean bean) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            NotAuthorizedException;

    /**
     * Use this endpoint to get information about a single Policy in the Plan version.
     * @summary Get Plan Policy
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @param policyId The Policy ID.
     * @statuscode 200 If the Policy is successfully returned.
     * @statuscode 404 If the Plan does not exist.
     * @return Full information about the Policy.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public PolicyBean getPlanPolicy(String organizationId, String planId, String version, long policyId) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to update the meta-data or configuration of a single Plan Policy.
     * @summary Update Plan Policy
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @param policyId The Policy ID.
     * @param bean New meta-data and/or configuration for the Policy.
     * @statuscode 204 If the Policy was successfully updated.
     * @statuscode 404 If the Plan does not exist.
     * @statuscode 404 If the Policy does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void updatePlanPolicy(String organizationId, String planId, String version, long policyId, UpdatePolicyBean bean) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a Policy from the Plan.
     * @summary Remove Plan Policy
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @param policyId The Policy ID.
     * @statuscode 204 If the Policy was successfully deleted.
     * @statuscode 404 If the Plan does not exist.
     * @statuscode 404 If the Policy does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws PolicyNotFoundException when trying to get, update, or remove a policy that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void deletePlanPolicy(String organizationId, String planId, String version, long policyId) throws OrganizationNotFoundException, PlanVersionNotFoundException,
            PolicyNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to list all of the Policies configured for the Plan.
     * @summary List All Plan Policies
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @statuscode 200 If the list of Policies is successfully returned.
     * @statuscode 404 If the Plan does not exist.
     * @return A List of Policies.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
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
     * @summary Re-Order Plan Policies
     * @param organizationId The Organization ID.
     * @param planId The Plan ID.
     * @param version The Plan version.
     * @param policyChain The Policies in the desired order.
     * @statuscode 204 If the re-ordering of Policies was successful.
     * @statuscode 404 If the Plan does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws PlanVersionNotFoundException when trying to get, update, or remove a plan version that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void reorderPlanPolicies(String organizationId, String planId, String version, PolicyChainBean policyChain) throws OrganizationNotFoundException,
            PlanVersionNotFoundException, NotAuthorizedException;

    /*
     * MEMBERS
     */

    /**
     * Grant membership in a role to a user.
     * @summary Grant Membership(s)
     * @param organizationId The Organization ID.
     * @param bean Roles to grant, and the ID of the user.
     * @statuscode 204 If the membership(s) were successfully granted.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws RoleNotFoundException when a request is sent for a role that does not exist
     * @throws UserNotFoundException when a request is sent for a user who does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void grant(String organizationId, GrantRolesBean bean) throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException, NotAuthorizedException;

    /**
     * Revoke membership in a role.
     * @summary Revoke Single Membership
     * @param organizationId The organization ID.
     * @param roleId The role ID.
     * @param userId The user ID.
     * @statuscode 204 If the membership was successfully revoked.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws RoleNotFoundException when a request is sent for a role that does not exist
     * @throws UserNotFoundException when a request is sent for a user who does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void revoke(String organizationId, String roleId, String userId)
            throws OrganizationNotFoundException, RoleNotFoundException, UserNotFoundException,
            NotAuthorizedException;

    /**
     * Revoke all of a user's role memberships from the org.
     * @summary Revoke All Memberships
     * @param organizationId The organization ID.
     * @param userId The user ID.
     * @statuscode 204 If the user's memberships were successfully revoked.
     * @statuscode 404 If the user does not exist.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws RoleNotFoundException when a request is sent for a role that does not exist
     * @throws UserNotFoundException when a request is sent for a user who does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public void revokeAll(String organizationId, String userId) throws OrganizationNotFoundException, RoleNotFoundException,
            UserNotFoundException, NotAuthorizedException;

    /**
     * Lists all members of the organization.
     * @summary List Organization Members
     * @param organizationId The organization ID.
     * @statuscode 200 If the list of members is returned successfully.
     * @return List of members.
     * @throws OrganizationNotFoundException when trying to get, update, or remove an organization that does not exist
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public List<MemberBean> listMembers(String organizationId) throws OrganizationNotFoundException, NotAuthorizedException;


    /* -----------------------------------------------------------------
     *                             Metrics
     * ----------------------------------------------------------------- */

    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return a full histogram of request count data based on the provided date range
     * and interval.  Valid intervals are:  month, week, day, hour, minute
     *
     * @summary Get Service Usage Metrics
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param interval A valid interval (month, week, day, hour, minute)
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public MetricsUsageList getUsage(String organizationId,
                                       String serviceId,  String version,
                                       HistogramIntervalType interval, String fromDate,
                                        String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;

    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return request count data broken down by application.  It basically answers
     * the question "who is calling my service?".
     *
     * @summary Get Service Usage Metrics (per App)
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    //public UsagePerAppBean getUsagePerApp(String organizationId,  String serviceId, String version,String fromDate, String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;


    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return request count data broken down by plan.  It basically answers
     * the question "which service plans are most used?".
     *
     * @summary Get Service Usage Metrics (per Plan)
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    //public UsagePerPlanBean getUsagePerPlan(String organizationId,  String serviceId, String version, String fromDate, String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;


    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return a full histogram of response statistics data based on the provided date range
     * and interval.  Valid intervals are:  month, week, day, hour, minute
     *
     * The data returned includes total request counts, failure counts, and error counts
     * for each data point in the histogram.
     *
     * @summary Get Service Response Statistics (Histogram)
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param interval A valid interval (month, week, day, hour, minute)
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Response statistics metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ResponseStatsHistogramBean getResponseStats(String organizationId,
                                                       String serviceId, String version,
                                                       HistogramIntervalType interval, String fromDate,
                                                       String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;

    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return total response type statistics over the given date range.  Basically
     * this will return three numbers: total request, # failed responses, # error
     * responses.
     *
     * @summary Get Service Response Statistics (Summary)
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    public ResponseStatsSummaryBean getResponseStatsSummary(
            String organizationId, String serviceId,
            String version, String fromDate,
            String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;

    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return response type statistics broken down by application.
     *
     * @summary Get Service Response Statistics (per App)
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    //public ResponseStatsPerAppBean getResponseStatsPerApp(String organizationId,  String serviceId, String version, String fromDate, String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;


    /**
     * Retrieves metrics/analytics information for a specific service.  This will
     * return response type statistics broken down by plan.
     *
     * @summary Get Service Response Statistics (per Plan)
     * @param organizationId The organization ID.
     * @param serviceId The service ID.
     * @param version The service version.
     * @param fromDate The start of a valid date range.
     * @param toDate The end of a valid date range.
     * @statuscode 200 If the metrics data is successfully returned.
     * @return Usage metrics information.
     * @throws NotAuthorizedException when the user attempts to do or see something that they are not authorized (do not have permission) to
     */
    //public ResponseStatsPerPlanBean getResponseStatsPerPlan(String organizationId, String serviceId,String version, String fromDate,String toDate) throws NotAuthorizedException, InvalidMetricCriteriaException;

}
