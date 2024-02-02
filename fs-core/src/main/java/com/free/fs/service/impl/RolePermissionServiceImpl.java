package com.free.fs.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.free.fs.domain.RolePermission;
import com.free.fs.mapper.RolePermissionMapper;
import com.free.fs.service.RolePermissionService;
import com.free.fs.service.UserRoleService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.free.fs.domain.table.PermissionTableDef.PERMISSION;
import static com.free.fs.domain.table.RolePermissionTableDef.ROLE_PERMISSION;
import static com.free.fs.domain.table.RoleTableDef.ROLE;
import static com.mybatisflex.core.query.QueryMethods.distinct;

/**
 * 角色权限关联表业务实现类
 *
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
        return getPermissionByRoles(roleCodes);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userRoleService.getRoleByUserId(Long.parseLong(String.valueOf(loginId)));
    }

    @Override
    public List<String> getPermissionByRoles(List<String> roleCodes) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(distinct(PERMISSION.PERMISSION_CODE))
                .from(ROLE_PERMISSION)
                .leftJoin(ROLE).on(
                        ROLE_PERMISSION.ROLE_ID.eq(ROLE.ID)
                )
                .leftJoin(PERMISSION).on(
                        ROLE_PERMISSION.PERMISSION_ID.eq(PERMISSION.ID)
                )
                .where(ROLE.ROLE_CODE.in(roleCodes));
        return this.listAs(queryWrapper, String.class);
    }
}
