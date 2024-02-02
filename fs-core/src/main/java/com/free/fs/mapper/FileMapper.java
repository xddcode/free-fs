package com.free.fs.mapper;

import com.free.fs.domain.FileInfo;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源文件表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {
}
