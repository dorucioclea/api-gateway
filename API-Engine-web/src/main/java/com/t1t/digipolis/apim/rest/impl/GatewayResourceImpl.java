package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.gateways.*;
import com.t1t.digipolis.apim.beans.summary.GatewaySummaryBean;
import com.t1t.digipolis.apim.beans.summary.GatewayTestResultBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.logging.ApimanLogger;
import com.t1t.digipolis.apim.core.logging.IApimanLogger;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.rest.impl.i18n.Messages;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IGatewayResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.codehaus.jackson.map.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the Gateway API.
 */
@ApplicationScoped
public class GatewayResourceImpl implements IGatewayResource {

    @Inject IStorage storage;
    @Inject IStorageQuery query;
    @Inject ISecurityContext securityContext;
    @Inject IGatewayLinkFactory gatewayLinkFactory;
    @Inject @ApimanLogger(GatewayResourceImpl.class) IApimanLogger log;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor.
     */
    public GatewayResourceImpl() {
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IGatewayResource#test(com.t1t.digipolis.apim.beans.gateways.NewGatewayBean)
     */
    @Override
    public GatewayTestResultBean test(NewGatewayBean bean) throws NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        GatewayTestResultBean rval = new GatewayTestResultBean();

        try {
            GatewayBean testGateway = new GatewayBean();
            testGateway.setName(bean.getName());
            testGateway.setType(bean.getType());
            testGateway.setConfiguration(bean.getConfiguration());
            IGatewayLink gatewayLink = gatewayLinkFactory.create(testGateway);
            SystemStatus status = gatewayLink.getStatus();
            String detail = mapper.writer().writeValueAsString(status);
            rval.setSuccess(true);
            rval.setDetail(detail);
        } catch (GatewayAuthenticationException e) {
            rval.setSuccess(false);
            rval.setDetail(Messages.i18n.format("GatewayResourceImpl.AuthenticationFailed")); //$NON-NLS-1$
        } catch (Exception e) {
            rval.setSuccess(false);
            rval.setDetail(e.getMessage());
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IGatewayResource#list()
     */
    @Override
    public List<GatewaySummaryBean> list() throws NotAuthorizedException {
        try {
            return query.listGateways();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IGatewayResource#create(com.t1t.digipolis.apim.beans.gateways.NewGatewayBean)
     */
    @Override
    public GatewayBean create(NewGatewayBean bean) throws GatewayAlreadyExistsException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();

        Date now = new Date();

        GatewayBean gateway = new GatewayBean();
        gateway.setId(BeanUtils.idFromName(bean.getName()));
        gateway.setName(bean.getName());
        gateway.setDescription(bean.getDescription());
        gateway.setType(bean.getType());
        gateway.setConfiguration(bean.getConfiguration());
        gateway.setCreatedBy(securityContext.getCurrentUser());
        gateway.setCreatedOn(now);
        gateway.setModifiedBy(securityContext.getCurrentUser());
        gateway.setModifiedOn(now);
        try {
            storage.beginTx();
            if (storage.getGateway(gateway.getId()) != null) {
                throw ExceptionFactory.gatewayAlreadyExistsException(gateway.getName());
            }
            // Store/persist the new gateway
            encryptPasswords(gateway);
            storage.createGateway(gateway);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
        decryptPasswords(gateway);

        log.debug(String.format("Successfully created new gateway %s: %s", gateway.getName(), gateway)); //$NON-NLS-1$
        return gateway;
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IGatewayResource#get(String)
     */
    @Override
    public GatewayBean get(String gatewayId) throws GatewayNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            GatewayBean bean = storage.getGateway(gatewayId);
            if (bean == null) {
                throw ExceptionFactory.gatewayNotFoundException(gatewayId);
            }
            if (!securityContext.isAdmin()) {
                bean.setConfiguration(null);
            } else {
                decryptPasswords(bean);
            }
            storage.commitTx();

            log.debug(String.format("Successfully fetched gateway %s: %s", bean.getName(), bean)); //$NON-NLS-1$
            return bean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IGatewayResource#update(String, com.t1t.digipolis.apim.beans.gateways.UpdateGatewayBean)
     */
    @Override
    public void update(String gatewayId, UpdateGatewayBean bean) throws GatewayNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            Date now = new Date();

            GatewayBean gbean = storage.getGateway(gatewayId);
            if (gbean == null) {
                throw ExceptionFactory.gatewayNotFoundException(gatewayId);
            }
            gbean.setModifiedBy(securityContext.getCurrentUser());
            gbean.setModifiedOn(now);
            if (bean.getDescription() != null)
                gbean.setDescription(bean.getDescription());
            if (bean.getType() != null)
                gbean.setType(bean.getType());
            if (bean.getConfiguration() != null)
                gbean.setConfiguration(bean.getConfiguration());
            encryptPasswords(gbean);
            storage.updateGateway(gbean);
            storage.commitTx();

            log.debug(String.format("Successfully updated gateway %s: %s", gbean.getName(), gbean)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see com.t1t.digipolis.apim.rest.resources.IGatewayResource#delete(String)
     */
    @Override
    public void delete(String gatewayId) throws GatewayNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            GatewayBean gbean = storage.getGateway(gatewayId);
            if (gbean == null) {
                throw ExceptionFactory.gatewayNotFoundException(gatewayId);
            }
            storage.deleteGateway(gbean);
            storage.commitTx();

            log.debug(String.format("Successfully deleted gateway %s: %s", gbean.getName(), gbean)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @param bean
     */
    private void encryptPasswords(GatewayBean bean) {
        if (bean.getConfiguration() == null) {
            return;
        }
        try {
            if (bean.getType() == GatewayType.REST) {
                RestGatewayConfigBean configBean = mapper.readValue(bean.getConfiguration(), RestGatewayConfigBean.class);
                configBean.setPassword(AesEncrypter.encrypt(configBean.getPassword()));
                bean.setConfiguration(mapper.writeValueAsString(configBean));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bean
     */
    private void decryptPasswords(GatewayBean bean) {
        if (bean.getConfiguration() == null) {
            return;
        }
        try {
            if (bean.getType() == GatewayType.REST) {
                RestGatewayConfigBean configBean = mapper.readValue(bean.getConfiguration(), RestGatewayConfigBean.class);
                configBean.setPassword(AesEncrypter.decrypt(configBean.getPassword()));
                bean.setConfiguration(mapper.writeValueAsString(configBean));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    /**
     * @return the securityContext
     */
    public ISecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(ISecurityContext securityContext) {
        this.securityContext = securityContext;
    }

}
