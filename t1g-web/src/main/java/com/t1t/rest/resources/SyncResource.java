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
import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@Api(value = "/sync", description = "Endpoints that serve to sync the manager data with the gateway")
@Path("/sync")
@ApplicationScoped
public class SyncResource implements ISyncResource {

    @Inject
    private SyncFacade syncFacade;

    @Override
    @ApiOperation(value = "Sync everything",
            notes = "Sync everything")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    public Response syncAll() {
        syncFacade.syncAll();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync services",
            notes = "Sync services")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/services")
    public Response syncServices() {
        syncFacade.syncServices();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync applications",
            notes = "Sync applications")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/applications")
    public Response syncApplications() {
        syncFacade.syncApplications();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync all policies",
            notes = "Sync all policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies")
    public Response syncAllPolicies() {
        syncFacade.syncPolicies();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync service policies",
            notes = "Sync service policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/services")
    public Response syncServicePolicies() {
        syncFacade.syncServicePolicies();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync contracts policies",
            notes = "Sync contracts policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/contracts")
    public Response syncContractPolicies() {
        syncFacade.syncContractPolicies();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync consent policies",
            notes = "Sync consent policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/consent")
    public Response syncConsentPolicies() {
        syncFacade.syncConsentPolicies();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync plan policies",
            notes = "Sync plan policies")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/policies/plans")
    public Response syncPlanPolicies() {
        syncFacade.syncPlanPolicies();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Backup tokens for rebuild",
            notes = "Backup oauth tokens prior to a gateway rebuild")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/oauth/tokens/backup")
    public Response backupTokens() {
        syncFacade.backUpOAuthTokens();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Sync tokens",
            notes = "Sync tokens")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("/oauth/tokens/restore")
    public Response syncTokens() {
        syncFacade.syncTokens();
        return Response.noContent().build();
    }

    @Override
    @ApiOperation(value = "Delete back",
            notes = "Sync everything")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @DELETE
    @Path("/oauth/tokens")
    public Response deleteTokenBackup() {
        syncFacade.deleteBackedUpTokens();
        return Response.noContent().build();
    }
}