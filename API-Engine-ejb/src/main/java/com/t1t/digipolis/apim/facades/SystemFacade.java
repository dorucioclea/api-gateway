package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.availability.AvailabilityBean;
import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by michallispashidis on 12/02/16.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SystemFacade {
    private static Logger _LOG = LoggerFactory.getLogger(SystemFacade.class.getName());
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext appContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;

    public Map<String, AvailabilityBean> getAvailableMarketplaces() throws StorageException {
        return query.listAvailableMarkets();
    }

    public List<WhitelistBean> getWhitelistRecords()throws StorageException{
        return query.listWhitelistRecords();
    }

    public List<BlacklistBean> getBlacklistRecords()throws StorageException{
        return query.listBlacklistRecords();
    }
}
