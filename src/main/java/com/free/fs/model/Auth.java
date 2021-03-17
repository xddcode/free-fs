package com.free.fs.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限表实体
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Data
@TableName("sys_auth")
@EqualsAndHashCode(callSuper = true)
public class Auth extends Model<Auth> {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 权限标识
     */
    private String authCode;

    /**
     * 权限名称
     */
    private String authName;
}
