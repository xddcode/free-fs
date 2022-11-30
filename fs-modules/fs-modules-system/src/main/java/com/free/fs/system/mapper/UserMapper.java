package com.free.fs.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.fs.system.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
