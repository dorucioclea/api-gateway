package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestKeyAuthBean extends AbstractAuthConsumerRequest {
    private String optionalKey;//optional

    public String getOptionalKey() {
        return optionalKey;
    }

    public void setOptionalKey(String optionalKey) {
        this.optionalKey = optionalKey;
    }

    @Override
    public String toString() {
        return "AuthConsumerRequestKeyAuthBean{" +
                "optionalKey='" + optionalKey + '\'' +
                '}'+"-"+super.toString();
    }
}
