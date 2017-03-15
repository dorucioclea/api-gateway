package com.t1t.digipolis.apim.idp;

import com.t1t.digipolis.apim.beans.idp.IDPBean;
import com.t1t.digipolis.apim.beans.idp.KeystoreBean;
import com.t1t.digipolis.apim.beans.mail.MailProviderBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.util.AesEncrypter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Default
public class IDPLinkFactoryImpl implements IDPLinkFactory {

    private static final Logger log = LoggerFactory.getLogger(IDPLinkFactoryImpl.class);

    @Inject
    private IStorageQuery query;
    @Inject
    private IStorage storage;

    @Override
    public IDPClient getIDPClient(String idpId) {
        try {
            IDPBean idp = storage.getIDP(idpId);
            if (idp == null) {
                throw ExceptionFactory.idpNotFoundException(idpId);
            }
            return getIDPClientInternal(idp);
        } catch (StorageException e) {
            throw ExceptionFactory.systemErrorException(e);
        }
    }

    @Override
    public IDPClient getDefaultIDPClient() {
        try {
            IDPBean idp = query.getDefaultIdp();
            if (idp == null) {
                log.error("There is no IDP defined as default");
                throw ExceptionFactory.idpNotFoundException("Default");
            }
            return getIDPClientInternal(idp);
        } catch (StorageException e) {
            throw ExceptionFactory.systemErrorException(e);
        }
    }

    private IDPClient getIDPClientInternal(IDPBean idp) throws StorageException {
        KeystoreBean kb = query.getDefaultKeystore();
        MailProviderBean mpb = query.getDefaultMailProvider();
        return new IDPClientImpl(createKeycloakClient(idp), idp, kb, mpb);
    }

    private Keycloak createKeycloakClient(IDPBean idp) {
        return KeycloakBuilder.builder()
                .serverUrl(idp.getServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(idp.getMasterRealm())
                .clientId(idp.getClientId())
                .clientSecret(AesEncrypter.decrypt(idp.getEncryptedClientSecret()))
                .build();
    }
}