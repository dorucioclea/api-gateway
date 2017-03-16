package com.t1t.util;

import com.t1t.apim.AppConfig;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.SAMLAuthException;
import com.t1t.apim.exceptions.i18n.Messages;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.SignableXMLObject;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michallispashidis on 15/12/15.
 */
public class SamlResponseValidator {

    private static Logger _LOG = LoggerFactory.getLogger(SamlResponseValidator.class.getName());
    private static final String SEPARATOR_CHAR = ",";

    public static void validateSAMLResponse(Response response, AppConfig config) throws SAMLAuthException {
        List<String> idpEntityIds;
        if (config.getIDPEntityId().contains(SEPARATOR_CHAR)) {
            idpEntityIds = Arrays.asList(config.getIDPEntityId().split(SEPARATOR_CHAR));
        }
        else {
            idpEntityIds = Arrays.asList(config.getIDPEntityId());
        }

        if (response.getIssuer() != null && !idpEntityIds.contains(response.getIssuer().getValue())) {
            throw ExceptionFactory.samlAuthException(Messages.i18n.format("samlIdpEntity", response.getIssuer().getValue(), idpEntityIds.toString()));
        }

        validateSignature(response.getSignature(), getCertificate(response));

        response.getAssertions().forEach(assertion -> validateSAMLAssertion(assertion, idpEntityIds, config.getIDPNotBeforeDelay()));

        _LOG.debug("SAML Response is valid");
    }

    private static void validateSignature(Signature signature, X509Certificate certificate) {
        try {

            BasicX509Credential publicCredential = new BasicX509Credential();

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(certificate.getPublicKey().getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(publicKeySpec);

            publicCredential.setPublicKey(key);
            publicCredential.setEntityCertificate(certificate);
            publicCredential.getEntityCertificateChain().add(certificate);

            SignatureValidator sigValidator = new SignatureValidator(publicCredential);

            sigValidator.validate(signature);

        }
        catch (ValidationException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw ExceptionFactory.samlAuthException(ex.getMessage());
        }
    }

    private static X509Certificate getCertificate(SignableXMLObject signableObject) {
        try {
            String base64Cert = signableObject.getSignature().getKeyInfo().getX509Datas().get(0).getX509Certificates().get(0).getValue();
            byte[] decodedCert = Base64.decode(base64Cert);
            return (X509Certificate) CertificateFactory
                    .getInstance("X.509")
                    .generateCertificate(new ByteArrayInputStream(decodedCert));
        }
        catch (Exception ex) {
            _LOG.error("Parsing certificate error:{}", ex);
            throw ExceptionFactory.samlAuthException(Messages.i18n.format("samlSignatureCertificate"));
        }
    }


    private static void validateSAMLAssertion(Assertion assertion, List<String> idpEntityIds, Integer delay) throws SAMLAuthException {
        //Validate IDP EntityID
        if (assertion.getIssuer() != null && !idpEntityIds.contains(assertion.getIssuer().getValue())) {
            throw ExceptionFactory.samlAuthException(Messages.i18n.format("samlIdpEntity", assertion.getIssuer().getValue(), idpEntityIds.toString()));
        }
        //Validate assertion conditions
        if (assertion.getConditions().getNotBefore() != null && assertion.getConditions().getNotBefore().minusSeconds(delay).isAfterNow()) {
            throw ExceptionFactory.samlAuthException(Messages.i18n.format("samlNotBefore"));
        }
        if (assertion.getConditions().getNotOnOrAfter() != null
                && (assertion.getConditions().getNotOnOrAfter().isBeforeNow() || assertion.getConditions().getNotOnOrAfter().isEqualNow())) {
            throw ExceptionFactory.samlAuthException(Messages.i18n.format("samlNotAfter"));
        }
        //Validate assertion signature
        if (assertion.getSignature() != null) {
            X509Certificate certificate = getCertificate(assertion);
            if (certificate != null) {
                validateSignature(assertion.getSignature(), getCertificate(assertion));
            }
            else {
                _LOG.debug("Assertion signature doesn't contain certificate");
            }
        }
        else {
            _LOG.debug("Assertion is not signed");
        }
    }
}