package com.free.fs.component.properties;

import com.free.fs.component.enums.StorageTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:41
 */
@Data
@ConfigurationProperties(prefix = "fs.file-storage")
public class FileStorageProperties {

    /**
     * 存储类型：aliyun, qiniu, local
     */
    private String type = StorageTypeEnum.LOCAL.name();

    /**
     * 本地存储
     */
    private LocalConfig local;

    /**
     * 阿里云 OSS
     */
    private AliyunOssConfig aliyunOss;

    /**
     * 七牛云 Kodo
     */
    private QiniuConfig qiniu;

    /**
     * 本地存储
     */
    @Data
    public static class LocalConfig {
        /**
         * 存储路径f
         */
        private String dir = "";
        /**
         * 展示域名
         */
        private String domain;
        /**
         * 上传路径前缀
         */
        private String prefix;
    }

    @Data
    public static class AliyunOssConfig {
        /**
         * 密钥key
         */
        private String accessKey;
        /**
         * accessSecret
         */
        private String accessSecret;
        /**
         * EndPoint
         */
        private String endPoint;
        /**
         * 桶
         */
        private String bucket;
        /**
         * 绑定的域名
         */
        private String domain;

        /**
         * 上传路径前缀
         */
        private String prefix;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QiniuConfig extends AliyunOssConfig {
        /**
         * 所属地区
         */
        private String region;

    }

}
