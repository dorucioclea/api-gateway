package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import org.bouncycastle.util.encoders.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * Created by michallispashidis on 17/11/15.
 */
public class KeyUtils {
    public static PublicKey getKey(String key){
        try{
            byte[] byteKey = Base64.decode(key.getBytes());
            //byte[] byteKey = Base64.decode(key.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to validate if all contractbeans have the same apikey. This method can be
     * executed before application registraiton in order to be sure the apikey convention
     * has been maintained. When that's not the case, a sync of apikeys can be performed
     * from the API Manager db towards all targeted gateways.
     *
     * @param contractBeans
     * @return
     */
    public static boolean validateKeySet(List<ContractSummaryBean> contractBeans){
        if(contractBeans.size()>0){
            String refApiKey = contractBeans.get(0).getApikey().toString();
            for(ContractSummaryBean bean:contractBeans){
                if(!refApiKey.equalsIgnoreCase(bean.getApikey().toString()))return false;
            }
        }
        return true;
    }
}
