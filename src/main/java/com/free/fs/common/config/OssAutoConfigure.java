package com.free.fs.common.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.free.fs.common.properties.FsServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云配置
 *
 * @author dinghao
 * @date 2020/5/14
 */
@Configuration
@ConditionalOnProperty(name = "fs.files-server.type", havingValue = "oss")
@RequiredArgsConstructor
public class OssAutoConfigure {

    private final FsServerProperties fileProperties;

    /**
     * 阿里云文件存储client
     * 只有配置了aliyun.oss.access-key才可以使用
     */
    @Bean
    public OSSClient ossClient() {
        return new OSSClient(fileProperties.getOss().getEndpoint()
                , new DefaultCredentialProvider(fileProperties.getOss().getAccessKey(), fileProperties.getOss().getSecretKey())
                , null);
    }

}
