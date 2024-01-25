package com.free.fs.service;

import com.free.fs.domain.RolePermission;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 角色权限关联表业务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/30 10:14
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 根据角色code码集合查询所有权限标识
     *
     * @param roleCodes 角色code码集合
     * @return
     */
    List<String> getPermissionByRoles(List<String> roleCodes);
}
