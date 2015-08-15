package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.gateways.*;
import com.t1t.digipolis.apim.beans.summary.GatewaySummaryBean;
import com.t1t.digipolis.apim.beans.summary.GatewayTestResultBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IGatewayResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Api(value = "/gateways", description = "The Gateway API.")
@Path("/gateways")
@ApplicationScoped
public class GatewayResource implements IGatewayResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    @APIEngineContext
    Logger log;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor.
     */
    public GatewayResource() {
    }

    @ApiOperation(value = "Test a Gateway",
            notes = "This endpoint is used to test the Gateway his settings prior to either creating or updating it.  The information will be used to attempt to create a link between the API Manager and the Gateway, by simply trying to ping the Gateway his status endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayTestResultBean.class, message = "The result of testing the Gateway settings.")
    })
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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

    @ApiOperation(value = "List All Gateways",
            notes = "This endpoint returns a list of all the Gateways that have been configured.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = GatewaySummaryBean.class, message = "A list of configured Gateways.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GatewaySummaryBean> list() throws NotAuthorizedException {
        try {
            return query.listGateways();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Create a Gateway",
            notes = "This endpoint is called to create a new Gateway.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayBean.class, message = "The newly created Gateway.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
            if (storage.getGateway(gateway.getId()) != null) {
                throw ExceptionFactory.gatewayAlreadyExistsException(gateway.getName());
            }
            // Store/persist the new gateway
            encryptPasswords(gateway);
            storage.createGateway(gateway);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        decryptPasswords(gateway);

        log.debug(String.format("Successfully created new gateway %s: %s", gateway.getName(), gateway)); //$NON-NLS-1$
        return gateway;
    }

    @ApiOperation(value = "Get a Gateway by ID",
            notes = "Call this endpoint to get the details of a single configured Gateway.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GatewayBean.class, message = "The Gateway identified by {gatewayId}.")
    })
    @GET
    @Path("/{gatewayId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GatewayBean get(@PathParam("gatewayId") String gatewayId) throws GatewayNotFoundException, NotAuthorizedException {
        try {
            GatewayBean bean = storage.getGateway(gatewayId);
            if (bean == null) {
                throw ExceptionFactory.gatewayNotFoundException(gatewayId);
            }
            if (!securityContext.isAdmin()) {
                bean.setConfiguration(null);
            } else {
                decryptPasswords(bean);
            }
            log.debug(String.format("Successfully fetched gateway %s: %s", bean.getName(), bean)); //$NON-NLS-1$
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Update a Gateway",
            notes = "Use this endpoint to update an existing Gateway.  Note that the name of the Gateway cannot be changed, as the name is tied closely with the Gateway his ID.  If you wish to rename the Gateway you must remove it and create a new one.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{gatewayId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("gatewayId") String gatewayId, UpdateGatewayBean bean) throws GatewayNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
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
            encryptPasswords(gbean);
            storage.updateGateway(gbean);
            log.debug(String.format("Successfully updated gateway %s: %s", gbean.getName(), gbean)); //$NON-NLS-1$
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Delete a Gateway",
            notes = "This endpoint deletes a Gateway by its unique ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{gatewayId}")
    public void remove(@PathParam("gatewayId") String gatewayId) throws GatewayNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
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
