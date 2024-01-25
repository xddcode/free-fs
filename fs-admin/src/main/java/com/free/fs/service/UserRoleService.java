package com.free.fs.service;

import com.free.fs.domain.UserRole;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 用户角色关联表业务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/30 10:19
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID获取角色编码
     *
     * @param userId 用户ID
     * @return 角色编码集合
     */
    List<String> getRoleByUserId(Long userId);
}
