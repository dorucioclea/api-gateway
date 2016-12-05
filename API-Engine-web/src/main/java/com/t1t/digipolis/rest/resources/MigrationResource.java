package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.facades.MigrationFacade;
import com.t1t.digipolis.apim.rest.resources.IMigrationResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Api(value = "/migration", description = "Test endpoint. Can be used to validate the url endpoint.")
@Path("/migration")
@ApplicationScoped
public class MigrationResource implements IMigrationResource {
    private static final Logger _LOG = LoggerFactory.getLogger(MigrationResource.class);

    @Inject private MigrationFacade migrationFacade;
    @Inject private ISecurityContext security;

    @Override
    @ApiOperation(value = "Rebuild Gateway",
                  notes = "Rebuild Gateway when empty")
    @ApiResponses({@ApiResponse(code = 204, message = "Rebuild registered")})
    @POST
    @Path("/gtw/rebuild")
    public void rebuild() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        migrationFacade.rebuildGtw();
    }


    //Obsolete should not be used past version 0.6.3
    /*@Override
    @ApiOperation(value = "Migrate ACL",
            notes = "Migrate ACL endpoint")
    @ApiResponses({@ApiResponse(code = 204, message = "Migration complete?")})
    @POST
    @Path("/acl/migrate")
    public void migrateToAcl() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        migrationFacade.migrateToAcl();
    }*/

    @Override
    @ApiOperation(value =  "Rename Application CustomId",
            notes = "Update applications custom Id's to contain version")
    @ApiResponses({@ApiResponse(code = 204, message = "Rename complete")})
    @POST
    @Path("applications/rename")
    public void updateConsumersCustomId() throws AbstractRestException {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        migrationFacade.renameApplicationCustomIds();
    }

    @Override
    @ApiOperation(value =  "Apply Default Service Policies",
            notes = "Apply the default service policies to every service version")
    @ApiResponses({@ApiResponse(code = 204, message = "Rename complete")})
    @POST
    @Path("services/default-policies/apply")
    public void applyDefaultPoliciesToServiceVersions() throws AbstractRestException {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        migrationFacade.applyDefaultPolicies();
    }

    //Obsolete, see "sync/applications/credentials/create-or-sync" endpoint

    /*@Override
    @ApiOperation(value =  "Synchronize business model with Kong gateways",
                  notes = "Use this endpoint to synchronize the business model with one or more gateways. This endpoint can be used for zero downtime deployment or in case of forced synchronization.")
    @ApiResponses({@ApiResponse(code = 204, message = "Rename complete")})
    @POST
    @Path("sync/apikeys")
    public void syncGateways() throws AbstractRestException{
        migrationFacade.syncBusinessModel();
    }*/

    @Override
    @ApiOperation(value =  "Issue JWT credentials for all cosumers.",
                  notes = "Issue JWT credentials for all cosumers, if consumer has JWT credentials, functionality is skipped to prevent renewal.")
    @ApiResponses({@ApiResponse(code = 204, message = "JWT issuance complete")})
    @POST
    @Path("sync/jwt-issuance")
    public void issueJWT() throws AbstractRestException, StorageException {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        migrationFacade.issueJWT();
    }

    @Override
    @ApiOperation(value =  "Sync service policies with plugin ids",
            notes = "Sync service policies with plugin ids")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("sync/service-policies")
    public void updatePoliciesWithGatewayPluginIds() {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        migrationFacade.updatePoliciesWithGatewayPluginIds();
    }

    @Override
    @ApiOperation(value =  "Create/sync application credentials",
            notes = "Create or sync all necessary application credentials. Currently, this enabled key authentication, OAuth authentication and JWT authentication for all non-retired applications")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("sync/applications/credentials/create-or-sync")
    public void syncOrCreateConsumerCredentials() {
        if (!security.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        migrationFacade.syncAndCreateConsumerCredentials();
    }

    //Obsolete should not be used past version 0.8.0
/*    @Override
    @ApiOperation(value =  "Split orgs by context",
            notes = "Use this endpoint in order to split orgs from v0.7x to v0.8.0")
    @ApiResponses({@ApiResponse(code = 204, message = "Split completed")})
    @POST
    @Path("sync/split-orgs")
    public void splitOrgs() throws Exception {
        //migrationFacade.splitOrgs();
    }

    @Override
    @ApiOperation(value =  "Create/sync def pols",
            notes = "Create or sync all necessary application credentials. Currently, this enabled key authentication, OAuth authentication and JWT authentication for all non-retired applications")
    @ApiResponses({@ApiResponse(code = 204, message = "sync complete")})
    @POST
    @Path("sync/services/syncpolicies")
    public void applydefPol() {
        migrationFacade.syncEmptyKongPluginIds();
    }*/
}