package com.t1t.digipolis.apim.auth.rest.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 31/07/16.
 */
@Api(value = "/gtw", description = "Gateway Resource.")
@Path("/gtw")
@ApplicationScoped
public class GatewayResource {

    @ApiOperation(value = "IDP Callback URL for the Marketplace",
                  notes = "Use this endpoint if no user is logged in, and a redirect to the IDP is needed. This enpoint is generating the SAML2 SSO redirect request using OpenSAML and the provided IDP URL. The requests specifies the client token expectations, 'jwt' token supported. The clientAppName property is optional and will serve as the JWT audience claim.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = Response.class, message = "Public Key PEM formatted - base64 encoded")
                  })
    @GET
    @Path("/tokens/pub")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJWTPublicKeyPEM() {
        return Response.ok().build();
    }
}
