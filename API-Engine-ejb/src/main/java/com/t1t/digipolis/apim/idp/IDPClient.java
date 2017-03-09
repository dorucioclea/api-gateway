package com.t1t.digipolis.apim.idp;


import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.idp.dto.Realm;
import com.t1t.digipolis.apim.idp.dto.RealmClient;

/**
 * Created by michallispashidis on 15/10/15.
 */
public interface IDPClient {

    Realm createRealm(OrganizationBean org);

    void deleteRealm(OrganizationBean org);

    RealmClient createClient(ApplicationVersionBean avb);

    void deleteClient(ApplicationVersionBean avb);
}
