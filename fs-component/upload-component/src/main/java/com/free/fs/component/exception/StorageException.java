package com.free.fs.component.exception;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:44
 */
public class StorageException extends RuntimeException {

    public StorageException() {
        super();
    }

    public StorageException(String message) {
        super(message);
    }
}
