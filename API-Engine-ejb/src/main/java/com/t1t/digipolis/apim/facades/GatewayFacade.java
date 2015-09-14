package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.gateways.*;
import com.t1t.digipolis.apim.beans.summary.GatewaySummaryBean;
import com.t1t.digipolis.apim.beans.summary.GatewayTestResultBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.AbstractRestException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GatewayFacade {
    @Inject @APIEngineContext private Logger log;
    @Inject @APIEngineContext private EntityManager em;
    @Inject private ISecurityContext securityContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private IGatewayLinkFactory gatewayLinkFactory;

    private static final ObjectMapper mapper = new ObjectMapper();

    public GatewayTestResultBean test(NewGatewayBean bean){
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

    public List<GatewaySummaryBean> list(){
        try {
            return query.listGateways();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public GatewayBean create(NewGatewayBean bean){
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
            if (storage.getGateway(gateway.getId()) != null) {
                throw ExceptionFactory.gatewayAlreadyExistsException(gateway.getName());
            }
            // Store/persist the new gateway
            // encryptPasswords(gateway);
            storage.createGateway(gateway);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        //decryptPasswords(gateway);
        log.debug(String.format("Successfully created new gateway %s: %s", gateway.getName(), gateway)); //$NON-NLS-1$
        return gateway;
    }

    public GatewayBean get(String gatewayId){
        try {
            GatewayBean bean = storage.getGateway(gatewayId);
            if (bean == null) {
                throw ExceptionFactory.gatewayNotFoundException(gatewayId);
            }
            if (!securityContext.isAdmin()) {
                bean.setConfiguration(null);
            }
/*            else {
                decryptPasswords(bean);
            }*/
            log.debug(String.format("Successfully fetched gateway %s: %s", bean.getName(), bean)); //$NON-NLS-1$
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void update(String gatewayId, UpdateGatewayBean bean){
        try {
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
            //encryptPasswords(gbean);
            storage.updateGateway(gbean);
            log.debug(String.format("Successfully updated gateway %s: %s", gbean.getName(), gbean)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void remove(String gatewayId){
        try {
            GatewayBean gbean = storage.getGateway(gatewayId);
            if (gbean == null) {
                throw ExceptionFactory.gatewayNotFoundException(gatewayId);
            }
            storage.deleteGateway(gbean);
            log.debug(String.format("Successfully deleted gateway %s: %s", gbean.getName(), gbean)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    /*********************************************UTILITIES**********************************************/
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
}
