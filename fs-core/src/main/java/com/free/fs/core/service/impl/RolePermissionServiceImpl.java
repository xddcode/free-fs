package com.free.fs.core.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.free.fs.core.domain.RolePermission;
import com.free.fs.core.mapper.RolePermissionMapper;
import com.free.fs.core.service.RolePermissionService;
import com.free.fs.core.service.UserRoleService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 15:07
 */
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService, StpInterface {

    private final UserRoleService userRoleService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> roleCodes = getRoleList(loginId, loginType);

        return mapper.selectPermissionByRoles(roleCodes);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userRoleService.getRoleByUserId(Long.parseLong(String.valueOf(loginId)));
    }
}
