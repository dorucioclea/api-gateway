package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.dto.UserDtoBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.dto.GatewayDtoBean;
import com.t1t.digipolis.apim.beans.idm.UserBean;

import java.util.Date;

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

}