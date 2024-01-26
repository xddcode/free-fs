package com.free.fs.uploader.core.uploader;

import com.free.fs.common.domain.FileBo;
import com.free.fs.uploader.core.IFileUploader;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/26 13:59
 */
public class LocalUploader implements IFileUploader {
    @Override
    public boolean bucketExists(String bucket) {
        return false;
    }

    @Override
    public void makeBucket(String bucket) {

    }

    @Override
    public FileBo upload(MultipartFile file) {
        return null;
    }

    @Override
    public void delete(String url) {

    }

    @Override
    public void download(String url, HttpServletResponse response) {

    }
}
