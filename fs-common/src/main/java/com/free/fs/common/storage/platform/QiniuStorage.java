package com.free.fs.common.storage.platform;

import com.free.fs.common.domain.FileBo;
import com.free.fs.common.storage.IFileStorage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 16:30
 */
public class QiniuStorage implements IFileStorage {
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

    @Override
    public String getUrl(String objectName) {
        return "";
    }
}
