package com.free.fs.mapper;

import com.free.fs.domain.Role;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
