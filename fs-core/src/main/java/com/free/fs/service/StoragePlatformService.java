package com.free.fs.service;

import com.free.fs.domain.StoragePlatform;
import com.free.fs.domain.vo.StoragePlatformVO;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 存储平台业务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 10:46
 */
public interface StoragePlatformService extends IService<StoragePlatform> {

    /**
     * 获取存储平台列表
     *
     * @return
     */
    List<StoragePlatform> getList();

    List<StoragePlatformVO> getConfigList();

    /**
     * 根据存储平台ID获取存储平台信息
     *
     * @param identifier 存储平台标识符
     * @return 存储平台信息
     */
    StoragePlatform getStoragePlatformByIdentifier(String identifier);

    /**
     * 检查存储平台配置方案是否合法
     *
     * @param identifier 存储平台标识符
     * @param config     存储平台配置
     * @return 是否合法
     */
    boolean checkConfigScheme(String identifier, String config);
}
