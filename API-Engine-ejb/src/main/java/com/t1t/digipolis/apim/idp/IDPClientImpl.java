package com.t1t.digipolis.apim.idp;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.idp.dto.Realm;
import com.t1t.digipolis.apim.idp.dto.RealmClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static com.t1t.digipolis.util.ConsumerConventionUtil.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IDPClientImpl implements IDPClient {

    private static final String OPENID_CONNECT = "openid-connect";

    private final Keycloak client;

    private static final Logger log = LoggerFactory.getLogger(IDPClientImpl.class);

    public IDPClientImpl(Keycloak client) {
        if (client == null) {
            throw ExceptionFactory.systemErrorException("No IDP Client provided");
        }
        this.client = client;
    }

    @Override
    public Realm createRealm(OrganizationBean org) {
        if (realmExists(org.getId())) {
            throw ExceptionFactory.organizationAlreadyExistsException(org.getId());
        }
        try {
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(org.getId());
            client.realms().create(realm);
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
    public void deleteRealm(OrganizationBean org) {
        if (realmExists(org.getId())) {
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
}