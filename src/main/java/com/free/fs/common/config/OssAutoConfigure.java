package com.free.fs.common.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.template.OssTemplate;
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
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "oss")
@RequiredArgsConstructor
public class OssAutoConfigure {

    private final FsServerProperties fileProperties;


    @Bean
    public OSS ossClient() {

        return new OSSClientBuilder().build(fileProperties.getOss().getEndpoint(), fileProperties.getOss().getAccessKey(), fileProperties.getOss().getSecretKey());
    }

    @Bean
    public OssTemplate ossTemplate() {

        return new OssTemplate(ossClient(), fileProperties.getOss());
    }
}
