package com.free.fs.common.config;

import com.free.fs.common.properties.FsServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

}
