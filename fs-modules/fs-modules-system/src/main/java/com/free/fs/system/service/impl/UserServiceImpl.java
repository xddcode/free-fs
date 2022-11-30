package com.free.fs.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.utils.EndecryptUtil;
import com.free.fs.system.dto.InsertUserDto;
import com.free.fs.system.mapper.RoleMapper;
import com.free.fs.system.mapper.UserMapper;
import com.free.fs.system.mapper.UserRoleMapper;
import com.free.fs.system.model.Role;
import com.free.fs.system.model.User;
import com.free.fs.system.model.UserRole;
import com.free.fs.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(InsertUserDto userDto) {
        userDto.setPassword(EndecryptUtil.encrytMd5(userDto.getPassword(), CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT));
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
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
