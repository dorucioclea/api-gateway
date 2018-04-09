package com.t1t.apim;

import com.t1t.apim.beans.config.ConfigBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.i18n.Messages;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by michallispashidis on 10/10/15.
 * This will load the configuration for a specific environment and produce the config throughout the application.
 * More information about the config Typesafe approach can be found: https://github.com/typesafehub/config#essential-information
 */
@Singleton(name = "AppConfig")
@Startup
public class AppConfig implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class.getName());

    private transient Config config;
    private transient Properties properties;
    private AppConfigBean configBean;

    @Inject
    private IStorage storageService;

    @PostConstruct
    public void postInit() {
        initConfig(null);
    }

    @Produces
    @T1G
    @Lock(LockType.READ)
    public AppConfigBean getConfig() {
        return configBean;
    }

    public void initConfig(ConfigBean optionalConfig) {
        final ConfigBean defaultConfig;
        if (optionalConfig == null) {
            try {
                final List<ConfigBean> configList = storageService.getDefaultConfig();
                if (configList != null && configList.size() > 0) defaultConfig = configList.get(0);
                else throw new StorageException("No configuration found.");
            } catch (StorageException e) {
                throw ExceptionFactory.systemErrorException("Could not start the service, missing configuration.");
            }
        } else defaultConfig = optionalConfig;
        Path configPath = Paths.get(defaultConfig.getConfigPath());
        log.info("Config path loaded:{}", configPath.toAbsolutePath());
        //read properties file
        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else throw ExceptionFactory.systemErrorException("API Engine basic property file not found.");
        //read specific application config, depends on the maven profile that has been set
        if (!configPath.toFile().exists())
            throw ExceptionFactory.systemErrorException("API Engine config property file not found.");
        config = ConfigFactory.parseFile(configPath.toFile());
        if (config == null) throw ExceptionFactory.systemErrorException("API Engine log not found");
        else {
            configBean = new AppConfigBean();
            configBean.setConfigPath(defaultConfig.getConfigPath());
            configBean.setEnvironment(getEnvironment());
            configBean.setVersion(getVersion());
            configBean.setBuildDate(getBuildDate());

            configBean.setDataDogMetricsApiKey(getDataDogMetricsApiKey());
            configBean.setDataDogMetricsUri(getDataDogMetricsURI());
            configBean.setDataDogMetricsApplicationKey(getDataDogMetricsApplicationKey());
            configBean.setHystrixMetricsTimeout(getHystrixMetricsTimeout());

            configBean.setRestResourceSecurity(getRestResourceSecurity());
            configBean.setRestAuthResourceSecurity(getRestAuthResourceSecurity());

            configBean.setNotificationsEnableDebug(getNotificationsEnableDebug());
            configBean.setNotificationStartupMail(getNotificationStartupMail());
            configBean.setNotificationMailFrom(getNotificationMailFrom());

            configBean.setLocalFilePath(getLocalFilePath());

            configBean.setApiEngineName(getApiEngineName());
            configBean.setApiEngineRequestPath(getApiEngineRequestPath());
            configBean.setApiEngineStripRequestPath(getApiEngineStripRequestPath());
            configBean.setApiEngineUpstreamUrl(getApiEngineUpstreamUrl());

            configBean.setApiEngineAuthName(getApiEngineAuthName());
            configBean.setApiEngineAuthRequestPath(getApiEngineAuthRequestPath());
            configBean.setApiEngineAuthStripRequestPath(getApiEngineAuthStripRequestPath());
            configBean.setApiEngineAuthUpstreamUrl(getApiEngineAuthUpstreamUrl());

            configBean.setGatewaykeysName(getGatewaykeysName());
            configBean.setGatewaykeysRequestPath(getGatewaykeysRequestPath());
            configBean.setGatewaykeysStripRequestPath(getGatewaykeysStripRequestPath());
            configBean.setGatewaykeysUpstreamUrl(getGatewaykeysUpstreamUrl());

            configBean.setClusterInfoName(getClusterInfoName());
            configBean.setClusterInfoRequestPath(getClusterInfoRequestPath());
            configBean.setClusterInfoStripRequestPath(getClusterInfoStripRequestPath());
            configBean.setClusterInfoUpstreamUrl(getClusterInfoUpstreamUrl());
            configBean.setStartupDate(DateTime.now());


            try {
                log.info("============================== API Engine configuration ==============================");
                log.info("General - Environment: {}", configBean.getEnvironment());
                log.info("General - Using configuration file: {}", configBean.getConfigPath());
                log.info("General - Build: {}", configBean.getBuildDate());
                log.info("General - Version: {}", configBean.getVersion());
                log.info("General - Local file path: {}", configBean.getLocalFilePath());
                log.info("Security - REST resource security: {}", configBean.getRestResourceSecurity());
                log.info("Security - REST AUTH resource security: {}", configBean.getRestAuthResourceSecurity());
                log.info("Notifications - Debug enabled? {}", configBean.getNotificationsEnableDebug());
                log.info("Notifications - Startup mail will be sent to {}", configBean.getNotificationStartupMail());
                log.info("Notifications - Mail will be send from {}", configBean.getNotificationMailFrom());
                log.info("Metrics - Timeout value: {}", configBean.getHystrixMetricsTimeout());
                log.info("Metrics - DataDog API key: {}", configBean.getDataDogMetricsApiKey());
                log.info("Metrics - DataDog Metrics URI: {}", configBean.getDataDogMetricsUri());
                log.info("Metrics - DataDog Application key: {}", configBean.getDataDogMetricsApplicationKey());
                log.info("Gateway - API Engine web upstream url: {}", configBean.getApiEngineUpstreamUrl());
                log.info("Gateway - API Engine web request path: {}", configBean.getApiEngineRequestPath());
                log.info("Gateway - API Engine auth upstream url: {}", configBean.getApiEngineAuthUpstreamUrl());
                log.info("Gateway - API Engine auth request path: {}", configBean.getApiEngineAuthRequestPath());
                log.info("Gateway - Gateway keys upstream url: {}", configBean.getGatewaykeysUpstreamUrl());
                log.info("Gateway - Gateway keys request path: {}", configBean.getGatewaykeysRequestPath());
                log.info("Gateway - Gateway cluster info url: {}", configBean.getGatewaykeysUpstreamUrl());
                log.info("Gateway - Gateway cluster info request path: {}", configBean.getGatewaykeysRequestPath());
                log.info("======================================================================================");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getEnvironment() {
        return getStringProperty(IConfig.APP_ENVIRONMENT);
    }

    private String getVersion() {
        return properties.getProperty(IConfig.PROP_FILE_VERSION);
    }

    private String getBuildDate() {
        return properties.getProperty(IConfig.PROP_FILE_DATE);
    }

    private String getDataDogMetricsApiKey() {
        return getStringProperty(IConfig.DATADOG_METRICS_API_KEY);
    }

    private String getDataDogMetricsURI() {
        return getStringProperty(IConfig.DATADOG_METRICS_URI);
    }

    private String getDataDogMetricsApplicationKey() {
        return getStringProperty(IConfig.DATADOG_METRICS_APPLICATION_KEY);
    }

    private Boolean getRestResourceSecurity() {
        return getBooleanProperty(IConfig.SECURITY_REST_RESORUCES);
    }

    private Boolean getRestAuthResourceSecurity() {
        return getBooleanProperty(IConfig.SECURITY_REST_AUTH_RESOURCES);
    }

    private Boolean getNotificationsEnableDebug() {
        return getBooleanProperty(IConfig.NOTIFICATION_ENABLE_DEBUG);
    }

    private String getNotificationStartupMail() {
        return getStringProperty(IConfig.NOTIFICATION_STARTUP_MAIL);
    }

    private String getNotificationMailFrom() {
        return getStringProperty(IConfig.NOTIFICATION_MAIL_FROM);
    }

    private Integer getHystrixMetricsTimeout() {
        return getIntegerProperty(IConfig.HYSTRIX_METRICS_TIMEOUT_VALUE);
    }

    private String getLocalFilePath() {
        return getStringProperty(IConfig.FILEPATH_LOCAL);
    }

    private String getApiEngineName() {
        return getStringProperty(IConfig.GWD_APIENGINE_NAME);
    }

    private String getApiEngineRequestPath() {
        return getStringProperty(IConfig.GWD_APIENGINE_REQUEST_PATH);
    }

    private Boolean getApiEngineStripRequestPath() {
        return getBooleanProperty(IConfig.GWD_APIENGINE_STRIP_REQUEST_PATH);
    }

    private String getApiEngineUpstreamUrl() {
        return getStringProperty(IConfig.GWD_APIENGINE_UPSTREAM_URL);
    }

    private String getApiEngineAuthName() {
        return getStringProperty(IConfig.GWD_APIENGINEAUTH_NAME);
    }

    private Boolean getApiEngineAuthStripRequestPath() {
        return getBooleanProperty(IConfig.GWD_APIENGINEAUTH_STRIP_REQUEST_PATH);
    }

    private String getApiEngineAuthUpstreamUrl() {
        return getStringProperty(IConfig.GWD_APIENGINEAUTH_UPSTREAM_URL);
    }

    private String getApiEngineAuthRequestPath() {
        return getStringProperty(IConfig.GWD_APIENGINEAUTH_REQUEST_PATH);
    }

    private String getGatewaykeysName() {
        return getStringProperty(IConfig.GWD_GATEWAYKEYS_NAME);
    }

    private Boolean getGatewaykeysStripRequestPath() {
        return getBooleanProperty(IConfig.GWD_GATEWAYKEYS_STRIP_REQUEST_PATH);
    }

    private String getGatewaykeysUpstreamUrl() {
        return getStringProperty(IConfig.GWD_GATEWAYKEYS_UPSTREAM_URL);
    }

    private String getGatewaykeysRequestPath() {
        return getStringProperty(IConfig.GWD_GATEWAYKEYS_REQUEST_PATH);
    }

    private String getClusterInfoName() {
        return getStringProperty(IConfig.GWD_CLUSTER_INFO_NAME);
    }

    private String getClusterInfoRequestPath() {
        return getStringProperty(IConfig.GWD_CLUSTER_INFO_REQUEST_PATH);
    }

    private String getClusterInfoUpstreamUrl() {
        return getStringProperty(IConfig.GWD_CLUSTER_INFO_UPSTREAM_URL);
    }

    private Boolean getClusterInfoStripRequestPath() {
        return getBooleanProperty(IConfig.GWD_CLUSTER_INFO_STRIP_REQUEST_PATH);
    }

    private String getStringProperty(String propKey) {
        try {
            return config.getString(propKey);
        } catch (ConfigException ex) {
            log.error(Messages.i18n.format(ErrorCodes.CONFIG_PROPERTY_MISSING, propKey));
            return null;
        }
    }

    private Boolean getBooleanProperty(String propKey) {
        try {
            return config.getBoolean(propKey);
        } catch (ConfigException ex) {
            log.error(Messages.i18n.format(ErrorCodes.CONFIG_PROPERTY_MISSING, propKey));
            return Boolean.FALSE;
        }
    }

    private Integer getIntegerProperty(String propKey) {
        try {
            return config.getInt(propKey);
        } catch (ConfigException ex) {
            log.error(Messages.i18n.format(ErrorCodes.CONFIG_PROPERTY_MISSING, propKey));
            return null;
        }
    }

    private List<String> getStringListProperty(String propKey) {
        try {
            return config.getStringList(propKey);
        } catch (ConfigException ex) {
            log.error(Messages.i18n.format(ErrorCodes.CONFIG_PROPERTY_MISSING, propKey));
            return Collections.emptyList();
        }
    }

}
