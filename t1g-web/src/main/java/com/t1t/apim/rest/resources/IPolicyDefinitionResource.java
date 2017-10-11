package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.apim.beans.policies.UpdatePolicyDefinitionBean;
import com.t1t.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.apim.exceptions.PolicyDefinitionAlreadyExistsException;
import com.t1t.apim.exceptions.PolicyDefinitionNotFoundException;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

/**
 * The Policy Definition API.
 */
public interface IPolicyDefinitionResource {

    /**
     * This endpoint returns a list of all policy definitions that have been added
     * to apiman.
     *
     * @return A list of policy definitions.
     * @throws NotAuthorizedException when not authorized to invoke this method
     * @summary List Policy Definitions
     * @statuscode 200 If the policy definition list is successfully returned.
     */
    public List<PolicyDefinitionSummaryBean> list() throws NotAuthorizedException;

    /**
     * Use this endpoint to add a policy definition to apiman.  The policy definition
     * can optionall include the 'id' property.  If no 'id' is supplied, one will be
     * generated based on the name.
     *
     * @param bean The policy definition to add.
     * @return Details about the policy definition that was added.
     * @throws PolicyDefinitionAlreadyExistsException when trying to create a Policy Definition that already exists
     * @throws NotAuthorizedException                 when not authorized to invoke this method
     * @summary Add Policy Definition
     * @servicetag admin
     * @statuscode 200 If the policy definition is added successfully.
     */
    public PolicyDefinitionBean create(PolicyDefinitionBean bean) throws PolicyDefinitionAlreadyExistsException, NotAuthorizedException;

    /**
     * Use this endpoint to get a single policy definition by its ID.
     *
     * @param policyDefinitionId The ID of the policy definition.
     * @return A policy definition if found.
     * @throws PolicyDefinitionNotFoundException when trying to get, update, or remove a policy definition that does not exist
     * @throws NotAuthorizedException            when not authorized to invoke this method
     * @summary Get Policy Definition by ID
     * @statuscode 200 If the policy definition is returned successfully.
     */
    public PolicyDefinitionBean get(String policyDefinitionId) throws PolicyDefinitionNotFoundException, NotAuthorizedException;

    /**
     * Update the meta information about a policy definition.
     *
     * @param policyDefinitionId The policy definition ID.
     * @param bean               New meta-data for the policy definition.
     * @throws PolicyDefinitionNotFoundException when trying to get, update, or remove a policy definition that does not exist
     * @throws NotAuthorizedException            when not authorized to invoke this method
     * @summary Update Policy Definition
     * @servicetag admin
     * @statuscode 204 If the update was successful.
     */
    public void update(String policyDefinitionId, UpdatePolicyDefinitionBean bean)
            throws PolicyDefinitionNotFoundException, NotAuthorizedException;

    /**
     * Use this endpoint to remove a policy definition by its ID.  If the policy definition
     * was added automatically from an installed plugin, this will fail.  The only way to
     * remove such policy definitions is to remove the plugin.
     *
     * @param policyDefinitionId The policy definition ID.
     * @throws PolicyDefinitionNotFoundException when trying to get, update, or remove a policy definition that does not exist
     * @throws NotAuthorizedException            when not authorized to invoke this method
     * @summary Delete policy definition.
     * @servicetag admin
     * @statuscode 204 If the policy definition is successfully deleted.
     */
    public void delete(String policyDefinitionId)
            throws PolicyDefinitionNotFoundException, NotAuthorizedException;

}
