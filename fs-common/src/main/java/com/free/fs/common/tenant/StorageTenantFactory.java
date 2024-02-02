package com.free.fs.common.tenant;

import com.free.fs.common.storage.StorageTenantManager;
import com.free.fs.common.utils.RequestHolder;
import com.mybatisflex.core.tenant.TenantFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 存储租户工厂
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 16:34
 */
public class StorageTenantFactory implements TenantFactory {

    @Override
    public Object[] getTenantIds() {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        Long tenantId = StorageTenantManager.getTenantAsLong(request);
        return new Object[]{tenantId};
    }
}
