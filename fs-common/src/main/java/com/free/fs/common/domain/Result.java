package com.free.fs.common.domain;

import cn.hutool.http.HttpStatus;
import com.free.fs.common.exception.ErrorCode;
import lombok.Data;

/**
 * 响应数据
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/8/29 14:44
 */
@Data
public class Result<T> {
    private int code;

    private String msg = "success";

    private T data;

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.HTTP_OK);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        return error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> error(String msg) {
        return error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
