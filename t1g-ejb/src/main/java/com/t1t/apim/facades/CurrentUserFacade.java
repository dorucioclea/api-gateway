package com.t1t.apim.facades;

import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.authorization.OAuth2Token;
import com.t1t.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.idm.*;
import com.t1t.apim.beans.pagination.OAuth2TokenPaginationBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.kong.model.KongOAuthToken;
import com.t1t.kong.model.KongOAuthTokenList;
import com.t1t.util.CustomCollectors;
import com.t1t.util.GatewayPaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.*;

/**
 * @author michallispashidis
 * @since 2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CurrentUserFacade {
    private static final Logger log = LoggerFactory.getLogger(CurrentUserFacade.class.getName());
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IStorageQuery query;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private IStorage storage;
    @Inject
    private GatewayFacade gatewayFacade;

    public CurrentUserBean getInfo() {
        String userId = securityContext.getCurrentUser();
        try {
            CurrentUserBean rval = new CurrentUserBean();
            UserBean user = idmStorage.getUser(userId);
            if (user == null) {
                user = new UserBean();
                user.setUsername(userId);
                if (securityContext.getFullName() != null) {
                    user.setFullName(securityContext.getFullName());
                } else {
                    user.setFullName(userId);
                }
                if (securityContext.getEmail() != null) {
                    user.setEmail(securityContext.getEmail());
                } else {
                    user.setEmail(userId + "@example.org");
                }
                user.setJoinedOn(new Date());
                try {
                    idmStorage.createUser(user);
                } catch (StorageException e1) {
                    throw new SystemErrorException(e1);
                }
                rval.initFromUser(user);
                rval.setAdmin(securityContext.isAdmin());
                rval.setPermissions(new HashSet<PermissionBean>());
            } else {
                rval.initFromUser(user);
                Set<PermissionBean> permissions = idmStorage.getPermissions(userId);
                if (securityContext.isAdmin()) {
                    rval.setPermissions(idmStorage.getAllPermissions());
                } else {
                    rval.setPermissions(permissions);
                }
                rval.setAdmin(securityContext.isAdmin());
            }

            log.debug(String.format("Getting info for user %s", user.getUsername())); //$NON-NLS-1$
            return rval;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public void updateInfo(UpdateUserBean info) {
        try {
            String username = securityContext.getCurrentUser();
            UserBean user = idmStorage.getUser(username);
            if (user == null) {
                throw new StorageException("User not found: " + username); //$NON-NLS-1$
            }
            if (info.getEmail() != null) user.setEmail(info.getEmail());
            if (info.getFullName() != null) user.setFullName(info.getFullName());
            if (info.getPic() != null) user.setBase64pic(info.getPic());
            else user.setBase64pic("");
            if (info.getCompany() != null) user.setCompany(info.getCompany());
            if (info.getLocation() != null) user.setLocation(info.getLocation());
            if (info.getWebsite() != null) user.setWebsite(info.getWebsite());
            if (info.getBio() != null) user.setBio(info.getBio());

            idmStorage.updateUser(user);
            log.debug(String.format("Successfully updated user %s: %s", user.getUsername(), user)); //$NON-NLS-1$
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getAppOrganizations() {
        try {
            return enrichOrgCounters(query.getOrgs(getPermittedOrganizations(PermissionType.appView)));
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getPlanOrganizations() {
        try {
            return query.getOrgs(getPermittedOrganizations(PermissionType.planView));
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<OrganizationSummaryBean> getServiceOrganizations() {
        try {
            return enrichOrgCounters(query.getOrgs(getPermittedOrganizations(PermissionType.svcView)));
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ApplicationSummaryBean> getApplications() {
        try {
            return query.getApplicationsInOrgs(getPermittedOrganizations(PermissionType.appView));
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public List<ServiceSummaryBean> getServices() {
        try {
            return query.getServicesInOrgs(getPermittedOrganizations(PermissionType.svcView));
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * Adds org counters for:
     * <ul>
     * <li>member count</li>
     * <li>locked plan count</li>
     * <li>published services</li>
     * <li>registered apps</li>
     * <li>events destination org</li>
     * </ul>
     *
     * @param organizationSummaryList
     * @return
     */
    private List<OrganizationSummaryBean> enrichOrgCounters(List<OrganizationSummaryBean> organizationSummaryList) throws StorageException {
        if (organizationSummaryList != null && organizationSummaryList.size() > 0) {
            for (OrganizationSummaryBean orgSumBean : organizationSummaryList) {
                final Integer eventCountForOrg = query.getEventCountForOrg(orgSumBean.getId());
                final Integer memberCountForOrg = query.getMemberCountForOrg(orgSumBean.getId());
                final Integer lockedPlanCountForOrg = query.getLockedPlanCountForOrg(orgSumBean.getId());
                final Integer publishedServiceCountForOrg = query.getPublishedServiceCountForOrg(orgSumBean.getId());
                final Integer registeredApplicationCountForOrg = query.getRegisteredApplicationCountForOrg(orgSumBean.getId());
                orgSumBean.setNumApps(registeredApplicationCountForOrg);
                orgSumBean.setNumMembers(memberCountForOrg);
                orgSumBean.setNumPlans(lockedPlanCountForOrg);
                orgSumBean.setNumServices(publishedServiceCountForOrg);
                orgSumBean.setNumEvents(eventCountForOrg);
            }
            return organizationSummaryList;
        }
        return organizationSummaryList;
    }

    /**
     * Returns an authenticated user's oauth2 tokens
     *
     * @return
     */
    public OAuth2TokenPaginationBean getCurrentUserOAuth2Tokens(String offset) {
        OAuth2TokenPaginationBean rval = new OAuth2TokenPaginationBean();
        rval.setTotal(0L);
        Set<OAuth2Token> tmpResult = new HashSet<>();
        Map<String, String> offsets = StringUtils.isEmpty(offset) ? new HashMap<>() : GatewayPaginationUtil.decodeOffsets(offset);
        Map<String, String> nextOffsets = new HashMap<>();
        try {
            List<GatewayBean> gatewayBeen = query.getAllGateways();
            for (GatewayBean gatewayBean : gatewayBeen) {
                KongOAuthTokenList tokens = null;
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayBean.getId());
                if (!offsets.isEmpty()) {
                    if (offsets.containsKey(gatewayBean.getId())) {
                        tokens = gateway.getConsumerOAuthTokenListByUserId(securityContext.getCurrentUser(), offsets.get(gateway.getGatewayId()));
                    }
                } else {
                    tokens = gateway.getConsumerOAuthTokenListByUserId(securityContext.getCurrentUser(), null);
                }
                if (tokens != null) {
                    rval.setTotal(rval.getTotal() + tokens.getTotal());
                    if (!StringUtils.isEmpty(tokens.getOffset())) {
                        nextOffsets.put(gateway.getGatewayId(), tokens.getOffset());
                    }
                    Map<String, String> gatewayAppCredentials = new HashMap<>();
                    tokens.getData().stream().map(KongOAuthToken::getCredentialId).distinct().forEach(credId -> {
                        gatewayAppCredentials.put(credId, gateway.getApplicationOAuthInformationByCredentialId(credId).getData().stream().map(oauth -> gateway.getConsumer(oauth.getConsumerId())).distinct().collect(CustomCollectors.getSingleResult()).getCustomId());
                    });
                    for (KongOAuthToken token : tokens.getData()) {
                        String[] appId = gatewayAppCredentials.get(token.getCredentialId()).split("\\.");
                        if (appId.length == 3) {
                            ApplicationVersionBean avb = storage.getApplicationVersion(appId[0], appId[1], appId[2]);
                            if (avb != null) {
                                tmpResult.add(new OAuth2Token(token, gatewayBean.getId(), avb));
                            }
                        }
                    }
                }
            }
            if (!tmpResult.isEmpty()) {
                rval.setCurrentPage(offsets.isEmpty() ? null : offset);
                rval.setNextPage(nextOffsets.isEmpty() ? null : GatewayPaginationUtil.encodeOffsets(nextOffsets));
            }
            rval.setData(tmpResult);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
        return rval;
    }

    public void revokeCurrentUserOAuth2Token(OAuth2TokenRevokeBean token) {
        IGatewayLink gateway = gatewayFacade.createGatewayLink(token.getGatewayId());
        KongOAuthTokenList gwTokenList = gateway.getOAuthToken(token.getId());
        for (KongOAuthToken gwToken : gwTokenList.getData()) {
            if (securityContext.getCurrentUser().equals(gwToken.getAuthenticatedUserid())) {
                gateway.revokeOAuthToken(token.getId());
            } else throw ExceptionFactory.notAuthorizedException();
        }
    }

    private Set<String> getPermittedOrganizations(PermissionType type) {
        String currentUser = securityContext.getCurrentUser();
        Set<String> rval;
        try {
            if (!StringUtils.isEmpty(currentUser) && idmStorage.getUser(currentUser).getAdmin()) {
                rval = storage.getAllOrganizations();
            } else {
                rval = securityContext.getPermittedOrganizations(type);
            }
        } catch (StorageException e) {
            throw ExceptionFactory.userNotFoundException(currentUser);
        }
        return rval == null ? new TreeSet<>() : rval;
    }
}
