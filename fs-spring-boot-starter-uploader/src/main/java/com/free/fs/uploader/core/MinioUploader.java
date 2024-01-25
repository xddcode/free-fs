package com.free.fs.uploader.core;

import cn.hutool.core.io.IoUtil;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.uploader.config.UploaderProperties.MinioProperties;
import com.free.fs.common.domain.FileBo;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Minio文件上传
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:05
 */
@Slf4j
public class MinioUploader implements IFileUploader {

    private final MinioClient minioClient;

    private final MinioProperties properties;

    public MinioUploader(MinioProperties properties) {
        this.minioClient = MinioClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndPoint())
                .build();
        this.properties = properties;
    }

    @Override
    public FileBo upload(MultipartFile file) {
        FileBo fileBo = FileBo.build(file);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(fileBo.getFileName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            String url = properties.getPath() + CommonConstant.DIR_SPLIT + fileBo.getFileName();
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
            return;
        }
        String key = url.replace(properties.getEndPoint() + CommonConstant.DIR_SPLIT, "");
        int index = key.indexOf(CommonConstant.DIR_SPLIT);
        String bucket = key.substring(0, index);
        String object = key.substring(index + 1);
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
            return;
        }
        String key = url.replace(properties.getEndPoint() + CommonConstant.DIR_SPLIT, "");
        int index = key.indexOf(CommonConstant.DIR_SPLIT);
        String bucket = key.substring(0, index);
        String object = key.substring(index + 1);
        InputStream in = null;
        OutputStream out = null;
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build();
            in = minioClient.getObject(getObjectArgs);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + object);
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("Minio文件下载失败: {}", e.getMessage());
            throw new BusinessException("文件下载失败");
        } finally {
            IoUtil.close(in);
            IoUtil.close(out);
        }
    }
}
