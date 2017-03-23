package com.t1t.apim;

import com.google.gson.Gson;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.facades.GatewayFacade;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.mail.MailService;
import com.t1t.kong.model.*;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.GatewayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 05/04/16.
 * The startup service can be extended with application functionality needed for:
 * <ul>
 * <li>migration actions</li>
 * <li>application startup checks</li>
 * <li>gateway configuration</li>
 * </ul>
 */
@Singleton(name = "StartupService")
@DependsOn("AppConfig")
@Startup
public class StartupService {
    private static final Logger _LOG = LoggerFactory.getLogger(StartupService.class.getName());
    @Inject private MailService mailService;
    @Inject private AppConfig config;
    @Inject private GatewayFacade gatewayFacade;
    @Inject private IStorageQuery query;
    @Inject private IStorage storage;


    /**
     * Verify if the necessary apis and consumers necessary for the API Manager are present on the gateway and
     * configured correctly and if not create them
     *
     */
    @PostConstruct
    public void init() {
        try {
            verifyEngineDependencies();
            verifyOrCreateGatewayDependencies();
            sendTestMail();
        }
        catch (Exception ex) {
            _LOG.error(ex.getMessage());
        }
    }

    private void sendTestMail() throws StorageException {
        _LOG.debug("Send test mail");
        mailService.sendTestMail();
    }

    private void verifyEngineDependencies() {
        //TODO - Verify that the engine's datastore is configured correctly and populated with the necessary entries for basic functionality
    }

    private void verifyOrCreateGatewayDependencies() throws StorageException {
        query.getAllGateways().stream().map(gwBean -> gatewayFacade.createGatewayLink(gwBean.getId())).forEach(gw -> {
            try {
                verifyOrCreateApiEngine(gw);
                verifyOrCreateApiEngineAuth(gw);
                verifyOrCreateGatewayKeys(gw);
                verifyOrCreateConsumers(gw);
            }
            catch (StorageException ex){
                throw ExceptionFactory.systemErrorException(ex);
            }
        });
    }

    private void verifyOrCreateApiEngine(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withRequestPath(config.getApiEngineRequestPath())
                .withName(config.getApiEngineName())
                .withStripRequestPath(config.getApiEngineStripRequestPath())
                .withUpstreamUrl(config.getApiEngineUpstream());

        api = verifyApi(gw, api);
        verifyPlugins(gw, api, Arrays.asList(Policies.JWT, Policies.KEYAUTHENTICATION, Policies.CORS, Policies.ANALYTICS));
    }

    private void verifyOrCreateApiEngineAuth(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withRequestPath(config.getApiEngineAuthRequestPath())
                .withName(config.getApiEngineAuthName())
                .withStripRequestPath(config.getApiEngineAuthStripRequestPath())
                .withUpstreamUrl(config.getApiEngineAuthUpstreamUrl());

        api = verifyApi(gw, api);
        verifyPlugins(gw, api, Arrays.asList(Policies.KEYAUTHENTICATION, Policies.CORS, Policies.ANALYTICS));
    }

    private void verifyOrCreateGatewayKeys(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withRequestPath(config.getGatewaykeysRequestPath())
                .withName(config.getGatewaykeysName())
                .withStripRequestPath(config.getGatewaykeysStripRequestPath())
                .withUpstreamUrl(config.getGatewaykeysUpstreamUrl());

        api = verifyApi(gw, api);
        verifyPlugins(gw, api, Arrays.asList(Policies.CORS));
    }

    private KongApi verifyApi(IGatewayLink gw, KongApi api) {
        KongApi existingApi = gw.getApi(api.getName());
        if (existingApi != null)  {
            if (!existingApi.getStripRequestPath().equals(api.getStripRequestPath()) ||
                    !existingApi.getUpstreamUrl().equals(api.getUpstreamUrl()) ||
                    !existingApi.getRequestPath().equals(api.getRequestPath())) {
                existingApi = gw.updateOrCreateApi(existingApi.withStripRequestPath(api.getStripRequestPath()).withRequestPath(api.getRequestPath()).withUpstreamUrl(api.getUpstreamUrl()));
            }
        }
        else {
            existingApi = gw.createApi(api);
        }
        return existingApi;
    }

    private void verifyPlugins(IGatewayLink gw, KongApi api, List<Policies> policies) {
        KongPluginConfigList plugins = gw.getServicePlugins(api.getName());
        if (plugins != null && !plugins.getData().isEmpty()) {
            plugins.getData().forEach(plugin -> {
                Policies pluginDef = GatewayUtils.convertKongPluginNameToPolicy(plugin.getName());
                if (!policies.contains(pluginDef)) {
                    gw.deleteApiPlugin(api.getId(), plugin.getId());
                }
                else {
                    try {
                        plugin = getDefaultConfigs(pluginDef, plugin);
                        gw.updatePlugin(plugin);
                    }
                    catch (StorageException ex) {
                        throw ExceptionFactory.systemErrorException(ex);
                    }
                }
            });
        }
        else if (plugins != null) {
            policies.forEach(polDef -> {
                try {
                    KongPluginConfig plugin = new KongPluginConfig().withName(polDef.getKongIdentifier());
                    plugin = getDefaultConfigs(polDef, plugin);
                    gw.createApiPlugin(api.getId(), plugin);
                }
                catch (StorageException ex) {
                    throw ExceptionFactory.systemErrorException(ex);
                }
            });
        }
    }

    private KongPluginConfig getDefaultConfigs(Policies polDef, KongPluginConfig plugin) throws StorageException {
        switch (polDef) {
            case CORS:
            case KEYAUTHENTICATION:
                plugin.setConfig(new Gson().fromJson(storage.getPolicyDefinition(polDef.getPolicyDefId()).getDefaultConfig(), polDef.getClazz()));
                break;
            case JWT:
                plugin.setConfig(new KongPluginJWT().withClaimsToVerify(Arrays.asList("exp")));
                break;
            case ANALYTICS:
                plugin.setConfig(new KongPluginAnalytics()
                        .withEnvironment(config.getEnvironment())
                        .withServiceToken(config.getAnalyticsServiceToken()));
                break;
            default:
                throw new IllegalArgumentException("Not a valid policy for required api's: " + polDef.toString());
        }
        return plugin;
    }

    private void verifyOrCreateConsumers(IGatewayLink gw) throws StorageException {
        query.getManagedAppForTypes(Arrays.asList(ManagedApplicationTypes.Consent, ManagedApplicationTypes.Publisher, ManagedApplicationTypes.InternalMarketplace, ManagedApplicationTypes.ExternalMarketplace))
                .forEach(mab -> {
            String id = ConsumerConventionUtil.createManagedApplicationConsumerName(mab);
            KongConsumer consumer = gw.getConsumer(id);
            if (consumer == null) {
                consumer = gw.createConsumer(id);
            }
            if (consumer != null) {
                KongPluginKeyAuthResponseList keyAuth = gw.getConsumerKeyAuth(id);
                if (keyAuth != null && keyAuth.getData().isEmpty()) {
                    mab.getApiKeys().forEach(key -> gw.addConsumerKeyAuth(id, key));
                } else if (keyAuth != null) {
                    Set<String> gwKeys = keyAuth.getData().stream().map(KongPluginKeyAuthResponse::getKey).collect(Collectors.toSet());
                    gwKeys.forEach(apikey -> {
                        if (!mab.getApiKeys().contains(apikey)) {
                            gw.deleteConsumerKeyAuth(id, apikey);
                        }
                    });
                    mab.getApiKeys().forEach(apikey -> {
                        if (!gwKeys.contains(apikey)) {
                            gw.addConsumerKeyAuth(id, apikey);
                        }
                    });
                }
            }
            else {
                throw ExceptionFactory.gatewayNotFoundException(gw.getGatewayId());
            }
        });

    }
}
