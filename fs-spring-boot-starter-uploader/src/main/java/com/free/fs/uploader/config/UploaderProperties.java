package com.free.fs.uploader.config;

import com.free.fs.uploader.enums.UploaderType;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:37
 */
@Data
@ConfigurationProperties(prefix = "fs.uploader")
public class UploaderProperties {

    private UploaderType type = UploaderType.LOCAL;

    private LocalProperties local = new LocalProperties();

    private AliyunOssProperties aliyunOss = new AliyunOssProperties();

    private MinioProperties minio = new MinioProperties();

    public static class LocalProperties {
        private String path;
    }

    @Data
    public static class AliyunOssProperties {
        private String accessKey;

        private String secretKey;

        private String endPoint;

        private String bucket;

        private String path;
    }

    @Data
    public static class MinioProperties {
        private String accessKey;

        private String secretKey;

        private String endPoint;

        private String bucket;

        private String path;
    }
}
