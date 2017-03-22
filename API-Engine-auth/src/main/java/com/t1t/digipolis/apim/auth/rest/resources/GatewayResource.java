package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.jwt.JWTPubKeyResponse;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.JWTPubKeyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.xml.security.utils.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by michallispashidis on 31/07/16.
 */
@Api(value = "/gtw", description = "Gateway Resource.")
@Path("/gtw")
@ApplicationScoped
public class GatewayResource {
    @Inject private IStorageQuery storageQuery;
    @ApiOperation(value = "Retrieve the gateway's public key",
                  notes = "Retrieve the gateway's public key")
    @ApiResponses({
                          @ApiResponse(code = 200, response = JWTPubKeyResponse.class, message = "Public Key PEM formatted - base64 encoded")
                  })
    @GET
    @Path("/tokens/pub")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJWTPublicKeyPEM() {
        String jwtPubkey = "";
        try {
            List<GatewayBean> gatewayBean = storageQuery.listGatewayBeans();
            if(gatewayBean!=null&&gatewayBean.size()>0){
                //get default - first gateway
                jwtPubkey = gatewayBean.get(0).getJWTPubKey();
            }
        } catch (StorageException e) {
            throw new JWTPubKeyException("Could not retrieve the JWT Pub Key");
        }
        JWTPubKeyResponse response = new JWTPubKeyResponse(Base64.encode(jwtPubkey.getBytes()));
        return Response.ok().entity(response).build();
    }
}
