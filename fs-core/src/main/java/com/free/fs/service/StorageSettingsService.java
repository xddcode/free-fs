package com.free.fs.service;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.domain.StorageSettings;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 存储平台配置Service
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 13:03
 */
public interface StorageSettingsService extends IService<StorageSettings> {

    /**
     * 获取用户已启用的存储平台配置
     *
     * @param userId
     * @return
     */
    default StorageSettings getStorageSettingsByUserEnabled(Long userId) {
        return getStorageSettingsByUser(userId, CommonConstant.ENABLE);
    }

    /**
     * 获取用户存储平台配置
     *
     * @param userId
     * @param enabled
     * @return
     */
    StorageSettings getStorageSettingsByUser(Long userId, Integer enabled);

    /**
     * 获取用户已配置的存储平台列表
     *
     * @param userId
     * @return
     */
    List<StorageSettings> getListByUser(Long userId);

    boolean enabled();

    boolean save();
}
