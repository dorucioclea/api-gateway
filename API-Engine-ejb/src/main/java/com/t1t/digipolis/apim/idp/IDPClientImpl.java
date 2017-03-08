package com.t1t.digipolis.apim.idp;

import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.idp.dto.Realm;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IDPClientImpl implements IDPClient {

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
        if (client.realm(org.getId()) != null) {
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
}