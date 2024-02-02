package com.free.fs.core.storage;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSONObject;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.domain.FileBo;
import com.free.fs.core.AbstractFileStorage;
import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.params.ClientPNames;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Minio文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:05
 */
@Slf4j
public class MinioStorage extends AbstractFileStorage {

    private final MinioClient minioClient;
    private final String endPoint;
    private final String bucket;

    public MinioStorage(String config) {
        JSONObject jsonObject = JSONObject.parseObject(config);
        String accessKey = jsonObject.getString("accessKey");
        String secretKey = jsonObject.getString("secretKey");
        String endPoint = jsonObject.getString("endPoint");
        String bucket = jsonObject.getString("bucket");

        this.minioClient = MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(endPoint)
                .build();
        this.endPoint = endPoint;
        this.bucket = bucket;
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
    public String getObjectNameByUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        String key = url.replace(endPoint + CommonConstant.DIR_SPLIT, "");
        int index = key.indexOf(CommonConstant.DIR_SPLIT);
        return key.substring(index + 1);
    }

    @Override
    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            log.error("Minio bucketExists Exception:{}", e.getMessage());
        }
        return false;
    }

    @Override
    public void makeBucket(String bucket) {
        try {
            if (!bucketExists(bucket)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("Minio makeBucket success bucketName:{}", bucket);
            }
        } catch (Exception e) {
            log.error("Minio makeBucket Exception:{}", e.getMessage());
        }
    }

    @Override
    public FileBo upload(MultipartFile file) {
        //String bucket = properties.getBucket();
        if (!bucketExists(bucket)) {
            log.info("Minio bucketName is not creat");
            makeBucket(bucket);
        }
        FileBo fileBo = FileBo.build(file);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileBo.getFileName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            String url = endPoint + CommonConstant.DIR_SPLIT + bucket + CommonConstant.DIR_SPLIT + fileBo.getFileName();
            fileBo.setUrl(url);
            return fileBo;
        } catch (Exception e) {
            log.error("文件上传失败", e);
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
            log.error("Minio文件删除失败: {}", e.getMessage());
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
        GetObjectResponse is = null;
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build();
            is = minioClient.getObject(getObjectArgs);
            // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding(CommonConstant.UTF_8);
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(object, StandardCharsets.UTF_8));
            IoUtil.copy(is, response.getOutputStream());
            log.info("Minio downloadFile success, filePath:{}", url);
        } catch (Exception e) {
            log.error("Minio文件下载失败: {}", e.getMessage());
            throw new BusinessException("文件下载失败");
        } finally {
            IoUtil.close(is);
        }
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
            log.error("Minio获取policyURL失败 : {}", e.getMessage());
        }
        return policyUrl;
    }
}
