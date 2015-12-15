package com.t1t.digipolis.util;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by michallispashidis on 15/12/15.
 */
public class SamlResponseValidator {
    public void validateSAMLResponse(String certPath, Response response) throws ValidationException {
        try {
            //Get Public Key
            BasicX509Credential publicCredential = new BasicX509Credential();
            File publicKeyFile = new File("/opt.cer");


            if (publicKeyFile.exists()) {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                InputStream fileStream = new FileInputStream(publicKeyFile);
                X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(fileStream);
                fileStream.close();

                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(certificate.getPublicKey().getEncoded());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey key = keyFactory.generatePublic(publicKeySpec);

                //Validate Public Key against Signature
                if (key != null) {
                    publicCredential.setPublicKey(key);
                    SignatureValidator signatureValidator = new SignatureValidator(publicCredential);
                    signatureValidator.validate(response.g);
                }
            }
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }
}
