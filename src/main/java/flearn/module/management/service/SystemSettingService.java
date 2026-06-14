package flearn.module.management.service;

public interface SystemSettingService {
    String MAINTENANCE_MODE_KEY = "MAINTENANCE_MODE";

    boolean isMaintenanceMode();

    void setMaintenanceMode(boolean enabled);
}
