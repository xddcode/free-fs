package com.free.fs.core.mapper;

import com.free.fs.core.domain.User;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表mapper接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 10:57
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
