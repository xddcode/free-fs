package com.free.fs.common.storage.platform;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.domain.FileBo;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.exception.StorageConfigException;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.storage.IFileStorage;
import com.free.fs.common.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云oss文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:32
 */
@Slf4j
public class AliyunOssStorage implements IFileStorage {

    private final OSS client;
    private final String endPoint;
    private final String bucket;

    public AliyunOssStorage(FsServerProperties.AliyunOssProperties config) {
        try {
            String accessKey = config.getAccessKey();
            String secretKey = config.getSecretKey();
            String endPoint = config.getEndpoint();
            String bucket = config.getBucket();
            this.client = new OSSClientBuilder().build(endPoint, accessKey, secretKey);
            this.endPoint = endPoint;
            this.bucket = bucket;
        } catch (Exception e) {
            log.error("[AliyunOSS] OSSClient build failed: {}", e.getMessage());
            throw new StorageConfigException("请检查阿里云OSS配置是否正确");
        }
    }

    @Override
    public boolean bucketExists(String bucket) {
        try {
            boolean exists = client.doesBucketExist(bucket);
            System.out.println(exists);
            return exists;
        } catch (Exception e) {
            log.error("[AliyunOSS] bucketExists Exception:{}", e.getMessage());
        } finally {
            client.shutdown();
        }
        return false;
    }

    @Override
    public void makeBucket(String bucket) {
        try {
            if (!bucketExists(bucket)) {
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
                // 设置存储空间读写权限为公共读，默认为私有。
                //createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
                log.info("[AliyunOSS] makeBucket success bucketName:{}", bucket);
            }
        } catch (Exception e) {
            log.error("[AliyunOSS] makeBucket Exception:{}", e.getMessage());
            throw new BusinessException("创建存储桶失败");
        } finally {
            client.shutdown();
        }
    }

    @Override
    public FileBo upload(MultipartFile file) {
        //需要开启对应ACL权限
        //makeBucket(bucket);
        try {
            FileBo fileBo = FileBo.build(file);
            PutObjectResult result = client.putObject(bucket, fileBo.getFileName(), file.getInputStream());
            if (result == null) {
                throw new BusinessException("文件上传失败");
            }
            String url = getUrl(fileBo.getFileName());
            fileBo.setUrl(url);
            return fileBo;
        } catch (Exception e) {
            log.error("[AliyunOSS] file upload failed: {}", e.getMessage());
            throw new BusinessException("文件上传失败");
        } finally {
            client.shutdown();
        }
    }

    @Override
    public void delete(String objectName) {
        if (StringUtils.isEmpty(objectName)) {
            throw new BusinessException("文件删除失败");
        }
        try {
            client.deleteObject(bucket, objectName);
        } catch (Exception e) {
            log.error("[AliyunOSS] file delete failed: {}", e.getMessage());
            throw new BusinessException("文件删除失败");
        } finally {
            client.shutdown();
        }
    }

    @SneakyThrows
    @Override
    public void download(String objectName, HttpServletResponse response) {
        if (StringUtils.isEmpty(objectName)) {
            throw new BusinessException("文件下载失败, 文件对象为空");
        }
        try (OSSObject ossObject = client.getObject(bucket, objectName)) {
            ResponseUtil.write(ossObject.getObjectContent(), objectName, response);
            log.info("[Minio] file download success, object:{}", objectName);
        } catch (Exception e) {
            log.error("[Minio] file download failed: {}", e.getMessage());
            throw new BusinessException("文件下载失败");
        } finally {
            client.shutdown();
        }
    }

    @Override
    public String getUrl(String objectName) {
        return "https://" + bucket + CommonConstant.SUFFIX_SPLIT + endPoint + CommonConstant.DIR_SPLIT + objectName;
    }
}

