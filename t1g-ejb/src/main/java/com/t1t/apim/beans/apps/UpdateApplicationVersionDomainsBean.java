package com.t1t.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateApplicationVersionDomainsBean {

    private Set<String> domains;

    public Set<String> getDomains() {
        return domains;
    }

    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }

    @Override
    public String toString() {
        return "UpdateApplicationVersionDomainsBean{" +
                "domains=" + domains +
                '}';
    }
}