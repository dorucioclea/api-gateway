package com.t1t.digipolis.apim.beans.services;

/**
 * Created by michallispashidis on 3/10/15.
 */
public class UpdateServiceTearmsBean {
    private String terms;

    public UpdateServiceTearmsBean() {
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "UpdateServiceTearmsBean{" +
                "terms='" + terms + '\'' +
                '}';
    }
}
