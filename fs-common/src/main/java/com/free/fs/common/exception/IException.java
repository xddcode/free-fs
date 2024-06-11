package com.free.fs.common.exception;

import java.io.Serial;

/**
 * 自定义异常基类
 *
 * @author dinghao
 * @date 2024/6/7 10:39
 */
public abstract class IException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1582874427218948396L;

    private Integer code;

    public IException() {
    }

    public IException(String message) {
        super(message);
    }

    public IException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}