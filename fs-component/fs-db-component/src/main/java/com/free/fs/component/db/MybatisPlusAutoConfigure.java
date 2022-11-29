package com.free.fs.component.db;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.free.fs.component.db.properties.MybatisPlusAutoFillProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Mybatis Plus自动配置
 *
 * @author hao.ding@insentek.com
 * @date 2022-05-09 15:07
 */
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
@RequiredArgsConstructor
public class MybatisPlusAutoConfigure {

    private final MybatisPlusAutoFillProperties autoFillProperties;

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusPageInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    /**
     * 事务乐观锁插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusTransactionInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
