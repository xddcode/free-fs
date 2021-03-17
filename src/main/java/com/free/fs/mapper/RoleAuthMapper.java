package com.free.fs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.fs.model.RoleAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * juese1权限关联表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface RoleAuthMapper extends BaseMapper<RoleAuth> {

    /**
     * 根据角色code码集合查询所有权限标识
     *
     * @param roleCodes 角色code码集合
     * @return
     */
    List<String> selectAuthCodesByRoleCodes(@Param("roleCodes") List<String> roleCodes);
}
