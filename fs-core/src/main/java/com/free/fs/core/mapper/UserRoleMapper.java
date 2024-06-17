package com.free.fs.core.mapper;

import com.free.fs.core.domain.UserRole;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色表mapper接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:05
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户id查询角色code码集合
     *
     * @param userId 用户id
     * @return
     */
    List<String> selectRoleByUserId(@Param("userId") Long userId);
}
