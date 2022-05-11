package com.free.fs.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.properties.TenantProperties;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus 配置
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022-05-11 14:45:46
 */
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
@MapperScan("com.free.fs.mapper")
@RequiredArgsConstructor
public class MybatisPlusConfigure {

    private final TenantProperties tenantProperties;

    private final FsServerProperties fsServerProperties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        boolean enableTenant = tenantProperties.getEnable();
        //是否开启多租户隔离
        if (enableTenant) {
            mpInterceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
                /**
                 * 获取租户id
                 */
                @Override
                public Expression getTenantId() {
                    String tenant = fsServerProperties.getType();
                    if (tenant != null) {
                        return new StringValue(tenant);
                    }
                    return new NullValue();
                }

                /**
                 * 获取租户列名
                 */
                @Override
                public String getTenantIdColumn() {
                    return CommonConstant.SYSTEM_TENANT_ID;
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
            }));
        }
        //分页插件
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return mpInterceptor;
    }
}
