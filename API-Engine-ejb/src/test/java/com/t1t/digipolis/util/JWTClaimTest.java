package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.jwt.JWTRequestBean;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by michallispashidis on 11/11/15.
 */
public class JWTClaimTest {
    private static final Logger _LOG = LoggerFactory.getLogger(JWTClaimTest.class.getName());
    // JWT - RSA256 - we expect this token to be send through the gateway form the client application
    private static final String JWT_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJjYTVmN2UwMC01NDhkLTQ2NTctYWZhNC01YzJlOTY4NDZiYWUiLCJleHAiOjE0NDcyNDE0ODQsIm5iZiI6MCwiaWF0IjoxNDQ3MjQxMTg0LCJpc3MiOiJodHRwczovL2lkcC50MXQuYmUvYXV0aC9yZWFsbXMvU2lnbkJveCIsImF1ZCI6InNpZ25ib3gtY2xpZW50Iiwic3ViIjoiNDYxYTdiOTYtYzI3NC00ZWQzLTk1MDQtNWZlZTQzYjQxZjQ2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2lnbmJveC1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNjhjMjY4M2QtY2E3Zi00ZThmLTk1MGUtY2M2MmEyZWFiNTI1IiwiY2xpZW50X3Nlc3Npb24iOiJjNWViYTNjMC0zMTQyLTRlNzctOTI3OC05ZDM5MGM4MDk0ZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo5MDA5Il0sInJlc291cmNlX2FjY2VzcyI6eyJzaWduYm94LWNsaWVudCI6eyJyb2xlcyI6WyJhZG1pbmlzdHJhdG9yIiwidXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJUZXN0dXNlciBUZXN0dXNlciIsInByZWZlcnJlZF91c2VybmFtZSI6Im1pY2hhbGxpc0B0ZWxlbmV0LmJlIiwiZ2l2ZW5fbmFtZSI6IlRlc3R1c2VyIiwiZmFtaWx5X25hbWUiOiJUZXN0dXNlciIsImVtYWlsIjoibWljaGFsbGlzQHRlbGVuZXQuYmUifQ.fA8XZsNS496Zb_JzKUEX-LCHMyP3CUuH7IMzucY9OR0bix-Og5-divO_jVYuFyPkg9iZjuGEef2uge9xkreOhEiyz-rihenVklfod1nmVqHMws6wh3BBp38yyPQxiIqeFHdAADJiVFnA39RoSpcE5aMeRkhnXX2mrtzWP0UxVhwsZK9NYbRYa7YkeOJTMVowKMt-ap1TNoXOpuckkiXHeOSDg7Gcz6xAYSoyzSmsU9Xl0vT9c-tL4RoBmaDcqZsssUqUxbaQpjUyNHrfyrnO2UxB-NZy8EDNn1rYMVwQcqHWivkeRIF3eHNj-KcK0tQF6Tr6cdflf9dpNEbmMuN06Q";

    /**
     * Test a JWT RSA-256 signed
     * @throws Exception
     */
    @Test
    public void validateJWT()throws Exception{
        // Sometimes X509 certificate(s) are provided out-of-band somehow by the signer/issuer
        // and the X509VerificationKeyResolver is helpful for that situation. It will use
        // the X.509 Certificate Thumbprint Headers (x5t or x5t#S256) from the JWS/JWT to
        // select from among the provided certificates to get the public key for verification.
        //X509Util x509Util = new X509Util();
        //X509Certificate certificate = x509Util.fromBase64Der("MIICnTCCAYUCBgFQ7AZbGDANBgkqhkiG9w0BAQsFADASMRAwDgYDVQQDDAdTaWduQm94MB4XDTE1MTEwOTExMzIzMFoXDTI1MTEwOTExMzQxMFowEjEQMA4GA1UEAwwHU2lnbkJveDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJ/EKv/3i251FBSaKiO7t1jIJpCl7h9c+Ivu0zQalprZ+zN1VmVLj93e94pAe80JUvNsT4Z8MArJOQDeAgEzrcRZ97MoQ5qWNrdfD+t2/BsMCXqq1H3gAg8GHFAYNEfGacS7RTLQ70G1DfPLT6kCOAcEcIEPuFCVUqGHBcf+HfMe62fgZ0iMoBgmdQqQ7+dL0BsA+OtkufiiaqRZQ8KKrXdLqExKSxoSql29LOkqyheHn2cOLUh/wzKZr7E2sOR2IXAMyfKoDhx19NtMsEIw3lA0ic8lDUKNPti6LfP3siZJqLpcjiNX+0LgecgQqiblqVMLncRh0Lf2xNyzNjAEdW8CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAekv18z0Pdn4IHaKqTpPAfOXVcPsVp4vHjLfARd1+nBAx5EEdWgY8hV/m91Hk9UO/JwwQS1YG9Yc6JrdU2RH3STc3m28JGmAzoxV0VLef8+vvuJnqgWxcIwjB67EFjSmS8uqy+Y+KaVcizhKR4l17/ohkSRAdz+dwFUdBCzEwNxWki+okkDdtZbx4rY53Aexg11jKjzDfN8IOSXhbCfgfuc1HzxtuF8tOmPsWGJK+D2hvOTYSonhMuJCoMWDtOE30B6KaxXSwk4pHvYx/Bx5oSFvvfQwOmD6ElCSUS6Jc8zeXstdxHFP1UySx6+DNA/bFXidWfs6tYbk3crEI+XJodA==");
        //X509VerificationKeyResolver x509VerificationKeyResolver = new X509VerificationKeyResolver(certificate);

        // Optionally the X509VerificationKeyResolver can attempt to verify the signature
        // with the key from each of the provided certificates, if no X.509 Certificate
        // Thumbprint Header is present in the JWT/JWS.
        //x509VerificationKeyResolver.setTryAllOnNoThumbHeader(true);

        // Construct the private key
        //This key is the public Realm key defined at our IDP Proxy
        RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) KeyUtils.getKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn8Qq//eLbnUUFJoqI7u3WMgmkKXuH1z4i+7TNBqWmtn7M3VWZUuP3d73ikB7zQlS82xPhnwwCsk5AN4CATOtxFn3syhDmpY2t18P63b8GwwJeqrUfeACDwYcUBg0R8ZpxLtFMtDvQbUN88tPqQI4BwRwgQ+4UJVSoYcFx/4d8x7rZ+BnSIygGCZ1CpDv50vQGwD462S5+KJqpFlDwoqtd0uoTEpLGhKqXb0s6SrKF4efZw4tSH/DMpmvsTaw5HYhcAzJ8qgOHHX020ywQjDeUDSJzyUNQo0+2Lot8/eyJkmoulyOI1f7QuB5yBCqJuWpUwudxGHQt/bE3LM2MAR1bwIDAQAB"));
        // There's also a key resolver that selects from among a given list of JWKs using the Key ID
        // and other factors provided in the header of the JWS/JWT.
        JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(rsaJsonWebKey);
        JwksVerificationKeyResolver jwksResolver = new JwksVerificationKeyResolver(jsonWebKeySet.getJsonWebKeys());

        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it typically advisable to require a expiration time, a trusted issuer, and
        // and audience that identifies your system as the intended recipient.
        // If the JWT is encrypted too, you need only provide a decryption key or
        // decryption key resolver to the builder.
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipAllDefaultValidators()//only for test purposes!!! otherwise we have invalidation due to exp
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer("https://idp.t1t.be/auth/realms/SignBox") // whom the JWT needs to have been issued by
                .setExpectedAudience("signbox-client") // to whom the JWT is intended for
                //.setVerificationKeyResolver(x509VerificationKeyResolver)
                .setVerificationKeyResolver(jwksResolver)
                .build(); // create the JwtConsumer instance
        JwtClaims claims = jwtConsumer.processToClaims(JWT_TOKEN);
        assertNotNull(claims);//this means, it validates
        _LOG.info("Extracted claims:{}",claims);
        _LOG.info("Claim names:{}",claims.getClaimNames());
        _LOG.info("Name:{}",claims.getClaimValue("name"));
    }

    @Test
    public void issueJWT() throws IOException{
        final String JWT_KEY = "7da8cb6408bb42a4c27785c2c5b467b2";
        final String JWT_SECRET = "ddfd1beb178d449fc4603bec701abb96";
        JWTRequestBean jwtRequestBean = new JWTRequestBean();
        jwtRequestBean.setIssuer(JWT_KEY);
        jwtRequestBean.setAudience("http://consumerapp");
        jwtRequestBean.setExpirationTimeMinutes(10);
        jwtRequestBean.setPlan("free");
        jwtRequestBean.setEmail("michallis@trust1team.com");
        jwtRequestBean.setName("Michallis Pashidis");
        jwtRequestBean.setGivenName("Michallis");
        jwtRequestBean.setSurname("Pashidis");
        jwtRequestBean.setSubject("ex02393");
        String jwt = null;
        try {
            jwt = JWTUtils.composeJWT(jwtRequestBean,JWT_SECRET);
        } catch (JoseException e) {
            fail();
        }
        assertNotNull(jwt);
        assertTrue(!StringUtils.isEmpty(jwt));
        _LOG.info("Generated JWT:{}",jwt);
        
    }
}
