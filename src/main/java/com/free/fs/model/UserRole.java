package com.free.fs.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联表实体
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Data
@TableName("sys_user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRole extends Model<UserRole> {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;
}
