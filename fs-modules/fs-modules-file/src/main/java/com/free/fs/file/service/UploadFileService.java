package com.free.fs.file.service;

import com.free.fs.common.model.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 文件上传接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 10:50
 */
public interface UploadFileService {


    /**
     * 上传文件
     *
     * @param files
     * @param dirIds
     * @return
     */
    R upload(MultipartFile[] files, String dirIds);

    /**
     * 分片上传大文件
     *
     * @param files
     * @return
     */
    R uploadSharding(MultipartFile[] files, String dirIds, HttpSession session);

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    void download(String url, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param url
     * @return
     */
    boolean delete(String url);
}
