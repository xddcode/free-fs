package com.free.fs.mapper;

import com.free.fs.domain.UserRole;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
