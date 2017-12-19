package com.t1t.apim.facades;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.events.EventType;
import com.t1t.apim.beans.events.NewEventBean;
import com.t1t.apim.beans.idm.*;
import com.t1t.apim.beans.idp.IdpIssuerBean;
import com.t1t.apim.beans.jwt.IJWT;
import com.t1t.apim.beans.jwt.JWT;
import com.t1t.apim.beans.search.PagingBean;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.*;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.kong.model.KongPluginJWTResponse;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.CustomCollectors;
import com.t1t.util.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by michallispashidis on 16/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserFacade implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserFacade.class.getName());

    @Inject
    private ISecurityContext securityContext;
    @Inject
    private ISecurityAppContext securityAppContext;
    @Inject
    private IStorage storage;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private AppConfig config;
    @Inject
    private Event<NewEventBean> event;

    public UserBean get(String userId) {
        try {
            UserBean user = idmStorage.getUser(userId);
            if (user == null) {
                throw ExceptionFactory.userNotFoundException(userId);
            }
            return user;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * Initializes new users if they don't exist yet. You can set aswell if the user should be an admin or not.
     * Deferred kong initialization is targeted for this method. Upon the first login of the addes user, necessary
     * tokens en user initialization will be done on the gateway (kong).
     *
     * @param user
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void initNewUser(NewUserBean user) throws UserAlreadyExistsException, StorageException {
        //create user
        UserBean newUser = new UserBean();
        newUser.setUsername(ConsumerConventionUtil.createUserUniqueId(user.getUsername()));
        newUser.setAdmin(user.getAdmin());
        idmStorage.createUser(newUser);
    }

    public void deleteUser(String userId) throws UserNotFoundException, StorageException {
        final UserBean user = idmStorage.getUser(userId);
        if (user == null) throw ExceptionFactory.userNotFoundException(userId);
        //check if user is owner of organizations
        final Set<RoleMembershipBean> userMemberships = idmStorage.getUserMemberships(userId);
        for (RoleMembershipBean role : userMemberships) {
            if (role.getRoleId().equalsIgnoreCase("owner")) throw ExceptionFactory.userCannotDeleteException(userId);
        }
        //Delete related events
        query.deleteAllEventsForEntity(userId);
        //if exception has not been thrown, delete user
        idmStorage.deleteUser(userId);
    }

    public UserBean update(String userId, UpdateUserBean user) {
        try {
            UserBean updatedUser = idmStorage.getUser(userId);
            if (updatedUser == null) throw ExceptionFactory.userNotFoundException(userId);
            if (user.getEmail() != null) updatedUser.setEmail(user.getEmail());
            if (user.getFullName() != null) updatedUser.setFullName(user.getFullName());
            if (user.getPic() != null) updatedUser.setBase64pic(user.getPic());
            if (user.getCompany() != null) updatedUser.setCompany(user.getCompany());
            if (user.getLocation() != null) updatedUser.setLocation(user.getLocation());
            if (user.getWebsite() != null) updatedUser.setWebsite(user.getWebsite());
            if (user.getBio() != null) updatedUser.setBio(user.getBio());
            idmStorage.updateUser(updatedUser);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
        return null;
    }

    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria) {
        try {
            return idmStorage.findUsers(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<UserBean> getAdmins() throws StorageException {
        return idmStorage.getAdminUsers();
    }

    public List<UserBean> getAllUsers() throws StorageException {
        return idmStorage.getAllUsers();
    }

    public void deleteAdminPriviledges(String userId) throws StorageException {
        if (!idmStorage.getUser(securityContext.getCurrentUser()).getAdmin())
            throw ExceptionFactory.notAuthorizedException();
        final UserBean user = idmStorage.getUser(userId);
        if (user == null) throw new UserNotFoundException("User unknow in the application: " + userId);
        user.setAdmin(false);
        idmStorage.updateUser(user);
        fireEvent(securityContext.getCurrentUser(), userId, EventType.ADMIN_REVOKED, null);
    }

    public void addAdminPriviledges(String userId) throws StorageException {
        if (!idmStorage.getUser(securityContext.getCurrentUser()).getAdmin())
            throw ExceptionFactory.notAuthorizedException();
        final UserBean user = idmStorage.getUser(userId);
        if (user == null) {
            NewUserBean newUserBean = new NewUserBean();
            newUserBean.setUsername(userId);
            newUserBean.setAdmin(true);
            initNewUser(newUserBean);
        } else {
            if (user.getAdmin()) {
                String message = new StringBuilder(StringUtils.isEmpty(user.getFullName()) ? user.getUsername() : user.getFullName())
                        .append(" is already an administrator")
                        .toString();
                throw ExceptionFactory.userAlreadyAdminException(message);
            }
            user.setAdmin(true);
            idmStorage.updateUser(user);
        }
        fireEvent(securityContext.getCurrentUser(), userId, EventType.ADMIN_GRANTED, null);
    }

    public List<OrganizationSummaryBean> getOrganizations(String userId) {
        Set<String> permittedOrganizations = new HashSet<>();
        try {
            Set<RoleMembershipBean> memberships = idmStorage.getUserMemberships(userId);
            for (RoleMembershipBean membership : memberships) {
                permittedOrganizations.add(membership.getOrganizationId());
            }
            return query.getOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ApplicationSummaryBean> getApplications(String userId) {
        Set<String> permittedOrganizations = new HashSet<>();
        try {
            Set<PermissionBean> permissions = idmStorage.getPermissions(userId);
            for (PermissionBean permission : permissions) {
                if (permission.getName() == PermissionType.appView) {
                    permittedOrganizations.add(permission.getOrganizationId());
                }
            }
            return query.getApplicationsInOrgs(permittedOrganizations, true);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServiceSummaryBean> getServices(String userId) {
        Set<String> permittedOrganizations = new HashSet<>();
        try {
            Set<PermissionBean> permissions = idmStorage.getPermissions(userId);
            for (PermissionBean permission : permissions) {
                if (permission.getName() == PermissionType.svcView) {
                    permittedOrganizations.add(permission.getOrganizationId());
                }
            }
            return query.getServicesInOrgs(permittedOrganizations);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public SearchResultsBean<AuditEntryBean> getActivity(String userId, int page, int pageSize) {
        int pg = page;
        int pgSize = pageSize;
        if (pg <= 1) {
            pg = 1;
        }
        if (pgSize == 0) {
            pgSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(pg);
            paging.setPageSize(pgSize);
            rval = query.auditUser(userId, paging);
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * Returns a user by given mail.
     * As we have an external user provisioning system, the user should be initialized in the system when found.
     * For new users, the JWT credentials will be generated on the API Gateway.
     *
     * @param email
     * @return
     */
    public ExternalUserBean getUserByEmail(String email) {
        try {
            UserBean userByMail = idmStorage.getUserByMail(email.toLowerCase());
            if (userByMail == null) throw new StorageException();
            log.debug("User found by mail ({}): {}", email, userByMail);
            ExternalUserBean extUser = new ExternalUserBean();
            extUser.setAccountId(userByMail.getUsername());
            extUser.setUsername(userByMail.getUsername());
            List<String> emails = new ArrayList<>();
            emails.add(userByMail.getEmail());
            extUser.setEmails(emails);
            extUser.setName(userByMail.getFullName());
            return extUser;
        } catch (StorageException e) {
            throw new UserNotFoundException("Email unknown to the application: " + email);
        }
    }

    public ExternalUserBean getUserByUsername(String username) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        ExternalUserBean extUser = null;
        try {
            UserBean user = idmStorage.getUser(username.toLowerCase());
            if (user == null) throw new StorageException();
            log.debug("User found by id ({}): {}", username, user);
            extUser = new ExternalUserBean();
            extUser.setUsername(user.getUsername());
            extUser.setAccountId(user.getUsername());
            extUser.setCreatedon(df.format(user.getJoinedOn()));
            extUser.setLastModified(df.format(user.getJoinedOn()));
            extUser.setGivenname(user.getFullName());
        } catch (StorageException e) {
            throw new UserNotFoundException("User unknown to the application: " + username);
        }
        return extUser;
    }

    /**
     * We create a user only in the API Engine db. Upon next visit of the user, the Kong consumer will be created on demand and JWT
     * token will be issued after user authentication.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public UserBean initNewUser(JwtClaims claims, String validatedUser) throws MalformedClaimException {
        log.info("Init new user with attributes:{}", claims);
        try {
            //create user
            UserBean newUser = new UserBean();
            newUser.setUsername(ConsumerConventionUtil.createUserUniqueId(StringUtils.isNotBlank(validatedUser) ? validatedUser : claims.getSubject()));
            if (claims.hasClaim(IJWT.GIVEN_NAME) && claims.hasClaim(IJWT.SURNAME)) {
                newUser.setFullName(claims.getStringClaimValue(IJWT.GIVEN_NAME) + " " + claims.getStringClaimValue(IJWT.SURNAME));
            }
            newUser.setAdmin(false);
            //TODO - parse the roles and organizations from the JWT claims
            idmStorage.createUser(newUser);
            return newUser;
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e);
        }
    }

    /**
     * Fires a new event with the following parameters
     *
     * @param origin
     * @param destination
     * @param type
     * @param body
     */
    private void fireEvent(String origin, String destination, EventType type, String body) {
        NewEventBean neb = new NewEventBean()
                .withOriginId(origin)
                .withDestinationId(destination)
                .withType(type)
                .withBody(body);
        event.fire(neb);
    }

    public JWT exchangeExternalToken(String token) {
        try {
            JwtContext unvalidatedContext = JWTUtils.consumeUnvalidatedToken(token);
            if (unvalidatedContext != null && unvalidatedContext.getJwtClaims() != null && StringUtils.isNotBlank(unvalidatedContext.getJwtClaims().getIssuer())) {
                String issuer = unvalidatedContext.getJwtClaims().getIssuer();
                IdpIssuerBean idpIssuer = storage.getIdpIssuer(issuer);
                if (idpIssuer == null) {
                    throw ExceptionFactory.idpIssuerNotFoundException(issuer);
                }
                JwtClaims validatedClaims = null;
                try {
                    HttpsJwks jwks = new HttpsJwks(idpIssuer.getJwksUri());
                    validatedClaims = JWTUtils.validateRSAToken(token, issuer, jwks).getJwtClaims();
                } catch (RetrofitError ex) {
                    log.error("Error retrieving public key from IDP: ", ex);
                    throw ExceptionFactory.tokenNotVerifiedException(Messages.i18n.format(ErrorCodes.PUB_KEY_IRRETRIEVABLE, idpIssuer.getJwksUri()));
                }
                if (validatedClaims == null) {
                    throw ExceptionFactory.tokenNotVerifiedException(Messages.i18n.format(ErrorCodes.PUB_KEY_IRRETRIEVABLE, issuer));
                }
                IGatewayLink gw = gatewayFacade.getDefaultGatewayLink();
                String consumingApplicationId = securityAppContext.getNonManagedApplication();
                if (StringUtils.isBlank(consumingApplicationId)) {
                    consumingApplicationId = securityAppContext.getApplication();
                    if (StringUtils.isBlank(consumingApplicationId)) {
                        throw ExceptionFactory.applicationContextMissing();
                    }
                }
                validatedClaims.setIssuer(gw.getConsumerJWT(consumingApplicationId).getData()
                        .stream()
                        .filter(cred -> cred.getRsaPublicKey().equals(gatewayFacade.getDefaultGatewayPublicKey())).map(KongPluginJWTResponse::getKey).collect(CustomCollectors.getFirstResult()));
                return new JWT(JWTUtils.getJwtWithExpirationTime(validatedClaims, gatewayFacade.getDefaultGatewayJwtExpirationTime(), gatewayFacade.getDefaultGatewayPrivateKey(), gatewayFacade.getDefaultGatewayPublicKeyEnpoint(), JWTUtils.JWT_RS256));
            } else {
                throw ExceptionFactory.jwtInvalidException(Messages.i18n.format("jwtParsingError"));
            }
        } catch (InvalidJwtException | MalformedClaimException | UnsupportedEncodingException ex) {
            log.error("Error Parsing JWT: ", ex);
            throw ExceptionFactory.jwtInvalidException(ex.getMessage());
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public JWT refreshToken(String token) {
        try {
            JwtClaims unvalidatedClaims = JWTUtils.getUnvalidatedClaims(token);
            if (unvalidatedClaims != null) {
                JwtClaims validatedClaims = JWTUtils.validateRSAToken(token, unvalidatedClaims.getIssuer(), gatewayFacade.getDefaultGatewayPublicKey()).getJwtClaims();
                if (validatedClaims != null) {
                    return new JWT(JWTUtils.getJwtWithExpirationTime(validatedClaims, gatewayFacade.getDefaultGatewayJwtExpirationTime(), gatewayFacade.getDefaultGatewayPrivateKey(), gatewayFacade.getDefaultGatewayPublicKeyEnpoint(), JWTUtils.JWT_RS256));
                }
            }
            throw ExceptionFactory.jwtInvalidException(Messages.i18n.format(ErrorCodes.JWT_INVALID_MSG, "JWT not validated"));
        }
        catch (InvalidJwtException | MalformedClaimException ex) {
            log.error("Error refreshing token: ", ex);
            throw ExceptionFactory.jwtInvalidException(Messages.i18n.format(ErrorCodes.JWT_INVALID_MSG, ex.getMessage()));
        }
    }
}