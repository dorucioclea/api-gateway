package com.t1t.digipolis.apim.idp;


import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.idp.dto.Realm;

/**
 * Created by michallispashidis on 15/10/15.
 */
public interface IDPClient {

    Realm createRealm(OrganizationBean org);
}
