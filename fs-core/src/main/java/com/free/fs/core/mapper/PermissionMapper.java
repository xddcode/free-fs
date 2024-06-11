package com.free.fs.core.mapper;

import com.free.fs.core.domain.Permission;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限表mapper接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:06
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
