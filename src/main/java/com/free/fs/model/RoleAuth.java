package com.free.fs.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限关联表实体
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Data
@TableName("sys_role_auth")
@EqualsAndHashCode(callSuper = true)
public class RoleAuth extends Model <RoleAuth>{

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long authId;
}
