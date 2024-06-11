package com.free.fs.core.mapper;

import com.free.fs.core.domain.FileInfo;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源文件表mapper接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 10:56
 */
@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {
}
