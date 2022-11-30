package com.free.fs.common.utils;

import com.free.fs.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 自定义断言校验
 *
 * @author hao.ding@insentek.com
 * @date 2022-08-05 09:29
 */
public class Assert {

    public static void notEmpty(@Nullable String param) {
        notEmpty(param, "[Assertion failed] - param is empty or null");
    }

    public static void notEmpty(@Nullable String param, String message) {
        if (StringUtils.isEmpty(param)) {
            throw new BusinessException(message);
        }
    }

    public static void isNull(@Nullable Object o) {
        isNull(o, "[Assertion failed] - object is not null");
    }

    public static void isNull(@Nullable Object o, String message) {
        if (o != null) {
            throw new BusinessException(message);
        }
    }

    public static void notNull(@Nullable Object o) {
        notNull(o, "[Assertion failed] - object is null");
    }

    public static void notNull(@Nullable Object o, String message) {
        if (o == null) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - collection is empty or null");
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(message);
        }
    }

    public static void notEquals(@Nullable CharSequence str1, @Nullable CharSequence str2, String message) {
        if (!StringUtils.equals(str1, str2)) {
            throw new BusinessException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    @Deprecated
    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }
}
