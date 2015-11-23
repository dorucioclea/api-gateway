package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.idm.ExternalUserBean;
import com.t1t.digipolis.apim.beans.jwt.IJWT;
import com.t1t.digipolis.apim.beans.jwt.JWTRequestBean;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michallispashidis on 19/11/15.
 */
public class JWTUtils {
    private static Logger _LOG = LoggerFactory.getLogger(JWTUtils.class.getName());

    /**
     * Validate token signed with RSA algorithm.
     *
     * @param jwtToken
     * @param publicKey
     * @param expectedIssuer
     * @param expectedAudience
     * @return
     * @throws InvalidJwtException
     */
    public static JwtContext validateRSAToken(String jwtToken, String publicKey, String expectedIssuer, String expectedAudience, Boolean skipDefaultValidators) throws InvalidJwtException {
        //This key is the public Realm key defined at our IDP Proxy
        RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) KeyUtils.getKey(publicKey));
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
        JwtConsumer jwtConsumer = null;
        if(skipDefaultValidators){
            jwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllDefaultValidators()//only for test purposes!!! otherwise we have invalidation due to exp
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setExpectedIssuer(expectedIssuer) // whom the JWT needs to have been issued by
                    .setExpectedAudience(expectedAudience) // to whom the JWT is intended for
                            //.setVerificationKeyResolver(x509VerificationKeyResolver)
                    .setVerificationKeyResolver(jwksResolver)
                    .build(); // create the JwtConsumer instance
        }else{
            jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setExpectedIssuer(expectedIssuer) // whom the JWT needs to have been issued by
                    .setExpectedAudience(expectedAudience) // to whom the JWT is intended for
                            //.setVerificationKeyResolver(x509VerificationKeyResolver)
                    .setVerificationKeyResolver(jwksResolver)
                    .build(); // create the JwtConsumer instance
        }

        JwtContext jwtContext = jwtConsumer.process(jwtToken);
        _LOG.info("JWT-token:{}", jwtContext.getJwt());
        _LOG.info("JWT-claims:{}", jwtContext.getJwtClaims());
        return jwtContext;
    }

    public static JwtContext validateHMACToken(String jwtToken, String secret, String expectedIssuer, String expectedAudience, Boolean skipDefaultValidators) throws InvalidJwtException, UnsupportedEncodingException {
        JwtConsumer jwtConsumer = null;
        if(skipDefaultValidators){
            jwtConsumer =  new JwtConsumerBuilder()
                    .setSkipAllDefaultValidators()
                    .setVerificationKey(new HmacKey(secret.getBytes()))
                    .setRelaxVerificationKeyValidation() // allow shorter HMAC keys when used w/ HSxxx algs
                    .build();
        }else{
            jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setExpectedIssuer(expectedIssuer) // whom the JWT needs to have been issued by
                    .setExpectedAudience(expectedAudience) // to whom the JWT is intended for
                    .setVerificationKey(new HmacKey(secret.getBytes()))
                    .build(); // create the JwtConsumer instance
        }

        JwtContext jwtContext = jwtConsumer.process(jwtToken);
        _LOG.info("JWT-token:{}", jwtContext.getJwt());
        _LOG.info("JWT-claims:{}", jwtContext.getJwtClaims());
        return jwtContext;
    }

    public static String composeJWT(JWTRequestBean jwtRequestBean, String secret) throws JoseException, UnsupportedEncodingException {
        // The JWT is signed using the private key
        Key key = new HmacKey(secret.getBytes("UTF-8"));

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(jwtRequestBean.getIssuer());  // who creates the token and signs it
        claims.setAudience(jwtRequestBean.getAudience()); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        //Custom fields
        claims.setSubject(jwtRequestBean.getSubject()); // the subject/principal is whom the token is about
        claims.setClaim(IJWT.NAME, jwtRequestBean.getName());
        claims.setClaim(IJWT.EMAIL, jwtRequestBean.getEmail()); // additional claims/attributes about the subject can be added
        claims.setClaim(IJWT.SURNAME, jwtRequestBean.getSurname());
        claims.setClaim(IJWT.GIVEN_NAME, jwtRequestBean.getGivenName());
        //List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        //claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        JsonWebSignature jws = new JsonWebSignature();
        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(key);
        jws.setHeader(IJWT.HEADER_TYPE,IJWT.HEADER_TYPE_VALUE);
        jws.setDoKeyValidation(false); // relaxes the key length requirement

        String issuedJwt = null;
        issuedJwt = jws.getCompactSerialization();
        return issuedJwt;
    }

    //TODO don't do this hard coded and unfinished :-)
    public List<String> getRoles(JwtContext jwtContext) {
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        org.json.JSONObject resourceObject = (org.json.JSONObject) jwtClaims.getClaimValue("resource_access");
        return null;
    }
}
