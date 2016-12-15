package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.IApiKeyGenerator;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.rest.GatewayValidation;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.t1t.digipolis.util.JWTUtils.JWT_RS256;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SyncFacade {

    private static final Logger log = LoggerFactory.getLogger(SyncFacade.class);
    private static final String PLACEHOLDER_CALLBACK_URI = "http://localhost/";

    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private IApiKeyGenerator apiKeyGenerator;
    @Inject
    private GatewayValidation gatewayValidation;

    @TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
    public void syncAll() {
        log.info("===== STARTING ENTIRE SYNC CYCLE =====");
        LocalDateTime start = LocalDateTime.now();
        try {
            syncUsers();
            syncServices();
            syncApplications();
            syncPolicies();
            syncTokens();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("==== SYNC CYCLE FAILED ====");
        }
        log.info("===== ENTIRE SYNC CYCLE END, COMPLETED IN {} =====", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncUsers() {
        log.info("=== SYNC USERS START ===");
        LocalDateTime start = LocalDateTime.now();
        try {
            List<IGatewayLink> gateways = query.getAllGateways().stream().map(gwBean -> gatewayFacade.createGatewayLink(gwBean.getId())).collect(Collectors.toList());
            idmStorage.getAllUsers().forEach(user -> {
                log.info("== STARTING SYNC FOR {} WITH ID {} ==", user.getKongUsername(), user.getUsername());
                String customId = ConsumerConventionUtil.createUserUniqueId(user.getUsername());
                gateways.forEach(gw -> {
                    try {
                        if (StringUtils.isNotEmpty(user.getKongUsername())) {
                            KongConsumer gwUser = gw.getConsumer(user.getKongUsername());
                            if (gwUser == null) {
                                gw.createConsumerWithKongId(user.getKongUsername(), customId);
                                gw.addConsumerJWT(user.getKongUsername(), JWT_RS256);
                                log.info("= Sync user with kong id {} and username {} and created JWT credentials =", user.getKongUsername(), customId);
                            } else if (gw.getConsumerJWT(gwUser.getId()).getData().stream().filter(jwt -> jwt.getAlgorithm().equals(JWT_RS256)).collect(Collectors.toList()).isEmpty()) {
                                gw.addConsumerJWT(user.getKongUsername(), JWT_RS256);
                                log.info("= Created JWT credentials for user with kong id {} and username {} =", user.getKongUsername(), customId);
                            }
                            else {
                                log.info("= No sync needed for user with kong id {} and username {} =", user.getKongUsername(), customId);
                            }
                        } else {
                            KongConsumer consumer = gw.getConsumerByCustomId(customId);
                            if (consumer != null) {
                                user.setKongUsername(consumer.getId());
                                log.info("= Synced user with kong id {} and username {} =", user.getKongUsername(), customId);
                            } else {
                                user.setKongUsername(gw.createConsumerWithCustomId(customId).getId());
                                log.info("= Created user with kong id {} and username {} =", user.getKongUsername(), customId);
                            }
                            try {
                                idmStorage.updateUser(user);
                            } catch (StorageException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    catch (Exception ex) {
                        log.error("= No sync executed for kong id {} and username {} =", user.getKongUsername(), customId);
                    }
                });
                log.info("== SYNC END FOR {} WITH ID {} ==", user.getKongUsername(), user.getUsername());
            });
        } catch (Exception e) {
            log.error("Synchronize Users failed due to:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("=== SYNC USERS END, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
    }


    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncServices() {
        try {
            LocalDateTime start = LocalDateTime.now();
            log.info("==== BEGIN SERVICES SYNC ====");
            query.getPublishedServiceVersions().forEach(svb -> {
                try {
                    String apiId = ServiceConventionUtil.generateServiceUniqueName(svb);
                    log.info("== BEGINNING SYNC FOR:{} ==", apiId);
                    String requestPath = GatewayPathUtilities.generateGatewayContextPath(svb.getService().getOrganization().getId(), svb.getService().getBasepath(), svb.getVersion());
                    svb.getGateways()
                            .stream()
                            .map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId()))
                            .forEach(gw -> {
                                try {
                                    KongApi api = gw.getApi(apiId);
                                    if (api == null) {
                                        gw.createApi(new KongApi().withStripRequestPath(true)
                                                .withPreserveHost(false)
                                                .withRequestHost(apiId)
                                                .withRequestPath(requestPath)
                                                .withUpstreamUrl(svb.getEndpoint())
                                                .withName(apiId));
                                        log.info("= API Missing on gateway, recreated:{} =", apiId);
                                    } else {
                                        api = compareServiceApis(api, svb, requestPath, apiId);
                                        if (api != null) {
                                            gw.updateOrCreateApi(api);
                                            log.info("= API out of sync, resynced:{} =", apiId);
                                        } else {
                                            log.info("= No sync necessary for:{} =", apiId);
                                        }
                                    }
                                }
                                catch (Exception ex) {
                                    log.error("== Sync failed on gateway:{} ==", gw.getGatewayId());
                                    ex.printStackTrace();
                                }
                                log.info("== SYNC END FOR:{} ==", apiId);
                            });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            log.info("=== SYNCING SERVICES END, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncApplications() {
        log.info("=== SYNC APPLICATIONS START ===");
        LocalDateTime start = LocalDateTime.now();
        try {
            query.findAllApplicationVersions().forEach(avb -> {
                String appId = ConsumerConventionUtil.createAppUniqueId(avb);
                try {
                    log.info("== SYNC START FOR {} ==", appId);

                    getApplicationVersionGatewayLinks(avb).forEach(gw -> {
                        String gwId = gw.getGatewayId();
                        try {
                            KongConsumer consumer = gw.getConsumer(appId);
                            if (consumer == null) {
                                consumer = gw.createConsumer(appId);
                                log.info("== Application missing, created for {} on gateway {} ==", appId, gwId);
                            }
                            else {
                                log.info("== No sync necessary for {} on gateway {} ==", appId, gwId);
                            }
                            boolean appModified = false;

                            if (StringUtils.isEmpty(avb.getoAuthClientId())) {
                                avb.setoAuthClientId(apiKeyGenerator.generate());
                                appModified = true;
                            }
                            if (StringUtils.isEmpty(avb.getOauthClientSecret())) {
                                avb.setOauthClientSecret(apiKeyGenerator.generate());
                                appModified = true;
                            }
                            if (avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().isEmpty()) {
                                avb.setOauthClientRedirects(new HashSet<>(Collections.singleton(PLACEHOLDER_CALLBACK_URI)));
                                appModified = true;
                            }
                            if (StringUtils.isEmpty(avb.getApikey())) {
                                avb.setApikey(apiKeyGenerator.generate());
                                appModified = true;
                            }

                            try {
                                //Sync or create OAuth2 credentials
                                KongPluginOAuthConsumerResponseList oauthCreds = gw.getConsumerOAuthCredentials(appId);
                                if (!oauthCreds.getData().isEmpty()) {
                                    //delete the credentials that do not match the stored credentials
                                    oauthCreds.getData().stream().filter(oauth -> !oauth.getRedirectUri().equals(avb.getOauthClientRedirects())
                                            || !oauth.getClientId().equals(avb.getoAuthClientId())
                                            || !oauth.getClientSecret().equals(avb.getOauthClientSecret())).forEach(oauth -> {
                                        //delete the credentials that do not match the stored credentials
                                        gw.deleteOAuthConsumerPlugin(appId, oauth.getId());
                                    });
                                    oauthCreds = gw.getConsumerOAuthCredentials(appId);
                                }
                                if (oauthCreds.getData().isEmpty()) {
                                    KongPluginOAuthConsumerResponse response = gw.enableConsumerForOAuth(appId, new KongPluginOAuthConsumerRequest()
                                            .withClientId(avb.getoAuthClientId())
                                            .withClientSecret(avb.getOauthClientSecret())
                                            .withRedirectUri(avb.getOauthClientRedirects())
                                            .withName(appId));
                                    avb.setOauthCredentialId(response.getId());
                                    appModified = true;
                                    log.info("= No OAuth creds found for app \"{}\" on gateway \"{}\", created =", appId, gwId);
                                } else {
                                    avb.setOauthCredentialId(oauthCreds.getData().get(0).getId());
                                    appModified = true;
                                    log.info("= No oauth sync necessary for app \"{}\" on gateway \"{}\" =", appId, gwId);
                                }
                            }
                            catch (Exception ex) {
                                log.error("= OAuth sync failed for app \"{}\" on gateway \"{}\" =", appId, gwId);
                            }

                            try {
                                //Sync or create Key Auth credentials
                                KongPluginKeyAuthResponseList keyAuthCreds = gw.getConsumerKeyAuth(appId);
                                if (!keyAuthCreds.getData().isEmpty()) {
                                    keyAuthCreds.getData().stream().filter(key -> !key.getKey().equals(avb.getApikey())).forEach(key -> {
                                        gw.deleteConsumerKeyAuth(appId, key.getKey());
                                    });
                                    keyAuthCreds = gw.getConsumerKeyAuth(appId);
                                }
                                if (keyAuthCreds.getData().isEmpty()) {
                                    gw.addConsumerKeyAuth(appId, avb.getApikey());
                                    log.info("= No Key Auth found for app \"{}\" on gateway \"{}\", created =", appId, gwId);
                                } else {
                                    log.info("= No key auth sync necessary for app \"{}\" on gateway \"{}\" =", appId, gwId);
                                }
                            }
                            catch (Exception ex) {
                                log.error("= Key auth sync failed for app \"{}\" on gateway \"{}\" =", appId, gwId);
                            }

                            try {
                                //Sync or create JWT credentials
                                KongPluginJWTResponseList jwtCreds = gw.getConsumerJWT(appId);
                                if (!jwtCreds.getData().isEmpty()) {
                                    jwtCreds.getData().stream().filter(jwt -> !jwt.getAlgorithm().equals(JWTUtils.JWT_RS256)).forEach(jwt -> {
                                        gw.deleteConsumerJwtCredential(appId, jwt.getId());
                                    });
                                    jwtCreds = gw.getConsumerJWT(appId);
                                } else {
                                    log.info("= No JWT credentials for app \"{}\" on gateway \"{}\" =", appId, gwId);
                                }
                                if (jwtCreds.getData().isEmpty()) {
                                    gw.addConsumerJWT(appId, JWT_RS256);
                                    log.info("= No JWT credentials found for app \"{}\" on gateway \"{}\", created =", appId, gwId);
                                } else {
                                    log.info("= No JWT sync necessary for app \"{}\" on gateway \"{}\" =", appId, gwId);
                                }
                            }
                            catch (Exception ex) {
                                log.error("= JWT sync failed for app \"{}\" on gateway \"{}\" =", appId, gwId);
                            }

                            //TODO - Add Basic Auth once plugin/policy is enabled

                            if (appModified) {
                                storage.updateApplicationVersion(avb);
                            }
                        }
                        catch (Exception ex) {
                            log.info("== Sync failed for {} on gateway {} ==", appId, gwId);
                        }
                    });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.info("== Sync failed for {} ==", appId);
                }
                log.info("== SYNC END FOR {} ==", appId);
            });
        } catch (Exception e) {
            log.error("== Synchronize Consumers failed due to {} =", e.getMessage());
            e.printStackTrace();
        }
        log.info("=== SYNC APPLICATIONS END, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncPolicies() {
        log.info("==== SYNC POLICIES START ====");
        LocalDateTime start = LocalDateTime.now();
        try {
            syncServicePolicies();
            syncContractPolicies();
            syncConsentPolicies();
            syncPlanPolicies();
        }
        catch (Exception ex) {
            log.error("=== SYNC POLICES FAILED DUE TO {}===", ex.getMessage());
        }
        log.info("==== SYNC POLICIES END, COMPLETED IN {} ====", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncServicePolicies() {
        Gson gson = new Gson();
        LocalDateTime start = LocalDateTime.now();
        log.info("=== SYNC SERVICE POLICIES START ===");
        try {
            query.getPublishedServiceVersions().forEach(svb -> {
                String serviceId = ServiceConventionUtil.generateServiceUniqueName(svb);
                try {
                    log.info("== SYNC START FOR SERVICE {} ==", serviceId);
                    query.listPoliciesForEntity(svb.getService().getOrganization().getId(), svb.getService().getId(), svb.getVersion(), PolicyType.Service)
                            .forEach(policy -> {
                                log.info("= SYNC START FOR {} POLICY FOR SERVICE {} =", policy.getName(), serviceId);
                                try {
                                    if (policy.getGatewayId() == null) {
                                        policy.setGatewayId(gatewayFacade.getDefaultGateway().getId());
                                    }
                                    IGatewayLink gw = gatewayFacade.createGatewayLink(policy.getGatewayId());
                                    KongApi api = gw.getApi(serviceId);
                                    Policies polDef = Policies.valueOf(policy.getDefinition().getId().toUpperCase());
                                    String kongPluginIdentifier = polDef.getKongIdentifier();

                                    if (policy.getKongPluginId() != null) {
                                        KongPluginConfig gwPlugin = gw.getPlugin(policy.getKongPluginId());
                                        if (gwPlugin == null) {
                                            gw.createApiPlugin(api.getId(), new KongPluginConfig()
                                                    .withId(policy.getKongPluginId())
                                                    .withName(kongPluginIdentifier)
                                                    .withConfig(gson.fromJson(policy.getConfiguration(), polDef.getClazz())));
                                        }
                                        else {
                                            gwPlugin.setConfig(gson.fromJson(policy.getConfiguration(), polDef.getClazz()));
                                            if (gw.updatePlugin(gwPlugin)!= null) {
                                                log.info("= Policy synced for:{} - {} =", serviceId, policy.getDefinition().getName());
                                            }
                                            else {
                                                log.info("= Policy sync failed for:{} - {} =", serviceId, policy.getDefinition().getName());
                                            }
                                        }
                                    } else {
                                        gw.getServicePlugins(serviceId).getData().stream()
                                                .filter(plugin -> plugin.getName().equals(kongPluginIdentifier) &&
                                                        StringUtils.isEmpty(plugin.getConsumerId()))
                                                .forEach(filteredPlugin -> policy.setKongPluginId(filteredPlugin.getId()));
                                        if (policy.getKongPluginId() == null) {
                                            KongPluginConfig newPlugin = new KongPluginConfig().withApiId(api.getId()).withEnabled(policy.isEnabled()).withName(kongPluginIdentifier);
                                            if (polDef.equals(Policies.OAUTH2)) {
                                                newPlugin.setConfig(gatewayValidation.validateExplicitOAuth((KongPluginOAuth) gson.fromJson(policy.getConfiguration(), polDef.getClazz())));
                                            } else {
                                                newPlugin.setConfig(gson.fromJson(policy.getConfiguration(), polDef.getClazz()));
                                            }
                                            policy.setKongPluginId(gw.createApiPlugin(serviceId, newPlugin).getId());
                                            log.info("= Policy missing on gateway, created:{} - {} =", serviceId, policy.getDefinition().getName());
                                        }
                                        try {
                                            storage.updatePolicy(policy);
                                        } catch (StorageException ex) {
                                            ex.printStackTrace();
                                            log.error("= Policy sync failed:{} - {} =", serviceId, policy.getDefinition().getName());
                                        }
                                    }
                                    storage.updatePolicy(policy);
                                    log.info("= SYNC END FOR {} POLICY FOR SERVICE {} =", policy.getName(), serviceId);
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                    log.error("= SYNC FAILED FOR {} POLICY FOR SERVICE {} =", policy.getName(), serviceId);
                                }
                            });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("== SYNC FAULED FOR SERVICE {} DUE TO {} ==", serviceId, ex.getMessage());
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.warn("=== Syncing service policies failed ===");
        }
        log.info("=== SYNC SERVICE POLICIES END, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncContractPolicies() {
        Gson gson = new Gson();
        LocalDateTime start = LocalDateTime.now();
        try {
            log.info("=== START CONTRACT POLICY SYNC ===");
            Map<Long, ContractBean> contracts = new HashMap<>();
            query.getAllPoliciesByType(PolicyType.Contract).forEach(policy -> {
                String entityId = ServiceConventionUtil.generateServiceUniqueName(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion());
                log.info("=== START CONTRACT POLICY SYNC FOR {} - {} ===", policy.getName(), entityId);
                try {
                    ContractBean contract = null;
                    if (contracts.containsKey(policy.getContractId()) && contracts.get(policy.getContractId()) != null) {
                        contract = contracts.get(policy.getContractId());
                    }
                    else {
                        contract = storage.getContract(policy.getContractId());
                        contracts.put(contract.getId(), contract);
                    }
                    if (contract == null) {
                        throw ExceptionFactory.contractNotFoundException();
                    }
                    String appId = ConsumerConventionUtil.createAppUniqueId(contract.getApplication());
                    String serviceId = ServiceConventionUtil.generateServiceUniqueName(contract.getService());
                    Policies polDef = Policies.valueOf(policy.getDefinition().getId().toUpperCase());
                    if (StringUtils.isNotEmpty(policy.getGatewayId()) && StringUtils.isNotEmpty(policy.getKongPluginId())) {
                        IGatewayLink gw = gatewayFacade.createGatewayLink(policy.getGatewayId());
                        String apiId = gw.getApi(serviceId).getId();
                        String consumerId = gw.getConsumer(appId).getId();
                        if (policy.getDefinition().getId().equals(Policies.ACL.getPolicyDefId())) {
                            String group = gson.fromJson(policy.getConfiguration(), KongPluginACLResponse.class).getGroup();
                            KongPluginACLResponse acl = gw.getConsumerACL(ConsumerConventionUtil.createAppUniqueId(contract.getApplication()), policy.getKongPluginId());
                            if (acl == null) {
                                acl = gw.getAllConsumerAcls(appId).getData().stream().filter(group::equals).collect(CustomCollectors.getSingleResult());
                                if (acl != null) {
                                    policy.setKongPluginId(acl.getId());
                                    log.info("== CONTRACT {} POLICY OUT OF SYNC FOR {}, SYNCED ==", policy.getName(), entityId);
                                }
                                else {
                                    policy.setKongPluginId(gw.addConsumerToACL(appId, group).getId());
                                    log.info("== CONTRACT {} POLICY NOT FOUND FOR {}, CREATED", policy.getName(), entityId);
                                }
                                storage.updatePolicy(policy);
                            }
                            else if (acl.getGroup().equals(group)){
                                log.info("== NO SYNC NECESSARY FOR {} ==", policy.getName(), entityId);
                            }
                            else {
                                gw.updateConsumerACL(acl.withGroup(group));
                                log.info("== CONTRACT {} POLICY FOR {} SYNCED ==", policy.getName(), entityId);
                            }
                        }
                        else {
                            KongPluginConfig plugin = gw.getPlugin(policy.getKongPluginId());
                            if (plugin == null) {
                                plugin = gw.getConsumerSpecificApiPlugins(consumerId, apiId).getData()
                                        .stream().filter(gwPlugin -> gwPlugin.getName().equals(polDef.getKongIdentifier())).collect(CustomCollectors.getSingleResult());
                                if (plugin != null) {
                                    policy.setKongPluginId(plugin.getId());
                                    storage.updatePolicy(policy);
                                    log.info("== CONTRACT {} POLICY OUT OF SYNC FOR {}, SYNCED ==", policy.getName(), entityId);
                                } else if (gw.updatePlugin(new KongPluginConfig()
                                        .withConfig(gson.fromJson(policy.getConfiguration(),  polDef.getClazz()))
                                        .withApiId(apiId)
                                        .withConsumerId(consumerId)
                                        .withEnabled(policy.isEnabled())
                                        .withCreatedAt((double) new Date().getTime())
                                        .withId(policy.getKongPluginId()).withName(polDef.getKongIdentifier())) != null) {
                                    log.info("== CONTRACT {} POLICY NOT FOUND FOR {}, CREATED ==", policy.getName(), entityId);
                                }
                                else {
                                    log.error("== CONTRACT {} POLICY NOT FOUND FOR {}, CREATION FAILED ==", policy.getName(), entityId);
                                }
                            }
                            else if (gw.updatePlugin(plugin
                                    .withConfig(gson.fromJson(policy.getConfiguration(),  polDef.getClazz()))) != null) {
                                log.info("== CONTRACT {} POLICY FOR {} SYNCED ==", policy.getName(), entityId);
                            }
                            else {
                                log.info("== CONTRACT {} POLICY FOR {} SYNC FAILED ==", policy.getName(), entityId);
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("== SYNC FAILED FOR CONTRACT POLICY {} ==", policy.getName());
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.info("=== CONTRACT POLICY SYNC FAILED DUE TO {} ===", ex.getMessage());
        }
        log.info("=== END CONTRACT POLICY SYNC, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncConsentPolicies() {
        Gson gson = new Gson();
        LocalDateTime start = LocalDateTime.now();
        try {
            log.info("=== BEGINNING CONSENT POLICIES SYNC ===");
            query.getAllPoliciesByType(PolicyType.Consent).forEach(policy -> {
                if (policy.getDefinition().getId().equals(Policies.ACL.getPolicyDefId())) {
                    String engineACLGroup = gson.fromJson(policy.getConfiguration(), KongPluginACLRequest.class).getGroup();
                    log.info("== SYNCING CONSENT ACL FOR:{} ==", engineACLGroup);
                    String consentAppId = ConsumerConventionUtil.createAppUniqueId(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion());
                    if (policy.getKongPluginId() != null && policy.getGatewayId() != null) {
                        IGatewayLink gw = gatewayFacade.createGatewayLink(policy.getGatewayId());
                        KongPluginACLResponse acl = gw.getConsumerACL(consentAppId, policy.getKongPluginId());
                        if (acl != null) {
                            if (!acl.getGroup().equals(engineACLGroup)) {
                                gw.updateConsumerACL(acl.withGroup(engineACLGroup));
                                log.info("= Consent ACL synced for: {} =", engineACLGroup);
                            }
                        } else {
                            try {
                                policy.setKongPluginId(gw.addConsumerToACL(consentAppId, engineACLGroup).getId());
                                storage.updatePolicy(policy);
                                log.info("= Consent ACL missing on gateway, created:{} =", engineACLGroup);
                            }
                            catch (StorageException ex) {
                                ex.printStackTrace();
                                log.error("= Failed to update policy with new kong plugin id =");
                            }
                        }
                        log.info("== SYNC CONSENT ACL ENDED FOR:{} ==", engineACLGroup);
                    }
                }
                else {
                    //TODO - Implement other consent policies if any
                    log.warn("== Unsupported Consent Policy type ==");
                }
            });
            log.info("=== CONSENT ACLS SYNC ENDED, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
        }
        catch (Exception ex) {
            log.error("=== SYNC CONSENT POLICIES FAILED DUE TO {} ===", ex.getMessage());
        }
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncPlanPolicies() {
        Gson gson = new Gson();
        LocalDateTime start = LocalDateTime.now();
        log.info("=== PLAN POLICY SYNC START ===");
        try {
            query.getPlanVersionContractMap().entrySet().forEach(entry -> {
                PlanVersionBean pvb = entry.getKey();
                String planId = ServiceConventionUtil.generateServiceUniqueName(pvb.getPlan().getOrganization().getId(), pvb.getPlan().getId(), pvb.getVersion());
                try {
                    List<ContractBean> contracts = entry.getValue();
                    List<PolicyBean> planPolicies = query.getPlanPolicies(pvb);
                    contracts.forEach(contract -> {
                        ServiceVersionBean svb = contract.getService();
                        String svcId = ServiceConventionUtil.generateServiceUniqueName(svb);
                        ApplicationVersionBean avb = contract.getApplication();
                        String appId = ConsumerConventionUtil.createAppUniqueId(avb);
                        planPolicies.forEach(planPol -> {
                            Policies polDef = Policies.valueOf(planPol.getDefinition().getId().toUpperCase());
                            Object config = gson.fromJson(planPol.getConfiguration(), polDef.getClazz());
                            svb.getGateways().stream().map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId())).forEach(gw -> {
                                try {
                                    String apiId = gw.getApi(svcId).getId();
                                    String consumerId = gw.getConsumer(appId).getId();
                                    PolicyBean pol = query.getPolicyByContractIdAndDefinitionForEntity(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion(), planPol.getDefinition().getId(), contract.getId(), gw.getGatewayId());
                                    if (pol == null) {
                                        PolicyBean policy = new PolicyBean();
                                        KongPluginConfig plugin = gw.createApiPlugin(svcId, new KongPluginConfig().withApiId(apiId).withConsumerId(consumerId).withEnabled(true).withName(polDef.getKongIdentifier()).withConfig(config));
                                        policy.setEnabled(true);
                                        policy.setId(null);
                                        policy.setDefinition(planPol.getDefinition());
                                        policy.setName(planPol.getDefinition().getName());
                                        policy.setConfiguration(gson.toJson(config));
                                        policy.setCreatedBy(planPol.getCreatedBy());
                                        policy.setCreatedOn(new Date());
                                        policy.setModifiedBy(planPol.getCreatedBy());
                                        policy.setModifiedOn(new Date());
                                        policy.setOrganizationId(avb.getApplication().getOrganization().getId());
                                        policy.setEntityId(avb.getApplication().getId());
                                        policy.setEntityVersion(avb.getVersion());
                                        policy.setType(PolicyType.Contract);
                                        policy.setOrderIndex(query.getMaxPolicyOrderIndex(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion(), PolicyType.Contract) + 1);
                                        policy.setKongPluginId(plugin.getId());
                                        policy.setContractId(contract.getId());
                                        policy.setGatewayId(gw.getGatewayId());
                                        storage.createPolicy(policy);
                                        log.info("== Contract policy does not exist for contract {} under plan {} on gateway {}, created ==", contract.getId(), planId, gw.getGatewayId());
                                    }
                                    else {
                                        KongPluginConfig plugin = gw.getPlugin(pol.getKongPluginId());
                                        if (plugin == null) {
                                            gw.createApiPlugin(svcId, new KongPluginConfig().withApiId(apiId).withConsumerId(consumerId).withEnabled(true).withName(polDef.getKongIdentifier()).withConfig(gson.fromJson(planPol.getConfiguration(), polDef.getClazz())));
                                            log.info("== Contract policy missing for contract {} under plan {} on gateway {}, created ==", contract.getId(), planId, gw.getGatewayId());
                                        }
                                        else {
                                            if (gw.updatePlugin(plugin.withConfig(config)) != null) {
                                                log.info("== Contract policy synced for contract {} under plan {} on gateway {} ==", contract.getId(), planId, gw.getGatewayId());
                                            }
                                            else {
                                                log.info("== Contract policy sync failed for contract {} under plan {} on gateway {} ==", contract.getId(), planId, gw.getGatewayId());
                                            }
                                        }
                                    }
                                }
                                catch (StorageException ex) {
                                    log.error("== SYNCING PLAN POLICIES FAILED FOR CONTRACT {} ON GATEWAY {} ==", contract.getId(), gw.getGatewayId());
                                    ex.printStackTrace();
                                }
                            });
                        });
                    });
                }
                catch (StorageException ex) {
                    log.error("== SYNCING POLICIES FOR PLAN {} FAILED ==", planId);
                    ex.printStackTrace();
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("=== SYNC PLAN POLICIES FAILED DUE TO {} ===", ex.getMessage());
        }
        log.info("=== PLAN POLICY SYNC END, COMPLETED IN {} ===", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
    public void syncTokens() {
        log.info("==== START TOKEN SYNC ====");
        LocalDateTime start = LocalDateTime.now();
        try {
            query.getAllOAuthTokens().forEach(this::syncToken);
            log.info("==== START TOKEN SYNC, COMPLETED IN {} ====", TimeUtil.getTimeSince(start));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("==== TOKEN SYNC FAILED DUE TO {} ====", ex.getMessage());
        }
    }

    public void deleteBackedUpTokens() {
        try {
            query.deleteAllOAuthTokens();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void backUpOAuthTokens() {
        log.info("==== START TOKEN BACKUP ====");
        LocalDateTime start = LocalDateTime.now();
        try {
            query.deleteAllOAuthTokens();
            Set<OAuth2TokenBean> tokens = new HashSet<>();
            query.getAllGateways().stream().map(gatewayBean -> gatewayFacade.createGatewayLink(gatewayBean.getId())).forEach(gateway -> {
                KongOAuthTokenList tokenList = gateway.getAllOAuth2Tokens(null);
                long total = tokenList.getTotal().longValue();
                long i = 0;
                tokens.addAll(tokenList.getData().stream().map(oauthToken -> new OAuth2TokenBean(oauthToken, gateway.getGatewayId())).collect(Collectors.toList()));
                while (tokenList.getOffset() != null && i < total/100) {
                    tokenList = gateway.getAllOAuth2Tokens(tokenList.getOffset());
                    tokens.addAll(tokenList.getData().stream().map(oauthToken -> new OAuth2TokenBean(oauthToken, gateway.getGatewayId())).collect(Collectors.toList()));
                    i++;
                }
            });
            tokens.forEach(token -> {
                try {
                    storage.updateOAuth2TokenBean(token);
                }
                catch (StorageException ex) {
                    throw ExceptionFactory.systemErrorException(ex);
                }
            });
            log.info("==== TOKEN BACKUP COMPLETED, {} TOKENS IN {}", tokens.size(), TimeUtil.getTimeSince(start));
        }
        catch (StorageException ex) {
            log.error("==== TOKEN BACKUP FAILED DUE TO {} ====", ex.getMessage());
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void syncEmptyKongPluginIds() {
        try{
            List<PolicyBean> policies = query.getDefaultUnpublishedPolicies();
            policies.forEach(policy -> {
                try {
                    IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                    KongPluginConfigList plugins = gateway.getServicePlugins(ServiceConventionUtil.generateServiceUniqueName(policy.getOrganizationId(), policy.getEntityId(), policy.getEntityVersion()));
                    for (KongPluginConfig plg : plugins.getData()) {
                        String plgDef = GatewayUtils.convertKongPluginNameToPolicy(plg.getName()).getPolicyDefId();
                        if (plgDef.equals(policy.getDefinition().getId())) {
                            policy.setKongPluginId(plg.getId());
                            policy.setGatewayId(gateway.getGatewayId());
                            storage.updatePolicy(policy);
                        }
                    }
                }
                catch (StorageException ex) {
                    throw ExceptionFactory.systemErrorException(ex);
                }
                catch (Exception ex) {
                    //do nothing
                }
            });
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    /************* PRIVATE METHODS *************/

    private KongApi compareServiceApis(KongApi api, ServiceVersionBean svb, String requestPath, String apiId) {
        if (!api.getUpstreamUrl().equals(svb.getEndpoint()) ||
                !api.getRequestHost().equals(apiId) ||
                !api.getRequestPath().equals(requestPath) ||
                api.getPreserveHost() ||
                !api.getStripRequestPath()) {
            return api.withRequestPath(requestPath)
                    .withRequestHost(apiId)
                    .withRequestPath(requestPath)
                    .withUpstreamUrl(svb.getEndpoint())
                    .withPreserveHost(false)
                    .withStripRequestPath(true);
        }
        else {
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void syncToken(OAuth2TokenBean token) {
        try {
            KongOAuthToken gwToken = gatewayFacade.createGatewayLink(token.getGatewayId()).createOAuthToken(token);
            if (gwToken != null) {
                storage.deleteOAuth2Token(token);
            }
        }
        catch (Exception ex) {
            gatewayFacade.createGatewayLink(token.getGatewayId()).revokeOAuthToken(token.getId());
            log.error("=== FAILED TO SYNC TOKEN {} ===", token.getAccessToken());
            ex.printStackTrace();
        }
    }

    private Set<IGatewayLink> getApplicationVersionGatewayLinks(ApplicationVersionBean avb) throws StorageException {
        Set<IGatewayLink> gateways = new HashSet<>();
        switch (avb.getStatus()) {
            case Ready:
            case Created:
                gateways.add(gatewayFacade.getDefaultGatewayLink());
                break;
            case Registered:
                gateways.addAll(query.getRegisteredApplicationVersionGatewayIds(avb).stream().map(gw -> gatewayFacade.createGatewayLink(gw)).collect(Collectors.toSet()));
                break;
            case Retired:
                log.info("== Application {} is in status {}, no gateways ==", ConsumerConventionUtil.createAppUniqueId(avb), avb.getStatus());
                break;
        }
        return gateways;
    }
}