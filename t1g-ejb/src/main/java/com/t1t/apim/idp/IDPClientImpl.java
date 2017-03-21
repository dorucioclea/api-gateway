package com.t1t.apim.idp;

import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.idp.IDPBean;
import com.t1t.apim.beans.idp.KeystoreBean;
import com.t1t.apim.beans.mail.MailProviderBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.idp.dto.Realm;
import com.t1t.apim.idp.dto.RealmClient;
import com.t1t.util.AesEncrypter;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.CustomCollectors;
import com.t1t.util.KeyUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ComponentRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IDPClientImpl implements IDPClient {

    private static final String OPENID_CONNECT = "openid-connect";

    private final Keycloak client;
    private final IDPBean idp;
    private final KeystoreBean defaultKeystore;
    private final MailProviderBean defaultMailProvider;

    private static final Logger log = LoggerFactory.getLogger(IDPClientImpl.class);

    IDPClientImpl(Keycloak client, IDPBean idp, KeystoreBean defaultKeystore, MailProviderBean defaultMailProvider) {
        if (client == null) {
            throw ExceptionFactory.systemErrorException("No IDP Client provided");
        }
        this.client = client;
        this.idp = idp;
        this.defaultKeystore = defaultKeystore;
        this.defaultMailProvider = defaultMailProvider;
    }

    @Override
    public Realm createRealm(OrganizationBean org, KeystoreBean keystore, MailProviderBean mailProvider) {
        if (realmExists(org.getId())) {
            throw ExceptionFactory.organizationAlreadyExistsException(org.getId());
        }
        try {
            //If no keystore or mailprovider is provided, use the defaults
            MailProviderBean mpb = mailProvider == null ? defaultMailProvider : mailProvider;
            KeystoreBean kb = keystore == null ? defaultKeystore : keystore;

            //Create the realm
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(org.getId());
            realm.setId(org.getId());
            realm.setLoginTheme(idp.getDefaultLoginThemeId());
            realm.setDisplayName(org.getName());
            realm.setSmtpServer(convertMailProviderToMap(mpb));
            realm.setEnabled(true);
            client.realms().create(realm);

            //Create the keystore
            ComponentRepresentation newKeystore = new ComponentRepresentation();
            newKeystore.setProviderType(IDPConstants.KEYSTORE_PROVIDER_TYPE);
            newKeystore.setProviderId(IDPConstants.KEYSTORE_PROVIDER_ID);
            newKeystore.setParentId(org.getId());
            newKeystore.setName(kb.getName());
            newKeystore.setConfig(convertKeystoreToMap(kb));
            client.realm(org.getId()).components().add(newKeystore);

            //Verify that the keystore was created
            if (client.realm(org.getId()).components().query(org.getId(), IDPConstants.KEYSTORE_PROVIDER_TYPE, kb.getName()).isEmpty()) {
                throw ExceptionFactory.actionException("Couldn't create keystore for realm");
            }

            Realm rval = new Realm();
            rval.setId(org.getId());
            return rval;
        }
        catch (Exception ex) {
            log.error("Error creating realm on IDP");
            //Attempt to delete the realm
            deleteRealm(org);
            throw ExceptionFactory.actionException("Couldn't create realm on IDP", ex);
        }
    }

    @Override
    public Realm createRealm(OrganizationBean org) {
        return createRealm(org, null, null);
    }

    @Override
    public void deleteRealm(OrganizationBean org) {
        try {
            if (realmExists(org.getId())) {
                client.realm(org.getId()).logoutAll();
                client.realm(org.getId()).remove();
            }
        }
        catch (Exception ex) {
            log.error("Couldn't delete realm: {}", ex.getMessage());
            throw ExceptionFactory.actionException("Couldn't delete realm.", ex);
        }
    }

    @Override
    public RealmClient createClient(ApplicationVersionBean avb) {
        try {
            String clientId = ConsumerConventionUtil.createAppUniqueId(avb);
            String realmName = avb.getApplication().getOrganization().getId();
            if (clientExistsInRealm(clientId, realmName)) {
                throw ExceptionFactory.applicationVersionAlreadyExistsException(avb.getApplication().getName(), avb.getVersion());
            }
            RealmResource realm = client.realm(realmName);

            ClientRepresentation cRep = getDefaultClientRepresentation();

            cRep.setClientId(clientId);
            cRep.setRedirectUris(new ArrayList<>(avb.getOauthClientRedirects()));

            realm.clients().create(cRep);
            RealmClient rval = new RealmClient();
            //Get the ID of the newly created client
            String keycloakId = realm.clients().findByClientId(clientId).get(0).getId();
            rval.setSecret(realm.clients().get(keycloakId).getSecret().getValue());
            rval.setRedirectUris(cRep.getRedirectUris());
            rval.setId(keycloakId);
            return rval;
        }
        catch (Exception ex) {
            log.error("Error creating client on IDP");
            deleteClient(avb);
            throw ExceptionFactory.actionException("Couldn't create client on IDP", ex);
        }
    }

    @Override
    public void deleteClient(ApplicationVersionBean avb) {
        try {
            if (clientExistsInRealm(ConsumerConventionUtil.createAppUniqueId(avb), avb.getApplication().getOrganization().getId())) {
                client.realm(avb.getApplication().getOrganization().getId()).clients().get(avb.getIdpClientId()).remove();
            }
        }
        catch (Exception ex) {
            log.error("Couldn't delete client: {}", ex.getMessage());
            throw ExceptionFactory.actionException("Couldn't delete client.", ex);
        }
    }

    @Override
    public String getRealmPublicKeyInPemFormat(OrganizationBean org) {
        String rval = null;
        if (realmExists(org.getId())) {
            String pem = client.realm(org.getId()).keys().getKeyMetadata().getKeys().stream()
                    //Filter the keys until you get either the custom org keystore's kid, or the default one's
                    .filter(key -> key.getKid().equals(org.getKeystoreKid() == null ? defaultKeystore.getKid() : org.getKeystoreKid()))
                    .collect(CustomCollectors.getSingleResult())
                    .getPublicKey();
            try {
                rval =  KeyUtils.convertPubKeyToPEM(pem);
            } catch (IOException e) {
                log.error("Couldn't convert to PEM: {}", e.getMessage());
            }
        }
        return rval;
    }

    /****** Utility Methods ******/

    private boolean realmExists(String realmName) {
        return client.realms().findAll().stream().map(RealmRepresentation::getRealm).collect(Collectors.toList()).contains(realmName);
    }

    private boolean clientExistsInRealm(String clientId, String realmName) {
        return realmExists(realmName) && client.realm(realmName).clients().findByClientId(clientId).stream().map(ClientRepresentation::getClientId).collect(Collectors.toList()).contains(clientId);
    }

    private Map<String, String> convertMailProviderToMap(MailProviderBean mpb) {
        Map<String , String> rval = new HashMap<>();
        rval.put(IDPConstants.MAIL_HOST, mpb.getHost());
        rval.put(IDPConstants.MAIL_PORT, mpb.getPort().toString());
        rval.put(IDPConstants.MAIL_AUTH, String.valueOf(mpb.isAuth()));
        rval.put(IDPConstants.MAIL_FROM, mpb.getFrom());
        rval.put(IDPConstants.MAIL_USER, mpb.getUser());
        rval.put(IDPConstants.MAIL_PASSWORD, AesEncrypter.decrypt(mpb.getEncryptedPassword()));
        return rval;
    }

    private MultivaluedHashMap<String, String> convertKeystoreToMap(KeystoreBean keystore) {
        MultivaluedHashMap<String, String> rval = new MultivaluedHashMap<>();
        rval.put(IDPConstants.KEYSTORE_ACTIVE, Collections.singletonList(String.valueOf(true)));
        rval.put(IDPConstants.KEYSTORE_ENABLED, Collections.singletonList(String.valueOf(true)));
        rval.put(IDPConstants.KEYSTORE_PATH, Collections.singletonList(keystore.getPath()));
        rval.put(IDPConstants.KEYSTORE_ALIAS, Collections.singletonList(keystore.getPrivateKeyAlias()));
        rval.put(IDPConstants.KEYSTORE_PASSWORD, Collections.singletonList(AesEncrypter.decrypt(keystore.getEncryptedKeystorePassword())));
        rval.put(IDPConstants.KEYSTORE_KEY_PASSWORD, Collections.singletonList(AesEncrypter.decrypt(keystore.getEncryptedKeyPassword())));
        rval.put(IDPConstants.KEYSTORE_PRIORITY, Collections.singletonList(keystore.getPriority().toString()));
        return rval;
    }

    private ClientRepresentation getDefaultClientRepresentation() {
        ClientRepresentation rval;
        List<ClientRepresentation> response = client.realm(idp.getMasterRealm()).clients().findByClientId(idp.getDefaultClient());
        if (response.isEmpty()) {
            rval = new ClientRepresentation();
            rval.setProtocol(OPENID_CONNECT);
            rval.setServiceAccountsEnabled(true);
            rval.setEnabled(false);
            rval.setClientId(idp.getDefaultClient());
            rval.setPublicClient(false);
            rval.setBearerOnly(false);
            rval.setConsentRequired(true);
            rval.setName(idp.getDefaultClient());
            rval.setRootUrl("http://localhost");
            rval.setAuthorizationServicesEnabled(true);
            //Set the authorization attributes
            Map<String, String> attributes = new HashMap<>();
            attributes.put(IDPConstants.CLIENT_REQUEST_OBJECT_SIGNATURE_ALGORITHM, IDPConstants.CLIENT_SIGNING_ALGORITHM_RS256);
            attributes.put(IDPConstants.CLIENT_USER_INFO_RESPONSE_SIGNATURE_ALGORITHM, IDPConstants.CLIENT_SIGNING_ALGORITHM_RS256);
            rval.setAttributes(attributes);
            client.realm(idp.getMasterRealm()).clients().create(rval);
        }
        else {
            rval = response.get(0);
        }
        //Scrub the default client of irrelevant info
        rval.setId(null);
        rval.setClientId(null);
        rval.setSecret(null);
        rval.setEnabled(true);
        rval.getProtocolMappers().forEach(mapper -> mapper.setId(null));
        return rval;
    }
}