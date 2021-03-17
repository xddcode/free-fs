package com.free.fs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.free.fs.model.User;

/**
 * 用户表业务接口
 *
 * @author dinghao
 * @date 2021/3/16
 */
public interface UserService extends IService<User> {

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    boolean addUser(User user);
}
