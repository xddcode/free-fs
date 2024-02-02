package com.free.fs.core;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.enums.StorageType;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.core.storage.AliyunOssStorage;
import com.free.fs.core.storage.MinioStorage;
import com.free.fs.domain.StoragePlatform;
import com.free.fs.domain.StorageSettings;
import com.free.fs.service.StoragePlatformService;
import com.free.fs.service.StorageSettingsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 默认文件上传平台工厂
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 13:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IStorageFactory implements IFileStorageProvider {

    private final StoragePlatformService storagePlatformService;

    private final StorageSettingsService storageSettingsService;

    private static final Map<StorageType, Supplier<IFileStorage>> storageProviders = new HashMap<>();

    @PostConstruct
    public void storageProviders() {

    }

    @Override
    public IFileStorage getStorage() {
        //获取当前用户的配置信息
        Long UserId = StpUtil.getLoginIdAsLong();
        StorageSettings storageSettings = storageSettingsService.getStorageSettingsByUserEnabled(UserId);
        if (storageSettings == null) {
            log.error("用户{}未配置存储平台", UserId);
            throw new BusinessException("用户未配置存储平台");
        }
        //根据配置信息获取对应的存储平台
        String platformIdentifier = storageSettings.getPlatformIdentifier();
        StoragePlatform storagePlatform = storagePlatformService.getStoragePlatformByIdentifier(platformIdentifier);
        if (storagePlatform == null) {
            log.error("存储平台{}不存在", platformIdentifier);
            throw new BusinessException("当前存储平台不存在");
        }
        return getFileStorage(platformIdentifier, storageSettings.getConfigData());
    }

    private IFileStorage getFileStorage(String platformIdentifier, String config) {
        StorageType storageType = StorageType.getStorageType(platformIdentifier);
        return switch (Objects.requireNonNull(storageType)) {
            case StorageType.Local -> new MinioStorage(config);
            case StorageType.Minio -> new AliyunOssStorage(config);
            case StorageType.AliyunOSS -> new AliyunOssStorage(config);
            case StorageType.Qiniu -> new AliyunOssStorage(config);
            default -> throw new BusinessException("不支持的存储平台");
        };
    }
}
