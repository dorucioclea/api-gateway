package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.config.Version;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.rest.resources.ISystemResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Implementation of the System API.
 */
@ApplicationScoped
public class SystemResourceImpl implements ISystemResource {

    @Inject
    private IStorage storage;
    @Inject
    private Version version;

    /**
     * Constructor.
     */
    public SystemResourceImpl() {
    }

    /**
     * @see ISystemResource#getStatus()
     */
    @Override
    public SystemStatusBean getStatus() {
        SystemStatusBean rval = new SystemStatusBean();
        rval.setId("apim-manager-api"); //$NON-NLS-1$
        rval.setName("API Manager REST API"); //$NON-NLS-1$
        rval.setDescription("The API Manager REST API is used by the API Manager UI to get stuff done.  You can use it to automate any api task you wish.  For example, create new Organizations, Plans, Applications, and Services."); //$NON-NLS-1$
        rval.setMoreInfo("http://www.trust1team.com"); //$NON-NLS-1$
        rval.setUp(getStorage() != null);
        if (getVersion() != null) {
            rval.setVersion(getVersion().getVersionString());
            rval.setBuiltOn(getVersion().getVersionDate());
        }
        return rval;
    }

    /**
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    /**
     * @return the version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Version version) {
        this.version = version;
    }
}
