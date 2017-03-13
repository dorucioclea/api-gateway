package com.t1t.digipolis.apim.idp;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.idp.IDPBean;
import com.t1t.digipolis.apim.beans.idp.KeystoreBean;
import com.t1t.digipolis.apim.beans.mail.MailProviderBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.idp.dto.Realm;
import com.t1t.digipolis.apim.idp.dto.RealmClient;
import com.t1t.digipolis.apim.mail.MailProvider;
import com.t1t.digipolis.util.AesEncrypter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ComponentRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.t1t.digipolis.util.ConsumerConventionUtil.*;

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

    public IDPClientImpl(Keycloak client, IDPBean idp, KeystoreBean defaultKeystore, MailProviderBean defaultMailProvider) {
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
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(org.getId());
            if (mailProvider != null) {
                realm.setSmtpServer(convertMailProviderToMap(mailProvider));
            }
            else {
                realm.setSmtpServer(convertMailProviderToMap(defaultMailProvider));
            }
            client.realms().create(realm);
            if (keystore != null) {
                ComponentRepresentation newKeystore = new ComponentRepresentation();
                newKeystore.setProviderType(IDPConstants.KEYSTORE_PROVIDER_TYPE);
                newKeystore.setProviderId(IDPConstants.KEYSTORE_PROVIDER_ID);
                newKeystore.setParentId(org.getId());
                newKeystore.setName(keystore.getName());
                newKeystore.setConfig();
                client.realm(org.getId()).components().add()
            }
            Realm rval = new Realm();
            rval.setId(org.getId());
            return rval;
        }
        catch (Exception ex) {
            log.error("Error creating realm on IDP");
            throw ExceptionFactory.systemErrorException(ex.getMessage());
        }
    }

    @Override
    public Realm createRealm(OrganizationBean org) {
        return createRealm(org, null, null);
    }

    @Override
    public void deleteRealm(OrganizationBean org) {
        if (realmExists(org.getId())) {
            client.realm(org.getId()).logoutAll();
            client.realm(org.getId()).remove();
        }
    }

    @Override
    public RealmClient createClient(ApplicationVersionBean avb) {
        String clientId = createAppUniqueId(avb);
        String realmName = avb.getApplication().getOrganization().getId();
        if (clientExistsInRealm(clientId, realmName)) {
            throw ExceptionFactory.applicationVersionAlreadyExistsException(avb.getApplication().getName(), avb.getVersion());
        }
        RealmResource realm = client.realm(realmName);
        ClientRepresentation cRep = new ClientRepresentation();
        cRep.setClientId(clientId);
        cRep.setProtocol(OPENID_CONNECT);
        realm.clients().create(cRep);
        RealmClient rval = new RealmClient();
        //Get the ID of the newly created client
        rval.setId(realm.clients().findByClientId(clientId).get(0).getId());
        return rval;
    }

    @Override
    public void deleteClient(ApplicationVersionBean avb) {
        try {
            if (clientExistsInRealm(createAppUniqueId(avb), avb.getApplication().getOrganization().getId())) {
                client.realm(avb.getApplication().getOrganization().getId()).clients().get(avb.getIdpClientId()).remove();
            }
        }
        catch (Exception ex) {
            log.error("Couldn't delete client: {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

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
        rval.put()
        return rval;
    }
}