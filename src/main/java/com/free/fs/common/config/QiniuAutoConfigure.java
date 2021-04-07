package com.free.fs.common.config;

import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.template.QiniuTemplate;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 七牛云配置
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Configuration
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "qiniu")
@Import(QiniuTemplate.class)
@RequiredArgsConstructor
public class QiniuAutoConfigure {

    private final FsServerProperties fileProperties;

    /**
     * 指定华南机房,配置自己空间所在的区域
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfig() {
        return new com.qiniu.storage.Configuration(Zone.zone2());
    }

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiniuConfig());
    }

    /**
     * 认证信息实例
     *
     * @return
     */
    @Bean
    public Auth auth() {
        return Auth.create(fileProperties.getQiniu().getAccessKey(), fileProperties.getQiniu().getSecretKey());
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qiniuConfig());
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
