package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class GatewayPaginationUtil {

    private static final Logger _LOG = LoggerFactory.getLogger(GatewayPaginationUtil.class);

    public static final Map<String, String> decodeOffsets(String encodedOffset) {
        try {
            Map<String, String> rval = new HashMap<>();
            String decodedOffsets = new String(Base64.decodeBase64(encodedOffset));
            String[] keyValuePairs = decodedOffsets.split(";");
            for (String keyValuePair : keyValuePairs) {
                String[] parsed = keyValuePair.split(" : ");
                rval.put(parsed[0], parsed[1]);
            }
            return rval;
        }
        catch (Exception ex) {
            throw ExceptionFactory.invalidArgumentException("invalidOffset");
        }
    }

    public static final String encodeOffsets(Map<String, String> offsets) {
        StringBuilder builder = new StringBuilder("");
        Iterator<String> it = offsets.keySet().iterator();
        while (it.hasNext()) {
            String gatewayId = it.next();
            builder.append(gatewayId)
                    .append(" : ")
                    .append(offsets.get(gatewayId));
            if (it.hasNext()) builder.append(";");
        }
        return Base64.encodeBase64String(builder.toString().getBytes());
    }

}