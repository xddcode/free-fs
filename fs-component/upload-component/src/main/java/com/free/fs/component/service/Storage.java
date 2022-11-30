package com.free.fs.component.service;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:47
 */
public class Storage {

    /**
     * 上传
     * @param request
     * @return
     */
    PutResponse put(PutFileRequest request);

    /**
     * 获取文件
     * @param request
     * @return
     */
    FileStream get(GetFileRequest request);

    /**
     * 获取url
     * @param request
     * @return
     */
    String getUrl(GetFileRequest request);

    /**
     * 获取文件信息
     * @param request
     * @return
     */
    FileMeta getMeta(GetFileRequest request);

    /**
     * 判断文件是否存在
     * @param request
     * @return
     */
    boolean isExists(GetFileRequest request);

    /**
     * 删除
     * @param request
     * @return
     */
    boolean remove(RemoveFileRequest request);
}
