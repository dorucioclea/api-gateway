package com.t1t.apim.idp;


import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.idp.KeystoreBean;
import com.t1t.apim.beans.mail.MailProviderBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.idp.dto.Realm;
import com.t1t.apim.idp.dto.RealmClient;

import java.util.List;

/**
 * Created by michallispashidis on 15/10/15.
 */
public interface IDPClient {

    Realm createRealm(String realmId, String realmName, KeystoreBean keystore, MailProviderBean mailProvider);

    Realm createRealm(OrganizationBean org, KeystoreBean keystore, MailProviderBean mailProvider);

    Realm createRealm(OrganizationBean org);

    void deleteRealm(OrganizationBean org);

    RealmClient createClient(ApplicationVersionBean avb);

    void deleteClient(ApplicationVersionBean avb);

    String getRealmPublicKeyInPemFormat(OrganizationBean org);

    String getRealmPublicKeyInPemFormat(String realmId, String keystoreKid);

    String getDefaultPublicKeyInPemFormat();

    boolean realmExists(String realmName);

    boolean realmKeystoreExists(String realmId, String keystoreKid);

    boolean realmMailProviderExists(String realmId, MailProviderBean mailProvider);

    boolean clientExistsInRealm(String clientId, String realmName);

    void setRealmKeystore(String realmId, KeystoreBean keystore);

    void setRealmMailProvider(String realmId, MailProviderBean mailProvider);

    RealmClient syncClient(ApplicationVersionBean avb);

    String regenerateClientSecret(String realmId, String idpClientId);

    RealmClient createClient(String defaultRealm, String idpClient, String clientName, String clientSecret, List<String> redirectUris);
}
