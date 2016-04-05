package com.t1t.digipolis.apim;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.kong.model.KongApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
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
@Default
public class StartupService {
    private static final Logger _LOG = LoggerFactory.getLogger(StartupService.class.getName());
    private static final String DUMMY_UPSTREAM_URI = "http://localhost:3000";
    private static final String GTW_SERVICE_PREFIX = "org";
    private static final String GTW_SERVICE_VERSION = "v1";
    @Inject
    private IStorageQuery storageQuery;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;

    /**
     * Verify if the available gateways have OAuth - centralized authorization - endpoints available
     *
     * @throws StorageException
     */
    public void initOAuthOnGateways() throws StorageException {
        _LOG.info("start init");
        List<GatewayBean> gatewayBeans = storageQuery.listGatewayBeans();
        gatewayBeans.forEach(gtw -> {
            IGatewayLink iGatewayLink = gatewayLinkFactory.create(gtw);
            if (!oauthEndpointExists(iGatewayLink, gtw)) {
                initGatewayOauthEndpoint(iGatewayLink, gtw);
            }//else nothing to do
        });
    }

    /**
     * Initializes the central authorization endpoints on the gateway.
     * The unique identifier for kong gateway is used as api-name for the dummy service that will be created.
     *
     * @param gtw
     */
    private void initGatewayOauthEndpoint(IGatewayLink gatewayLink, GatewayBean gtw) {
        Service gtwApi = new Service();
        gtwApi.setBasepath(gtw.getOauthContext());
        gtwApi.setEndpoint(DUMMY_UPSTREAM_URI);
        gtwApi.setOrganizationId(GTW_SERVICE_PREFIX);//version will not change, but provided conventionally
        gtwApi.setServiceId(gtw.getId().toLowerCase());
        gtwApi.setVersion(GTW_SERVICE_VERSION);
        gtwApi.setPublicService(true);
    }

    /**
     * Verifies if oauth endpoints are registered on Kong.
     * The endpoints are defined in the context property of each kong instance.
     *
     * @param gtw
     * @return
     */
    private boolean oauthEndpointExists(IGatewayLink gatewayLink, GatewayBean gtw) {
        KongApi gtwApi = gatewayLink.getApi(gtw.getId());
        if (gtwApi != null && gtwApi.getId() != null) return true;
        else return false;
    }
}
