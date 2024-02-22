package com.free.fs.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONPath;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.domain.StorageSettings;
import com.free.fs.service.StorageSettingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 本地文件上传映射目录拦截器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/22 15:22
 */
@Component
public class LocalUploadDirectoryInterceptor implements HandlerInterceptor {

    @Autowired
    private StorageSettingsService settingsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理/upload/开头的请求
        String uri = request.getRequestURI();
        if (uri.startsWith(CommonConstant.LOCAL_DIRECTORY_MAPPING)) {
            String filePath = uri.substring(CommonConstant.LOCAL_DIRECTORY_MAPPING.length()); // 提取请求的文件路径
            Long userId = StpUtil.getLoginIdAsLong();
            StorageSettings storageSettings = settingsService.getByUserPlatformIdentifier(userId, CommonConstant.FILE_TYPE_LOCAL);
            String data = storageSettings.getConfigData();
            String actualDirectory = (String) JSONPath.eval(data, "$.directory");
            request.getRequestDispatcher("/" + actualDirectory + "/" + filePath).forward(request, response);
            return false;
        }
        return true;
    }
}
