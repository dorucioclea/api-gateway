package com.t1t.digipolis.apim.rest.resources.filter;

import com.t1t.digipolis.apim.beans.jwt.IJWT;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.rest.JaxRsActivator;
import com.t1t.digipolis.util.JWTUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by michallispashidis on 20/09/15.
 */
public class RequestAPIMFilter implements ContainerRequestFilter {
    /**
     * Logger: is not possible to inject logger in filters
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestAPIMFilter.class.getName());
    private static final String HEADER_APIKEY_APP = "X-Consumer-Username";//considerred to be an application consumer - we use this to setup an apllication context
    private static final String HEADER_USER_AUTHORIZATION = "Authorization"; // will contain the JWT user token
    //exclusions
    private static final String REDIRECT_PATH = "/users/idp/redirect";
    private static final String IDP_CALLBACK = "/users/idp/callback";
    private static final String IDP_SLO = "/users/idp/slo";
    private static final String SYSTEM_INFO = "/system/status";
    private static final String SWAGGER_DOC_URI = "API-Engine-web";
    private static final String SWAGGER_DOC_JSON = "/API-Engine-web/v1/swagger.json";

    //Security context
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext securityAppContext;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //dev mode
        LOG.debug("Security context - request:{}",containerRequestContext.getUriInfo().getRequestUri().getPath());
        if (!JaxRsActivator.securedMode) {
            securityContext.setCurrentUser("admin");
            securityAppContext.setCurrentApplication("dummyapp");
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(REDIRECT_PATH)) {
            ;//allow access without setting security context.
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(IDP_CALLBACK)) {
            ;//allow from idp
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(IDP_SLO)) {
            ;//allow from idp
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(SYSTEM_INFO)) {
            ;//allow from idp
        } else {
            //Get apikey - app context - SHOULD BE ALWAYS PROVIDED
            String appId = containerRequestContext.getHeaderString(HEADER_APIKEY_APP);
            securityAppContext.setCurrentApplication(appId);//TODO parse consumer name for appid
            //Get the authorization header
            String jwt = containerRequestContext.getHeaderString(HEADER_USER_AUTHORIZATION);
            if(jwt!=null){
                //remove Bearer prefix
                jwt.replaceFirst("Bearer","").trim();
                String validatedUser = "";
                try {
                    JwtContext jwtContext = JWTUtils.validateHMACToken(jwt);
                    validatedUser = (String) jwtContext.getJwtClaims().getClaimValue(IJWT.NAME);
                    validatedUser = securityContext.setCurrentUser(validatedUser);
                } catch (InvalidJwtException|UserNotFoundException ex) {
                    LOG.debug("Unauthorized user:{}", validatedUser);
                    containerRequestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity("User cannot access the resource:"+validatedUser)
                            .build());
                    return;
                }
            }else{
                securityContext.setCurrentUser("");
            }
            return;
        }
    }
}

