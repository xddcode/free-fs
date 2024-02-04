package com.free.fs.common.storage;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.StorageTenantException;
import com.free.fs.common.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 存储租户管理
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 9:48
 */
public class StorageTenantManager {

    public static void setTenant(HttpServletRequest request) {
        String tenant = request.getHeader(CommonConstant.STORAGE_TENANT_REQUEST);
        request.setAttribute(CommonConstant.STORAGE_TENANT_REQUEST, tenant);
    }

    public static String getTenant(HttpServletRequest request) {
        Object tenant = request.getAttribute(CommonConstant.STORAGE_TENANT_REQUEST);
        if (ObjectUtils.isEmpty(tenant)) {
            throw new StorageTenantException("Missing tenant identification");
        }
        return (String) tenant;
    }
}
