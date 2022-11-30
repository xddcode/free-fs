package com.free.fs.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.free.fs.system.dto.InsertUserDto;
import com.free.fs.system.model.User;

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
    boolean addUser(InsertUserDto userDto);
}
