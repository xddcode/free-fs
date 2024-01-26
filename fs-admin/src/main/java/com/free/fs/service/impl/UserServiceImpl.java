package com.free.fs.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.utils.CaptchaUtil;
import com.free.fs.domain.User;
import com.free.fs.domain.UserRole;
import com.free.fs.domain.dto.LoginBody;
import com.free.fs.domain.dto.UserDto;
import com.free.fs.domain.vo.UserVO;
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

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> loginByAccount(LoginBody body) {
        //校验验证码
        boolean verify = CaptchaUtil.verify(body.getUuid(), body.getCode());
        if (!verify) {
            throw new BusinessException("验证码不正确.");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.USERNAME.ge(body.getUsername()));
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!SaSecureUtil.sha256(body.getPassword()).equals(user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        StpUtil.login(user.getId());
        //构建返回对象
        UserVO userVO = this.getOneAs(new QueryWrapper().where(USER.ID.eq(user.getId())), UserVO.class);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("user", userVO);
        map.put("token", tokenInfo);
        return map;
    }

    /**
     * 注册用户
     *
     * @param userDto 用户对象
     * @return 是否新增成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(UserDto userDto) {
        //校验验证码
        CaptchaUtil.verify(userDto.getImgUUID(), userDto.getCode());
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
