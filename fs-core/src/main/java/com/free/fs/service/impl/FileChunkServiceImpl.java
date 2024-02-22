package com.free.fs.service.impl;

import com.free.fs.domain.FileChunkInfo;
import com.free.fs.domain.dto.FileChuckDTO;
import com.free.fs.mapper.FileChunkMapper;
import com.free.fs.service.FileChunkService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import static com.free.fs.domain.table.FileChunkInfoTableDef.FILE_CHUNK_INFO;

/**
 * 文件分块信息业务接口实现
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/5 11:04
 */
@Service
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunkInfo> implements FileChunkService {
    @Override
    public boolean isChunkUploaded(String md5, Integer chunkNumber) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(FILE_CHUNK_INFO.MD5.eq(md5))
                .and(FILE_CHUNK_INFO.CHUNK_NUMBER.eq(chunkNumber));
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean isAllChunkUploaded(String md5, Integer totalChunks) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(FILE_CHUNK_INFO.MD5.eq(md5));
        return this.count(queryWrapper) == totalChunks;
    }
}
