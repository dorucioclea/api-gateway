package com.t1t.apim.rest.resources;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface ISyncResource {

    /**
     * Back up the gateways OAuth tokens (for example, prior to a gateway rebuild
     */
    public void backupTokens();

    /**
     * Sync the OAuth tokens
     */
    public void syncTokens();

    /**
     * Delete the backed up tokens
     */

    public void deleteTokenBackup();

    /**
     * Sync services
     */
    public void syncServices();

    /**
     * Sync applications
     */
    public void syncApplications();

    /**
     * Sync users
     */
    public void syncUsers();

    /**
     * Sync service policies
     */
    public void syncServicePolicies();

    /**
     * Sync consent policies
     */
    public void syncConsentPolicies();

    /**
     * Sync plan policies
     */
    public void syncPlanPolicies();

    /**
     * Sync contract policies
     */
    public void syncContractPolicies();

    /**
     * Sync all policies
     */
    public void syncAllPolicies();

    /**
     * Sync everything
     */
    public void syncAll();
}
