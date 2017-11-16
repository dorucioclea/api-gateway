package com.t1t.apim.beans.services;

import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceVersionWithMarketInfoBean extends ServiceVersionBean {

    private ServiceMarketInfoBean marketInfo;

    public ServiceVersionWithMarketInfoBean(ServiceVersionBean svb) {
        this.setAutoAcceptContracts(svb.getAutoAcceptContracts());
        this.setOauthScopes(svb.getOauthScopes());
        this.setEndpointType(svb.getEndpointType());
        this.setEndpointProperties(svb.getEndpointProperties());
        this.setCreatedBy(svb.getCreatedBy());
        this.setCreatedOn(svb.getCreatedOn());
        this.setDefinitionType(svb.getDefinitionType());
        this.setModifiedBy(svb.getModifiedBy());
        this.setModifiedOn(svb.getModifiedOn());
        this.setDeprecatedOn(svb.getDeprecatedOn());
        this.setId(svb.getId());
        this.setGateways(svb.getGateways());
        this.setOnlinedoc(svb.getOnlinedoc());
        this.setVisibility(svb.getVisibility());
        this.setStatus(svb.getStatus());
        this.setPlans(svb.getPlans());
        this.setPublicService(svb.isPublicService());
        this.setPublishedOn(svb.getPublishedOn());
        this.setRetiredOn(svb.getRetiredOn());
        this.setService(svb.getService());
        this.setVersion(svb.getVersion());
    }

    public ServiceMarketInfoBean getMarketInfo() {
        return marketInfo;
    }

    public void setMarketInfo(ServiceMarketInfoBean marketInfo) {
        this.marketInfo = marketInfo;
    }

    @Override
    public String toString() {
        return "ServiceVersionWithMarketInfoBean{" +
                super.toString() +
                "marketInfo=" + marketInfo +
                '}';
    }
}