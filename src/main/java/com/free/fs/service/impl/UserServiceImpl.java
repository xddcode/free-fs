package com.free.fs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.shiro.EndecryptUtil;
import com.free.fs.mapper.RoleMapper;
import com.free.fs.mapper.UserMapper;
import com.free.fs.mapper.UserRoleMapper;
import com.free.fs.model.Role;
import com.free.fs.model.User;
import com.free.fs.model.UserRole;
import com.free.fs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户业务接口实现
 *
 * @author dinghao
 * @date 2021/3/16
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserRoleMapper userRoleMapper;

    private final RoleMapper roleMapper;

    @Override
    public boolean addUser(User user) {
        user.setPassword(EndecryptUtil.encrytMd5(user.getPassword(), CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT));
        if (baseMapper.insert(user) <= 0) {
            throw new BusinessException("用户新增失败");
        }
        //给用户设置基本角色
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId(roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleCode, CommonConstant.ROLE_USER)).getId());
        if (userRoleMapper.insert(ur) <= 0) {
            throw new BusinessException("用户新增失败");
        }
        return true;
    }
}
