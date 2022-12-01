package com.free.fs.component.db;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.free.fs.component.db.interceptor.CustomTenantInterceptor;
import com.free.fs.component.db.properties.TenantProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

/**
 * Mybatis Plus自动配置
 *
 * @author hao.ding@insentek.com
 * @date 2022-05-09 15:07
 */
@RequiredArgsConstructor
public class MybatisPlusAutoConfigure {

    private final TenantLineHandler tenantLineHandler;

    private final TenantProperties tenantProperties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        boolean enableTenant = tenantProperties.getEnable();
        //是否开启多租户隔离
        if (enableTenant) {
            CustomTenantInterceptor tenantInterceptor = new CustomTenantInterceptor(
                    tenantLineHandler, tenantProperties.getIgnoreSqls());
            interceptor.addInnerInterceptor(tenantInterceptor);
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
