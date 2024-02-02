package com.free.fs.common.exception;

import java.io.Serial;

/**
 * 存储租户异常
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/2 10:30
 */
public class StorageTenantException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageTenantException(String msg) {
        super(msg);
    }
}
