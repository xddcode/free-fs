package com.free.fs.common.orm;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.orm.decipher.Decipher;
import com.free.fs.common.orm.tenant.MyTenantFactory;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.MessageCollector;
import com.mybatisflex.core.datasource.DataSourceDecipher;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.core.query.QueryColumnBehavior;
import com.mybatisflex.core.tenant.TenantFactory;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Mybatis-Flex自动配置
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/24 15:52
 */
@AutoConfiguration
@MapperScan("${mybatis-flex.mapper-package}")
public class MybatisFlexAutoConfigure implements ConfigurationCustomizer, MyBatisFlexCustomizer {

    @Value("${mybatis-flex.audit_enable}")
    private Boolean enableAudit = false;

    @Value("${mybatis-flex.sql_print}")
    private Boolean sqlPrint = false;

    static {
        QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_BLANK);
        QueryColumnBehavior.setSmartConvertInToEquals(true);
    }

    /**
     * 数据源解密
     */
    @Bean
    public DataSourceDecipher decipher() {
        DataSourceDecipher decipher = new Decipher();
        return decipher;
    }

    @Override
    public void customize(FlexConfiguration configuration) {
        //mybatis实现的打印详细sql及返回结果到控制台，便于调试
        if (sqlPrint) {
            configuration.setLogImpl(StdOutImpl.class);
        }
    }

    /**
     * Mybatis-Flex自定义初始化配置
     *
     * @param globalConfig 全局配置
     */
    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        // 设置租户列
        FlexGlobalConfig.getDefaultConfig().setTenantColumn(CommonConstant.SYSTEM_TENANT_ID);
        // 开启审计功能
        AuditManager.setAuditEnable(enableAudit);
        if (sqlPrint) {
            // 开启sql打印默认会开启sql审计
            AuditManager.setAuditEnable(true);
            //设置 SQL 审计收集器
            MessageCollector collector = new ConsoleMessageCollector();
            AuditManager.setMessageCollector(collector);
        }
    }

    @Bean
    public TenantFactory tenantFactory() {
        return new MyTenantFactory();
    }
}
