package com.t1t.apim;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class AppConfigBean {

    private String configPath;
    private String environment;
    private String version;
    private String buildDate;
    private String dataDogMetricsApiKey;
    private String dataDogMetricsUri;
    private String dataDogMetricsApplicationKey;
    private Boolean restResourceSecurity;
    private Boolean restAuthResourceSecurity;
    private Boolean notificationsEnableDebug;
    private String notificationStartupMail;
    private String notificationMailFrom;
    private Integer hystrixMetricsTimeout;
    private String localFilePath;
    private String apiEngineName;
    private String apiEngineRequestPath;
    private Boolean apiEngineStripRequestPath;
    private String apiEngineUpstreamUrl;
    private String apiEngineAuthName;
    private Boolean apiEngineAuthStripRequestPath;
    private String apiEngineAuthUpstreamUrl;
    private String apiEngineAuthRequestPath;
    private String gatewaykeysName;
    private Boolean gatewaykeysStripRequestPath;
    private String gatewaykeysUpstreamUrl;
    private String gatewaykeysRequestPath;
    private String clusterInfoName;
    private String clusterInfoRequestPath;
    private String clusterInfoUpstreamUrl;
    private Boolean clusterInfoStripRequestPath;

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getDataDogMetricsApiKey() {
        return dataDogMetricsApiKey;
    }

    public void setDataDogMetricsApiKey(String dataDogMetricsApiKey) {
        this.dataDogMetricsApiKey = dataDogMetricsApiKey;
    }

    public String getDataDogMetricsUri() {
        return dataDogMetricsUri;
    }

    public void setDataDogMetricsUri(String dataDogMetricsUri) {
        this.dataDogMetricsUri = dataDogMetricsUri;
    }

    public String getDataDogMetricsApplicationKey() {
        return dataDogMetricsApplicationKey;
    }

    public void setDataDogMetricsApplicationKey(String dataDogMetricsApplicationKey) {
        this.dataDogMetricsApplicationKey = dataDogMetricsApplicationKey;
    }

    public Boolean getRestResourceSecurity() {
        return restResourceSecurity;
    }

    public void setRestResourceSecurity(Boolean restResourceSecurity) {
        this.restResourceSecurity = restResourceSecurity;
    }

    public Boolean getRestAuthResourceSecurity() {
        return restAuthResourceSecurity;
    }

    public void setRestAuthResourceSecurity(Boolean restAuthResourceSecurity) {
        this.restAuthResourceSecurity = restAuthResourceSecurity;
    }

    public Boolean getNotificationsEnableDebug() {
        return notificationsEnableDebug;
    }

    public void setNotificationsEnableDebug(Boolean notificationsEnableDebug) {
        this.notificationsEnableDebug = notificationsEnableDebug;
    }

    public String getNotificationStartupMail() {
        return notificationStartupMail;
    }

    public void setNotificationStartupMail(String notificationStartupMail) {
        this.notificationStartupMail = notificationStartupMail;
    }

    public String getNotificationMailFrom() {
        return notificationMailFrom;
    }

    public void setNotificationMailFrom(String notificationMailFrom) {
        this.notificationMailFrom = notificationMailFrom;
    }

    public Integer getHystrixMetricsTimeout() {
        return hystrixMetricsTimeout;
    }

    public void setHystrixMetricsTimeout(Integer hystrixMetricsTimeout) {
        this.hystrixMetricsTimeout = hystrixMetricsTimeout;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getApiEngineName() {
        return apiEngineName;
    }

    public void setApiEngineName(String apiEngineName) {
        this.apiEngineName = apiEngineName;
    }

    public String getApiEngineRequestPath() {
        return apiEngineRequestPath;
    }

    public void setApiEngineRequestPath(String apiEngineRequestPath) {
        this.apiEngineRequestPath = apiEngineRequestPath;
    }

    public Boolean getApiEngineStripRequestPath() {
        return apiEngineStripRequestPath;
    }

    public void setApiEngineStripRequestPath(Boolean apiEngineStripRequestPath) {
        this.apiEngineStripRequestPath = apiEngineStripRequestPath;
    }

    public String getApiEngineUpstreamUrl() {
        return apiEngineUpstreamUrl;
    }

    public void setApiEngineUpstreamUrl(String apiEngineUpstreamUrl) {
        this.apiEngineUpstreamUrl = apiEngineUpstreamUrl;
    }

    public String getApiEngineAuthName() {
        return apiEngineAuthName;
    }

    public void setApiEngineAuthName(String apiEngineAuthName) {
        this.apiEngineAuthName = apiEngineAuthName;
    }

    public Boolean getApiEngineAuthStripRequestPath() {
        return apiEngineAuthStripRequestPath;
    }

    public void setApiEngineAuthStripRequestPath(Boolean apiEngineAuthStripRequestPath) {
        this.apiEngineAuthStripRequestPath = apiEngineAuthStripRequestPath;
    }

    public String getApiEngineAuthUpstreamUrl() {
        return apiEngineAuthUpstreamUrl;
    }

    public void setApiEngineAuthUpstreamUrl(String apiEngineAuthUpstreamUrl) {
        this.apiEngineAuthUpstreamUrl = apiEngineAuthUpstreamUrl;
    }

    public String getApiEngineAuthRequestPath() {
        return apiEngineAuthRequestPath;
    }

    public void setApiEngineAuthRequestPath(String apiEngineAuthRequestPath) {
        this.apiEngineAuthRequestPath = apiEngineAuthRequestPath;
    }

    public String getGatewaykeysName() {
        return gatewaykeysName;
    }

    public void setGatewaykeysName(String gatewaykeysName) {
        this.gatewaykeysName = gatewaykeysName;
    }

    public Boolean getGatewaykeysStripRequestPath() {
        return gatewaykeysStripRequestPath;
    }

    public void setGatewaykeysStripRequestPath(Boolean gatewaykeysStripRequestPath) {
        this.gatewaykeysStripRequestPath = gatewaykeysStripRequestPath;
    }

    public String getGatewaykeysUpstreamUrl() {
        return gatewaykeysUpstreamUrl;
    }

    public void setGatewaykeysUpstreamUrl(String gatewaykeysUpstreamUrl) {
        this.gatewaykeysUpstreamUrl = gatewaykeysUpstreamUrl;
    }

    public String getGatewaykeysRequestPath() {
        return gatewaykeysRequestPath;
    }

    public void setGatewaykeysRequestPath(String gatewaykeysRequestPath) {
        this.gatewaykeysRequestPath = gatewaykeysRequestPath;
    }

    public String getClusterInfoName() {
        return clusterInfoName;
    }

    public void setClusterInfoName(String clusterInfoName) {
        this.clusterInfoName = clusterInfoName;
    }

    public String getClusterInfoRequestPath() {
        return clusterInfoRequestPath;
    }

    public void setClusterInfoRequestPath(String clusterInfoRequestPath) {
        this.clusterInfoRequestPath = clusterInfoRequestPath;
    }

    public String getClusterInfoUpstreamUrl() {
        return clusterInfoUpstreamUrl;
    }

    public void setClusterInfoUpstreamUrl(String clusterInfoUpstreamUrl) {
        this.clusterInfoUpstreamUrl = clusterInfoUpstreamUrl;
    }

    public Boolean getClusterInfoStripRequestPath() {
        return clusterInfoStripRequestPath;
    }

    public void setClusterInfoStripRequestPath(Boolean clusterInfoStripRequestPath) {
        this.clusterInfoStripRequestPath = clusterInfoStripRequestPath;
    }

    @Override
    public String toString() {
        return "AppConfigBean{" +
                "configPath='" + configPath + '\'' +
                ", environment='" + environment + '\'' +
                ", version='" + version + '\'' +
                ", buildDate='" + buildDate + '\'' +
                ", dataDogMetricsApiKey='" + dataDogMetricsApiKey + '\'' +
                ", dataDogMetricsUri='" + dataDogMetricsUri + '\'' +
                ", dataDogMetricsApplicationKey='" + dataDogMetricsApplicationKey + '\'' +
                ", restResourceSecurity=" + restResourceSecurity +
                ", restAuthResourceSecurity=" + restAuthResourceSecurity +
                ", notificationsEnableDebug=" + notificationsEnableDebug +
                ", notificationStartupMail='" + notificationStartupMail + '\'' +
                ", notificationMailFrom='" + notificationMailFrom + '\'' +
                ", hystrixMetricsTimeout=" + hystrixMetricsTimeout +
                ", localFilePath='" + localFilePath + '\'' +
                ", apiEngineName='" + apiEngineName + '\'' +
                ", apiEngineRequestPath='" + apiEngineRequestPath + '\'' +
                ", apiEngineStripRequestPath=" + apiEngineStripRequestPath +
                ", apiEngineUpstreamUrl='" + apiEngineUpstreamUrl + '\'' +
                ", apiEngineAuthName='" + apiEngineAuthName + '\'' +
                ", apiEngineAuthStripRequestPath=" + apiEngineAuthStripRequestPath +
                ", apiEngineAuthUpstreamUrl='" + apiEngineAuthUpstreamUrl + '\'' +
                ", apiEngineAuthRequestPath='" + apiEngineAuthRequestPath + '\'' +
                ", gatewaykeysName='" + gatewaykeysName + '\'' +
                ", gatewaykeysStripRequestPath=" + gatewaykeysStripRequestPath +
                ", gatewaykeysUpstreamUrl='" + gatewaykeysUpstreamUrl + '\'' +
                ", gatewaykeysRequestPath='" + gatewaykeysRequestPath + '\'' +
                ", clusterInfoName='" + clusterInfoName + '\'' +
                ", clusterInfoRequestPath='" + clusterInfoRequestPath + '\'' +
                ", clusterInfoUpstreamUrl='" + clusterInfoUpstreamUrl + '\'' +
                ", clusterInfoStripRequestPath=" + clusterInfoStripRequestPath +
                '}';
    }
}