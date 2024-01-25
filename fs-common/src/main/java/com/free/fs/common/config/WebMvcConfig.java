package com.free.fs.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 默认SpringMVC拦截器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/22 15:33
 */
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

//    private final FsServerProperties fsServerProperties;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 文件上传路径:把上传文件地址file:/data/free-fs/xxx.jpg  映射为 /uploads/**
//        registry.addResourceHandler(fsServerProperties.getLocal().getUploadMapping() + "/**")
//                .addResourceLocations("file:" + fsServerProperties.getLocal().getUploadDir());
//    }

    /**
     * 注册sa拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin())).addPathPatterns("/file/**")
                .excludePathPatterns("/login", "/reg", "/informationPass", "/uploads/**,/assets/**", "/oauth/**");
    }
}
