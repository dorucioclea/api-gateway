package com.t1t.apim.core;

import com.t1t.apim.beans.idm.ExternalUserBean;
import com.t1t.apim.exceptions.ExternalUserNotFoundException;

/**
 * Created by michallispashidis on 23/11/15.
 */
public interface IUserExternalInfoService {
    /**
     * Get user information based on a user property and it's value.
     *
     * @param key   the user property to look for
     * @param value the user value for the given key
     * @return
     */
    ExternalUserBean getUserInfoByQuery(String key, String value) throws ExternalUserNotFoundException;

    ExternalUserBean getUserInfoByUsername(String username) throws ExternalUserNotFoundException;

    ExternalUserBean getUserInfoByMail(String email) throws ExternalUserNotFoundException;

    ExternalUserBean getUserInfoByUserId(String userId) throws ExternalUserNotFoundException;

}
