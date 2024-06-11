package com.free.fs.core.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 10:58
 */
@Data
@Table("sys_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id(keyType = KeyType.Auto)
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
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 唯一uuid
     */
    private String uuid;

    /**
     * 注册时间
     */
    private Date createTime;


    /**
     * 角色集合
     */
    @Column(ignore = true)
    private List<String> roleList;

    /**
     * 权限集合
     */
    @Column(ignore = true)
    private List<String> authList;
}
