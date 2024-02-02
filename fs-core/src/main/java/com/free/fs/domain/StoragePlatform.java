package com.free.fs.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 存储平台实体类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 10:42
 */
@Data
@NoArgsConstructor
@Table(value = "f_storage_platform")
public class StoragePlatform implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 存储平台名称
     */
    private String name;

    /**
     * 存储平台标识符
     */
    private String identifier;

    /**
     * 存储平台配置描述
     */
    private String configScheme;
}
