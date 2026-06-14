package flearn.module.management.service.impl;

import flearn.entity.SystemSetting;
import flearn.repository.SystemSettingRepository;
import flearn.module.management.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemSettingServiceImpl implements SystemSettingService {
    private final SystemSettingRepository systemSettingRepository;

    @Override
    public boolean isMaintenanceMode() {
        return systemSettingRepository.findBySettingKey(MAINTENANCE_MODE_KEY)
                .map(SystemSetting::getSettingValue)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    @Override
    @Transactional
    public void setMaintenanceMode(boolean enabled) {
        SystemSetting setting = systemSettingRepository.findBySettingKey(MAINTENANCE_MODE_KEY)
                .orElseGet(() -> SystemSetting.builder()
                        .settingKey(MAINTENANCE_MODE_KEY)
                        .build());
        setting.setSettingValue(Boolean.toString(enabled));
        systemSettingRepository.save(setting);
    }
}
