package com.free.fs.service;

import com.free.fs.domain.StorageSettings;
import com.free.fs.domain.dto.StorageConfigDTO;
import com.free.fs.domain.vo.StorageSettingsVO;
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
     * @param userId 用户ID
     * @return
     */
    StorageSettings getByUserEnabled(Long userId);

    /**
     * 根据存储平台标识符获取用户平台配置
     *
     * @param userId             用户ID
     * @param platformIdentifier 存储平台标识符
     * @return
     */
    StorageSettings getByUserPlatformIdentifier(Long userId, String platformIdentifier);


    StorageSettingsVO getByVoUserPlatformIdentifier(Long userId, String platformIdentifier);

    /**
     * 获取用户已配置的存储平台列表
     *
     * @param userId 用户ID
     * @return
     */
    List<StorageSettings> getListByUser(Long userId);

    /**
     * 检查当前用户对此存储平台是否已经配置
     *
     * @param userId             用户ID
     * @param platformIdentifier 存储平台标识符
     * @return
     */
    boolean checkConfigByUser(Long userId, String platformIdentifier);

    /**
     * 切换存储平台
     *
     * @param userId             用户ID
     * @param platformIdentifier 存储平台标识符
     * @return
     */
    boolean toggleStoragePlatform(Long userId, String platformIdentifier);

    /**
     * 新增或修改存储平台配置
     *
     * @param storageConfigDTO 存储平台配置信息
     * @return
     */
    boolean saveOrUpdateConfig(StorageConfigDTO storageConfigDTO);
}
