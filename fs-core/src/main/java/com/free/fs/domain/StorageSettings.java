package com.free.fs.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 存储平台配置
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 10:47
 */
@Data
@NoArgsConstructor
@Table(value = "f_storage_settings")
public class StorageSettings implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 所属存储平台标识符
     */
    private String platformIdentifier;

    /**
     * 存储平台配置
     */
    private String configData;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
