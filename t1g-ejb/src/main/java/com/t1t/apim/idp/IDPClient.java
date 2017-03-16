package com.t1t.apim.idp;


import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.idp.KeystoreBean;
import com.t1t.apim.beans.mail.MailProviderBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.idp.dto.Realm;
import com.t1t.apim.idp.dto.RealmClient;

/**
 * Created by michallispashidis on 15/10/15.
 */
public interface IDPClient {

    Realm createRealm(OrganizationBean org, KeystoreBean keystore, MailProviderBean mailProvider);

    Realm createRealm(OrganizationBean org);

    void deleteRealm(OrganizationBean org);

    RealmClient createClient(ApplicationVersionBean avb);

    void deleteClient(ApplicationVersionBean avb);

    String getRealmPublicKeyInPemFormat(OrganizationBean org);
}
