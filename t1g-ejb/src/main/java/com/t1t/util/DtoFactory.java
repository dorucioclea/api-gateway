package com.t1t.util;

import com.t1t.apim.beans.dto.GatewayDtoBean;
import com.t1t.apim.beans.dto.PolicyDtoBean;
import com.t1t.apim.beans.dto.UserDtoBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.beans.policies.PolicyBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class DtoFactory {

    public static GatewayDtoBean createGatewayDtoBean(GatewayBean gw) {
        GatewayDtoBean rval = null;
        if (gw != null) {
            rval = new GatewayDtoBean();
            rval.setId(gw.getId());
            rval.setName(gw.getName());
            rval.setEndpoint(gw.getEndpoint());
            rval.setDescription(gw.getDescription());
            rval.setCreatedBy(gw.getCreatedBy());
            rval.setCreatedOn(gw.getCreatedOn());
            rval.setModifiedBy(gw.getModifiedBy());
            rval.setModifiedOn(gw.getModifiedOn());
            rval.setOAuthExpTime(gw.getOAuthExpTime());
            rval.setJWTExpTime(gw.getJWTExpTime());
            rval.setJWTPubKey(gw.getJWTPubKey());
            rval.setJWTPubKeyEndpoint(gw.getJWTPubKeyEndpoint());
            rval.setJWTPrivKey(gw.getJWTPrivKey());
            rval.setType(gw.getType());
            rval.setConfiguration(gw.getConfiguration());
        }
        return rval;
    }

    public static UserDtoBean createUserDtoBean(UserBean user) {
        UserDtoBean rval = null;
        if (user != null) {
            rval = new UserDtoBean();
            rval.setUsername(user.getUsername());
            rval.setKongUsername(user.getKongUsername());
            rval.setFullName(user.getFullName());
            rval.setEmail(user.getEmail());
            rval.setJoinedOn(user.getJoinedOn());
            rval.setAdmin(user.getAdmin());
            rval.setCompany(user.getCompany());
            rval.setLocation(user.getLocation());
            rval.setWebsite(user.getWebsite());
            rval.setBio(user.getBio());
            rval.setBase64pic(user.getBase64pic());
            rval.setJwtKey(user.getJwtKey());
            rval.setJwtSecret(user.getJwtSecret());
        }
        return rval;
    }

    public static PolicyDtoBean createPolicyDtoBean(PolicyBean policy) {
        PolicyDtoBean rval = null;
        if (policy != null) {
            rval = new PolicyDtoBean();
            rval.setId(policy.getId());
            rval.setType(policy.getType());
            rval.setOrganizationId(policy.getOrganizationId());
            rval.setEntityId(policy.getEntityId());
            rval.setEntityVersion(policy.getEntityVersion());
            rval.setName(policy.getName());
            rval.setDescription(policy.getDescription());
            rval.setConfiguration(policy.getConfiguration());
            rval.setCreatedBy(policy.getCreatedBy());
            rval.setCreatedOn(policy.getCreatedOn());
            rval.setModifiedBy(policy.getModifiedBy());
            rval.setModifiedOn(policy.getModifiedOn());
            rval.setDefinition(policy.getDefinition());
            rval.setOrderIndex(policy.getOrderIndex());
            rval.setKongPluginId(policy.getKongPluginId());
            rval.setContractId(policy.getContractId());
            rval.setGatewayId(policy.getGatewayId());
            rval.setEnabled(policy.isEnabled());
        }
        return rval;
    }

}