package com.free.fs.core.mapper;

import com.free.fs.core.domain.RolePermission;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限表mapper接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:06
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 根据角色code码集合查询所有权限标识
     *
     * @param roleCodes 角色code码集合
     * @return
     */
    List<String> selectPermissionByRoles(@Param("roleCodes") List<String> roleCodes);
}
