package com.free.fs.service.impl;

import com.free.fs.domain.StoragePlatform;
import com.free.fs.service.StoragePlatformService;
import com.mybatisflex.core.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yann
 */
@Slf4j
@Service
public class StoragePlatformServiceImpl implements StoragePlatformService {

    @Override
    public StoragePlatform getStoragePlatformByIdentifier(String identifier) {
        return null;
    }

    @Override
    public BaseMapper<StoragePlatform> getMapper() {
        return null;
    }
}
