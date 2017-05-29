package com.t1t.apim.facades;

import com.google.gson.Gson;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.apim.beans.contracts.ContractBean;
import com.t1t.apim.beans.idp.KeystoreBean;
import com.t1t.apim.beans.mail.MailProviderBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.policies.PolicyBean;
import com.t1t.apim.beans.policies.PolicyType;
import com.t1t.apim.beans.services.SchemeType;
import com.t1t.apim.beans.services.ServiceUpstreamTargetBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.core.IApiKeyGenerator;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.idp.IDPClient;
import com.t1t.apim.idp.IDPLinkFactory;
import com.t1t.apim.idp.dto.RealmClient;
import com.t1t.kong.model.*;
import com.t1t.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SyncFacade {

    private static final Logger log = LoggerFactory.getLogger(SyncFacade.class);
    private static final String PLACEHOLDER_CALLBACK_URI = "http://localhost/";

    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private GatewayFacade gatewayFacade;
    @Inject private IApiKeyGenerator apiKeyGenerator;
    @Inject private IDPLinkFactory idpLinkFactory;

    @TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
    public void syncAll() {
        log.info("===== STARTING ENTIRE SYNC CYCLE =====");
        LocalDateTime start = LocalDateTime.now();
        try {
            syncOrganizations();
            syncServices();
            syncApplications();
            syncPolicies();
            syncTokens();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("===== SYNC CYCLE FAILED DUE TO {} =====", ex.getMessage());
        }
        log.info("===== ENTIRE SYNC CYCLE END, COMPLETED IN {} =====", TimeUtil.getTimeSince(start));
    }

    @TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
    public void syncOrganizations() {
        log.info("==== STARTING ORGANIZATION SYNC ====");
        LocalDateTime start = LocalDateTime.now();
        try {
            IDPClient idp = idpLinkFactory.getDefaultIDPClient();
            List<OrganizationBean> allOrgs = query.getAllOrgs();
            for (OrganizationBean o : allOrgs) {
                log.info("=== START SYNC FOR ORGANIZATION: {} ===", o.getName());
                boolean changed = false;
                try {
                    KeystoreBean keystore = query.getDefaultKeystore();
                    MailProviderBean mailProvider = query.getDefaultMailProvider();
                    if (StringUtils.isEmpty(o.getKeystoreKid())) {
                        if (keystore == null) throw ExceptionFactory.keystoreNotFoundException("default");
                        o.setKeystoreKid(keystore.getKid());
                        changed = true;
                    }
                    if (o.getMailProviderId() == null) {
                        if (mailProvider == null) throw ExceptionFactory.mailProviderNotFoundException(0L);
                        o.setMailProviderId(mailProvider.getId());
                        changed = true;
                    }
                    if (idp.realmExists(o.getId())) {
                        log.info("=== REALM {} ALREADY EXISTS ON IDP ===", o.getId());
                        //Check if the keystore and mailprovider are set
                        if (!idp.realmKeystoreExists(o.getId(), keystore.getKid())) {
                            idp.setRealmKeystore(o.getId(), keystore);
                        }
                        if (!idp.realmMailProviderExists(o.getId(), mailProvider)) {
                            idp.setRealmMailProvider(o.getId(), mailProvider);
                        }
                    } else {
                        idp.createRealm(o, keystore, mailProvider);
                        log.info("=== REALM CREATED ON IDP ===", o.getId());
                    }
                    if (changed) storage.updateOrganization(o);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("=== SYNC FOR ORGANIZATION {} FAILED DUE TO {}", o.getName(), ex.getMessage());
                }
                log.info("=== END SYNC FOR ORGANIZATION: {} ===", o.getName());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("==== ORGANIZATION SYNC FAILED DUE TO {} ====", ex.getMessage());
        }
        log.info("==== ORGANIZATION SYNC COMPLETED ====");
    }

    @TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
    public void syncServices() {
        try {
            LocalDateTime start = LocalDateTime.now();
            log.info("==== BEGIN SERVICES SYNC ====");
            query.getPublishedServiceVersions().forEach(svb -> {
                String apiId = ServiceConventionUtil.generateServiceUniqueName(svb);
                try {
                    if (svb.getUpstreamScheme() == null || svb.getUpstreamTargets().isEmpty()) {
                        try {
                            URI endpoint = new URI(svb.getEndpoint());
                            svb.setUpstreamPath(endpoint.getPath());
                            svb.setUpstreamScheme(SchemeType.valueOf(endpoint.getScheme().toUpperCase()));
                            ServiceUpstreamTargetBean target = new ServiceUpstreamTargetBean();
                            target.setTarget(endpoint.getHost());
                            target.setPort(endpoint.getPort() == -1 ? svb.getUpstreamScheme().getPort() : Long.valueOf(endpoint.getPort()));
                            if (svb.getUpstreamTargets() == null) svb.setUpstreamTargets(new HashSet<>());
                            svb.getUpstreamTargets().add(target);
                            storage.updateServiceVersion(svb);
                        }
                        catch (Exception ex) {
                            //Do nothing
                        }
                    }
                    log.info("=== BEGINNING SYNC FOR:{} ===", apiId);
                    List<String> requestPaths = GatewayPathUtilities.generateGatewayContextPath(svb.getService().getOrganization().getId(), svb.getService().getBasepaths(), svb.getVersion());
                    final String upstreamUrl;
                    if (svb.getUpstreamTargets().size() == 1) {
                        ServiceUpstreamTargetBean target = svb.getUpstreamTargets().stream().collect(CustomCollectors.getFirstResult());
                        upstreamUrl = URIUtils.buildEndpoint(svb.getUpstreamScheme(), target.getTarget(), target.getPort(), svb.getUpstreamPath());
                    }
                    else {
                        upstreamUrl = URIUtils.buildEndpoint(svb.getUpstreamScheme(), apiId, null, svb.getUpstreamPath());
                    }
                    svb.getGateways()
                            .stream()
                            .map(svcGw -> gatewayFacade.createGatewayLink(svcGw.getGatewayId()))
                            .forEach(gw -> {
                                try {
                                    KongApi api = gw.getApi(apiId);
                                    if (api == null) {
                                        gw.createApi(new KongApi().withStripUri(true)
                                                .withPreserveHost(false)
                                                .withHosts(new ArrayList<>(svb.getHostnames()))
                                                .withUris(requestPaths)
                                                .withUpstreamUrl(URIUtils.uriBackslashRemover(upstreamUrl))
                                                .withName(apiId));
                                        log.info("== API {} MISSING ON GATEWAY {}, CREATED ==", apiId, gw.getGatewayId());
                                    } else {
                                        api = compareServiceApis(api, svb, requestPaths, apiId);
                                        if (api != null) {
                                            gw.updateOrCreateApi(api);
                                            log.info("== API {} OUT OF SYNC, RESYNCED ==", apiId);
                                        } else {
                                            log.info("== NO SYNC NECESSARY FOR {} ==", apiId);
                                        }
                                    }
                                    if (svb.getUpstreamTargets().size() > 1) {
                                        log.info("== SYNCING API UPSTREAMS ==");
                                        if (gw.getServiceUpstream(apiId) == null) {
                                            gw.createServiceUpstream(svb.getService().getOrganization().getId(), svb.getService().getId(), svb.getVersion());
                                        }
                                        svb.getUpstreamTargets().forEach(target -> {
                                            gw.createOrUpdateServiceUpstreamTarget(apiId, target);
                                        });
                                        log.info("== SYNC END FOR API UPSTREAMS ==");
                                    }
                                }
                                catch (Exception ex) {
                                    log.error("== SYNC FOR {} FAILED ON GATEWAY {} ==", apiId, gw.getGatewayId());
                                    ex.printStackTrace();
                                }
                                log.info("== SYNC END FOR {} ON GATEWAY {} ==", apiId, gw.getGatewayId());
                            });
                    log.info("=== SYNC END FOR SERVICE {} ===", apiId);
                }
                catch (Exception ex) {
                    log.error("=== SYNC FAILED FOR SERVICE {} DUE TO {} ===", apiId, ex.getMessage());
                    ex.printStackTrace();
                }
            });
            log.info("==== SYNCING SERVICES END, COMPLETED IN {} ====", TimeUtil.getTimeSince(start));
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
    public void syncApplications() {
        log.info("==== SYNC APPLICATIONS START ====");
        LocalDateTime start = LocalDateTime.now();
        try {
            query.findAllApplicationVersions().forEach(avb -> {
                String appId = ConsumerConventionUtil.createAppUniqueId(avb);
                try {
                    log.info("=== SYNC START FOR APPLICATION {} ===", appId);
                    boolean appModified = false;
                    boolean needsOAuthCredId = StringUtils.isEmpty(avb.getOauthCredentialId());

                    if (avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().isEmpty()) {
                        avb.setOauthClientRedirects(new HashSet<>(Collections.singleton(PLACEHOLDER_CALLBACK_URI)));
                        appModified = true;
                    }

                    //Sync the application on the IDP
                    RealmClient idpAppClient = null;
                    try {
                        IDPClient idp = idpLinkFactory.getDefaultIDPClient();
                        if (idp.realmExists(avb.getApplication().getOrganization().getId())) {
                            idpAppClient = idp.syncClient(avb);
                        }
                        else {
                            log.error("== SYNCING APPLICATION ON IDP FAILED - ORGANIZATION REALM DOES NOT EXIST ==");
                        }
                    }
                    catch (Exception ex) {
                        log.error("== SYNC APPLICATION \"{}\" ON IDP FAILED DUE TO {}", appId, ex);
                        throw ex;
                    }

                    if (StringUtils.isEmpty(avb.getIdpClientId()) || !avb.getIdpClientId().equals(idpAppClient.getId())) {
                        avb.setIdpClientId(idpAppClient.getId());
                        appModified = true;
                    }

                    if (StringUtils.isEmpty(avb.getoAuthClientId())) {
                        avb.setoAuthClientId(appId);
                        appModified = true;
                    }
                    else if (!avb.getoAuthClientId().equals(appId)) {
                        avb.setoAuthClientId(appId);
                        appModified = true;
                    }

                    if (StringUtils.isEmpty(avb.getOauthClientSecret())) {
                        avb.setOauthClientSecret(idpAppClient.getSecret());
                        appModified = true;
                    }

                    if (StringUtils.isNotEmpty(avb.getJwtKey()) && !avb.getJwtKey().equals(appId)) {
                        avb.setJwtKey(appId);
                        appModified = true;
                    }

                    if (StringUtils.isEmpty(avb.getApikey())) {
                        avb.setApikey(apiKeyGenerator.generate());
                        appModified = true;
                    }

                    for (IGatewayLink gw : gatewayFacade.getApplicationVersionGatewayLinks(avb)) {
                        String gwId = gw.getGatewayId();
                        try {
                            KongConsumer consumer = gw.getConsumer(appId);
                            if (consumer == null) {
                                consumer = gw.createConsumer(appId);
                                log.info("== APPLICATION {} MISSING ON GATEWAY {}, CREATED ==", appId, gwId);
                            }
                            else {
                                log.info("== NO SYNC NECESSARY FOR APPLICATION {} ON GATEWAY {} ==", appId, gwId);
                            }
                            try {
                                //Sync or create OAuth2 credentials
                                KongPluginOAuthConsumerResponseList oauthCreds = gw.getConsumerOAuthCredentials(appId);
                                if (!oauthCreds.getData().isEmpty()) {
                                    oauthCreds.getData().stream()
                                            .filter(oauth -> !oauth.getRedirectUri().equals(avb.getOauthClientRedirects())
                                                    || !oauth.getClientId().equals(avb.getoAuthClientId())
                                                    || !oauth.getClientSecret().equals(avb.getOauthClientSecret()))
                                            .forEach(oauth -> {
                                                //delete the credentials that do not match the stored credentials
                                                gw.deleteOAuthConsumerPlugin(appId, oauth.getId());
                                            });
                                    oauthCreds = gw.getConsumerOAuthCredentials(appId);
                                }
                                if (oauthCreds.getData().isEmpty()) {
                                    KongPluginOAuthConsumerRequest request = new KongPluginOAuthConsumerRequest()
                                            .withClientId(avb.getoAuthClientId())
                                            .withClientSecret(avb.getOauthClientSecret())
                                            .withRedirectUri(avb.getOauthClientRedirects())
                                            .withName(appId);
                                    if (!needsOAuthCredId) {
                                        request.setId(avb.getOauthCredentialId());
                                    }
                                    KongPluginOAuthConsumerResponse response = gw.enableConsumerForOAuth(appId, request);

                                    if (needsOAuthCredId) {
                                        avb.setOauthCredentialId(response.getId());
                                        appModified = true;
                                        needsOAuthCredId = false;
                                    }
                                    log.info("= NO OAUTH CREDENTIALS FOUND FOR APPLICATION \"{}\" ON GATEWAY \"{}\", CREATED =", appId, gwId);
                                }
                                else {
                                    if (needsOAuthCredId || !avb.getOauthCredentialId().equals(oauthCreds.getData().get(0).getId())) {
                                        avb.setOauthCredentialId(oauthCreds.getData().get(0).getId());
                                        appModified = true;
                                        needsOAuthCredId = false;
                                    }
                                    log.info("= NO OAUTH SYNC NECESSARY FOR APPLICATION \"{}\" ON GATEWAY \"{}\" =", appId, gwId);
                                }
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                log.error("= OAUTH SYNC FAILED FOR APPLICATION \"{}\" ON GATEWAY \"{}\" DUE TO {} =", appId, gwId, ex.getMessage());
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
                                    log.info("= NO KEY AUTHENTICATION FOUND FOR APPLICATION \"{}\" ON GATEWAY \"{}\", CREATED =", appId, gwId);
                                } else {
                                    log.info("= NO KEY AUTHENTICATION SYNC NECESSARY FOR APPLICATION  \"{}\" ON GATEWAY \"{}\" =", appId, gwId);
                                }
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                log.error("= KEY AUTHENTICATION FAILED FOR APPLICATION \"{}\" ON GATEWAY \"{}\" DUE TO {} =", appId, gwId, ex.getMessage());
                            }

                            try {
                                String pubKey = idpLinkFactory.getDefaultIDPClient().getRealmPublicKeyInPemFormat(avb.getApplication().getOrganization());
                                if (StringUtils.isNotEmpty(pubKey)) {
                                    //Sync or create JWT credentials
                                    KongPluginJWTResponseList jwtCreds = gw.getConsumerJWT(appId);
                                    KongPluginJWTResponse jwtCred = null;
                                    if (!jwtCreds.getData().isEmpty()) {
                                        jwtCreds.getData().stream().filter(jwt -> {
                                            return !jwt.getAlgorithm().equals(JWTUtils.JWT_RS256) || !jwt.getRsaPublicKey().equals(pubKey);
                                        }).forEach(jwt -> gw.deleteConsumerJwtCredential(appId, jwt.getId()));
                                        jwtCreds = gw.getConsumerJWT(appId);
                                    }
                                    if (jwtCreds.getData().isEmpty()) {
                                        jwtCred = gw.addConsumerJWT(appId, pubKey);
                                        log.info("= NO JWT CREDENTIALS FOUND FOR APPLICATION \"{}\" ON GATEWAY \"{}\", CREATED =", appId, gwId);
                                    } else {
                                        jwtCred = jwtCreds.getData().get(0);
                                        log.info("= NO JWT SYNC NECESSARY FOR APPLICATION \"{}\" ON GATEWAY \"{}\" =", appId, gwId);
                                    }
                                }
                                else {
                                    log.error("= JWT SYNC FAILED FOR APPLICATION \"{}\" ON GATEWAY \"{}\": COULD NOT RETRIEVE IDP PUBLIC KEY", appId, gwId);
                                }
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                log.error("= JWT SYNC FAILED FOR APPLICATION \"{}\" ON GATEWAY \"{}\" DUE TO {} =", appId, gwId, ex.getMessage());
                            }

                            //TODO - Add Basic Auth once plugin/policy is enabled

                            if (appModified) {
                                storage.updateApplicationVersion(avb);
                            }
                        }
                        catch (Exception ex) {
                            log.info("== SYNC FAILED FOR APPLICATION {} ON GATEWAY {} DUE TO {} ==", appId, gwId);
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.info("=== SYNC FAILED FOR APPLICATION {} DUE TO {} ===", appId, ex.getMessage());
                }
                log.info("=== SYNC END FOR {} ===", appId);
            });
        } catch (Exception e) {
            log.error("=== SYNCHRONIZE APPLICATION FAILED DUE TO {} ===", e.getMessage());
            e.printStackTrace();
        }
        log.info("==== SYNC APPLICATIONS END, COMPLETED IN {} ====", TimeUtil.getTimeSince(start));
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
            log.error("==== SYNC POLICES FAILED DUE TO {} ====", ex.getMessage());
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

                                    KongConfigValue config = (KongConfigValue) gson.fromJson(policy.getConfiguration(), polDef.getClazz());
                                    if (polDef.equals(Policies.OAUTH2)) {
                                        ((KongPluginOAuth) config).setProvisionKey(svb.getProvisionKey());
                                    }

                                    if (policy.getKongPluginId() != null) {
                                        KongPluginConfig gwPlugin = gw.getPlugin(policy.getKongPluginId());
                                        if (gwPlugin == null) {
                                            gw.createApiPlugin(api.getId(), new KongPluginConfig()
                                                    .withId(policy.getKongPluginId())
                                                    .withName(kongPluginIdentifier)
                                                    .withConfig(config));
                                        }
                                        else {
                                            gwPlugin.setConfig(config);
                                            if (gw.updatePlugin(gwPlugin)!= null) {
                                                log.info("= POLICY {} SYNCED FOR SERVICE {} =", policy.getDefinition().getName(), serviceId);
                                            }
                                            else {
                                                log.error("= POLICY {} SYNC FOR SERVICE {} FAILED =", policy.getDefinition().getName(), serviceId);
                                            }
                                        }
                                    } else {
                                        gw.getServicePlugins(serviceId).getData().stream()
                                                .filter(plugin -> plugin.getName().equals(kongPluginIdentifier) &&
                                                        StringUtils.isEmpty(plugin.getConsumerId()))
                                                .forEach(filteredPlugin -> policy.setKongPluginId(filteredPlugin.getId()));
                                        if (policy.getKongPluginId() == null) {
                                            KongPluginConfig newPlugin = new KongPluginConfig().withApiId(api.getId()).withEnabled(policy.isEnabled())
                                                    .withConfig(config)
                                                    .withName(kongPluginIdentifier);

                                            policy.setKongPluginId(gw.createApiPlugin(serviceId, newPlugin).getId());
                                            log.info("= POLICY {} FOR SERVICE {} ON GATEWAY {} MISSING, CREATED =", policy.getDefinition().getName(), serviceId, gw.getGatewayId());
                                        }
                                    }
                                    storage.updatePolicy(policy);
                                    log.info("= SYNC END FOR {} POLICY FOR SERVICE {} =", policy.getName(), serviceId);
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                    log.error("= SYNC FAILED FOR {} POLICY FOR SERVICE {} DUE TO {} =", policy.getName(), serviceId, ex.getMessage());
                                }
                            });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("== SYNC FAiLED FOR SERVICE {} DUE TO {} ==", serviceId, ex.getMessage());
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.warn("=== SYNC SERVICES FAILED DUE TO {} ===", ex.getMessage());
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
                        if (contract == null) {
                            throw ExceptionFactory.contractNotFoundException(policy.getContractId());
                        }
                        else {
                            contracts.put(contract.getId(), contract);
                        }
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
                                } else if (gw.createApiPlugin(apiId, new KongPluginConfig()
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
                    log.error("== SYNC FAILED FOR CONTRACT POLICY {} DUE TO {} ==", policy.getName(), ex.getMessage());
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
                                log.info("= CONSENT ACL SYNCED FOR {} =", engineACLGroup);
                            }
                        } else {
                            try {
                                policy.setKongPluginId(gw.addConsumerToACL(consentAppId, engineACLGroup).getId());
                                storage.updatePolicy(policy);
                                log.info("= CONSENT ACL {} MISSING ON GATEWAY {}, CREATED =", engineACLGroup);
                            }
                            catch (StorageException ex) {
                                ex.printStackTrace();
                                log.error("= FAILED TO UPDATE KONG PLUGIN ID =");
                            }
                        }
                        log.info("== SYNC CONSENT ACL ENDED FOR {} ==", engineACLGroup);
                    }
                }
                else {
                    //TODO - Implement other consent policies if any
                    log.warn("== UNSUPPORTED CONSENT POLICY TYPE: {} ==", policy.getDefinition().getId());
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
            query.getPlanVersionContractMap().forEach((pvb, contracts) -> {
                String planId = ServiceConventionUtil.generateServiceUniqueName(pvb.getPlan().getOrganization().getId(), pvb.getPlan().getId(), pvb.getVersion());
                try {
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
                                        log.info("== CONTRACT POLICY DOES NOT EXIST FOR CONTRACT {} UNDER PLAN {} ON GATEWAY {}, CREATED ==", contract.getId(), planId, gw.getGatewayId());
                                    } else {
                                        KongPluginConfig plugin = gw.getPlugin(pol.getKongPluginId());
                                        if (plugin == null) {
                                            gw.createApiPlugin(svcId, new KongPluginConfig().withApiId(apiId).withConsumerId(consumerId).withEnabled(true).withName(polDef.getKongIdentifier()).withConfig(gson.fromJson(planPol.getConfiguration(), polDef.getClazz())));
                                            log.info("== CONTRACT POLICY MISSING FOR CONTRACT {} UNDER PLAN {} ON GATEWAY {}, CREATED ==", contract.getId(), planId, gw.getGatewayId());
                                        } else {
                                            if (gw.updatePlugin(plugin.withConfig(config)) != null) {
                                                log.info("== CONTRACT POLICY SYNCED FOR CONTRACT {} UNDER PLAN {} ON GATEWAY {} ==", contract.getId(), planId, gw.getGatewayId());
                                            } else {
                                                log.error("== CONTRACT POLICY SYNC FAILED FOR CONTRACT {} UNDER PLAN {} ON GATEWAY {} ==", contract.getId(), planId, gw.getGatewayId());
                                            }
                                        }
                                    }
                                } catch (StorageException ex) {
                                    log.error("== SYNCING PLAN POLICIES FAILED FOR CONTRACT {} ON GATEWAY {} ==", contract.getId(), gw.getGatewayId());
                                    ex.printStackTrace();
                                }
                            });
                        });
                    });
                } catch (StorageException ex) {
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
            List<OAuth2TokenBean> tokens = query.getAllOAuthTokens();
            Map<String, IGatewayLink> tokenGws = tokens.stream().map(OAuth2TokenBean::getGatewayId).distinct().collect(Collectors.toMap(gwId -> gwId, gwId -> gatewayFacade.createGatewayLink(gwId)));
            tokens.forEach(token -> syncToken(token, tokenGws.get(token.getGatewayId())));
            log.info("==== START TOKEN SYNC, COMPLETED IN {} ====", TimeUtil.getTimeSince(start));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("==== TOKEN SYNC FAILED DUE TO {} ====", ex.getMessage());
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteBackedUpTokens() {
        try {
            query.deleteAllOAuthTokens();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
    public void backUpOAuthTokens() {
        log.info("==== START TOKEN BACKUP ====");
        LocalDateTime start = LocalDateTime.now();
        try {
            deleteBackedUpTokens();
            query.getAllGateways().stream().map(gatewayBean -> gatewayFacade.createGatewayLink(gatewayBean.getId())).forEach(gateway -> {
                KongOAuthTokenList tokenList = gateway.getAllOAuth2Tokens(null);
                long total = tokenList.getTotal().longValue();
                long i = 0;
                tokenList.getData().stream().map(oauthToken -> new OAuth2TokenBean(oauthToken, gateway.getGatewayId())).forEach(this::updateOrCreateToken);
                while (tokenList.getOffset() != null && i < total/100) {
                    tokenList = gateway.getAllOAuth2Tokens(tokenList.getOffset());
                    tokenList.getData().stream().map(oauthToken -> new OAuth2TokenBean(oauthToken, gateway.getGatewayId())).forEach(this::updateOrCreateToken);
                    i++;
                }
            });
            log.info("==== TOKEN BACKUP COMPLETED, {} TOKENS IN {}", query.getOAuth2TokenCount(), TimeUtil.getTimeSince(start));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("==== TOKEN BACKUP FAILED DUE TO {} ====", ex.getMessage());
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

    private KongApi compareServiceApis(KongApi api, ServiceVersionBean svb, List<String> requestPaths, String apiId) {
        final String upstreamUrl;
        if (svb.getUpstreamTargets().size() == 1) {
            ServiceUpstreamTargetBean target = svb.getUpstreamTargets().stream().collect(CustomCollectors.getFirstResult());
            upstreamUrl = URIUtils.buildEndpoint(svb.getUpstreamScheme(), target.getTarget(), target.getPort(), svb.getUpstreamPath());
        }
        else {
            upstreamUrl = URIUtils.buildEndpoint(svb.getUpstreamScheme(), apiId, null, svb.getUpstreamPath());
        }
        if (!api.getUpstreamUrl().equals(upstreamUrl) ||
                !new TreeSet<>(api.getHosts()).equals(new TreeSet<>(svb.getHostnames()))||
                !new TreeSet<>(api.getUris()).equals(new TreeSet<>(requestPaths)) ||
                api.getPreserveHost() ||
                !api.getStripUri()) {
            return api.withUris(requestPaths)
                    .withHosts(new ArrayList<>(svb.getHostnames()))
                    .withUris(requestPaths)
                    .withUpstreamUrl(upstreamUrl)
                    .withPreserveHost(false)
                    .withStripUri(true);
        }
        else {
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void syncToken(OAuth2TokenBean token, IGatewayLink gatewayLink) {
        IGatewayLink gw = gatewayLink;
        if (gw == null) {
            gw = gatewayFacade.createGatewayLink(token.getGatewayId());
        }
        try {
            if (gw.createOAuthToken(token) == null) {
                log.info("== SYNC FOR TOKEN {} NOT NECESSARY ==", token.getAccessToken());
            }
            storage.deleteOAuth2Token(token);
        }
        catch (Exception ex) {
            log.error("=== FAILED TO SYNC TOKEN {} ===", token.getAccessToken());
            ex.printStackTrace();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void updateOrCreateToken(OAuth2TokenBean token) {
        try {
            storage.updateOAuth2TokenBean(token);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to backup token: {}", ex.getMessage());
        }
    }
}