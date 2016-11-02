package com.t1t.digipolis.apim;

import com.t1t.digipolis.apim.beans.gateways.Gateway;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.mail.MailService;
import com.t1t.digipolis.kong.model.KongApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by michallispashidis on 05/04/16.
 * The startup service can be extended with application functionality needed for:
 * <ul>
 * <li>migration actions</li>
 * <li>application startup checks</li>
 * <li>gateway configuration</li>
 * </ul>
 */
@ApplicationScoped
@DependsOn(value="AppConfig")
public class StartupService {
    private static final Logger _LOG = LoggerFactory.getLogger(StartupService.class.getName());
    @Inject private MailService mailService;

    /**
     * Verify if the available gateways have OAuth - centralized authorization - endpoints available
     *
     * @throws StorageException
     */
    @PostConstruct
    public void initOAuthOnGateways() {
        try{
            _LOG.debug("Send test mail");
            mailService.sendTestMail();
        }catch (StorageException ex){
            _LOG.error(ex.getMessage());
        }
    }
}
