package com.free.fs.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 用户表实体
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class User extends Model<User> {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 角色集合
     */
    @TableField(exist = false)
    private List<String> roleList;

    /**
     * 权限集合
     */
    @TableField(exist = false)
    private List<String> authList;
}
