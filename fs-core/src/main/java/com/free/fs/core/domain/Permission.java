package com.free.fs.core.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限实体类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:01
 */
@Data
@Table("sys_permission")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 权限标识
     */
    private String PermissionCode;

    /**
     * 权限名称
     */
    private String PermissionName;
}
