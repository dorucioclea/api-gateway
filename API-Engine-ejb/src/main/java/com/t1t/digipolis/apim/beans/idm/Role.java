package com.t1t.digipolis.apim.beans.idm;

/**
 * Created by michallispashidis on 01/12/15.
 */
public enum Role {
    OWNER("Owner"),WATCHER("Watcher"),DEVELOPER("Developer");
    String refId;
    Role(String refString){
        this.refId=refString;
    }

    @Override
    public String toString() {
        return refId;
    }
}
