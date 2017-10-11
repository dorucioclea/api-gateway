package com.t1t.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.apim.beans.gateways.Gateway;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.services.ServiceUpstreamTargetBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ServiceVersionSummaryBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.BrandingNotAvailableException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.gateway.GatewayAuthenticationException;
import com.t1t.apim.gateway.dto.*;
import com.t1t.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.apim.rest.KongClient;
import com.t1t.apim.rest.adapters.KongSafeTypeAdapterFactory;
import com.t1t.kong.model.*;
import com.t1t.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A REST client for accessing the Gateway API.
 * TODO ACL groups cannot be mixed through API => unique naming
 */
public class GatewayClient {
    private static final String AUTH_API_KEY = "apikey";
    private static final String DUMMY_UPSTREAM_URI = "http://localhost:3000";
    private static Logger log = LoggerFactory.getLogger(GatewayClient.class.getName());
    private KongClient httpClient;
    private GatewayBean gatewayBean;
    private IStorage storage;
    private AppConfig appConfig;
    private GatewayValidation gatewayValidation;
    private Gson gson;

    /**
     * Constructor.
     *
     * @param httpClient the http client
     */
    public GatewayClient(KongClient httpClient, GatewayBean gateway, IStorage storage, AppConfig appConfig, GatewayValidation gatewayValidation) {
        Preconditions.checkNotNull(httpClient);
        Preconditions.checkNotNull(storage);
        Preconditions.checkNotNull(gateway);
        Preconditions.checkNotNull(appConfig);
        this.httpClient = httpClient;
        this.gatewayBean = gateway;
        this.storage = storage;
        this.appConfig = appConfig;
        this.gatewayValidation = gatewayValidation;
        this.gson = new GsonBuilder().registerTypeAdapterFactory(new KongSafeTypeAdapterFactory()).create();
    }

    public SystemStatus getStatus() throws GatewayAuthenticationException {
        KongExtraInfo kongInfo = httpClient.getInfo();
        KongInfo kongInformation = httpClient.getParsedInfo();
        Object kongStatus = httpClient.getStatus();
        Object kongCluster = httpClient.getCluster();
        //Remove NginX Config & SSL Cert/Key path from KongInfo
        kongInfo.getConfiguration().setSslCert(null);
        kongInfo.getConfiguration().setSslCertCsrDefault(null);
        kongInfo.getConfiguration().setSslCertKey(null);
        kongInfo.getConfiguration().setSslCertKeyDefault(null);
        kongInfo.getConfiguration().setSslCertDefault(null);
        kongInfo.getConfiguration().setNginxConf(null);
        kongInfo.getConfiguration().setNginxKongConf(null);
        SystemStatus systemStatus = new SystemStatus();
        systemStatus.setDescription(kongInformation.getTagline());
        systemStatus.setVersion(kongInformation.getVersion());
        systemStatus.setName(kongInformation.getHostname());
        systemStatus.setId(kongInformation.getHostname());
        systemStatus.setInfo(gson.toJson(kongInfo));
        systemStatus.setStatus(gson.toJson(kongStatus));
        systemStatus.setCluster(gson.toJson(kongCluster));
        systemStatus.setUp(true);
        return systemStatus;
    }

    public ServiceEndpoint getServiceEndpoint(Set<String> basePaths, String organizationId, String serviceId, String version) throws GatewayAuthenticationException {
        ServiceEndpoint endpoint = new ServiceEndpoint();
        StringBuilder url = new StringBuilder();
        url.append(gatewayBean.getEndpoint());
        Service service = new Service();
        service.setOrganizationId(organizationId);
        service.setServiceId(serviceId);
        service.setVersion(version);
        service.setBasepaths(basePaths);
        //TODO set basepath
        url.append(GatewayPathUtilities.generateGatewayContextPath(service));
        endpoint.setEndpoint(url.toString());
        return endpoint;

    }

    /**
     * Register an application is:
     * <ul>
     * <li>create the consumer (app.version)</li>
     * <li>create an acl (org.app.version.plan.version) and add to api</li>
     * <li>add acl group to the whitelist of the api</li>
     * <li>add client application to the group</li>
     * <li>add keyauth with API key for specific API and consumer</li>
     * </ul>
     * <p>
     * Each plan should contain the following policies:
     * <ul>
     * <li>HTTP Log configured for the metrics engine for each service consumer</li>
     * <li>Key authentication</li>
     * </ul>
     * <p>
     * The following policies can be part of a plan:
     * <ul>
     * <li>IP Restriction</li>
     * <li>Rate Limiting</li>
     * <li>Request size limiting</li>
     * </ul>
     *
     * @param application
     * @throws RegistrationException
     * @throws GatewayAuthenticationException
     */
    public Map<Contract, KongPluginConfigList> register(Application application) throws RegistrationException, GatewayAuthenticationException {
        Map<Contract, KongPluginConfigList> rval = new HashMap<>();
        //create consumer
        String consumerId = ConsumerConventionUtil.createAppUniqueId(application.getOrganizationId(), application.getApplicationId(), application.getVersion());
        KongConsumer consumer = httpClient.getConsumer(consumerId);

        KongApi api;
        //context of API
        for (Contract contract : application.getContracts()) {
            //get the API
            String apiName = ServiceConventionUtil.generateServiceUniqueName(contract.getServiceOrgId(), contract.getServiceId(), contract.getServiceVersion());
            api = httpClient.getApi(apiName);
            List<KongPluginConfig> data = new ArrayList<>();
            for (Policy policy : contract.getPolicies()) {
                //execute policy
                Policies polDef = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                //Check if there isn't already a policy on the gateway for that contract
                KongPluginConfigList plugins = getConsumerSpecificApiPlugins(consumer.getId(), api.getId());
                KongPluginConfig plugin = null;
                if (plugins != null && !plugins.getData().isEmpty()) {
                    plugin = plugins.getData().stream().filter(plgn -> plgn.getName().equals(polDef.getKongIdentifier())).collect(CustomCollectors.getSingleResult());
                }
                if (plugin == null) {
                    plugin = createPlanPolicy(api, consumer, policy, polDef.getKongIdentifier(), polDef.getClazz());
                }
                data.add(plugin);
            }
            KongPluginConfigList plugins = new KongPluginConfigList();
            plugins.setData(data);
            rval.put(contract, plugins);
        }
        return rval;
    }

    public void registerAppConsumer(Application application, KongConsumer appConsumer) {
        //register consumer application
        //for each API register consumer on API
        KongApi api;
        //context of API
        for (Contract contract : application.getContracts()) {
            //get the API
            String apiName = ServiceConventionUtil.generateServiceUniqueName(contract.getServiceOrgId(), contract.getServiceId(), contract.getServiceVersion());
            api = httpClient.getApi(apiName);
            for (Policy policy : contract.getPolicies()) {
                //execute policy
                Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                switch (policies) {
                    //all policies can be available here
                    case IPRESTRICTION:
                        createPlanPolicy(api, appConsumer, policy, Policies.IPRESTRICTION.getKongIdentifier(), Policies.IPRESTRICTION.getClazz());
                        break;
                    case RATELIMITING:
                        createPlanPolicy(api, appConsumer, policy, Policies.RATELIMITING.getKongIdentifier(), Policies.RATELIMITING.getClazz());
                        break;
                    case REQUESTSIZELIMITING:
                        createPlanPolicy(api, appConsumer, policy, Policies.REQUESTSIZELIMITING.getKongIdentifier(), Policies.REQUESTSIZELIMITING.getClazz());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void unregister(String organizationId, String applicationId, String version) throws RegistrationException, GatewayAuthenticationException {
        //remove the application consumer and it's keyauth credentials
        String consumerId = ConsumerConventionUtil.createAppUniqueId(organizationId, applicationId, version);
        KongPluginKeyAuthResponseList credentials = httpClient.getConsumerKeyAuthCredentials(consumerId);
        if (credentials != null && credentials.getData() != null && credentials.getData().size() > 0) {
            for (KongPluginKeyAuthResponse cred : credentials.getData())
                httpClient.deleteConsumerKeyAuthCredential(consumerId, cred.getId());
        }
        //remove application consumer - all other policies and credentials will be removed as well
        httpClient.deleteConsumer(consumerId);
    }

    /**
     * Publishes a dummy endpoint on the given gateway that can be used as a single point of OAuth 2 Authorization.
     * The endpoint is merely a service endpoint with dummy upstream, where OAuth policy applies.
     * The endpoint is prefixed by a gateway context, this can only be used when services with oauth enabled policies are
     * registered after the given gateway path.
     * The dummy endpoint should be kept updated with all published and prefixed scopes of endpoints.
     * <p>
     * Introducing this centralized OAuth endpoint is complementary to the Oauth endpoints defined on service level.
     *
     * @throws PublishingException
     */
    public void publishGatewayOAuthEndpoint(Gateway gtw) throws PublishingException {
        KongApi api = new KongApi();
        api.setStripUri(true);
        api.setName(gtw.getId().toLowerCase());
        if (gtw.getOauthBasePath().startsWith("/")) api.setUris(Collections.singletonList(gtw.getOauthBasePath()));
        else api.setUris(Collections.singletonList("/" + gtw.getOauthBasePath()));
        api.setUpstreamUrl(DUMMY_UPSTREAM_URI);
        log.info("Initialize oauth for gateway to Kong:{}", api.toString());
        //safe publish
        api = publishAPIWithFallback(api);
        //apply OAuth policy
        registerDefaultOAuthPolicy(api);
    }

    /**
     * Updates the OAuth2 central endpoint token expiration time.
     *
     * @param gtw
     * @param expirationTimeSeconds
     */
    public void updateOAuth2ExpirationForCentralOAuth(GatewayBean gtw, Integer expirationTimeSeconds) {
        final KongPluginConfigList gtwPluginConfigList = httpClient.getKongPluginConfig(gtw.getId().toLowerCase(), Policies.OAUTH2.getKongIdentifier());
        if (gtwPluginConfigList != null && gtwPluginConfigList.getData().size() > 0) {
            KongPluginConfig gtwPluginConfig = gtwPluginConfigList.getData().get(0);
            KongPluginOAuthEnhanced gtwOAuthValue = gson.fromJson(gson.toJson(gtwPluginConfig.getConfig()),
                    KongPluginOAuthEnhanced.class);
            gtwOAuthValue.setTokenExpiration(expirationTimeSeconds.longValue());
            gtwPluginConfig.setConfig(gtwOAuthValue);
            KongPluginConfig updatedConfig = new KongPluginConfig()
                    .withId(gtwPluginConfig.getId())
                    .withName(Policies.OAUTH2.getKongIdentifier())
                    .withConfig(gtwPluginConfig.getConfig())
                    .withCreatedAt(gtwPluginConfig.getCreatedAt());
            httpClient.updateOrCreatePluginConfig(gtw.getId().toLowerCase(), updatedConfig);
        }
    }

    /**
     * Returns the central OAuth configuration for a gateway.
     *
     * @param gtw
     * @return
     */
    public KongPluginOAuthEnhanced getOAuth2ExpirationForCentralOAuth(GatewayBean gtw) {
        final KongPluginConfigList gtwPluginConfigList = httpClient.getKongPluginConfig(gtw.getId().toLowerCase(), Policies.OAUTH2.getKongIdentifier());
        if (gtwPluginConfigList != null && gtwPluginConfigList.getData().size() > 0) {
            KongPluginConfig gtwPluginConfig = gtwPluginConfigList.getData().get(0);
            KongPluginOAuthEnhanced gtwOAuthValue = gson.fromJson(gson.toJson(gtwPluginConfig.getConfig()),
                    KongPluginOAuthEnhanced.class);
            return gtwOAuthValue;
        } else return null;
    }

    public void addGatewayOAuthScopes(GatewayBean gtw, KongApi api) {
        final KongPluginConfigList gtwPluginConfigList = httpClient.getKongPluginConfig(gtw.getId().toLowerCase(), Policies.OAUTH2.getKongIdentifier());
        if (gtwPluginConfigList != null && gtwPluginConfigList.getData().size() > 0) {
            KongPluginConfig gtwPluginConfig = gtwPluginConfigList.getData().get(0);
            KongPluginOAuthEnhanced gtwOAuthValue = gson.fromJson(gson.toJson(gtwPluginConfig.getConfig()),
                    KongPluginOAuthEnhanced.class);
            //get oauth scopes from api
            final KongPluginConfigList apiPluginConfigList = httpClient.getKongPluginConfig(api.getId(), Policies.OAUTH2.getKongIdentifier());
            if (apiPluginConfigList != null && apiPluginConfigList.getData().size() > 0) {
                KongPluginConfig apiPluginConfig = apiPluginConfigList.getData().get(0);
                KongPluginOAuthEnhanced apiOAuthValue = gson.fromJson(
                        gson.toJson(apiPluginConfig.getConfig()),
                        KongPluginOAuthEnhanced.class);
                if (!apiOAuthValue.getScopes().isEmpty()) {
                    Set<String> gtwScopes = objectListToStringSet(gtwOAuthValue.getScopes());
                    Set<String> apiScopes = objectListToStringSet(apiOAuthValue.getScopes());
                    log.debug("-->gateway socpe collection:{}", gtwScopes);
                    log.debug("-->api scopes to resolve:{}", apiScopes);
                    gtwScopes.addAll(apiScopes);
                    gtwOAuthValue.setScopes(stringSetToObjectList(gtwScopes));
                    gtwPluginConfig.setConfig(gtwOAuthValue);
                    KongPluginConfig updatedConfig = new KongPluginConfig()
                            .withId(gtwPluginConfig.getId())
                            .withName(Policies.OAUTH2.getKongIdentifier())
                            .withConfig(gtwPluginConfig.getConfig())
                            .withCreatedAt(gtwPluginConfig.getCreatedAt());
                    httpClient.updateOrCreatePluginConfig(gtw.getId().toLowerCase(), updatedConfig);
                }
            }
        }
    }

    /**
     * @param initlist
     * @return
     */
    public Set<String> objectListToStringSet(List<Object> initlist) {
        Set<String> resultList = new TreeSet<>();
        if (initlist != null && initlist.size() > 0) {
            initlist.forEach(obj -> resultList.add(obj.toString()));
        }
        return resultList;
    }

    public List<Object> stringSetToObjectList(Set<String> initlist) {
        List<Object> resultList = new ArrayList<>();
        if (initlist != null && initlist.size() > 0) {
            initlist.forEach(obj -> resultList.add(obj.toString()));
        }
        return resultList;
    }

    public void removeGatewayOAuthScopes(GatewayBean gtw, KongApi api) {
        KongPluginConfigList gtwPluginConfigList = null;
        try {
            gtwPluginConfigList = httpClient.getKongPluginConfig(gtw.getId().toLowerCase(), Policies.OAUTH2.getKongIdentifier());
        } catch (RetrofitError ex) {
            log.debug("Error retrieving oauth2 configs:{}", ex);
        }
        if (gtwPluginConfigList != null && gtwPluginConfigList.getData().size() > 0) {
            KongPluginConfig gtwPluginConfig = gtwPluginConfigList.getData().get(0);
            KongPluginOAuthEnhanced gtwOAuthValue = gson.fromJson(gson.toJson(gtwPluginConfig.getConfig()),
                    KongPluginOAuthEnhanced.class);
            //get oauth scopes from api
            final KongPluginConfigList apiPluginConfigList = httpClient.getKongPluginConfig(api.getId(), Policies.OAUTH2.getKongIdentifier());
            if (apiPluginConfigList != null && apiPluginConfigList.getData().size() > 0) {
                KongPluginConfig apiPluginConfig = apiPluginConfigList.getData().get(0);
                KongPluginOAuthEnhanced apiOAuthValue = gson.fromJson(
                        gson.toJson(apiPluginConfig.getConfig()),
                        KongPluginOAuthEnhanced.class);
                if (!apiOAuthValue.getScopes().isEmpty()) {
                    Set<String> gtwScopes = objectListToStringSet(gtwOAuthValue.getScopes());
                    Set<String> apiScopes = objectListToStringSet(apiOAuthValue.getScopes());
                    log.debug("-->gateway socpe collection:{}", gtwScopes);
                    log.debug("-->api scopes to resolve:{}", apiScopes);
                    apiScopes.forEach(scope -> {
                        log.info("trying to remove:{}", scope);
                        gtwScopes.remove(scope);
                        log.info("after fremovel:{}", gtwScopes);
                    });
                    gtwOAuthValue.setScopes(stringSetToObjectList(gtwScopes));
                    gtwPluginConfig.setConfig(gtwOAuthValue);
                    KongPluginConfig updatedConfig = new KongPluginConfig()
                            .withId(gtwPluginConfig.getId())
                            .withName(Policies.OAUTH2.getKongIdentifier())
                            .withConfig(gtwPluginConfig.getConfig())
                            .withCreatedAt(gtwPluginConfig.getCreatedAt());
                    httpClient.updateOrCreatePluginConfig(gtw.getId().toLowerCase(), updatedConfig);
                }
            }
        }
    }

    private KongApi publishAPIWithFallback(KongApi api) {
        try {
            //If service exists already on the gateway due to an invalid action before, delete the existing with the same name (republish)
            KongApi existingAPI = httpClient.getApi(api.getName());
            if (existingAPI != null && !StringUtils.isEmpty(existingAPI.getId())) {
                //the API exists already - please override - this is restricted due to the naming convention
                httpClient.deleteApi(existingAPI.getId());
            }
        } catch (Exception ex) {
            //start new client to comm with kong
            log.info("Warning during service publication: {}", ex.getMessage());
            //continue
        }
        return createApi(api);
    }


    /**
     * Publishes an API to the kong gateway.
     * The properties path and target_url are variable, and will be retrieved from the service dto.
     * The properties strip_path, preserve_host and public_dns will be set equally for all registered endpoints
     * The path should be: /organizationname/applicationname/version
     * <p>
     * Default enable:
     * <ul>
     * <li>Key authentication</li>
     * <li>CORS</li></loi>
     * </ul>
     *
     * @param service
     * @return the published service
     * @throws PublishingException
     * @throws GatewayAuthenticationException
     */
    public Service publish(Service service) throws PublishingException, GatewayAuthenticationException {
        Preconditions.checkNotNull(service);
        //create the service using path, and target_url
        KongApi api = new KongApi();
        api.setStripUri(true);

        //api.setPublicDns();
        String name = ServiceConventionUtil.generateServiceUniqueName(service);
        //name wil be: organization.application.version
        api.setName(name);
        //version wil be: organization.application.version
        api.setHosts(service.getHosts() == null ? Collections.emptyList() : new ArrayList<>(service.getHosts()));
        //real URL to target
        String upstreamUri = null;
        if (service.getUpstreamTargets().size() > 1) {
            upstreamUri = URIUtils.buildEndpoint(service.getUpstreamScheme(), name, null, service.getUpstreamPath());
        } else if (service.getUpstreamTargets().size() == 1) {
            upstreamUri = URIUtils.buildEndpoint(service.getUpstreamScheme(), service.getUpstreamTargets().get(0).getTarget(), service.getUpstreamTargets().get(0).getPort(), service.getUpstreamPath());
        }
        api.setUpstreamUrl(upstreamUri);
        //context path that will be stripped away
        api.setUris(validateServicePath(service));
        log.info("Send to Kong:{}", api.toString());

        //safe publish API
        api = publishAPIWithFallback(api);

        //verify if api creation has been succesfull
        if (!StringUtils.isEmpty(api.getId())) {
            try {
                List<Policy> policyList = service.getServicePolicies();
                for (Policy policy : policyList) {
                    //execute policy
                    createServicePolicy(service.getOrganizationId(), service.getServiceId(), service.getVersion(), policy);
                }
                //Create the upstream and targets & check if the endpoint host has been replace with a virtual host
                if (service.getUpstreamTargets().size() > 1) {
                    createServiceUpstream(service.getOrganizationId(), service.getServiceId(), service.getVersion());
                    for (ServiceUpstreamTargetBean target : service.getUpstreamTargets()) {
                        createOrUpdateServiceUpstreamTargets(name, target);
                    }
                }
            } catch (Exception e) {
                //if anything goes wrong, return exception and rollback api created
                if (api != null && !StringUtils.isEmpty(api.getId())) {
                    httpClient.deleteApi(api.getId());
                }
                throw new SystemErrorException(e);
            }
        }

        //Create branding APIs
        publishServiceBrandings(service);

        return service;
    }

    public void deleteAPI(String apiName) {
        KongApi api = httpClient.getApi(apiName);
        if (api != null && !StringUtils.isEmpty(api.getId())) {
            httpClient.deleteApi(api.getId());
        }
    }

    private void publishServiceBrandings(Service service) {
        for (String branding : service.getBrandings()) {
            try {
                publishServiceBranding(service, branding);
            } catch (BrandingNotAvailableException ex) {

            }
        }
    }

    public void publishServiceBranding(Service service, String branding) {
        String managedEndpoint = (gatewayBean.getEndpoint().endsWith("\\") ? gatewayBean.getEndpoint().substring(0, gatewayBean.getEndpoint().length() - 1) : gatewayBean.getEndpoint()) +
                GatewayPathUtilities.generateGatewayContextPath(service);
        String brandingNameAndDNS = ServiceConventionUtil.generateServiceUniqueName(branding, service.getServiceId(), service.getVersion());

        KongApi brandingApi = new KongApi()
                .withName(brandingNameAndDNS)
                .withHosts(Collections.singletonList(brandingNameAndDNS))
                .withStripUri(true)
                .withUris(GatewayPathUtilities.generateGatewayContextPath(branding, service.getBasepaths(), service.getVersion()))
                .withUpstreamUrl(managedEndpoint);
        publishAPIWithFallback(brandingApi);
    }

    /**
     * After applying the OAuth2 plugin to a service the following action gets executed.
     * In case of OAuth2 - add provision_key to the service version, and additional scopes.
     *
     * @param policy
     */
    public void postOAuth2Actions(String organizationId, String serviceId, String version, Policy policy, KongPluginConfig config) {
        Preconditions.checkNotNull(organizationId);
        Preconditions.checkNotNull(serviceId);
        Preconditions.checkNotNull(version);
        Preconditions.checkNotNull(policy);
        Preconditions.checkNotNull(config);
        //config contains provisioning and scopes
        try {
            //retrieve scope info from policy json
            KongPluginOAuth oauthValue = gson.fromJson(policy.getPolicyJsonConfig(), KongPluginOAuth.class);//original request - we need this for the scope descriptions
            //TODO Temp quick fix: kong response oauth plugin returns empty scopes as {} object, while it returns filled in scope as array []
            String conf = gson.toJson(config.getConfig());
            KongPluginOAuthEnhanced enhancedOAuthValue = gson.fromJson(conf, KongPluginOAuthEnhanced.class);//response from Kong - we need this for the provisioning key
            log.info("Response after applying oauth on API:{}", enhancedOAuthValue);
            ServiceVersionBean svb = storage.getServiceVersion(organizationId, serviceId, version);
            svb.setProvisionKey(enhancedOAuthValue.getProvisionKey());
            Map<String, String> scopeMap = new HashMap<>();
            List<KongPluginOAuthScope> scopeObjects = oauthValue.getScopes();
            for (KongPluginOAuthScope scope : scopeObjects) {
                scopeMap.put(scope.getScope(), scope.getScopeDesc());
            }
            svb.setOauthScopes(scopeMap);
            storage.updateServiceVersion(svb);
        } catch (StorageException e) {
            throw new GatewayException("Error update service version bean with OAuth2 info:" + e.getMessage());
        }
    }

    public void postOAuth2Actions(KongPluginOAuth engineConfig, String provisionKey, String api) {
        try {
            ServiceVersionSummaryBean svsb = ServiceConventionUtil.getServiceVersionSummaryFromUniqueName(api);
            ServiceVersionBean svb = storage.getServiceVersion(svsb.getOrganizationId(), svsb.getId(), svsb.getVersion());
            svb.setProvisionKey(provisionKey);
            Map<String, String> scopeMap = new HashMap<>();
            List<KongPluginOAuthScope> scopeObjects = engineConfig.getScopes();
            for (KongPluginOAuthScope scope : scopeObjects) {
                scopeMap.put(scope.getScope(), scope.getScopeDesc());
            }
            svb.setOauthScopes(scopeMap);
            storage.updateServiceVersion(svb);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    private void registerDefaultOAuthPolicy(KongApi api) {
        KongPluginOAuthEnhanced oauthPlugin = new KongPluginOAuthEnhanced();
        oauthPlugin.setEnableAuthorizationCode(true);
        oauthPlugin.setEnableClientCredentials(true);
        oauthPlugin.setEnableImplicitGrant(true);
        oauthPlugin.setEnablePasswordGrant(true);
        oauthPlugin.setHideCredentials(false);
        oauthPlugin.setMandatoryScope(true);
        oauthPlugin.setScopes(new ArrayList<>());
        oauthPlugin.setTokenExpiration(new Long(7200));
        oauthPlugin.setProvisionKey(UUID.randomUUID().toString());
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.OAUTH2.getKongIdentifier())
                .withConfig(oauthPlugin);
        httpClient.createPluginConfig(api.getId(), config);
    }

    /**
     * Validates the service path property before passing it to Kong.
     *
     * @param service
     * @return
     */
    private List<String> validateServicePath(Service service) {
        return GatewayPathUtilities.generateGatewayContextPath(service);
    }

    /**
     * The retire functionality removes the api and deletes the policies that are applied on the api.
     * The policies are removed as a responsability of Kong gateway.
     *
     * @param service
     * @throws RegistrationException
     * @throws GatewayAuthenticationException
     */
    public void retire(Service service) throws RegistrationException, GatewayAuthenticationException {
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getOrganizationId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getServiceId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getVersion()));
        //create the service using path, and target_url
        String nameAndDNS = ServiceConventionUtil.generateServiceUniqueName(service.getOrganizationId(), service.getServiceId(), service.getVersion());
        Set<String> kongApiNames = new HashSet<>();
        kongApiNames.add(nameAndDNS);
        if (service.getBrandings() != null && !service.getBrandings().isEmpty()) {
            kongApiNames.addAll(service.getBrandings().stream().map(branding -> ServiceConventionUtil.generateServiceUniqueName(branding, service.getServiceId(), service.getVersion())).collect(Collectors.toSet()));
        }
        kongApiNames.stream().forEach(name -> {
            KongApi existingApi = httpClient.getApi(name);
            if (existingApi != null && !StringUtils.isEmpty(existingApi.getId())) {
                httpClient.deleteApi(existingApi.getId());
            }
        });
        if (service.isCustomLoadBalancing()) {
            httpClient.deleteKongUpstream(nameAndDNS);
        }
    }

    public KongConsumer getConsumer(String id) {
        KongConsumer responseConsumer = new KongConsumer();
        try {
            responseConsumer = httpClient.getConsumer(id);
            return responseConsumer;
        } catch (RetrofitError err) {
            //user is not existing
            return null;
        }
    }

    public KongConsumer createConsumer(String userUniqueName) {
        return httpClient.createConsumer(new KongConsumer().withUsername(userUniqueName).withCustomId(userUniqueName));
    }

    public KongConsumer createConsumerWithCustomID(String customId) {
        return httpClient.createConsumer(new KongConsumer().withCustomId(customId));
    }

    public KongConsumer createConsumer(String userUniqueId, String customId) {
        return httpClient.createConsumer(new KongConsumer().withUsername(userUniqueId).withCustomId(customId));
    }

    public KongConsumer createConsumerWithKongID(String kongId, String customId) {
        return httpClient.createConsumer(new KongConsumer().withId(kongId).withCustomId(customId));
    }

    public KongPluginKeyAuthResponseList getConsumerKeyAuth(String id) {
        return httpClient.getConsumerKeyAuthCredentials(id);
    }

    public KongPluginKeyAuthResponse createConsumerKeyAuth(String id) {
        return httpClient.createConsumerKeyAuthCredentials(id, new KongPluginKeyAuthRequest());
    }

    public KongPluginKeyAuthResponse createConsumerKeyAuth(String id, String apiKey) {
        return httpClient.createConsumerKeyAuthCredentials(id, new KongPluginKeyAuthRequest().withKey(apiKey));
    }

    public KongPluginJWTResponse createConsumerJWT(String consumerName, String publicRsaKey) {
        KongPluginJWTRequest jwtRequest = new KongPluginJWTRequest()
                .withKey(consumerName)
                .withAlgorithm(JWTUtils.JWT_RS256)
                .withRsaPublicKey(publicRsaKey);
        return httpClient.createConsumerJWTCredentials(consumerName, jwtRequest);
    }

    public KongPluginJWTResponseList getConsumerJWT(String id) {
        return httpClient.getConsumerJWTCredentials(id);
    }

    public KongPluginConfigList getServicePlugins(String serviceId) {
        return httpClient.getKongPluginConfigList(serviceId);
    }

    public KongPluginConfigList getServicePlugin(String serviceId, String pluginId) {
        return httpClient.getKongPluginConfig(serviceId, pluginId);
    }

    public void deleteConsumerKeyAuth(String id, String apikey) {
        //get all registered api key values for a consumer
        KongPluginKeyAuthResponseList keyAuthCredentials = httpClient.getConsumerKeyAuthCredentials(id);
        if (keyAuthCredentials != null & keyAuthCredentials.getData() != null && keyAuthCredentials.getData().size() > 0) {
            for (KongPluginKeyAuthResponse cred : keyAuthCredentials.getData()) {
                if (cred.getKey().equals(apikey)) httpClient.deleteConsumerKeyAuthCredential(id, cred.getId());
            }
        }
    }

    public KongPluginBasicAuthResponse createConsumerBasicAuth(String userId, String userLoginName, String userPassword) {
        return httpClient.createConsumerBasicAuthCredentials(userId, new KongPluginBasicAuthRequest().withUsername(userLoginName).withPassword(userPassword));
    }

    public KongPluginBasicAuthResponseList getConsumerBasicAuth(String id) {
        return httpClient.getConsumerBasicAuthCredentials(id);
    }

    public KongApi getApi(String id) {
        try {
            return httpClient.getApi(id);
        } catch (RetrofitError err) {
            //expected := no api found with given id
            return null;
        }
    }

    public void deleteConsumer(String id) {
        httpClient.deleteConsumer(id);
    }

    public KongPluginOAuthConsumerResponse enableConsumerForOAuth(String consumerId, KongPluginOAuthConsumerRequest request) {
        return httpClient.enableOAuthForConsumer(consumerId, request.getName(), request.getClientId(), request.getClientSecret(), request.getRedirectUri());
    }

    public KongPluginOAuthConsumerResponse updateConsumerOAuthCredentials(String consumerId, KongPluginOAuthConsumerRequest request) {
        return httpClient.updateConsumerOAuthCredentials(consumerId, request);
    }

    public KongPluginKeyAuthResponse updateConsumerKeyAuthCredentials(String consumerId, String oldApiKey, String newApiKey) {
        KongPluginKeyAuthResponseList plugins = httpClient.getConsumerKeyAuthCredentials(consumerId);
        for (KongPluginKeyAuthResponse credentials : plugins.getData()) {
            if (credentials.getKey().equals(oldApiKey)) {
                httpClient.deleteConsumerKeyAuthCredential(consumerId, credentials.getId());
            }
        }
        return createConsumerKeyAuth(consumerId, newApiKey);
    }

    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformation(String clientId) {
        return httpClient.getApplicationOAuthInformation(clientId);
    }

    public KongPluginOAuthConsumerResponseList getConsumerOAuthCredentials(String consumerId) {
        return httpClient.getConsumerOAuthCredentials(consumerId);
    }

    public void deleteOAuthConsumerCredential(String consumerId, String oauthPluginId) {
        httpClient.deleteOAuth2Credential(consumerId, oauthPluginId);
    }

    /**
     * Enables ACL on a service and adds the service unique name to the group whitelist
     *
     * @param service the service on wich to enable ACL
     * @return
     */
    public KongPluginConfig createACLPlugin(Service service) {
        String serviceDNS = ServiceConventionUtil.generateServiceUniqueName(service);
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.ACL.getKongIdentifier())
                .withConfig(new KongPluginACL()
                        .withWhitelist(Arrays.asList(serviceDNS)));
        return httpClient.createPluginConfig(serviceDNS, config);
    }

    /**
     * Adds a consumer to a service's ACL.
     * Recovers when the acl is already placed on the consumer. When that's the case, the ACL will
     * be retrieved from the consumer's ACL list in order to return and update the ACL id.
     *
     * @param consumerId       the consumer to add to an ACL
     * @param serviceVersionId the service ACL to which the consumer should be added
     * @return
     */
    public KongPluginACLResponse addConsumerToACL(String consumerId, String serviceVersionId) {
        try {
            return httpClient.addConsumerToACL(consumerId, new KongPluginACLRequest().withGroup(serviceVersionId));
        } catch (RetrofitError rfe) {
            //it's possible that the group already exists - try to recover
            List<KongPluginACLResponse> consumerACLs = httpClient.getConsumerACLs(consumerId).getData();
            for (KongPluginACLResponse acl : consumerACLs) {
                if (acl.getGroup().equalsIgnoreCase(serviceVersionId)) return acl;
            }
            //if not recovered throw error
            throw rfe;
        }
    }

    /**
     * Removes a consumer from a service's ACL
     *
     * @param consumerId the consumer to remove from an ACL
     * @param pluginId   the pluginId corresponding to the consumer's ACL membership
     */
    public void deleteConsumerACLPlugin(String consumerId, String pluginId) {
        httpClient.deleteConsumerACLEntry(consumerId, pluginId);
    }
    /*Service policies*/

    /**
     * This method creates the policy on given api with pre-defined values.
     *
     * @param apiId
     * @param policy
     * @param kongIdentifier
     * @param clazz
     * @param <T>
     */
    private <T extends KongConfigValue> KongPluginConfig createServicePolicyInternal(String apiId, Policy policy, String kongIdentifier, Class<T> clazz) throws PublishingException {
        return createServicePolicyInternal(apiId, policy.getPolicyJsonConfig(), kongIdentifier, clazz);
    }

    private <T extends KongConfigValue> KongPluginConfig createServicePolicyInternal(String apiId, String policyJsonConfig, String kongIdentifier, Class<T> clazz) throws PublishingException {
        //perform value mapping
        KongConfigValue plugin = gson.fromJson(policyJsonConfig, clazz);
        KongPluginConfig config = new KongPluginConfig()
                .withName(kongIdentifier)//set required kong identifier
                .withConfig(plugin);
        //TODO: strong validation should be done and rollback of the service registration upon error?!
        config = httpClient.createPluginConfig(apiId, config);
        return config;
    }

    private KongPluginConfig createCorsServicePolicy(String apiId, Policy policy) throws PublishingException {
        //Add the default headers to the custom headers in the policy
        Set<String> customHeaders = new HashSet<>(Arrays.asList("Accept", "Accept-Version", "Content-Length", "Content-MD5", "Content-Type", "Date", AUTH_API_KEY, "Authorization"));
        KongPluginCors plugin = gson.fromJson(policy.getPolicyJsonConfig(), KongPluginCors.class);
        customHeaders.addAll(plugin.getHeaders());
        plugin.setHeaders(new ArrayList<>(customHeaders));
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.CORS.getKongIdentifier())
                .withConfig(plugin);
        return httpClient.createPluginConfig(apiId, config);
    }

    /**
     * This method creates a policy for a given plan, applied on an API.
     *
     * @param api
     * @param policy
     * @param kongIdentifier
     * @param clazz
     * @param <T>
     * @throws PublishingException
     */
    private <T extends KongConfigValue> KongPluginConfig createPlanPolicy(KongApi api, KongConsumer consumer, Policy policy, String kongIdentifier, Class<T> clazz) throws PublishingException {
        //perform value mapping
        KongConfigValue plugin = gson.fromJson(policy.getPolicyJsonConfig(), clazz);
        KongPluginConfig config = new KongPluginConfig()
                .withName(kongIdentifier)//set required kong identifier
                .withConsumerId(consumer.getId())
                .withConfig(plugin);
        //TODO: strong validation should be done and rollback of the service registration upon error?!
        //execute
        return httpClient.createPluginConfig(api.getId(), config);
    }

    public KongConsumerList getConsumers(String offset) {
        if (StringUtils.isNotEmpty(offset)) {
            return httpClient.getConsumers(offset);
        }
        return httpClient.getConsumers();
    }

    public KongConsumer upateOrCreateConsumer(KongConsumer consumer) {
        return httpClient.updateOrCreateConsumer(consumer);
    }

    public KongApi updateServiceVersionOnGateway(ServiceVersionBean svb) {
        String apiId = ServiceConventionUtil.generateServiceUniqueName(svb);
        KongApi api = getApi(apiId);

        String upstreamUrl;
        if (svb.getUpstreamTargets().size() == 1) {
            ServiceUpstreamTargetBean target = svb.getUpstreamTargets().stream().collect(CustomCollectors.getFirstResult());
            upstreamUrl = URIUtils.buildEndpoint(svb.getUpstreamScheme(), target.getTarget(), target.getPort(), svb.getUpstreamPath());
        } else {
            upstreamUrl = URIUtils.buildEndpoint(svb.getUpstreamScheme(), apiId, null, svb.getUpstreamPath());
        }
        api.setUpstreamUrl(URIUtils.uriBackslashRemover(upstreamUrl));
        api.setHosts(svb.getHostnames() == null ? Collections.emptyList() : new ArrayList<>(svb.getHostnames()));
        api.setUris(GatewayPathUtilities.generateGatewayContextPath(svb.getService().getOrganization().getId(), svb.getService().getBasepaths(), svb.getVersion()));
        api.setUpstreamConnectTimeout(svb.getUpstreamConnectTimeout());
        api.setUpstreamReadTimeout(svb.getUpstreamReadTimeout());
        api.setUpstreamSendTimeout(svb.getUpstreamSendTimeout());
        return httpClient.updateOrCreateApi(api);
    }

    public void deleteApiPlugin(String ApiId, String pluginId) {
        httpClient.deletePlugin(ApiId, pluginId);
    }

    public KongConsumer updateConsumer(String kongConsumerId, KongConsumer updatedConsumer) {
        return httpClient.updateConsumer(kongConsumerId, updatedConsumer);
    }

    public KongOAuthTokenList getConsumerOAuthTokenList(String consumerOAuthCredentialId, String offset) {
        if (StringUtils.isEmpty(offset)) {
            return httpClient.getOAuthTokensByCredentialId(consumerOAuthCredentialId);
        }
        return httpClient.getOAuthTokensByCredentialId(consumerOAuthCredentialId, offset);
    }

    public KongOAuthTokenList getConsumerOAuthTokenListByUserId(String authenticatedUserId, String offset) {
        if (StringUtils.isEmpty(offset)) {
            return httpClient.getOAuthTokensByAuthenticatedUser(authenticatedUserId);
        }
        return httpClient.getOAuthTokensByAuthenticatedUser(authenticatedUserId, offset);
    }

    public void revokeOAuthToken(String tokenId) {
        httpClient.revokeOAuthToken(tokenId);
    }

    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformationByCredentialId(String credentialId) {
        return httpClient.getApplicationOAuthInformationByCredentialId(credentialId);
    }

    public KongOAuthTokenList getOAuthToken(String tokenId) {
        return httpClient.getOAuthToken(tokenId);
    }

    public KongOAuthToken getGatewayOauthToken(String token) {
        KongOAuthToken rval = null;
        KongOAuthTokenList tokens = httpClient.getOAuthTokensByAccessToken(token);
        if (token != null && tokens.getData() != null && !tokens.getData().isEmpty()) {
            if (tokens.getData().size() == 1) {
                rval = tokens.getData().get(0);
            } else {
                log.error("More than one token found:{}", tokens.getData());
            }
        }
        return rval;
    }

    public void revokeOAuthTokenByAccessToken(String accessToken) {
        KongOAuthTokenList tokens = httpClient.getOAuthTokensByAccessToken(accessToken);
        if (tokens != null && tokens.getData() != null && !tokens.getData().isEmpty()) {
            if (tokens.getTotal() > 1) {
                log.warn("Multiple tokens found for access token:{}", accessToken);
            }
            tokens.getData().forEach(gwToken -> httpClient.revokeOAuthToken(gwToken.getId()));
        }
    }

    public void deleteConsumerJwtCredential(String consumerId, String credentialId) {
        httpClient.deleteConsumerJwtCredential(consumerId, credentialId);
    }

    public KongPluginConfig getPlugin(String pluginId) {
        try {
            return httpClient.getPlugin(pluginId);
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public Policy createServicePolicy(String organizationId, String serviceId, String version, Policy policy) {
        String api = ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version);
        Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
        KongPluginConfig plugin = null;
        switch (policies) {
            //all policies can be available here
            case BASICAUTHENTICATION:
                plugin = createServicePolicyInternal(api, policy, Policies.BASICAUTHENTICATION.getKongIdentifier(), Policies.BASICAUTHENTICATION.getClazz());
                break;
            case CORS:
                plugin = createCorsServicePolicy(api, policy);
                break;
            case FILELOG:
                plugin = createServicePolicyInternal(api, policy, Policies.FILELOG.getKongIdentifier(), Policies.FILELOG.getClazz());
                break;
            case HTTPLOG:
                plugin = createServicePolicyInternal(api, policy, Policies.HTTPLOG.getKongIdentifier(), Policies.HTTPLOG.getClazz());
                break;
            case UDPLOG:
                plugin = createServicePolicyInternal(api, policy, Policies.UDPLOG.getKongIdentifier(), Policies.UDPLOG.getClazz());
                break;
            case TCPLOG:
                plugin = createServicePolicyInternal(api, policy, Policies.TCPLOG.getKongIdentifier(), Policies.TCPLOG.getClazz());
                break;
            case IPRESTRICTION:
                plugin = createServicePolicyInternal(api, policy, Policies.IPRESTRICTION.getKongIdentifier(), Policies.IPRESTRICTION.getClazz());
                break;
            case KEYAUTHENTICATION:
                plugin = createServicePolicyInternal(api, policy, Policies.KEYAUTHENTICATION.getKongIdentifier(), Policies.KEYAUTHENTICATION.getClazz());
                break;
            //for OAuth2 we have an exception, we validate the form data at this moment to keep track of OAuth2 scopes descriptions
            case OAUTH2:
                plugin = createServicePolicyInternal(api, gatewayValidation.validateExplicitOAuth(policy), Policies.OAUTH2.getKongIdentifier(), KongPluginOAuthEnhanced.class);
                log.info("start post oauth2 actions");
                //upon transformation we use another enhanced object for json deserialization
                postOAuth2Actions(organizationId, serviceId, version, policy, plugin);
                break;
            case RATELIMITING:
                plugin = createServicePolicyInternal(api, policy, Policies.RATELIMITING.getKongIdentifier(), Policies.RATELIMITING.getClazz());
                break;
            case JWTUP:
                plugin = createServicePolicyInternal(api, policy, Policies.JWTUP.getKongIdentifier(), Policies.JWTUP.getClazz());
                //flagJWT=true;
                break;
            case JWT:
                //Originally we skipped the jwt plugin if a service has a jwtup policy
                //if (!flagJWT) {
                plugin = createServicePolicyInternal(api, policy, Policies.JWT.getKongIdentifier(), Policies.JWT.getClazz());
                //}
                break;
            case REQUESTSIZELIMITING:
                plugin = createServicePolicyInternal(api, policy, Policies.REQUESTSIZELIMITING.getKongIdentifier(), Policies.REQUESTSIZELIMITING.getClazz());
                break;
            case REQUESTTRANSFORMER:
                plugin = createServicePolicyInternal(api, policy, Policies.REQUESTTRANSFORMER.getKongIdentifier(), Policies.REQUESTTRANSFORMER.getClazz());
                break;
            case RESPONSETRANSFORMER:
                plugin = createServicePolicyInternal(api, policy, Policies.RESPONSETRANSFORMER.getKongIdentifier(), Policies.RESPONSETRANSFORMER.getClazz());
                break;
            case SSL:
                plugin = createServicePolicyInternal(api, policy, Policies.SSL.getKongIdentifier(), Policies.SSL.getClazz());
                break;
            case ANALYTICS:
                plugin = createServicePolicyInternal(api, policy, Policies.ANALYTICS.getKongIdentifier(), Policies.ANALYTICS.getClazz());
                break;
            case ACL:
                plugin = createServicePolicyInternal(api, policy, Policies.ACL.getKongIdentifier(), Policies.ACL.getClazz());
                break;
            case JSONTHREATPROTECTION:
                plugin = createServicePolicyInternal(api, policy, Policies.JSONTHREATPROTECTION.getKongIdentifier(), Policies.JSONTHREATPROTECTION.getClazz());
                break;
            case LDAPAUTHENTICATION:
                plugin = createServicePolicyInternal(api, policy, Policies.LDAPAUTHENTICATION.getKongIdentifier(), Policies.LDAPAUTHENTICATION.getClazz());
                break;
            case HAL:
                plugin = createServicePolicyInternal(api, policy, Policies.HAL.getKongIdentifier(), Policies.HAL.getClazz());
                break;
            default:
                break;
        }
        if (plugin != null && !StringUtils.isEmpty(plugin.getId())) {
            policy.setKongPluginId(plugin.getId());
            policy.setPolicyJsonConfig(gson.toJson(plugin.getConfig()).replace(":{}", ":[]"));
        }
        return policy;
    }

    public KongOAuthTokenList getAllOAuth2Tokens(String offset) {
        if (StringUtils.isEmpty(offset)) {
            return httpClient.getOAuthTokens();
        } else {
            return httpClient.getOAuthTokens(offset);
        }
    }

    public KongPluginConfig updateServicePlugin(String api, KongPluginConfig plugin) {
        Policies policies = GatewayUtils.convertKongPluginNameToPolicy(plugin.getName());
        KongPluginConfig kpc = plugin;
        switch (policies) {
            //for OAuth2 we have an exception, we validate the form data at this moment to keep track of OAuth2 scopes descriptions
            case OAUTH2:
                KongPluginOAuth engineConfig = (KongPluginOAuth) kpc.getConfig();
                KongPluginOAuthEnhanced gwConfig = gatewayValidation.validateExplicitOAuth(engineConfig);
                kpc = httpClient.updateKongPluginConfig(api, kpc.withConfig(gwConfig));
                log.info("start post oauth2 actions");
                //upon transformation we use another enhanced object for json deserialization
                postOAuth2Actions(engineConfig, gwConfig.getProvisionKey(), api);
                break;
            default:
                kpc = httpClient.updateKongPluginConfig(api, kpc);
                break;
        }
        return kpc;
    }

    public KongOAuthToken createOAuthToken(OAuth2TokenBean token) {
        try {
            return httpClient.createOAuthToken(new KongOAuthToken()
                    .withAccessToken(token.getAccessToken())
                    .withAuthenticatedUserid(token.getAuthenticatedUserId())
                    .withCredentialId(token.getCredentialId())
                    .withExpiresIn(token.getExpiresIn())
                    .withId(token.getId())
                    .withScope(token.getScope())
                    .withRefreshToken(token.getRefreshToken())
                    .withTokenType(token.getTokenType()));
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongApi createApi(KongApi api) {
        try {
            if (CollectionUtils.isEmpty(api.getHosts())) api.setHosts(null);
            if (CollectionUtils.isEmpty(api.getMethods())) api.setMethods(null);
            api.setUpstreamUrl(URIUtils.uriBackslashRemover(api.getUpstreamUrl()));
            return httpClient.addApi(api);
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongApi updateApi(KongApi api) {
        api.setUpstreamUrl(URIUtils.uriBackslashRemover(api.getUpstreamUrl()));
        return httpClient.updateOrCreateApi(api);
    }

    public KongPluginConfig createApiPlugin(String apiId, KongPluginConfig plugin) {
        try {
            return httpClient.createPluginConfig(apiId, transformPolicyConfig(plugin));
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongPluginACLResponse updateConsumerAcl(KongPluginACLResponse acl) {
        return httpClient.updateConsumerAcl(acl.getConsumerId(), acl);
    }

    public KongPluginACLResponse getConsumerAcl(String consumerId, String kongPluginId) {
        try {
            return httpClient.getConsumerAcl(consumerId, kongPluginId);
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongConsumer getConsumerByCustomId(String customId) {
        try {
            KongConsumerList result = httpClient.getConsumerByCustomId(customId);
            if (result.getTotal() == 0) {
                return null;
            } else if (result.getTotal() == 1) {
                return result.getData().get(0);
            } else {
                Optional<KongConsumer> opt = result.getData().stream().max(Comparator.comparing(KongConsumer::getCreatedAt));
                return opt.isPresent() ? opt.get() : null;
            }
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongPluginConfigList getConsumerPlugins(String consumerId) {
        try {
            return httpClient.getConsumerPlugins(consumerId);
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongPluginConfig updatePlugin(KongPluginConfig plugin) {
        try {
            return httpClient.updatePlugin(transformPolicyConfig(plugin));
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public KongPluginACLResponseList getAllConsumerAcls(String consumerId) {
        KongPluginACLResponseList rval = new KongPluginACLResponseList().withData(new ArrayList<>());
        try {
            KongPluginACLResponseList result = httpClient.getConsumerACLs(consumerId);
            rval.setData(result.getData());
            rval.setTotal(result.getTotal());
            while (result.getOffset() != null) {
                result = httpClient.getConsumerACLs(consumerId, result.getOffset());
                rval.getData().addAll(result.getData());
                rval.setTotal(rval.getTotal() + result.getTotal());
            }
        } catch (RetrofitError ex) {
            //Do nothing
        }
        return rval;
    }

    public KongPluginConfigList getConsumerSpecificApiPlugins(String consumerId, String apiId) {
        KongPluginConfigList rval = new KongPluginConfigList().withData(new ArrayList<>());
        try {
            KongPluginConfigList results = httpClient.getConsumerSpecificApiPlugins(consumerId, apiId);
            rval.setData(results.getData());
            rval.setTotal(results.getTotal());
            while (results.getOffset() != null) {
                results = httpClient.getConsumerSpecificApiPlugins(consumerId, apiId, results.getOffset());
                rval.getData().addAll(results.getData());
                rval.setTotal(rval.getTotal() + results.getTotal());
            }
        } catch (RetrofitError ex) {
            //Do nothing
        }
        return rval;
    }

    private KongPluginConfig transformPolicyConfig(KongPluginConfig plugin) {
        switch (GatewayUtils.convertKongPluginNameToPolicy(plugin.getName())) {
            case OAUTH2:
                return plugin.withConfig(gatewayValidation.validateExplicitOAuth((KongPluginOAuth) plugin.getConfig()));
            default:
                return plugin;
        }
    }

    public void deleteServiceUpstream(String upstreamVirtualHost) {
        try {
            httpClient.deleteKongUpstream(upstreamVirtualHost);
        } catch (RetrofitError ex) {
            log.error("Error deleting upstream on gateway: {}", ex.getMessage());
        }
    }

    public KongUpstream getServiceUpstream(String upstreamName) {
        try {
            return httpClient.getKongUpstream(upstreamName);
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public void createServiceUpstream(String organizationId, String serviceId, String version) {
        String upstreamName = ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version);
        try {
            if (getServiceUpstream(upstreamName) == null) {
                KongUpstream upstream = new KongUpstream().withName(upstreamName).withSlots(1000L);
                httpClient.createKongUpstream(upstream);
            }
        } catch (RetrofitError ex) {
            log.error("Error creating upstream: {}", ex);
            throw ExceptionFactory.serviceVersionUpdateException("Upstream creation");
        }
    }

    public void createOrUpdateServiceUpstreamTargets(String upstreamName, ServiceUpstreamTargetBean target) {
        KongUpstream upstream = getServiceUpstream(upstreamName);
        try {
            if (upstream != null) {
                httpClient.createKongUpstreamTarget(upstreamName, new KongUpstreamTarget().withTarget(URIUtils.appendPort(target.getTarget(), target.getPort())).withWeight(target.getWeight()));
            }
        } catch (RetrofitError ex) {
            log.error("Error creating an upstream target: {}", ex);
            throw ExceptionFactory.serviceVersionUpdateException("Target creation");
        }
    }

    public KongUpstreamTargetList listActiveKongUpstreamTargets(String upstreamName) {
        try {
            return httpClient.listActiveKongUpstreamTargets(upstreamName);
        } catch (RetrofitError ex) {
            return null;
        }
    }

    public Boolean togglePlugin(String kongPluginId) {
        try {
            KongPluginConfig plugin = httpClient.getPlugin(kongPluginId);
            if (plugin != null) {
                plugin.setEnabled(!plugin.getEnabled());
                plugin = httpClient.updatePlugin(plugin);
            }
            return plugin.getEnabled();
        } catch (RetrofitError ex) {
            return null;
        }
    }
}