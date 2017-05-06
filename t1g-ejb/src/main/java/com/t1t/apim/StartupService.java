package com.t1t.apim;

import com.google.gson.Gson;
import com.t1t.apim.beans.jwt.IJWT;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.facades.GatewayFacade;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.idp.IDPLinkFactory;
import com.t1t.apim.mail.MailService;
import com.t1t.kong.model.*;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.GatewayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.*;
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
    @Inject private IDPLinkFactory idpLinkFactory;


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
            ex.printStackTrace();
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
                verifyOrCreateClusterInfo(gw);
                verifyOrCreateConsumers(gw);
            }
            catch (StorageException ex){
                throw ExceptionFactory.systemErrorException(ex);
            }
        });
    }

    private void verifyOrCreateApiEngine(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withUris(Collections.singletonList(config.getApiEngineRequestPath()))
                .withName(config.getApiEngineName())
                .withStripUri(config.getApiEngineStripRequestPath())
                .withUpstreamUrl(config.getApiEngineUpstream());

        api = verifyApi(gw, api);
        List<Policies> policies = new ArrayList<>();
        policies.add(Policies.CORS);
        policies.add(Policies.JWT);
        policies.add(Policies.KEYAUTHENTICATION);
        policies.add(Policies.DATADOG);
        verifyPlugins(gw, api, policies);
    }

    private void verifyOrCreateApiEngineAuth(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withUris(Collections.singletonList(config.getApiEngineAuthRequestPath()))
                .withName(config.getApiEngineAuthName())
                .withStripUri(config.getApiEngineAuthStripRequestPath())
                .withUpstreamUrl(config.getApiEngineAuthUpstreamUrl());

        api = verifyApi(gw, api);
        List<Policies> policies = new ArrayList<>();
        policies.add(Policies.CORS);
        policies.add(Policies.KEYAUTHENTICATION);
        policies.add(Policies.DATADOG);
        verifyPlugins(gw, api, policies);
    }

    private void verifyOrCreateGatewayKeys(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withUris(Collections.singletonList(config.getGatewaykeysRequestPath()))
                .withName(config.getGatewaykeysName())
                .withStripUri(config.getGatewaykeysStripRequestPath())
                .withUpstreamUrl(config.getGatewaykeysUpstreamUrl());

        api = verifyApi(gw, api);
        List<Policies> policies = new ArrayList<>();
        policies.add(Policies.CORS);
        verifyPlugins(gw, api, policies);
    }

    private void verifyOrCreateClusterInfo(IGatewayLink gw) {
        KongApi api = new KongApi()
                .withUris(Collections.singletonList(config.getClusterInfoRequestPath()))
                .withName(config.getClusterInfoName())
                .withStripUri(config.getClusterInfoStripRequestPath())
                .withUpstreamUrl(config.getClusterInfoUpstreamUrl());

        api = verifyApi(gw, api);
        List<Policies> policies = new ArrayList<>();
        policies.add(Policies.CORS);
        verifyPlugins(gw, api, policies);
    }

    private KongApi verifyApi(IGatewayLink gw, KongApi api) {
        KongApi existingApi = gw.getApi(api.getName());
        if (existingApi != null)  {
            if (!existingApi.getStripUri().equals(api.getStripUri()) ||
                    !existingApi.getUpstreamUrl().equals(api.getUpstreamUrl()) ||
                    !existingApi.getUris().equals(api.getUris())) {
                existingApi = gw.updateOrCreateApi(existingApi.withStripUri(api.getStripUri()).withUris(api.getUris()).withUpstreamUrl(api.getUpstreamUrl()));
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
            List<Policies> policiesOnGateway = new ArrayList<>();
            plugins.getData().forEach(plugin -> {
                Policies pluginDef = GatewayUtils.convertKongPluginNameToPolicy(plugin.getName());
                if (!policies.contains(pluginDef)) {
                    gw.deleteApiPlugin(api.getId(), plugin.getId());
                }
                else {
                    try {
                        plugin = getDefaultConfigs(pluginDef, plugin);
                        gw.updatePlugin(plugin);
                        policiesOnGateway.add(pluginDef);
                    }
                    catch (StorageException ex) {
                        throw ExceptionFactory.systemErrorException(ex);
                    }
                }
            });
            policies.removeAll(policiesOnGateway);
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
            case DATADOG:
                plugin.setConfig(new Gson().fromJson(storage.getPolicyDefinition(polDef.getPolicyDefId()).getDefaultConfig(), polDef.getClazz()));
                break;
            case JWT:
                plugin.setConfig(new KongPluginJWT().withClaimsToVerify(Arrays.asList(IJWT.EXPIRATION_CLAIM)).withKeyClaimName(IJWT.AUDIENCE_CLAIM));
                break;
            default:
                throw new IllegalArgumentException("Not a valid policy for required api's: " + polDef.toString());
        }
        return plugin;
    }

    private void verifyOrCreateConsumers(IGatewayLink gw) throws StorageException {
        String pemPublicKey = idpLinkFactory.getDefaultIDPClient().getDefaultPublicKeyInPemFormat();
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
                if (StringUtils.isNotEmpty(mab.getIdpClient())) {
                    KongPluginJWTResponseList jwtCreds = gw.getConsumerJWT(id);
                    if (jwtCreds != null && jwtCreds.getData().isEmpty()) {
                        gw.addConsumerJWT(id, mab.getIdpClient(), pemPublicKey);
                    } else if (jwtCreds != null) {
                        List<KongPluginJWTResponse> removed = new ArrayList<>();
                        jwtCreds.getData().forEach(cred -> {
                            if (!cred.getRsaPublicKey().trim().equals(pemPublicKey.trim())) {
                                gw.deleteConsumerJwtCredential(id, cred.getId());
                                removed.add(cred);
                            }
                        });
                        jwtCreds.getData().removeAll(removed);
                        if (jwtCreds.getData().isEmpty()) {
                            gw.addConsumerJWT(id, mab.getIdpClient(), pemPublicKey);
                        }
                    }
                }
            }
            else {
                throw ExceptionFactory.gatewayNotFoundException(gw.getGatewayId());
            }
        });

    }
}
