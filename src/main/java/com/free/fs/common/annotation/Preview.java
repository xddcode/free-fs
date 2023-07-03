package com.free.fs.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.*;

/**
 * 演示环境限制
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/7/3 17:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Preview {

    /**
     * 限制上传次数
     */
    int count() default 5;
}
