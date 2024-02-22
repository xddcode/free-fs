package com.free.fs.common.exception;

import java.io.Serial;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/22 14:05
 */
public class StorageConfigException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageConfigException(String msg) {
        super(msg);
    }
}
