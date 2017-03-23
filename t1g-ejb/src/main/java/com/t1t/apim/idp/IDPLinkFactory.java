package com.t1t.apim.idp;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IDPLinkFactory {

    IDPClient getIDPClient(String idpId);

    IDPClient getDefaultIDPClient();

}