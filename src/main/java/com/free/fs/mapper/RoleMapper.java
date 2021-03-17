package com.free.fs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.fs.model.Role;
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
