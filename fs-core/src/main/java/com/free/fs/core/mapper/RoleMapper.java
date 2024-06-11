package com.free.fs.core.mapper;

import com.free.fs.core.domain.Role;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表mapper接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:05
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
