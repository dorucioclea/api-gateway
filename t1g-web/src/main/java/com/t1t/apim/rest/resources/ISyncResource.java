package com.t1t.apim.rest.resources;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface ISyncResource {

    /**
     * Back up the gateways OAuth tokens (for example, prior to a gateway rebuild
     */
    public Response backupTokens();

    /**
     * Sync the OAuth tokens
     */
    public Response syncTokens();

    /**
     * Delete the backed up tokens
     */

    public Response deleteTokenBackup();

    /**
     * Sync services
     */
    public Response syncServices();

    /**
     * Sync applications
     */
    public Response syncApplications();

    /**
     * Sync service policies
     */
    public Response syncServicePolicies();

    /**
     * Sync consent policies
     */
    public Response syncConsentPolicies();

    /**
     * Sync plan policies
     */
    public Response syncPlanPolicies();

    /**
     * Sync contract policies
     */
    public Response syncContractPolicies();

    /**
     * Sync all policies
     */
    public Response syncAllPolicies();

    /**
     * Sync everything
     */
    public Response syncAll();
}
