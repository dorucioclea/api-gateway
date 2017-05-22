package com.t1t.apim.idp;

import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.idp.IDPBean;
import com.t1t.apim.beans.idp.KeystoreBean;
import com.t1t.apim.beans.mail.MailProviderBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.core.i18n.Messages;
import com.t1t.apim.exceptions.ApplicationAlreadyExistsException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.idp.dto.Realm;
import com.t1t.apim.idp.dto.RealmClient;
import com.t1t.util.AesEncrypter;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.CustomCollectors;
import com.t1t.util.KeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
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

    private static final String RSA = "RSA";

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
        return createRealm(org.getId(), org.getName(), keystore, mailProvider);
    }

    @Override
    public Realm createRealm(OrganizationBean org) {
        return createRealm(org, null, null);
    }

    @Override
    public void deleteRealm(OrganizationBean org) {
        deleteRealm(org.getId());
    }

    @Override
    public RealmClient createClient(ApplicationVersionBean avb) {
        try {
            String clientId = ConsumerConventionUtil.createAppUniqueId(avb);
            String realmName = avb.getApplication().getOrganization().getId();
            return createClient(realmName, clientId, avb.getApplication().getName(), avb.getOauthClientSecret(), new ArrayList<>(avb.getOauthClientRedirects()));
        }
        catch (ApplicationAlreadyExistsException ex) {
            throw ExceptionFactory.applicationVersionAlreadyExistsException(avb.getApplication().getName(), avb.getVersion());
        }
        catch (Exception ex) {
            log.error("Error creating client on IDP", ex);
            deleteClient(avb);
            throw ExceptionFactory.actionException("Couldn't create client on IDP", ex);
        }
    }

    public RealmClient createClient(String realmId, String clientId, String clientName, String clientSecret, List<String> redirectUris) {
        try {
            if (clientExistsInRealm(clientId, realmId)) {
                throw ExceptionFactory.applicationVersionAlreadyExistsException(clientId, realmId);
            }
            RealmResource realm = client.realm(realmId);

            ClientRepresentation cRep = getDefaultClientRepresentation();

            cRep.setClientId(clientId);
            cRep.setName(clientName);
            cRep.setRedirectUris(redirectUris);
            if (StringUtils.isNotEmpty(clientSecret)) cRep.setSecret(clientSecret);

            realm.clients().create(cRep);
            RealmClient rval = new RealmClient();
            //Get the ID of the newly created client
            String keycloakId = realm.clients().findByClientId(clientId).get(0).getId();
            rval.setSecret(realm.clients().get(keycloakId).getSecret().getValue());
            rval.setRedirectUris(cRep.getRedirectUris());
            rval.setId(keycloakId);
            return rval;
        }
        catch (ApplicationAlreadyExistsException ex) {
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error creating client on IDP", ex);
            try {
                client.realm(realmId).clients().get(clientId).remove();
            }
            catch (Exception e) {
                //Do nothing, we tried our best
            }
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
        return getRealmPublicKeyInPemFormat(org.getId(), org.getKeystoreKid());
    }

    @Override
    public String getDefaultPublicKeyInPemFormat() {
        return getRealmPublicKeyInPemFormat(idp.getDefaultRealm(), defaultKeystore.getKid());
    }

    @Override
    public String getRealmPublicKeyInPemFormat(String realmId, String keystoreKid) {
        String rval = null;
        if (realmExists(realmId)) {
            String pem = client.realm(realmId).keys().getKeyMetadata().getKeys().stream()
                    //Filter the keys until you get either the custom org keystore's kid, or the default one's
                    .filter(key -> key.getKid().equals(keystoreKid == null ? defaultKeystore.getKid() : keystoreKid))
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

    @Override
    public boolean realmExists(String realmName) {
        return client.realms().findAll().stream().map(RealmRepresentation::getRealm).collect(Collectors.toList()).contains(realmName);
    }

    @Override
    public boolean realmKeystoreExists(String realmId, String keystoreKid) {
        if (realmExists(realmId)) {
            Map<String, String> activeKeys = client
                    .realm(realmId)
                    .keys()
                    .getKeyMetadata()
                    .getActive();
            return (activeKeys.containsKey(RSA) && activeKeys.get(RSA).equals(keystoreKid));
        }
        return false;
    }

    @Override
    public void setRealmKeystore(String realmId, KeystoreBean keystore) {
        if (realmExists(realmId)) {
            createKeystoreInternal(realmId, keystore);
        }
    }

    @Override
    public void setRealmMailProvider(String realmId, MailProviderBean mailProvider) {
        if (realmExists(realmId)) {
            RealmRepresentation realm = client.realm(realmId).toRepresentation();
            realm.setSmtpServer(convertMailProviderToMap(mailProvider));
            client.realm(realmId).update(realm);
        }
    }

    @Override
    public String regenerateClientSecret(String realmId, String idpClientId) {
        if (realmExists(realmId)) {
            ClientResource cRes = client.realm(realmId).clients().get(idpClientId);
            if (cRes != null && cRes.toRepresentation()!= null) {
                return cRes.generateNewSecret().getValue();
            }
            else {
                throw ExceptionFactory.actionException(Messages.i18n.format("idpClientNotFound", idpClientId));
            }
        }
        else throw ExceptionFactory.actionException(Messages.i18n.format("realmNotFound", realmId));
    }

    @Override
    public RealmClient syncClient(ApplicationVersionBean avb) {
        RealmClient rval = new RealmClient();
        ClientsResource realmClients = client.realm(avb.getApplication().getOrganization().getId()).clients();
        if (clientExistsInRealm(avb.getApplication().getId(), avb.getApplication().getOrganization().getId())) {
            ClientResource cRes = realmClients.get(avb.getIdpClientId());
            ClientRepresentation cRep;
            if (cRes != null) {
                cRep = cRes.toRepresentation();
            }
            else {
                cRep = realmClients.findByClientId(avb.getApplication().getId()).get(0);
            }
            if (StringUtils.isNotEmpty(avb.getOauthClientSecret())) cRep.setSecret(avb.getJwtSecret());
            cRep.setRedirectUris(new ArrayList<>(avb.getOauthClientRedirects()));
            cRes.update(cRep);
            rval.setId(cRep.getId());
            rval.setSecret(cRep.getSecret());
            rval.setRedirectUris(cRep.getRedirectUris());
        }
        else {
            rval = createClient(avb);
        }
        return rval;
    }

    @Override
    public boolean realmMailProviderExists(String realmId, MailProviderBean mailProvider) {
        boolean equals;
        if (equals = realmExists(realmId)) {
            RealmRepresentation realm = client.realm(realmId).toRepresentation();
            Map<String, String> mailMap = convertMailProviderToMap(mailProvider);
            Map<String, String> idpMailMap = realm.getSmtpServer();
            for (Map.Entry<String, String> entry : mailMap.entrySet()) {
                equals = idpMailMap.containsKey(entry.getKey()) && idpMailMap.get(entry.getKey()).equals(entry.getValue());
            }
        }
        return equals;
    }

    @Override
    public Realm createRealm(String realmId, String realmName, KeystoreBean keystore, MailProviderBean mailProvider) {
        if (realmExists(realmId)) {
            throw ExceptionFactory.organizationAlreadyExistsException(realmId);
        }
        try {
            //If no keystore or mailprovider is provided, use the defaults
            MailProviderBean mpb = mailProvider == null ? defaultMailProvider : mailProvider;
            KeystoreBean kb = keystore == null ? defaultKeystore : keystore;

            //Create the realm
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(realmId);
            realm.setId(realmId);
            realm.setLoginTheme(idp.getDefaultLoginThemeId());
            realm.setDisplayName(realmName);
            realm.setSmtpServer(convertMailProviderToMap(mpb));
            realm.setEnabled(true);
            client.realms().create(realm);

            //Create the keystore
            createKeystoreInternal(realmId, keystore);

            //Verify that the keystore was created
            if (client.realm(realmId).components().query(realmId, IDPConstants.KEYSTORE_PROVIDER_TYPE, kb.getName()).isEmpty()) {
                throw ExceptionFactory.actionException("Couldn't create keystore for realm");
            }

            Realm rval = new Realm();
            rval.setId(realmId);
            return rval;
        }
        catch (Exception ex) {
            log.error("Error creating realm on IDP");
            //Attempt to delete the realm
            deleteRealm(realmId);
            throw ExceptionFactory.actionException("Couldn't create realm on IDP", ex);
        }
    }

    @Override
    public boolean clientExistsInRealm(String clientId, String realmName) {
        return realmExists(realmName) && client.realm(realmName).clients().findByClientId(clientId).stream().map(ClientRepresentation::getClientId).collect(Collectors.toList()).contains(clientId);
    }

    /****** Utility Methods ******/

    private void createKeystoreInternal(String realmId, KeystoreBean keystore) {
        ComponentRepresentation newKeystore = new ComponentRepresentation();
        newKeystore.setProviderType(IDPConstants.KEYSTORE_PROVIDER_TYPE);
        newKeystore.setProviderId(IDPConstants.KEYSTORE_PROVIDER_ID);
        newKeystore.setParentId(realmId);
        newKeystore.setName(keystore.getName());
        newKeystore.setConfig(convertKeystoreToMap(keystore));
        client.realm(realmId).components().add(newKeystore);
    }

    private void deleteRealm(String realmId) {
        try {
            if (realmExists(realmId)) {
                client.realm(realmId).logoutAll();
                client.realm(realmId).remove();
            }
        }
        catch (Exception ex) {
            log.error("Couldn't delete realm: {}", ex.getMessage());
            throw ExceptionFactory.actionException("Couldn't delete realm.", ex);
        }
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
        RealmResource defaultRealm = getDefaultRealm();
        List<ClientRepresentation> response = defaultRealm.clients().findByClientId(idp.getDefaultClient());
        if (response.isEmpty()) {
            rval = new ClientRepresentation();
            rval.setProtocol(IDPConstants.OPENID_CONNECT);
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
            defaultRealm.clients().create(rval);
            response = defaultRealm.clients().findByClientId(idp.getDefaultClient());
        }
        rval = response.get(0);
        //Scrub the default client of irrelevant info
        rval.setId(null);
        rval.setName(null);
        rval.setClientId(null);
        rval.setSecret(null);
        rval.setEnabled(true);
        rval.getProtocolMappers().forEach(mapper -> mapper.setId(null));
        return rval;
    }

    private RealmResource getDefaultRealm() {
        if (!realmExists(idp.getDefaultRealm())) {
            createRealm(idp.getDefaultRealm(), idp.getDefaultRealm(), defaultKeystore, defaultMailProvider);
        }
        return client.realm(idp.getDefaultRealm());
    }
}