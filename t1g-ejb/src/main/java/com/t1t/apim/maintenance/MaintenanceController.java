package com.t1t.apim.maintenance;

import com.t1t.apim.beans.operation.OperatingBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@ApplicationScoped
@TransactionManagement(TransactionManagementType.CONTAINER)
@Singleton
public class MaintenanceController {

    @Inject
    private IStorageQuery query;
    @Inject
    private IStorage storage;

    private OperatingBean maintenanceMode;

    public void getStatus() {
        try {
            maintenanceMode = query.getMaintenanceModeStatus();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public Boolean isEnabled() {
        getStatus();
        return maintenanceMode.isEnabled();
    }

    public String getMessage() {
        getStatus();
        return maintenanceMode.getMessage();
    }

    public void enableMaintenanceMode(String message) {
        getStatus();
        maintenanceMode.setEnabled(true);
        maintenanceMode.setMessage(message);
        try {
            storage.updateOperatingBean(maintenanceMode);
        }
        catch (StorageException ex) {
            maintenanceMode.setEnabled(false);
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void disableMaintenanceMode() {
        getStatus();
        if (maintenanceMode.isEnabled()) {
            String oldMessage = maintenanceMode.getMessage();
            maintenanceMode.setMessage(null);
            maintenanceMode.setEnabled(false);
            try {
                storage.updateOperatingBean(maintenanceMode);
            }
            catch (StorageException ex) {
                maintenanceMode.setEnabled(true);
                maintenanceMode.setMessage(oldMessage);
                throw ExceptionFactory.systemErrorException(ex);
            }
        }
    }

    public void updateMaintenanceMessage(String message) {
        getStatus();
        try {
            maintenanceMode.setMessage(message);
            storage.updateOperatingBean(maintenanceMode);
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }
}