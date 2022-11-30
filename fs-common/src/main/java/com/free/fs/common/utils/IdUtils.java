package com.free.fs.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * 全局唯一id生成
 *
 * @author hao.ding@insentek.com
 * @date 2022-07-12 13:12
 */
public class IdUtils {

    public static long getId() {
        return IdUtil.getSnowflakeNextId();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(IdUtils.getId());
        }
    }
}
