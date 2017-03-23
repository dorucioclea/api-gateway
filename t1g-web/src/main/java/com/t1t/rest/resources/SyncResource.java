package com.t1t.rest.resources;

import com.t1t.apim.facades.SyncFacade;
import com.t1t.apim.rest.resources.ISyncResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@Api(value = "/sync", description = "Endpoints that serve to sync the manager data with the gateway")
@Path("/sync")
@ApplicationScoped
public class SyncResource implements ISyncResource {

    @Inject private SyncFacade syncFacade;

    @Override
    @ApiOperation(value =  "Sync everything",
            notes = "Sync everything")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    public void syncAll() {
        syncFacade.syncAll();
    }

    @Override
    @ApiOperation(value =  "Sync users",
            notes = "Sync users")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/users")
    public void syncUsers() {
        syncFacade.syncUsers();
    }

    @Override
    @ApiOperation(value =  "Sync services",
            notes = "Sync services")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/services")
    public void syncServices() {
        syncFacade.syncServices();
    }

    @Override
    @ApiOperation(value =  "Sync applications",
            notes = "Sync applications")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/applications")
    public void syncApplications() {
        syncFacade.syncApplications();
    }

    @Override
    @ApiOperation(value =  "Sync all policies",
            notes = "Sync all policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies")
    public void syncAllPolicies() {
        syncFacade.syncPolicies();
    }

    @Override
    @ApiOperation(value =  "Sync service policies",
            notes = "Sync service policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/services")
    public void syncServicePolicies() {
        syncFacade.syncServicePolicies();
    }

    @Override
    @ApiOperation(value =  "Sync contracts policies",
            notes = "Sync contracts policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/contracts")
    public void syncContractPolicies() {
        syncFacade.syncContractPolicies();
    }

    @Override
    @ApiOperation(value =  "Sync consent policies",
            notes = "Sync consent policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/consent")
    public void syncConsentPolicies() {
        syncFacade.syncConsentPolicies();
    }

    @Override
    @ApiOperation(value =  "Sync plan policies",
            notes = "Sync plan policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/plans")
    public void syncPlanPolicies() {
        syncFacade.syncPlanPolicies();
    }

    @Override
    @ApiOperation(value =  "Backup tokens for rebuild",
            notes = "Backup oauth tokens prior to a gateway rebuild")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/oauth/tokens/backup")
    public void backupTokens() {
        syncFacade.backUpOAuthTokens();
    }

    @Override
    @ApiOperation(value =  "Sync tokens",
            notes = "Sync tokens")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/oauth/tokens/restore")
    public void syncTokens() {
        syncFacade.syncTokens();
    }

    @Override
    @ApiOperation(value =  "Delete back",
            notes = "Sync everything")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @DELETE
    @Path("/oauth/tokens")
    public void deleteTokenBackup() {
        syncFacade.deleteBackedUpTokens();
    }
}