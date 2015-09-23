package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.qualifier.APIEngineContext;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 23/09/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OAuthFacade {
    @Inject @APIEngineContext private Logger log;
}
