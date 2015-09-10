package com.t1t.digipolis.apim.facades;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.GatewaySummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.LoginRequestBean;
import com.t1t.digipolis.apim.beans.user.LoginResponseBean;
import com.t1t.digipolis.apim.beans.user.SAMLResponseRedirect;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.SAMLAuthException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.qualifier.APIEngineContext;
import net.sf.ehcache.Ehcache;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.*;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.*;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
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
public class UserFacade {
    @Inject
    @APIEngineContext
    private Logger log;
    @Inject
    @APIEngineContext
    private EntityManager em;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IStorage storage;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    @APIEngineContext
    private Ehcache ehcache;
    @Inject private OrganizationFacade organizationFacade;

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

    public void update(String userId, UpdateUserBean user) {
        try {
            UserBean updatedUser = idmStorage.getUser(userId);
            if (updatedUser == null) {
                throw ExceptionFactory.userNotFoundException(userId);
            }
            if (user.getEmail() != null) {
                updatedUser.setEmail(user.getEmail());
            }
            if (user.getFullName() != null) {
                updatedUser.setFullName(user.getFullName());
            }
            idmStorage.updateUser(updatedUser);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
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

    public LoginResponseBean login(LoginRequestBean credentials) {

        return null;
    }

    public String generateSAML2AuthRequest(String idpUrl, String spUrl, String spName, String clientUrl) {
        // Initialize the library
        try {
            //Bootstrap OpenSAML
            DefaultBootstrap.bootstrap();
            //Generate the request
            AuthnRequest authnRequest = buildAuthnRequestObject(spUrl, spName);
            //set client application name and callback in the cache
            ehcache.put(new net.sf.ehcache.Element(spName, clientUrl));
            String encodedRequestMessage = encodeAuthnRequest(authnRequest);
            return idpUrl + "?SAMLRequest=" + encodedRequestMessage;
            //redirectUrl = identityProviderUrl + "?SAMLRequest=" + encodedAuthRequest + "&RelayState=" + relayState;
        } catch (MarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not generate the SAML2 Auth Request: " + ex.getMessage());
        }
    }

    /**
     * Build SAML2 authentication request
     *
     * @param spUrl
     * @param spName
     * @return
     */
    private AuthnRequest buildAuthnRequestObject(String spUrl, String spName) {
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
        nameIdPolicy.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");//TODO can be set aswel as param?
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
        return authRequest;
    }

    /**
     * Encode the Authentication Request (base64 and URL encoded)
     *
     * @param authnRequest
     * @return
     * @throws MarshallingException
     * @throws IOException
     */
    private String encodeAuthnRequest(AuthnRequest authnRequest) throws MarshallingException, IOException {
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
    public SAMLResponseRedirect processSAML2Response(String response) {
        // Initialize the library
        //get only the samlResponse
        String samlResp = response.split("&")[0];//remove other form params
        String base64EncodedResponse = samlResp.replaceFirst("SAMLResponse=", "").trim();
        String clientAppName = "";
        String userName = "";
        StringBuffer clientUrl = new StringBuffer("");
        try {
            Assertion assertion = processSSOResponse(response);
            clientAppName = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI();
            userName = assertion.getSubject().getNameID().getValue();
            log.info("Audience URI found: {}", clientAppName);
        } catch (SAXException | ParserConfigurationException | UnmarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not process the SAML2 Response: " + ex.getMessage());
        }
        SAMLResponseRedirect responseRedirect = new SAMLResponseRedirect();
        //return the SAML2 Bearer token
        //responseRedirect.setToken(base64EncodedResponse);
        responseRedirect.setToken(updateOrCreateConsumerOnGateway(userName));
        clientUrl.append((String) ehcache.get(clientAppName).getObjectValue());
        if (!clientUrl.toString().endsWith("/")) clientUrl.append("/");
        responseRedirect.setClientUrl(clientUrl.toString());
        return responseRedirect; //be aware that this is enflated.

        //Bootstrap OpenSAML - only Assertion?!
/*      try {
            DefaultBootstrap.bootstrap();
            Assertion assertion = processSSOResponse(response);
            return encodeSAML2BearerToken(assertion);//bearer token as a query param
        } catch (SAXException | ParserConfigurationException | MarshallingException | UnmarshallingException | IOException | ConfigurationException ex) {
            throw new SAMLAuthException("Could not process the SAML2 Response: " + ex.getMessage());
        }*/
    }

    //TODO check travelocity example for decryption when using X509 certificates
    //we have to take out only the saml assertion in order to construct the saml bearer token.

    /**
     * Method processes the SAML2 Response in order to capture the SAML Assertion.
     * See {@link http://sureshatt.blogspot.be/2012/11/how-to-read-saml-20-response-with.html}
     *
     * @param responseString
     * @return
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ConfigurationException
     * @throws IOException
     * @throws UnmarshallingException
     */
    private Assertion processSSOResponse(String responseString) throws SAXException, ParserConfigurationException, ConfigurationException, IOException, UnmarshallingException {
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
    private String encodeSAML2BearerToken(Assertion assertion) throws MarshallingException, IOException {
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
     * TODO we don't use this method, retest SAML exchange for OAuth when certificates are present.
     *
     * @param responseMessage
     * @return
     * @throws ConfigurationException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws UnmarshallingException
     */
    public String exchangeSAMLTokenToOauth(String responseMessage) throws ConfigurationException, ParserConfigurationException, IOException, SAXException, UnmarshallingException {
        DefaultBootstrap.bootstrap();
        String samlResp = responseMessage.split("&")[0];
        String base64EncodedResponse = samlResp.replaceFirst("SAMLResponse=", "").trim();
        String base64URLDecodedResponse = URLDecoder.decode(base64EncodedResponse, "UTF-8");
        byte[] base64DecodedResponse = Base64.decode(base64URLDecodedResponse);
        ByteArrayInputStream is = new ByteArrayInputStream(base64DecodedResponse);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(is);
        Element element = document.getDocumentElement();
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
        XMLObject responseXmlObj = unmarshaller.unmarshall(element);
        Response responseObj = (Response) responseXmlObj;
        // Get the SAML2 Assertion part from the response
        StringWriter rspWrt = new StringWriter();
        XMLHelper.writeNode(responseObj.getAssertions().get(0).getDOM(), rspWrt);
        String requestMessage = rspWrt.toString();
        // Get the Base64 encoded string of the message
        // Then Get it prepared to send it over HTTP protocol
        String encodedRequestMessage = Base64.encodeBytes(requestMessage.getBytes(), Base64.DONT_BREAK_LINES);
        String enc_rslt = URLEncoder.encode(encodedRequestMessage, "UTF-8").trim();
        //Create connection to the Token endpoint of API manger
        URL url = new URL("https://idp.t1t.be:9443/oauth2/token/");
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url.toString());
        //add header
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        String userCredentials = "W6FcDk905p5jT5_C_DDec4hAwBMa:nyu7u2If6XBBcQXxi7M6wfHkoK4a";
        String basicAuth = "Basic " + new String(Base64.encodeBytes(userCredentials.getBytes()));
        basicAuth = basicAuth.replaceAll("\\r|\\n", "");
        post.setHeader("Authorization", basicAuth);
        //add content
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("grant_type", "urn:ietf:params:oauth:grant-type:saml2-bearer"));
        urlParameters.add(new BasicNameValuePair("assertion", enc_rslt));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        //Execute
        HttpResponse response = client.execute(post);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    /**
     * Return default gateway - normally one should be supported for each environment.
     * The Kong gateway will sync all actions towards the other shards in the same environment.
     *
     * @return
     * @throws StorageException
     */
    private GatewaySummaryBean getDefaultGateway() throws StorageException {
        return query.listGateways().get(0);
    }

    /**
     * Updates or creates a consumer on the gateway and in the data model.
     * TODO: should updated with ACL
     *
     * @param userName
     * @return
     */
    private String updateOrCreateConsumerOnGateway(String userName) {
        String keytoken = "";
        // Publish the service to all relevant gateways
        try {
            String gatewayId = getDefaultGateway().getId();
            Preconditions.checkArgument(!StringUtils.isEmpty(gatewayId));
            IGatewayLink gatewayLink = createGatewayLink(gatewayId);
            KongConsumer consumer = gatewayLink.getConsumer(userName);
            if (consumer == null) {
                //user doesn't exists, implicit creation
                consumer = gatewayLink.createConsumer(userName);
                KongPluginKeyAuthResponse keyAuthResponse = gatewayLink.addConsumerKeyAuth(consumer.getId());
                keytoken = keyAuthResponse.getKey();
                //we are sure that this consumer must be a physical user
                UserBean tempUser = idmStorage.getUser(userName);
                if (tempUser == null) {
                    //create user
                    UserBean newUser = new UserBean();
                    newUser.setUsername(userName);
                    idmStorage.createUser(newUser);
                    //TODO add apike/user to ACL -> version 0.5.0 of Kong
                    //assign to default company
                    //assign default roles in company
                    initNewUser(userName);
                }
            } else {
                //TEMP list, but should be ACL - a consumer can have more keys
                KongPluginKeyAuthResponseList response = gatewayLink.getConsumerKeyAuth(consumer.getId());
                if (response.getData().size() > 0) keytoken = response.getData().get(0).getKey();
            }
            gatewayLink.close();
            //TODO add audit trail for consumer actions
        } catch (PublishingException e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("PublishError"), e); //$NON-NLS-1$
        } catch (Exception e) {
            throw ExceptionFactory.actionException(Messages.i18n.format("GrantError"), e); //$NON-NLS-1$
        }
        return keytoken;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void initNewUser(String username){
        Set<String> roles = new TreeSet<>();
        roles.add("ApplicationDeveloper");
        roles.add("ServiceDeveloper");
        roles.add("OrganizationOwner");
        GrantRolesBean usergrants = new GrantRolesBean();
        usergrants.setRoleIds(roles);
        usergrants.setUserId(username);
        organizationFacade.grant("Digipolis",usergrants);
    }

    /**
     * Creates a gateway link given a gateway id.
     * TODO duplicated in ActionFacade => set as utility
     *
     * @param gatewayId
     */
    private IGatewayLink createGatewayLink(String gatewayId) throws PublishingException {
        try {
            GatewayBean gateway = storage.getGateway(gatewayId);
            if (gateway == null) {
                throw new GatewayNotFoundException();
            }
            IGatewayLink link = gatewayLinkFactory.create(gateway);
            return link;
        } catch (GatewayNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PublishingException(e.getMessage(), e);
        }
    }

}
