package com.free.fs.common.exception;

/**
 * 业务异常
 *
 * @author dinghao
 * @date 2021/3/10
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 6610083281801529147L;

    private Integer code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
