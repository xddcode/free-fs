package com.free.fs.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户返回实体
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/26 15:54
 */
@Data
public class UserVO implements Serializable {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
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
    private List<String> roleList;

    /**
     * 权限集合
     */
    private List<String> authList;
}
