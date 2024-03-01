package com.free.fs.core.storage;

import com.alibaba.fastjson2.JSONPath;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.domain.FileBo;
import com.free.fs.common.exception.StorageConfigException;
import com.free.fs.common.utils.ResponseUtil;
import com.free.fs.core.IFileStorage;
import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * Minio文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:05
 */
@Slf4j
public class MinioStorage implements IFileStorage {

    private final MinioClient minioClient;
    private final String endPoint;
    private final String bucket;

    public MinioStorage(String config) {
        try {
            String accessKey = (String) JSONPath.eval(config, "$.accessKey");
            String secretKey = (String) JSONPath.eval(config, "$.secretKey");
            String endPoint = (String) JSONPath.eval(config, "$.endPoint");
            String bucket = (String) JSONPath.eval(config, "$.bucket");
            this.minioClient = MinioClient.builder()
                    .credentials(accessKey, secretKey)
                    .endpoint(endPoint)
                    .build();
            this.endPoint = endPoint;
            this.bucket = bucket;
        } catch (Exception e) {
            log.error("[Minio] MinioClient build failed: {}", e.getMessage());
            throw new StorageConfigException("请检查Minio配置是否正确");
        }
    }

    @Override
    public String getBucketByUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        String key = url.replace(endPoint + CommonConstant.DIR_SPLIT, "");
        int index = key.indexOf(CommonConstant.DIR_SPLIT);
        return key.substring(0, index);
    }


    @Override
    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            log.error("[Minio] bucketExists Exception:{}", e.getMessage());
        }
        return false;
    }

    @Override
    public void makeBucket(String bucket) {
        try {
            if (!bucketExists(bucket)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("[Minio] makeBucket success bucketName:{}", bucket);
            }
        } catch (Exception e) {
            log.error("[Minio] makeBucket Exception:{}", e.getMessage());
            throw new BusinessException("创建存储桶失败");
        }
    }

    @Override
    public FileBo upload(MultipartFile file) {
        makeBucket(bucket);
        FileBo fileBo = FileBo.build(file);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileBo.getFileName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            String url = getUrl(fileBo.getFileName());
            fileBo.setUrl(url);
            return fileBo;
        } catch (Exception e) {
            log.error("[Minio] file upload failed: {}", e.getMessage());
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public void delete(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("文件删除失败,文件路径为空");
        }
        String bucket = this.getBucketByUrl(url);
        String object = this.getObjectNameByUrl(url);
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("[Minio] file delete failed: {}", e.getMessage());
            throw new BusinessException("文件删除失败");
        }
    }

    @Override
    public void download(String url, HttpServletResponse response) {
        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("文件下载失败,文件路径为空");
        }
        String bucket = this.getBucketByUrl(url);
        String object = this.getObjectNameByUrl(url);
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build();
            GetObjectResponse is = minioClient.getObject(getObjectArgs);
            ResponseUtil.write(is, object, response);
            log.info("[Minio] file download success, path:{}", url);
        } catch (Exception e) {
            log.error("[Minio] file download failed: {}", e.getMessage());
            throw new BusinessException("文件下载失败");
        }
    }

    @Override
    public String getUrl(String objectName) {
        return endPoint + CommonConstant.DIR_SPLIT + bucket + CommonConstant.DIR_SPLIT + objectName;
    }

    @Override
    public String getPolicyUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        String bucket = this.getBucketByUrl(url);
        String object = this.getObjectNameByUrl(url);
        String policyUrl = "";
        try {
            policyUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(object)
                    .expiry(10000, TimeUnit.MINUTES).build());
        } catch (Exception e) {
            log.error("[Minio] failed to obtain policy URL: {}", e.getMessage());
        }
        return policyUrl;
    }
}
