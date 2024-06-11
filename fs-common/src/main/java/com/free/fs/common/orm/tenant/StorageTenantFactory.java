package com.free.fs.common.orm.tenant;

import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.storage.StorageType;
import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 存储租户工厂
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 16:34
 */
@Component
public class StorageTenantFactory implements TenantFactory {

    @Autowired
    private FsServerProperties fsServerProperties;

    @Override
    public Object[] getTenantIds() {
        StorageType tenant = fsServerProperties.getType();
        return new Object[]{tenant.name()};
    }
}
