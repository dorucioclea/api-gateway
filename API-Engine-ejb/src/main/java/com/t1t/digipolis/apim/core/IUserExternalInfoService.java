package com.t1t.digipolis.apim.core;

import com.t1t.digipolis.apim.beans.idm.ExternalUserBean;
import com.t1t.digipolis.apim.exceptions.ExternalUserNotFoundException;
import org.omg.CORBA.UserException;

/**
 * Created by michallispashidis on 23/11/15.
 */
public interface IUserExternalInfoService {
    /**
     * Get user information based on a user property and it's value.
     *
     * @param key       the user property to look for
     * @param value     the user value for the given key
     * @return
     */
    ExternalUserBean getUserInfoByQuery(String key, String value)throws ExternalUserNotFoundException;
    ExternalUserBean getUserInfoByUsername(String username)throws ExternalUserNotFoundException;
    ExternalUserBean getUserInfoByMail(String email)throws ExternalUserNotFoundException;
    ExternalUserBean getUserInfoByUserId(String userId)throws ExternalUserNotFoundException;

}
