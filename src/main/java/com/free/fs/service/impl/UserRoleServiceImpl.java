package com.free.fs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.free.fs.mapper.UserRoleMapper;
import com.free.fs.model.UserRole;
import com.free.fs.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/30 10:20
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<String> getRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }
}
