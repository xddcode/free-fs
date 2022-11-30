package com.free.fs.common.exception.handler;

import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.exception.FileException;
import com.free.fs.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 9:27
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException 异常处理返回json
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public R<?> badRequestException(IllegalArgumentException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * HttpRequestMethodNotSupportedException 异常处理返回json
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public R<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return defHandler("不支持当前请求方法", e);
    }

    /**
     * Bean 校验异常 Validate
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(message, e);
        return R.failed(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 方法参数校验异常 Validate
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleValidationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();
        log.error(message, e);
        return R.failed(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * FileException 自定义文件异常
     * 返回状态码:500
     */
    @ExceptionHandler(FileException.class)
    public R<?> handleFileException(FileException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * UnauthorizedException 无权限异常
     * 返回状态码:403
     */
    @ExceptionHandler(UnauthorizedException.class)
    public R<?> handleUnauthorizedException(UnauthorizedException e) {
        return R.failed(HttpStatus.FORBIDDEN, "您没有访问此接口的权限");
    }


    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        return defHandler("unknown.error", e);
    }

    /**
     * 统一返回
     */
    private R<?> defHandler(String msg, Exception e) {
        log.error(msg, e);
        return R.failed(msg);
    }
}
