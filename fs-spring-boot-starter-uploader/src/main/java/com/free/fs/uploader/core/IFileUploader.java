package com.free.fs.uploader.core;

import com.free.fs.common.domain.FileBo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 9:43
 */
public interface IFileUploader {

    FileBo upload(MultipartFile file);

    void delete(String url);

    void download(String url, HttpServletResponse response);
}
