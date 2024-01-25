package com.free.fs.service;

import com.free.fs.domain.User;
import com.free.fs.domain.dto.LoginBody;
import com.free.fs.domain.dto.UserDto;
import com.mybatisflex.core.service.IService;

/**
 * 用户表业务接口
 *
 * @author dinghao
 * @date 2021/3/16
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     *
     * @param body 登录信息
     * @return 是否登录成功
     */
    boolean doLogin(LoginBody body);

    /**
     * 新增用户
     *
     * @param userDto 用户对象
     * @return 是否新增成功
     */
    boolean addUser(UserDto userDto);
}
