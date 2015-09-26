package com.t1t.digipolis.apim.auth.rest.resources;

import io.swagger.annotations.Api;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;

/**
 * Created by michallispashidis on 26/09/15.
 */
@Api(value = "/oauth", description = "OAuth 2 API Engine resource.")
@Path("/oauth")
@ApplicationScoped
public class OAuthResource implements IOAuth2Authorization {

}
