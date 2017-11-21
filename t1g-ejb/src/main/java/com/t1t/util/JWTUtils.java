package com.t1t.util;

import com.t1t.apim.beans.jwt.IJWT;
import com.t1t.apim.beans.jwt.JWTRequestBean;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.i18n.Messages;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by michallispashidis on 19/11/15.
 */
public class JWTUtils {
    public static final String JWT_HS256 = "HS256";
    public static final String JWT_RS256 = "RS256";
    private static final String NO_LONGER_VALID = "The JWT is no longer valid";
    private static Logger _LOG = LoggerFactory.getLogger(JWTUtils.class.getName());


    /*public static JwtContext validateRSAToken(String jwtToken, String publicKey, String expectedIssuer, String expectedAudience, Boolean skipDefaultValidators) throws InvalidJwtException {
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
    }*/

    /**
     * Validate token signed with RSA algorithm
     *
     * @param jwt
     * @param expectedIssuer
     * @param publicKeys
     * @return
     * @throws InvalidJwtException
     */
    public static JwtContext validateRSAToken(String jwt, String expectedIssuer, Set<String> publicKeys) throws InvalidJwtException {
        JwtContext context = null;
        JsonWebKeySet jsonWebKeySet = new JsonWebKeySet();
        publicKeys.forEach(key -> {
            jsonWebKeySet.addJsonWebKey(new RsaJsonWebKey((RSAPublicKey) KeyUtils.getPublicKey(key)));
        });
        return validateRSAToken(jwt, expectedIssuer, jsonWebKeySet, null);
    }

    /**
     * Validate token signed with RSA algorithm
     *
     * @param token
     * @param issuer
     * @param jwks
     * @return
     */
    public static JwtContext validateRSAToken(String token, String issuer, HttpsJwks jwks) throws InvalidJwtException {
        return validateRSAToken(token, issuer, null, jwks);
    }

    /**
     * Validate token signed with RSA algorithm
     *
     * @param jwt
     * @param expectedIssuer
     * @param publicKey
     * @return
     * @throws InvalidJwtException
     */
    public static JwtContext validateRSAToken(String jwt, String expectedIssuer, String publicKey) throws InvalidJwtException {
        return validateRSAToken(jwt, expectedIssuer, Collections.singleton(publicKey));
    }

    /**
     * Validate token signed with RSA algorithm.
     *
     * @param jwt
     * @param expectedIssuer
     * @param publicKeys
     * @return
     * @throws InvalidJwtException
     */
    public static JwtContext validateRSAToken(String jwt, String expectedIssuer, JsonWebKeySet jsonWebKeySet, HttpsJwks jwks) throws InvalidJwtException {
        try {
            JwtContext context = null;
            JwtConsumerBuilder builder = new JwtConsumerBuilder()
                    .setExpectedIssuer(expectedIssuer)
                    .setRequireSubject()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireExpirationTime()
                    .setSkipDefaultAudienceValidation();
            if (jsonWebKeySet != null) {
                builder.setVerificationKeyResolver(new JwksVerificationKeyResolver(jsonWebKeySet.getJsonWebKeys()));
            }
            else if (jwks != null) {
                builder.setVerificationKeyResolver(new HttpsJwksVerificationKeyResolver(jwks));
            }
            else {
                throw ExceptionFactory.tokenNotVerifiedException(Messages.i18n.format(ErrorCodes.PUB_KEY_IRRETRIEVABLE, expectedIssuer));
            }

            context = builder.build().process(jwt);
            if (context == null) {
                throw ExceptionFactory.jwtInvalidException(Messages.i18n.format(ErrorCodes.JWT_SIGNATURE_VERIFICATION_ERROR));
            }
            return context;
        } catch (InvalidJwtException ex) {
            if (ex.getMessage().contains(NO_LONGER_VALID)) {
                throw ExceptionFactory.jwtExpiredException(Messages.i18n.format(ErrorCodes.EXPIRED_JWT), ex);
            } else {
                throw ExceptionFactory.jwtInvalidException(Messages.i18n.format(ErrorCodes.JWT_SIGNATURE_VERIFICATION_ERROR), ex);
            }
        }
    }

    /**
     * Kong validates the JWT token, thus for receiving tokens, we don't need to revalidate, just parse and return the JwtContext.
     *
     * @param jwtToken
     * @return
     * @throws InvalidJwtException
     * @throws UnsupportedEncodingException
     */
    public static JwtContext consumeUnvalidatedToken(String jwtToken) throws InvalidJwtException, UnsupportedEncodingException, MalformedClaimException {
        return getUnvalidatedContext(jwtToken);
    }

    public static JwtContext validateHMACToken(String jwt, String secret, String expectedIssuer) throws InvalidJwtException, UnsupportedEncodingException {
        return new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setRequireSubject()
                .setAllowedClockSkewInSeconds(30)
                .setSkipDefaultAudienceValidation()
                .setRelaxVerificationKeyValidation()
                .setVerificationKey(new HmacKey(secret.getBytes("UTF-8")))
                .setExpectedIssuer(expectedIssuer)
                .build().process(jwt);
    }


    public static JwtContext validateHMACToken(String jwtToken, String secret, String expectedIssuer, String expectedAudience, Boolean skipDefaultValidators) throws InvalidJwtException, UnsupportedEncodingException {
        JwtConsumer jwtConsumer = null;
        // Build a JwtConsumer that doesn't check signatures or do any validation.
        if (skipDefaultValidators) {
            jwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllDefaultValidators()
                    .setDisableRequireSignature()
                    .setSkipSignatureVerification()
                    .build();
            //.setVerificationKey(new HmacKey(secret.getBytes()))
            //.setRelaxVerificationKeyValidation() // allow shorter HMAC keys when used w/ HSxxx algs
        } else {
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

    public static String getJwtWithExpirationTime(JwtClaims jwtClaims, Integer jwtExpirationTime, Key privateKey, String pubKeyEndpoint, String algorithm) {
        //add optional claims
        jwtClaims.setExpirationTimeMinutesInTheFuture(jwtExpirationTime.floatValue() / 60); // time when the token will expire (10 minutes from now)
        jwtClaims.setNotBeforeMinutesInThePast(2);//TODO fix this issue on the gateway
        try {
            return composeJWT(jwtClaims, privateKey, pubKeyEndpoint, algorithm);
        } catch (JoseException | UnsupportedEncodingException ex) {
            throw ExceptionFactory.jwtEncodingException(Messages.i18n.format(ErrorCodes.JWT_ENCODING_ERROR, ex.getMessage()), ex);
        }
    }

    public static String composeJWT(JwtClaims jwtClaims, Key privateKey, String pubKeyEndpoint, String algorithm) throws JoseException, UnsupportedEncodingException {

        // The JWT is signed using the private key
        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        JsonWebSignature jws = new JsonWebSignature();
        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(jwtClaims.toJson());
        jws.setAlgorithmHeaderValue(algorithm);
        jws.setKey(privateKey);
        if (algorithm.equals(JWT_RS256)) {
            jws.setHeader(IJWT.HEADER_X5U, pubKeyEndpoint);
            jws.setHeader(IJWT.HEADER_TYPE, IJWT.HEADER_TYPE_VALUE);
        }

        jws.setDoKeyValidation(false); // relaxes the key length requirement

        String issuedJwt = null;
        issuedJwt = jws.getCompactSerialization();
        return issuedJwt;
    }

    public static String composeJWT(JWTRequestBean jwtRequestBean, Integer jwtExpTime, Key privateKey, String pubKeyEnpoint) throws JoseException, UnsupportedEncodingException {
        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        //add optional claims
        //List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        //claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array
        addOptionalClaims(claims, jwtRequestBean.getOptionalClaims());
        //It's important to set the optional claims before, otherwise fix claims can be overriden
        claims.setIssuer(jwtRequestBean.getIssuer());  // who creates the token and signs it
        claims.setAudience(jwtRequestBean.getAudience()); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(jwtExpTime.floatValue() / 60); // time when the token will expire (60 minutes from now by default)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        //TODO fix this issue on the gateway
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        //Custom fields
        claims.setSubject(jwtRequestBean.getSubject()); // the subject/principal is whom the token is about
        claims.setClaim(IJWT.NAME, jwtRequestBean.getName());//unique username
        claims.setClaim(IJWT.SURNAME, jwtRequestBean.getSurname());
        claims.setClaim(IJWT.GIVEN_NAME, jwtRequestBean.getGivenName());
        return composeJWT(claims, privateKey, pubKeyEnpoint, AlgorithmIdentifiers.RSA_USING_SHA256);
    }

    /**
     * Utility to add optional claims retreived from the JWTRequestBean.
     *
     * @param claims
     * @param optionalClaims
     */
    private static void addOptionalClaims(JwtClaims claims, Map<String, String> optionalClaims) {
        if (optionalClaims != null && optionalClaims.size() > 0) {
            Set<String> claimKeySet = optionalClaims.keySet();
            claimKeySet.stream().forEach(key -> {
                claims.setStringClaim(key, optionalClaims.get(key));
            });
        }
    }

    public static JwtClaims getUnvalidatedClaims(String jwt) throws InvalidJwtException, MalformedClaimException {
        return getUnvalidatedContext(jwt).getJwtClaims();
    }

    private static JwtContext getUnvalidatedContext(String jwt) throws InvalidJwtException, MalformedClaimException {
        return new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build()
                .process(jwt);
    }
}
