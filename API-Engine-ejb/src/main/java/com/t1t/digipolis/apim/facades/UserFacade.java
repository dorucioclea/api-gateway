package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.LoginRequestBean;
import com.t1t.digipolis.apim.beans.user.LoginResponseBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SAMLAuthException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.joda.time.DateTime;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.saml2.core.impl.AuthnRequestBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDPolicyBuilder;
import org.opensaml.saml2.core.impl.RequestedAuthnContextBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import sun.jvm.hotspot.utilities.Assert;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Created by michallispashidis on 16/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserFacade {
    @Inject @APIEngineContext private Logger log;
    @Inject @APIEngineContext private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private IIdmStorage idmStorage;

    public UserBean get(String userId){
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

    public void update(String userId, UpdateUserBean user){
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

    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria){
        try {
            return idmStorage.findUsers(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getOrganizations(String userId){
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

    public List<ApplicationSummaryBean> getApplications(String userId){
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

    public List<ServiceSummaryBean> getServices(String userId){
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

    public SearchResultsBean<AuditEntryBean> getActivity(String userId, int page,int pageSize){
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

    public LoginResponseBean login(LoginRequestBean credentials){

        return null;
    }

    public String generateSAML2AuthRequest(String idpUrl, String spUrl, String spName){
        // Initialize the library
        try{
            //Bootstrap OpenSAML
            DefaultBootstrap.bootstrap();
            //Generate the request
            AuthnRequest authnRequest = buildAuthnRequestObject(spUrl, spName);
            String encodedRequestMessage = encodeAuthnRequest(authnRequest);
            return idpUrl+ "?SAMLRequest=" + encodedRequestMessage;
            //redirectUrl = identityProviderUrl + "?SAMLRequest=" + encodedAuthRequest + "&RelayState=" + relayState;
        }catch(MarshallingException |IOException |ConfigurationException ex){
            throw new SAMLAuthException("Could not generate the SAML2 Auth Request: "+ex.getMessage());
        }
    }

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

    public String processSAML2Response(String response){
        // Initialize the library
        try{
            //Bootstrap OpenSAML
            DefaultBootstrap.bootstrap();
            Assertion assertion = processSSOResponse(response);
            //TODO must be signed
            return encodeSAML2BearerToken(assertion);//bearer token as a query param
        }catch(SAXException| ParserConfigurationException| MarshallingException|UnmarshallingException |IOException |ConfigurationException ex){
            throw new SAMLAuthException("Could not process the SAML2 Response: "+ex.getMessage());
        }
    }

    //TODO check travelocity example for decryption when using X509 certificates
    //we have to take out only the saml assertion in order to construct the saml bearer token.
    private Assertion processSSOResponse(String responseString) throws SAXException, ParserConfigurationException, ConfigurationException, IOException, UnmarshallingException {
        //remove other query params
        String samlResp = responseString.split("&")[0];
        String base64EncodedResponse = samlResp.replaceFirst("SAMLResponse=", "").trim();
        String base64URLDecodedResponse = URLDecoder.decode(base64EncodedResponse,"UTF-8");
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

    private String encodeSAML2BearerToken(Assertion assertion) throws MarshallingException, IOException {
        Marshaller marshaller = null;
        org.w3c.dom.Element authDOM = null;
        StringWriter requestWriter = null;
        String requestMessage = null;
        Deflater deflater = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        DeflaterOutputStream deflaterOutputStream = null;
        String encodedRequestMessage = null;
        marshaller = org.opensaml.Configuration.getMarshallerFactory().getMarshaller(assertion); // object to DOM converter
        authDOM = marshaller.marshall(assertion); // converting to a DOM
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
}
