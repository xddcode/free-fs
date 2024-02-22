package com.free.fs.mapper;

import com.free.fs.domain.FileChunkInfo;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件分片信息mapper
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/5 11:03
 */
@Mapper
public interface FileChunkMapper extends BaseMapper<FileChunkInfo> {
}
