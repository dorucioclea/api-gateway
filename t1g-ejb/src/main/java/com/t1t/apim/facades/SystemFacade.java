package com.t1t.apim.facades;

import com.t1t.apim.AppConfigBean;
import com.t1t.apim.T1G;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.services.AvailabilityBean;
import com.t1t.apim.beans.system.SystemStatusBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.gateway.GatewayAuthenticationException;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.gateway.dto.SystemStatus;
import com.t1t.apim.maintenance.MaintenanceController;

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
    private static final String DESCRIPTION = "The API Manager REST API is used by the API Manager UI to get stuff done.  You can use it to automate any api task you wish.  For example, create new Organizations, Plans, Applications, and Services.";
    private static final String ID = "apim-manager-api";
    private static final String MORE_INFO = "http://www.trust1team.com";
    private static final String NAME = "API Manager REST API";

    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    @T1G
    private AppConfigBean config;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private MaintenanceController maintenance;

    public Map<String, AvailabilityBean> getAvailableMarketplaces() throws StorageException {
        final List<ManagedApplicationBean> managedApplicationList = query.listAvailableMarkets();
        Map<String, AvailabilityBean> result = new HashMap<>();
        for (ManagedApplicationBean mb : managedApplicationList) {
            result.put(mb.getPrefix(), new AvailabilityBean(mb.getPrefix(), mb.getName()));
        }
        return result;
    }

    public SystemStatusBean getStatus() throws StorageException, GatewayAuthenticationException {
        SystemStatusBean rval = new SystemStatusBean();
        rval.setId(ID); //$NON-NLS-1$
        rval.setName(NAME); //$NON-NLS-1$
        rval.setDescription(DESCRIPTION); //$NON-NLS-1$
        rval.setMoreInfo(MORE_INFO); //$NON-NLS-1$
        rval.setEnvironment(config.getEnvironment());
        rval.setBuiltOn(config.getBuildDate());
        rval.setVersion(config.getVersion());
        rval.setUp(storage != null);
        rval.setMaintenanceModeEnabled(maintenance.isEnabled());
        if (rval.getMaintenanceModeEnabled()) {
            rval.setMaintenanceMessage(maintenance.getMessage());
        }
        return rval;
    }

    public SystemStatusBean getAdminStatus() throws StorageException, GatewayAuthenticationException {
        SystemStatusBean rval = getStatus();
        //get Kong info
        IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
        SystemStatus status = gateway.getStatus();
        rval.setKongInfo(status.getInfo());
        rval.setKongCluster(status.getCluster());
        rval.setKongStatus(status.getStatus());
        return rval;
    }
}
