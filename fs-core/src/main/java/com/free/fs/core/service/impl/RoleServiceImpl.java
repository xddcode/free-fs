package com.free.fs.core.service.impl;

import com.free.fs.core.domain.Role;
import com.free.fs.core.mapper.RoleMapper;
import com.free.fs.core.service.RoleService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import static com.free.fs.core.domain.table.RoleTableDef.ROLE;

/**
 * 角色表业务接口实现
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/24 16:31
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * 根据角色编码获取角色ID
     *
     * @param code 角色编码
     * @return 角色ID
     */
    @Override
    public Long getIdByCode(String code) {
        Role role = this.getOne(new QueryWrapper().where(ROLE.ROLE_CODE.eq(code)));
        if (role == null) {
            return null;
        }
        return role.getId();
    }
}
