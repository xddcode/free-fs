package com.free.fs.common.exception;

/**
 * 文件异常
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 9:28
 */
public class FileException extends RuntimeException {

    private static final long serialVersionUID = -562821550606290746L;

    public FileException(String message) {
        super(message);
    }
}
