package com.free.fs.core.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.core.domain.User;
import com.free.fs.core.domain.UserRole;
import com.free.fs.core.domain.dto.LoginBody;
import com.free.fs.core.domain.dto.UpdatePasswordDTO;
import com.free.fs.core.domain.dto.UserDTO;
import com.free.fs.core.mapper.UserMapper;
import com.free.fs.core.service.RoleService;
import com.free.fs.core.service.UserRoleService;
import com.free.fs.core.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.free.fs.core.domain.table.UserTableDef.USER;


/**
 * 用户表 服务实现类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:14
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    @Override
    public boolean doLogin(LoginBody body) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.USERNAME.eq(body.getUsername()));
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!SaSecureUtil.sha256(body.getPassword()).equals(user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        StpUtil.login(user.getId(), body.isRememberMe());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(UserDTO dto) {
        long count = this.count(new QueryWrapper().where(USER.USERNAME.eq(dto.getUsername())));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        dto.setPassword(SaSecureUtil.sha256(dto.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        if (!this.save(user)) {
            throw new BusinessException("用户新增失败");
        }
        //给用户设置基本角色
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId(roleService.getIdByCode(CommonConstant.ROLE_USER));
        if (!userRoleService.save(ur)) {
            throw new BusinessException("用户新增失败");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePassword(UpdatePasswordDTO dto) {
        if (dto.getId() == 1) {
            throw new BusinessException("ADMIN用户密码不允许修改");
        }
        User user = this.getById(dto.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!SaSecureUtil.sha256(dto.getPassword()).equals(user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }
        user.setPassword(SaSecureUtil.sha256(dto.getNewPassword()));
        return this.updateById(user);
    }
}