package com.free.fs.core.service;

import com.free.fs.core.domain.dto.LoginBody;
import com.free.fs.core.domain.User;
import com.free.fs.core.domain.dto.UpdatePasswordDTO;
import com.free.fs.core.domain.dto.UserDTO;
import com.mybatisflex.core.service.IService;

/**
 * 用户服务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:08
 */
public interface UserService extends IService<User> {
    /**
     * 登录
     *
     * @param body
     * @return
     */
    boolean doLogin(LoginBody body);

    /**
     * 新增用户
     *
     * @param dto
     * @return
     */
    boolean addUser(UserDTO dto);

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    boolean updatePassword(UpdatePasswordDTO dto);
}
