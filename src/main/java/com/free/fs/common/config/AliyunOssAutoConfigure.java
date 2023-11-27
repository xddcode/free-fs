package com.free.fs.common.config;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.template.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云配置
 *
 * @author dinghao
 * @date 2020/5/14
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "oss")
@EnableConfigurationProperties({FsServerProperties.class})
@RequiredArgsConstructor
public class AliyunOssAutoConfigure {

    private final FsServerProperties properties;


    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public OSS ossClient() {
        // 检查参数
        if (StrUtil.isEmpty(properties.getOss().getAccessKeyId()) || StrUtil.isEmpty(properties.getOss().getAccessKeySecret())
                || StrUtil.isEmpty(properties.getOss().getBucket()) || StrUtil.isEmpty(properties.getOss().getEndpoint())) {
            log.error("OSS配置缺失，请补充!");
            throw new BusinessException("[Aliyun oss]Aliyun OSS配置缺失，请补充!");
        }

        OSS ossClient = new OSSClientBuilder().build(properties.getOss().getEndpoint(), properties.getOss().getAccessKeyId(),
                properties.getOss().getAccessKeySecret());
        log.info("OSS配置成功，Bucket=" + properties.getOss().getBucket() + "，Endpoint=" + properties.getOss().getEndpoint());
        return ossClient;
    }

    @Bean
    public OssTemplate ossTemplate() {

        return new OssTemplate(ossClient(), properties.getOss());
    }
}
