package com.free.fs.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author Yann
 */
@Data
public class StorageSettingsVO {

    private Long id;

    /**
     * 所属存储平台标识符
     */
    private String platformIdentifier;

    /**
     * 存储平台配置
     */
    private Map<String, Object> configData;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
