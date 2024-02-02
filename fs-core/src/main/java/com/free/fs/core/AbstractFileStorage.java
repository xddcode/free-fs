package com.free.fs.core;

import com.free.fs.common.domain.FileBo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 抽象文件存储类:
 * <p>所有oss存储公共方法实现</p>
 *
 * @author yann
 */
public abstract class AbstractFileStorage implements IFileStorage {




    @Override
    public String getBucketByUrl(String url) {
        return null;
    }

    @Override
    public String getObjectNameByUrl(String url) {
        return null;
    }

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
    public String getPolicyUrl(String url) {
        return null;
    }
}
