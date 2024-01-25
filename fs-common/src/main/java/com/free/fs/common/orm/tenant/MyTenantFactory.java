package com.free.fs.common.orm.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 租户工厂
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 16:34
 */
public class MyTenantFactory implements TenantFactory {

    @Value("${fs.uploader.type}")
    private String uploaderType;

    @Override
    public Object[] getTenantIds() {
        return new Object[]{uploaderType};
    }
}
