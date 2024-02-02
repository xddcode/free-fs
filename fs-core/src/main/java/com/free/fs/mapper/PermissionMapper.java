package com.free.fs.mapper;

import com.free.fs.domain.Permission;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
