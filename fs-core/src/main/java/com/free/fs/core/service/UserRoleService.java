package com.free.fs.core.service;

import com.free.fs.core.domain.UserRole;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 用户角色服务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:08
 */
public interface UserRoleService extends IService<UserRole> {

    List<String> getRoleByUserId(Long userId);
}
