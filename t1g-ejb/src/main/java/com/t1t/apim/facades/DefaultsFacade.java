package com.t1t.apim.facades;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.defaults.DefaultsBean;
import com.t1t.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefaultsFacade {

    private static final Logger _LOG = LoggerFactory.getLogger(DefaultsFacade.class);

    @Inject
    private AppConfig config;

    @Inject
    private IStorage storage;

    public DefaultServiceTermsBean getDefaultServiceTerms() {
        try {
            DefaultsBean defaults = storage.getDefaults(config.getEnvironment());
            if (defaults != null) {
                if (defaults.getServiceTerms() == null) {
                    String fileTerms = readLocalTerms();
                    if (!StringUtils.isEmpty(fileTerms)) {
                        defaults.setServiceTerms(readLocalTerms());
                        storage.updateDefaults(defaults);
                        _LOG.debug("Defaults Updated:{}", defaults);
                    }
                }
            }
            else {
                defaults = new DefaultsBean(config.getEnvironment(), readLocalTerms());
                storage.createDefaults(defaults);
                _LOG.debug("Defaults Created:{}", defaults);
            }
            return new DefaultServiceTermsBean(defaults.getServiceTerms());
        }
        catch (IOException | StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }

    }

    public DefaultServiceTermsBean updateDefaultServiceTerms(DefaultServiceTermsBean bean) {
        try {
            DefaultsBean defaults = storage.getDefaults(config.getEnvironment());
            defaults.setServiceTerms(bean.getTerms());
            storage.updateDefaults(defaults);
            _LOG.debug("Defaults Updated:{}", defaults);
            return bean;
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }/*
        try {
            Path termsPath = Paths.get(config.getLocalFilePath(), "terms.md");
            if (!Files.exists(termsPath)) {
                Files.createDirectories(termsPath.getParent());
            }
            Files.write(termsPath, bean.getTerms().getBytes());
            _LOG.info("FILE CREATED: {}", termsPath.toString());
            return new DefaultServiceTermsBean(bean.getTerms());
        }
        catch (IOException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }*/
    }

    private String readLocalTerms() throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(config.getLocalFilePath(), "terms.md")), Charset.forName("UTF-8"));
        }
        catch (NoSuchFileException ex) {
            //Return an empty string if file is not found
            return "";
        }
    }
}