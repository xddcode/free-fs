package com.free.fs.core.service;

import com.free.fs.core.domain.Role;
import com.mybatisflex.core.service.IService;

/**
 * 角色表业务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/24 16:30
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色编码获取角色ID
     *
     * @param code 角色编码
     * @return 角色ID
     */
    Long getIdByCode(String code);
}
