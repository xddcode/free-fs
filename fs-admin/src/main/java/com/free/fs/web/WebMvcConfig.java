package com.free.fs.web;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.interceptor.FsWebInvokeTimeInterceptor;
import com.free.fs.interceptor.LocalUploadDirectoryInterceptor;
import com.free.fs.interceptor.StorageTenantInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 默认SpringMVC拦截器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/22 15:33
 */
@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private PermitResource permitResource;

    @Autowired
    private LocalUploadDirectoryInterceptor localUploadDirectoryInterceptor;

    @Autowired
    private StorageTenantInterceptor storageTenantInterceptor;

    @Autowired
    private FsWebInvokeTimeInterceptor webInvokeTimeInterceptor;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 文件上传路径:把上传文件地址file:/data/free-fs/xxx.jpg  映射为 /uploads/**
//        registry.addInterceptor(uploadDirectoryInterceptor)
//                .addResourceHandler(CommonConstant.LOCAL_DIRECTORY_MAPPING + "**")
//                .addResourceLocations("file:" + "D:/free-fs/");
//    }

    /**
     * 注册sa拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 本地文件上传映射目录拦截器
        registry.addInterceptor(localUploadDirectoryInterceptor)
                .addPathPatterns(CommonConstant.LOCAL_DIRECTORY_MAPPING + "**");

        List<String> excludePaths = permitResource.getPermitList();
        String prefix = permitResource.getPrefix();
        // 全局访问性能拦截
        registry.addInterceptor(webInvokeTimeInterceptor);
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns(prefix)
                .excludePathPatterns(excludePaths.toArray(new String[0]));
        // 注册存储租户拦截
        registry.addInterceptor(storageTenantInterceptor);
    }
}
