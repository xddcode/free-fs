package com.free.fs.common.config;

import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.template.LocalTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 本地上传配置
 *
 * @author dinghao
 * @date 2020/5/14
 */
@Configuration
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "local")
@Import(LocalTemplate.class)
@RequiredArgsConstructor
public class LocalAutoConfigure {

    private final FsServerProperties fileProperties;
}
