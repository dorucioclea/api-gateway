package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.services.AvailabilityBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.HashMap;
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
    @Inject private AppConfig config;
    @Inject private GatewayFacade gatewayFacade;
    @Inject private MigrationFacade migrationFacade;

    public Map<String, AvailabilityBean> getAvailableMarketplaces() throws StorageException {
        final List<ManagedApplicationBean> managedApplicationList = query.listAvailableMarkets();
        Map<String,AvailabilityBean> result = new HashMap<>();
        for(ManagedApplicationBean mb:managedApplicationList){
            result.put(mb.getPrefix(),new AvailabilityBean(mb.getPrefix(),mb.getName()));
        }
        return result;
    }

    public List<WhitelistBean> getWhitelistRecords()throws StorageException{
        return query.listWhitelistRecords();
    }

    public List<BlacklistBean> getBlacklistRecords()throws StorageException{
        return query.listBlacklistRecords();
    }

    public SystemStatusBean getStatus() throws StorageException, GatewayAuthenticationException {
        SystemStatusBean rval = new SystemStatusBean();
        rval.setId("apim-manager-api"); //$NON-NLS-1$
        rval.setName("API Manager REST API"); //$NON-NLS-1$
        rval.setDescription("The API Manager REST API is used by the API Manager UI to get stuff done.  You can use it to automate any api task you wish.  For example, create new Organizations, Plans, Applications, and Services."); //$NON-NLS-1$
        rval.setMoreInfo("http://www.trust1team.com"); //$NON-NLS-1$
        rval.setEnvironment(config.getEnvironment());
        rval.setBuiltOn(config.getBuildDate());
        rval.setVersion(config.getVersion());
        rval.setUp(storage != null);
        rval.setKongInfo("");
        rval.setKongCluster("");
        rval.setKongStatus("");
        return rval;
    }

    public SystemStatusBean getAdminStatus() throws StorageException, GatewayAuthenticationException {
        SystemStatusBean rval = new SystemStatusBean();
        rval.setId("apim-manager-api"); //$NON-NLS-1$
        rval.setName("API Manager REST API"); //$NON-NLS-1$
        rval.setDescription("The API Manager REST API is used by the API Manager UI to get stuff done.  You can use it to automate any api task you wish.  For example, create new Organizations, Plans, Applications, and Services."); //$NON-NLS-1$
        rval.setMoreInfo("http://www.trust1team.com"); //$NON-NLS-1$
        rval.setEnvironment(config.getEnvironment());
        rval.setBuiltOn(config.getBuildDate());
        rval.setVersion(config.getVersion());
        rval.setUp(storage != null);
        //get Kong info
        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
        SystemStatus status = gateway.getStatus();
        rval.setKongInfo(status.getInfo());
        rval.setKongCluster(status.getCluster());
        rval.setKongStatus(status.getStatus());
        return rval;
    }
}
