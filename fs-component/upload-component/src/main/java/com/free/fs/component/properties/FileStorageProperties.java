package com.free.fs.component.properties;

import com.free.fs.component.enums.StorageTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:41
 */
@Data
@ConfigurationProperties(prefix = "filestorage")
public class FileStorageProperties {

    /**
     * 存储类型：aliyun, qiniu, qcloud, minio, local, custom
     */
    private String primary = StorageTypeEnum.LOCAL.name();

    private Config<Bucket> aliyun;

    private Config<RegionBucket> qiniu;

    private LocalConfig local;

    private Map<String, String> ext = new HashMap<>();

    @Data
    public static class Config<T extends Bucket> {
        /**
         * 密钥key
         */
        private String accessKey;
        /**
         * accessSecret
         */
        private String accessSecret;
        /**
         * 默认的桶
         */
        private String primaryBucket;
        /**
         * 桶列表
         */
        private List<T> buckets;

    }

    /**
     * 本地
     */
    @Data
    public static class LocalConfig {
        /**
         * 保存路径
         */
        private String dir = "upload";
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
    public static class Bucket {
        /**
         * 桶
         */
        private String bucket;
        /**
         * 类型
         * private:隐私桶，即所有人都可访问
         * public:公共桶，需要密钥才允许访问
         */
        private String type = "private";
        /**
         * 绑定的域名
         */
        private String domain;
        /**
         * EndPoint
         */
        private String endPoint;
        /**
         * 上传路径前缀
         */
        private String prefix;
        /**
         * 过期时间，单位/秒
         */
        private Long expires = Consts.EXPIRES_TIME;

        public Long getExpiresTime() {
            return null == getExpires() || getExpires() <= 0 ? Consts.EXPIRES_TIME : getExpires();
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RegionBucket extends Bucket {
        /**
         * 所属地区
         */
        private String region;

    }

}
