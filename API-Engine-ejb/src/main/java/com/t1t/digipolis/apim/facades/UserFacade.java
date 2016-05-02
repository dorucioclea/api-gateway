package com.t1t.digipolis.apim.facades;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.cache.WebClientCacheBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.jwt.JWTRefreshRequestBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRefreshResponseBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRequestBean;
import com.t1t.digipolis.apim.beans.mail.MembershipAction;
import com.t1t.digipolis.apim.beans.mail.UpdateAdminMailBean;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.SAMLRequest;
import com.t1t.digipolis.apim.beans.user.SAMLResponseRedirect;
import com.t1t.digipolis.apim.beans.user.UserSession;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.IUserExternalInfoService;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.mail.MailService;
import com.t1t.digipolis.apim.saml2.ISAML2;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.apim.security.IdentityAttributes;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginJWTResponse;
import com.t1t.digipolis.kong.model.KongPluginJWTResponseList;
import com.t1t.digipolis.util.CacheUtil;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;
import org.joda.time.DateTime;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.lang.JoseException;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.*;
import org.opensaml.saml2.encryption.Decrypter;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.EncryptedKey;
import org.opensaml.xml.encryption.EncryptedKeyResolver;
import org.opensaml.xml.io.*;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.StaticKeyInfoCredentialResolver;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.IDIndex;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.crypto.SecretKey;
import javax.ejb.*;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Created by michallispashidis on 16/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserFacade implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserFacade.class.getName());
    private static final String SAML2_KEY_RELAY_STATE = "RelayState=";
    private static final String SAML2_KEY_RESPONSE = "SAMLResponse=";
    private static final String SAML2_KEY_REQUEST = "SAMLRequest=";
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
    private CacheUtil cacheUtil;
    @Inject
    private OrganizationFacade organizationFacade;
    @Inject
    private IUserExternalInfoService userExternalInfoService;
    @Inject
    private AppConfig config;
    @Inject
    private MailService mailService;

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
        if (user==null)throw ExceptionFactory.userNotFoundException(userId);
        //check if user is owner of organizations
        final Set<RoleMembershipBean> userMemberships = idmStorage.getUserMemberships(userId);
        for(RoleMembershipBean role: userMemberships){
            if(role.getRoleId().equalsIgnoreCase("owner"))throw ExceptionFactory.userCannotDeleteException(userId);
        }
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

    public List<UserBean> getAllUsers()throws StorageException{
        return idmStorage.getAllUsers();
    }

    public void deleteAdminPriviledges(String userId)throws StorageException{
        if (!idmStorage.getUser(securityContext.getCurrentUser()).getAdmin())
            throw ExceptionFactory.notAuthorizedException();
        final UserBean user = idmStorage.getUser(userId);
        if(user==null)throw new UserNotFoundException("User unknow in the application: " + userId);
        user.setAdmin(false);
        idmStorage.updateUser(user);
        //send email
        try{
            final UserBean userBean = get(userId);
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateAdminMailBean updateAdminMailBean = new UpdateAdminMailBean();
                updateAdminMailBean.setTo(userBean.getEmail());
                updateAdminMailBean.setMembershipAction(MembershipAction.DELETE_MEMBERSHIP);
                mailService.sendUpdateAdmin(updateAdminMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
        }
    }

    public void addAdminPriviledges(String userId)throws StorageException{
        if (!idmStorage.getUser(securityContext.getCurrentUser()).getAdmin())
            throw ExceptionFactory.notAuthorizedException();
        final UserBean user = idmStorage.getUser(userId);
        if(user==null){
            NewUserBean newUserBean = new NewUserBean();
            newUserBean.setUsername(userId);
            newUserBean.setAdmin(true);
            initNewUser(newUserBean);
        }else{
            user.setAdmin(true);
            idmStorage.updateUser(user);
        }
        //send email
        try{
            final UserBean userBean = get(userId);
            if(userBean!=null && !StringUtils.isEmpty(userBean.getEmail())){
                UpdateAdminMailBean updateAdminMailBean = new UpdateAdminMailBean();
                updateAdminMailBean.setTo(userBean.getEmail());
                updateAdminMailBean.setMembershipAction(MembershipAction.NEW_MEMBERSHIP);
                mailService.sendUpdateAdmin(updateAdminMailBean);
            }
        }catch(Exception e){
            log.error("Error sending mail:{}",e.getMessage());
        }
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
            return query.getApplicationsInOrgs(permittedOrganizations);
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
        if (page <= 1) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        try {
            SearchResultsBean<AuditEntryBean> rval = null;
            PagingBean paging = new PagingBean();
            paging.setPage(page);
            paging.setPageSize(pageSize);
            rval = query.auditUser(userId, paging);
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * Generates a SAML2 login/authentication request.
     *
     * @param samlRequest
     * @return
     */
    public String generateSAML2AuthRequest(SAMLRequest samlRequest) {//String idpUrl, String spUrl, String spName, String clientUrl, ClientTokeType token, Map<String,String> optClaimMap
        // Initialize the library
        log.info("Initate SAML2 request for {}", samlRequest.getIdpUrl());
        try {
            //we need to send the clienUrl as a relaystate - should be URL encoded
            URI clientAppRedirectURI = new URI(samlRequest.getClientAppRedirect());
            if (clientAppRedirectURI==null)throw new URISyntaxException(samlRequest.getClientAppRedirect(),"Invalid callback URI");
            String condensedUri = clientAppRedirectURI.getHost();
            String urlEncodedClientUrl = URLEncoder.encode(condensedUri, "UTF-8");
            String encodedRequestMessage = getSamlRequestEncoded(samlRequest, urlEncodedClientUrl);
            return samlRequest.getIdpUrl() + "?" + SAML2_KEY_REQUEST + encodedRequestMessage + "&" + SAML2_KEY_RELAY_STATE + urlEncodedClientUrl;
        } catch(URISyntaxException uris){
            throw new SAMLAuthException("The callback URL is not a valid URI: "+ uris.getMessage());
        } catch (MarshallingException | IOException | ConfigurationException |StorageException ex) {
            throw new SAMLAuthException("Could not generate the SAML2 Auth Request: " + ex.getMessage());
        }
    }

    private String getSamlRequestEncoded(SAMLRequest samlRequest, String urlEncodedClientUrl) throws IOException, MarshallingException, ConfigurationException, StorageException {
        //Bootstrap OpenSAML
        DefaultBootstrap.bootstrap();
        final GatewayBean gatewayBean = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId());
        //Generate the request
        AuthnRequest authnRequest = buildAuthnRequestObject(samlRequest.getSpUrl(), samlRequest.getSpName(), samlRequest.getClientAppRedirect());
        WebClientCacheBean webCache = new WebClientCacheBean();
        webCache.setToken(samlRequest.getToken());
        webCache.setClientAppRedirect(samlRequest.getClientAppRedirect());
        if(gatewayBean.getJWTExpTime()!=null&&gatewayBean.getJWTExpTime()>0){
            webCache.setTokenExpirationTimeMinutes(gatewayBean.getJWTExpTime());
        }else{
            webCache.setTokenExpirationTimeMinutes(config.getJWTDefaultTokenExpInMinutes());
        }
        webCache.setOptionalClaimset(samlRequest.getOptionalClaimMap());
        webCache.setAppRequester(securityAppContext.getApplicationIdentifier());
        //set client application name and callback in the cache
        cacheUtil.cacheWebClientCacheBean(urlEncodedClientUrl, webCache);//the callback url is maintained as a ref for the cache - the saml2 response relaystate will correlate this value
        log.info("Cache contains:{}", cacheUtil.toString());
        return encodeAuthnRequest(authnRequest);
    }

    /**
     * Generates a SAML2 logout request.
     *
     * @param idpUrl
     * @param spName
     * @param user
     * @return
     */
    public String generateSAML2LogoutRequest(String idpUrl, String spName, String user) {
        // Initialize the library
        try {
            //Bootstrap OpenSAML
            DefaultBootstrap.bootstrap();
            //Generate the request
            LogoutRequest authnRequest = buildLogoutRequest(user, idpUrl, spName);
            //set client application name and callback in the cache
            String encodedRequestMessage = encodeAuthnRequest(authnRequest);
            return idpUrl + "?" + SAML2_KEY_REQUEST + encodedRequestMessage;
            //redirectUrl = identityProviderUrl + "?SAMLRequest=" + encodedAuthRequest + "&RelayState=" + relayState;
        } catch (MarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not generate the SAML2 Logout Request: " + ex.getMessage());
        }
    }

    /**
     * Build SAML2 authentication request
     *
     * @param spUrl
     * @param spName
     * @return
     */
    private AuthnRequest buildAuthnRequestObject(String spUrl, String spName, String clientUrl) {
        IssuerBuilder issuerBuilder = null;
        Issuer issuer = null;
        NameIDPolicyBuilder nameIdPolicyBuilder = null;
        NameIDPolicy nameIdPolicy = null;
        AuthnContextClassRefBuilder authnContextClassRefBuilder = null;
        AuthnContextClassRef authnContextClassRef = null;
        RequestedAuthnContextBuilder requestedAuthnContextBuilder = null;
        RequestedAuthnContext requestedAuthnContext = null;
        DateTime issueInstant = null;
        AuthnRequestBuilder authRequestBuilder = null;
        AuthnRequest authRequest = null;

        // Issuer object
        issuerBuilder = new IssuerBuilder();
        issuer = issuerBuilder.buildObject("urn:oasis:names:tc:SAML:2.0:assertion", "Issuer", "samlp");
        issuer.setValue(spName);

        // NameIDPolicy
        nameIdPolicyBuilder = new NameIDPolicyBuilder();
        nameIdPolicy = nameIdPolicyBuilder.buildObject();
        nameIdPolicy.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
        nameIdPolicy.setSPNameQualifier("apiengine");
        nameIdPolicy.setAllowCreate(new Boolean(true));

        // AuthnContextClass
        authnContextClassRefBuilder = new AuthnContextClassRefBuilder();
        authnContextClassRef = authnContextClassRefBuilder.buildObject("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnContextClassRef", "saml");
        authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");

        // AuthnContex
        requestedAuthnContextBuilder = new RequestedAuthnContextBuilder();
        requestedAuthnContext = requestedAuthnContextBuilder.buildObject();
        requestedAuthnContext.setComparison(AuthnContextComparisonTypeEnumeration.EXACT);
        requestedAuthnContext.getAuthnContextClassRefs().add(authnContextClassRef);

        // Creation of AuthRequestObject
        issueInstant = new DateTime();
        authRequestBuilder = new AuthnRequestBuilder();
        authRequest = authRequestBuilder.buildObject("urn:oasis:names:tc:SAML:2.0:protocol", "AuthnRequest", "samlp");
        authRequest.setForceAuthn(new Boolean(false));
        authRequest.setIsPassive(new Boolean(false));
        authRequest.setIssueInstant(issueInstant);
        authRequest.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");//urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect
        authRequest.setAssertionConsumerServiceURL(spUrl);
        authRequest.setIssuer(issuer);
        authRequest.setNameIDPolicy(nameIdPolicy);
        authRequest.setRequestedAuthnContext(requestedAuthnContext);
        authRequest.setID(Integer.toHexString(new Double(Math.random()).intValue())); //random id
        authRequest.setVersion(SAMLVersion.VERSION_20);
        //set extension in order to avoid creating service providers for all web applications
        //authRequest.setExtensions(buildExtensions(clientUrl));//doesn't return the value - leaving it out at the moment.
        return authRequest;
    }

    /**
     * Builds a SAML2 authRequest extension claim.
     *
     * @param clientUrl
     * @return
     */
    protected Extensions buildExtensions(String clientUrl) {
        XSAny extraElement = new XSAnyBuilder().buildObject("urn:myexample:extraAttribute", "ExtraElement", "myexample");
        extraElement.setTextContent("extraValue");
        Extensions extensions = new ExtensionsBuilder().buildObject();
        extensions.getUnknownXMLObjects().add(extraElement);
        return extensions;
    }

    private LogoutRequest buildLogoutRequest(String userId, String idpUrl, String spName) {
        LogoutRequest logoutReq = (new LogoutRequestBuilder()).buildObject();
        logoutReq.setID(Integer.toHexString(new Double(Math.random()).intValue()));
        logoutReq.setDestination(idpUrl);
        DateTime issueInstant = new DateTime();
        logoutReq.setIssueInstant(issueInstant);
        logoutReq.setNotOnOrAfter(new DateTime(issueInstant.getMillis() + 300000L));
        IssuerBuilder issuerBuilder = new IssuerBuilder();
        Issuer issuer = issuerBuilder.buildObject();
        issuer.setValue(spName);
        logoutReq.setIssuer(issuer);

        //Get cached sessionIndex and subject ID.
        //TODO nullpointer when read! verify
        SessionIndex sessionIndex = (new SessionIndexBuilder()).buildObject();
        UserSession sIndex = null;
        if (cacheUtil.getSessionIndex(userId) != null) {
            sIndex = cacheUtil.getSessionIndex(userId);
            sessionIndex.setSessionIndex(sIndex.getSessionIndex());
        }
        NameID nameId = (new NameIDBuilder()).buildObject();
        nameId.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
        nameId.setValue(sIndex.getSubjectId());
        logoutReq.setNameID(nameId);
        logoutReq.getSessionIndexes().add(sessionIndex);
        logoutReq.setReason("Single Logout");
        return logoutReq;
    }

    /**
     * Encode the Authentication Request (base64 and URL encoded)
     *
     * @param authnRequest
     * @return
     * @throws MarshallingException
     * @throws IOException
     */
    private String encodeAuthnRequest(RequestAbstractType authnRequest) throws MarshallingException, IOException {
        Marshaller marshaller = null;
        org.w3c.dom.Element authDOM = null;
        StringWriter requestWriter = null;
        String requestMessage = null;
        Deflater deflater = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        DeflaterOutputStream deflaterOutputStream = null;
        String encodedRequestMessage = null;
        marshaller = org.opensaml.Configuration.getMarshallerFactory().getMarshaller(authnRequest); // object to DOM converter
        authDOM = marshaller.marshall(authnRequest); // converting to a DOM
        requestWriter = new StringWriter();
        XMLHelper.writeNode(authDOM, requestWriter);
        requestMessage = requestWriter.toString(); // DOM to string
        deflater = new Deflater(Deflater.DEFLATED, true);
        byteArrayOutputStream = new ByteArrayOutputStream();
        deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
        deflaterOutputStream.write(requestMessage.getBytes()); // compressing
        deflaterOutputStream.close();
        encodedRequestMessage = Base64.encodeBytes(byteArrayOutputStream.toByteArray(), Base64.DONT_BREAK_LINES);
        encodedRequestMessage = URLEncoder.encode(encodedRequestMessage, "UTF-8").trim(); // encoding string
        return encodedRequestMessage;
    }

    /**
     * Processes the SAML Assertion and returns a SAML2 Bearer token.
     * If in restricted mode, the resolved user is not an admin, then null is returned
     * in order for the rest layer to send the appropriate error to the UI.
     *
     * @param samlResponse
     * @param relaystate
     *
     * @return
     */
    public SAMLResponseRedirect processSAML2Response(String samlResponse, String relaystate) throws Exception {

        String relayState = relaystate;
        StringBuffer clientUrl = new StringBuffer("");
        Assertion assertion = null;
        IdentityAttributes idAttribs;
        utilPrintCache();
        String urlEncodedRelaystate = URLEncoder.encode(relayState, "UTF-8");
        WebClientCacheBean webClientCacheBean = cacheUtil.getWebCacheBean(urlEncodedRelaystate.trim());
        try {
            assertion = processSSOResponse(samlResponse);
            //clientAppName = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI(); -> important to validate audience
            idAttribs = resolveSaml2AttributeStatements(assertion.getAttributeStatements());
            String userId = ConsumerConventionUtil.createUserUniqueId(idAttribs.getId());
            //preempt if restricted mode and user is not admin; be carefull to scope the application, non api-engine applications uses this endpoint as well.
            if (config.getRestrictedMode() && webClientCacheBean.getAppRequester() != null && config.getAppliedRestrictions().contains(webClientCacheBean.getAppRequester().getAppId())) {
                final UserBean user = idmStorage.getUser(userId);
                if (user == null || !user.getAdmin()) {
                    SAMLResponseRedirect responseRedirect = new SAMLResponseRedirect();
                    responseRedirect.setToken("unauthorized");
                    clientUrl.append(webClientCacheBean.getClientAppRedirect());
                    if (!clientUrl.toString().endsWith("/")) clientUrl.append("/");
                    responseRedirect.setClientUrl(clientUrl.toString());
                    return responseRedirect;
                }
            }
            idAttribs.setSubjectId(userId);
            log.debug("Relay state found with correlation: {}", relayState);
        } catch (SAXException | ParserConfigurationException | UnmarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not process the SAML2 Response: " + ex.getMessage());
        }
        SAMLResponseRedirect responseRedirect = new SAMLResponseRedirect();
        responseRedirect.setToken(updateOrCreateConsumerJWTOnGateway(idAttribs, webClientCacheBean));
        clientUrl.append(webClientCacheBean.getClientAppRedirect());
        if (!clientUrl.toString().endsWith("/")) clientUrl.append("/");
        responseRedirect.setClientUrl(clientUrl.toString());
        //for logout, we should keep the SessionIndex in cache with the username
        if (assertion != null && assertion.getAuthnStatements().size() > 0) {
            //update or create user sessionindex in cache -- id::the subject of JWT -- subject::saml2 subjectid -- sessionindex
            cacheUtil.cacheSessionIndex(idAttribs.getId(), new UserSession(idAttribs.getSubjectId(), assertion.getAuthnStatements().get(0).getSessionIndex()));
        }
        return responseRedirect; //be aware that this is enflated.
    }

    /**
     * Processes the SAML Assertion and returns a SAML2 Bearer token.
     * Expects a string starting with "SAMLResponse=".
     *
     * @param response
     * @return
     */
    public SAMLResponseRedirect validateExtSAML2(String response) throws Exception {
        Assertion assertion = null;
        IdentityAttributes idAttribs;
        try {
            assertion = processSSOResponse(response);
            idAttribs = resolveSaml2AttributeStatements(assertion.getAttributeStatements());
            String userId = ConsumerConventionUtil.createUserUniqueId(idAttribs.getId());
            idAttribs.setSubjectId(userId);
            log.debug("External SAML response validation");
        } catch (SAXException | ParserConfigurationException | UnmarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not process the SAML2 Response: " + ex.getMessage());
        }
        SAMLResponseRedirect responseRedirect = new SAMLResponseRedirect();
        responseRedirect.setToken(updateOrCreateConsumerJWTOnGateway(idAttribs, null));
        //for logout, we should keep the SessionIndex in cache with the username
        if (assertion != null && assertion.getAuthnStatements().size() > 0) {
            //update or create user sessionindex in cache -- id::the subject of JWT -- subject::saml2 subjectid -- sessionindex
            cacheUtil.cacheSessionIndex(idAttribs.getId(), new UserSession(idAttribs.getSubjectId(), assertion.getAuthnStatements().get(0).getSessionIndex()));
        }
        return responseRedirect; //be aware that this is enflated.
    }

    /**
     * Parses SAML2 attributes to custom IdenityAttributes object.
     * This common IdentityAttr object is commonly used with SCIM.
     *
     * @param attributeStatements
     * @return
     */
    private IdentityAttributes resolveSaml2AttributeStatements(List<AttributeStatement> attributeStatements) {
        IdentityAttributes identityAttributes = new IdentityAttributes();
        Map<String, String> extractedAttributes = new TreeMap<>();
        //only consider the first attribute statement.
        if (attributeStatements.size() > 0) {
            AttributeStatement attributeStatement = attributeStatements.get(0);
            IDIndex idIndex = attributeStatement.getIDIndex();
            List<Attribute> attributes = attributeStatement.getAttributes();
            for (Attribute attrib : attributes) {
                String name = attrib.getName();
                List<XMLObject> attributeValues = attrib.getAttributeValues();
                XMLObject xmlObject = attributeValues.get(0);
                String nodeValue = xmlObject.getDOM().getFirstChild().getNodeValue();
                extractedAttributes.put(name, nodeValue);
            }
        }
        //map values
        if (extractedAttributes.size() > 0) {
            if (extractedAttributes.containsKey(ISAML2.ATTR_ID)) {
                identityAttributes.setId(extractedAttributes.get(ISAML2.ATTR_ID));
            } else {
                identityAttributes.setId("none");
            }
            if (extractedAttributes.containsKey(ISAML2.ATTR_USER_NAME)) {
                identityAttributes.setUserName(extractedAttributes.get(ISAML2.ATTR_USER_NAME));
            } else {
                identityAttributes.setUserName("");
            }
            if (extractedAttributes.containsKey(ISAML2.ATTR_FAMILY_NAME)) {
                identityAttributes.setFamilyName(extractedAttributes.get(ISAML2.ATTR_FAMILY_NAME));
            } else {
                identityAttributes.setFamilyName("");
            }
            if (extractedAttributes.containsKey(ISAML2.ATTR_GIVEN_NAME)) {
                identityAttributes.setGivenName(extractedAttributes.get(ISAML2.ATTR_GIVEN_NAME));
            } else {
                identityAttributes.setGivenName("");
            }
        }
        return identityAttributes;
    }

    /**
     * Method processes the SAML2 Response in order to capture the SAML Assertion.
     * See: http://sureshatt.blogspot.be/2012/11/how-to-read-saml-20-response-with.html
     *
     * @param samlResp
     * @return
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ConfigurationException
     * @throws IOException
     * @throws UnmarshallingException
     */
    private Assertion processSSOResponse(String samlResp) throws SAXException, ParserConfigurationException, ConfigurationException, IOException, UnmarshallingException {
        DefaultBootstrap.bootstrap();
        //String base64URLDecodedResponse = URLDecoder.decode(samlResp, "UTF-8");
        byte[] base64DecodedResponse = Base64.decode(samlResp);
        String samlResponseString = new String(base64DecodedResponse);
        log.info("Decoded SAML response:{}", samlResponseString);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(samlResponseString.trim().getBytes()));
        Element element = document.getDocumentElement();
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
        Response response = (Response) unmarshaller.unmarshall(element);

        //TODO validate signature - see SSOAgent example of WSO2
        //String certificate = response.getSignature().getKeyInfo().getX509Datas().get(0).getX509Certificates().get(0).getValue();
        //get assertion
        Assertion assertion = response.getAssertions().get(0);
        //Return base64url encoded assertion
        return assertion;
    }

    /**
     * Encodes the SAML2 Bearer token - only the assertion
     *
     * @param assertion
     * @return
     * @throws MarshallingException
     * @throws IOException
     */
    private String encodeSAML2BearerToken(Assertion assertion) {
        try {
            Marshaller marshaller = null;
            org.w3c.dom.Element assertionDOM = null;
            StringWriter requestWriter = null;
            String requestMessage = null;
            Deflater deflater = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            DeflaterOutputStream deflaterOutputStream = null;
            String encodedRequestMessage = null;
            marshaller = org.opensaml.Configuration.getMarshallerFactory().getMarshaller(assertion); // object to DOM converter

            assertionDOM = marshaller.marshall(assertion); // converting to a DOM
            requestWriter = new StringWriter();
            XMLHelper.writeNode(assertionDOM, requestWriter);
            requestMessage = requestWriter.toString(); // DOM to string
/*        deflater = new Deflater(Deflater.DEFLATED, true);
        byteArrayOutputStream = new ByteArrayOutputStream();
        deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
        deflaterOutputStream.write(requestMessage.getBytes()); // compressing
        deflaterOutputStream.close();*/
            //encodedRequestMessage = Base64.encodeBytes(byteArrayOutputStream.toByteArray(), Base64.DONT_BREAK_LINES);
            encodedRequestMessage = Base64.encodeBytes(requestMessage.getBytes(), Base64.DONT_BREAK_LINES);
            encodedRequestMessage = URLEncoder.encode(encodedRequestMessage, "UTF-8").trim(); // encoding string
            return encodedRequestMessage;
        } catch (MarshallingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String userFromSAML2BearerToken(String token) throws SAXException, ParserConfigurationException, ConfigurationException, IOException, UnmarshallingException {
        //Bootstrap OpenSAML
        DefaultBootstrap.bootstrap();
        String base64URLDecodedResponse = URLDecoder.decode(token, "UTF-8");
        byte[] base64DecodedResponse = Base64.decode(base64URLDecodedResponse);
        String samlResponseString = new String(base64DecodedResponse);
        //log.info("Decoded SAML response:{}", samlResponseString);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(samlResponseString.trim().getBytes()));
        Element element = document.getDocumentElement();
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
        Response response = (Response) unmarshaller.unmarshall(element);
        //Return user
        return response.getAssertions().get(0).getSubject().getNameID().getValue();
    }

    /**
     * Updates or creates a consumer on the gateway and in the data model.
     * No ACL activation.
     * Users are created implicitly, first we check if the user exists already on the gateway.
     * <p>
     * The security flow param triggers optional flows for validation and token issuance.
     *
     * @param identityAttributes SAML2 attributes
     * @return
     */
    private String updateOrCreateConsumerJWTOnGateway(IdentityAttributes identityAttributes, WebClientCacheBean cacheBean) {
        String jwtKey = "";
        String jwtSecret = "";
        String issuedJWT = "";
        // Publish the service to all relevant gateways
        try {
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
            IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
            //get user from local DB - if doesn't exists -> create new consumer
            UserBean user = idmStorage.getUser(ConsumerConventionUtil.createUserUniqueId(identityAttributes.getId()));
            if (user == null) {//exists already
                user = initNewUser(identityAttributes);
            }
            log.info("User found:{}", user);
            KongConsumer consumer = null;
            if (!StringUtils.isEmpty(user.getKongUsername()))
                consumer = gatewayLink.getConsumer(user.getKongUsername());
            if (consumer == null) {
                //user doesn't exists, implicit creation in order to sync with local db => when user is deleted from Kong, will be recreated and username will be updated
                consumer = gatewayLink.createConsumerWithCustomId(ConsumerConventionUtil.createUserUniqueId(user.getUsername()));
                //update kong username in local userbean
                user.setKongUsername(consumer.getId());
                idmStorage.updateUser(user);
                KongPluginJWTResponse jwtResponse = gatewayLink.addConsumerJWT(consumer.getId());
                jwtKey = jwtResponse.getKey();//JWT "iss"
                jwtSecret = jwtResponse.getSecret();
            } else {
                KongPluginJWTResponseList response = gatewayLink.getConsumerJWT(consumer.getId());
                if (response.getData().size() > 0) {
                    jwtKey = response.getData().get(0).getKey();
                    jwtSecret = response.getData().get(0).getSecret();
                } else {
                    //create jwt credentials
                    KongPluginJWTResponse jwtResponse = gatewayLink.addConsumerJWT(consumer.getId());
                    jwtKey = jwtResponse.getKey();//JWT "iss"
                    jwtSecret = jwtResponse.getSecret();
                }
            }
            //set the cache for performance and resilience
            setTokenCache(jwtKey, jwtSecret);
            //start composing JWT token
            JWTRequestBean jwtRequestBean = new JWTRequestBean();
            jwtRequestBean.setIssuer(jwtKey);
            if (cacheBean != null) {
                if (cacheBean.getTokenExpirationTimeMinutes() != null)
                    jwtRequestBean.setExpirationTimeMinutes(cacheBean.getTokenExpirationTimeMinutes());
                else jwtRequestBean.setExpirationTimeMinutes(config.getJWTDefaultTokenExpInMinutes());
                jwtRequestBean.setAudience(cacheBean.getClientAppRedirect());//callback serves as audience
                jwtRequestBean.setOptionalClaims(cacheBean.getOptionalClaimset());
            } else {
                jwtRequestBean.setExpirationTimeMinutes(config.getJWTDefaultTokenExpInMinutes());
            }
            jwtRequestBean.setName(identityAttributes.getUserName());
            jwtRequestBean.setGivenName(identityAttributes.getGivenName());
            jwtRequestBean.setSurname(identityAttributes.getFamilyName());
            jwtRequestBean.setSubject(identityAttributes.getId());
            final GatewayBean gatewayBean = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId());
            Integer jwtExpirationTime = config.getJWTDefaultTokenExpInMinutes();
            if(gatewayBean.getJWTExpTime()!=null&&gatewayBean.getJWTExpTime()>0){
                jwtExpirationTime = gatewayBean.getJWTExpTime();
            }
            issuedJWT = JWTUtils.composeJWT(jwtRequestBean, jwtSecret, jwtExpirationTime);
            //close gateway
            gatewayLink.close();
        } catch (PublishingException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e); //$NON-NLS-1$
        }
        return issuedJWT;
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
        try{
            UserBean userByMail = idmStorage.getUserByMail(email);
            if(userByMail==null)throw new StorageException();
            log.debug("User found by mail ({}): {}",email, userByMail);
            ExternalUserBean extUser = new ExternalUserBean();
            extUser.setAccountId(userByMail.getUsername());
            extUser.setUsername(userByMail.getUsername());
            List<String> emails = new ArrayList<>();
            emails.add(userByMail.getEmail());
            extUser.setEmails(emails);
            extUser.setName(userByMail.getFullName());
            return extUser;
        }catch (StorageException e) {
            throw new UserNotFoundException("Email unknow to the application: " + email);
        }
    }

    public ExternalUserBean getUserByUsername(String username) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        ExternalUserBean extUser = null;
        try {
            UserBean user = idmStorage.getUser(username);
            if(user==null)throw new StorageException();
            log.debug("User found by id ({}): {}",username, user);
            extUser = new ExternalUserBean();
            extUser.setUsername(user.getUsername());
            extUser.setAccountId(user.getUsername());
            extUser.setCreatedon(df.format(user.getJoinedOn()));
            extUser.setLastModified(df.format(user.getJoinedOn()));
            extUser.setGivenname(user.getFullName());
        } catch (StorageException e) {
            throw new UserNotFoundException("User unknow to the application: " + username);
        }
        return extUser;
    }

    /**
     * Stores the secret in a cache, such that we don't have to call each time the Kong client.
     *
     * @param jwtKey
     * @param jwtSecret
     */
    private void setTokenCache(String jwtKey, String jwtSecret) {
        cacheUtil.cacheToken(jwtKey, jwtSecret);
    }

    /**
     * Retrieves the secret for a given subject, if the key is not found in the cache, a Kong request will be sent to retrieve the value;.
     *
     * @param key
     * @return
     */
    private String getSecretFromTokenCache(String key, String userName) {
        String secret = cacheUtil.getToken(key);
        if (StringUtils.isEmpty(secret)) {
            //retrieve from Kong
            String gatewayId = null;
            try {
                gatewayId = gatewayFacade.getDefaultGateway().getId();
                IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
                List<KongPluginJWTResponse> data = gatewayLink.getConsumerJWT(userName).getData();
                if (data != null && data.size() > 0) {
                    secret = data.get(0).getSecret();
                } else throw new StorageException("Refresh JWT - somehow the user is not known");
            } catch (StorageException e) {
                new GatewayException("Error connection to gateway:{}" + e.getMessage());
            }
        }
        return secret;
    }

    public JWTRefreshResponseBean refreshToken(JWTRefreshRequestBean jwtRefreshRequestBean) throws UnsupportedEncodingException, InvalidJwtException, MalformedClaimException, JoseException, StorageException {
        //get body
        JwtContext jwtContext = JWTUtils.validateHMACToken(jwtRefreshRequestBean.getOriginalJWT());
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        //get gateway default expiration time for JWT
        final GatewayBean gatewayBean = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId());
        Integer jwtExpirationTime = 60;//default 60min.
        if(gatewayBean.getJWTExpTime()!=null&&gatewayBean.getJWTExpTime()>0){
            jwtExpirationTime = gatewayBean.getJWTExpTime();
        }else{
            jwtExpirationTime = config.getJWTDefaultTokenExpInMinutes();
        }
        //get secret based on iss/username - cached
        String secret = getSecretFromTokenCache(jwtClaims.getIssuer().toString(), jwtClaims.getSubject());
        JWTRefreshResponseBean jwtRefreshResponseBean = new JWTRefreshResponseBean();
        jwtRefreshResponseBean.setJwt(JWTUtils.refreshJWT(jwtRefreshRequestBean, jwtClaims, secret, jwtExpirationTime));
        return jwtRefreshResponseBean;
    }

    /**
     * We create a user only in the API Engine db. Upon next visit of the user, the Kong consumer will be created on demand and JWT
     * token will be issued after user authentication.
     *
     * @param identityAttributes
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private UserBean initNewUser(IdentityAttributes identityAttributes) {
        log.info("Init new user with attributes:{}", identityAttributes);
        try {
            //create user
            UserBean newUser = new UserBean();
            newUser.setUsername(ConsumerConventionUtil.createUserUniqueId(identityAttributes.getId()));//upn of UUID
            newUser.setFullName(identityAttributes.getGivenName() + " " + identityAttributes.getFamilyName());
            newUser.setAdmin(false);
            idmStorage.createUser(newUser);
            return newUser;
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e);
        }
    }

    /**
     * Utility method used for decrypting the SAML Assertion response.
     *
     * @param encryptedAssertion
     * @param credential
     * @return
     * @throws Exception
     */
    public Assertion getDecryptedAssertion(EncryptedAssertion encryptedAssertion, X509Credential credential) throws Exception {
        StaticKeyInfoCredentialResolver keyResolver = new StaticKeyInfoCredentialResolver(credential);
        EncryptedKey key = (EncryptedKey) encryptedAssertion.getEncryptedData().getKeyInfo().getEncryptedKeys().get(0);
        Decrypter decrypter = new Decrypter((KeyInfoCredentialResolver) null, keyResolver, (EncryptedKeyResolver) null);
        SecretKey dkey = (SecretKey) decrypter.decryptKey(key, encryptedAssertion.getEncryptedData().getEncryptionMethod().getAlgorithm());
        BasicCredential shared = SecurityHelper.getSimpleCredential(dkey);
        decrypter = new Decrypter(new StaticKeyInfoCredentialResolver(shared), (KeyInfoCredentialResolver) null, (EncryptedKeyResolver) null);
        decrypter.setRootInNewDocument(true);
        return decrypter.decrypt(encryptedAssertion);
    }

    /**
     * Print cache debug util.
     * Prints the full cache in the log file in order to verify application request parameters.
     */
    public void utilPrintCache() {
        log.debug("SessionIndex cache values:");
        Set<String> ssoKeys = cacheUtil.getSSOKeys();
        log.debug("SSOKeys: {}", ssoKeys);
        log.debug("SSO cache values: {}", ssoKeys);
        ssoKeys.forEach(key -> log.debug("Key found:{} with value {}", key, cacheUtil.getWebCacheBean(key)));
        Set<String> sessionKeys = cacheUtil.getSessionKeys();
        log.debug("Sessionkeys: {}", sessionKeys);
        log.debug("Session cach values: {}", sessionKeys);
        sessionKeys.forEach(key -> log.debug("Key found:{} with value {}", key, cacheUtil.getSessionIndex(key)));
        Set<String> tokenKeys = cacheUtil.getTokenKeys();
        log.debug("Tokenkeys: {}", tokenKeys);
        log.debug("Token cache values:");
        tokenKeys.forEach(key -> log.debug("Key found:{} with value {}", key, cacheUtil.getToken(key)));
    }

    /**
     * Parses SCIM attributes to custom IdenityAttributes object.
     * This common IdentityAttr object is commonly used with SAML2.
     *
     * @param externalUserBean
     * @return
     */
    private IdentityAttributes resolveScimAttributeStatements(ExternalUserBean externalUserBean) {
        IdentityAttributes identityAttributes = new IdentityAttributes();
        //map values
        identityAttributes.setId(externalUserBean.getAccountId());
        identityAttributes.setUserName(ConsumerConventionUtil.createUserUniqueId(externalUserBean.getUsername()));
        identityAttributes.setFamilyName(externalUserBean.getSurname());
        identityAttributes.setGivenName(externalUserBean.getGivenname());
        return identityAttributes;
    }

}
