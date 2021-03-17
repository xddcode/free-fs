package com.free.fs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.fs.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户id查询角色code码集合
     *
     * @param userId 用户id
     * @return
     */
    List<String> selectRoleCodesById(@Param("userId") Long userId);
}
