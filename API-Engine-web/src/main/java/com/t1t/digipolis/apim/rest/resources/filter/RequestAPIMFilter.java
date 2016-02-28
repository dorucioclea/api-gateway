package com.t1t.digipolis.apim.rest.resources.filter;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.rest.JaxRsActivator;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.JWTUtils;
import org.jose4j.jwt.MalformedClaimException;
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
 * The request filter doesn't take into account any JWT.
 * The request filter is based upon APIkey, the application context will be provided.
 */
public class RequestAPIMFilter implements ContainerRequestFilter {
    /**
     * Logger: is not possible to inject logger in filters
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestAPIMFilter.class.getName());
    private static final String HEADER_CONSUMER_USERNAME = "x-consumer-username";//considerred to be an application consumer - we use this to setup an application context
    private static final String HEADER_CONSUMER_ID = "x-consumer-id";
    private static final String HEADER_USER_AUTHORIZATION = "Authorization"; // will contain the JWT user token
    //exclusions
    private static final String REDIRECT_PATH = "/users/idp/redirect";
    private static final String IDP_CALLBACK = "/users/idp/callback";
    private static final String IDP_SLO = "/users/idp/slo";
    private static final String SYSTEM_INFO = "/system";
    private static final String SWAGGER_DOC_URI = "API-Engine-web";
    private static final String SWAGGER_DOC_JSON = "/API-Engine-web/v1/swagger.json";

    //Security context
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext securityAppContext;
    @Inject private AppConfig config;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //dev mode
        LOG.debug("Security context - request:{}", containerRequestContext.getUriInfo().getRequestUri().getPath());
        if (!config.getRestResourceSecurity()) {
            try {
                securityContext.setCurrentUser("admin");
                securityAppContext.setCurrentApplication("dummyapp");
            } catch (StorageException e) {
                throw new IOException(e);
            }
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
            String appId = containerRequestContext.getHeaderString(HEADER_CONSUMER_USERNAME);
            try {
                securityAppContext.setCurrentApplication(appId);
            } catch (StorageException e) {
                throw new IOException(e);
            }
            //Get the authorization header
            String jwt = containerRequestContext.getHeaderString(HEADER_USER_AUTHORIZATION);
            if (jwt != null) {
                //remove Bearer prefix
                jwt = jwt.replaceFirst("Bearer", "").trim();
                String validatedUser = "";
                try {
                    JwtContext jwtContext = JWTUtils.validateHMACToken(jwt);
                    validatedUser = jwtContext.getJwtClaims().getSubject();
                    validatedUser = securityContext.setCurrentUser(ConsumerConventionUtil.createUserUniqueId(validatedUser));
                } catch (InvalidJwtException | UserNotFoundException | MalformedClaimException ex) {
                    //this shouldnt be thrown because of implicit user creation during initial user intake (saml2 provider and JWT issuance)
                    LOG.error("Unauthorized user:{}", validatedUser);
                    containerRequestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity("User cannot access the resource:" + validatedUser)
                            .build());
                    return;
                }
            } else {
                securityContext.setCurrentUser("");
            }
            return;
        }
    }
}

