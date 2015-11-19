package com.t1t.digipolis.util;

import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPublicKey;
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
    public JwtContext validateToken(String jwtToken, String publicKey, String expectedIssuer, String expectedAudience) throws InvalidJwtException {
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
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipAllDefaultValidators()//only for test purposes!!! otherwise we have invalidation due to exp
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer(expectedIssuer) // whom the JWT needs to have been issued by
                .setExpectedAudience(expectedAudience) // to whom the JWT is intended for
                        //.setVerificationKeyResolver(x509VerificationKeyResolver)
                .setVerificationKeyResolver(jwksResolver)
                .build(); // create the JwtConsumer instance
        JwtContext jwtContext = jwtConsumer.process(jwtToken);
        _LOG.info("JWT-token:{}", jwtContext.getJwt());
        _LOG.info("JWT-claims:{}", jwtContext.getJwtClaims());
        return jwtContext;
    }

    //TODO don't do this hard coded and unfinished :-)
    public List<String> getRoles(JwtContext jwtContext) {
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        org.json.JSONObject resourceObject = (org.json.JSONObject) jwtClaims.getClaimValue("resource_access");
        return null;
    }
}
