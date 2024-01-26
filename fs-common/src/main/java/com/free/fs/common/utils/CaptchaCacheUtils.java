package com.free.fs.common.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * 验证码缓存工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/26 16:38
 */
public class CaptchaCacheUtils {

    /**
     * 默认5分钟过期
     */
    private final static TimedCache<String, String> cache = CacheUtil.newTimedCache(5 * 60 * 1000);

    private final static String CACHE_KEY = "captcha:";

    public static void put(String key, String value) {
        cache.put(CACHE_KEY + key, value);
    }

    public static String get(String key) {
        return cache.get(CACHE_KEY + key);
    }
}
