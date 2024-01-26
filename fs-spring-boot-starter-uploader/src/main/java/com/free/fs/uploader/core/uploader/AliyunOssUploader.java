package com.free.fs.uploader.core.uploader;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.free.fs.uploader.config.UploaderProperties.AliyunOssProperties;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.domain.FileBo;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.uploader.core.IFileUploader;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云oss文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:32
 */
@Slf4j
public class AliyunOssUploader implements IFileUploader {

    private final OSS ossClient;
    private final AliyunOssProperties properties;

    public AliyunOssUploader(AliyunOssProperties properties) {
        this.ossClient = new OSSClientBuilder().build(properties.getEndPoint(), properties.getAccessKey(),
                properties.getSecretKey());
        this.properties = properties;
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
        try {
            FileBo fileBo = FileBo.build(file);
            PutObjectResult result = ossClient.putObject(properties.getBucket(), fileBo.getFileName(), file.getInputStream());
            if (result == null) {
                throw new BusinessException("文件上传失败");
            }
            String url = properties.getPath() + CommonConstant.DIR_SPLIT + fileBo.getFileName();
            fileBo.setUrl(url);
            return fileBo;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BusinessException("文件上传失败");
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void delete(String url) {

    }

    @Override
    public void download(String url, HttpServletResponse response) {

    }
}
