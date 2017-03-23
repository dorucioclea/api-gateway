package com.t1t.apim.kong;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by michallispashidis on 20/08/15.
 */
public class KongUtils {
    private static Logger log = LoggerFactory.getLogger(KongUtils.class.getName());

    public static void main(String []args){
        log.info("basic auth header: "+getBasicAuthHeaderValueEncoded("W6FcDk905p5jT5_C_DDec4hAwBMa","nyu7u2If6XBBcQXxi7M6wfHkoK4a"));
        log.info(base64UrlEncode("<saml2:Assertion ID=\"lphaoleemibbkkbgdleajedcmdkmjdfmgappplpd\" IssueInstant=\"2015-09-05T08:54:15.400Z\" Version=\"2.0\" xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\"><saml2:Issuer Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:entity\">localhost</saml2:Issuer><saml2:Subject><saml2:NameID Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:entity\">admin</saml2:NameID><saml2:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\"><saml2:SubjectConfirmationData NotOnOrAfter=\"2015-09-05T08:59:15.400Z\" Recipient=\"http://localhost:8080/API-Engine-web/v1/users/idp/callback\"/></saml2:SubjectConfirmation></saml2:Subject><saml2:Conditions NotBefore=\"2015-09-05T08:54:15.400Z\" NotOnOrAfter=\"2015-09-05T08:59:15.400Z\"><saml2:AudienceRestriction><saml2:Audience>apimarket</saml2:Audience></saml2:AudienceRestriction></saml2:Conditions><saml2:AuthnStatement AuthnInstant=\"2015-09-05T08:54:15.400Z\"><saml2:AuthnContext><saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml2:AuthnContextClassRef></saml2:AuthnContext></saml2:AuthnStatement></saml2:Assertion>"));
    }
    /**
     * Utility method that encodes a name and password for basic authentication.
     * @param name
     * @param password
     * @return
     */
    public static String getBasicAuthHeaderValueEncoded(String name, String password){
        String authString = name + ":" + password;
        System.out.println("auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        return new String(authEncBytes);
    }

    public static String base64UrlEncode(String xmlString){
        String result ="";
        try {
            result = URLEncoder.encode(Base64.encodeBase64String(xmlString.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
