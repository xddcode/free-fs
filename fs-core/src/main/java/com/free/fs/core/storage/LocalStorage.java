package com.free.fs.core.storage;

import com.alibaba.fastjson2.JSONPath;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.domain.FileBo;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.utils.StringUtil;
import com.free.fs.core.IFileStorage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 本地文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/26 13:59
 */
@Slf4j
public class LocalStorage implements IFileStorage {

    private final String directory;
    private final String endPoint;
    private final String nginxUrl;

    public LocalStorage(String config) {
        String directory = (String) JSONPath.eval(config, "$.directory");
        String endPoint = (String) JSONPath.eval(config, "$.endPoint");
        String nginxUrl = (String) JSONPath.eval(config, "$.nginxUrl");
        this.directory = directory;
        this.endPoint = endPoint;
        this.nginxUrl = nginxUrl;
    }

    @Override
    public String getBucketByUrl(String url) {
        return null;
    }

    @Override
    public String getObjectNameByUrl(String url) {
        return null;
    }

    @Override
    public boolean bucketExists(String directory) {
        File file = new File(directory);
        return file.exists();
    }

    @Override
    public void makeBucket(String directory) {
        try {
            if (!bucketExists(directory)) {
                File file = new File(directory);
                file.mkdirs();
            }
        } catch (Exception e) {
            log.error("[Local] makeBucket Exception:{}", e.getMessage());
            throw new BusinessException("创建存储桶失败");
        }
    }

    @Override
    public FileBo upload(MultipartFile file) {
        makeBucket(directory);
        FileBo fileBo = FileBo.build(file);
        try {
            String destName = directory + fileBo.getFileName();
            File dest = new File(destName);
            file.transferTo(dest);
            String url = getUrl(fileBo.getFileName());
            fileBo.setUrl(url);
            return fileBo;
        } catch (IOException e) {
            log.error("[Local] file upload failed: {}", e.getMessage());
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public void delete(String url) {

    }

    @Override
    public void download(String url, HttpServletResponse response) {

    }

    @Override
    public String getUrl(String objectName) {
        // 如果配置了nginxUrl则使用nginxUrl
        if (StringUtil.isNotBlank(nginxUrl)) {
            return nginxUrl + objectName;
        }
        // 否则使用endPoint, 一般是http://本机ip:本机port/mapping/fileName
        return endPoint + CommonConstant.DIR_SPLIT + CommonConstant.LOCAL_DIRECTORY_MAPPING + objectName;
    }
}
