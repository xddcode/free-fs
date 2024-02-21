package com.free.fs.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 存储平台配置
 * @author Yann
 */
@Data
public class StoragePlatformVO {

    /**
     * 存储平台名称
     */
    public String name;

    /**
     * 存储平台唯一标识
     */
    public String identifier;

    /**
     * 配置状态: true: 已配置, false: 未配置
     */
    public Boolean status;

    /**
     * 启用状态: 0: 未启用, 1: 已启用
     */
    public Boolean enabled;

    /**
     * 配置结构
     */
    public List<PlatformStructureVO> configScheme;

    /**
     * 存储平台icon
     */
    public String icon;
}
