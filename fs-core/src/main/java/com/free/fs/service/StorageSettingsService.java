package com.free.fs.service;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.domain.StorageSettings;
import com.mybatisflex.core.service.IService;

/**
 * 存储平台配置Service
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 13:03
 */
public interface StorageSettingsService extends IService<StorageSettings> {

    default StorageSettings getStorageSettingsByUserEnabled(Long userId) {
        return getStorageSettingsByUser(userId, CommonConstant.ENABLE);
    }

    StorageSettings getStorageSettingsByUser(Long userId, Integer enabled);
}
