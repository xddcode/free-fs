package com.free.fs.component.db;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.free.fs.common.context.TenantContextHolder;
import com.free.fs.component.db.properties.TenantProperties;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 多租户配置
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/12/1 9:30
 */
@EnableConfigurationProperties(TenantProperties.class)
@RequiredArgsConstructor
public class TenantAutoConfigure {

    private final TenantProperties tenantProperties;

    @Bean
    public TenantLineHandler tenantLineHandler() {
        return new TenantLineHandler() {
            /**
             * 获取租户id
             */
            @Override
            public Expression getTenantId() {
                String tenant = TenantContextHolder.getTenant();
                if (tenant != null) {
                    return new StringValue(TenantContextHolder.getTenant());
                }
                return new NullValue();
            }

            /**
             * 过滤不需要根据租户隔离的表
             * @param tableName 表名
             */
            @Override
            public boolean ignoreTable(String tableName) {
                return tenantProperties.getIgnoreTables().stream().anyMatch(
                        (e) -> e.equalsIgnoreCase(tableName)
                );
            }
        };
    }
}
