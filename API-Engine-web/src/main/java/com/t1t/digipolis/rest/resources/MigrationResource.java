package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.AbstractRestException;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.InvalidServiceStatusException;
import com.t1t.digipolis.apim.exceptions.ServiceVersionNotFoundException;
import com.t1t.digipolis.apim.facades.MigrationFacade;
import com.t1t.digipolis.apim.rest.resources.IMigrationResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javafx.scene.input.KeyCode.M;

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

    @Override
    @ApiOperation(value = "Migrate ACL",
            notes = "Migrate ACL endpoint")
    @ApiResponses({@ApiResponse(code = 204, message = "Migration complete?")})
    @POST
    @Path("/acl/migrate")
    public void migrateToAcl() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException {
        migrationFacade.migrateToAcl();
    }

    @Override
    @ApiOperation(value =  "Rename Application CustomId",
            notes = "Update applications custom Id's to contain version")
    @ApiResponses({@ApiResponse(code = 204, message = "Rename complete")})
    @POST
    @Path("applications/rename")
    public void updateConsumersCustomId() throws AbstractRestException {
        migrationFacade.renameApplicationCustomIds();
    }

    @Override
    @ApiOperation(value =  "Synchronize business model with Kong gateways",
                  notes = "Use this endpoint to synchronize the business model with one or more gateways. This endpoint can be used for zero downtime deployment or in case of forced synchronization.")
    @ApiResponses({@ApiResponse(code = 204, message = "Rename complete")})
    @POST
    @Path("sync/apikeys")
    public void syncGateways() throws AbstractRestException{
        migrationFacade.syncBusinessModel();
    }
}