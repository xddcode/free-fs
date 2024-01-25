package com.free.fs.service.impl;

import com.free.fs.domain.UserRole;
import com.free.fs.mapper.UserRoleMapper;
import com.free.fs.service.UserRoleService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.free.fs.domain.table.RoleTableDef.ROLE;
import static com.free.fs.domain.table.UserRoleTableDef.USER_ROLE;

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
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(ROLE.ROLE_CODE)
                .from(ROLE)
                .innerJoin(USER_ROLE).on(
                        ROLE.ID.eq(USER_ROLE.ROLE_ID).and(USER_ROLE.USER_ID.eq(userId))
                );
        return this.listAs(queryWrapper, String.class);
    }
}
