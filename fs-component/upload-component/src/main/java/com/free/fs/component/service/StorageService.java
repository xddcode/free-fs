package com.free.fs.component.service;

import com.free.fs.component.model.*;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:47
 */
public interface StorageService extends AutoCloseable{

    /**
     * 上传类型
     *
     * @return
     */
    String type();

    /**
     * 上传
     *
     * @param request
     * @return
     */
    PutResponse put(PutFileRequest request);

    /**
     * 删除文件
     *
     * @param request
     * @return
     */
    boolean remove(RemoveFileRequest request);

    /**
     * 下载文件
     *
     * @param request
     * @return
     */
    void download(GetFileRequest request, Consumer<InputStream> consumer);


    /**
     * 判断文件是否存在
     *
     * @param request
     * @return
     */
    boolean isExists(GetFileRequest request);

    /**
     * 释放相关资源
     */
    void close();
}
