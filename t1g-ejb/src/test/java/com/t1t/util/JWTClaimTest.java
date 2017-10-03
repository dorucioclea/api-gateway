package com.t1t.util;

import com.google.gson.Gson;
import com.t1t.apim.beans.jwt.JWTRequestBean;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.VerificationJwkSelector;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPublicKey;

import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 11/11/15.
 */
public class JWTClaimTest {
    private static final Logger _LOG = LoggerFactory.getLogger(JWTClaimTest.class.getName());
    // JWT - RSA256 - we expect this token to be send through the gateway form the client application
    private static final String JWT_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJjYTVmN2UwMC01NDhkLTQ2NTctYWZhNC01YzJlOTY4NDZiYWUiLCJleHAiOjE0NDcyNDE0ODQsIm5iZiI6MCwiaWF0IjoxNDQ3MjQxMTg0LCJpc3MiOiJodHRwczovL2lkcC50MXQuYmUvYXV0aC9yZWFsbXMvU2lnbkJveCIsImF1ZCI6InNpZ25ib3gtY2xpZW50Iiwic3ViIjoiNDYxYTdiOTYtYzI3NC00ZWQzLTk1MDQtNWZlZTQzYjQxZjQ2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2lnbmJveC1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNjhjMjY4M2QtY2E3Zi00ZThmLTk1MGUtY2M2MmEyZWFiNTI1IiwiY2xpZW50X3Nlc3Npb24iOiJjNWViYTNjMC0zMTQyLTRlNzctOTI3OC05ZDM5MGM4MDk0ZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo5MDA5Il0sInJlc291cmNlX2FjY2VzcyI6eyJzaWduYm94LWNsaWVudCI6eyJyb2xlcyI6WyJhZG1pbmlzdHJhdG9yIiwidXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJUZXN0dXNlciBUZXN0dXNlciIsInByZWZlcnJlZF91c2VybmFtZSI6Im1pY2hhbGxpc0B0ZWxlbmV0LmJlIiwiZ2l2ZW5fbmFtZSI6IlRlc3R1c2VyIiwiZmFtaWx5X25hbWUiOiJUZXN0dXNlciIsImVtYWlsIjoibWljaGFsbGlzQHRlbGVuZXQuYmUifQ.fA8XZsNS496Zb_JzKUEX-LCHMyP3CUuH7IMzucY9OR0bix-Og5-divO_jVYuFyPkg9iZjuGEef2uge9xkreOhEiyz-rihenVklfod1nmVqHMws6wh3BBp38yyPQxiIqeFHdAADJiVFnA39RoSpcE5aMeRkhnXX2mrtzWP0UxVhwsZK9NYbRYa7YkeOJTMVowKMt-ap1TNoXOpuckkiXHeOSDg7Gcz6xAYSoyzSmsU9Xl0vT9c-tL4RoBmaDcqZsssUqUxbaQpjUyNHrfyrnO2UxB-NZy8EDNn1rYMVwQcqHWivkeRIF3eHNj-KcK0tQF6Tr6cdflf9dpNEbmMuN06Q";
    private static final String JWT_TOKEN_RS = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiI0MjYzZmJmM2ViYWY0Y2I2ODgzMDc1MGFmNDI2MmNkNiIsImF1ZCI6Imh0dHBzOi8vZGV2bWt0LnQxdC5iZSIsImV4cCI6MTQ2MzU2NDAyMSwianRpIjoiSVUzV09MSmEwVkhoaUNIX1pJOG9zQSIsImlhdCI6MTQ2MzU2MDQyMSwibmJmIjoxNDYzNTQ5NjIxLCJzdWIiOiJtaWNoYWxsaXNAdHJ1c3QxdGVhbS5jb20iLCJuYW1lIjoiIiwic3VybmFtZSI6IlBhc2hpZGlzIiwiZ2l2ZW5uYW1lIjoiTWljaGFsbGlzIn0.uYgaBb1Z1Mze-zGZikZdm2t9K1Si_G5ZzkizJiW01fONTl_SX37vMx7_NTAuYkb3yRGw7JKnd2v6HqDk1gc6VII5_Jj6P2_ivGABs2i3NQOjahO_CCY67ZvhRCGrXV5OTBY3L-d28_aRhoVaHXZHsCMjhMo757sTaM82GrG0JdmS8YmmS8tEE1WFD-_n8Zh2rTEcmeLCbrrioXXy2AAmc9v6Ih4nQeJMXqb5GHb3oQVGlNawcvIH9tiJ9oc-5zakZbl7sM30iN_iT7-o-wP4GeYxCG8OOv6HzTZG4JjTVKi1wtyJ11PeFjTocmf6rDj9zUkEbryiQFuVbyB6O5u-Ug";

    @Test
    public void validateRsJWT()throws Exception{
        RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) KeyUtils.getKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy5MMbLY62zFu4nvYsEbk\n" +
                "I1oSMgAnaTJAysT+jScIW7hosh1eAMqOpcdWJqvXB7R0eCHFmTu4YwoUgSRLeNsJ\n" +
                "JYk0Fzv/3bTN/2vtGNyCdNSj9LVFd/JcNpvNYKFq5AhZxrJ9rWLYS8+Q9HDA/lve\n" +
                "M9MIS3JMMa72FXOtCcHIruE7AjVSdq7Wc9M+ZE6M3qpiLlYpylfd3+PI6qSigP/G\n" +
                "CB9v804xuk8nLFEBvVHlg2/EvuvOuiDEu9H7UNe/cgjKEcHFm3lPHApHIqqU2blr\n" +
                "Vft+vUl+liuChqTlWpwSBpXc+O3c6WRYcgUfuCciyjiPXDl1SIsidXnptnX6qeIN\n" +
                "GwIDAQAB"));
        JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(rsaJsonWebKey);
        JwksVerificationKeyResolver jwksResolver = new JwksVerificationKeyResolver(jsonWebKeySet.getJsonWebKeys());
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
        JwtClaims claims = jwtConsumer.processToClaims(JWT_TOKEN_RS);
        jwtConsumer.process(JWT_TOKEN_RS).getJoseObjects();


        assertNotNull(claims);//this means, it validates
        _LOG.info("Extracted claims:{}",claims);
        _LOG.info("Claim names:{}",claims.getClaimNames());
        _LOG.info("Name:{}",claims.getClaimValue("name"));
    }

    @Test
    public void retrieveJWTPubKey()throws Exception{
        RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey)KeyUtils.getKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy5MMbLY62zFu4nvYsEbk\n" +
                "I1oSMgAnaTJAysT+jScIW7hosh1eAMqOpcdWJqvXB7R0eCHFmTu4YwoUgSRLeNsJ\n" +
                "JYk0Fzv/3bTN/2vtGNyCdNSj9LVFd/JcNpvNYKFq5AhZxrJ9rWLYS8+Q9HDA/lve\n" +
                "M9MIS3JMMa72FXOtCcHIruE7AjVSdq7Wc9M+ZE6M3qpiLlYpylfd3+PI6qSigP/G\n" +
                "CB9v804xuk8nLFEBvVHlg2/EvuvOuiDEu9H7UNe/cgjKEcHFm3lPHApHIqqU2blr\n" +
                "Vft+vUl+liuChqTlWpwSBpXc+O3c6WRYcgUfuCciyjiPXDl1SIsidXnptnX6qeIN\n" +
                "GwIDAQAB"));
        JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(rsaJsonWebKey);
        new JwksVerificationKeyResolver(jsonWebKeySet.getJsonWebKeys());
        JsonWebSignature jws = new JsonWebSignature();
        jws.setCompactSerialization(JWT_TOKEN_RS);
        VerificationJwkSelector jwkSelector = new VerificationJwkSelector();
        JsonWebKey jwk = jwkSelector.select(jws, jsonWebKeySet.getJsonWebKeys());
        Gson gson = new Gson();
        _LOG.info("Extracted JWK:{}",gson.toJson(jwk));
        _LOG.info("Extracted public key:{}",jwk.getKey());
    }

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
    public void issueJWT() throws Exception{
        final String JWT_KEY = "7da8cb6408bb42a4c27785c2c5b467b2";
        final String JWT_SECRET = "ddfd1beb178d449fc4603bec701abb96";
        /*final PrivateKey privateKey = KeyUtils.getPrivateKey("-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpAIBAAKCAQEAw5mp3MS3hVLkHwB9lMrEx34MjYCmKeH/XeMLexNpTd1FzuNv\n" +
                "6rArovTY763CDo1Tp0xHz0LPlDJJtpqAgsnfDwCcgn6ddZTo1u7XYzgEDfS8J4SY\n" +
                "dcKxZiSdVTpb9k7pByXfnwK/fwq5oeBAJXISv5ZLB1IEVZHhUvGCH0udlJ2vadqu\n" +
                "R03phBHcvlNmMbJGWAetkdcKyi+7TaW7OUSjlge4WYERgYzBB6eJH+UfPjmw3aSP\n" +
                "ZcNXt2RckPXEbNrL8TVXYdEvwLJoJv9/I8JPFLiGOm5uTMEk8S4txs2efueg1Xyy\n" +
                "milCKzzuXlJvrvPA4u6HI7qNvuvkvUjQmwBHgwIDAQABAoIBAQCP3ZblTT8abdRh\n" +
                "xQ+Y/+bqQBjlfwk4ZwRXvuYz2Rwr7CMrP3eSq4785ZAmAaxo3aP4ug9bL23UN4Sm\n" +
                "LU92YxqQQ0faZ1xTHnp/k96SGKJKzYYSnuEwREoMscOS60C2kmWtHzsyDmhg/bd5\n" +
                "i6JCqHuHtPhsYvPTKGANjJrDf+9gXazArmwYrdTnyBeFC88SeRG8uH2lP2VyqHiw\n" +
                "ZvEQ3PkRRY0yJRqEtrIRIlgVDuuu2PhPg+MR4iqR1RONjDUFaSJjR7UYWY/m/dmg\n" +
                "HlalqpKjOzW6RcMmymLKaW6wF3y8lbs0qCjCYzrD3bZnlXN1kIw6cxhplfrSNyGZ\n" +
                "BY/qWytJAoGBAO8UsagT8tehCu/5smHpG5jgMY96XKPxFw7VYcZwuC5aiMAbhKDO\n" +
                "OmHxYrXBT/8EQMIk9kd4r2JUrIx+VKO01wMAn6fF4VMrrXlEuOKDX6ZE1ay0OJ0v\n" +
                "gCmFtKB/EFXXDQLV24pgYgQLxnj+FKFV2dQLmv5ZsAVcmBHSkM9PBdUlAoGBANFx\n" +
                "QPuVaSgRLFlXw9QxLXEJbBFuljt6qgfL1YDj/ANgafO8HMepY6jUUPW5LkFye188\n" +
                "J9wS+EPmzSJGxdga80DUnf18yl7wme0odDI/7D8gcTfu3nYcCkQzeykZNGAwEe+0\n" +
                "SvhXB9fjWgs8kFIjJIxKGmlMJRMHWN1qaECEkg2HAoGBAIb93EHW4as21wIgrsPx\n" +
                "5w8up00n/d7jZe2ONiLhyl0B6WzvHLffOb/Ll7ygZhbLw/TbAePhFMYkoTjCq++z\n" +
                "UCP12i/U3yEi7FQopWvgWcV74FofeEfoZikLwa1NkV+miUYskkVTnoRCUdJHREbE\n" +
                "PrYnx2AOLAEbAxItHm6vY8+xAoGAL85JBePpt8KLu+zjfximhamf6C60zejGzLbD\n" +
                "CgN/74lfRcoHS6+nVs73l87n9vpZnLhPZNVTo7QX2J4M5LHqGj8tvMFyM895Yv+b\n" +
                "3ihnFVWjYh/82Tq3QS/7Cbt+EAKI5Yzim+LJoIZ9dBkj3Au3eOolMym1QK2ppAh4\n" +
                "uVlJORsCgYBv/zpNukkXrSxVHjeZj582nkdAGafYvT0tEQ1u3LERgifUNwhmHH+m\n" +
                "1OcqJKpbgQhGzidXK6lPiVFpsRXv9ICP7o96FjmQrMw2lAfC7stYnFLKzv+cj8L9\n" +
                "h4hhNWM6i/DHXjPsHgwdzlX4ulq8M7dR8Oqm9DrbdAyWz8h8/kzsnA==\n" +
                "-----END RSA PRIVATE KEY-----");*/
        final String JWT_AUDIENCE = "http://consumerapp";
        JWTRequestBean jwtRequestBean = new JWTRequestBean();
        jwtRequestBean.setIssuer(JWT_KEY);
        jwtRequestBean.setAudience(JWT_AUDIENCE);
        jwtRequestBean.setExpirationTimeSeconds(10);
        jwtRequestBean.setPlan("free");
        jwtRequestBean.setEmail("michallis@trust1team.com");
        jwtRequestBean.setName("Michallis Pashidis");
        jwtRequestBean.setGivenName("Michallis");
        jwtRequestBean.setSurname("Pashidis");
        jwtRequestBean.setSubject("ex02393");
        String jwt = null;
        try {
            jwt = JWTUtils.composeJWT(jwtRequestBean, 60, new HmacKey(JWT_SECRET.getBytes("UTF-8")), "somenendpoint");
        } catch (JoseException e) {
            fail();
        }
        assertNotNull(jwt);
        assertTrue(!StringUtils.isEmpty(jwt));
        _LOG.info("Generated JWT:{}",jwt);

        //validate
        try {
            JwtContext jwtContext = JWTUtils.validateHMACToken(jwt, JWT_SECRET, JWT_KEY, JWT_AUDIENCE, Boolean.FALSE);
            _LOG.info("JWT validation succeeded.");
            _LOG.info("Claim names:{}",jwtContext.getJwtClaims().getClaimsMap());
        } catch (InvalidJwtException e) {
            fail();
        }
    }
}
