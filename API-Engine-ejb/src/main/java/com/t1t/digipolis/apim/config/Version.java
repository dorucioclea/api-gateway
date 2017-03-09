package com.t1t.digipolis.apim.config;

import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

/**
 * Accessor - used to get the current version of the app.
 *
 */
@ApplicationScoped
public class Version {
    
    private String versionString;
    private String versionDate;
    
    /**
     * Constructor.
     */
    public Version() {
    }
    
    @PostConstruct
    public void postConstruct() {
        load();
    }

    /**
     * Loads the version info from version.properties.
     */
    private void load() {
        URL url = Version.class.getResource("version.properties"); //$NON-NLS-1$
        if (url == null) {
            this.versionString = "Unknown"; //$NON-NLS-1$
            this.versionDate = new Date().toString();
        } else {
            InputStream is = null;
            Properties props = new Properties();
            try {
                is = url.openStream();
                props.load(is);
                this.versionString = props.getProperty("version", "Unknown"); //$NON-NLS-1$ //$NON-NLS-2$
                this.versionDate = props.getProperty("date", new Date().toString()); //$NON-NLS-1$
            } catch (IOException e) {
                throw ExceptionFactory.systemErrorException(e);
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
     * @return the versionString
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * @return the versionDate
     */
    public String getVersionDate() {
        return versionDate;
    }

}
