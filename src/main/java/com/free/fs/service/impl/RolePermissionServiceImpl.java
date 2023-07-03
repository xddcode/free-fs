package com.free.fs.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.free.fs.mapper.RolePermissionMapper;
import com.free.fs.model.RolePermission;
import com.free.fs.service.RolePermissionService;
import com.free.fs.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/30 10:15
 */
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService, StpInterface {

    private final UserRoleService userRoleService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> roleCodes = getRoleList(loginId, loginType);
        return baseMapper.selectPermissionByRoles(roleCodes);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userRoleService.getRoleByUserId(Long.parseLong(String.valueOf(loginId)));
    }
}
