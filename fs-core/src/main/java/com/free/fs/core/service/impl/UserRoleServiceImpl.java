package com.free.fs.core.service.impl;

import com.free.fs.core.domain.UserRole;
import com.free.fs.core.mapper.UserRoleMapper;
import com.free.fs.core.service.UserRoleService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色关联表业务实现
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/30 10:20
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 根据用户ID获取角色编码
     *
     * @param userId 用户ID
     * @return 角色编码集合
     */
    @Override
    public List<String> getRoleByUserId(Long userId) {
        return mapper.selectRoleByUserId(userId);
    }
}
