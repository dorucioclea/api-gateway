package com.t1t.digipolis.apim;

import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 05/04/16.
 * The startup service can be extended with application functionality needed for:
 * <ul>
 *     <li>migration actions</li>
 *     <li>application startup checks</li>
 *     <li>gateway configuration</li>
 * </ul>
 */
@ApplicationScoped
@Default
public class StartupService {
    private Logger _LOG = LoggerFactory.getLogger(StartupService.class.getName());
    @Inject private IGatewayLinkFactory gatewayLinkFactory;
    /**
     * Verify if the available gateways have OAuth - centralized authorization - endpoints available
     * @throws StorageException
     */
    public void initOAuthOnGateways()throws StorageException{
        _LOG.info("start init");
    }
}
