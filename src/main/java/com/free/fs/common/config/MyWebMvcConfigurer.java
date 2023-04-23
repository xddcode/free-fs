package com.free.fs.common.config;

import com.free.fs.common.properties.FsServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    private final FsServerProperties fsServerProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件上传路径:把上传文件地址file:E:/upload/  映射为 /uploads/**
        registry.addResourceHandler(fsServerProperties.getLocal().getUploadMapping() + "/**")  // /images/**
                .addResourceLocations("file:" + fsServerProperties.getLocal().getUploadDir());  // file:E:/upload/
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}
