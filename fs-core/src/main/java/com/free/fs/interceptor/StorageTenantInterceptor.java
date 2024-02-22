package com.free.fs.interceptor;

import com.free.fs.common.storage.StorageTenantManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 10:04
 */
@Component
public class StorageTenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request
            , HttpServletResponse response, Object handler) throws Exception {
        //设置租户ID到 request 的 attribute
        StorageTenantManager.setTenant(request);
        return true;
    }
}
