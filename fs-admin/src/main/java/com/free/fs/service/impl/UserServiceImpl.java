package com.free.fs.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.domain.User;
import com.free.fs.domain.UserRole;
import com.free.fs.domain.dto.LoginBody;
import com.free.fs.domain.dto.UserDto;
import com.free.fs.mapper.UserMapper;
import com.free.fs.mapper.UserRoleMapper;
import com.free.fs.service.RoleService;
import com.free.fs.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.free.fs.domain.table.UserTableDef.USER;

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

    private final RoleService roleService;

    /**
     * 登录
     *
     * @param body 登录信息
     * @return 是否登录成功
     */
    @Override
    public boolean doLogin(LoginBody body) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.USERNAME.ge(body.getUsername()));
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!SaSecureUtil.sha256(body.getPassword()).equals(user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        StpUtil.login(user.getId(), body.getRememberMe());
        return true;
    }

    /**
     * 新增用户
     *
     * @param userDto 用户对象
     * @return 是否新增成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(UserDto userDto) {
        long count = this.count(new QueryWrapper().where(USER.USERNAME.eq(userDto.getUsername())));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        userDto.setPassword(SaSecureUtil.sha256(userDto.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        if (!this.save(user)) {
            throw new BusinessException("用户新增失败");
        }
        //给用户设置基本角色
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId(roleService.getIdByCode(CommonConstant.ROLE_USER));
        if (userRoleMapper.insert(ur) <= 0) {
            throw new BusinessException("用户新增失败");
        }
        return true;
    }
}
