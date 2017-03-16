package com.t1t.apim.facades;

import com.t1t.apim.beans.BeanUtils;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.NewGatewayBean;
import com.t1t.apim.beans.gateways.UpdateGatewayBean;
import com.t1t.apim.beans.summary.GatewaySummaryBean;
import com.t1t.apim.beans.summary.GatewayTestResultBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.AbstractRestException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.GatewayNotFoundException;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.gateway.GatewayAuthenticationException;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.gateway.IGatewayLinkFactory;
import com.t1t.apim.gateway.dto.SystemStatus;
import com.t1t.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.util.ConsumerConventionUtil;
import com.t1t.util.KeyUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GatewayFacade {
    private static Logger log = LoggerFactory.getLogger(GatewayFacade.class.getName());
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
/*            if (!securityContext.isAdmin()) {
                bean.setConfiguration(null);
            }*/
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
            if (bean.getJwtExpTime() != null)
                gbean.setJWTExpTime(bean.getJwtExpTime());
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
     * Return default gateway - normally one should be supported for each environment.
     * The Kong gateway will sync all actions towards the other shards in the same environment.
     *
     * @return
     * @throws StorageException
     */
    public GatewaySummaryBean getDefaultGateway() throws StorageException {
        final List<GatewaySummaryBean> gatewaySummaryBeen = query.listGateways();
        if(gatewaySummaryBeen!=null&&gatewaySummaryBeen.size()>0)return query.listGateways().get(0);
        else return null;
    }

    /**
     * Creates a gateway link given a gateway id.
     * TODO duplicated in ActionFacade => set as utility
     *
     * @param gatewayId
     */
    public IGatewayLink createGatewayLink(String gatewayId) throws PublishingException {
        try {
            GatewayBean gateway = storage.getGateway(gatewayId);
            if (gateway == null) {
                throw new GatewayNotFoundException();
            }
            IGatewayLink link = gatewayLinkFactory.create(gateway);
            return link;
        } catch (GatewayNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PublishingException(e.getMessage(), e);
        }
    }

    /**
     * Returns the private key of the default gateway
     * @return
     */
    public PrivateKey getDefaultGatewayPrivateKey() {
        try {
            return KeyUtils.getPrivateKey(get(getDefaultGateway().getId()).getJWTPrivKey());
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    /**
     * Returns the public key of the default gateway
     * @return
     */
    public String getDefaultGatewayPublicKey() {
        try {
            return get(getDefaultGateway().getId()).getJWTPubKey();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    /**
     * Returns the public key endpoint for the default gateway
     * @return
     */
    public String getDefaultGatewayPublicKeyEnpoint() {
        try {
            GatewayBean gatewayBean = get(getDefaultGateway().getId());
            return gatewayBean.getEndpoint()+gatewayBean.getJWTPubKeyEndpoint();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    /**
     * Returns a gatewaylink for the default gateway
     * @return
     */
    public IGatewayLink getDefaultGatewayLink(){
        try {
            return createGatewayLink(getDefaultGateway().getId());
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public Set<IGatewayLink> getApplicationVersionGatewayLinks(ApplicationVersionBean avb) throws StorageException {
        Set<IGatewayLink> gateways = new HashSet<>();
        switch (avb.getStatus()) {
            case Ready:
            case Created:
                gateways.add(getDefaultGatewayLink());
                break;
            case Registered:
                gateways.addAll(query.getGatewayIdsForApplicationVersionContracts(avb).stream().map(this::createGatewayLink).collect(Collectors.toSet()));
                break;
            case Retired:
                log.info("== Application {} is in status {}, no gateways ==", ConsumerConventionUtil.createAppUniqueId(avb), avb.getStatus());
                break;
            default:
                break;
        }
        return gateways;
    }
}
