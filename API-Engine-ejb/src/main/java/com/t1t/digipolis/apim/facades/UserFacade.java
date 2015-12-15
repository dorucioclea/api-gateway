package com.t1t.digipolis.apim.facades;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.authorization.ProxyAuthRequest;
import com.t1t.digipolis.apim.beans.cache.WebClientCacheBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.jwt.JWTRefreshRequestBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRefreshResponseBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRequestBean;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.ClientTokeType;
import com.t1t.digipolis.apim.beans.user.SAMLResponseRedirect;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.IUserExternalInfoService;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.idp.IDPClient;
import com.t1t.digipolis.apim.idp.IDPRestServiceBuilder;
import com.t1t.digipolis.apim.idp.RestIDPConfigBean;
import com.t1t.digipolis.apim.saml2.ISAML2;
import com.t1t.digipolis.apim.saml2.ProxyLoginFailSafe;
import com.t1t.digipolis.apim.security.IdentityAttributes;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginJWTResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginJWTResponseList;
import com.t1t.digipolis.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
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
import org.opensaml.saml2.core.Response;
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
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import retrofit.RetrofitError;

import javax.crypto.SecretKey;
import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
    @PersistenceContext
    private EntityManager em;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IStorage storage;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private CacheUtil ehcache;
    @Inject
    private OrganizationFacade organizationFacade;
    @Inject
    private IUserExternalInfoService userExternalInfoService;
    @Inject
    private AppConfig config;

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
     * @param idpUrl
     * @param spUrl
     * @param spName
     * @param clientUrl
     * @param token
     * @return
     */
    public String generateSAML2AuthRequest(String idpUrl, String spUrl, String spName, String clientUrl, ClientTokeType token, Map<String,String> optClaimMap) {
        // Initialize the library
        log.info("Initate SAML2 request for {}", clientUrl);
        try {
            //Bootstrap OpenSAML
            DefaultBootstrap.bootstrap();
            //Generate the request
            AuthnRequest authnRequest = buildAuthnRequestObject(spUrl, spName, clientUrl);
            WebClientCacheBean webCache = new WebClientCacheBean();
            webCache.setToken(token);
            webCache.setClientAppRedirect(clientUrl);
            webCache.setTokenExpirationTimeMinutes(config.getJWTDefaultTokenExpInMinutes());
            webCache.setOptionalClaimset(optClaimMap);
            //we need to send the clienUrl as a relaystate - should be URL encoded
            String condensedUri = clientUrl.replaceAll("https://","");
            String urlEncodedClientUrl = URLEncoder.encode(condensedUri,"UTF-8");
            //set client application name and callback in the cache
            ehcache.getClientAppCache().put(new net.sf.ehcache.Element(urlEncodedClientUrl, webCache));//the callback url is maintained as a ref for the cache - the saml2 response relaystate will correlate this value
            log.info("Cache contains:{}", ehcache.toString());
            utilPrintCache();

            String encodedRequestMessage = encodeAuthnRequest(authnRequest);
            return idpUrl + "?"+ SAML2_KEY_REQUEST + encodedRequestMessage+"&" + SAML2_KEY_RELAY_STATE +urlEncodedClientUrl;
            //redirectUrl = identityProviderUrl + "?SAMLRequest=" + encodedAuthRequest + "&RelayState=" + relayState;
        } catch (MarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not generate the SAML2 Auth Request: " + ex.getMessage());
        }
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
            return idpUrl + "?"+ SAML2_KEY_REQUEST + encodedRequestMessage;
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
        nameIdPolicy.setSPNameQualifier("Isser");
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

    private LogoutRequest buildLogoutRequest(String user, String idpUrl, String spName) {
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
        NameID nameId = (new NameIDBuilder()).buildObject();
        nameId.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
        nameId.setValue(user);
        logoutReq.setNameID(nameId);
        SessionIndex sessionIndex = (new SessionIndexBuilder()).buildObject();
        //add sessionindex from user
        //TODO nullpointer when read! verify
        if (ehcache.getClientAppCache().get(user) != null) {
            String sIndex = (String) ehcache.getClientAppCache().get(user).getObjectValue();
            sessionIndex.setSessionIndex(sIndex);
        }
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
     *
     * @param response
     * @return
     */
    public SAMLResponseRedirect processSAML2Response(String response) throws ValidationException {
        String relayState = response.split("&")[1].replaceFirst(SAML2_KEY_RELAY_STATE,"").trim();//the relaystate contains the correlation id for the calling web client == callbackurl
        StringBuffer clientUrl = new StringBuffer("");
        Assertion assertion = null;
        IdentityAttributes idAttribs;
        try {
            assertion = processSSOResponse(response);
            //clientAppName = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI();
            idAttribs = resolveSaml2AttributeStatements(assertion.getAttributeStatements());
            idAttribs.setUserName(UserConventionUtil.formatUserName(assertion.getSubject().getNameID().getValue()));
            log.info("Audience URI found: {}", relayState);
        } catch (SAXException | ParserConfigurationException | UnmarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not process the SAML2 Response: " + ex.getMessage());
        }
        SAMLResponseRedirect responseRedirect = new SAMLResponseRedirect();
        utilPrintCache();
        WebClientCacheBean webClientCacheBean = (WebClientCacheBean) ehcache.getClientAppCache().get(relayState.trim()).getObjectValue();
        if (assertion != null && webClientCacheBean.getToken().equals(ClientTokeType.jwt)) {
            List<AttributeStatement> attributeStatements = assertion.getAttributeStatements();
            responseRedirect.setToken(updateOrCreateConsumerJWTOnGateway(idAttribs,webClientCacheBean));
        } else {
            responseRedirect.setToken(updateOrCreateConsumerKeyAuthOnGateway(idAttribs.getUserName()));
        }
        clientUrl.append(webClientCacheBean.getClientAppRedirect());
        if (!clientUrl.toString().endsWith("/")) clientUrl.append("/");
        responseRedirect.setClientUrl(clientUrl.toString());
        //for logout, we should keep the SessionIndex in cache with the username
        if (assertion != null && assertion.getAuthnStatements().size() > 0) {
            //update or create user sessionindex in cache
            ehcache.getClientAppCache().put(new net.sf.ehcache.Element(idAttribs.getUserName(), assertion.getAuthnStatements().get(0).getSessionIndex()));
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
    private IdentityAttributes resolveSaml2AttributeStatements(List<AttributeStatement> attributeStatements){
        IdentityAttributes identityAttributes = new IdentityAttributes();
        Map<String,String> extractedAttributes = new TreeMap<>();
        //only consider the first attribute statement.
        if(attributeStatements.size()>0){
            AttributeStatement attributeStatement = attributeStatements.get(0);
            IDIndex idIndex = attributeStatement.getIDIndex();
            List<Attribute> attributes = attributeStatement.getAttributes();
            for(Attribute attrib:attributes){
                String name = attrib.getName();
                List<XMLObject> attributeValues = attrib.getAttributeValues();
                XMLObject xmlObject = attributeValues.get(0);
                String nodeValue = xmlObject.getDOM().getFirstChild().getNodeValue();
                extractedAttributes.put(name,nodeValue);
            }
        }
        //map values
        if(extractedAttributes.size()>0){
            identityAttributes.setId(extractedAttributes.get(ISAML2.ATTR_ID));
            identityAttributes.setUserName(extractedAttributes.get(ISAML2.ATTR_USER_NAME));
            identityAttributes.setFamilyName(extractedAttributes.get(ISAML2.ATTR_FAMILY_NAME));
            identityAttributes.setGivenName(extractedAttributes.get(ISAML2.ATTR_GIVEN_NAME));
        }
        return identityAttributes;
    }

    /**
     * Method processes the SAML2 Response in order to capture the SAML Assertion.
     * See: http://sureshatt.blogspot.be/2012/11/how-to-read-saml-20-response-with.html
     *
     * @param responseString
     * @return
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ConfigurationException
     * @throws IOException
     * @throws UnmarshallingException
     */
    private Assertion processSSOResponse(String responseString) throws SAXException, ParserConfigurationException, ConfigurationException, IOException, UnmarshallingException, ValidationException {
        DefaultBootstrap.bootstrap();
        //remove other query params
        String samlResp = responseString.split("&")[0];
        String base64EncodedResponse = samlResp.replaceFirst("SAMLResponse=", "").trim();
        String base64URLDecodedResponse = URLDecoder.decode(base64EncodedResponse, "UTF-8");
        byte[] base64DecodedResponse = Base64.decode(base64URLDecodedResponse);
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

        //validate response
        try {
            SamlResponseValidator.validateSAMLResponse(config.getIDPPublicKeyFile(),response);
        } catch (ValidationException e) {
            throw e;
        }
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
     *
     * @param userName
     * @return
     */
    private String updateOrCreateConsumerKeyAuthOnGateway(String userName) {
        String keytoken = "";
        // Publish the service to all relevant gateways
        try {
            String gatewayId = gatewayFacade.getDefaultGateway().getId();
            Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
            IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
            KongConsumer consumer = gatewayLink.getConsumer(userName);
            if (consumer == null) {
                //user doesn't exists, implicit creation
                consumer = gatewayLink.createConsumer(ConsumerConventionUtil.createUserUniqueId(userName));
                KongPluginKeyAuthResponse keyAuthResponse = gatewayLink.addConsumerKeyAuth(consumer.getId());
                keytoken = keyAuthResponse.getKey();
                //we are sure that this consumer must be a physical user
                UserBean tempUser = idmStorage.getUser(userName);
                if (tempUser == null) {
                    initNewUser(userName);
                }
            } else {
                KongPluginKeyAuthResponseList response = gatewayLink.getConsumerKeyAuth(consumer.getId());
                if (response.getData().size() > 0) keytoken = response.getData().get(0).getKey();
                //it is possible that the user exists in the gateway but not in the API Engine
                UserBean userToBeVerified = idmStorage.getUser(userName);
                if (userToBeVerified == null || StringUtils.isEmpty(userToBeVerified.getUsername())) {
                    initNewUser(userName);
                }
            }
            gatewayLink.close();
        } catch (PublishingException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e); //$NON-NLS-1$
        }
        return keytoken;
    }

    /**
     * Updates or creates a consumer on the gateway and in the data model.
     * No ACL activation.
     * Users are created implicitly, first we check if the user exists already on the gateway.
     *
     * The securityflow param triggers optional flows for validation and token issuance.
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
            KongConsumer consumer = gatewayLink.getConsumer(identityAttributes.getUserName());
            if (consumer == null) {
                //user doesn't exists, implicit creation
                consumer = gatewayLink.createConsumer(ConsumerConventionUtil.createUserUniqueId(identityAttributes.getUserName()));
                KongPluginJWTResponse jwtResponse = gatewayLink.addConsumerJWT(consumer.getId());
                jwtKey = jwtResponse.getKey();//JWT "iss"
                jwtSecret = jwtResponse.getSecret();
                //we are sure that this consumer must be a physical user
                UserBean tempUser = idmStorage.getUser(identityAttributes.getUserName());
                if (tempUser == null) {
                    initNewUser(identityAttributes.getUserName());
                }
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
                //it is possible that the user exists in the gateway but not in the API Engine
                UserBean userToBeVerified = idmStorage.getUser(identityAttributes.getUserName());
                if (userToBeVerified == null || StringUtils.isEmpty(userToBeVerified.getUsername())) {
                    initNewUser(identityAttributes.getUserName());
                }
            }
            //set the cache for performance and resilience
            setTokenCache(jwtKey,jwtSecret);
            //start composing JWT token
            JWTRequestBean jwtRequestBean = new JWTRequestBean();
            jwtRequestBean.setIssuer(jwtKey);
            if(cacheBean.getTokenExpirationTimeMinutes()!=null)jwtRequestBean.setExpirationTimeMinutes(cacheBean.getTokenExpirationTimeMinutes());
            else jwtRequestBean.setExpirationTimeMinutes(config.getJWTDefaultTokenExpInMinutes());
            jwtRequestBean.setName(identityAttributes.getUserName());
            jwtRequestBean.setGivenName(identityAttributes.getGivenName());
            jwtRequestBean.setSurname(identityAttributes.getFamilyName());
            jwtRequestBean.setSubject(identityAttributes.getId());
            jwtRequestBean.setAudience(cacheBean.getClientAppRedirect());//callback serves as audience
            jwtRequestBean.setOptionalClaims(cacheBean.getOptionalClaimset());
            issuedJWT = JWTUtils.composeJWT(jwtRequestBean, jwtSecret);
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
    public ExternalUserBean getUserByEmail(String email){
        ExternalUserBean scimUser = userExternalInfoService.getUserInfoByMail(email);
        return getExteralUserAndInit(scimUser);
    }

    public ExternalUserBean getUserByUsername(String username){
        ExternalUserBean scimUser = userExternalInfoService.getUserInfoByUsername(username);
        return getExteralUserAndInit(scimUser);
    }

    public ExternalUserBean getExteralUserAndInit(ExternalUserBean scimUser){
        Preconditions.checkNotNull(scimUser);
        if(scimUser!=null && !StringUtils.isEmpty(scimUser.getUsername())){
            //create the user in early stage - because it's a know external user - and we have the unique identifier coming from a trusted source
            try {
                String gatewayId = gatewayFacade.getDefaultGateway().getId();
                Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
                IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
                String userName = ConsumerConventionUtil.createUserUniqueId(scimUser.getUsername());//should be unique
                KongConsumer consumer = gatewayLink.getConsumer(userName);
                if (consumer == null) {
                    //user doesn't exists, implicit creation
                    consumer = gatewayLink.createConsumer(userName);
                    KongPluginJWTResponse jwtResponse = gatewayLink.addConsumerJWT(consumer.getId());
                    //we are sure that this consumer must be a physical user
                    UserBean tempUser = idmStorage.getUser(userName);
                    if (tempUser == null) {
                        initNewUser(userName);
                    }
                } else {
                    KongPluginJWTResponseList response = gatewayLink.getConsumerJWT(consumer.getId());
                    //it is possible that the user exists in the gateway but not in the API Engine
                    UserBean userToBeVerified = idmStorage.getUser(userName);
                    if (userToBeVerified == null || StringUtils.isEmpty(userToBeVerified.getUsername())) {
                        initNewUser(userName);
                    }
                }
                //close gateway
                gatewayLink.close();
            } catch (PublishingException e) {
                throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
            } catch (Exception e) {
                throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e); //$NON-NLS-1$
            }
        }
        return scimUser;
    }

    /**
     * Stores the secret in a cache, such that we don't have to call each time the Kong client.
     *
     * @param jwtKey
     * @param jwtSecret
     */
    private void setTokenCache(String jwtKey, String jwtSecret) {
        ehcache.getUserTokenCache().put(new net.sf.ehcache.Element(jwtKey,jwtSecret));
    }

    /**
     * Retrieves the secret for a given subject, if the key is not found in the cache, a Kong request will be sent to retrieve the value;.
     *
     * @param key
     * @return
     */
    private String getSecretFromTokenCache(String key, String userName){
        String secret = (String) ehcache.getUserTokenCache().get(key).getObjectValue();
        if(StringUtils.isEmpty(secret)){
            //retrieve from Kong
            String gatewayId = null;
            try {
                gatewayId = gatewayFacade.getDefaultGateway().getId();
                IGatewayLink gatewayLink = gatewayFacade.createGatewayLink(gatewayId);
                List<KongPluginJWTResponse> data = gatewayLink.getConsumerJWT(userName).getData();
                if(data!=null && data.size()>0){
                    secret = data.get(0).getSecret();
                }else throw new StorageException("Refresh JWT - somehow the user is not known");
            } catch (StorageException e) {
                new GatewayException("Error connection to gateway:{}"+e.getMessage());
            }
        }
        return secret;
    }

    public JWTRefreshResponseBean refreshToken(JWTRefreshRequestBean jwtRefreshRequestBean) throws UnsupportedEncodingException, InvalidJwtException, MalformedClaimException, JoseException {
        //get body
        JwtContext jwtContext = JWTUtils.validateHMACToken(jwtRefreshRequestBean.getOriginalJWT());
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        //get secret based on iss/username - cached
        String secret = getSecretFromTokenCache(jwtClaims.getIssuer().toString(),jwtClaims.getSubject());
        JWTRefreshResponseBean jwtRefreshResponseBean = new JWTRefreshResponseBean();
        jwtRefreshResponseBean.setJwt(JWTUtils.refreshJWT(jwtRefreshRequestBean,jwtClaims,secret));
        return jwtRefreshResponseBean;
    }

    /**
     * TODO Specific role implementation should be covered here - depending on using XACML or JWT roles.
     *
     * @param username
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void initNewUser(String username) {
        try {
            //TODO add check if SCIM suppor tis available
            ExternalUserBean userInfoByUsername = userExternalInfoService.getUserInfoByUsername(username);
            //create user
            UserBean newUser = new UserBean();
            newUser.setUsername(UserConventionUtil.formatUserName(username));
            newUser.setAdmin(false);
            if(userInfoByUsername!=null){
                if(userInfoByUsername.getEmails()!=null&&userInfoByUsername.getEmails().size()>0)newUser.setEmail(userInfoByUsername.getEmails().get(0));
                if(!StringUtils.isEmpty(userInfoByUsername.getName()))newUser.setFullName(userInfoByUsername.getName());
                else{
                    if(!StringUtils.isEmpty(userInfoByUsername.getGivenname())&&!StringUtils.isEmpty(userInfoByUsername.getSurname()))newUser.setFullName(userInfoByUsername.getGivenname()+" "+userInfoByUsername.getSurname());
                }
            }
            idmStorage.createUser(newUser);
            //assign default roles in company
            Set<String> roles = new TreeSet<>();
            //TODO do it decently
/*            roles.add(Role.WATCHER.toString());
            roles.add(Role.DEVELOPER.toString());*/
            roles.add(Role.OWNER.toString());
            //assign to default company
            GrantRolesBean usergrants = new GrantRolesBean();
            usergrants.setRoleIds(roles);
            usergrants.setUserId(username);
            organizationFacade.grant(config.getDefaultOrganization(), usergrants);
        } catch (StorageException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e); //$NON-NLS-1$
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
        log.debug("Cache:{}", ehcache.getClientAppCache().getName());
        List keys = ehcache.getClientAppCache().getKeys();
        keys.forEach(key -> log.info("Key found:{} with value {}", key, ehcache.getClientAppCache().get(key)));
    }

    /**
     * Helper method, uses the username and password to perform a Resource owner password grant.
     * This method is used in order to authenticate as well the end user through the registered API engine Service Provider.
     * The following curl does the same:
     * curl -v -k -X POST --user W6FcDk905p5jT5_C_DDec4hAwBMa:nyu7u2If6XBBcQXxi7M6wfHkoK4a
     * -H "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"
     * -d 'grant_type=password&username=michallis&password=Mp12345&scope=read,write'
     * https://idp.t1t.be:9443/oauth2/token
     * <p>
     * The scope parameter is optional!
     *
     * @return
     */
    public String authenticateResourceOwnerCredential(ProxyAuthRequest request) {
        //create webcache object for JWT
        WebClientCacheBean webClientCacheBean = new WebClientCacheBean();
        //check params and add to web cache in order to build JWT
        if(request.getOptionalClaimset()!=null&&request.getOptionalClaimset().size()>0)webClientCacheBean.setOptionalClaimset(request.getOptionalClaimset());
        if(!StringUtils.isEmpty(request.getExpectedAudience()))webClientCacheBean.setClientAppRedirect(request.getExpectedAudience());
        RestIDPConfigBean restConfig=null;
        IDPRestServiceBuilder restServiceBuilder=null;
        try {
            //rest config
            restConfig = new RestIDPConfigBean();
            restConfig.setEndpoint(config.getIDPOAuthTokenEndpoint());
            restConfig.setUsername(config.getIDPOAuthClientId());
            restConfig.setPassword(config.getIDPOAuthClientSecret());
            log.debug("auth config:{}",restConfig);
            restServiceBuilder = new IDPRestServiceBuilder();
            IDPClient idpClient = restServiceBuilder.getSecureService(restConfig, IDPClient.class);
            retrofit.client.Response response = new ProxyLoginFailSafe(idpClient, request.getUsername(),request.getPassword()).execute(); //idpClient.authenticateUser("password", request.getUsername(), request.getPassword(), "authenticate");
            if(response==null)throw new ResourceNotAvailableException("The IDP - OAuth resource is not available at the moment.");
            log.debug("Resource owner OAuth2 authentication towards IDP, response:{}", response.getStatus());
            if (response.getStatus() == HttpStatus.SC_OK){
                //use SCIM to get user info
                ExternalUserBean userInfoByUsername = userExternalInfoService.getUserInfoByUsername(request.getUsername());
                return updateOrCreateConsumerJWTOnGateway(resolveScimAttributeStatements(userInfoByUsername), webClientCacheBean);
            }
            throw new UserNotFoundException("No user found with username:"+request.getUsername());
        } catch (RetrofitError error){
            log.debug("Authentication result:{}",error.getResponse());
            log.debug("error stack:{}",error.getStackTrace());
            throw new UserNotFoundException("No user found with username:"+request.getUsername());
        } finally {
            restConfig = null;
            restServiceBuilder = null;
        }
    }

    /**
     * Parses SCIM attributes to custom IdenityAttributes object.
     * This common IdentityAttr object is commonly used with SAML2.
     *
     * @param externalUserBean
     * @return
     */
    private IdentityAttributes resolveScimAttributeStatements(ExternalUserBean externalUserBean){
        IdentityAttributes identityAttributes = new IdentityAttributes();
        //map values
        identityAttributes.setId(externalUserBean.getAccountId());
        identityAttributes.setUserName(UserConventionUtil.formatUserName(externalUserBean.getUsername()));
        identityAttributes.setFamilyName(externalUserBean.getSurname());
        identityAttributes.setGivenName(externalUserBean.getGivenname());
        return identityAttributes;
    }

}
