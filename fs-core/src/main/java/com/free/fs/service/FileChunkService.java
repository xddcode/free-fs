package com.free.fs.service;

import com.free.fs.domain.FileChunkInfo;
import com.free.fs.domain.dto.FileChuckDTO;
import com.mybatisflex.core.service.IService;

/**
 * 文件分快信息业务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/5 11:03
 */
public interface FileChunkService extends IService<FileChunkInfo> {


    /**
     * 判断文件当前分块是否已经完成上传
     *
     * @param md5         文件分块md5
     * @param chunkNumber 文件分块编号
     * @return true 已上传 false 未上传
     */
    boolean isChunkUploaded(String md5, Integer chunkNumber);

    /**
     * 判断文件所有分块是否全部完成上传
     *
     * @param md5         文件分块md5
     * @param totalChunks 文件总分块数
     * @return
     */
    boolean isAllChunkUploaded(String md5, Integer totalChunks);
}
