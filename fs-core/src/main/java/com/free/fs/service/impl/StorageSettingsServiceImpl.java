package com.free.fs.service.impl;

import com.free.fs.domain.StorageSettings;
import com.free.fs.service.StorageSettingsService;
import com.mybatisflex.core.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yann
 */
@Slf4j
@Service
public class StorageSettingsServiceImpl implements StorageSettingsService {
    @Override
    public StorageSettings getStorageSettingsByUser(Long userId, Integer enabled) {
        return null;
    }

    @Override
    public BaseMapper<StorageSettings> getMapper() {
        return null;
    }
}
