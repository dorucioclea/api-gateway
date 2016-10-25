package com.t1t.digipolis.apim.rest.resources.filter;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.operation.OperatingBean;
import com.t1t.digipolis.apim.beans.operation.SafeHTTPMethods;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.rest.JaxRsActivator;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.JWTUtils;
import org.jose4j.jwt.JwtClaims;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javafx.scene.input.KeyCode.M;

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
    //private static final String HEADER_CONSUMER_USERNAME = "x-consumer-username";//considerred to be an application consumer - we use this to setup an application context
    //private static final String HEADER_CONSUMER_ID = "x-consumer-id";
    private static final String HEADER_USER_AUTHORIZATION = "Authorization"; // will contain the JWT user token
    private static final String HEADER_CREDENTIAL_USERNAME = "X-Credential-Username";
    private static final String HEADER_API_KEY = "apikey";
    //exclusions
    private static final String REDIRECT_PATH = "/users/idp/redirect";
    private static final String IDP_CALLBACK = "/users/idp/callback";
    private static final String IDP_SLO = "/users/idp/slo";
    private static final String SYSTEM_INFO = "API-Engine-web/v1/system";
    private static final String SWAGGER_DOC_URI = "API-Engine-web";
    private static final String SWAGGER_DOC_JSON = "/API-Engine-web/v1/swagger.json";


    //Security context
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext securityAppContext;
    @Inject private AppConfig config;
    @Inject private IStorageQuery query;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        try {
            OperatingBean maintenanceMode = query.getMaintenanceModeStatus();
            if (maintenanceMode != null && maintenanceMode.isEnabled()) {
                try {
                    SafeHTTPMethods.valueOf(containerRequestContext.getMethod());
                }
                catch (IllegalArgumentException ex) {
                    throw ExceptionFactory.maintenanceException(maintenanceMode.getMessage());
                }
            }
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
        //dev mode
        LOG.debug("Security context - request:{}", containerRequestContext.getUriInfo().getRequestUri().getPath());
        if (!config.getRestResourceSecurity()) {
            try {
                securityContext.setCurrentUser("admin");
                securityAppContext.setCurrentApplication("dummyorg.dummyapp.version");
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
            try {
                //We shouldn't resolve the application context based on the X-Consumer-id header
                //We do require the apikey in order to resolve which managed application is linked to the 
                String apikey = containerRequestContext.getHeaderString(HEADER_API_KEY);
                ManagedApplicationBean mab = query.resolveManagedApplicationByAPIKey(apikey);
                String managedAppId = mab == null ? "" : ConsumerConventionUtil.createManagedApplicationConsumerName(mab);
                securityAppContext.setCurrentApplication(managedAppId);
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
                    JwtClaims jwtClaims = JWTUtils.validateHMACToken(jwt).getJwtClaims();
                    //Check if the JWT comes from a user that authenticated using LDAP
                    validatedUser = jwtClaims.getSubject() != null ?
                            jwtClaims.getSubject() : jwtClaims.getStringClaimValue(HEADER_CREDENTIAL_USERNAME) != null ?
                            jwtClaims.getStringClaimValue(HEADER_CREDENTIAL_USERNAME) : "";
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

