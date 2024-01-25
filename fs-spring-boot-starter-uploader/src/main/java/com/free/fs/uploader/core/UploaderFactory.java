package com.free.fs.uploader.core;

import com.aliyun.oss.OSS;
import com.free.fs.uploader.config.UploaderProperties;
import com.free.fs.uploader.enums.UploaderType;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传对象工厂
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 13:50
 */
@Slf4j
public class UploaderFactory implements IFileUploaderProvider {

    private final UploaderProperties properties;

    public UploaderFactory(UploaderProperties properties) {
        this.properties = properties;
    }

    @Override
    public IFileUploader getUploader() {
        UploaderType type = properties.getType();
        if (type.equals(UploaderType.MINIO)) {
            return new MinioUploader(properties.getMinio());
        } else if (type.equals(UploaderType.aliyunOss)) {
            return new AliyunOssFileUploader(properties.getAliyunOss());
        }
        return null;
    }

}
