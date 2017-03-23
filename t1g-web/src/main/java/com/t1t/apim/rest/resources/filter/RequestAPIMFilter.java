package com.t1t.apim.rest.resources.filter;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.operation.SafeHTTPMethods;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.UserNotFoundException;
import com.t1t.apim.maintenance.MaintenanceController;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.JWTUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
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
    //private static final String HEADER_CONSUMER_USERNAME = "x-consumer-username";//considerred to be an application consumer - we use this to setup an application context
    //private static final String HEADER_CONSUMER_ID = "x-consumer-id";
    private static final String HEADER_USER_AUTHORIZATION = "Authorization"; // will contain the JWT user token
    private static final String HEADER_API_KEY = "apikey";
    //exclusions

    private static final String BASE_PATH = "/t1g-web/v1";
    private static final String REDIRECT_PATH = "/users/idp/redirect";
    private static final String IDP_CALLBACK = "/users/idp/callback";
    private static final String IDP_SLO = "/users/idp/slo";
    private static final String SYSTEM_INFO = "t1g-web/v1/system";
    private static final String MAINTENANCE_PATH = "/admin/maintenance/";
    private static final String SYNC_PATH = "/sync";
    private static final String SEARCH_PATH = "/search/";
    //private static final String SWAGGER_DOC_URI = "t1g-web";
    //private static final String SWAGGER_DOC_JSON = "/t1g-web/v1/swagger.json";


    //Security context
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext securityAppContext;
    @Inject private AppConfig config;
    @Inject private IStorageQuery query;
    @Inject private MaintenanceController maintenance;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String path = containerRequestContext.getUriInfo().getRequestUri().getPath();
        if (maintenance.isEnabled()
                && !path.startsWith(BASE_PATH + MAINTENANCE_PATH)
                && !path.startsWith(BASE_PATH + SEARCH_PATH)
                && !path.startsWith(BASE_PATH + SYNC_PATH)) {
            try {
                SafeHTTPMethods.valueOf(containerRequestContext.getMethod());
            }
            catch (IllegalArgumentException ex) {
                throw ExceptionFactory.maintenanceException(maintenance.getMessage());
            }
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
            //allow access without setting security context.
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(IDP_CALLBACK)) {
            //allow from idp
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(IDP_SLO)) {
            //allow from idp
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(SYSTEM_INFO)) {
            //allow from idp
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
                    JwtClaims jwtClaims = JWTUtils.getUnvalidatedClaims(jwt);
                    //Check if the JWT comes from a user that authenticated using LDAP
                    validatedUser = securityContext.setCurrentUser(jwtClaims);
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
