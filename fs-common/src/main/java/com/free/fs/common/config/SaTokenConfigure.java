package com.free.fs.common.config;

import cn.dev33.satoken.thymeleaf.dialect.SaTokenDialect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/29 17:39
 */
@AutoConfiguration
public class SaTokenConfigure {
    // Sa-Token 标签方言 (Thymeleaf版)
    @Bean
    public SaTokenDialect getSaTokenDialect() {
        return new SaTokenDialect();
    }
}
